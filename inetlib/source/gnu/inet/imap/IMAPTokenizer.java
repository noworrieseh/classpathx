/*
 * IMAPTokenizer.java
 * Copyright (C) 2013 The Free Software Foundation
 * 
 * This file is part of GNU Classpath Extensions (classpathx).
 * For more information please visit https://www.gnu.org/software/classpathx/
 *
 * classpathx is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * classpathx is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with classpathx.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package gnu.inet.imap;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * IMAP stream tokenizer.
 * @version 1.2
 * @since 1.2
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
class IMAPTokenizer
{

  static final int NIL =  1<<0;
  static final int ATOM = 1<<1;
  static final int NUMBER = 1<<2;
  static final int LITERAL = 1<<3;
  static final int QUOTED_STRING = 1<<4;
  static final int LPAREN = 1<<5;
  static final int RPAREN = 1<<6;
  static final int LBRACKET = 1<<7;
  static final int RBRACKET = 1<<8;
  static final int TAG = 1<<9;
  static final int UNTAGGED_RESPONSE = 1<<10;
  static final int CONTINUATION = 1<<11;
  static final int EOL = 1<<12;

  static final int STRING = QUOTED_STRING | LITERAL;
  static final int ASTRING = ATOM | STRING;
  static final int NSTRING = NIL | STRING;

  static class Token
  {
    final int type;
    final String value;
    final byte[] literal;

    Token(int type)
    {
      this.type = type;
      value = null;
      literal = null;
    }

    Token(int type, String value)
    {
      this.type = type;
      this.value = value;
      literal = null;
    }

    Token(int type, byte[] literal)
    {
      this.type = type;
      this.literal = literal;
      value = null;
    }

    public String toString() {
      StringBuilder buf = new StringBuilder();
      buf.append(TYPES.get(type));
      if (value != null)
        {
          buf.append(':');
          buf.append(value);
        }
      else if (literal != null)
        {
          try
            {
              String s = new String(literal, "US-ASCII");
              buf.append(':');
              buf.append(s);
            }
          catch (IOException e)
            {
            }
        }
      return buf.toString();
    }

    private static final Map<Integer,String> TYPES;
    static
    {
      TYPES = new HashMap();
      TYPES.put(NIL, "NIL");
      TYPES.put(ATOM, "ATOM");
      TYPES.put(NUMBER, "NUMBER");
      TYPES.put(LITERAL, "LITERAL");
      TYPES.put(QUOTED_STRING, "QUOTED_STRING");
      TYPES.put(LPAREN, "LPAREN");
      TYPES.put(RPAREN, "RPAREN");
      TYPES.put(LBRACKET, "LBRACKET");
      TYPES.put(RBRACKET, "RBRACKET");
      TYPES.put(TAG, "TAG");
      TYPES.put(UNTAGGED_RESPONSE, "UNTAGGED_RESPONSE");
      TYPES.put(CONTINUATION, "CONTINUATION");
      TYPES.put(EOL, "EOL");
    }

  }

  private static final int STATE_INIT = 0;
  private static final int STATE_RESPONSE_BODY = 1;
  private static final int STATE_EOL = 2;
  private static final int SP = 32;
  private static final int LF = 10;
  private static final int CR = 13;

  private int state;
  private int depth; // parenthesized list depth
  private InputStream in;
  private Logger logger;
  private ByteArrayOutputStream sink;
  private ByteArrayOutputStream trace;
  private boolean peeking;

  IMAPTokenizer(InputStream in, Logger logger)
  {
    this.in = in;
    this.logger = logger;
    sink = new ByteArrayOutputStream();
    if (logger.isLoggable(IMAPConnection.IMAP_TRACE))
      {
        trace = new ByteArrayOutputStream();
      }
  }

  Token next()
    throws IOException
  {
    if (state == STATE_EOL)
      {
        return new Token(EOL);
      }
    sink.reset();
    int c = readeof();
    if (c == CR)
      {
        in.mark(1);
        int d = readeof();
        if (d == LF)
          {
            state = STATE_EOL;
            return new Token(EOL);
          }
        in.reset();
      }
    else if (c == SP)
      {
        c = readeof(); // skip SP
      }
    switch (state)
      {
      case STATE_INIT:
        state = STATE_RESPONSE_BODY;
        if (c == '*')
          {
            if (readeof() != SP)
              {
                throw new IOException("err.bad_untagged");
              }
            return new Token(UNTAGGED_RESPONSE);
          }
        else if (c == '+')
          {
            if (readeof() != SP)
              {
                throw new IOException("err.bad_continuation");
              }
            return new Token(CONTINUATION);
          }
        return new Token(TAG, parseAtom(c));
      case STATE_RESPONSE_BODY:
        if (c == '(')
          {
            depth++;
            return new Token(LPAREN);
          }
        else if (c == '[')
          {
            depth++;
            return new Token(LBRACKET);
          }
        else if (c == ')')
          {
            depth--;
            return new Token(RPAREN);
          }
        else if (c == ']')
          {
            depth--;
            return new Token(RBRACKET);
          }
        else if (c >= 48 && c <= 57)
          {
            return parseNumber(c);
          }
        else if (c == '"')
          {
            return parseQuotedString();
          }
        else if (c == '{')
          {
            return parseLiteral();
          }
        else
          {
            String atom = parseAtom(c);
            if ("NIL".equals(atom))
              {
                return new Token(NIL);
              }
            return new Token(ATOM, atom);
          }
      default:
        throw new IllegalStateException();
      }
  }

  /**
   * We have reached EOL. Reset for the next line.
   */
  void reset()
  {
    state = STATE_INIT;
    if (trace != null)
      {
        byte[] buf = trace.toByteArray();
        // Sanitize for ASCII
        for (int i = 0; i < buf.length; i++)
          {
            byte c = buf[i];
            if (c < 32 || c > 126)
              {
                buf[i] = 63; // '?'
              }
          }
        try
          {
            logger.log(IMAPConnection.IMAP_TRACE,
                       "< " + new String(buf, "US-ASCII"));
          }
        catch (IOException e) // Won't happen
          {
          }
        trace.reset();
      }
  }

  Token peek()
    throws IOException
  {
    in.mark(1024);
    peeking = true;
    Token token = next();
    peeking = false;
    in.reset();
    return token;
  }

  String collectToEOL()
    throws IOException
  {
    if (state == STATE_EOL)
      {
        return null;
      }
    int c;
    do
      {
        c = readeof();
        if (c == CR)
          {
            in.mark(1);
            int d = readeof();
            if (d == LF)
              {
                state = STATE_EOL;
                return new String(sink.toByteArray(), "US-ASCII");
              }
            in.reset();
          }
        sink.write(c);
      }
    while (true);
  }

  private static final byte[] ATOM_SPECIALS = new byte[]
    {
      SP, '"', '%', '(', ')', '*', '\\', ']', '{'
    };

  private int readeof()
    throws IOException
  {
    int c = in.read();
    if (c == -1)
      {
        throw new EOFException();
      }
    if (!peeking && trace != null)
      {
        trace.write(c);
      }
    return c;
  }

  private String parseAtom(int c)
    throws IOException
  {
    boolean isFlag = false;
    if (c < SP || Arrays.binarySearch(ATOM_SPECIALS, (byte) c) >= 0)
      {
        if (c == '\\') // NB we treat flags as atoms
          {
            isFlag = true;
          }
        else
          {
            throw new IOException("err.bad_atom c="+(char)c);
          }
      }
    do
      {
        sink.write(c);
        in.mark(1);
        c = readeof();
        if (c < SP || Arrays.binarySearch(ATOM_SPECIALS, (byte) c) >= 0)
          {
            if (isFlag && c == '*' && sink.size() == 1)
              {
                // flag-perm
                sink.write(c);
              }
            else if (c != SP)
              {
                in.reset();
              }
            return new String(sink.toByteArray(), "US-ASCII");
          }
      }
    while (true);
  }

  private Token parseNumber(int c)
    throws IOException
  {
    do
      {
        sink.write(c);
        in.mark(1);
        c = readeof();
        if (c < 48 || c > 57)
          {
            if (c != SP)
              {
                in.reset();
              }
            String val = new String(sink.toByteArray(), "US-ASCII");
            return new Token(NUMBER, val);
          }
      }
    while (true);
  }

  private Token parseQuotedString()
    throws IOException
  {
    boolean escaped = false;
    do
      {
        in.mark(1);
        int c = readeof();
        if (c == '\\')
          {
            if (escaped)
              {
                sink.write(c);
              }
            escaped = !escaped;
          }
        else if (c == '"')
          {
            if (escaped)
              {
                sink.write(c);
              }
            else
              {
                // consume a following SP
                in.mark(1);
                c = readeof();
                if (c != SP)
                  {
                    in.reset();
                  }
                return new Token(QUOTED_STRING, sink.toByteArray());
              }
          }
        else if (c == LF)
          {
            throw new IOException("err.bad_quoted_string");
          }
        else
          {
            sink.write(c);
          }
      }
    while (true);
  }

  private Token parseLiteral()
    throws IOException
  {
    int c;
    for (c = readeof(); c >= 48 && c <= 57; c = readeof())
      {
        sink.write(c);
      }
    if (c != '}' || readeof() != CR || readeof() != LF)
      {
        throw new IOException("err.bad_literal");
      }
    int total = Integer.parseInt(new String(sink.toByteArray(), "US-ASCII"));
    sink.reset();
    int count = 0;
    while (count < total)
      {
        sink.write(readeof());
      }
    byte[] literal = sink.toByteArray();
    sink.reset();
    return new Token(LITERAL, literal);
  }

}

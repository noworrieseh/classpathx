/*
 * HeaderTokenizer.java
 * Copyright (C) 2001 dog <dog@dog.net.uk>
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package javax.mail.internet;

/**
 * This class tokenizes RFC822 and MIME headers into the basic symbols 
 * specified by RFC822 and MIME.
 * <p>
 * This class handles folded headers (ie headers with embedded CRLF SPACE
 * sequences). The folds are removed in the returned tokens.
 */
public class HeaderTokenizer
{

  /**
   * The Token class represents tokens returned by the HeaderTokenizer.
   */
  public static class Token
  {

    /**
     * Token type indicating an ATOM.
     */
    public static final int ATOM = -1;

    /**
     * Token type indicating a quoted string.
     * The value field contains the string without the quotes.
     */
    public static final int QUOTEDSTRING = -2;

    /**
     * Token type indicating a comment.
     * The value field contains the comment string without the comment 
     * start and end symbols.
     */
    public static final int COMMENT = -3;

    /**
     * Token type indicating end of input.
     */
    public static final int EOF = -4;

    /*
     * The type of the token.
     */
    private int type;

    /*
     * The value of the token if it is of type ATOM, QUOTEDSTRING, or
     * COMMENT.
     */
    private String value;

    /**
     * Constructor.
     * @param type Token type
     * @param value Token value
     */
    public Token(int type, String value)
    {
      this.type = type;
      this.value = value;
    }
    
    /**
     * Return the type of the token.
     * If the token represents a delimiter or a control character,
     * the type is that character itself, converted to an integer.
     * Otherwise, it's value is one of the following:
     * <ul>
     * <li>ATOM A sequence of ASCII characters delimited by either 
     * SPACE, CTL, '(', '"' or the specified SPECIALS
     * <li>QUOTEDSTRING A sequence of ASCII characters within quotes
     * <li>COMMENT A sequence of ASCII characters within '(' and ')'.
     * <li>EOF End of header
     */
    public int getType()
    {
      return type;
    }

    /**
     * Returns the value of the token just read.
     * When the current token is a quoted string, this field contains 
     * the body of the string, without the quotes.
     * When the current token is a comment, this field contains the body
     * of the comment.
     */
    public String getValue()
    {
      return value;
    }

  }

  /**
   * RFC822 specials
   */
  public static final String RFC822 = "()<>@,;:\\\"\t .[]";

  /**
   * MIME specials
   */
  public static final String MIME = "()<>@,;:\\\"\t []/?=";
  
  /*
   * The EOF token.
   */
  private static final Token EOF = new Token(Token.EOF, null);

  /*
   * The header string to parse.
   */
  private String header;

  /*
   * The delimiters separating tokens.
   */
  private String delimiters;

  /*
   * Whather or not to skip comments.
   */
  private boolean skipComments;

  /*
   * The index of the character identified as current for the token()
   * call.
   */
  private int pos = 0;

  /*
   * The index of the character that will be considered current on a call to
   * next().
   */
  private int next = 0;

  /*
   * The index of the character that will be considered current on a call to
   * peek().
   */
  private int peek = 0;
  
  private int maxPos;

  /**
   * Constructor that takes a rfc822 style header.
   * @param header The rfc822 header to be tokenized
   * @param delimiters Set of delimiter characters to be used to delimit ATOMS.
   * These are usually RFC822 or MIME
   * @param skipComments If true, comments are skipped and not returned 
   * as tokens
   */
  public HeaderTokenizer(String header, String delimiters,
      boolean skipComments)
  {
    this.header = (header==null) ? "" : header;
    this.delimiters = delimiters;
    this.skipComments = skipComments;
    pos = next = peek = 0;
    maxPos = header.length();
  }

  /**
   * Constructor.
   * Comments are ignored and not returned as tokens
   * @param header The header that is tokenized
   * @param delimiters The delimiters to be used
   */
  public HeaderTokenizer(String header, String delimiters)
  {
    this(header, delimiters, true);
  }

  /**
   * Constructor.
   * The RFC822 defined delimiters - RFC822 - are used to delimit ATOMS.
   * Also comments are skipped and not returned as tokens
   */
  public HeaderTokenizer(String header)
  {
    this(header, RFC822, true);
  }

  /**
   * Parses the next token from this String.
   * <p>
   * Clients sit in a loop calling <code>next()</code> to parse successive 
   * tokens until an EOF Token is returned.
   * @return the next token
   * @exception ParseException if the parse fails
   */
  public Token next()
    throws ParseException
  {
    pos = next;
    Token token = token();
    next = pos;
    peek = next;
    return token;
  }

  /**
   * Peek at the next token, without actually removing the token 
   * from the parse stream.
   * Invoking this method multiple times will return successive tokens,
   * until <code>next()</code> is called.
   * @return the next peek token
   * @param ParseException if the parse fails
   */
  public Token peek()
    throws ParseException
  {
    pos = peek;
    Token token = token();
    peek = pos;
    return token;
  }

  /**
   * Return the rest of the header.
   */
  public String getRemainder()
  {
    return header.substring(next);
  }

  /*
   * Returns the next token.
   */
  private Token token()
    throws ParseException
  {
    if (pos>=maxPos)
      return EOF;
    if (skipWhitespace()==Token.EOF)
      return EOF;
    
    boolean needsFilter = false;
    char c;
    
    // comment
    for (c = header.charAt(pos); c=='('; c = header.charAt(pos))
    {
      int start = ++pos;
      int parenCount = 1;
      while (parenCount>0 && pos<maxPos)
      {
        c = header.charAt(pos);
        if (c == '\\')
        {
          pos++;
          needsFilter = true;
        }
        else if (c=='\r')
          needsFilter = true;
        else if (c=='(')
          parenCount++;
        else if (c==')')
          parenCount--;
        pos++;
      }

      if (parenCount!=0)
        throw new ParseException("Illegal comment");
      
      if (!skipComments)
      {
        if (needsFilter)
          return new Token(Token.COMMENT, filter(header, start, pos-1));
        else
          return new Token(Token.COMMENT, header.substring(start, pos-1));
      }
      
      if (skipWhitespace()==Token.EOF)
        return EOF;
    }

    // quotedstring
    if (c=='"')
    {
      int start = ++pos;
      while (pos<maxPos)
      {
        c = header.charAt(pos);
        if (c=='\\')
        {
          pos++;
          needsFilter = true;
        }
        else if (c=='\r')
          needsFilter = true;
        else if (c=='"')
        {
          pos++;
          if (needsFilter)
            return new Token(Token.QUOTEDSTRING, 
                filter(header, start, pos-1));
          else
            return new Token(Token.QUOTEDSTRING, 
                header.substring(start, pos-1));
        }
        pos++;
      }
      throw new ParseException("Illegal quoted string");
    }

    // delimiter
    if (c<' ' || c>='\177' || delimiters.indexOf(c)>=0)
    {
      pos++;
      char[] chars = new char[1];
      chars[0] = c;
      return new Token(c, new String(chars));
    }
    
    // atom
    int start = pos;
    while (pos<maxPos)
    {
      c = header.charAt(pos);
      if (c<' ' || c>='\177' || c=='(' || c==' ' || c=='"' || 
          delimiters.indexOf(c)>=0)
        break;
      pos++;
    }
    return new Token(Token.ATOM, header.substring(start, pos));
  }

  /*
   * Advance pos over any whitespace delimiters.
   */
  private int skipWhitespace()
  {
    while (pos<maxPos)
    {
      char c = header.charAt(pos);
      if (c!=' ' && c!='\t' && c!='\r' && c!='\n')
        return pos;
      pos++;
    }
    return Token.EOF;
  }

  /*
   * Process out CR and backslash (line continuation) bytes.
   */
  private String filter(String s, int start, int end)
  {
    StringBuffer buffer = new StringBuffer();
    boolean backslash = false;
    boolean cr = false;
    for (int i = start; i<end; i++)
    {
      char c = s.charAt(i);
      if (c=='\n' && cr)
        cr = false;
      else
      {
        cr = false;
        if (!backslash)
        {
          if (c=='\\')
            backslash = true;
          else if (c=='\r')
            cr = true;
          else
            buffer.append(c);
        }
        else
        {
          buffer.append(c);
          backslash = false;
        }
      }
    }
    return buffer.toString();
  }

}

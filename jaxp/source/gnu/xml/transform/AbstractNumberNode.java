/*
 * AbstractNumberNode.java
 * Copyright (C) 2004 The Free Software Foundation
 * 
 * This file is part of GNU JAXP, a library.
 *
 * GNU JAXP is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * GNU JAXP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Linking this library statically or dynamically with other modules is
 * making a combined work based on this library.  Thus, the terms and
 * conditions of the GNU General Public License cover the whole
 * combination.
 *
 * As a special exception, the copyright holders of this library give you
 * permission to link this library with independent modules to produce an
 * executable, regardless of the license terms of these independent
 * modules, and to copy and distribute the resulting executable under
 * terms of your choice, provided that you also meet, for each linked
 * independent module, the terms and conditions of the license of that
 * module.  An independent module is a module which is not derived from
 * or based on this library.  If you modify this library, you may extend
 * this exception to your version of the library, but you are not
 * obliged to do so.  If you do not wish to do so, delete this
 * exception statement from your version. 
 */

package gnu.xml.transform;

import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import gnu.xml.xpath.Expr;

/**
 * A template node representing the XSL <code>number</code> instruction.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
abstract class AbstractNumberNode
  extends TemplateNode
{

  static final int ALPHABETIC = 0;
  static final int TRADITIONAL = 1;

  final String format;
  final String lang;
  final int letterValue;
  final String groupingSeparator;
  final int groupingSize;

  AbstractNumberNode(TemplateNode children, TemplateNode next,
                     String format, String lang,
                     int letterValue, String groupingSeparator,
                     int groupingSize)
  {
    super(children, next);
    this.format = format;
    this.lang = lang;
    this.letterValue = letterValue;
    this.groupingSeparator = groupingSeparator;
    this.groupingSize = groupingSize;
  }

  void doApply(Stylesheet stylesheet, String mode,
               Node context, int pos, int len,
               Node parent, Node nextSibling)
    throws TransformerException
  {
    Document doc = (parent instanceof Document) ? (Document) parent :
      parent.getOwnerDocument();
    String value = format(compute(stylesheet, context));
    Text text = doc.createTextNode(value);
    if (nextSibling != null)
      {
        parent.insertBefore(text, nextSibling);
      }
    else
      {
        parent.appendChild(text);
      }
    // xsl:number doesn't process children
    if (next != null)
      {
        next.apply(stylesheet, mode, 
                   context, pos, len,
                   parent, nextSibling);
      }
  }

  String format(int[] number)
  {
    int start = 0, end = 0, len = format.length(); // region of format
    int pos = 0; // number index
    StringBuffer buf = new StringBuffer();
    
    while (pos < number.length)
      {
        while (end < len && !isAlphanumeric(format.charAt(end)))
          {
            end++;
          }
        if (end > start)
          {
            buf.append(format.substring(start, end));
          }
        else
          {
            buf.append('.');
          }
        start = end;
        while (end < len && isAlphanumeric(format.charAt(end)))
          {
            end++;
          }
        if (end > start)
          {
            format(buf, number[pos], format.substring(start, end));
          }
        else
          {
            format(buf, number[pos], "1");
          }
        pos++;
      }
    return buf.toString();
  }

  void format(StringBuffer buf, int number, String formatToken)
  {
    int len = formatToken.length();
    char c = formatToken.charAt(len - 1);
    if (Character.digit(c, 10) == 1)
      {
        // Check preceding characters
        for (int i = len - 2; i >= 0; i--)
          {
            if (formatToken.charAt(i) != (c - 1))
              {
                format(buf, number, "1");
              }
          }
        // Decimal representation
        String val = Integer.toString(number);
        for (int d = len - val.length(); d > 0; d--)
          {
            buf.append('0');
          }
        buf.append(val);
      }
    else if ("A".equals(formatToken))
      {
        buf.append(alphabetic('@', number));
      }
    else if ("a".equals(formatToken))
      {
        buf.append(alphabetic('`', number));
      }
    else if ("i".equals(formatToken))
      {
        buf.append(roman(false, number));
      }
    else if ("I".equals(formatToken))
      {
        buf.append(roman(true, number));
      }
    else
      {
        // Unknown numbering sequence
        format(buf, number, "1");
      }
  }

  static final boolean isAlphanumeric(char c)
  {
    switch (Character.getType(c))
      {
      case Character.DECIMAL_DIGIT_NUMBER: // Nd
      case Character.LETTER_NUMBER: // Nl
      case Character.OTHER_NUMBER: // No
      case Character.UPPERCASE_LETTER: // Lu
      case Character.LOWERCASE_LETTER: // Ll
      case Character.TITLECASE_LETTER: // Lt
      case Character.MODIFIER_LETTER: // Lm
      case Character.OTHER_LETTER: // Lo
        return true;
      default:
        return false;
      }
  }

  static final String alphabetic(char offset, int number)
  {
    StringBuffer buf = new StringBuffer();
    while (number > 0)
      {
        int r = number % 26;
        number = number / 26;
        buf.insert(0, (char) offset + r);
      }
    return buf.toString();
  }

  static final int[] roman_numbers = {1, 5, 10, 50, 100, 500, 1000};
  static final char[] roman_chars = {'i', 'v', 'x', 'l', 'c', 'd', 'm'};

  static final String roman(boolean upper, int number)
  {
    StringBuffer buf = new StringBuffer();
    for (int pos = roman_numbers.length - 1; pos >= 0; pos -= 2)
      {
        int f = number / roman_numbers[pos];
        if (f != 0)
          {
            number = number % (f * roman_numbers[pos]);
          }
        if (f > 4 && f < 9)
          {
            buf.append(roman_chars[pos + 1]);
            f -= 5;
          }
        if (f == 4)
          {
            buf.append(roman_chars[pos]);
            buf.append(roman_chars[pos + 1]);
          }
        else if (f == 9)
          {
            buf.append(roman_chars[pos]);
            buf.append(roman_chars[pos + 2]);
          }
        else
          {
            for (; f > 0; f--)
              {
                buf.append(roman_chars[pos]);
              }
          }
      }
    return upper ? buf.toString().toUpperCase() : buf.toString();
  }
  
  abstract int[] compute(Stylesheet stylesheet, Node context)
    throws TransformerException;

  public String toString()
  {
    StringBuffer buf = new StringBuffer(getClass().getName());
    buf.append('[');
    buf.append("format=");
    buf.append(format);
    buf.append(']');
    return buf.toString();
  }

}

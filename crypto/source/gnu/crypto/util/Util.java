package gnu.crypto.util;

// ----------------------------------------------------------------------------
// $Id: Util.java,v 1.4 2002-06-24 11:56:33 raif Exp $
//
// Copyright (C) 2001-2002, Free Software Foundation, Inc.
//
// This program is free software; you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the Free
// Software Foundation; either version 2 of the License or (at your option) any
// later version.
//
// This program is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
// more details.
//
// You should have received a copy of the GNU General Public License along with
// this program; see the file COPYING.  If not, write to the
//
//    Free Software Foundation Inc.,
//    59 Temple Place - Suite 330,
//    Boston, MA 02111-1307
//    USA
//
// As a special exception, if you link this library with other files to produce
// an executable, this library does not by itself cause the resulting
// executable to be covered by the GNU General Public License.  This exception
// does not however invalidate any other reasons why the executable file might
// be covered by the GNU General Public License.
// ----------------------------------------------------------------------------

/**
 * <p>A collection of utility methods used throughout this project.</p>
 *
 * @version $Revision: 1.4 $
 */
public class Util {

   // Constants and variables
   // -------------------------------------------------------------------------

   private static final char[] HEX_DIGITS = {
      '0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'
   };

   // Constructor(s)
   // -------------------------------------------------------------------------

   /** Trivial constructor to enforce Singleton pattern. */
   private Util() {
      super();
   }

   // Class methods
   // -------------------------------------------------------------------------

   /**
    * <p>Returns <code>true</code> if the two designated byte arrays are
    * (a) non-null, (b) of the same length, and (c) contain the same values.</p>
    *
    * @param a the first byte array.
    * @param b the second byte array.
    * @return <code>true</code> if the two designated arrays contain the same
    * values. Returns <code>false</code> otherwise.
    */
   public static boolean areEqual(byte[] a, byte[] b) {
      if (a == null || b == null) {
         return false;
      }
      int aLength = a.length;
      if (aLength != b.length) {
         return false;
      }
      for (int i = 0; i < aLength; i++) {
         if (a[i] != b[i]) {
            return false;
         }
      }
      return true;
   }

   /**
    * <p>Returns a string of hexadecimal digits from a byte array. Each byte is
    * converted to 2 hex symbols; zero(es) included.</p>
    *
    * <p>This method calls the method with same name and three arguments as:</p>
    *
    * <pre>
    *    toString(ba, 0, ba.length);
    * </pre>
    *
    * @param ba the byte array to convert.
    * @return a string of hexadecimal characters (two for each byte)
    * representing the designated input byte array.
    */
   public static String toString(byte[] ba) {
      return toString(ba, 0, ba.length);
   }

   /**
    * <p>Returns a string of hexadecimal digits from a byte array, starting at
    * <code>offset</code> and consisting of <code>length</code> bytes. Each byte
    * is converted to 2 hex symbols; zero(es) included.
    *
    * @param ba the byte array to convert.
    * @param offset the index from which to start considering the bytes to
    * convert.
    * @param length the count of bytes, starting from the designated offset to
    * convert.
    * @return a string of hexadecimal characters (two for each byte)
    * representing the designated input byte sub-array.
    */
   public static final String toString(byte[] ba, int offset, int length) {
      char[] buf = new char[length * 2];
      for (int i = 0, j = 0, k; i < length; ) {
         k = ba[offset + i++];
         buf[j++] = HEX_DIGITS[(k >>> 4) & 0x0F];
         buf[j++] = HEX_DIGITS[ k        & 0x0F];
      }
      return new String(buf);
   }

   /**
    * <p>Returns a byte array from a string of hexadecimal digits.</p>
    *
    * @param s a string of hexadecimal ASCII characters
    * @return the decoded byte array from the input hexadecimal string.
    */
   public static byte[] toBytesFromString(String s) {
      int limit = s.length();
      byte[] result = new byte[((limit + 1) / 2)];
      int i = 0, j = 0;
      if ((limit % 2) == 1) {
         result[j++] = (byte) fromDigit(s.charAt(i++));
      }
      while (i < limit) {
         result[j++] = (byte)(
               (fromDigit(s.charAt(i++)) << 4) | fromDigit(s.charAt(i++)));
      }
      return result;
   }

   /**
    * <p>Returns a number from <code>0</code> to <code>15</code> corresponding
    * to the designated hexadecimal digit.</p>
    *
    * @param c a hexadecimal ASCII symbol.
    */
   public static int fromDigit(char c) {
      if (c >= '0' && c <= '9') {
         return c - '0';
      } else if (c >= 'A' && c <= 'F') {
         return c - 'A' + 10;
      } else if (c >= 'a' && c <= 'f') {
         return c - 'a' + 10;
      } else
         throw new IllegalArgumentException("Invalid hexadecimal digit: " + c);
   }

   /**
    * <p>Returns a string of 8 hexadecimal digits (most significant digit first)
    * corresponding to the unsigned integer <code>n</code>.</p>
    *
    * @param n the unsigned integer to convert.
    * @return a hexadecimal string 8-character long.
    */
   public static String toString(int n) {
      char[] buf = new char[8];
      for (int i = 7; i >= 0; i--) {
         buf[i] = HEX_DIGITS[n & 0x0F];
         n >>>= 4;
      }
      return new String(buf);
   }

   /**
    * <p>Returns a string of hexadecimal digits from an integer array. Each int
    * is converted to 4 hex symbols.</p>
    */
   public static String toString(int[] ia) {
      int length = ia.length;
      char[] buf = new char[length * 8];
      for (int i = 0, j = 0, k; i < length; i++) {
         k = ia[i];
         buf[j++] = HEX_DIGITS[(k >>> 28) & 0x0F];
         buf[j++] = HEX_DIGITS[(k >>> 24) & 0x0F];
         buf[j++] = HEX_DIGITS[(k >>> 20) & 0x0F];
         buf[j++] = HEX_DIGITS[(k >>> 16) & 0x0F];
         buf[j++] = HEX_DIGITS[(k >>> 12) & 0x0F];
         buf[j++] = HEX_DIGITS[(k >>>  8) & 0x0F];
         buf[j++] = HEX_DIGITS[(k >>>  4) & 0x0F];
         buf[j++] = HEX_DIGITS[ k         & 0x0F];
      }
      return new String(buf);
   }

   /**
    * <p>Returns a string of 16 hexadecimal digits (most significant digit
    * first) corresponding to the unsigned long <code>n</code>.</p>
    *
    * @param n the unsigned long to convert.
    * @return a hexadecimal string 16-character long.
    */
   public static String toString(long n) {
      char[] b = new char[16];
      for (int i = 15; i >= 0; i--) {
         b[i] = HEX_DIGITS[(int)(n & 0x0FL)];
         n >>>= 4;
      }
      return new String(b);
   }

   /**
    * <p>Similar to the <code>toString()</code> method except that the Unicode
    * escape character is inserted before every pair of bytes. Useful to
    * externalise byte arrays that will be constructed later from such strings;
    * eg. s-box values.</p>
    *
    * @exception ArrayIndexOutOfBoundsException if the length is odd.
    */
   public static String toUnicodeString(byte[] ba) {
      return toUnicodeString(ba, 0, ba.length);
   }

   /**
    * <p>Similar to the <code>toString()</code> method except that the Unicode
    * escape character is inserted before every pair of bytes. Useful to
    * externalise byte arrays that will be constructed later from such strings;
    * eg. s-box values.</p>
    *
    * @exception ArrayIndexOutOfBoundsException if the length is odd.
    */
   public static final String
   toUnicodeString(byte[] ba, int offset, int length) {
      StringBuffer sb = new StringBuffer();
      int i = 0;
      int j = 0;
      int k;
      sb.append('\n').append("\"");
      while (i < length) {
         sb.append("\\u");

         k = ba[offset + i++];
         sb.append(HEX_DIGITS[(k >>> 4) & 0x0F]);
         sb.append(HEX_DIGITS[ k        & 0x0F]);

         k = ba[offset + i++];
         sb.append(HEX_DIGITS[(k >>> 4) & 0x0F]);
         sb.append(HEX_DIGITS[ k        & 0x0F]);

         if ((++j % 8) == 0) {
            sb.append("\"+").append('\n').append("\"");
         }
      }
      sb.append("\"").append('\n');
      return sb.toString();
   }

   /**
    * <p>Similar to the <code>toString()</code> method except that the Unicode
    * escape character is inserted before every pair of bytes. Useful to
    * externalise integer arrays that will be constructed later from such
    * strings; eg. s-box values.</p>
    *
    * @exception ArrayIndexOutOfBoundsException if the length is not a multiple
    * of 4.
    */
   public static String toUnicodeString(int[] ia) {
      StringBuffer sb = new StringBuffer();
      int i = 0;
      int j = 0;
      int k;
      sb.append('\n').append("\"");
      while (i < ia.length) {
         k = ia[i++];
         sb.append("\\u");
         sb.append(HEX_DIGITS[(k >>> 28) & 0x0F]);
         sb.append(HEX_DIGITS[(k >>> 24) & 0x0F]);
         sb.append(HEX_DIGITS[(k >>> 20) & 0x0F]);
         sb.append(HEX_DIGITS[(k >>> 16) & 0x0F]);
         sb.append("\\u");
         sb.append(HEX_DIGITS[(k >>> 12) & 0x0F]);
         sb.append(HEX_DIGITS[(k >>>  8) & 0x0F]);
         sb.append(HEX_DIGITS[(k >>>  4) & 0x0F]);
         sb.append(HEX_DIGITS[ k         & 0x0F]);

         if ((++j % 4) == 0) {
            sb.append("\"+").append('\n').append("\"");
         }
      }
      sb.append("\"").append('\n');
      return sb.toString();
   }

   public static byte[] toBytesFromUnicode(String s) {
      int limit = s.length() * 2;
      byte[] result = new byte[limit];
      char c;
      for (int i = 0; i < limit; i++) {
         c = s.charAt(i >>> 1);
         result[i] = (byte)(((i & 1) == 0) ? c >>> 8 : c);
      }

      return result;
   }

   // Instance methods
   // -------------------------------------------------------------------------
}

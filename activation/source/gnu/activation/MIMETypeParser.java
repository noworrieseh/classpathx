/*
  GNU-Classpath Extensions: java bean activation framework
  Copyright (C) 2000 2001  Andrew Selkirk

  For more information on the classpathx please mail:
  nferrier@tapsellferrier.co.uk

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
*/
package gnu.activation;

// Imports
import javax.activation.MimeType;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.Hashtable;

/**
 * MIME Type Parser
 * @author Andrew Selkirk
 * @author Nic Ferrier
 * @version $Revision: 1.1 $
 */
public class MIMETypeParser {

  private static final int STARTLINE = 0;
  private static final int READTYPE = 1;
  private static final int READEXT = 2;

  /**
   * Parse MIME type formatted data stream
   * @param reader Reader
   * @return Mapping of extension to MIMEType object
   */
  public static Hashtable parseStream(Reader reader) {

    // Variables
    Hashtable       registry;
    int             state;    // State Register
    MimeType        mimetype;
    StreamTokenizer tokens;
    StringBuffer    mimeTypeBuffer;
    StringBuffer    extBuffer;

    // Initialize
    registry = new Hashtable();
    state = READTYPE;
    mimetype = null;
    mimeTypeBuffer = new StringBuffer();
    extBuffer = new StringBuffer();

    // Initialize Tokenizer
    tokens = new StreamTokenizer(reader);
    tokens.commentChar('#');
    tokens.eolIsSignificant(true);
    tokens.wordChars('/', '/');
    tokens.wordChars('-', '-');

    try
    {
      while (true)
      {
        switch (tokens.nextToken())
        {
        case StreamTokenizer.TT_EOF:
          return registry;
        case StreamTokenizer.TT_EOL:
          switch (state)
          {
          case READEXT:
            //set the state
            state = READTYPE;
            continue;
          default:
            continue;
          }
        case StreamTokenizer.TT_WORD:
          switch(state)
          {
            case READTYPE:
              //type has been read - create the object
              mimetype = new MimeType(tokens.sval);
              state = READEXT;
              continue;
            case READEXT:
              //the extension has been read - store the
              // mimetype against it
              registry.put(tokens.sval, mimetype);
              continue;
            default:
              continue;
          } // switch
        } // switch
      } // while
    }
    catch (Exception e) {
      //this is only here for debugging
      e.printStackTrace();
    } // try

    // Return registry
    return registry;

  } // parseStream()


} // MIMETypeParser

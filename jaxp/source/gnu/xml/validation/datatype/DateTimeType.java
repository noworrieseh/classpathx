package gnu.xml.validation.datatype;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

/**
 * The XML Schema dateTime type.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class DateTimeType
  extends AtomicSimpleType
{

  static final int[] CONSTRAINING_FACETS = {
    Facet.PATTERN,
    Facet.ENUMERATION,
    Facet.WHITESPACE,
    Facet.MAX_INCLUSIVE,
    Facet.MAX_EXCLUSIVE,
    Facet.MIN_INCLUSIVE,
    Facet.MIN_EXCLUSIVE
  };

  DateTimeType()
  {
    super(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "dateTime"),
          Type.ANY_SIMPLE_TYPE);
  }

  public int[] getConstrainingFacets()
  {
    return CONSTRAINING_FACETS;
  }

  public boolean matches(String value)
  {
    int len = value.length();
    int state = 0;
    int start = 0;
    for (int i = 0; i < len; i++)
      {
        char c = value.charAt(i);
        if (c == '-' && i == 0)
          {
            start++;
            continue;
          }
        if (c >= 0x30 && c <= 0x39)
          {
            continue;
          }
        switch (state)
          {
          case 0: // year
            if (c == '-')
              {
                String year = value.substring(start, i);
                if ("0000".equals(year) || year.length() < 4)
                  {
                    return false;
                  }
                state = 1;
                start = i + 1;
                continue;
              }
            break;
          case 1: // month
            if (c == '-')
              {
                if (i - start != 2)
                  {
                    return false;
                  }
                state = 2;
                start = i + 1;
                continue;
              }
            break;
          case 2: // day
            if (c == 'T')
              {
                if (i - start != 2)
                  {
                    return false;
                  }
                state = 3;
                start = i + 1;
                continue;
              }
            break;
          case 3: // hour
            if (c == ':')
              {
                if (i - start != 2)
                  {
                    return false;
                  }
                state = 4;
                start = i + 1;
                continue;
              }
            break;
          case 4: // minute
            if (c == ':')
              {
                if (i - start != 2)
                  {
                    return false;
                  }
                state = 5;
                start = i + 1;
                continue;
              }
            break;
          case 5: // second
            if (c == '.')
              {
                if (i - start != 2)
                  {
                    return false;
                  }
                state = 6;
                start = i + 1;
                continue;
              }
            else if (c == ' ')
              {
                if (i - start != 2)
                  {
                    return false;
                  }
                state = 7;
                start = i + 1;
                continue;
              }
            break;
          case 6: // second fraction
            if (c == ' ')
              {
                state = 7;
                start = i + 1;
                continue;
              }
            break;
          case 7: // timezone 1
            if (start == i)
              {
                if (c == '+' || c == '-')
                  {
                    continue;
                  }
                else if (c == 'Z')
                  {
                    state = 9;
                    start = i + 1;
                    continue;
                  }
              }
            if (c == ':')
              {
                if (i - start != 2)
                  {
                    return false;
                  }
                state = 8;
                start = i + 1;
                continue;
              }
            break;
          }
        return false;
      }
    switch (state)
      {
      case 5: // second
        if (len - start != 2)
          {
            return false;
          }
        break;
      case 6: // second fraction
        break;
      case 8: // timezone 2
        if (len - start != 2)
          {
            return false;
          }
        break;
      case 9: // post Z
        break;
      default:
        return false;
      }
    return true;
  }
  
}


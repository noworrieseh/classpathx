package gnu.xml.validation.xmlschema;

/**
 * Container for element content.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
class Particle
{

  final Integer minOccurs;
  final Integer maxOccurs;

  final Object term;

  Particle(Integer minOccurs, Integer maxOccurs, Object term)
  {
    this.minOccurs = minOccurs;
    this.maxOccurs = maxOccurs;
    this.term = term;
  }
  
}


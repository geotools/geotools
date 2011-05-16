Converter
---------

The **Converter** API is a way to convert a value of one type into a value of another type. The difference is that we supply a plugins so you can teach the system how to perform additional conversions over time.

* The most common case of this is to parse a string into another type of value.

The Converter interface looks like this::
  
  public interface Converter {
      Object convert( Object source, Class target ) throws Exception;
  }

When you implement this you can either:

* Handle it correctly (returning an object of the target class
* Return null (if you could not handle it correctly)
* Throw an exeption (if you could not handle it correctly)

Converter for Enum
^^^^^^^^^^^^^^^^^^

Lets look at a real world example coming to us from the land of Java 5. Java 5 features a new type of construct called Enum. This is not something GeoTools can usually deal with as a Java 1.4 project - but by supplying a converter you can teach GeoTools to get along with your Java 5 application.

Example enum::
  
  public enum Choice {
     THIS,
     THAT;
  }

Example enum converter::
  
  class Choice2TextConverter {
     Object convert( Object source, Class target ){
          if( target != Choice.class ) return null;
          return Choice.valueOf( (String) source ) );
     }
 }

In general the freedom to return null or throw an exception lets you program converters very quickly.

A common case that we see in many places in GeoTools is parsing a string into the type defined by an AttributeType.

GeoTools 2.3 code (before converters were around)::
  
  FeatureType featureType = .... ; 
  AttributeType intType = featureType.getAttributeType( "intProperty" );
  String string = "1234";
  
  Integer integer = (Integer) intType.parse( string );

Using the Converters utility class this becomes::
  
  FeatureType featureType = .... ; 
  AttributeType intType = featureType.getAttributeType( "intProperty" );
  String string = "1234";
  
  Integer integer = Converters.convert( string, intType.getType() );

The Converters are the technology behind our great support for the Filter API Expression::
  
   Expression expr = ff.literal("#FF0000")
   Color color = expr.evaualte( null, Color.class );
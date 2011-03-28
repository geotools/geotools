Unit
----

GeoTools makes use of a formal Unit library in order to safely work with values, and minimise accidentally confusing distance with angle (for example).

GeoTools has patiently waited for the Java Community Process to agree on adding a Unit library into Java and has sat through. These efforts have stalled out repeatedly, but have produced working code - in part thanks to the excellent **JScience** project.

References:

* http://jscience.org/ 
* JSR-108 Withdrawn 2004 (http://jcp.org/en/jsr/detail?id=108)
* JSR-275 Rejected March 2010 (http://jcp.org/en/jsr/detail?id=275)

Here is a quick example using Système international d'unités (SI) commonly known as metric.
  
  // let us work in km - we will adjust the base unit METER
  Unit<Length> km = SI.KILO(SI.METER);
  
  Quantity<Length> shortJog = Measure.valueOf( 5, km );

We can also set out a distance in NonSI units::
  
  Quantity<Length> afternoonWalk = Measure.valueOf( 6, NonSI.MILE );

And safely grab values out (using a common unit)::
  
  double jog = shortJog.doubleValue( SI.METER );
  double walk = afternoonWalk.doubleValue( SI.METER );

Behind the scenes it obviously performs a conversion; we can do that ourself::

  UnitConverter mileToKm = NonSI.MILE.getConverterTo( SI.KILO(SI.METER) );
  double kms = mileToKm.convert( miles );

The library is powerful and supports currency and compound units.
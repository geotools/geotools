Unit
----

GeoTools makes use of a formal Unit library in order to safely work with values, and minimize accidentally confusing distance with angle (for example).

GeoTools has patiently waited for the Java Community Process to agree on adding a Unit library into Java and has sat through. These efforts have stalled out repeatedly, but have produced working code - in part thanks to the excellent ``JScience`` project.

References:

* http://jscience.org/ 
* JSR-108 Withdrawn 2004 (http://jcp.org/en/jsr/detail?id=108)
* JSR-275 Rejected March 2010 (http://jcp.org/en/jsr/detail?id=275)

Here is a quick example using "Système International d'Unités" (SI) commonly known as metric.::
  
   // let us work in km - we will adjust the base unit METRE
   Unit<Length> km = MetricPrefix.KILO(SI.METRE);

   Quantity<Length> shortJog = Quantities.getQuantity( 5, km );

We can also set out a distance in ``NonSI`` units::
  
   Quantity<Length> afternoonWalk = Quantities.getQuantity( 6, USCustomary.MILE );

And safely grab values out (using a common unit)::
  
    double jog = shortJog.to( SI.METRE ).getValue().doubleValue();
    double walk = afternoonWalk.to( SI.METRE ).getValue().doubleValue();

Behind the scenes it obviously performs a conversion; we can do that ourselves::

    double miles = 10;
    UnitConverter mileToKm = USCustomary.MILE.getConverterTo( MetricPrefix.KILO(SI.METRE) );
    double kms = mileToKm.convert( miles );

The library is powerful and supports currency and compound units.

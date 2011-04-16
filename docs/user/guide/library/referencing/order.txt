Axis Order
----------

Computer scientists and geographers disagree about one of the basic tenants of geography. Rather then decide who is right, as a library GeoTools gives your application a chance to match your users communities expectations.

**Eeek**

Chances are you have been referred to this page from the GeoTools user list. There is a lot of text on this page - and you are busy.

Add the following line to your application main method::

   // Setting the system-wide default at startup time
   System.setProperty("org.geotools.referencing.forceXY", "true");

Now that your program is working you can read the rest of this page.

History
^^^^^^^

When the OGC defined the WMS specification, they were very much focused on making things simple for web developers. So they basically decided that all images would appear "correctly" as far as appearance on the screen was concerned. Computer guys understand that to be x first, and then y (the same assumption is used by the JTS geometries in Geotools). The geographically empowered understand this to be easting and then northing.

This was a great idea in the short term, WMS is a great success. However this was not a good idea as we moved over to working with data. How should things appear when we are looking at Antarctica? Which way is north? More specifically we have problem with the most common data latitude and longitude. The screen order would be (east, north) axis order, but the geographers would expect it the other way around.

The reasons this matters is that geographers define the Coordinate Reference Systems we use to understand what is going on. The most common "authority" on Coordinate Reference Systems is the European Petroleum Survey Group (EPSG) who defines a database every year or so mapping a magic "code" to a definition of a CRS.

Here are some EPSG example codes and what they mean:

* 4326 - world coordinates
* 3006 - BC Albert's equal area projection

The problem
^^^^^^^^^^^

In the EPSG database, 4326 maps to a geographic CRS with (latitude, longitude) axis order. However, most software in the field understand EPSG:4326 as a geographic CRS with (longitude, latitude) axis order, because legacy OGC specifications were designed that way. The problem is not limited to EPSG:4326; it applies to most geographic CRS defined in the EPSG database. The axis order understood by currently existing software is in violation with what the EPSG database said.

Prior to the June 2005 meetings, there was considerable email dialogue between OGC members regarding axis order and coordinate reference systems. After some discussion, the members in attendance agreed that going forward, for new specifications, coordinate values shall be listed in the axis order specified by the referenced coordinate reference system (CRS). However no clarification was ever gained on what the situation with existing specifications was. The reality-on-the-ground is that every WFS 1.0 implementation tested that advertised EPSG:4326 as its CRS returned data on (longitude, latitude) order.

Starting in WMS 1.1, the specification actually explicitly states that the EPSG axis order is to be used, while making a special case exception for EPSG:4326, which was to be in (longitude, latitude) order. Oddly, they did not make exceptions for all geographic coordinate systems (like, say EPSG:4269) just for EPSG:4326. Finally in WMS 1.3, that exception was dropped as well, to consternation all around. From WMS 1.3.0 / ISO 19128:2005, published by ISO on 2005-11-23:

* EXAMPLE: EPSG:4326 refers to WGS 84 geographic latitude, then longitude. That is, in this CRS the x axis corresponds to latitude, and the y axis to longitude.

The GeoTools referencing module can not guess by itself when to returns a coordinate reference system (CRS) exactly as specified in the EPSG database, and when to returns a CRS with axis order forced to (longitude, latitude) no matter what the database said. The decision is left to the users or to the DataStore implementers. The choice may changes among different versions of the same standard.

Because GeoTools is designed for handling data from different sources, users need to choose between "strict EPSG axis order" and "modified axis order" on a case-by-case basis. A consequence is that a running Java Virtual Machine may contains two different instances of a CRS, one with (latitude, longitude) axis order and the other one with (longitude, latitude) axis order, and both claiming to be EPSG:4326. Experience has show that the coexistence of such conflicting CRS in the same application can lead to some error like StackOverflowError in non-cautious code.

GeoTools Solution
^^^^^^^^^^^^^^^^^

This problem do not requires any change in opengis interfaces or with usage of CoordinateReferenceSystem or CoordinateOperationFactory classes in client code. The problem can be seen as strictly limited to using the right instance of CRSAuthorityFactory by DataStores. For the GeoTools referencing module however, it has the following implications:

* GeoTools must be robust to the coexistence of different CRS with the same authority code (e.g. EPSG:4326) but different axis order. In other words, GeoTools must not trust blindly the authority code and performs paranoiac checks when possible.
* GeoTools must be able to force the axis order from an arbitrary authority factory to the (longitude, latitude) axis order on user's request.
* If the user do not request explicitly a (longitude, latitude) axis order, the default order is as specified in the authority factory.

Example of using Hints::
  
  Hints hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
  CRSAuthorityFactory factory = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", hints);
  CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem("EPSG:4326");

If the FORCE_LONGITUDE_FIRST_AXIS_ORDER hint is not provided, then the default value is FALSE. The simple CRS class can also be used to specify an authority factory with (longitude, latitude) axis order, with the CRS.getAuthorityFactory(boolean longitudeFirst):

CRS class example::
  
  CRSAuthorityFactory   factory = CRS.getAuthorityFactory(true);
  CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem("EPSG:4326");

To help older applications make the transition, the default value for the FORCE_LONGITUDE_FIRST_AXIS_ORDER hint can be forced to TRUE by setting the **org.geotools.referencing.forceXY** system property. This property can be set by an application usually at startup time, but in all cases before the first request for a CRS factory, as below:

Setting the system-wide default at startup time::
  
  System.setProperty("org.geotools.referencing.forceXY", "true");

If the application code can't be modified, an alternative is to specify this property when invoking java on the command line. The first example below displays all registered referencing factories. Note the "CRSAuthorityFactory" box: its content has a different order when the system property is set to true. The second command line demonstrates that the EPSG:4326 is really forced to (longitude, latitude) axis order when this property is set.

Setting the system-wide default from the command line::
  
  java -Dorg.geotools.referencing.forceXY=true org.geotools.referencing.FactoryFinder
  java -Dorg.geotools.referencing.forceXY=true org.geotools.referencing.factory.epsg.DefaultFactory 4326

Note that the FORCE_LONGITUDE_FIRST_AXIS_ORDER hint has precedence over the org.geotools.referencing.forceXY system property. This means that if the hint is provided with value FALSE, then (latitude, longitude) axis order will be used even if the system property was set to true. This allow axis order control on particular well-known CRS authority factories, and keep the system-wide property as the default value only for cases where axis order is unspecified.

Writing Code
^^^^^^^^^^^^

Here are some guidelines we ask GeoTools API developers to follow. The goal here is to allow GeoTools to work correctly
and let your application make decisions based on your knowledge of your users.

Guidelines for CRS handling in GeoTools:

* Work with CoordinateReferenceSystem objects when defining an API (not magic strings).
* Believe the CoordinateReferenceSystem your user gave you! Trust them, they may built it by hand.

In turn here is what we would like you to do when writing your application.

* Please provide us with a CRS that correctly represents your data:
* If you are in charge of your own data collection, please follow the EPSG database directly.
* If using (longitude, latitude) ordered data all the time, then:
  
  * Geotools 2.2: use the epsg-wkt plugin to the referencing system.
  * Geotools 2.3: use the epsg-hsql plugin and set the org.geotools.referencing.forceXY system property to true.

* If using (latitude,longitude) ordered data all the time, then use the epsg-hsql plugin to the referencing system.
* If using mixed data, then please configure the CRSAuthorityFactory used by your DataStore with assumptions matching your data requirements. The CRS
  factory can be configured using the FORCE_LONGITUDE_FIRST_AXIS_ORDER hint on a feature source basis. Note that it may not be necessary to pass a fully
  constructed CRSAuthorityFactory object; the above-cited hint will suffise in some cases (depending on DataStore implementation).
  
  The last recommendation applies especially to DataStore implementers. We recommend to set the FORCE_LONGITUDE_FIRST_AXIS_ORDER hint every time the axis
  order is well known, even if the value is FALSE. Do not rely on the default value since it may be system-wide settings dependent.

URN Syntax
^^^^^^^^^^

You can make use of the OGC supplied urn syntax (ie urn:x-ogc:def:crs:EPSG:<version>:<code>) rather than just an EPSG code::
  
  CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem("urn:x-ogc:def:crs:EPSG:4326");

If possible use the URI authority syntax where possible, as it does not suffer from the axis order problem.

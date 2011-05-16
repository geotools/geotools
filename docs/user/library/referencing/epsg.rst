EPSG
----

The European Petroleum Survey Group (http://www.epsg.org) maintains a freely available database
with standard codes for coordinate systems, datums, spheroids, units and such like. Additionally,
this database contains the parameters for these objects or - if they cannot easily be expressed as
values - at least references to where such parameters can be found. This database is the standard
"authority" used by many GIS-related programs and projects such as, for example, OpenGIS standards,
GeoTools and GeoTiff and its related projects.

Since some members of the EPSG also worked on the OGC CTS specifications, the OGC CTS API closely
maches the EPSG database schema.

This database has a high value for the GIS community for the following reasons:

* Since every geographic object (coordinate system, spheroid, unit etc.) gets assigned a unique
  number, different programs can easily exchange information. The sending program sends the point(s)
  or features together with a horizontal or projected coordinate system code and the receiving program
  knows precisely where to place the points or features on the earth.
* The database is under active maintenance. The official release at the time of writing (April 2004)
  is v6.5. EPSG puts a lot of effort into ensuring downward compatibility. For example, one code will
  never change its meaning or parameters. If errors are found, the code gets deprecated and the object
  gets assigned a new code if necessary. However, there are still major structural changes ongoing -
  together with a increase in the number of objects catalogued and some objects being deprecated.
  EPSG, therefore, is far from being considered "final" or "nearly final". Still, this database can be
  considered unavoidable when working with and exchanging geographic information.

The database contains coordinate systems and their components, coordinate transformations and their
details (e.g. Bursa Wolf transformation parameters) as well as a table with units of measure and a
list of predefined queries. For more information about its structure and contents, see GEOTOOLS:A
structural comparison of coordinate system concepts.

You can download the database (in MS Access 97 and SQL scripts) from http://www.epsg.org.
For Windows installation HowTo's see their documentation and for Linux see our documentation for the
**gt-epsh-postgres** plugin.

EPSG also maintains some "Guidance notes" on coordinate references systems, map design naming and
similar topics.

GeoTools
^^^^^^^^

GeoTools follows OpenGIS specifications. This applies particularly to the
org.geotools.referencing package and 
our coordinate transformation services. Since these specifications are very flexible, so are the
GeoTools implementations.

For example GeoTools can handle:

* geographic coordinate systems that use {lat; lon} as well as {lon; lat};
* geographic CoordainteSystems whose axes increase to the west and south; and
* CoordainteSystems of every possible unit of measure: degree, radians, grad, meter, inch, feet, and
  all other more uncommon units used in certain countries.

However this means that it cannot be assumed by the user that a geographic CRS is always {lat; lon}
with degrees and axes increasing to north and east although this is mostly the case in North America
and Europe. It it always necessary to verify  and respect the coordinate system properties, that
the specific coordinate system object provides!

There exists a WGS84 constant which defines the commonly used geographic CRS. It is defined as being
{lon; lat}, using degrees, having axes increasing to north and east and using the WGS84 geodetic
datum - which itself is defined as a constant in GeoTools. Since many Bursa-Wolf paremeters define
a transformation between a geographic CRS and WGS84, a transformation between two geographic CRS
would (only using Bursa-Wolf transformations), for example, work as follows: User selected input
geographic CRS -> WGS84 (constant) -> user selected output geographic CRS.

* GeoTools version 2.0 followed the OpenGIS specification 01-009 (Coordinate Transformation Services
  (CT)).
* Geotools 2.1 and latter use the more recent coordinate system concepts described in the OpenGIS
  abstract specification 03-073r1 (Spatial Referencing by Coordinates).

Although the EPSG database already contains the new structures described therein, the database v6.x
can be used by GeoTools without problems - the EPSG factory takes care of all the details. But what
are these differences actually? This is the topic of the following chapter...

Comparison
^^^^^^^^^^

Geographic coordinate systems are quite complex, and their creation, coordinate transformations and
storage are not straightforward at all. This chapter will help to explain the underlying structures
as it describes the state-of-the-art concepts regarding geographical coordinate systems and their
parts as, for example, geodetic datums and ellipsoids.

In order to gain a better understanding, it is recommended that you have the EPSG database
installed. Choose a database viewer and have a look at the database.

Note: Just in case you don't have a good viewer at hand, OpenOffice does a good job of viewing the
database. You will have to register the database under Extras->Data sources as an ODBC database.
Then, in any document, press F4 to open the database window. Now you can easily browse the
database's tables.

You should see a structure like the one in the following screenshot:

.. image:: /images/epsg_database.png

You will soon see that the database (v6.x) has a hierarchical structure that is equal to the new
OpenGIS abstract specification Spatial Referencing by Coordinates. The "old" OpenGIS specification
Coordinate Transformation Services (CT) had a slightly different structure - as had the "old" EPSG
datbases v5.x. The following paragraphs give a simplified overview of these hierarchies.

EPSG6.x
'''''''

The new EPSG database, in contrast to older approaches, distinguishes between coordinate systems
(CS) and coordinate reference systems (CRS).

A CS is a coordinate system in the mathematical sense. There are cartesian CS for flat things like
maps, ellipsoidal CS that describe an ellipsoid and geocentric CS which have their origin at the
center of the ellipsoid (the earth).

On the other hand, a CRS is a specific implementation of such a CS with additional parameters that
pin the CS to the earth. In other words, while a CS is general, a CRS is specific to describing
locations points on/in/above the earth.

Projected Coordinate Reference System (CRS)

* "Cartesian" Coordinate System (CS): Axis Info
* Projection
* "Geographic2D" Coordinate Reference System (CRS)
  
  * "Ellipsoidal" Coordinate System (CS): Axis info
  * Geodetic datum: which defines the conversion to WGS84
    
    * Ellipsoid: Linear unit of measure, Semi-major axis, Inverse flattening OR  Semi-minor axis
    * Prime Meridian: Angular unit of measure, Greenwich longitude

EPSG5.x
'''''''

"Projected" Coordinate System
 
* Axis info
* Projection
* "Geographic2D" Coordinate System: Axis info
   * Geodetic datum: which defines the conversion to WGS84
     
     * Ellipsoid: Linear unit of measure, Semi-major axis, Inverse flattening OR  Semi-minor axis
     * Prime Meridian: Angular unit of measure, Greenwich longitude

EPSG v6.x structure in more detail
''''''''''''''''''''''''''''''''''

In the following hierarchical structure, columns that merely serve for
description or database maintenance have been omitted for clarity.

"Projected" Coordinate Reference System (CRS)

* AREA_OF_USE_CODE
* COORD_REF_SYS_KIND = "projected"
* COORD_SYS_CODE
  
  * COORD_SYS_TYPE ="cartesian"
  * DIMENSION (1 to 3)
  * COORD_AXIS_NAME_CODE , ..._ORIENTATION and ..._ABBREVIATION for each axis
  * UOM_CODE for each axis
  * ORDER for each axis
  
* PROJECTION_CONV_CODE
* SOURCE_GEOGCS_CODE (= Geographic2D Coordinate Reference System)

  * AREA_OF_USE_CODE
  * COORD_REF_SYS_KIND = "geographic2D"
  * COORD_SYS_CODE
    
    * COORD_SYS_TYPE ="ellipsoidal"
    * DIMENSION (1 to 3)
    * COORD_AXIS_NAME_CODE , ..._ORIENTATION and ..._ABBREVIATION for each axis
    * UOM_CODE for each axis
    * ORDER for each axis
  
  * DATUM_CODE
    
    * DATUM_TYPE = "geodetic"
    * ELLIPSOID_CODE
      
      * UOM_CODE
      * SEMI_MAJOR_AXIS
      * INV_FLATTENING or
      * SEMI_MINOR_AXIS
      * ELLIPSOID_SHAPE = mostly true
      
    * PRIME_MERIDIAN_CODE
      
      * GREENWICH_LONGITUDE
      * UOM_CODE
    * AREA_OF_USE_CODE

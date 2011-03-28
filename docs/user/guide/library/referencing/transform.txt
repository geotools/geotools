Transform
---------

The following is a list of the projection parameters used in the Geotools referencing module. These try to follow the OGC Coordinate Transformation Services specification (OGC 01-009), but this document only specified the parameters for a few projections. When in doubt we have followed the EPSG Guidance Note Number 7 and suggestions from the Projections Transform List.

Parameter Names
^^^^^^^^^^^^^^^

Parameters can have more than one name, and are recognised by all the names known to the MathTransformFactory. Many classification and parameter names in GeoTools come from the legacy OGC 01-009 document. But what about other standards for classification names (e.g., "Transverse_Mercator" and "Orthogonal") and parameter names (e.g., "semi_major")?

**GeoTIFF parameter names**

In addition to OGC 01-009, we used an other source of very valuable references for classification and parameter names: the GeoTIFF projection list:

http://www.remotesensing.org/geotiff/proj_list/

This list provides both GeoTIFF and OGC classification and parameter names. This web site was actually one of our main sources for classification and parameter names in Geotools. The "Mercator (1SP)" projection in this list provides the "latitude_of_origin" and similar parameters.

**Custom parameter names**

But let's say I'm a developer and I want to develop something that has no "official" names for the parameters? If we can't find a standard name, we use ours own. This is the case for example of the "Logarithmic" transform.

**Authorities for Parameter and Classification Names**

However, each name is always associated to an authority (or a "scope" in the context of scoped names). Thats said, the full name of "semi_major" is actually "OGC:semi_major", where "semi_major" is the LocalName (from the org.opengis.util package) and "OGC" is the scope. The mechanism is similar to fully qualified names in Java packages.

It should always be possible to know if a particular name is an OGC's name or a GeoTIFF one: just look at the scope. For our own transformation, we use "Geotools:" scope. If we find an official name from OGC later, we will add that "OGC:" scoped name.

**Multiple names for Parameters**

A single physical parameter can have more than one name. Each classification/parameter can have an arbitrary number of names. Actually they do have. For example (using again the "Mercator_1SP" classification), one can try the following from the command line::
  
  java org.geotools.referencing.operation.DefaultMathTransformFactory Mercator_1SP

Geotools will tells you that it know all of the above names for this transformation:

* OGC:Mercator_1SP
* EPSG:Mercator (1SP)
* EPSG:9804
* GeoTIFF:CT_Mercator
* Geotools:Projection Mercator cylindrique

Note the Geotools name is localised, so it may appears in another language depending on how you have your machine set up.

One can use any of those names. Geotools will recognizes "CT_Mercator" as well as "Mercator_1SP". However, if one wants to be sure that he is looking for a GeoTIFF name and not an OGC name, we can always use the fully qualified name.

For example GeoTools:

* GeoTIFF:CT_Mercator - will accept this name
* OGC:CT_Mercator - but won't recognise this one
* OGC:Mercator_1SP - recognised

One can use DefaultMathTransformFactory in order to experiment the names from the command line.

Map Projection Parameters
^^^^^^^^^^^^^^^^^^^^^^^^^

All projections have the following default parameters:

* semi_major - equatorial radius of the ellipsoid of reference
* semi_minor - polar radius of the ellipsoid of reference
* central_meridian - longitude for the natural origin (for rectangular coordinates)
* false_easting - easting value assigned to the natural origin (added to all x coordinates)
* false_northing - northing value assigned to the natural origin (added to all y coordinates)

The "semi_major" and "semi_minor" parameters do not need to be specified in the projection section of WKT strings, since this information can be obtained from the GEOGCS.

Additional parameters are shown in the following table. Note that "latitude_of_origin" and "scale_factor" are usually default parameters, except in the Mercator and some conic projections.


.. list-table:: Map Projection Parameters
   :widths: 60 30 30 30 30 30
   :header-rows: 1
   
   * - Projection
     - latitude_of_origin
     - scale_factor
     - standard_parallel_1
     - standard_parallel_2
     - latitude_true_scale
   * - Mercator_1SP
     - 
     - Yes
     - 
     - 
     - 
   * - Mercator_2SP
     - 
     - 
     - Yes
     - 
     - 
   * - Transverse_Mercator
     - Yes
     - Yes
     - 
     - 
     - 
   * - Lambert_Conformal_Conic_1SP
     - Yes
     - Yes
     - 
     - 
     - 
   * - Lambert_Conformal_Conic_2SP
     - Yes
     - 
     - Yes
     - Yes
     - 
   * - Lambert_Conformal_Conic_2SP_Belgium
     - Yes
     - 
     - Yes
     - Yes
     - 
   * - Albers_Conic_Equal_Area
     - Yes
     - 
     - Yes
     - Yes
     - 
   * - Oblique_Stereographic
     - Yes
     - Yes
     - 
     - 
     - 
   * - Polar_Stereographic
     - Yes
     - Yes
     - 
     - 
     - Yes
   * - Polar_Stereographic_Series
     - Yes
     - Yes
     - 
     - 
     - 
   * - Stereographic
     - Yes
     - Yes
     - 
     - 
     - 
   * - Orthographic
     - Yes
     - Yes
     - 
     - 
     - 

latitude_of_origin
  latitude of the natural origin (for rectangular coordinates)

scale_factor
  scale factor at the natural origin (along a parallel of latitude)

standard_parallel_1
  latitude of first standard parallel (true to scale)

standard_parallel_2
  latitude of second standard parallel (true to scale)

latitude_true_scale
  latitude (parallel) where the scale will equal the scale factor

Notes

* For the "Lambert_Conformal_Conic_1SP", the standard parallel is equal to the latitude of origin.
* The "latitude_true_scale" parameter of the "Polar_Stereographic" is not a standard EPSG parameter. "Polar_Stereographic_Series" uses the correct EPSG parameters (but a non-standard name).
* For more information, see the javadocs for the projection classes in Geotools.

Notes about projections in ESRI's ArcGIS 8.x

* The "Mercator_1SP" and "Mercator_2SP" are simply called the "Mercator" in ArcGIS. The distinction between the 1 and 2 standard parallel cases is determined based on the "standard_parallel_1" parameter.
* "Lambert_Conformal_Conic_1SP" and "Lambert_Conformal_Conic_2SP" are simply called the "Lambert_Conformal_Conic". The distinction is based on the values of the "standard_parallel_1" and "standard_parallel_2" parameters. Note that the "Lambert_Conformal_Conic" will NOT use the "latitude_of_origin" as the standard_parallel in the 1 SP case: you must also specify a "standard_parallel_1" parameter.
* The "Albers_Conic_Equal_Area" is called the "Albers" in ArcGIS.

Other Math Transform Parameters
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

"Affine"

* num_row - number of rows in matrix
* num_col - number of columns in matrix
* elt_<r>_<c> - element of matrix (where r is from 0 to (num_row - 1) and c is from 0 to (num_col - 1)

"Geocentric_To_Ellipsoid" and "Ellipsoid_To_Geocentric"

* semi_major - equatorial radius of the ellipsoid of reference
* semi_minor - polar radius of the ellipsoid of reference

"Molodenski" and "Abridged_Molodenski"

* dim - dimension of points (2 or 3)
* dx - x shift (m)
* dy - y shift (m)
* dz - z shift (m)
* src_semi_major - source equatorial radius (m)
* src_semi_minor - source polar radius (m)
* tgt_semi_major - target equatorial radius (m)
* tgt_semi_minor - target polar radius (m)

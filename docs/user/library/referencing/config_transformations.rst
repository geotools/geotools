Coordinate operations tuning
----------------------------

The referencing subsystem provides a single MathTransformation object going from a source
and a target CoordinateReferenceSystem. However, the EPSG database contains a number of
possible transformation paths between two CRS. The referencing module will select one
that is not deprecated, not superceded, and has the highest accuracy.

This behavior can be modified by setting the following system properties:

* "org.geotools.referencing.operation.order", with possible values "AccuracyFirst" and "AreaFirst".
  "AccuracyFirst" is the default, will prefer the most accurate available transformation, at the
  expense of area coverage (if two transformation share the same accuracy, the one covering the
  largest area will be selected). "AreaFirst" will prefer the transformation covering the largest
  area, at the expense of accuracy. If two transformation share the same area coverage, the most
  accurate one will be selected.

* "org.geotools.referencing.pivots", which is a comma separate list of EPSG CoordinateReferenceSystem
  identifiers to be used as pivots in case there is no direct transformation path between two geographic
  CRSs. It defaults to "4258,4269", that is, ETRS89 and NAD83. This is useful for improved accuracy
  as the EPSG database might not contain a direct transformation path between two CRSs, but it might
  contain a path going through a pivot CRS, that's more accurate than using the TOWGS84 parameters.
  The value can be empty to disable pivots.


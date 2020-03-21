NetCDF Plugin
-------------

The NetCDF plugin supports gridded NetCDF files having dimensions
following the COARDS convention (custom, Time, Elevation, Lat, Lon). The
NetCDF plugin supports plain NetCDF data sets (``.nc`` files) as well ``.ncml``
files (which aggregate and/or modify one or more data sets) and Feature
Collections. It supports Forecast Model Run Collection Aggregations
(FMRC) either through the NCML or Feature Collection syntax. It supports
an unlimited amount of custom dimensions, including runtime.

Further documentation is available in `the GeoServer project <https://docs.geoserver.org/latest/en/user/extensions/netcdf/netcdf.html#notes-on-supported-netcdfs>`_

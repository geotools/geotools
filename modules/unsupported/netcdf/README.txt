/*****************************************
 * GEONETCDF
 * June 19, 2012
 * v. 1.0
 *****************************************/

geonetcdf is designed to run with geoserver-2.2-beta1

To use geonetcdf:

1. Package project with "mvn clean package"
2. Target directory should now contain geonetcdf-1.0-zip-with-dependencies.zip 
   - contains project jar and two dependent jars: joda-time and netcdf
     * joda-time-1.5.2.jar and netcdf-4.2-min.jar are required to use geonetcdf
3. Extract geonetcdf-1.0-zip-with-dependencies.zip to the
    ../geoserver/WEB-INF/lib directory of your web server
   
The NetCDF reader should now be available as a raster data source.

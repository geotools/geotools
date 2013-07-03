GeoTools netcdf project
-----------------------
The netcdf project creates a NetCDFFormat to read NetCDF files and render a GridCoverage2D based on its data.
NetCDF data files can contain many dimensions, and this project currently handles elevation, time and analysis time.



To build GeoTools with the NetCDF module included
-------------------------------------------------
1. Because the netcdf module is currently an unsupported module, confirm its immediate parent POM, <yourGeoToolsProject>/modules/unsupported/pom.xml, is configured to include the netcdf module in the build.  It should include the entry below.  If it or its activation all property are commented out, uncomment it.

     <profile>
       <id>netcdf</id>
       <activation>
         <property><name>all</name></property>
       </activation>
       <modules>
         <module>netcdf</module>
       </modules>
     </profile>

2. Build GeoTools, include "-Dall" to tell it to build all the modules
  mvn install -Dall
http://docs.geotools.org/latest/userguide/build/index.html

(or could use -Dnetcdf if only want it, as long as the netcdf profile is not commented out in step 1)

3. TODO add a confirmation step/instructions on how to use it in code (besides the GeoServer example below).


To create a NetCDF datastore and layer in GeoServer
--------------------------------------------------- 

1. Build GeoServer with a version of GeoTools that has the NetCDF module included.
GeoServer knows how to make use of any formats exposed by GeoTools.

2. Deploy GeoServer to your application server.

3. Get a NetCDF file you want to use.  If you don't have one, you can choose one from the GeoTools netcdf project's test directory.

4. Configure a NetCDF data store in GeoServer.
  a. log into the GeoServer admin ui.
  b. in the left nav, choose Data, Stores.
  c. choose Add New Store.
  d. choose Raster Data Sources, NetCDF (if NetCDF is not in the list, there is a problem).
  e. enter a new name for your store and enter the file location in the URL field.
  f. choose Save.

5. Add a layer based on the NetCDF data store.
  a. step 4. should send you to a New Layer page, choose the Publish button on that page.
  b. if that did not happen, in the left nav, choose Layers, Add New Layer. 
  c. either 5a. or 5b. should send you to an Edit Layer page with several tabs.
  d. accept the defaults on the first tab, except scroll down to bottom, Coverage Parameters section, and set the value of the NETCDFPARAMETER you want to use (needs to be one of the parameters in the NetCDF file you chose for your data store).
  e. move to the Dimensions tab, and enable Time and/or Elevation and their presentation style.
  f. choose Save.

6. Preview the layer.
  a. in the left nav, choose Data, Layer Preview.
  b. find the new NetCDF layer in the layer list and choose a preview option.
  c. a new window/tab should open up with the requested preview.  Depending on the data in the file and on the default style it may or may not render well.
ImageIO-EXT GDAL Plugin
-----------------------

The ImageIO-EXT plug-in allows GeoTools to make use of the additional formats provided via the
ImageIO-EXT project.

The Java ImageIO library comes with a few formats out of the box (such as PNG, etc...) the
ImageIO-EXT project provides support for additional geospatial formats.

**References**

* https://github.com/geosolutions-it/imageio-ext

**Maven**:

.. code-block:: xml
  
   <dependency>
     <groupId>org.geotools</groupId>
     <artifactId>gt-imageio-ext-gdal</artifactId>
     <version>${geotools.version}</version>
   </dependency>

Formats
^^^^^^^

At the GeoTools level, the set of formats supported by the GeoTools ImageIO-Ext GDAL plugin
is composed of:

* DTED Elevation Raster
* ESRI ``.hdr`` labeled
* Erdas Imagine Images
* NITF: National Imagery Transmission Format
* ECW: ERMapper Compressed Wavelets
* ERMapper JPEG2000
* Kakadu JPEG2000
* MrSID: Multi-resolution Seamless Image Database
* MrSID JPEG2000 

GDAL
^^^^

If your Java Runtime Environment includes GDAL libraries you will be able to support additional
file formats:

* Arc/Info ASCII Grid
* Arc/Info Binary Grid
* ENVI ``.hdr`` labeled
* ENVISAT Image Format
* GDAL VRT
* GeoTIFF
* IDRISI
* JPEG
* RPF TOC
* SRP (ASRP/USRP) formats

To make use of the GDAL based coverage readers:

* GDAL must be installed
* linux: Include GDAL libraries in ``LD_LIBRARY_PATH`` environmental variable
* windows: Include GDAL libraries in ``PATH`` environmental variable
* macos: Include GDAL libraries in ``java.library.path`` system property
* ``GDAL_DATA`` environmental variable set to the location of GDAL, OGR and PROJ libraries
* Environmental variable ``GDAL_PATH`` set to the location of your GDAL installation
* Environmental variable ``PROJ_LIB`` set the the location of PROJ 

See the ImageIO-EXT page on `GDAL-framework-and-plugins <https://github.com/geosolutions-it/imageio-ext/wiki/GDAL-framework-and-plugins>`__ for more info about the list of supported formats and available drivers.

This functionality makes use of GDAL SWIG bindings, with GDAL 3.2 bindings included at the time of writing via transitive dependency:

.. code-block:: xml

   <dependency>
     <groupId>org.geotools</groupId>
     <artifactId>gt-imageio-ext-gdal</artifactId>
     <version>${geotools.version}</version>
   </dependency>

To match the specific GDAL bindings available in your environment, use dependency exclusions:

.. code-block:: xml

   <dependency>
     <groupId>org.geotools</groupId>
     <artifactId>gt-imageio-ext-gdal</artifactId>
     <version>${geotools.version}</version>
     <exclusions>
       <exclusion>
         <groupId>org.gdal</groupId>
         <artifactId>gdal</artifactId>
       </exclusion>
     </exclusions>
   </dependency>
   <!-- Ubuntu 21.10 includes gdal 3.4.1 -->
   <dependency>
     <groupId>org.gdal</groupId>
     <artifactId>gdal</artifactId>
     <version>3.4.1</version>
   </dependency>

The ImageIO-EXT class ``GDALUtilities`` class is responsible for ensuring GDAL available:

.. code-block::
   
   GDALUtilities.loadGDAL();

The load ``loadGDAL()`` method checks compatibility for:

* GDAL 3 and later rely on SWIG bindings ``gdal.jar`` to load the library
* GDAL 2.3 and later use the ``gdalalljni`` library
* GDAL 2.3 and earlier use the ``gdaljni`` library.

If the library is unavailable you will see a warning:

  Failed to load the GDAL native libs. This is not a problem
  unless you need to use the GDAL plugins: they won't be enabled.

You can confirm if GDAL is loaded correctly, and GDAL formats are registered, using:

.. code-block:: java
   
   if (GDALUtilities.isGDALAvailable()) {
       // gdal bridge is active
   }

Example Use
^^^^^^^^^^^

You can use the additional file formats with FormatFinder.

The following example of direct uses goes to great lengths to show some of the available
parameters:

.. code-block:: java
  
  final File file = new File ("C:/testdata/sampledata.sid");
  final MrSIDReader reader = new MrSIDReader(file);
  
  //
  //
  // Setting GridGeometry for reading half coverage.
  //
  //
  final Rectangle range = reader.getOriginalGridRange().toRectangle();
  final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope();
  final GeneralEnvelope reducedEnvelope = new GeneralEnvelope(new double[] {
        originalEnvelope.getLowerCorner().getOrdinate(0),
        originalEnvelope.getLowerCorner().getOrdinate(1)},
        new double[] { originalEnvelope.getMedian().getOrdinate(0),
                  originalEnvelope.getMedian().getOrdinate(1)});
  reducedEnvelope.setCoordinateReferenceSystem(reader.getCrs());
  
  final ParameterValue gg = (ParameterValue) ((AbstractGridFormat) reader
          .getFormat()).READ_GRIDGEOMETRY2D.createValue();
  
  gg.setValue(new GridGeometry2D(new GeneralGridRange(new Rectangle(0, 0,
        (int) (range.width / 2.0),
        (int) (range.height / 2.0))), reducedEnvelope));
  
  // /////////////////////////////////////////////////////////////////////
  //
  // Read ignoring overviews with subsampling and crop, using Jai,
  // multithreading and customized tilesize
  //
  // /////////////////////////////////////////////////////////////////////
  final ParameterValue policy = (ParameterValue) ((AbstractGridFormat) reader
        .getFormat()).OVERVIEW_POLICY.createValue();
  policy.setValue(OverviewPolicy.IGNORE);
  
  // //
  //
  // Enable multithreading read
  //
  // //
  final ParameterValue mt = (ParameterValue) ((BaseGDALGridFormat) reader
          .getFormat()).USE_MULTITHREADING.createValue();
  mt.setValue(true);
  
  // //
  //
  // Customizing Tile Size
  //
  // //
  final ParameterValue tilesize = (ParameterValue) ((BaseGDALGridFormat) reader
          .getFormat()).SUGGESTED_TILE_SIZE.createValue();
  tilesize.setValue("512,512");
  
  // //
  //
  // Setting read type: use JAI ImageRead
  //
  // //
  final ParameterValue useJaiRead = (ParameterValue) ((BaseGDALGridFormat) reader
          .getFormat()).USE_JAI_IMAGEREAD.createValue();
  useJaiRead.setValue(true);
  
  // //
  //
  // Setting the footprint behavior
  //
  // For this example, there should be a C:/testdata/sampledata.wkt file containing 
  // the footprint, so that the masking can occur
  // //
  final ParameterValue<String> footprint = AbstractGridFormat.FOOTPRINT_BEHAVIOR.createValue();
  footprint.setValue(FootprintBehavior.Transparent.toString());
  
  GridCoverage gc = (GridCoverage2D) reader.read(new GeneralParameterValue[] { gg,
          policy, mt, tilesize, useJaiRead, footprint});

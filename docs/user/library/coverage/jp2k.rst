JP2K Plugin
-----------

The JP2K plug-in allows GeoTools to make use of the JP2K format provided via the available JP2K
image readers in the JAI ImageIO project plus an optional ImageIO plugin based on the Kakadu
library which is activated only when the Kakadu SDK is in the path (the Kakakdu SDK is not
Open Source but t comes with a variety of licenses).

Notice that:

1. The Kakadu ImageIO plugin, when available, takes precedence over the standard ImageIO JP2K plugins
2. The JP2K plugins available through GDAL, which relies on the ImageIO-Ext GDAL plugin, are separate from this plugin and leave in their own module

**Maven**::
   
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-jp2k</artifactId>
      <version>${geotools.version}</version>
    </dependency>

Use of Kakadu
^^^^^^^^^^^^^

Much better performances in accessing JP2K data with respect to standard JAI ImageIO and GDAL supported SDKs are obtained by directly using the Kakadu libraries in this plugin. Please, noticet that the Image I/O-Ext project contains also a plug-in built on top GDAL which in turn uses Kakadu as well, however this new plugin does not need GDAL but only the Kakadu binaries.

In order to know how to build your Kakadu native libs needed to enable support for it, please, check the imageio-ext setup guide. If your Java Runtime Environment includes the Kakadu native libraries, you will be able to support the JP2K format by means of this plugin, otherwise the JP2K format data access capabilities will be provided by the SUN's ImageIO JP2K plugin. The plugin, when Kakadu is in the path, is able to parse georeferencing information by supporting the following mechanisms:

1. World File + PRJ File (they take precedence over the other mechanism and therefore they can be used to override missing or improper georeferencing).
2. Degenerated GeoTIFF Box (seems that the wording GeoJP2 cannot be used for licensing problems, but that is essentially how everybody calls this box)
3. MSIG WorldFile Box (if presents), contained within propers UUID boxes.

Notes:

* This plugin allows to override the georeferencing information as it is obtained
  from the internal supported JP2K Boxes. This means that if the CRS is missing
  and/or incorrect you can ovveride it via providing a PRJ file (filename.prj);
  on the same line, if you want to override the grid to world transformation you
  can do that via providing a world file(filename.j2w, filename.wld).
* Notice that at this time, no GMLJP2 support has been implemented even though that
  could be done relatively easily. Further information on those boxes are available here.
* Notice that at this time, we have tested this plugin with Kakadu 5.2.6 and 6.3,
  where the latter is much faster.

Standard JP2K readers

In case you will use the SUN's ImageIO JP2K plugins (as an instance, in case you don't have a copy of the Kakadu software), we have not yet implemented full support to the georeferencing information contained within the JP2K metadata. For this reason, you need to add a PRJ and a WorldFile (J2W file or WLD file) beside your JP2K dataset, in case you'd want to be able to serve it as a geospatial dataset.

Format parameters

A set of parameters allows to customize the way the coverage will be accesed:

InputTransparentColor
   Set the transparent color for the image

USE_JAI_IMAGEREAD
   Controls the low level mechanism to read the granules. If ‘true’ the plugin will make use of JAI ImageRead operation and its deferred loading mechanism, if ‘false’ it will perform direct ImageIO read calls which will result in immediate loading.

USE_MULTITHREADING
   if ‘true’, the plugin will use a version of the JAI ImageRead with support for multithreading loading.

SUGGESTED_TILE_SIZE
   Controls the tile size of the input images when using tiled read. It consists of two positive integers separated by a comma, like 512,512. 

**Code Example**

Here below we have added some code to show how to interact with the plugin to read JP2K data as GridCoverage2D. There is not much difference between this code and the code to interact with, let's say, a GeoTiff.::

     final File file = new File ("/home/simone/data/geosolutions.jp2");
     final JP2KReader reader = new JP2KReader (file);
     
     // //
     //
     // Setting GridGeometry for reading half coverage.
     //
     // //
     final Rectangle range = ((GridEnvelope2D)reader.getOriginalGridRange()).getBounds();
     final GeneralEnvelope originalEnvelope = reader.getOriginalEnvelope();
     final GeneralEnvelope reducedEnvelope = new GeneralEnvelope(
                new double[] {
              originalEnvelope.getLowerCorner().getOrdinate(0),
              originalEnvelope.getLowerCorner().getOrdinate(1)},
             new double[] {
                 originalEnvelope.getMedian().getOrdinate(0),
              originalEnvelope.getMedian().getOrdinate(1)});
    	reducedEnvelope.setCoordinateReferenceSystem(reader.getCrs());
     
    	final ParameterValue<GridGeometry2D> gg = JP2KFormat.READ_GRIDGEOMETRY2D.createValue();
    	gg.setValue(new GridGeometry2D(new GridEnvelope2D(new Rectangle(
                0,
                0,
             (int) (range.width / 2.0),
             (int) (range.height / 2.0))), reducedEnvelope)
        );
     
    	// /////////////////////////////////////////////////////////////////////
    	//
    	// Read with subsampling and crop, using Jai and customized tilesize
    	//
    	// /////////////////////////////////////////////////////////////////////
     
    	// //
    	//
    	// Customizing Tile Size
    	//
    	// //
    	final ParameterValue<String> tilesize = JP2KFormat.SUGGESTED_TILE_SIZE.createValue();
    	tilesize.setValue("512,512");
     
    	// //
    	//
    	// Setting read type: use JAI ImageRead
    	//
    	// //
    	final ParameterValue<Boolean> useJaiRead = JP2KFormat.USE_JAI_IMAGEREAD.createValue();
    	useJaiRead.setValue(true);
     
    	final GridCoverage gc = (GridCoverage2D) reader.read(new GeneralParameterValue[] { gg,tilesize, useJaiRead });


ImageIO-EXT GDAL Plugin
-----------------------

The ImageIO-EXT plug-in allows geotools to make use of the additional formats provided via the Java Extension ImageIO-EXT.

The Java ImageIO library comes with a few formats out of the box (such as PNG, etc...) the imageio-ext project provides support for additional geospatial formats.

References:

* http://java.net/projects/imageio-ext/

At the GeoTools level, the set of formats supported by the GeoTools ImageIO-Ext GDAL plugin is composed of:

* DTED Elevation Raster
* ESRI .hdr labelled
* Erdas Imagine Images
* NITF: National Imagery Transmission Format
* ECW: ERMapper Compressed Wavelets
* ERMapper JPEG2000
* Kakadu JPEG2000
* MrSID: Multi-resolution Seamless Image Database
* MrSID JPEG2000 

GDAL
^^^^

If your Java Runtime Environment includes GDAL libraries you will be able to support additional file formats:

* Arc/Info ASCII Grid
* Arc/Info Binary Grid
* ENVI .hdr Labelled
* ENVISAT Image Format
* GeoTIFF
* JPEG

The ImageIO-EXT website includes ready to use ImageIO-Ext binaries including GDAL libraries. See "Release Information" for more info about the list of supported formats and available drivers.

Example Use
^^^^^^^^^^^

You can use the additional file formats with FormatFinder.

The following example of direct uses goes to great lengths to show some of the available
parameters::
  
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
  
  GridCoverage gc = (GridCoverage2D) reader.read(new GeneralParameterValue[] { gg,
          policy, mt, tilesize, useJaiRead });

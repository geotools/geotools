ArcGrid Plugin
--------------

The arcgrid module in the plugin group provides access to the ARCGRID raster format defined by ESRI and used in that company's software suites.

This is a straightforward plugin with no additional information needed beyond that advertised by the GridCoverageExchange API.

Please note that this format is well suited to data exchange; especially when considered as a text
file is is obviously not suited to high performance.

The arcgrid plugin supports:

* Normal GridFormatFinder use::

            File f = new File("ArcGrid.asc");
            // Reading the coverage through a file
            AbstractGridFormat format = GridFormatFinder.findFormat( f );
            AbstractGridCoverage2DReader reader = format.getReader(f);
            
            GridCoverage2D gc = reader.read(null);
  
  This is the preferred approach as it allows your code to remain format
  independent.

* Direct use for reading from a file::

        File f = new File"arcgrid/spearfish.asc.gz");
        
        GridCoverageReader reader = new ArcGridReader(f);
        GridCoverage2D gc = (GridCoverage2D) reader.read(null);

* Reading the gzipped coverage through an ImageInputStream::

        ImageInputStream iiStream = ImageIO.createImageInputStream(
               new GZIPInputStream(new FileInputStream(f)));
        GridCoverageReader reader = new ArcGridReader(iiStream,hints);
        GridCoverage2D gc = (GridCoverage2D) reader.read(null);

* Reading the gzipped coverage through an InputStream::

        GridCoverageReader reader = new ArcGridReader( new GZIPInputStream(
                new FileInputStream(f)),hints);
        GridCoverage2D gc = (GridCoverage2D) reader.read(null);
        
* Reading the gzipped coverage through a URL::
        
        GridCoverageReader reader = new ArcGridReader( f.toURI().toURL(),hints);
        GridCoverage2D gc = (GridCoverage2D) reader.read(null);


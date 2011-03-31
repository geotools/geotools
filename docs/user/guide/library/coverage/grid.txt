GridCoverage
------------

To quickly read a GridCoverage we can make use of the grid coverage format support.:
   
.. literalinclude:: /../src/main/java/org/geotools/coverage/CoverageExamples.java
   :language: java
   :start-after: // exampleGridFormat start
   :end-before: // exampleGridFormat end

If you already know what kind of file you have you have you can create the reader directly::

  File file = new File("test.tiff");
  
  GeoTiffReader reader = new GeoTiffReader(file, new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE));
  GridCoverage2D coverage = reader.read(null);

You can also create a GridCoverage "by hand" if you have a BufferedImage in memory that you would like to place onto a map.

Here is an example of creating a grid coverage from a tile (containing a bufferedImage and a ReferencedEnvelope location:

.. literalinclude:: /../src/main/java/org/geotools/coverage/CoverageExamples.java
   :language: java
   :start-after: // exampleGridCoverageUsing start
   :end-before: // exampleGridCoverageUsing end

Using
^^^^^

Once we have created a GridCoverage2D, we can then use the class to perform useful tasks. Many uses require access to the class itself but more generic uses can interact with the instance through its OpenGIS GridCoverage interface.

GridCoverage2D provides access to:
  
.. literalinclude:: /../src/main/java/org/geotools/coverage/CoverageExamples.java
   :language: java
   :start-after: // exampleGridCoverageFactory start
   :end-before: // exampleGridCoverageFactory end

GridCovearge2D supports simple use directly using the methods above.
For more complex uses we go through the Operations class as explained in the next section.

You can also evaluate the coverage at a specific point in order to determine what values are present at that location.:

.. literalinclude:: /../src/main/java/org/geotools/coverage/CoverageExamples.java
   :language: java
   :start-after: // exampleGridCoverageDirect start
   :end-before: // exampleGridCoverageDirect end


Views
'''''

There are several 'views' of a GridCoverage2D object. Views are themselves GridCoverage2D objects but they privilege a particular numerical representation.

The GridCoverage2D can present its values as they were in the original data source; such values are considered to be the 'NATIVE' values. Under a different view, the GridCoverage2D can present the values as transformed by the Category defined for each SampleDimension which are the 'GEOPHYSICAL' values. Under the ViewType.RENDERED view, the values obtained from the GridCoverage2d will be the same as under the 'NATIVE' view unless those values cannot be rendered by the rendering system in which case the GeoTools module will change the values into renderable numbers. The 'PHOTOGRAPHIC' view type will convert the original values first to their geophysical realization and then using the default color table of each Category into some coloured pixel value.

To change the view on a GridCoverage2D called myGridCov, we can::
  
  GridCoverage2D scicov = myGridCov.getView(ViewType.GEOPHYSICS);

Similarly we can get a GridCoverage2D view of one of the other kind even going back to the original view.::
  
  evaluate(..)

Once armed with the appropriate view, we can use the evaluate(DirectPosition dp) method to obtain a nearest-neighbor interpolated value for any DirectPosition within the extent of the GridCoverage.

As a trivial example, we could evaluate the scientific value of the 'GEOPHYSICS' view created above at a particular DirectPosition with::
  
  Object result = scicov.evaluate(somedp);

The return value is dependent on the contents of our grid coverage. There are a series of such evaluate methods which return values in more useful formats.

RenderedImage
'''''''''''''

We can trivially get an AWT image from a GridCoverage2D with a simple call to the getRenderedImage() method. The result is a standard RenderedImage.

Obviously we can combine two of these steps to go directly from the created grid coverage to the final image::
  
  RenderedImage ri = myGridCoverage.getView(ViewType.GEOPHYSICS).getRenderedImage();

It should be possible, when generating the RenderedImage to specify the color scheme to use for that particular image. Currently this
is handled as part of the rendering process using a RasterSymbolizer.

Advanced
^^^^^^^^

The coverage module provides some powerful methods to use the different views of the grid coverage. Many of these uses are backed by the standard JAI mechanisms behind the scenes, sometimes combined with the georeferencing operations of the referencing module.

The GeoTools coverage module provides **Operations** for convenience, this class bundles up the sophisticated and powerful internal mechanism of the processing package. The package summary in javadocs begins the explanation of the internal mechanism. This documentation page will need to be expanded with a better explanation of the coverage module operation system.

The basic pattern of the operations mechanism runs as follows:

1. Instantiate a new Processor
2. Get the parameter group for the desired operation
3. Configure the parameters in the parameter group
4. Call the doOperation(..) method on the Processor
   
   With several options for configuration along the way.

Operations
^^^^^^^^^^

The Operations class provides a default instance with a series of methods to perform operations on a grid coverage. We use the static instance Operations.DEFAULT which acts merely as a convenience wrapper around the DefaultProcessor.doOperation(..) method and provides type safety and a simpler argument list.

For all the methods which return Coverages, when using the GeoTools implementation we can simply cast them to the GridCoverage2D.

resample
''''''''

We can create a new grid coverage which is a resampling of the original grid coverage into a new image. We start by defining the georeferenced geometry of the resulting image and then call the resample(..) method of the Operations.DEFAULT instance.

A new GridGeometry class can be defined using either an Envelope, height and width for convenience or, like for the GridCoverage2D constructor, using the MathTransform and CoordinateReferencingSystem instead of the envelope.

Then we can invoke the resample(..) operation.::
  
  GridGeometry mygg = new GridGeometry(...);
  GridCoverage2D covresample = (GridCoverage2D) Operations.DEFAULT.resample(scicov,mygg);
  Georeferencing Transformation

We can use the resample(..) method to transform any GridCoverage to another CRS.::
  
  CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:32632");
  GridCoverage2D covtransformed = (GridCoverage2D) Operations.DEFAULT.resample(scicov,targetCRS);

Wow, this is getting fun!

This georeferencing transformation works backwards from what one might naively expect and from what happens with vector data. With vector data, tranformations take the positions in the original referencing system and calculate the positions in the target referencing system. In the GridCoverage system, we are expecting a regular grid in the target referencing system. Therefore, we first calculate the position of the grid points in the target referencing system, then, for each, we calculate the equivalent position in the original referencing system and estimate the value which the original grid would have at that calculated position. This is why we go through the resample(..) operation.

interpolate
'''''''''''

Similarly, we can interpolate an image using the Operations class. In this case we need to pick the interpolation method we will use.::
  
  javax.media.jai.Interpolation interp = Interpolation.getInstance(Interpolation.INTERP_BILINEAR);
  GridCoverage2D covinterpol =  (GridCoverage2D) Operations.DEFAULT.interpolate(scicov, interp);
  RenderedImage ri = covinterpol.getRenderedImage();

crop
''''

The Crop operation provides a way to crop (cut) a GridCoverage in order to obtain a sub-area defined by an Envelope.::
  
  final AbstractProcessor processor = new DefaultProcessor(null);
  
  final ParameterValueGroup param = processor.getOperation("CoverageCrop").getParameters();
  
  GridCoverage2D coverage = ...{get a coverage from somewhere}...;
  final GeneralEnvelope crop = new GeneralEnvelope( ... );
  param.parameter("Source").setValue( coverage );
  param.parameter("Envelope").setValue( crop );
  
  GridCoverage2D cropped = (GridCoverage2D) processor.doOperation(param);

Available Operations
''''''''''''''''''''

The package summary javadoc should contain the full list of available operations. Unfortunately, the system is not automatic so it is possible to create new operations without documenting them.

The full list of available operations can be obtained from the library with::
  
  final DefaultProcessor proc = new DefaultProcessor(null);
  for (Operation o : proc.getOperations() ){
      System.out.println(o.getName());
      System.out.println(o.getDescription());
      System.out.println();
  }

Creating new Operations
'''''''''''''''''''''''

Users can create their own operations and add them to the operation system.

CoverageStack
^^^^^^^^^^^^^

We can combine several GridCoverage2D instances into a 3 dimensional stack of coverages using the CoverageStack class.

Utility classes
^^^^^^^^^^^^^^^

GeoTools coverage module also provides some utility code.

SpatioTemporalCoverage
^^^^^^^^^^^^^^^^^^^^^^

This is a wrapper around another coverage which allows us to call the .evaluate(..) method with coordinate and time values rather than forcing us to create the appropriate DirectPosition argument.

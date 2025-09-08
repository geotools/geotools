.. _imagen:
.. _jaiext:

***********************
Eclipse ImageN Tutorial
***********************

Introduction
=============

From GeoTools 34.x Eclipse ImageN is used, integrating the prevous JAI and JAI-Ext projects into a unified library.

Previously GeoTools 14.x used JAI library, and `JAI-EXT <https://github.com/geosolutions-it/jai-ext>`_ extension.
The ``JAI-EXT`` extension offers a fast, high-scalability API for image processing. The main feature of this API
is the ability to support external ``ROI`` objects and Image ``NoData`` for most of its processing operations.

This page has been written to describe the use of these operators inside GeoTools, to demonstrate use, and to explain
best practices.

Usage
=======

Registration
------------

GeoTools depends on the Eclipse ImageN project, both the core engine and the individual operators required.

OperationDescriptors are registered automatically, being discovered on the classpath. 

.. note:: ``OperationDescriptor`` is a class describing the parameters to set for executing a ImageN operation.

Eclipse ImageN provides both the newer operator implementations, available as individual java jars, and legacy implementations from the historical JAI Project. If you require the use of a legacy operator, take care on how
you launch the operation:

#. Using a ``ParameterBlock`` instance, which does not provide the same checks present in the ``ParameterBlockJAI`` class which may lead to unexpected exceptions. Here is an example

	.. code-block:: java
	
		// Inputs
		RenderedImage img; // Example image (256x256)
		double[] scales = new double[]{2.0d};
		double[] offsets = new double[]{1.0d};
		// Input NoData, for example 128. It must be passed as a JAI-EXT "Range"
		Range nodata = RangeFactory.create(128d, 128d);
		// Input ROI
		ROI roi = new ROIShape(new Rectangle(0, 0, 128, 128));
		// Optional input RenderingHints
		RenderingHints hints;
		
		// Creation of the ParameterBlock
		ParameterBlock pb = new ParameterBlock();
		// Setting the parameters
		pb.setSource(img, 0); // The source image.
		pb.set(scales, 0);    // The per-band constants to multiply by.
		pb.set(offsets, 1);   // The per-band offsets to be added.
		pb.set(roi, 2);       // ROI
		pb.set(nodata, 3);    // NoData range
		// Calling the operation
		RenderedOp result = JAI.create("Rescale", pb, hints);


#. Call the related GeoTools ``ImageWorker`` method, if present:

	.. code-block:: java
	
		// Same inputs as above
		// Instantiation of the ImageWorker
		ImageWorker w = new ImageWorker(img);
		// Setting RenderingHints
		w.setRenderingHints(img);
		// Setting ROI and NoData
		w.setROI(roi);
		w.setNoData(nodata);
		
		// Executing the operation
		w.rescale(scales, offsets);
		
		// Getting the result
		RenderedOp result = w.getRenderedImage();


GeoTools registration
----------------------

The majority of the GeoTools operations are internally bound to the ImageN operations.

Use a ``CoverageProcessor`` instance for getting a GeoTools coverage *operation*. It is better to get a new ``CoverageProcessor`` instance by using the static factory method ``CoverageProcessor.getInstance()`` since this method allows caching of ``CoverageProcessor`` instances and their reuse if needed.

When an ``OperationDescriptor`` is replaced, users should take care to remove the existing associated GeoTools operation from all the ``CoverageProcessor`` instances and then to insert it again. This procedure must be done in order to avoid having a GeoTools operation with an internal ``OperationDescriptor`` which has been replaced; this situation may lead to wrong parameter initialization which could then lead to exceptions during Coverage processing. 

The procedure is described below.
	
.. code-block:: java

        CoverageProcessor.removeOperationFromProcessors("Warp"); // Removal of the operation from the processors
        CoverageProcessor.updateProcessors(); // Update of all the processors with the new operation

Best Practice
--------------

Below is a simple piece of code for how to handle ``NoData`` for a ``GridCoverage``.

	.. code-block:: java
	
		// Creation of a new GridCoverage2D from a RenderedImage
		RenderedImage img; // Example image (256x256)
		// Coverage CRS
		CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");
		// Coverage Envelope
		Envelope envelope = new ReferencedEnvelope(-180, 180, -90, 90, crs);
		// Coverage Properties
		Map<String, Object> properties = new HashMap<String, Object>();
		// NoData definition
		double nodata = -9999;
		// Wrapping NoData inside a container
		NoDataContainer container = new NoDataContainer(nodata);
		// Setting NoData as property
		CoverageUtilities.setNoDataProperty(properties, container);
		
		// Setting ROI as property
		ROI roi = new ROIShape(new Rectangle(0, 0, 128, 128));
		CoverageUtilities.setROIProperty(properties, roi);

		// Creating the GridCoverage
		GridCoverageFactory factory = new GridCoverageFactory();
		GridCoverage2D coverage = factory.create("Test", img, envelope);

		// Retrieving NoData from the GridCoverage
		NoDataContainer newContainer = CoverageUtilities.getNoDataProperty(coverage);
		
		// Retrieving ROI from GridCoverage
		ROI newROI = CoverageUtilities.getROIProperty(coverage);

It should be noted that ``NoData`` is always returned as *NoDataContainer* instance. This class provides useful methods for accessing ``NoData`` as array, single value or ``Range``. In the following code shows how to change the ``NoData`` value after executing a single operation.

	.. code-block:: java
	
		// Getting CoverageProcessor
		CoverageProcessor processor = CoverageProcessor.getInstance();
		
		// Getting Scale operation
		Operation scale = processor.getOperation("Scale");
		// Getting scale parameters
		ParameterValueGroup params = processor.getParameters();
		params.parameter("Source0").setValue(coverage);
		// Setting the Background. The first value will be taken as NoData if a NoData was already present in input
		params.parameter("backgroundValues").setValue(new double[]{100});
		
		// Executing the operation
		GridCoverage2D result = (GridCoverage2D) processor.doOperation(params);
		// Getting the new NoData value
		NoDataContainer newNoDataContainer = CoverageUtilities.getNoDataProperty(result); // it should have 100 as NoData

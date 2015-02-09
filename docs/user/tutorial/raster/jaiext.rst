.. _jaiext:

*****************
JAI-EXT Tutorial
*****************

Introduction
=============

From GeoTools 14.x a new JAI extension API has been integrated, this new API is called `JAI-EXT <https://github.com/geosolutions-it/jai-ext>`_.

**JAI-EXT** is an open-source Project which provides a fast, high-scalable API for image processing. The main feature of this API is the ability to
support external **ROI** objects and Image **NoData** for most of its processing operations.

This page has been written for describing how to use this new API inside GeoTools and which are the best practices.

Usage
=======

JAI-EXT registration
---------------------

A project which would like to use JAI-EXT operations should take care of their registration. This may be accomplished by calling, at the top of your project, the following piece of code:

.. code-block:: java

	static{
		JAIExt.initJAIEXT();
	}

This method allows to register all the JAI-EXT operations inside the JAI *OperationRegistry* in order to use them instead of old JAI operations. 

.. note:: It should be pointed out that if this method is called more than one times, it has no effect since it is only an initialization method.

Of course, if a user would like to come back to the JAI operations, this can be done as described below::

	JAIExt.registerJAIDescriptor("Warp") --> Replace the JAI-EXT "Warp" operation with the JAI one 
	
	JAIExt.registerJAIEXTDescriptor("Warp") --> Replace the JAI "Warp" operation with the JAI-EXT one

Such methods allows to replace the *OperationDescriptor* associated to an operation from JAI to JAI-EXT and vice-versa.

.. note:: *OperationDescriptor* is a class describing the parameters to set for executing a JAI/JAI-EXT operation.

In order to avoid exceptions when replacing the *OperationDescriptor* associated to a JAI/JAI-EXT operation, users should take care on how to launch a JAI/JAI-EXT operation:

#. Using a *ParameterBlock* instance, which does not provide the same checks present in the *ParameterBlockJAI* class which may lead to unexpected exceptions. Here is an example

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


#. Call the related GeoTools *ImageWorker* method, if present:

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
		
.. note:: The main aim of JAI-EXT project is to completely replace all the JAI framework. In this temporary phase users may notice a few error messages on startup similar to this one:
	
	.. code-block:: bash
		
		Error in registry file at line number #5
		A descriptor is already registered against the name "OrderedDither" under registry mode "rendered"
	
	This errors are reported by JAI at low level and will be removed when JAI will be totally replaced.
		
GeoTools registration
----------------------

Since the majority of the **GeoTools** operations are internally bound to the **JAI** operations, users must take care on how to handle them with **JAI-EXT**. 

The first suggestion is to always use a **CoverageProcessor** instance for getting a GeoTools coverage *operation*. It should be better to get a new *CoverageProcessor* instance by using the static factory method **CoverageProcessor.getInstance()** since this method allows to cache the various *CoverageProcessor* instances and reuse them if needed.

When an *OperationDescriptor* is replaced, users should take care of removing the existing associated GeoTools operation from all the *CoverageProcessor* instances and then to insert it again. This procedure must be done because it avoids to have a GeoTools operation with an internal *OperationDescriptor* which has been replaced. This situation may lead to wrong parameter initialization which then could lead to exceptions during Coverage processing. 

The procedure is described below::
	
	CoverageProcessor.removeOperationFromProcessors("Warp"); --> Removal of the operation from the processors
	
	CoverageProcessor.updateProcessors(); --> Update of all the processors with the new operation

Best Practice
--------------

Below is described a simple piece of code for how to handle NoData for a GridCoverage.

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

It should be noticed that NoData is always returned as *NoDataContainer* instance. This class provides useful methods for accessing NoData as array, single value or *Range*. In the next piece of code will be described how to change NoData value after executing a single operation.

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
		
.. warning::
	
	Since *GTCrop* operation has been moved to the JAI-EXT project, users should take care that replacing JAI-EXT Crop with JAI one will result in a loss of all the fixes provided by *GTCrop*.  

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2007 TOPP - www.openplans.org.
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.process.raster;

import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.RenderedOp;
import javax.media.jai.operator.BandSelectDescriptor;

import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.process.gs.GSProcess;

import org.geotools.coverage.Category;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.image.jai.Registry;
import org.geotools.process.ProcessException;
import org.geotools.renderer.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.image.ColorUtilities;
import org.geotools.util.logging.Logging;
import org.jaitools.media.jai.rangelookup.RangeLookupDescriptor;
import org.jaitools.media.jai.rangelookup.RangeLookupRIF;
import org.jaitools.media.jai.rangelookup.RangeLookupTable;
import org.jaitools.numeric.Range;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.util.ProgressListener;

/**
 * A raster reclassified process
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 * @author Emanuele Tajariol (GeoSolutions)
 * @author Simone Giannecchini (GeoSolutions)
 * @author Andrea Aime - GeoSolutions
 * @author Daniele Romagnoli - GeoSolutions
 *
 * @source $URL$
 */
@DescribeProcess(title = "Reclassify", description = "Reclassifies a continous raster into integer values defined by a set of ranges")
public class RangeLookupProcess implements GSProcess {
	
    private final static double DEFAULT_NODATA = 0d;
	
	private final static Logger LOGGER = Logging.getLogger(RangeLookupProcess.class);
	
	static {
        Registry.registerRIF(JAI.getDefaultInstance(), new RangeLookupDescriptor(), new RangeLookupRIF(), Registry.JAI_TOOLS_PRODUCT);
    }

    @DescribeResult(name = "reclassified", description = "The reclassified raster")
    public GridCoverage2D execute(
            @DescribeParameter(name = "coverage", description = "Input raster") GridCoverage2D coverage,
            @DescribeParameter(name = "band", description = "Source band to use for classification (default is 0)", min = 0) Integer classificationBand,
            @DescribeParameter(name = "ranges", description = "Specifier for a value range in the format ( START ; END ).  START and END values are optional. [ and ] can also be used as brackets, to indicate inclusion of the relevant range endpoint.", 
            collectionType=Range.class) List<Range> classificationRanges,
            @DescribeParameter(name = "outputPixelValues", description = "Value to be assigned to corresponding range", min = 0 ) int[] outputPixelValues,
            @DescribeParameter(name = "noData", description = "Value to be assigned to pixels outside any range (defaults to 0)", min = 0 ) Double noData,
            ProgressListener listener) throws ProcessException {
    	
    	//
    	// initial checks
    	//
    	if(coverage==null){
    		throw new ProcessException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1,"coverage"));
    	}
    	if(classificationRanges==null){
    		throw new ProcessException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1,"classificationRanges"));
    	}
    	double nd = DEFAULT_NODATA;
    	if (noData != null){
    	    nd = noData.doubleValue();
    	}
    	if (outputPixelValues != null && outputPixelValues.length > 0){
    	    final int ranges = classificationRanges.size();
    	    if (ranges != outputPixelValues.length){
    	        throw new ProcessException(Errors.format(ErrorKeys.MISMATCHED_ARRAY_LENGTH, "outputPixelValues"));
    	    }
    	}

        RenderedImage sourceImage = coverage.getRenderedImage();
    	
        // parse the band
        if (classificationBand != null) {
            final int band = classificationBand;
            final int numbands=sourceImage.getSampleModel().getNumBands();
            if(band<0 || numbands<=band){
            	throw new ProcessException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2,"band",band));
            }
            
            if(band==0 && numbands>0 || band>0)
            	sourceImage=BandSelectDescriptor.create(sourceImage, new int []{band}, null);
        }


        //
        // Check the number of ranges we have in order to decide which type we can use for the output values. 
        // Our goal is to use the smallest possible data type that can hold the image values.
        //
        
        // Builds the range lookup table
        final RangeLookupTable lookupTable;
        final int size=classificationRanges.size();
        switch (ColorUtilities.getTransferType(size)) {
		case DataBuffer.TYPE_BYTE:
			lookupTable = CoverageUtilities.getRangeLookupTable(classificationRanges, outputPixelValues, (byte) nd );
			break;
		case DataBuffer.TYPE_USHORT:
			lookupTable = CoverageUtilities.getRangeLookupTable(classificationRanges, outputPixelValues, (short) nd );
			break;
		case DataBuffer.TYPE_INT:
			lookupTable = CoverageUtilities.getRangeLookupTable(classificationRanges, outputPixelValues, nd );
			break;			
		default:
			throw new IllegalArgumentException(org.geotools.resources.i18n.Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2,
			        "classification ranges size",size));
		}
        // reclassify the source image
        ParameterBlockJAI pb = new ParameterBlockJAI("RangeLookup");
        pb.setSource("source0", sourceImage);
        pb.setParameter("table", lookupTable);
        final RenderedOp indexedClassification = JAI.create("RangeLookup", pb);

        
        //
        // build the output coverage
        //
        
        
        // build the output sample dimensions, use the default value ( 0 ) as the no data
        final GridSampleDimension outSampleDimension = new GridSampleDimension("classification",
                new Category[] { Category.NODATA }, null).geophysics(true);
        final GridCoverageFactory factory = CoverageFactoryFinder.getGridCoverageFactory(null);
        final GridCoverage2D output = factory.create("reclassified", indexedClassification, coverage
                .getGridGeometry(), new GridSampleDimension[] { outSampleDimension },
                new GridCoverage[] { coverage }, new HashMap<String,Double>(){{
                	put("GC_NODATA",0d);
                }});
        return output;
    }
    
    
    /**
     * Execute the RangeLookupProcess on the provided coverage (left for backwards compatibility)
     * 
     * @param coverage The continuous coverage to be reclassified
     * @param classificationBand The band to be used for classification
     * @param classificationRanges The list of ranges to be applied
     * @param listener The progress listener
     * @return The reclassified coverage
     * @throws ProcessException
     */
    public GridCoverage2D execute(GridCoverage2D coverage, Integer classificationBand,
            List<Range> classificationRanges, ProgressListener listener) throws ProcessException {
        return execute(coverage, classificationBand, classificationRanges, null, 0d, listener);
    }

}

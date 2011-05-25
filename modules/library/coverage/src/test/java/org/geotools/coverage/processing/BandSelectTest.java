/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.processing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;

import javax.media.jai.PlanarImage;

import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.TypeMap;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.ViewType;
import org.geotools.coverage.grid.Viewer;
import org.geotools.image.ImageWorker;
import org.junit.Test;
import org.opengis.coverage.ColorInterpretation;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.TransformException;


/**
 * Tests the SelectSampleDimension operation.
 *
 *
 * @source $URL$
 * @author Simone Giannecchini (GeoSolutions)
 *
 * @since 2.5
 */
@SuppressWarnings("deprecation")
public final class BandSelectTest extends GridProcessingTestBase {

    /**
     * Tests the "SelectSampleDimension" operation in a simple way.
     *
     */
    @Test
    public void testBandSelectSimple() {
        final CoverageProcessor processor = CoverageProcessor.getInstance();
        /*
         * Get the source coverage and build the cropped envelope.
         */
        final GridCoverage2D source = EXAMPLES.get(2).view(ViewType.NATIVE);
        final Envelope envelope = source.getEnvelope();
        final RenderedImage rgbImage= new ImageWorker(source.getRenderedImage()).forceComponentColorModel().getRenderedImage();
        final GridCoverage2D newCoverage=CoverageFactoryFinder.getGridCoverageFactory(null).create("sample", rgbImage, envelope);
        assertTrue(newCoverage.getNumSampleDimensions()==3);
        
        /*
         * Do the crop without conserving the envelope.
         */
        ParameterValueGroup param = processor.getOperation("SelectSampleDimension").getParameters();
        param.parameter("Source").setValue(newCoverage);
        param.parameter("SampleDimensions").setValue(new int[]{2});
        GridCoverage2D singleBanded = (GridCoverage2D) processor.doOperation(param);
        if (SHOW) {
        	Viewer.show(source);
            Viewer.show(singleBanded);
        } else {
            // Force computation
            assertNotNull(PlanarImage.wrapRenderedImage(singleBanded.getRenderedImage()).getTiles());
        }
        RenderedImage raster = singleBanded.getRenderedImage();
        assertEquals(1, raster.getSampleModel().getNumBands());
        assertNotNull(raster.getColorModel());

    }
    
    
    /**
     * Tests the "SelectSampleDimension" operation in a more complex way.
     *
     */
    @Test
    public void testBandSelect() throws TransformException {
        final CoverageProcessor processor = CoverageProcessor.getInstance();
        /*
         * Get the source coverage and build the cropped envelope.
         */
        final GridCoverage2D source = EXAMPLES.get(2).view(ViewType.NATIVE);
        final Envelope envelope = source.getEnvelope();
        final RenderedImage rgbImage= new ImageWorker(source.getRenderedImage()).forceComponentColorModel().getRenderedImage();
        final SampleModel sm=rgbImage.getSampleModel();
        final ColorModel cm=rgbImage.getColorModel();
		final int numBands = sm.getNumBands();
		final GridSampleDimension[] bands = new GridSampleDimension[numBands];
		// setting bands names.
		for (int i = 0; i < numBands; i++) {
		        final ColorInterpretation colorInterpretation=TypeMap.getColorInterpretation(cm, i);
		        if(colorInterpretation==null)
		               assertFalse("Unrecognized sample dimension type",true);
			bands[i] = new GridSampleDimension(colorInterpretation.name()).geophysics(true);
		}        
        final GridCoverage2D newCoverage=
        	CoverageFactoryFinder.getGridCoverageFactory(null).create(
        			"sample",
        			rgbImage, 
        			envelope,
        			bands,
        			null,
        			null);
        assertTrue(newCoverage.getNumSampleDimensions()==3);
        
        /*
         * Do the crop without conserving the envelope.
         */
        ParameterValueGroup param = processor.getOperation("SelectSampleDimension").getParameters();
        param.parameter("Source").setValue(newCoverage);
        param.parameter("SampleDimensions").setValue(new int[]{0});
        GridCoverage2D singleBanded = (GridCoverage2D) processor.doOperation(param);
        if (SHOW) {
        	Viewer.show(source);
            Viewer.show(singleBanded);
        } else {
            // Force computation
            assertNotNull(PlanarImage.wrapRenderedImage(singleBanded.getRenderedImage()).getTiles());
        }
        RenderedImage raster = singleBanded.getRenderedImage();
        assertEquals(1, raster.getSampleModel().getNumBands());
        assertNotNull(raster.getColorModel());
        
        
        /*
         * Do the crop without conserving the envelope.
         */
        param = processor.getOperation("SelectSampleDimension").getParameters();
        param.parameter("Source").setValue(newCoverage);
        param.parameter("SampleDimensions").setValue(new int[]{2});
        singleBanded = (GridCoverage2D) processor.doOperation(param);
        if (SHOW) {
        	Viewer.show(source);
            Viewer.show(singleBanded);
        } else {
            // Force computation
            assertNotNull(PlanarImage.wrapRenderedImage(singleBanded.getRenderedImage()).getTiles());
        }
        raster = singleBanded.getRenderedImage();
        assertEquals(1, raster.getSampleModel().getNumBands());
        assertNotNull(raster.getColorModel());        


    }    
}

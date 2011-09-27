/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.gce.imagecollection;

import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.FilenameUtils;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.data.DataSourceException;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.factory.epsg.CartesianAuthorityFactory;
import org.geotools.test.TestData;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.NoSuchAuthorityCodeException;

/**
 * @author Daniele Romagnoli, GeoSolutions
 * @author Andrea Aime, GeoSolutions
 * @author Simone Giannecchini, GeoSolutions
 *
 *
 *
 * @source $URL$
 */
public class ImageCollectionReaderTest extends Assert {

    @Test
    public void testReader() throws IllegalArgumentException, IOException,
            NoSuchAuthorityCodeException, CQLException {

        final File file = TestData.file(this, "sample");
        final String string = "PATH='folder1/world.tif'";
        Filter filter = CQL.toFilter(string);
        
        final ImageCollectionReader reader = new ImageCollectionReader(file);
        final ParameterValue<GridGeometry2D> gg =  AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        final GeneralEnvelope envelope = new GeneralEnvelope(new Rectangle(1000,-800,1000,400));
        envelope.setCoordinateReferenceSystem(CartesianAuthorityFactory.GENERIC_2D);
        final Rectangle rasterArea = new Rectangle(0, 0, 500, 200);
        final GridEnvelope2D range= new GridEnvelope2D(rasterArea);
        gg.setValue(new GridGeometry2D(range, envelope));
        
        final ParameterValue<Filter> ff =  ImageCollectionFormat.FILTER.createValue();
        ff.setValue(filter);
        
        final ParameterValue<double[]> background =  ImageCollectionFormat.BACKGROUND_VALUES.createValue();
        background.setValue(new double[]{0});
        
        GeneralParameterValue[] params = new GeneralParameterValue[] {ff, gg, background};        
        if (reader != null) {
            // reading the coverage
            GridCoverage2D coverage = (GridCoverage2D) reader.read(params);
            RenderedImage image = coverage.getRenderedImage();
            assertTrue(image.getWidth() == 500);
            assertTrue(image.getHeight() == 200);
        }
    }
    
    @Test
    public void testForbiddenPath() throws IllegalArgumentException, IOException,
            NoSuchAuthorityCodeException, CQLException {

        final File file = TestData.file(this, "sample");
        final String absolutePath = FilenameUtils.separatorsToUnix(file.getAbsolutePath());
        final String string = "PATH='" + absolutePath + "/folder1/../../forbiddenFolder/classifiedFile.tif'";
        Filter filter = CQL.toFilter(string);
        
        final ImageCollectionReader reader = new ImageCollectionReader(file);
        final ParameterValue<Filter> ff =  ImageCollectionFormat.FILTER.createValue();
        ff.setValue(filter);
        
        GeneralParameterValue[] params = new GeneralParameterValue[] {ff};        
        if (reader != null) {
            // reading the coverage
        	GridCoverage2D coverage = null;
        	boolean exception = true;
        	try {
        		coverage = (GridCoverage2D) reader.read(params);
        		exception = false;
        	} catch (DataSourceException exc){
        		assertTrue(exception);
        	}
        }
    }
}

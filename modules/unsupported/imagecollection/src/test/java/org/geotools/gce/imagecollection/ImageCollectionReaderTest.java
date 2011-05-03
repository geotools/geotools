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
import java.io.File;
import java.io.IOException;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.test.TestData;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.NoSuchAuthorityCodeException;

/**
 */
public class ImageCollectionReaderTest {

    @Test
    public void emptyTest(){
        
    }
    
    @Ignore
    public void testReader() throws IllegalArgumentException, IOException,
            NoSuchAuthorityCodeException, CQLException {

        final File file = null;//new File("D:\\data\\dynamic");
        // getting a reader
        final String string = "PATH='folder2/subfolder1/a.TIF'";
        Filter filter = CQL.toFilter(string);
        
        ImageCollectionReader reader = new ImageCollectionReader(file);
        final ParameterValue<GridGeometry2D> gg =  AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        final GeneralEnvelope envelope = new GeneralEnvelope(new Rectangle(3000,3000,4000,4000));
        envelope.setCoordinateReferenceSystem(DefaultEngineeringCRS.CARTESIAN_2D);
        final Rectangle rasterArea = new Rectangle(3000, 3000, 500, 500);
        final GridEnvelope2D range= new GridEnvelope2D(rasterArea);
        gg.setValue(new GridGeometry2D(range,envelope));
        
        final ParameterValue<Filter> ff =  ImageCollectionFormat.FILTER.createValue();
        ff.setValue(filter);
        
        final ParameterValue<double[]> background =  ImageCollectionFormat.BACKGROUND_VALUES.createValue();
        background.setValue(new double[]{0});
        
        GeneralParameterValue[] params = new GeneralParameterValue[] {ff, gg, background};        
        if (reader != null) {

            // reading the coverage
            GridCoverage2D coverage = (GridCoverage2D) reader.read(params);

            if (TestData.isInteractiveTest())
                coverage.show();
            else
                coverage.getRenderedImage().getData();

        }

    }
}

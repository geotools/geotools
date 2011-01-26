/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.grid.oblong;

import java.util.Map;

import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.grid.DefaultFeatureBuilder;
import org.geotools.grid.GridFeatureBuilder;
import org.geotools.grid.GridElement;
import org.geotools.grid.TestBase;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;

import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the Oblongs class.
 *
 * @author mbedward
 * @since 2.7
 * @source $URL$
 * @version $Id$
 */
public class OblongsTest extends TestBase {

    @Test
    public void create() {
        GridElement oblong = Oblongs.create(1, 2, 3, 4, null);
        assertNotNull(oblong);
        assertEnvelope(new ReferencedEnvelope(1, 4, 2, 6, null), oblong.getBounds());
    }

    @Test(expected = IllegalArgumentException.class)
    public void badWidth() {
        Oblongs.create(1, 2, -1, 4, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void badHeight() {
        Oblongs.create(1, 2, 3, -1, null);
    }

    @Test
    public void createGrid() throws Exception {
        final SimpleFeatureType TYPE = DataUtilities.createType("obtype", "oblong:Polygon,id:Integer");

        final double SPAN = 100;
        final ReferencedEnvelope bounds = new ReferencedEnvelope(0, SPAN, 0, SPAN, null);

        class Setter extends GridFeatureBuilder {
            int id = 0;

            public Setter(SimpleFeatureType type) {
                super(type);
            }

            @Override
            public void setAttributes(GridElement el, Map<String, Object> attributes) {
                attributes.put("id", ++id);
            }
        }

        Setter setter = new Setter(TYPE);

        final double WIDTH = 5.0;
        final double HEIGHT = 10.0;
        SimpleFeatureSource gridSource = Oblongs.createGrid(bounds, WIDTH, HEIGHT, setter);
        assertNotNull(gridSource);

        int expectedCols = (int) (SPAN / WIDTH);
        int expectedRows = (int) (SPAN / HEIGHT);

        assertEquals(expectedCols * expectedRows, setter.id);
        assertEquals(setter.id, gridSource.getFeatures().size());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void createGrid_InvalidBounds() {
        Oblongs.createGrid(ReferencedEnvelope.EVERYTHING, 1.0, 1.0, new DefaultFeatureBuilder());
    }

    @Test(expected=IllegalArgumentException.class)
    public void createGrid_NullBounds() {
        Oblongs.createGrid(null, 1.0, 1.0, new DefaultFeatureBuilder());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void createGrid_badWidth() {
        Oblongs.createGrid(new ReferencedEnvelope(0, 10, 0, 10, null), 0, 1.0, new DefaultFeatureBuilder());
    }

    @Test(expected=IllegalArgumentException.class)
    public void createGrid_badHeight() {
        Oblongs.createGrid(new ReferencedEnvelope(0, 10, 0, 10, null), 1, 0, new DefaultFeatureBuilder());
    }

    @Test(expected=IllegalArgumentException.class)
    public void createGrid_MisMatchedCRS() {
        try {
        ReferencedEnvelope env = new ReferencedEnvelope(0, 10, 0, 10, DefaultGeographicCRS.WGS84);
        CoordinateReferenceSystem otherCRS = CRS.parseWKT(getSydneyWKT());
        GridFeatureBuilder builder = new DefaultFeatureBuilder(otherCRS);

        Oblongs.createGrid(env, 0, 1.0, builder);
        
        } catch (FactoryException ex) {
            throw new IllegalStateException("Error in test code");
        } catch (MismatchedDimensionException ex) {
            throw new IllegalStateException("Error in test code");
        }
    }

}

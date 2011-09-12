/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swing;

import java.awt.Rectangle;
import java.util.Arrays;

import com.vividsolutions.jts.geom.Polygon;

import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.testutils.WaitingMapPaneListener;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Base class for map pane tests.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/swing/src/test/java/org/geotools/swing/JMapPaneGraphicsTest.java $
 * @version $Id: JMapPaneGraphicsTest.java 37777 2011-08-03 13:19:58Z mbedward $
 */
public abstract class JMapPaneTestBase {

    protected static final int HEIGHT = 150;
    protected static final int WIDTH = 200;
    protected static final double TOL = 1.0E-6;
    protected static final long WAIT_TIMEOUT = 1000;
    
    protected WaitingMapPaneListener listener;
    protected JMapPane mapPane;

    /**
     * Creates a new, empty MapContent with the bounds set for
     * the viewport to match the aspect ratio of the map pane.
     *
     * @return new map content
     */
    protected MapContent createMapContentWithBoundsSet() {
        MapContent mapContent = new MapContent();
        mapContent.getViewport().setBounds(createMatchedBounds());
        return mapContent;
    }

    /**
     * Creates a ReferencedEnvelope with the same aspect ratio as the
     * map pane.
     *
     * @return new envelope
     */
    protected ReferencedEnvelope createMatchedBounds() {
        Rectangle r0 = mapPane.getVisibleRect();
        return new ReferencedEnvelope(0, (double) r0.width / r0.height, 0, 1.0, DefaultEngineeringCRS.CARTESIAN_2D);
    }

    /**
     * Creates a new MapContent optionally populated with single-feature Layers
     * having the specified bounds.
     * 
     * @param boundsOfLayers 0 or more bounds for layers
     * @return new map content
     */
    protected MapContent createMapContent(ReferencedEnvelope ...boundsOfLayers) {
        MapContent mapContent = new MapContent();
        if (boundsOfLayers != null) {
            for (ReferencedEnvelope env : boundsOfLayers) {
                mapContent.addLayer(createLayer(env));
            }
        }
        return mapContent;
    }

    /**
     * Creates a new feature layer.
     * 
     * @param env layer bounds
     * @return the new layer
     */
    protected Layer createLayer(ReferencedEnvelope env) {
        SimpleFeatureCollection fc = singlePolygonFeatureCollection(env);
        Style style = SLD.createSimpleStyle(fc.getSchema());
        return new FeatureLayer(fc, style);
    }

    /**
     * Creates a feature collection containing a single feature with a
     * polygon geometry based on the input envelope.
     *
     * @param env the input envelope
     * @return new feature collection
     */
    protected SimpleFeatureCollection singlePolygonFeatureCollection(ReferencedEnvelope env) {
        if (env == null || env.isEmpty()) {
            throw new IllegalArgumentException("env must not be null or empty");
        }

        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.setName("rectangle");
        typeBuilder.add("shape", Polygon.class, env.getCoordinateReferenceSystem());
        typeBuilder.add("label", String.class);
        final SimpleFeatureType TYPE = typeBuilder.buildFeatureType();

        SimpleFeature feature = SimpleFeatureBuilder.build(
                TYPE, new Object[]{JTS.toGeometry(env), "a rectangle"}, null);

        SimpleFeatureCollection fc = new ListFeatureCollection(TYPE, Arrays.asList(feature));
        ReferencedEnvelope bounds = fc.getBounds();
        return fc;
    }

}

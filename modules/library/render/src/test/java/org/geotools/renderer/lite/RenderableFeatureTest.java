/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.renderer.lite;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.awt.geom.AffineTransform;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.Property;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.feature.type.Name;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.style.Symbolizer;
import org.geotools.feature.NameImpl;
import org.geotools.geometry.jts.LiteShape2;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.Mockito;

public class RenderableFeatureTest {

    @Test
    public void testNoDefGeometryComplexFeature() throws FactoryException {
        // tests that a RenderableFeature doesn't thrown NPE when finding a geometry
        // of a Complex feature if the Symbolizer has null geom attribute and the
        // default geom of the Feature is null.
        String ns = "namespace";
        String gName = "geom";
        Name name = new NameImpl(ns, gName);
        Property property = Mockito.mock(Property.class);
        Point point = new GeometryFactory().createPoint();
        when(property.getValue()).thenReturn(point);
        Feature feature = Mockito.mock(Feature.class);
        FeatureType type = Mockito.mock(FeatureType.class);
        GeometryDescriptor descriptor = Mockito.mock(GeometryDescriptor.class);
        when(descriptor.getName()).thenReturn(name);
        when(feature.getDefaultGeometryProperty()).thenReturn(null);
        when(type.getGeometryDescriptor()).thenReturn(descriptor);
        when(feature.getProperty(name)).thenReturn(property);
        when(feature.getType()).thenReturn(type);
        Symbolizer symbolizer = Mockito.mock(Symbolizer.class);
        when(symbolizer.getGeometry()).thenReturn(null);
        StreamingRenderer.RenderableFeature rf =
                new StreamingRenderer().new RenderableFeature("layerId", false) {
                    @Override
                    public LiteShape2 getShape(
                            Symbolizer symbolizer, AffineTransform at, Geometry g, boolean clone)
                            throws FactoryException {
                        assertEquals(point, g);
                        return null;
                    }
                };
        rf.setFeature(feature);
        rf.getShape(symbolizer, null);
    }
}

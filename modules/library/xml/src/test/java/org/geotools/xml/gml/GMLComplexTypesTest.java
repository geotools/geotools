/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009-2016, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.xml.gml;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.Map;
import javax.naming.OperationNotSupportedException;
import org.apache.commons.collections.map.HashedMap;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.GeometryBuilder;
import org.geotools.xml.PrintHandler;
import org.geotools.xml.gml.GMLSchema.GMLComplexType;
import org.geotools.xml.schema.Element;
import org.geotools.xml.xsi.XSISimpleTypes;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.mockito.Mockito;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class GMLComplexTypesTest {

    @Test
    public void whenEncodePointInPlaceOfLineStringThenThrowOperationNotSupportedException()
            throws Exception {
        GMLComplexType instance = GMLComplexTypes.LineStringPropertyType.getInstance();
        Point value = mock(Point.class);
        PrintHandler printHandler = mock(PrintHandler.class);
        try {
            instance.encode(null, value, printHandler, null);
            Assert.fail();
        } catch (OperationNotSupportedException e) {
        }
    }

    @Test
    public void whenEncodeLineStringWithoutElementThenEncodeWithDefaultElement() throws Exception {
        GMLComplexType instance = GMLComplexTypes.LineStringPropertyType.getInstance();
        LineString value = mock(LineString.class);
        when(value.getNumPoints()).thenReturn(2);
        PrintHandler printHandler = mock(PrintHandler.class);
        try {
            instance.encode(null, value, printHandler, null);
            verify(printHandler).startElement(GMLSchema.NAMESPACE, "lineStringProperty", null);
            verify(printHandler).endElement(GMLSchema.NAMESPACE, "lineStringProperty");
        } catch (OperationNotSupportedException e) {
            Assert.fail();
        }
    }

    @Test
    public void whenEncodeLineStringWithElementThenEncodeWithGivenElement() throws Exception {
        GMLComplexType instance = GMLComplexTypes.LineStringPropertyType.getInstance();
        LineString value = mock(LineString.class);
        when(value.getNumPoints()).thenReturn(2);
        PrintHandler printHandler = mock(PrintHandler.class);
        Element element = mock(Element.class);
        URI uri = new URI("test");
        when(element.getNamespace()).thenReturn(uri);
        String nameElement = "elementName";
        when(element.getName()).thenReturn(nameElement);
        try {
            instance.encode(element, value, printHandler, null);
            verify(printHandler).startElement(uri, nameElement, null);
            verify(printHandler).endElement(uri, nameElement);
        } catch (OperationNotSupportedException e) {
            Assert.fail();
        }
    }

    /**
     * Test for encoding of null values. No exception must occur if features containing a null
     * geometry are encoded.
     */
    @Test
    public void testEncodeFeatureWithNullAttributes() throws Exception {
        // given: Feature with null geometry
        GeometryBuilder geomBuilder = new GeometryBuilder();
        MultiPolygon multiPolygon =
                geomBuilder.multiPolygon(geomBuilder.polygon(0, 0, 0, 1, 1, 1, 0, 0));
        GMLComplexType instance = GMLComplexTypes.AbstractFeatureType.getInstance();
        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.setName("person");
        typeBuilder.add("mpolygon", MultiPolygon.class);
        typeBuilder.add("name", MultiPolygon.class);
        SimpleFeatureType type = typeBuilder.buildFeatureType();
        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(type);
        SimpleFeature lFeature = featureBuilder.buildFeature(null);

        Element element = mock(Element.class);
        Element element_mpolygon = mock(Element.class);
        Element element_name = mock(Element.class);
        when(element.getType()).thenReturn(GMLComplexTypes.AbstractFeatureType.getInstance());
        when(element.findChildElement(eq("mpolygon"))).thenReturn(element_mpolygon);
        when(element.findChildElement(eq("name"))).thenReturn(element_name);
        when(element_mpolygon.getType())
                .thenReturn(GMLComplexTypes.MultiPolygonPropertyType.getInstance());
        when(element_name.getType()).thenReturn(XSISimpleTypes.String.getInstance());

        Map<?, ?> hints = new HashedMap();
        PrintHandler printHandler = mock(PrintHandler.class);

        // when: encode is called to serialize empty feature
        instance.encode(element, lFeature, printHandler, hints);

        // then:
        // - no exception must occur
        // - start*() and end*() must be called once for feature itself and once for empty "name"
        // attr
        verify(printHandler, Mockito.times(2)).startElement(any(), any(), any());
        verify(printHandler, Mockito.times(2)).endElement(any(), any());

        // when: encode is called to serialize feature with "mpolygon" and "name"
        lFeature.setAttribute("name", "Pink Panther");
        lFeature.setAttribute("mpolygon", multiPolygon);
        printHandler = mock(PrintHandler.class);
        instance.encode(element, lFeature, printHandler, hints);

        // then:
        // - no exception must occur
        // - start*() and end*() must be called several times for feature, "name" and "mpolygon"
        verify(printHandler, Mockito.times(9)).startElement(any(), any(), any());
        verify(printHandler, Mockito.times(9)).endElement(any(), any());
    }
}

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml3.bindings;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFeature;
import org.eclipse.xsd.XSDNamedComponent;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.data.DataUtilities;
import org.geotools.gml2.bindings.GMLEncodingUtils;
import org.geotools.gml3.GML;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.SchemaIndex;
import org.geotools.xsd.Schemas;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;

/** Test GML 3.1 encoding utilities. Most of the work is delegate on {@link GMLEncodingUtils}. */
public class GML3EncodingUtilsTest {

    @Test
    public void testGeometriesGML3FeatureEncoding() throws Exception {
        // create a schema index for GML 3.1
        Configuration configuration = new GMLConfiguration();
        SchemaIndex index = Schemas.findSchemas(configuration);
        // create a simple feature type with multi lines and multi polygons
        SimpleFeatureType featureType =
                DataUtilities.createType(
                        "feature",
                        "geometry1:LineString,geometry2:MultiLineString,geometry3:Polygon,"
                                + "geometry4:MultiPolygon,geometry5:Point,geometry6:MultiPoint");
        // both GML 3.1 and GML 3.2 encoding utils delegate most of the work on the GML encoding
        // utils
        GMLEncodingUtils encoder = new GMLEncodingUtils(GML.getInstance());
        // create the XSD type definition for our feature type
        XSDTypeDefinition type =
                encoder.createXmlTypeFromFeatureType(featureType, index, Collections.emptySet());
        // get the XSD elements representing our geometries attributes
        List<XSDElementDeclaration> elements = Schemas.getChildElementDeclarations(type, false);
        assertThat(elements, notNullValue());
        assertThat(elements.size(), is(6));
        // extract the type names and ignore the NULL values
        List<String> typesNames =
                elements.stream()
                        .map(XSDFeature::getType)
                        .filter(elementType -> elementType != null)
                        .map(XSDNamedComponent::getName)
                        .collect(Collectors.toList());
        // check that our geometries have the correct type
        assertThat(typesNames.size(), is(6));
        assertThat(
                typesNames,
                hasItems(
                        "LineStringPropertyType",
                        "MultiLineStringPropertyType",
                        "PolygonPropertyType",
                        "MultiPolygonPropertyType",
                        "PointPropertyType",
                        "MultiPointPropertyType"));
    }
}

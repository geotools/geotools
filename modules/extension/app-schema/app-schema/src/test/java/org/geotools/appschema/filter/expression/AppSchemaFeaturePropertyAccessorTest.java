/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.appschema.filter.expression;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;
import org.geotools.data.complex.config.AppSchemaFeatureTypeRegistry;
import org.geotools.data.complex.expression.FeaturePropertyAccessorFactory;
import org.geotools.data.complex.feature.type.Types;
import org.geotools.data.complex.util.EmfComplexFeatureReader;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.test.AppSchemaTestSupport;
import org.geotools.util.factory.Hints;
import org.geotools.xsd.SchemaIndex;
import org.junit.Test;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * This is to demonstrate evaluating XPaths as attribute expressions when complex
 * attributes/features are passed in, instead of simple features. This is necessary since complex
 * features could contain nested properties, and we should be able to get properties of any level
 * from the features.
 *
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 */
public class AppSchemaFeaturePropertyAccessorTest extends AppSchemaTestSupport {

    private static final String GSMLNS = "http://www.cgi-iugs.org/xml/GeoSciML/2";
    private static final String XLINKNS = "http://www.w3.org/1999/xlink";

    private static final String schemaBase = "/test-data/";

    /** Gsml name space */
    static final NamespaceSupport GSMLNAMESPACES =
            new NamespaceSupport() {
                {
                    declarePrefix("gsml", GSMLNS);
                    declarePrefix("xlink", XLINKNS);
                }
            };

    /**
     * Load schema
     *
     * @param location schema location path that can be found through getClass().getResource()
     */
    private SchemaIndex loadSchema(final String location) throws IOException {
        EmfComplexFeatureReader reader = EmfComplexFeatureReader.newInstance();
        final URL catalogLocation = getClass().getResource(schemaBase + "mappedPolygons.oasis.xml");
        reader.setResolver(catalogLocation);
        return reader.parse(new URL(location));
    }

    /** Tests getting descriptor from GeoSciML type, supporting polymorphism */
    @Test
    public void testPolymorphism() throws Exception {
        SchemaIndex schemaIndex = loadSchema("http://schemas.opengis.net/GeoSciML/Gsml.xsd");

        AppSchemaFeatureTypeRegistry typeRegistry = new AppSchemaFeatureTypeRegistry();

        try {
            typeRegistry.addSchemas(schemaIndex);

            Name typeName = Types.typeName(GSMLNS, "MappedFeatureType");
            ComplexType mf = (ComplexType) typeRegistry.getAttributeType(typeName);
            assertNotNull(mf);
            assertTrue(mf instanceof FeatureType);

            AttributeExpressionImpl ex =
                    new AttributeExpressionImpl(
                            "gsml:specification/gsml:GeologicUnit/gsml:preferredAge/gsml:GeologicEvent/gsml:eventAge/gsml:CGI_TermRange/gsml:upper/gsml:CGI_TermValue/gsml:value",
                            new Hints(
                                    FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT,
                                    GSMLNAMESPACES));

            Object o = ex.evaluate(mf);
            assertNotNull(o);
            assertTrue(o instanceof PropertyDescriptor);

            ex =
                    new AttributeExpressionImpl(
                            "gsml:specification/gsml:GeologicUnit/gsml:composition/gsml:CompositionPart/gsml:lithology/@xlink:href",
                            new Hints(
                                    FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT,
                                    GSMLNAMESPACES));

            o = ex.evaluate(mf);
            assertNotNull(o);
            assertTrue(o.equals(Types.typeName(XLINKNS, "href")));

            ex =
                    new AttributeExpressionImpl(
                            "gsml:specification/gsml:GeologicUnit/gsml:composition/gsml:CompositionPart/gsml:lithology/@foo:bar",
                            new Hints(
                                    FeaturePropertyAccessorFactory.NAMESPACE_CONTEXT,
                                    GSMLNAMESPACES));

            o = ex.evaluate(mf);
            assertNull(o);
        } finally {
            typeRegistry.disposeSchemaIndexes();
        }
    }
}

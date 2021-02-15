/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2015, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.xml.namespace.QName;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.util.XSDResourceFactoryImpl;
import org.geotools.data.complex.config.AppSchemaFeatureTypeRegistry;
import org.geotools.data.complex.feature.type.Types;
import org.geotools.data.complex.util.EmfComplexFeatureReader;
import org.geotools.data.complex.util.XPathUtil;
import org.geotools.data.complex.util.XPathUtil.StepList;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.ComplexFeatureBuilder;
import org.geotools.feature.TypeBuilder;
import org.geotools.gml3.GMLSchema;
import org.geotools.test.AppSchemaTestSupport;
import org.geotools.xsd.SchemaIndex;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.Feature;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * @author Gabriel Roldan (Axios Engineering)
 * @version $Id$
 * @since 2.4
 */
public class XPathTest extends AppSchemaTestSupport {

    private static final String COMPLEX_WITH_TEXT_CONTENT_SCHEMA_LOCATION =
            "/test-data/complexWithTextContent.xsd";
    private static final String URI = "http://www.geotools.org/appschema/test";

    private static EmfComplexFeatureReader reader;

    @BeforeClass
    public static void oneTimeSetUp() {
        reader = EmfComplexFeatureReader.newInstance();

        // need to register custom factory to load schema resources
        Resource.Factory.Registry.INSTANCE
                .getExtensionToFactoryMap()
                .put("xsd", new XSDResourceFactoryImpl());
    }

    /** Test that some simple-content and non-simple-content types are correctly detected. */
    @Test
    public void testIsSimpleContentType() {
        assertTrue(Types.isSimpleContentType(GMLSchema.CODETYPE_TYPE));
        assertTrue(Types.isSimpleContentType(GMLSchema.MEASURETYPE_TYPE));
        assertFalse(Types.isSimpleContentType(GMLSchema.POINTTYPE_TYPE));
        assertFalse(Types.isSimpleContentType(GMLSchema.POINTPROPERTYTYPE_TYPE));
        assertFalse(Types.isSimpleContentType(GMLSchema.ABSTRACTFEATURETYPE_TYPE));
        assertFalse(Types.isSimpleContentType(GMLSchema.ABSTRACTFEATURECOLLECTIONTYPE_TYPE));
    }

    /** Test that complex elements that can hold text content are correctly detected. */
    @Test
    public void testCanHaveTextContent() throws Exception {
        SchemaIndex schemaIndex =
                reader.parse(getClass().getResource(COMPLEX_WITH_TEXT_CONTENT_SCHEMA_LOCATION));

        XSDElementDeclaration unrestrictedElDecl =
                schemaIndex.getElementDeclaration(new QName(URI, "unrestrictedEl"));
        assertNotNull(unrestrictedElDecl);
        XSDElementDeclaration restrictedElDecl =
                schemaIndex.getElementDeclaration(new QName(URI, "restrictedEl"));
        assertNotNull(restrictedElDecl);

        AppSchemaFeatureTypeRegistry typeRegistry = new AppSchemaFeatureTypeRegistry();
        typeRegistry.addSchemas(schemaIndex);

        Name unrestrictedTypeName = Types.typeName(URI, "UnrestrictedType");
        ComplexType unrestrictedType =
                (ComplexType) typeRegistry.getAttributeType(unrestrictedTypeName);
        assertNotNull(unrestrictedType);
        assertTrue(Types.canHaveTextContent(unrestrictedType));

        Name restrictedTypeName = Types.typeName(URI, "RestrictedType");
        ComplexType restrictedType =
                (ComplexType) typeRegistry.getAttributeType(restrictedTypeName);
        assertNotNull(restrictedType);
        assertFalse(Types.canHaveTextContent(restrictedType));
    }

    /**
     * Test that complex elements that can hold text content are correctly detected.
     *
     * @throws Exception
     */
    @Test
    public void testXPathSetXlink() throws Exception {
        TypeBuilder typeBuilder = new TypeBuilder(CommonFactoryFinder.getFeatureTypeFactory(null));
        typeBuilder.addAttribute(
                "simpleProp", typeBuilder.name("simplePropType").bind(String.class).attribute());
        typeBuilder.setName("subSubType");
        typeBuilder.setMinOccurs(0);
        typeBuilder.setMaxOccurs(0);
        ComplexType subSubType = typeBuilder.complex();
        typeBuilder.setName("subType");
        typeBuilder.addAttribute("subSubProp", subSubType);
        typeBuilder.setMinOccurs(0);
        typeBuilder.setMaxOccurs(0);
        ComplexType subType = typeBuilder.complex();
        typeBuilder.setName("mainType");
        typeBuilder.addAttribute("subProp", subType);
        FeatureType mainType = typeBuilder.feature();

        ComplexFeatureBuilder featureBuilder = new ComplexFeatureBuilder(mainType);
        Feature feat = featureBuilder.buildFeature("test");

        XPath xpathAttributeBuilder = new XPath();
        StepList xpath =
                XPathUtil.steps(feat.getDescriptor(), "subProp/subSubProp", new NamespaceSupport());
        xpathAttributeBuilder.set(feat, xpath, null, null, subType, true, null);
        xpathAttributeBuilder.set(feat, xpath, null, null, subType, true, null);

        assertEquals(1, feat.getProperties().size());
        assertEquals(2, ((ComplexAttribute) feat.getProperty("subProp")).getProperties().size());
    }
}

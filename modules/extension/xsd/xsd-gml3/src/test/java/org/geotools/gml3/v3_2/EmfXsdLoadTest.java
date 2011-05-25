/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.gml3.v3_2;

import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;
import org.eclipse.xsd.impl.XSDSchemaImpl;
import org.eclipse.xsd.util.XSDResourceImpl;
import org.geotools.xml.Configuration;
import org.geotools.xml.SchemaIndex;
import org.geotools.xml.Schemas;
import org.geotools.xml.XSD;
import org.geotools.xml.impl.SchemaIndexImpl;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test that complex type child elements have types. This version does not use the GeoTools
 * {@link Configuration} or {@link XSD} support, relying only on EMF and its native file-based
 * schema include/import support.
 * 
 * @author Ben Caradoc-Davies, CSIRO Earth Science and Resource Engineering
 * 
 *
 * @source $URL$
 */
public class EmfXsdLoadTest {

    /**
     * Test that gml:AbstractGMLType child elements have types.
     */
    @Test
    public void test_AbstractGMLType() {
        checkChildElementTypes("gml.xsd", org.geotools.gml3.v3_2.GML.NAMESPACE, "AbstractGMLType");
    }

    /**
     * Test that gts:TM_Primitive_PropertyType child elements have types.
     */
    @Test
    public void test_TM_Primitive_PropertyType() {
        checkChildElementTypes("gml.xsd", "http://www.isotc211.org/2005/gts",
                "TM_Primitive_PropertyType");
    }

    /**
     * Test that gts:TM_PeriodDuration_PropertyType child elements have types.
     */
    @Test
    public void test_TM_PeriodDuration_PropertyType() {
        checkChildElementTypes("gml.xsd", "http://www.isotc211.org/2005/gts",
                "TM_PeriodDuration_PropertyType");
    }

    /**
     * Test that gmd:CI_ResponsibleParty_Type child elements have types.
     */
    @Test
    public void test_CI_ResponsibleParty_Type() {
        checkChildElementTypes("gml.xsd", "http://www.isotc211.org/2005/gmd",
                "CI_ResponsibleParty_Type");
    }

    /**
     * Test that gss:GM_Point_PropertyType child elements have types.
     */
    @Test
    public void test_GM_Point_PropertyType() {
        checkChildElementTypes("gml.xsd", "http://www.isotc211.org/2005/gss",
                "GM_Point_PropertyType");
    }

    /**
     * Check that a type found by loading a top-level schema has children whose types are not null.
     * 
     * @param filename
     *            top-level schema file
     * @param namespace
     *            namespace of type to check
     * @param name
     *            local name of type to check
     */
    @SuppressWarnings("unchecked")
    private static void checkChildElementTypes(String filename, String namespace, String name) {
        ResourceSet resourceSet = XSDSchemaImpl.createResourceSet();
        XSDResourceImpl resource = (XSDResourceImpl) resourceSet.getResource(
                URI.createURI(EmfXsdLoadTest.class.getResource(filename).toString()), true);
        XSDSchema schema = resource.getSchema();
        Assert.assertNotNull(schema);
        SchemaIndex index = null;
        XSDTypeDefinition type = null;
        try {
            index = new SchemaIndexImpl(new XSDSchema[] { schema });
            type = index.getTypeDefinition(new QName(namespace, name));
        } finally {
            if (index != null) {
                index.destroy();
            }
        }
        Assert.assertNotNull(type);
        System.err.println("Child element declaration types for " + name + " :");
        List<XSDElementDeclaration> children = Schemas.getChildElementDeclarations(type);
        boolean foundNull = false;
        for (XSDElementDeclaration child : children) {
            System.err.println("QName: " + child.getQName() + " URI: " + child.getURI() + " Type: "
                    + child.getTypeDefinition()
                    + (child.getTypeDefinition() == null ? " <<< FAILURE" : ""));
            if (child.getTypeDefinition() == null) {
                foundNull = true;
            }
        }
        System.err.println();
        Assert.assertFalse("Unexpected child element declaration with null type", foundNull);
    }

}

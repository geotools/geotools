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

import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.xml.Configuration;
import org.geotools.xml.SchemaIndex;
import org.geotools.xml.Schemas;
import org.geotools.xml.impl.SchemaIndexImpl;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test that complex type child elements have types.
 * 
 * @author Ben Caradoc-Davies, CSIRO Earth Science and Resource Engineering
 * 
 *
 * @source $URL$
 */
public class ChildElementTypeTest {

    /**
     * Test that gml:AbstractGMLType child elements have types.
     */
    @Test
    public void test_AbstractGMLType() {
        checkChildElementTypes(new org.geotools.gml3.v3_2.GMLConfiguration(), new QName(
                org.geotools.gml3.v3_2.GML.NAMESPACE, "AbstractGMLType"));
    }

    /**
     * Test that gts:TM_Primitive_PropertyType child elements have types.
     */
    @Test
    public void test_TM_Primitive_PropertyType() {
        checkChildElementTypes(new org.geotools.gml3.v3_2.GMLConfiguration(), new QName(
                "http://www.isotc211.org/2005/gts", "TM_Primitive_PropertyType"));
    }

    /**
     * Test that gts:TM_PeriodDuration_PropertyType child elements have types.
     */
    @Test
    public void test_TM_PeriodDuration_PropertyType() {
        checkChildElementTypes(new org.geotools.gml3.v3_2.GMLConfiguration(), new QName(
                "http://www.isotc211.org/2005/gts", "TM_PeriodDuration_PropertyType"));
    }

    /**
     * Test that gmd:CI_ResponsibleParty_Type child elements have types.
     */
    @Test
    public void test_CI_ResponsibleParty_Type() {
        checkChildElementTypes(new org.geotools.gml3.v3_2.GMLConfiguration(), new QName(
                "http://www.isotc211.org/2005/gmd", "CI_ResponsibleParty_Type"));
    }

    /**
     * Test that gss:GM_Point_PropertyType child elements have types.
     */
    @Test
    public void test_GM_Point_PropertyType() {
        checkChildElementTypes(new org.geotools.gml3.v3_2.GMLConfiguration(), new QName(
                "http://www.isotc211.org/2005/gss", "GM_Point_PropertyType"));
    }

    /**
     * Check that the child elements of a complex type all have types.
     * 
     * @param configuration
     *            XML configuration for the schema
     * @param name
     *            complex type to be tested
     */
    private static void checkChildElementTypes(Configuration configuration, QName name) {
        SchemaIndex index = null;
        XSDTypeDefinition type = null;
        try {
            index = Schemas.findSchemas(configuration);
            type = index.getTypeDefinition(name);
        } finally {
            if (index != null) {
                index.destroy();
            }
        }
        Assert.assertNotNull(type);
        System.err.println("Child element declaration types for " + name + " :");
        @SuppressWarnings("unchecked")
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

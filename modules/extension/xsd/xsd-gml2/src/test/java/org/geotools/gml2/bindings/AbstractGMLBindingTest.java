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
 */
package org.geotools.gml2.bindings;

import java.io.IOException;
import java.util.List;
import javax.xml.namespace.QName;
import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSimpleTypeDefinition;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.gml2.GMLConfiguration;
import org.geotools.xsd.AttributeInstance;
import org.geotools.xsd.Binding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.InstanceComponent;
import org.geotools.xsd.Node;
import org.geotools.xsd.impl.AttributeImpl;
import org.geotools.xsd.impl.ElementImpl;
import org.geotools.xsd.impl.NodeImpl;
import org.junit.After;
import org.junit.Before;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequenceFactory;
import org.locationtech.jts.geom.impl.CoordinateArraySequenceFactory;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

public class AbstractGMLBindingTest {
    XSDSchema schema;
    MutablePicoContainer container;
    SimpleFeatureTypeBuilder ftBuilder;

    @Before
    public void setUp() throws Exception {
        GMLConfiguration configuration = new GMLConfiguration();
        XSDSchema result;
        try {
            result = configuration.getXSD().getSchema();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        schema = result;
        container = new DefaultPicoContainer();

        configuration.registerBindings(container);

        ftBuilder = new SimpleFeatureTypeBuilder();
    }

    @After
    public void tearDown() throws Exception {
        container.dispose();
    }

    Binding getBinding(QName type) {
        return (Binding) container.getComponentInstance(type);
    }

    Node createNode(
            InstanceComponent instance,
            ElementInstance[] elements,
            Object[] elementValues,
            AttributeInstance[] attributes,
            Object[] attValues) {
        NodeImpl node = new NodeImpl(instance);

        if (elements != null && elements.length > 0) {
            for (int i = 0; i < elements.length; i++) {
                node.addChild(new NodeImpl(elements[i], elementValues[i]));
            }
        }

        if (attributes != null && attributes.length > 0) {
            for (int i = 0; i < attributes.length; i++) {
                node.addAttribute(new NodeImpl(attributes[i], attValues[i]));
            }
        }

        return node;
    }

    AttributeInstance createAtribute(String namespace, String name, QName type, String text) {
        XSDAttributeDeclaration declaration = XSDFactory.eINSTANCE.createXSDAttributeDeclaration();
        declaration.setName(name);
        declaration.setTargetNamespace(namespace);
        declaration.setTypeDefinition((XSDSimpleTypeDefinition) findTypeDefinition(schema, type));

        AttributeInstance attribute = new AttributeImpl(declaration);
        attribute.setName(name);
        attribute.setNamespace(namespace);
        attribute.setText(text);

        return attribute;
    }

    ElementInstance createElement(String namespace, String name, QName type, String text) {
        XSDElementDeclaration declaration = XSDFactory.eINSTANCE.createXSDElementDeclaration();
        declaration.setName(name);
        declaration.setTargetNamespace(namespace);
        declaration.setTypeDefinition(findTypeDefinition(schema, type));

        ElementInstance element = new ElementImpl(declaration);

        element.setName(name);
        element.setNamespace(namespace);
        element.setText(text);

        return element;
    }

    public CoordinateSequence createCoordinateSequence(Coordinate c) {
        return createCoordinateSequence(new Coordinate[] {c});
    }

    public CoordinateSequence createCoordinateSequence(Coordinate[] c) {
        CoordinateSequenceFactory csFactory = CoordinateArraySequenceFactory.instance();

        return csFactory.create(c);
    }

    public SimpleFeature createFeature(String[] names, Class[] types, Object[] values) {
        ftBuilder.setName("test");

        for (int i = 0; i < names.length; i++) {
            ftBuilder.add(names[i], values[i].getClass());
        }

        try {
            SimpleFeatureType fType = ftBuilder.buildFeatureType();

            return SimpleFeatureBuilder.build(fType, values, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    XSDTypeDefinition findTypeDefinition(XSDSchema schema, QName type) {
        List types = schema.getTypeDefinitions();

        for (Object o : types) {
            XSDTypeDefinition typeDef = (XSDTypeDefinition) o;

            if (type.getNamespaceURI().equals(typeDef.getTargetNamespace())
                    && type.getLocalPart().equals(typeDef.getName())) {
                return typeDef;
            }
        }

        return null;
    }
}

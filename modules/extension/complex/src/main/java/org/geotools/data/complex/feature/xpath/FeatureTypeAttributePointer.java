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
package org.geotools.data.complex.feature.xpath;

import org.apache.commons.jxpath.ri.Compiler;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.compiler.NodeTypeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.api.feature.type.ComplexType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.feature.type.PropertyDescriptor;
import org.geotools.feature.type.Types;

/**
 * Pointer to a single attribute of a feature type.
 *
 * @author Niels Charlier (Curtin University of Technology)
 */
public class FeatureTypeAttributePointer extends NodePointer {

    /** */
    private static final long serialVersionUID = -7238823373667263032L;

    /** the feature type */
    protected ComplexType parentType;

    /** the feature type */
    protected AttributeType attType;

    /** descriptor */
    protected PropertyDescriptor descriptor;

    /** the indedx of hte property being pointed at */
    protected Name name;

    /**
     * Creates the pointer.
     *
     * @param parent The parent pointer, pointer at the feature type.
     * @param parentType Feature Type of parent
     * @param name Name of Attribute
     */
    public FeatureTypeAttributePointer(NodePointer parent, ComplexType parentType, Name name) {
        super(parent);

        this.parentType = parentType;
        this.name = name;

        descriptor = getDescriptor();
        attType = (AttributeType) descriptor.getType();
    }

    public PropertyDescriptor getDescriptor() {
        return Types.findDescriptor(parentType, name);
    }

    /** */
    @Override
    public boolean isLeaf() {
        return !(attType instanceof ComplexType);
    }

    /** */
    @Override
    public boolean isCollection() {
        return false;
    }

    /** Return number of elements */
    @Override
    public int getLength() {
        return 1;
    }

    /** Returns the qname */
    @Override
    public QName getName() {
        return new QName(name.getNamespaceURI(), name.getLocalPart());
    }

    @Override
    public Object getBaseValue() {
        return parentType;
    }

    @Override
    public Object getImmediateNode() {
        return descriptor;
    }

    @Override
    public void setValue(Object value) {
        throw new UnsupportedOperationException("Feature types are immutable");
    }

    @Override
    public int compareChildNodePointers(NodePointer pointer1, NodePointer pointer2) {
        return 0;
    }

    @Override
    public NodeIterator childIterator(NodeTest test, boolean reverse, NodePointer startWith) {
        if (test instanceof NodeNameTest) {
            NodeNameTest nodeNameTest = (NodeNameTest) test;

            if (!nodeNameTest.isWildcard()) {
                String localName = nodeNameTest.getNodeName().getName();
                String nameSpace = nodeNameTest.getNamespaceURI();
                if (nameSpace == null) nameSpace = getNamespaceResolver().getNamespaceURI("");

                return new SingleFeatureTypeAttributeIterator(
                        this, (ComplexType) attType, Types.typeName(nameSpace, localName));
            } else {
                return new FeatureTypeAttributeIterator(this, (ComplexType) attType);
            }
        }

        if (test instanceof NodeTypeTest) {
            NodeTypeTest nodeTypeTest = (NodeTypeTest) test;

            if (nodeTypeTest.getNodeType() == Compiler.NODE_TYPE_NODE) {
                return new FeatureTypeAttributeIterator(this, (ComplexType) attType);
            }
        }

        return super.childIterator(test, reverse, startWith);
    }

    @Override
    public NodeIterator attributeIterator(QName qname) {
        return new DescriptorXmlAttributeNodeIterator(
                this, Types.typeName(getNamespaceResolver().getNamespaceURI(qname.getPrefix()), qname.getName()));
    }
}

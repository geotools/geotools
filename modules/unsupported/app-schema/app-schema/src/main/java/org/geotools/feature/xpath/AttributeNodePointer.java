/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.feature.xpath;

import java.util.Collection;

import org.apache.commons.jxpath.ri.Compiler;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.compiler.NodeTypeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.geotools.feature.Types;
import org.opengis.feature.Attribute;
import org.opengis.feature.ComplexAttribute;

/**
 * Special node pointer for {@link org.geotools.feature.Feature}.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 * @author Gabriel Roldan, Axios Engineering
 * 
 *
 * @source $URL$
 */
public class AttributeNodePointer extends NodePointer {

    /**
     * 
     */
    private static final long serialVersionUID = -5637103253645991273L;

    /**
     * The name of hte node.
     */
    QName name;

    /**
     * The underlying feature
     */
    Attribute feature;

    protected AttributeNodePointer(NodePointer parent, Attribute feature, QName name) {
        super(parent);
        this.name = name;
        this.feature = feature;
    }

    public boolean isLeaf() {
        return !(feature instanceof ComplexAttribute);       
    }

    public boolean isCollection() {
        return false;
    }

    public int getLength() {
        return 1;
    }

    public QName getName() {
        return name;
    }

    public Object getBaseValue() {
        return null;
    }

    public Object getImmediateNode() {
        return feature;
    }

    public void setValue(Object value) {
        feature = (Attribute) value;
    }

    public int compareChildNodePointers(NodePointer pointer1, NodePointer pointer2) {

        return 0;
    }

    public NodeIterator childIterator(NodeTest test, boolean reverse, NodePointer startWith) {
        if (test instanceof NodeNameTest) {
            NodeNameTest nodeNameTest = (NodeNameTest) test;

            if (!nodeNameTest.isWildcard()) {
                String localName = nodeNameTest.getNodeName().getName();
                String nameSpace = nodeNameTest.getNamespaceURI();
                if (nameSpace==null) nameSpace = getNamespaceResolver().getNamespaceURI("");
                                
                return new AttributeNodeIterator(this, Types.typeName(nameSpace, localName));
            } else {
                return new AttributeNodeIterator(this);
            }
        }

        if (test instanceof NodeTypeTest) {
            NodeTypeTest nodeTypeTest = (NodeTypeTest) test;
            if (nodeTypeTest.getNodeType() == Compiler.NODE_TYPE_NODE) {
                return new AttributeNodeIterator(this);
            }
        }

        return super.childIterator(test, reverse, startWith);
    }

    public NodeIterator attributeIterator(QName qname) {        
        return new XmlAttributeNodeIterator(this, Types.typeName(getNamespaceResolver().getNamespaceURI(qname.getPrefix()), qname.getName()));
    }

}

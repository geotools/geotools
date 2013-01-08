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
package org.geotools.feature.xpath;

import org.apache.commons.jxpath.ri.Compiler;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.compiler.NodeTypeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.geotools.feature.type.Types;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;

/**
 * Pointer to a single attribute of a feature type.
 * 
 * @author Niels Charlier (Curtin University of Technology)
 * 
 *
 *
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/app-schema/app-schema/src/main
 *         /java/org/geotools/feature/xpath/FeatureTypeAttributePointer.java $
 * 
 */

public class FeatureTypeAttributePointer extends NodePointer {
    
    /**
     * 
     */
    private static final long serialVersionUID = -7238823373667263032L;

    /**
     * the feature type
     */
    protected ComplexType parentType;

    /**
     * the feature type
     */
    protected AttributeType attType;
    
    /**
     * descriptor
     */
    protected PropertyDescriptor descriptor;    

    /**
     * the indedx of hte property being pointed at
     */
    protected Name name;

    /**
     * Creates the pointer.
     * 
     * @param parent
     *            The parent pointer, pointer at the feature type.
     * @param parentType
     *           Feature Type of parent
     * @param name
     *           Name of Attribute
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

    /**
     * 
     */
    public boolean isLeaf() {
        return !(attType instanceof ComplexType);
    }

    /**
     * 
     */
    public boolean isCollection() {
        return false;
    }

    /**
     * Return number of elements
     */
    public int getLength() {
        return 1;
    }

    /**
     * Returns the qname 
     */
    public QName getName() {
        return new QName(name.getNamespaceURI(), name.getLocalPart());
    }

    public Object getBaseValue() {
        return parentType;
    }

    public Object getImmediateNode() {
        return descriptor;
    }
     

    public void setValue(Object value) {
        throw new UnsupportedOperationException("Feature types are immutable");
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
                                
                return new SingleFeatureTypeAttributeIterator(this, 
                        ((ComplexType) attType), Types.typeName(nameSpace, localName));
            } else {
                return new FeatureTypeAttributeIterator(this, 
                        ((ComplexType) attType));
            }
        }

        if (test instanceof NodeTypeTest) {
            NodeTypeTest nodeTypeTest = (NodeTypeTest) test;

            if (nodeTypeTest.getNodeType() == Compiler.NODE_TYPE_NODE) {
                return new FeatureTypeAttributeIterator(this, 
                        ((ComplexType) attType));
            }
        }

        return super.childIterator(test, reverse, startWith);
    }
    
    public NodeIterator attributeIterator(QName qname) {        
        return new DescriptorXmlAttributeNodeIterator(this, Types.typeName(getNamespaceResolver().getNamespaceURI(qname.getPrefix()), qname.getName()));
    }
}

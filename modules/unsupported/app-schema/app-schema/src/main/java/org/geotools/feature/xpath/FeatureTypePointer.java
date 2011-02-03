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
package org.geotools.feature.xpath;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.ri.Compiler;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.compiler.NodeTypeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.geotools.feature.Types;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.PropertyDescriptor;

/**
 * Special node pointer for {@link org.geotools.feature.FeatureType}.
 * 
 * @author Niels Charlier, Curtin University of Technology
 * 
 * @source $URL:
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/app-schema/app-schema/src/main
 *         /java/org/geotools/feature/xpath/FeatureTypePointer.java $
 * 
 */
public class FeatureTypePointer extends NodePointer {

    /**
     * 
     */
    private static final long serialVersionUID = 7329150854098309040L;

    /**
     * The name of the node.
     */
    protected QName name;

    /**
     * The underlying feature type
     */
    protected ComplexType featureType;

    protected FeatureTypePointer(NodePointer parent, ComplexType featureType, QName name) {
        super(parent);
        this.name = name;
        this.featureType = featureType;
    }

    public boolean isLeaf() {
        return false;
    }

    public boolean isCollection() {
        return true;
    }

    public int getLength() {
        //get list of descriptors from types and all supertypes
        List<PropertyDescriptor> children = new ArrayList<PropertyDescriptor>();
        ComplexType loopType = featureType;
        while (loopType != null) { 
            children.addAll(loopType.getDescriptors());
            loopType = loopType.getSuper() instanceof ComplexType? (ComplexType) loopType.getSuper() : null;
        }
        
        return children.size();
    }

    public QName getName() {
        return name;
    }

    public Object getBaseValue() {
        return null;
    }

    public Object getImmediateNode() {
        return featureType;
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
                
                return new SingleFeatureTypeAttributeIterator(this, featureType,
                         Types.typeName(nameSpace, localName));
            } else {
                return new FeatureTypeAttributeIterator(this, featureType);
            }
        }

        if (test instanceof NodeTypeTest) {
            NodeTypeTest nodeTypeTest = (NodeTypeTest) test;

            if (nodeTypeTest.getNodeType() == Compiler.NODE_TYPE_NODE) {
                return new FeatureTypeAttributeIterator(this, featureType);
            }
        }

        return super.childIterator(test, reverse, startWith);
    }
}

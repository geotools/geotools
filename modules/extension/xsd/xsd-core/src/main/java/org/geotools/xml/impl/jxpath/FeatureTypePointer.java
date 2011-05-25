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
package org.geotools.xml.impl.jxpath;

import org.apache.commons.jxpath.ri.Compiler;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.compiler.NodeTest;
import org.apache.commons.jxpath.ri.compiler.NodeTypeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.opengis.feature.simple.SimpleFeatureType;


/**
 * Special node pointer for {@link org.geotools.feature.FeatureTy}.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 * @source $URL$
 */
public class FeatureTypePointer extends NodePointer {
    /**
     * The name of hte node.
     */
    QName name;

    /**
     * The underlying feature type
     */
    SimpleFeatureType featureType;

    protected FeatureTypePointer(NodePointer parent, SimpleFeatureType featureType, QName name) {
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
        return featureType.getAttributeCount();
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
        featureType = (SimpleFeatureType) value;
    }

    public int compareChildNodePointers(NodePointer pointer1, NodePointer pointer2) {
        return 0;
    }

    public NodeIterator childIterator(NodeTest test, boolean reverse, NodePointer startWith) {
        if (test instanceof NodeNameTest) {
            NodeNameTest nodeNameTest = (NodeNameTest) test;

            if (!nodeNameTest.isWildcard()) {
                int index = featureType.indexOf(nodeNameTest.getNodeName().getName());

                if (index > -1) {
                    return new SingleFeatureTypeAttributeIterator(this, index);
                }
            } else {
                return new FeatureTypeAttributeIterator(this);
            }
        }

        if (test instanceof NodeTypeTest) {
            NodeTypeTest nodeTypeTest = (NodeTypeTest) test;

            if (nodeTypeTest.getNodeType() == Compiler.NODE_TYPE_NODE) {
                return new FeatureTypeAttributeIterator(this);
            }
        }

        return super.childIterator(test, reverse, startWith);
    }
}

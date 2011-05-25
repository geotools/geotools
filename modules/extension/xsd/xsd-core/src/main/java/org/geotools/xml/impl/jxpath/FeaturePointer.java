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
import org.opengis.feature.simple.SimpleFeature;


/**
 * Special node pointer for {@link org.geotools.feature.Feature}.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 * @source $URL$
 */
public class FeaturePointer extends NodePointer {
    /**
     * The name of hte node.
     */
    QName name;

    /**
     * The underlying feature
     */
    SimpleFeature feature;

    protected FeaturePointer(NodePointer parent, SimpleFeature feature, QName name) {
        super(parent);
        this.name = name;
        this.feature = feature;
    }

    public boolean isLeaf() {
        return false;
    }

    public boolean isCollection() {
        return true;
    }

    public int getLength() {
        return feature.getAttributeCount();
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
        feature = (SimpleFeature) value;
    }

    public int compareChildNodePointers(NodePointer pointer1, NodePointer pointer2) {
        return 0;
    }

    public NodeIterator childIterator(NodeTest test, boolean reverse, NodePointer startWith) {
        if (test instanceof NodeNameTest) {
            NodeNameTest nodeNameTest = (NodeNameTest) test;

            if (!nodeNameTest.isWildcard()) {
                int index = feature.getFeatureType().indexOf(nodeNameTest.getNodeName().getName());

                if (index > -1) {
                    return new SingleFeaturePropertyIterator(this, index);
                }
            } else {
                return new FeaturePropertyIterator(this);
            }
        }

        if (test instanceof NodeTypeTest) {
            NodeTypeTest nodeTypeTest = (NodeTypeTest) test;

            if (nodeTypeTest.getNodeType() == Compiler.NODE_TYPE_NODE) {
                return new FeaturePropertyIterator(this);
            }
        }

        return super.childIterator(test, reverse, startWith);
    }

    public NodeIterator attributeIterator(QName qname) {
        if (qname.getName().equals("id") || qname.getName().equals("fid")) {
            return new SingleFeaturePropertyIterator(this, -1);
        }

        return super.attributeIterator(qname);
    }
}

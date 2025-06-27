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

import java.util.Iterator;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDElementDeclaration;
import org.geotools.api.feature.type.ComplexType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.feature.type.PropertyDescriptor;
import org.geotools.api.feature.type.PropertyType;
import org.geotools.xsd.Schemas;

/**
 * Special node pointer for an XML-attribute inside an attribute.
 *
 * @author Niels Charlier (Curtin University of Technology)
 */
public class DescriptorXmlAttributeNodePointer extends NodePointer {

    /** */
    private static final long serialVersionUID = 8096170689141331692L;

    /** The name of the node. */
    Name name;

    /** The underlying descriptor */
    PropertyDescriptor descriptor;

    protected DescriptorXmlAttributeNodePointer(NodePointer parent, PropertyDescriptor descriptor, Name name) {
        super(parent);
        this.name = name;
        this.descriptor = descriptor;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public boolean isCollection() {
        return false;
    }

    @Override
    public boolean isAttribute() {
        return true;
    }

    @Override
    public QName getName() {
        return new QName(name.getURI(), name.getLocalPart());
    }

    @Override
    public Object getBaseValue() {
        return null;
    }

    @Override
    public Object getImmediateNode() {

        // first try regular way
        PropertyType pt = descriptor.getType();
        if (pt instanceof ComplexType) {
            ComplexType ct = (ComplexType) pt;
            PropertyDescriptor ad = ct.getDescriptor("@" + name.getLocalPart());
            if (ad != null) {
                return ad;
            }
        }

        XSDElementDeclaration decl =
                (XSDElementDeclaration) descriptor.getUserData().get(XSDElementDeclaration.class);

        Iterator it = Schemas.getAttributeDeclarations(decl).iterator();
        while (it.hasNext()) {
            XSDAttributeDeclaration attDecl = (XSDAttributeDeclaration) it.next();
            if (attDecl.getURI()
                    .equals((name.getNamespaceURI() == null ? "" : name.getNamespaceURI())
                            + "#"
                            + name.getLocalPart())) {
                return name;
            }
        }
        return null;
    }

    @Override
    public void setValue(Object value) {
        throw new UnsupportedOperationException("Feature types are immutable");
    }

    @Override
    public int compareChildNodePointers(NodePointer arg0, NodePointer arg1) {
        return 0;
    }

    @Override
    public int getLength() {
        return 0;
    }
}

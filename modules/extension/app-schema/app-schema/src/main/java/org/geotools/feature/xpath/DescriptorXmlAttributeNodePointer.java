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

import java.util.Iterator;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDElementDeclaration;
import org.geotools.xml.Schemas;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;

/**
 * Special node pointer for an XML-attribute inside an attribute.
 * 
 * @author Niels Charlier (Curtin University of Technology)
 * 
 *
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/app-schema/app-schema/src/main/java/org/geotools/feature/xpath/DescriptorXmlAttributeNodePointer.java $
 */
public class DescriptorXmlAttributeNodePointer extends NodePointer {

    /**
     * 
     */
    private static final long serialVersionUID = 8096170689141331692L;

    /**
     * The name of the node.
     */
    Name name;

    /**
     * The underlying descriptor
     */
    PropertyDescriptor descriptor;

    protected DescriptorXmlAttributeNodePointer(NodePointer parent, PropertyDescriptor descriptor, Name name) {
        super(parent);
        this.name = name;
        this.descriptor = descriptor;
    }

    public boolean isLeaf() {
        return true;
    }

    public boolean isCollection() {
        return false;
    }
    
    public boolean isAttribute() {
        return true;
    }

    public QName getName() {
        return new QName( name.getURI(), name.getLocalPart() );
    }

    public Object getBaseValue() {
        return null;
    }

    public Object getImmediateNode() {
         XSDElementDeclaration decl = (XSDElementDeclaration) descriptor.getUserData().get(XSDElementDeclaration.class);
            
         Iterator it = Schemas.getAttributeDeclarations(decl).iterator();
         while (it.hasNext()) {
            XSDAttributeDeclaration attDecl = ((XSDAttributeDeclaration) it.next());
            if ( attDecl.getURI().equals((name.getNamespaceURI()==null?"":name.getNamespaceURI())  + "#" + name.getLocalPart() ))   {
               return name;
            }            
         }    
         return null;
    }

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

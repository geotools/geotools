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

import java.util.Map;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.opengis.feature.Attribute;
import org.opengis.feature.type.Name;
import org.xml.sax.Attributes;

/**
 * Special node pointer for an XML-attribute inside an attribute.
 * 
 * @author Niels Charlier (Curtin University of Technology)
 * 
 *
 *
 *
 *
 * @source $URL$
 */
public class XmlAttributeNodePointer extends NodePointer {

    /**
     * 
     */
    private static final long serialVersionUID = 3315524792964171784L;

    /**
     * The name of hte node.
     */
    Name name;

    /**
     * The underlying feature
     */
    Attribute feature;

    protected XmlAttributeNodePointer(NodePointer parent, Attribute feature, Name name) {
        super(parent);
        this.name = name;
        this.feature = feature;
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

    @SuppressWarnings("unchecked")    
    public Object getImmediateNode() {
        //FIXME - better id checking
        if (name.getLocalPart().equals("id")) {
            return feature.getIdentifier().getID();
        }
        else {
            Map<Name, Object> map = (Map<Name, Object>) feature.getUserData().get(Attributes.class);
            if (map != null) {
                return map.get(name);
            }
            else throw new NullPointerException();
        }
    }

    @SuppressWarnings("unchecked")    
    public void setValue(Object value) {
       if (!name.getLocalPart().equals("id")) {
           Map<Name, Object> map = (Map<Name, Object>) feature.getUserData().get(Attributes.class);
           if (map != null) {
              map.put(name, value);
           }
       }
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

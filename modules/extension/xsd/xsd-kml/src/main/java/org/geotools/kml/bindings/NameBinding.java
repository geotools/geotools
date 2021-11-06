/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package org.geotools.kml.bindings;

import javax.xml.namespace.QName;
import org.geotools.kml.Folder;
import org.geotools.kml.FolderStack;
import org.geotools.kml.v22.KML;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.Binding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class NameBinding extends AbstractComplexBinding {

    private final FolderStack folderStack;

    private static final String FOLDER = KML.Folder.getLocalPart();

    public NameBinding(FolderStack folderStack) {
        this.folderStack = folderStack;
    }

    @Override
    public QName getTarget() {
        return KML.name;
    }

    @Override
    public int getExecutionMode() {
        return Binding.OVERRIDE;
    }

    @Override
    public Class getType() {
        return String.class;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        Node parent = node.getParent();
        if (parent != null) {
            String parentElementName = parent.getComponent().getName();
            if (FOLDER.equals(parentElementName)) {
                Folder folder = folderStack.peek();
                if (folder != null) {
                    folder.setName(value.toString());
                }
            }
        }
        return super.parse(instance, node, value);
    }

    @Override
    public Element encode(Object object, Document document, Element value) throws Exception {
        value.setTextContent(object.toString());
        return value;
    }
}

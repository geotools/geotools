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
import org.opengis.feature.simple.SimpleFeature;
import org.picocontainer.MutablePicoContainer;

public class FolderBinding extends AbstractComplexBinding {

    private final FolderStack folderStack;

    public FolderBinding(FolderStack folderStack) {
        this.folderStack = folderStack;
    }

    @Override
    public QName getTarget() {
        return KML.Folder;
    }

    @Override
    public Class getType() {
        return SimpleFeature.class;
    }

    @Override
    public int getExecutionMode() {
        return Binding.AFTER;
    }

    @Override
    public void initialize(ElementInstance instance, Node node, MutablePicoContainer context) {
        super.initialize(instance, node, context);
        folderStack.push(new Folder());
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        folderStack.pop();
        return value;
    }
}

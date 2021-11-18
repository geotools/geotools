/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg.wps.xml;

import javax.xml.namespace.QName;
import org.geotools.geopkg.wps.GeoPackageProcessRequest;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

public class ParameterBinding extends AbstractComplexBinding {

    private static final String NAME = "name";

    @Override
    public QName getTarget() {
        return GPKG.parametertype;
    }

    @Override
    public Class getType() {
        return GeoPackageProcessRequest.Parameter.class;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        GeoPackageProcessRequest.Parameter parameter = new GeoPackageProcessRequest.Parameter();
        parameter.setName((String) node.getAttributeValue(NAME));
        parameter.setValue(node.getComponent().getText());

        return parameter;
    }
}

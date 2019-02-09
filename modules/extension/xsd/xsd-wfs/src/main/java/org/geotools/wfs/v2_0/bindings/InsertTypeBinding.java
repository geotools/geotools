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

package org.geotools.wfs.v2_0.bindings;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import net.opengis.wfs20.InsertType;
import net.opengis.wfs20.Wfs20Factory;
import org.eclipse.xsd.XSDElementDeclaration;
import org.geotools.gml3.v3_2.GML;
import org.geotools.wfs.v2_0.WFS;
import org.geotools.xsd.AbstractComplexEMFBinding;

public class InsertTypeBinding extends AbstractComplexEMFBinding {

    public InsertTypeBinding(Wfs20Factory factory) {
        super(factory);
    }

    public QName getTarget() {
        return WFS.InsertType;
    }

    public Class<?> getType() {
        return InsertType.class;
    }

    @Override
    public List getProperties(Object object, XSDElementDeclaration element) throws Exception {
        InsertType insert = (InsertType) object;
        List properties = new ArrayList();
        for (final Object feature : insert.getAny()) {
            properties.add(new Object[] {GML.AbstractFeature, feature});
        }
        return properties;
    }
}

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wfs.v2_0.bindings;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.xsd.XSDElementDeclaration;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.gml3.v3_2.GML;
import org.geotools.wfs.v2_0.WFS;
import org.geotools.xml.AbstractComplexBinding;

public class EnvelopePropertyTypeBinding extends AbstractComplexBinding {

    public QName getTarget() {
        return WFS.EnvelopePropertyType;
    }

    public Class getType() {
        return ReferencedEnvelope.class;
    }

    @Override
    public List getProperties(Object object, XSDElementDeclaration element) throws Exception {
        List l = new ArrayList();
        l.add(new Object[]{GML.Envelope, object});
        return l;
    }
}

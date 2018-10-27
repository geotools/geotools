/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

import javax.xml.namespace.QName;
import net.opengis.wfs20.MetadataURLType;
import net.opengis.wfs20.Wfs20Factory;
import org.geotools.wfs.v2_0.WFS;
import org.geotools.xsd.AbstractComplexEMFBinding;

public class MetadataURLTypeBinding extends AbstractComplexEMFBinding {

    public MetadataURLTypeBinding(Wfs20Factory factory) {
        super(factory);
    }

    @Override
    public Class getType() {
        return MetadataURLType.class;
    }

    @Override
    public QName getTarget() {
        return WFS.MetadataURLType;
    }
}

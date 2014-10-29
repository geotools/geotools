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

package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;

import net.opengis.fes20.Fes20Factory;
import net.opengis.fes20.GeometryOperandsType;

import org.geotools.filter.v2_0.FES;
import org.geotools.xml.AbstractComplexEMFBinding;

public class GeometryOperandsTypeBinding extends AbstractComplexEMFBinding {

    public GeometryOperandsTypeBinding(Fes20Factory factory) {
        super(factory);
    }
    
    @Override
    public QName getTarget() {
        return FES.GeometryOperandsType;
    }
    
    @Override
    public Class getType() {
        return GeometryOperandsType.class;
    }

}

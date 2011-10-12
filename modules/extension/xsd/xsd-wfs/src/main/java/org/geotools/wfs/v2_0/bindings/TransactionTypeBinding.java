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

import javax.xml.namespace.QName;

import net.opengis.wfs20.Wfs20Factory;

import org.eclipse.emf.ecore.EObject;
import org.geotools.wfs.v2_0.WFS;
import org.geotools.xml.AbstractComplexEMFBinding;

public class TransactionTypeBinding extends AbstractComplexEMFBinding {

    public TransactionTypeBinding() {
        super(Wfs20Factory.eINSTANCE);
    }
    
    public QName getTarget() {
        return WFS.TransactionType;
    }

    @Override
    protected void setProperty(EObject eObject, String property, Object value, boolean lax) {
        super.setProperty(eObject, property, value, lax);
    }
}

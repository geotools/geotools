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
 */
package org.geotools.ows.bindings;

import java.util.List;
import javax.xml.namespace.QName;
import org.eclipse.emf.ecore.EFactory;
import org.geotools.xsd.ComplexEMFBinding;

/**
 * Parses rangeBinding
 *
 * @author Andrea Aime - GeoSolutions
 */
public class RangeBinding extends ComplexEMFBinding {

    public RangeBinding(EFactory factory, QName target) {
        super(factory, target);
    }

    protected void setProperty(
            org.eclipse.emf.ecore.EObject eObject, String property, Object value, boolean lax) {
        // trick required because rangeClouser for some unfathomable reason was declared to extend
        // NMTOKENS instead of NMTOKEN (and it's this a whitespace separated list schema wise)
        if ("rangeClosure".equals(property) && value instanceof List) {
            value = ((List) value).get(0);
        }
        super.setProperty(eObject, property, value, lax);
    };

    @Override
    public Object getProperty(Object object, QName name) throws Exception {
        // TODO Auto-generated method stub
        return super.getProperty(object, name);
    }
}

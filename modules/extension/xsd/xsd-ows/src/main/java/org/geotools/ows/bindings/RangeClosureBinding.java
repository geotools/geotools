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
package org.geotools.ows.bindings;

import javax.xml.namespace.QName;
import net.opengis.ows11.RangeClosureType;
import org.geotools.ows.v1_1.OWS;
import org.geotools.xsd.AbstractSimpleBinding;
import org.geotools.xsd.InstanceComponent;

public class RangeClosureBinding extends AbstractSimpleBinding {

    public RangeClosureBinding() {}

    /** @generated */
    public QName getTarget() {
        return OWS.rangeClosure;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return RangeClosureType.class;
    }

    @Override
    public int getExecutionMode() {
        return OVERRIDE;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value) throws Exception {
        Object result = null;
        if (value != null) {
            result = RangeClosureType.get(value.toString());
        }

        return result;
    }

    @Override
    public String encode(Object object, String value) throws Exception {
        return ((RangeClosureType) object).getLiteral();
    }
}

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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

import java.lang.reflect.Field;

import javax.measure.unit.BaseUnit;
import javax.measure.unit.DerivedUnit;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import javax.measure.unit.UnitFormat;
import javax.xml.namespace.QName;

import org.geotools.ows.v1_1.OWS;
import org.geotools.xml.AbstractSimpleBinding;
import org.geotools.xml.InstanceComponent;

public class UnitBinding extends AbstractSimpleBinding {

	public QName getTarget() {
		return OWS.UOM;
	}

	public Class getType() {
		return Unit.class;
	}
	
    
    public int getExecutionMode() {
        return OVERRIDE;
    }
    
    /**
     * @override
     */
    public Object parse(InstanceComponent instance, Object value)
        throws Exception {
    	//Object parseObject = UnitFormat.getInstance().parseObject((String) value);
        //Object parseObject = UnitFormat.getAsciiInstance().parseObject((String) value);
    	Unit valueOf = lookup( (String) value );
        return valueOf;
    }
    
    private Unit lookup(String name) {
        Unit unit = lookup(SI.class, name);
        if (unit != null)
            return unit;

        unit = lookup(NonSI.class, name);
        if (unit != null)
            return unit;

        if (name.endsWith("s") || name.endsWith("S")) {
            return lookup(name.substring(0, name.length() - 1));
        }
        // if we get here, try some aliases
        if (name.equalsIgnoreCase("feet")) {
            return lookup(NonSI.class, "foot");
        }
        // if we get here, try some aliases
        if (name.equalsIgnoreCase("meters") || name.equalsIgnoreCase("meter")) {
            return lookup(SI.class, "m");
        }
        if (name.equalsIgnoreCase("unity")) {
            return Unit.ONE;
        }
        return null;
    }

	private Unit lookup(Class class1, String name) {
		Unit unit = null;
		Field[] fields = class1.getDeclaredFields();
		for (int i=0; i<fields.length; i++) {
			Field field = fields[i];
			String name2 = field.getName();
			if ( (field.getType().isAssignableFrom(BaseUnit.class) ||
					field.getType().isAssignableFrom(DerivedUnit.class)) &&
					name2.equalsIgnoreCase(name) ) {

				try {
					unit = (Unit) field.get(unit);
					return unit;
				} catch (Exception e) {
					// continue searching
				}
				
			}
		}
		return unit;
	}


	/**
     * Performs the encoding of the object as a String.
     *
     * @param object The object being encoded, never null.
     * @param value The string returned from another binding in the type
     * hierachy, which could be null.
     *
     * @return A String representing the object.
     * @override
     */
    public String encode(Object object, String value) throws Exception {
        return object.toString();
    }

}

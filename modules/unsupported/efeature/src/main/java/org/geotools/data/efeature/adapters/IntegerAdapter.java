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
package org.geotools.data.efeature.adapters;

/**
 * @author kengu - 15. juni 2011
 *
 *
 * @source $URL$
 */
public class IntegerAdapter extends org.eclipse.emf.query.conditions.numbers.NumberAdapter.IntegerAdapter {

    public static final org.geotools.data.efeature.adapters.IntegerAdapter 
        DEFAULT = new org.geotools.data.efeature.adapters.IntegerAdapter();

    /** Hide constructor */
    private IntegerAdapter() { /*NOP*/};
    
    @Override
    public int intValue(Object value) {
        return value instanceof Integer ? ((Integer)value).intValue() : (Integer)value;
    }

}

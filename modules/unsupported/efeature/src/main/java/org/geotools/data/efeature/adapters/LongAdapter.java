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
 */
public class LongAdapter extends org.eclipse.emf.query.conditions.numbers.NumberAdapter.LongAdapter {

    public static final org.geotools.data.efeature.adapters.LongAdapter 
        DEFAULT = new org.geotools.data.efeature.adapters.LongAdapter();

    /** Hide constructor */
    private LongAdapter() { /*NOP*/};
    
    @Override
    public long longValue(Object value) {
        return value instanceof Long ? ((Long)value).longValue() : (Long)value;
    }

}

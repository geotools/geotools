/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wcs.v2_0;

import java.util.Map;

import net.opengis.wcs20.RangeIntervalType;
import net.opengis.wcs20.RangeItemType;
import net.opengis.wcs20.RangeSubsetType;
import net.opengis.wcs20.Wcs20Factory;

import org.geotools.xml.ComplexEMFBinding;
import org.geotools.xml.Configuration;

/**
 * Parser configuration for the http://www.opengis.net/wcs/range-subsetting/1.0 schema.
 * 
 * @generated
 */
public class RangeSubsetConfiguration extends Configuration {

    /**
     * Creates a new configuration.
     * 
     * @generated
     */
    public RangeSubsetConfiguration() {
        super(RangeSubset.getInstance());
    }

    /**
     * Registers the bindings for the configuration.
     * 
     * @generated
     */
    @SuppressWarnings("unchecked")
    protected final void registerBindings(Map bindings) {
        // manually setup bindings
        bindings.put(WCS.rangeSubsetType, new ComplexEMFBinding(Wcs20Factory.eINSTANCE,
                WCS.rangeSubsetType, RangeSubsetType.class));
        bindings.put(WCS.rangeIntervalType, new ComplexEMFBinding(Wcs20Factory.eINSTANCE,
                WCS.rangeIntervalType, RangeIntervalType.class));
        bindings.put(WCS.rangeItemType, new ComplexEMFBinding(Wcs20Factory.eINSTANCE,
                WCS.rangeItemType, RangeItemType.class));
    }

}
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

import net.opengis.wcs20.ScaleAxisByFactorType;
import net.opengis.wcs20.ScaleAxisType;
import net.opengis.wcs20.ScaleByFactorType;
import net.opengis.wcs20.ScaleToExtentType;
import net.opengis.wcs20.ScaleToSizeType;
import net.opengis.wcs20.ScalingType;
import net.opengis.wcs20.TargetAxisExtentType;
import net.opengis.wcs20.TargetAxisSizeType;
import net.opengis.wcs20.Wcs20Factory;

import org.geotools.xml.ComplexEMFBinding;
import org.geotools.xml.Configuration;

/**
 * Parser configuration for the http://www.opengis.net/WCS_service-extension_scaling/1.0 schema.
 * 
 * @generated
 */
public class ScalingConfiguration extends Configuration {

    /**
     * Creates a new configuration.
     * 
     * @generated
     */
    public ScalingConfiguration() {
        super(Scaling.getInstance());
    }
    
    /**
     * Registers the bindings for the configuration.
     * 
     * @generated
     */
    @SuppressWarnings("unchecked")
    protected final void registerBindings(Map bindings) {
        // manually setup bindings
        bindings.put(Scaling.ScalingType, new ComplexEMFBinding(Wcs20Factory.eINSTANCE,
                Scaling.ScalingType, ScalingType.class));
        bindings.put(Scaling.ScaleByFactorType, new ComplexEMFBinding(Wcs20Factory.eINSTANCE,
                Scaling.ScaleByFactorType, ScaleByFactorType.class));
        bindings.put(Scaling.ScaleAxesByFactorType, new ComplexEMFBinding(Wcs20Factory.eINSTANCE,
                Scaling.ScaleAxesByFactorType, ScaleAxisByFactorType.class));
        bindings.put(Scaling.ScaleAxisType, new ComplexEMFBinding(Wcs20Factory.eINSTANCE,
                Scaling.ScaleAxisType, ScaleAxisType.class));
        bindings.put(Scaling.ScaleToSizeType, new ComplexEMFBinding(Wcs20Factory.eINSTANCE,
                Scaling.ScaleToSizeType, ScaleToSizeType.class));
        bindings.put(Scaling.TargetAxisSizeType, new ComplexEMFBinding(Wcs20Factory.eINSTANCE,
                Scaling.TargetAxisSizeType, TargetAxisSizeType.class));
        bindings.put(Scaling.ScaleToExtentType, new ComplexEMFBinding(Wcs20Factory.eINSTANCE,
                Scaling.ScaleToExtentType, ScaleToExtentType.class));
        bindings.put(Scaling.TargetAxisExtentType, new ComplexEMFBinding(Wcs20Factory.eINSTANCE,
                Scaling.TargetAxisExtentType, TargetAxisExtentType.class));
    }

}
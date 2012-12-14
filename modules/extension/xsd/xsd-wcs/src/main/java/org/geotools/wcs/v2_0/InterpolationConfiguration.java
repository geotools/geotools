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

import net.opengis.wcs20.Wcs20Factory;

import org.geotools.xml.ComplexEMFBinding;
import org.geotools.xml.Configuration;

/**
 * Parser configuration for the http://www.opengis.net/WCS_service-extension_interpolation/1.0
 * schema.
 * 
 * @generated
 */
public class InterpolationConfiguration extends Configuration {

    /**
     * Creates a new configuration.
     * 
     * @generated
     */
    public InterpolationConfiguration() {
        super(Interpolation.getInstance());
    }

    /**
     * Registers the bindings for the configuration.
     * 
     * @generated
     */
    @SuppressWarnings("unchecked")
    protected final void registerBindings(Map bindings) {
        // manually setup bindings
        bindings.put(Interpolation.InterpolationType, new ComplexEMFBinding(Wcs20Factory.eINSTANCE,
                Interpolation.InterpolationType));
        bindings.put(Interpolation.InterpolationMethodType, new ComplexEMFBinding(
                Wcs20Factory.eINSTANCE, Interpolation.InterpolationMethodType));
        bindings.put(Interpolation.InterpolationAxesType, new ComplexEMFBinding(
                Wcs20Factory.eINSTANCE, Interpolation.InterpolationAxesType));
        bindings.put(Interpolation.InterpolationAxisType, new ComplexEMFBinding(
                Wcs20Factory.eINSTANCE, Interpolation.InterpolationAxisType));
    }

}
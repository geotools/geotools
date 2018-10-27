/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wfs.v1_1;

import org.geotools.filter.v1_1.OGCConfiguration;
import org.geotools.wfs.bindings.InsertResultsTypeBinding;
import org.geotools.wfs.bindings.LockFeatureResponseTypeBinding;
import org.geotools.wfs.bindings.OperationsTypeBinding;
import org.geotools.wfs.bindings.TransactionResponseTypeBinding;
import org.geotools.wfs.bindings.TransactionResultsTypeBinding;
import org.geotools.xsd.ows.OWSConfiguration;

/**
 * Parser configuration for the wfs 1.1 schema.
 *
 * @generated
 */
public class WFSConfiguration extends org.geotools.wfs.WFSConfiguration {
    /**
     * Creates a new configuration.
     *
     * @generated
     */
    public WFSConfiguration() {
        super(WFS.getInstance());

        addDependency(new OWSConfiguration());
        addDependency(new OGCConfiguration());
    }

    protected void configureBindings(org.picocontainer.MutablePicoContainer container) {
        super.configureBindings(container);

        container.registerComponentImplementation(
                WFS.TransactionResponseType, TransactionResponseTypeBinding.class);
        container.registerComponentImplementation(
                WFS.InsertResultsType, InsertResultsTypeBinding.class);
        container.registerComponentImplementation(
                WFS.TransactionResultsType, TransactionResultsTypeBinding.class);
        container.registerComponentImplementation(
                WFS.LockFeatureResponseType, LockFeatureResponseTypeBinding.class);
        container.registerComponentImplementation(WFS.OperationsType, OperationsTypeBinding.class);

        // override feature collection binding
        container.registerComponentImplementation(
                WFS.FeatureCollectionType, FeatureCollectionTypeBinding.class);
    }
}

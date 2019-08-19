/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg.wps.xml;

import org.geotools.filter.v2_0.FESConfiguration;
import org.geotools.xsd.Configuration;
import org.picocontainer.MutablePicoContainer;

/**
 * Parser configuration for the http://www.opengis.net/gpkg schema.
 *
 * @generated
 */
public class GPKGConfiguration extends Configuration {

    /**
     * Creates a new configuration.
     *
     * @generated
     */
    public GPKGConfiguration() {
        super(GPKG.getInstance());

        addDependency(new FESConfiguration());
    }

    /**
     * Registers the bindings for the configuration.
     *
     * @generated
     */
    protected final void registerBindings(MutablePicoContainer container) {
        // Types
        container.registerComponentImplementation(GPKG.coveragetype, CoveragetypeBinding.class);
        container.registerComponentImplementation(GPKG.geopkgtype, GeopkgtypeBinding.class);
        container.registerComponentImplementation(GPKG.gridsettype, GridsettypeBinding.class);
        container.registerComponentImplementation(GPKG.gridtype, GridtypeBinding.class);
        container.registerComponentImplementation(GPKG.layertype, LayertypeBinding.class);
        container.registerComponentImplementation(
                GPKG.geopkgtype_features, Geopkgtype_featuresBinding.class);
        container.registerComponentImplementation(
                GPKG.geopkgtype_tiles, Geopkgtype_tilesBinding.class);
        container.registerComponentImplementation(
                GPKG.gridsettype_grids, Gridsettype_gridsBinding.class);
        container.registerComponentImplementation(GPKG.bboxtype, BboxtypeBinding.class);
    }
}

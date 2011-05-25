/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2010, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.gce.imagemosaic.processing;

import java.awt.image.renderable.RenderedImageFactory;
import javax.media.jai.OperationDescriptor;
import javax.media.jai.OperationRegistry;
import javax.media.jai.OperationRegistrySpi;
import javax.media.jai.registry.RenderedRegistryMode;

/**
 * OperationRegistrySpi implementation to register the "ArtifactsFilter"
 * operation and its associated image factories.
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/plugin/imagemosaic/src/main/java/org/geotools/gce/imagemosaic/processing/ArtifactsFilterSpi.java $
 */
public class ArtifactsFilterSpi implements OperationRegistrySpi {

    /** The name of the product to which these operations belong. */
    private String productName = "org.geotools.gce.imagemosaic.processing";
 
    /** Default constructor. */
    public ArtifactsFilterSpi() {}

    /**
     * Registers the ArtifactsFilter operation
     *
     * @param registry The registry with which to register the operation
     */
    public void updateRegistry(OperationRegistry registry) {
        final OperationDescriptor op = new ArtifactsFilterDescriptor();
        registry.registerDescriptor(op);
        final String descName = op.getName();
        final RenderedImageFactory rif = new ArtifactsFilterRIF();
        registry.registerFactory(RenderedRegistryMode.MODE_NAME, descName,  productName, rif);

    }
}

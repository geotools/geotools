/*
 *  Copyright (C) 2010 GeoSolutions S.A.S.
 *  http://www.geo-solutions.it
 *
 *  GPLv3 + Classpath exception
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
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

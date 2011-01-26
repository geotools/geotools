/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic;

import org.geotools.gce.imagemosaic.catalog.GranuleCatalog;

/**
 * A class which can be used as input of an {@link ImageMosaicReader} to describe
 * an available mosaic without the need to access an external file such as a shapeFile,
 * a property file, a data directory or something else.
 * 
 * @author Daniele Romagnoli, GeoSolutions S.A.S.
 * @author Simone Giannecchini, GeoSolutions S.A.S.
 *
 */
public class ImageMosaicDescriptor {
    
    /** A {@link MosaicConfigurationBean} describing the common properties of each granule
     * composing the mosaic, such as number of overviews, resolution levels, suggested SPI. 
     */
    private MosaicConfigurationBean configuration;

    public MosaicConfigurationBean getConfiguration() {
        return configuration;
    }

    /**
     * A {@link GranuleCatalog} representing the real catalog of granules composing the mosaic
     * described by this {@link ImageMosaicDescriptor} instance.
     */
    private GranuleCatalog catalog;

    public GranuleCatalog getCatalog() {
        return catalog;
    }
    
    public ImageMosaicDescriptor(final MosaicConfigurationBean configurationBean, final GranuleCatalog catalog){
        this.configuration = new MosaicConfigurationBean(configurationBean);
        this.catalog = catalog;
    }
    
}

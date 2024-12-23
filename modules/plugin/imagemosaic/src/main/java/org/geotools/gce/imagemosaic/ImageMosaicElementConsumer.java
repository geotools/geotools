/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;

/**
 * An interface used to consume imageMosaic elements (i.e. File, URL) provided by an ImageMosaicWalker during its walk.
 * A check can be performed on the element before being handled.
 */
interface ImageMosaicElementConsumer<T> {

    /** Check that the granule can be consumed and added to the mosaic */
    boolean checkElement(T element, ImageMosaicWalker provider);

    /** Handle the provided element */
    void handleElement(T element, ImageMosaicWalker provider) throws IOException;
}

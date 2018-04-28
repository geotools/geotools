/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2016, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */

package org.geotools.gce.imagemosaic.granulecollector;

import java.io.IOException;
import java.util.List;
import org.geotools.gce.imagemosaic.GranuleDescriptor;
import org.geotools.gce.imagemosaic.MosaicElement;

/**
 * Responsible for creating subsets of the whole imagemosaic. Since there are frequently parts of a
 * mosaic that need to be handled differently, a submosaic producer is responsible for handling
 * those parts separately before they're added to the whole mosaic. For example a single dimension
 * can be handled on its own before being
 */
public interface SubmosaicProducer {
    boolean accept(GranuleDescriptor granuleDescriptor);

    List<MosaicElement> createMosaic() throws IOException;

    boolean doInputTransparency();

    boolean hasAlpha();

    double[][] getSourceThreshold();

    default boolean isReprojecting() {
        return false;
    }
}

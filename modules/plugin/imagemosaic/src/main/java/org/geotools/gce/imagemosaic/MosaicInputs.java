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

package org.geotools.gce.imagemosaic;

import java.util.List;

/**
 * This class represents the inputs for a mosaic JAI operation.
 *
 * <p>It contains
 *
 * <ol>
 *   <li>the images
 *   <li>their transparencies
 *   <li>the ROIs
 *   <li>source thresholds
 *   <li>indications on the alpha
 * </ol>
 *
 * @author Simone Giannecchini, GeoSolutions SAS
 */
public class MosaicInputs {

    private final boolean doInputTransparency;

    private final boolean hasAlpha;

    private final List<MosaicElement> sources;

    private final double[][] sourceThreshold;

    public MosaicInputs(
            boolean doInputTransparency,
            boolean hasAlpha,
            List<MosaicElement> sources,
            double[][] sourceThreshold) {
        this.doInputTransparency = doInputTransparency;
        this.hasAlpha = hasAlpha;
        this.sources = sources;
        this.sourceThreshold = sourceThreshold;
    }

    public boolean isDoInputTransparency() {
        return doInputTransparency;
    }

    public boolean isHasAlpha() {
        return hasAlpha;
    }

    public List<MosaicElement> getSources() {
        return sources;
    }

    public double[][] getSourceThreshold() {
        return sourceThreshold;
    }
}

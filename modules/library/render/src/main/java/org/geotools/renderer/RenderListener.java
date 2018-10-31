/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer;

import org.opengis.feature.simple.SimpleFeature;

/**
 * A RenderListener is notified each time a feature is rendered and each time an error occurs during
 * rendering. Therefore <b>VERY LITTLE WORK</b> should be done in the listener!!!
 *
 * @author jeichar
 */
public interface RenderListener {

    /**
     * Reports that a specific feature has been rendered. The same feature might be reported
     * multiple times, if
     *
     * @param feature
     */
    public void featureRenderer(SimpleFeature feature);

    /**
     * Reports a rendering error. The rendering is not normally stopped on it, a listener that wants
     * to stop it can call {@link GTRenderer#stopRendering()}
     *
     * @param e
     */
    public void errorOccurred(Exception e);
}

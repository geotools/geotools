/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data;


/**
 * Interface to be implemented by all listeners of FeatureEvents.
 *
 * <p>
 * Event notification is based on p[roviding the Envelope of the modification
 * (if known).
 * </p>
 *
 * @author Jody Garnett, Refractions Research, Inc.
 * @since GeoTools 2.0
 * @source $URL$
 */
public interface FeatureListener extends java.util.EventListener {
    /**
     * Gets called when a FeatureEvent is fired.
     *
     * <p>
     * Typically fired to signify that a change has occurred in the DataStore
     * backing the FeatureSource.
     * </p>
     *
     * @param featureEvent The FeatureEvent being fired
     */
    void changed(FeatureEvent featureEvent);
}

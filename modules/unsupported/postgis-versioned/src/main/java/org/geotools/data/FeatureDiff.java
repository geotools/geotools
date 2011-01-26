/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.List;

import org.opengis.feature.simple.SimpleFeature;

/**
 * Represents the changes occurred between two versions of the same simple feature
 * 
 * @author Andrea Aime - OpenGeo
 * @since 2.6
 * 
 */
public interface FeatureDiff {

    /**
     * Feature does not exists in fromVersion, has been created in the meantime (change map contains
     * all attributes in this case)
     */
    public static final int INSERTED = 0;

    /**
     * Feature exists in both versions, but has been modified
     */
    public static final int UPDATED = 1;

    /**
     * Feature existed in fromVersion, but has been deleted (change map is empty)
     */
    public static final int DELETED = 2;

    /**
     * Returns a read only list of modified attribute names if state is {@link #UPDATED}, an empty
     * list otherwise
     * 
     * @return
     */
    public List getChangedAttributes();

    /**
     * The feature ID
     * 
     * @return
     */
    public String getID();

    /**
     * The type of difference, either::
     * <ul>
     * <li>{@link #UPDATED}</li>
     * <li>{@link #INSERTED}</li>
     * <li>{@link #DELETED}</li>
     * </ul>
     * 
     * @return
     */
    public int getState();

    /**
     * Returns the inserted feature, if the state is {@link #INSERTED}, the new feature, if the
     * state is {@link #UPDATED}, null otherwise
     */
    public SimpleFeature getFeature();

    /**
     * Returns the old feature, if the state is {@link #UPDATED} or {@link #DELETED}, null otherwise
     */
    public SimpleFeature getOldFeature();
}

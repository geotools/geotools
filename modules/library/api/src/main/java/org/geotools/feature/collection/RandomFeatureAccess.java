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
package org.geotools.feature.collection;

import java.util.NoSuchElementException;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.opengis.feature.simple.SimpleFeature;


/**
 * Access Feature content using Feature "Id".
 * <p>
 * Many SimpleFeatureCollection classes will make use of this
 * API to avoid unnecessary caching of content. Supporting
 * this interface will allow SubCollections to occur based
 * on FeatureIds, with a suitable improvement in memory
 * consumption.
 * </p>
 * <p>
 * For an addition improvement in memory comsumption SubCollections
 * may use of a sparse reprsentation where only (beginId,endId] ranges
 * are kept in memory.
 * </p>
 * @author Jody Garnett, Refractions Research Inc.
 * @source $URL$
 */
public interface RandomFeatureAccess extends SimpleFeatureCollection {
    /**
     * Access Feature content by feature id.
     *
     * @param id
     * @return Feature with the indicated or id
     * @throws NoSuchElementException if a Feature with the indicated id is not present
     */
    public SimpleFeature getFeatureMember(String id) throws NoSuchElementException;

    /** Optional Method */
    public SimpleFeature removeFeatureMember(String id);
}

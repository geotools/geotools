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
package org.geotools.data.shapefile;

import org.geotools.data.QueryCapabilities;
import org.opengis.filter.sort.SortBy;

/**
 * The usual capabilities, plug a twist: we always return features sorted according to the
 * feature id
 * 
 * @author Andrea Aime - GeoSolutions
 */
class ShapefileQueryCapabilities extends QueryCapabilities {

    @Override
    public boolean supportsSorting(SortBy[] sortAttributes) {
        // we always return features in the pk increasing order
        if(sortAttributes != null && sortAttributes.length == 1 && sortAttributes[0] == SortBy.NATURAL_ORDER) {
            return true;
        } else {
            return super.supportsSorting(sortAttributes);
        }
    }  
}

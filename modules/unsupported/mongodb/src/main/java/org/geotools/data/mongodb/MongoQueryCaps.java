package org.geotools.data.mongodb;

import org.geotools.data.QueryCapabilities;
import org.opengis.filter.sort.SortBy;

/**
 * 
 * @author Gerald Gay, Data Tactics Corp.
 * @source $URL$
 * 
 *         (C) 2011, Open Source Geospatial Foundation (OSGeo)
 * 
 * @see The GNU Lesser General Public License (LGPL)
 */
/* This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; either version
 * 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with this library;
 * if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301 USA */
public class MongoQueryCaps extends QueryCapabilities
{

    public MongoQueryCaps ()
    {
    }

    // TODO implement sorting...
    @Override
    public boolean supportsSorting (SortBy[] sortAttributes)
    {
        return false;
    }

}

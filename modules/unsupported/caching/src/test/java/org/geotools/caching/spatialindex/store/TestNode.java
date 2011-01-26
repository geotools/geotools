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
package org.geotools.caching.spatialindex.store;

import org.geotools.caching.grid.spatialindex.GridNode;
import org.geotools.caching.grid.spatialindex.GridSpatialIndex;
import org.geotools.caching.spatialindex.Region;
import org.geotools.caching.spatialindex.RegionNodeIdentifier;


class TestNode extends GridNode {
    /**
     *
     */
    private static final long serialVersionUID = 6760237342477762805L;

    TestNode() {
        super();
    }

    TestNode(GridSpatialIndex grid, Region mbr) {
        super(new RegionNodeIdentifier(mbr));
    }
}

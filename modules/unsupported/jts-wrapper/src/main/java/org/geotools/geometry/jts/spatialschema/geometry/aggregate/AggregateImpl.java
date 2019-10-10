/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
/*$************************************************************************************************
 **
 ** $Id$
 **
 ** $Source: /cvs/ctree/LiteGO1/src/jar/com/polexis/lite/spatialschema/geometry/aggregate/AggregateImpl.java,v $
 **
 ** Copyright (C) 2003 Open GIS Consortium, Inc. All Rights Reserved. http://www.opengis.org/Legal/
 **
 *************************************************************************************************/
package org.geotools.geometry.jts.spatialschema.geometry.aggregate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.geotools.geometry.jts.spatialschema.geometry.GeometryImpl;
import org.geotools.geometry.jts.spatialschema.geometry.JTSGeometry;
import org.geotools.geometry.jts.spatialschema.geometry.JTSUtils;
import org.opengis.geometry.aggregate.Aggregate;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public abstract class AggregateImpl extends GeometryImpl implements Aggregate {
    private Set /*<Primitive>*/ elements;

    public AggregateImpl() {
        super();
    }

    public AggregateImpl(final CoordinateReferenceSystem crs) {
        super(crs);
        this.elements = new HashSet();
    }

    protected org.locationtech.jts.geom.Geometry computeJTSPeer() {
        ArrayList childParts = new ArrayList();
        Iterator children = elements.iterator();
        while (children.hasNext()) {
            JTSGeometry jtsGeom = (JTSGeometry) children.next();
            childParts.add(jtsGeom.getJTSGeometry());
        }
        org.locationtech.jts.geom.Geometry result =
                JTSUtils.GEOMETRY_FACTORY.buildGeometry(childParts);
        return result;
    }

    public Set getElements() {
        return elements;
    }
}

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

import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.geometry.aggregate.Aggregate;

import org.geotools.geometry.jts.spatialschema.geometry.GeometryImpl;
import org.geotools.geometry.jts.JTSGeometry;
import org.geotools.geometry.jts.JTSUtils;


public abstract class AggregateImpl extends GeometryImpl implements Aggregate {
    private Set/*<Primitive>*/ elements;

    public AggregateImpl() {
        super();
    }

    public AggregateImpl(final CoordinateReferenceSystem crs) {
        super(crs);
        this.elements = new HashSet();
    }

    protected com.vividsolutions.jts.geom.Geometry computeJTSPeer() {
        ArrayList childParts = new ArrayList();
        Iterator children = elements.iterator();
        while (children.hasNext()) {
            JTSGeometry jtsGeom = (JTSGeometry) children.next();
            childParts.add(jtsGeom.getJTSGeometry());
        }
        com.vividsolutions.jts.geom.Geometry result =
            JTSUtils.GEOMETRY_FACTORY.buildGeometry(childParts);
        return result;
    }

    public Set getElements() {
        return elements;
    }
}

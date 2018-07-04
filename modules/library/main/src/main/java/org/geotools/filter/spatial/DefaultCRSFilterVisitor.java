/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2007, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.spatial;

import org.geotools.filter.visitor.DuplicatingFilterVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.spatial.BBOX;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.SingleCRS;

/**
 * Returns a clone of the provided filter where all geometries and bboxes that do not have a CRS
 * gets the specified default one.
 *
 * @author Andrea Aime - The Open Planning Project
 * @source $URL$
 */
public class DefaultCRSFilterVisitor extends DuplicatingFilterVisitor {
    private CoordinateReferenceSystem defaultCrs;

    public DefaultCRSFilterVisitor(FilterFactory2 factory, CoordinateReferenceSystem defaultCrs) {
        super(factory);
        this.defaultCrs = defaultCrs;
    }

    public Object visit(BBOX filter, Object extraData) {
        // if no srs is specified we can't transform anyways
        String srs = filter.getSRS();
        if (srs != null && !"".equals(srs.trim())) return super.visit(filter, extraData);

        if (defaultCrs == null
                || filter.getBounds() == null
                || defaultCrs.getCoordinateSystem().getDimension()
                        == filter.getBounds().getDimension()) {
            return getFactory(extraData)
                    .bbox(
                            filter.getExpression1(),
                            ReferencedEnvelope.create(filter.getBounds(), defaultCrs));
        } else {
            try {
                SingleCRS horizontalCRS = CRS.getHorizontalCRS(defaultCrs);
                ReferencedEnvelope bounds =
                        ReferencedEnvelope.create(filter.getBounds(), horizontalCRS);
                return getFactory(extraData).bbox(filter.getExpression1(), bounds);
            } catch (Exception e) {
                throw new RuntimeException("Could not decode srs '" + srs + "'", e);
            }
        }
    }

    public Object visit(Literal expression, Object extraData) {
        if (!(expression.getValue() instanceof Geometry)) return super.visit(expression, extraData);

        // check if reprojection is needed
        Geometry geom = (Geometry) expression.getValue();
        if (geom.getUserData() != null && geom.getUserData() instanceof CoordinateReferenceSystem)
            return super.visit(expression, extraData);

        // clone the geometry and assign the new crs
        Geometry clone = geom.getFactory().createGeometry(geom);
        clone.setUserData(defaultCrs);

        // clone
        return ff.literal(clone);
    }
}

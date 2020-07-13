/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.filter.text.ecql;

import java.util.List;
import org.geotools.filter.text.commons.BuildResultStack;
import org.geotools.filter.text.cql2.CQLException;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;

/**
 * Builds a {@link GeometryCollection} using the
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.6
 */
final class GeometryCollectionBuilder extends GeometryBuilder {

    /** */
    public GeometryCollectionBuilder(String statement, BuildResultStack resultStack) {
        super(statement, resultStack);
    }

    @Override
    public Geometry build(int jjtgeometryliteral) throws CQLException {

        List<Geometry> geometryList = popGeometryLiteral(jjtgeometryliteral);

        Geometry[] geometries = geometryList.toArray(new Geometry[geometryList.size()]);

        GeometryCollection geometryCollection =
                getGeometryFactory().createGeometryCollection(geometries);

        return geometryCollection;
    }
}

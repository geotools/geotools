/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.crs;

import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.projection.Homolosine;
import org.geotools.referencing.operation.projection.MapProjection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Returns a {@link ProjectionHandler} for the {@link Homolosine} projection that will cut
 * geometries on interruptions before reprojection
 *
 * @author Andrea Aime - GeoSolutions
 */
public class HomolosineHandlerFactory implements ProjectionHandlerFactory {

    static final double EPS = 0.00001;

    @Override
    public ProjectionHandler getHandler(
            ReferencedEnvelope renderingEnvelope,
            CoordinateReferenceSystem sourceCrs,
            boolean wrap,
            int maxWraps)
            throws FactoryException {
        if (renderingEnvelope == null) {
            return null;
        }

        MapProjection mapProjection =
                CRS.getMapProjection(renderingEnvelope.getCoordinateReferenceSystem());
        if (mapProjection instanceof Homolosine) {
            final double[] MASK_COORDS = {
                // north side
                -180,
                -90,
                -180,
                90,
                -40 - EPS,
                90,
                -40 - EPS,
                0,
                -40 + EPS,
                0,
                -40 + EPS,
                90,
                180,
                90,
                // south side
                180,
                -90,
                80 + EPS,
                -90,
                80 + EPS,
                0,
                80 - EPS,
                0,
                80 - EPS,
                -90,
                -20 + EPS,
                -90,
                -20 + EPS,
                0,
                -20 - EPS,
                0,
                -20 - EPS,
                -90,
                -100 + EPS,
                -90,
                -100 + EPS,
                0,
                -100 - EPS,
                0,
                -100 - EPS,
                -90,
                -180,
                -90
            };
            LiteCoordinateSequence cs = new LiteCoordinateSequence(MASK_COORDS, 2);
            GeometryFactory gf = new GeometryFactory();
            LinearRing shell = gf.createLinearRing(cs);
            Polygon mask = gf.createPolygon(shell);

            return new ProjectionHandler(sourceCrs, mask, renderingEnvelope);
        }

        return null;
    }
}

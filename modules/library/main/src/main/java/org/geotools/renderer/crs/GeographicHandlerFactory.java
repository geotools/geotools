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
package org.geotools.renderer.crs;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.CRS.AxisOrder;
import org.geotools.referencing.operation.projection.Mercator;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.operation.MathTransform;

/** Returns a {@link ProjectionHandler} for any {@link GeographicCRS} */
public class GeographicHandlerFactory implements ProjectionHandlerFactory {

    static final double MAX_LATITUDE = 89.99;
    static final double MIN_LATITUDE = -89.99;

    public ProjectionHandler getHandler(
            ReferencedEnvelope renderingEnvelope,
            CoordinateReferenceSystem sourceCrs,
            boolean wrap,
            int maxWraps)
            throws FactoryException {
        CoordinateReferenceSystem crs = renderingEnvelope.getCoordinateReferenceSystem();
        if (renderingEnvelope != null && crs instanceof GeographicCRS) {
            GeographicCRS geogCrs = (GeographicCRS) CRS.getHorizontalCRS(crs);
            CoordinateReferenceSystem horizontalSourceCrs = CRS.getHorizontalCRS(sourceCrs);

            ReferencedEnvelope validArea = null;
            if (horizontalSourceCrs instanceof GeographicCRS
                    && !CRS.equalsIgnoreMetadata(horizontalSourceCrs, geogCrs)) {
                // datum shifts will create unpleasant effects if we have the poles in the mix,
                // cut them out
                if (!CRS.equalsIgnoreMetadata(horizontalSourceCrs, geogCrs)) {
                    if (CRS.getAxisOrder(sourceCrs) == AxisOrder.NORTH_EAST) {
                        validArea =
                                new ReferencedEnvelope(
                                        MIN_LATITUDE,
                                        MAX_LATITUDE,
                                        Float.MAX_VALUE,
                                        -Float.MAX_VALUE,
                                        horizontalSourceCrs);
                    } else {
                        validArea =
                                new ReferencedEnvelope(
                                        Integer.MAX_VALUE,
                                        -Integer.MAX_VALUE,
                                        MIN_LATITUDE,
                                        MAX_LATITUDE,
                                        horizontalSourceCrs);
                    }
                }
            }

            if (wrap && maxWraps > 0) {
                double centralMeridian =
                        geogCrs.getDatum().getPrimeMeridian().getGreenwichLongitude();
                WrappingProjectionHandler handler =
                        new WrappingProjectionHandler(
                                renderingEnvelope, validArea, sourceCrs, centralMeridian, maxWraps);
                handler.setDatelineWrappingCheckEnabled(
                        !isWrappingException(crs, horizontalSourceCrs));
                return handler;
            } else {
                return new ProjectionHandler(sourceCrs, validArea, renderingEnvelope);
            }
        }

        return null;
    }

    private boolean isWrappingException(
            CoordinateReferenceSystem sourceCrs, CoordinateReferenceSystem targetCRS)
            throws FactoryException {
        MathTransform mt = CRS.findMathTransform(sourceCrs, targetCRS);
        // this projection does not wrap coordinates, generates values larger than 180 instead
        if (mt instanceof Mercator) {
            return true;
        }

        return false;
    }
}

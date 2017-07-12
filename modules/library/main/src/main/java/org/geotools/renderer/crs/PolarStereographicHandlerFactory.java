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

import java.util.ArrayList;
import java.util.List;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.projection.MapProjection;
import org.geotools.referencing.operation.projection.MapProjection.AbstractProvider;
import org.geotools.referencing.operation.projection.PolarStereographic;
import org.geotools.referencing.operation.projection.TransverseMercator;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

/**
 * Returns a {@link ProjectionHandler} for the {@link TransverseMercator} projection
 * that will cut geometries 45° away from the projection central meridian  
 * 
 * @author Andrea Aime - OpenGeo
 *
 *
 * @source $URL$
 */
public class PolarStereographicHandlerFactory implements ProjectionHandlerFactory {

    public ProjectionHandler getHandler(ReferencedEnvelope renderingEnvelope, CoordinateReferenceSystem sourceCrs, boolean wrap, int maxWraps) throws FactoryException {
        MapProjection mapProjection = CRS.getMapProjection(renderingEnvelope
                .getCoordinateReferenceSystem());
        if (renderingEnvelope != null && mapProjection instanceof PolarStereographic) {
            final boolean north;
            // variant B uses standard_parallel
            ParameterValue<?> stdParallel = null;
            try {
                stdParallel = mapProjection.getParameterValues().parameter(
                        AbstractProvider.STANDARD_PARALLEL_1.getName().getCode());
            } catch (ParameterNotFoundException e) {
                // ignore
            }
            if(stdParallel != null) {
                north = stdParallel.doubleValue() > 0;
            } else {
                // variant A uses latitude of origin
                ParameterValue<?> latOrigin = null;
                try {
                    latOrigin = mapProjection.getParameterValues().parameter(
                            AbstractProvider.LATITUDE_OF_ORIGIN.getName().getCode());
                } catch (ParameterNotFoundException e) {
                    // ignore
                }
                if(latOrigin != null) {
                    north = latOrigin.doubleValue() > 0;
                } else {
                    return null;
                }
            }

            ReferencedEnvelope validArea;
            if(north) {
                validArea = new ReferencedEnvelope(-Double.MAX_VALUE, Double.MAX_VALUE, -0, 90, DefaultGeographicCRS.WGS84);
            } else {
                validArea = new ReferencedEnvelope(-Double.MAX_VALUE, Double.MAX_VALUE, -90, 0, DefaultGeographicCRS.WGS84);
            }
            
            return new ProjectionHandler(sourceCrs, validArea, renderingEnvelope) {
                @Override
                public List<ReferencedEnvelope> getQueryEnvelopes() throws TransformException,
                        FactoryException {
                    // check if we are crossing the antimeridian and are fully below the pole,
                    // in this case we'd end up reading the full globe when we'd have to just
                    // read two portions near the dateline
                    List<ReferencedEnvelope> envelopes;
                    if (!north && renderingEnvelope.getMaxY() < 0
                            && renderingEnvelope.getMinX() < 0
                            && renderingEnvelope.getMaxX() > 0) {
                        ReferencedEnvelope e1 = new ReferencedEnvelope(renderingEnvelope.getMinX(),
                                -1e-6, renderingEnvelope.getMinY(), renderingEnvelope.getMaxY(),
                                renderingEnvelope.getCoordinateReferenceSystem());
                        ReferencedEnvelope e2 = new ReferencedEnvelope(1e-6,
                                renderingEnvelope.getMaxX(), renderingEnvelope.getMinY(),
                                renderingEnvelope.getMaxY(),
                                renderingEnvelope.getCoordinateReferenceSystem());
                        envelopes = new ArrayList<ReferencedEnvelope>();
                        envelopes.add(e1);
                        envelopes.add(e2);
                        reprojectEnvelopes(sourceCRS, envelopes);
                    } else if (north && renderingEnvelope.getMinY() > 0
                            && renderingEnvelope.getMinX() < 0 && renderingEnvelope.getMaxX() > 0) {
                        ReferencedEnvelope e1 = new ReferencedEnvelope(renderingEnvelope.getMinX(),
                                -1e-6, renderingEnvelope.getMinY(), renderingEnvelope.getMaxY(),
                                renderingEnvelope.getCoordinateReferenceSystem());
                        ReferencedEnvelope e2 = new ReferencedEnvelope(1e-6,
                                renderingEnvelope.getMaxX(), renderingEnvelope.getMinY(),
                                renderingEnvelope.getMaxY(),
                                renderingEnvelope.getCoordinateReferenceSystem());
                        envelopes = new ArrayList<ReferencedEnvelope>();
                        envelopes.add(e1);
                        envelopes.add(e2);
                        reprojectEnvelopes(sourceCRS, envelopes);
                    } else {
                        envelopes = super.getQueryEnvelopes();
                    }

                    return envelopes;
                }
            };
        }

        return null;
    }

}

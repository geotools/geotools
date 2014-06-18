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

import static org.geotools.referencing.crs.DefaultGeographicCRS.WGS84;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.transform.ConcatenatedTransform;
import org.geotools.referencing.operation.transform.GeocentricTransform;
import org.opengis.metadata.extent.GeographicBoundingBox;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.precision.EnhancedPrecisionOp;

/**
 * A class that can perform transformations on geometries to handle the singularity of the rendering
 * CRS, deal with geometries that are crossing the dateline, and eventually wrap them around to
 * produce a seamless continuous map effect.<p>
 * 
 * This basic implementation will cut the geometries that get outside of the area of validity of the
 * projection (as provided by the constructor)
 * 
 * WARNING: this API is not finalized and is meant to be used by StreamingRenderer only
 * @author Andrea Aime - OpenGeo
 *
 *
 * @source $URL$
 */
public class ProjectionHandler {

    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(ProjectionHandler.class);

    ReferencedEnvelope renderingEnvelope;
    
    final ReferencedEnvelope validAreaBounds;
    
    final Geometry validArea;
    
    final CoordinateReferenceSystem sourceCRS;

    final CoordinateReferenceSystem targetCRS;

    /**
     * Initializes a projection handler 
     * 
     * @param sourceCRS The source CRS
     * @param validArea The valid area (used to cut geometries that go beyond it)
     * @param renderingEnvelope The target rendering area and target CRS
     * 
     * @throws FactoryException
     */
    public ProjectionHandler(CoordinateReferenceSystem sourceCRS, Envelope validAreaBounds, ReferencedEnvelope renderingEnvelope) throws FactoryException {
        this.renderingEnvelope = renderingEnvelope;
        this.sourceCRS = sourceCRS;
        this.targetCRS = renderingEnvelope.getCoordinateReferenceSystem();
        this.validAreaBounds = validAreaBounds != null ? new ReferencedEnvelope(validAreaBounds,
                sourceCRS) : null;
        this.validArea = null;
    }
    
    /**
     * Initializes a projection handler 
     * 
     * @param sourceCRS The source CRS
     * @param validArea The valid area (used to cut geometries that go beyond it)
     * @param renderingEnvelope The target rendering area and target CRS
     * 
     * @throws FactoryException
     */
    public ProjectionHandler(CoordinateReferenceSystem sourceCRS, Geometry validArea, ReferencedEnvelope renderingEnvelope) throws FactoryException {
        if(validArea.isRectangle()) {
            this.renderingEnvelope = renderingEnvelope;
            this.sourceCRS = sourceCRS;
            this.targetCRS = renderingEnvelope.getCoordinateReferenceSystem();
            this.validAreaBounds = new ReferencedEnvelope(validArea.getEnvelopeInternal(),
                    sourceCRS);
            this.validArea = null;
        } else {
            this.renderingEnvelope = renderingEnvelope;
            this.sourceCRS = sourceCRS;
            this.targetCRS = renderingEnvelope.getCoordinateReferenceSystem();
            this.validAreaBounds = new ReferencedEnvelope(validArea.getEnvelopeInternal(),
                    sourceCRS);
            this.validArea = validArea;
        }
    }

    /**
     * Returns the current rendering envelope
     */
    public ReferencedEnvelope getRenderingEnvelope() {
        return renderingEnvelope;
    }
    
    public CoordinateReferenceSystem getSourceCRS() {
        return this.sourceCRS;
    }
    
    /**
     * Returns a set of envelopes that will be used to query the data given the specified rendering
     * envelope and the current query envelope
     */
    public List<ReferencedEnvelope> getQueryEnvelopes()
            throws TransformException, FactoryException {
        CoordinateReferenceSystem renderingCRS = renderingEnvelope.getCoordinateReferenceSystem();
        if(renderingCRS instanceof GeographicCRS && !CRS.equalsIgnoreMetadata(renderingCRS, WGS84)) {
            // special case, if we just transform the coordinates are going to be wrapped by the referencing
            // subsystem directly
            ReferencedEnvelope re = renderingEnvelope;
            List<ReferencedEnvelope> envelopes = new ArrayList<ReferencedEnvelope>();
            envelopes.add(re);

            if(CRS.getAxisOrder(renderingCRS) == CRS.AxisOrder.NORTH_EAST) {
                if (re.getMinY() >= -180.0 && re.getMaxY() <= 180) {
                    return Collections
                            .singletonList(transformEnvelope(renderingEnvelope, sourceCRS));
                }
                
                // We need to split reprojected envelope and normalize it. To be lenient with
                // situations in which the data is just broken (people saying 4326 just because they
                // have no idea at all) we don't actually split, but add elements
                if (re.getMinY() < -180) {
                    envelopes.add(new ReferencedEnvelope(re.getMinY() + 360, 180, re.getMinX(), re
                            .getMaxX(), re.getCoordinateReferenceSystem()));
                }
                if (re.getMaxY() > 180) {
                    envelopes.add(new ReferencedEnvelope(-180, re.getMaxY() - 360, re.getMinX(), re
                            .getMaxX(), re.getCoordinateReferenceSystem()));
                }
                
            } else {
                if (re.getMinX() >= -180.0 && re.getMaxX() <= 180) {
                    return Collections
                            .singletonList(transformEnvelope(renderingEnvelope, sourceCRS));
                }
            
                // We need to split reprojected envelope and normalize it. To be lenient with
                // situations in which the data is just broken (people saying 4326 just because they
                // have no idea at all) we don't actually split, but add elements
                
                if (re.getMinX() < -180) {
                    envelopes.add(new ReferencedEnvelope(re.getMinX() + 360, 180, re.getMinY(), re
                            .getMaxY(), re.getCoordinateReferenceSystem()));
                }
                if (re.getMaxX() > 180) {
                    envelopes.add(new ReferencedEnvelope(-180, re.getMaxX() - 360, re.getMinY(), re
                            .getMaxY(), re.getCoordinateReferenceSystem()));
                }
            }
    
            mergeEnvelopes(envelopes);
            reprojectEnvelopes(sourceCRS, envelopes);
    
            return envelopes;
            
            
        } else {
            // check if we are crossing the dateline
            ReferencedEnvelope re = transformEnvelope(renderingEnvelope, WGS84);
            if (re.getMinX() >= -180.0 && re.getMaxX() <= 180)
                return Collections.singletonList(transformEnvelope(renderingEnvelope, sourceCRS));
    
            // We need to split reprojected envelope and normalize it. To be lenient with
            // situations in which the data is just broken (people saying 4326 just because they
            // have no idea at all) we don't actually split, but add elements
            List<ReferencedEnvelope> envelopes = new ArrayList<ReferencedEnvelope>();
            envelopes.add(re);
            if (re.getMinX() < -180) {
                envelopes.add(new ReferencedEnvelope(re.getMinX() + 360, 180, re.getMinY(), re
                        .getMaxY(), re.getCoordinateReferenceSystem()));
            }
            if (re.getMaxX() > 180) {
                envelopes.add(new ReferencedEnvelope(-180, re.getMaxX() - 360, re.getMinY(), re
                        .getMaxY(), re.getCoordinateReferenceSystem()));
            }
    
            mergeEnvelopes(envelopes);
            reprojectEnvelopes(sourceCRS, envelopes);
    
            return envelopes;
        }
    }

    private ReferencedEnvelope transformEnvelope(ReferencedEnvelope envelope,
            CoordinateReferenceSystem targetCRS) throws TransformException, FactoryException {
        try {
            return envelope.transform(targetCRS, true, 10);
        } catch (Exception e) {
            LOGGER.fine("Failed to reproject the envelope " + envelope + " to " + targetCRS
                    + " trying an area restriction");

            ReferencedEnvelope envWGS84 = envelope.transform(DefaultGeographicCRS.WGS84, true);

            // let's see if we can restrict the area we're reprojecting back using a projection
            // handler for the source CRS
            ProjectionHandler handler = ProjectionHandlerFinder.getHandler(envelope,
                    envelope.getCoordinateReferenceSystem(), false);
            if (handler != null && handler.validAreaBounds != null) {

                Envelope intersection = envWGS84.intersection(validAreaBounds);
                if (intersection.isNull()) {
                    return null;
                } else {
                    try {
                        return ReferencedEnvelope.reference(intersection)
                                .transform(targetCRS, true);
                    } catch (Exception e2) {
                        LOGGER.fine("Failed to reproject the restricted envelope " + intersection
                                + " to " + targetCRS);
                    }

                }
            }

            // ok, let's see if we have an area of validity then
            GeographicBoundingBox bbox = CRS.getGeographicBoundingBox(targetCRS);
            if (bbox != null) {
                ReferencedEnvelope restriction = new ReferencedEnvelope(
                        bbox.getEastBoundLongitude(), bbox.getWestBoundLongitude(),
                        bbox.getSouthBoundLatitude(), bbox.getNorthBoundLatitude(),
                        DefaultGeographicCRS.WGS84);
                Envelope intersection = envWGS84.intersection(restriction);
                if (intersection.isNull()) {
                    return null;
                } else {
                    try {
                        return ReferencedEnvelope.reference(intersection)
                                .transform(targetCRS, true);
                    } catch (Exception e2) {
                        LOGGER.fine("Failed to reproject the restricted envelope " + intersection
                                + " to " + targetCRS);
                    }

                }

            }

            throw new TransformException("All attemptsto reproject the envelope " + envelope
                    + " to " + targetCRS + " failed");
        }
    }

    protected void reprojectEnvelopes(CoordinateReferenceSystem queryCRS,
            List<ReferencedEnvelope> envelopes) throws TransformException, FactoryException {
        // reproject the surviving envelopes
        for (int i = 0; i < envelopes.size(); i++) {
            envelopes.set(i, transformEnvelope(envelopes.get(i), queryCRS));
        }
    }

    private void mergeEnvelopes(List<ReferencedEnvelope> envelopes) {
        // the envelopes generated might overlap, check and merge if necessary, we
        // don't want the data backend to deal with ORs against the spatial index
        // unless necessary
        boolean merged = true;
        while (merged && envelopes.size() > 1) {
            merged = false;
            for (int i = 0; i < envelopes.size() - 1; i++) {
                ReferencedEnvelope curr = envelopes.get(i);
                for (int j = i + 1; j < envelopes.size();) {
                    ReferencedEnvelope next = envelopes.get(j);
                    if (curr.intersects((Envelope) next)) {
                        curr.expandToInclude(next);
                        envelopes.remove(j);
                        merged = true;
                    } else {
                        j++;
                    }
                }
            }
        }
    }

    /**
     * Returns true if the geometry needs special handling
     */
    public boolean requiresProcessing(Geometry geometry) {
        // if there is no valid area, no cutting is required
        if(validAreaBounds == null)
            return false;
        
        // if not reprojection is going on, we don't need to cut
        if (CRS.equalsIgnoreMetadata(sourceCRS, renderingEnvelope.getCoordinateReferenceSystem())) {
            return false;
        }
        
        return true;
    }

    /**
     * Pre processes the geometry, e.g. cuts it, splits it, etc. in its native srs. May return null
     * if the geometry is not to be drawn
     */
    public Geometry preProcess(Geometry geometry) throws TransformException, FactoryException {
        // if there is no valid area, no cutting is required either
        if(validAreaBounds == null)
            return geometry;
        
        // if not reprojection is going on, we don't need to cut
        CoordinateReferenceSystem geometryCRS = CRS.getHorizontalCRS(sourceCRS);
        if (geometryCRS == null || CRS.equalsIgnoreMetadata(geometryCRS, renderingEnvelope.getCoordinateReferenceSystem())) {
            return geometry;
        }
        
        Geometry mask;
        // fast path for the rectangular case, more complex one for the
        // non rectangular one
        if(validArea == null) {
            // if the geometry is within the valid area for this projection
            // just skip expensive cutting
            ReferencedEnvelope ge = new ReferencedEnvelope(geometry.getEnvelopeInternal(), geometryCRS);
            ReferencedEnvelope geWGS84 = ge.transform(WGS84, true);
            if (validAreaBounds.contains((Envelope) geWGS84)) {
                return geometry;
            }

            // we need to cut, first thing, we intersect the geometry envelope
            // and the valid area in WGS84, which is a neutral, everything can
            // be turned into it, and then turn back the intersection into
            // the origin SRS
            ReferencedEnvelope envIntWgs84 = new ReferencedEnvelope(validAreaBounds.intersection(geWGS84), WGS84);
            
            // if the intersection is empty the geometry is completely outside of the valid area, skip it
            if(envIntWgs84.isEmpty()) {
                return null;
            }
                
            ReferencedEnvelope envInt = envIntWgs84.transform(geometryCRS, true);
            mask = JTS.toGeometry((Envelope) envInt);
        } else {
            // if the geometry is within the valid area for this projection
            // just skip expensive cutting
            ReferencedEnvelope ge = new ReferencedEnvelope(geometry.getEnvelopeInternal(), geometryCRS);
            ReferencedEnvelope geWGS84 = ge.transform(WGS84, true);

            // we need to cut, first thing, we intersect the geometry envelope
            // and the valid area in WGS84, which is a neutral, everything can
            // be turned into it, and then turn back the intersection into
            // the origin SRS
            ReferencedEnvelope envIntWgs84 = new ReferencedEnvelope(validAreaBounds.intersection(geWGS84), WGS84);
            
            // if the intersection is empty the geometry is completely outside of the valid area, skip it
            if(envIntWgs84.isEmpty()) {
                return null;
            } 
            
            Polygon polyIntWgs84 = JTS.toGeometry(envIntWgs84);
            Geometry maskWgs84 = intersect(validArea, polyIntWgs84);
            if(maskWgs84 == null || maskWgs84.isEmpty()) {
                return null;
            }
            mask = JTS.transform(maskWgs84, CRS.findMathTransform(WGS84, geometryCRS));
        }
        
        return intersect(geometry, mask);
    }

    private Geometry intersect(Geometry geometry, Geometry mask) {
        Geometry result;
        try {
            result = geometry.intersection(mask);
        } catch(Exception e1) {
            try {
                result = EnhancedPrecisionOp.intersection(geometry, mask);
            } catch(Exception e2) {
                result = geometry;
            }
        }
        
        // handle in special way empty intersections
        if (result instanceof GeometryCollection && ((GeometryCollection) result).isEmpty()) {
            return null;
        } else {
            return result;
        }
    }
    
    /**
     * Can modify/wrap the transform to handle specific projection issues
     * @return
     * @throws FactoryException 
     */
    public MathTransform getRenderingTransform(MathTransform mt) throws FactoryException {
        List<MathTransform> elements = new ArrayList<MathTransform>();
        accumulateTransforms(mt, elements);
        
        List<MathTransform> wrapped = new ArrayList<MathTransform>();
        List<MathTransform> datumShiftChain = null;
        boolean datumShiftDetected = false;
        for (MathTransform element : elements) {
            if(datumShiftChain != null) {
                datumShiftChain.add(element);
                if(element.getClass().getName().equals(GeocentricTransform.class.getName() + "$Inverse")) {
                    datumShiftDetected = true;
                    MathTransform combined = concatenateTransforms(datumShiftChain);
                    GeographicOffsetWrapper wrapper = new GeographicOffsetWrapper(combined);
                    wrapped.add(wrapper);
                    datumShiftChain = null;
                } 
            } else if(element instanceof GeocentricTransform) {
                datumShiftChain = new ArrayList<MathTransform>();
                datumShiftChain.add(element);
            } else {
                wrapped.add(element);
            }
        }
        
        if(datumShiftDetected) {
            if(datumShiftChain != null) {
                wrapped.addAll(datumShiftChain);
            }
            return concatenateTransforms(wrapped);
        } else {
            return mt;
        }
    }

    private MathTransform concatenateTransforms(List<MathTransform> datumShiftChain) {
		if(datumShiftChain.size() == 1) {
			return datumShiftChain.get(0);
		} else {
			MathTransform mt = ConcatenatedTransform.create(datumShiftChain.get(0), datumShiftChain.get(1));
			for (int i = 2; i < datumShiftChain.size(); i++) {
				MathTransform curr = datumShiftChain.get(i);
				mt = ConcatenatedTransform.create(mt, curr);
			}
			
			return mt;
		}
	}

	private void accumulateTransforms(MathTransform mt, List<MathTransform> elements) {
		if(mt instanceof ConcatenatedTransform) {
			ConcatenatedTransform ct = (ConcatenatedTransform) mt;
			accumulateTransforms(ct.transform1, elements);
			accumulateTransforms(ct.transform2, elements);
		} else {
			elements.add(mt);
		}
		
	}

	/**
     * Processes the geometry already projected to the target SRS. May return null if the geometry
     * is not to be drawn.
     * @param mt optional reverse transformation to facilitate unwrapping
     */
    public Geometry postProcess(MathTransform mt, Geometry geometry) {
        return geometry;
    }

    /**
     * Returns the area where the transformation from source to target is valid, expressed in the
     * source coordinate reference system, or null if there is no limit
     * 
     * @return
     */
    public ReferencedEnvelope getValidAreaBounds() {
        return validAreaBounds;
    }
    
}

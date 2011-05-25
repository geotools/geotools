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
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;

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
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/render/src/main/java/org/geotools/renderer/crs/ProjectionHandler.java $
 */
public class ProjectionHandler {

    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(ProjectionHandler.class);

    ReferencedEnvelope renderingEnvelope;
    
    final ReferencedEnvelope validArea;

    public ProjectionHandler(ReferencedEnvelope renderingEnvelope, ReferencedEnvelope validArea) {
        this.renderingEnvelope = renderingEnvelope;
        this.validArea = validArea;
    }

    /**
     * Returns the current rendering envelope
     */
    public ReferencedEnvelope getRenderingEnvelope() {
        return renderingEnvelope;
    }
    
    /**
     * Sets the current rendering envelope. Will be used to compute the envelopes to be queried,
     * and to perform map wrapping when enabled
     * @return
     */
    public void setRenderingEnvelope(ReferencedEnvelope renderingEnvelope) {
        this.renderingEnvelope = renderingEnvelope;
    }

    /**
     * Returns a set of envelopes that will be used to query the data given the specified rendering
     * envelope and the current query envelope
     */
    public List<ReferencedEnvelope> getQueryEnvelopes(CoordinateReferenceSystem queryCRS)
            throws TransformException, FactoryException {
        // check if we are crossing the dateline
        ReferencedEnvelope re = renderingEnvelope.transform(WGS84, true, 10);
        if (re.getMinX() >= -180.0 && re.getMaxX() <= 180)
            return Collections.singletonList(renderingEnvelope.transform(queryCRS, true, 10));

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

        // reproject the surviving envelopes
        for (int i = 0; i < envelopes.size(); i++) {
            envelopes.set(i, envelopes.get(i).transform(queryCRS, true, 10));
        }

        return envelopes;
    }

    /**
     * Returns true if the geometry needs special handling
     */
    public boolean requiresProcessing(CoordinateReferenceSystem geomCRS, Geometry geometry) {
        // if there is no valid area, no cutting is required
        if(validArea == null)
            return false;
        
        // if not reprojection is going on, we don't need to cut
        if (CRS.equalsIgnoreMetadata(geomCRS, renderingEnvelope.getCoordinateReferenceSystem())) {
            return false;
        }
        
        return true;
    }

    /**
     * Pre processes the geometry, e.g. cuts it, splits it, etc. in its native srs. May return null
     * if the geometry is not to be drawn
     */
    public Geometry preProcess(CoordinateReferenceSystem geomCRS, Geometry geometry) throws TransformException, FactoryException {
        // if not reprojection is going on, we don't need to cut
        if (CRS.equalsIgnoreMetadata(geomCRS, renderingEnvelope.getCoordinateReferenceSystem())) {
            return geometry;
        }
        
        // if there is no valid area, no cutting is required either
        if(validArea == null)
            return geometry;

        // if the geometry is within the valid area for this projection
        // just skip expensive cutting
        ReferencedEnvelope ge = new ReferencedEnvelope(geometry.getEnvelopeInternal(), geomCRS);
        ReferencedEnvelope geWGS84 = ge.transform(WGS84, true);
        if (validArea.contains((Envelope) geWGS84)) {
            return geometry;
        }

        // we need to cut, first thing, we intersect the geometry envelope
        // and the valid area in WGS84, which is a neutral, everything can
        // be turned into it, and then turn back the intersection into
        // the origin SRS
        ReferencedEnvelope envIntWgs84 = new ReferencedEnvelope(validArea.intersection(geWGS84), WGS84);
        
        // if the intersection is empty the geometry is completely outside of the valid area, skip it
        if(envIntWgs84.isEmpty())
            return null;
            
        ReferencedEnvelope envInt = envIntWgs84.transform(geomCRS, true);

        // turn the envelope into a geometry and perform the intersection
        Geometry result = geometry.intersection(JTS.toGeometry((Envelope) envInt));

        // handle in special way empty intersections
        if (result instanceof GeometryCollection && ((GeometryCollection) result).isEmpty())
            return null;
        else
            return result;
    }

    /**
     * Processes the geometry already projected to the target SRS. May return null if the geometry
     * is not to be drawn
     */
    public Geometry postProcess(Geometry geometry) {
        return geometry;
    }
    
}

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature.visitor;

import org.geotools.geometry.jts.ReferencedEnvelope;

import com.vividsolutions.jts.geom.Envelope;


/**
 * Calculates the extents (envelope) of the features it visits.
 *
 * @author Cory Horner, Refractions
 *
 * @since 2.2.M2
 *
 * @source $URL$
 */
public class BoundsVisitor implements FeatureCalc {
    ReferencedEnvelope bounds = new ReferencedEnvelope();    
        
    public void visit(org.opengis.feature.Feature feature) {
        bounds.include( feature.getBounds() );
    }

    public ReferencedEnvelope getBounds() {
        return bounds;
    }

    public void reset(Envelope bounds) {
        this.bounds = new ReferencedEnvelope();
    }

    public CalcResult getResult() {
    	if(bounds == null || bounds.isEmpty()) {
    		return CalcResult.NULL_RESULT;
    	}
        return new BoundsResult(bounds);
    }

    public static class BoundsResult extends AbstractCalcResult {
        private ReferencedEnvelope bbox;

        public BoundsResult(ReferencedEnvelope bbox) {
            this.bbox = bbox;
        }

        public ReferencedEnvelope getValue() {
            return new ReferencedEnvelope(bbox);
        }

        public boolean isCompatible(CalcResult targetResults) {
            //list each calculation result which can merge with this type of result
            if (targetResults instanceof BoundsResult || targetResults == CalcResult.NULL_RESULT) {
                return true;
            }

            return false;
        }

        public CalcResult merge(CalcResult resultsToAdd) {
            if (!isCompatible(resultsToAdd)) {
                throw new IllegalArgumentException(
                    "Parameter is not a compatible type");
            }
            
            if(resultsToAdd == CalcResult.NULL_RESULT) {
        		return this;
        	}

            if (resultsToAdd instanceof BoundsResult) {
                BoundsResult boundsToAdd = (BoundsResult) resultsToAdd;
                
                //add one set to the other (to create one big unique list)
                ReferencedEnvelope newBounds = new ReferencedEnvelope(bbox);
                newBounds.include( boundsToAdd.getValue());

                return new BoundsResult(newBounds);
            } else {
                throw new IllegalArgumentException(
                    "The CalcResults claim to be compatible, but the appropriate merge method has not been implemented.");
            }
        }
    }
}

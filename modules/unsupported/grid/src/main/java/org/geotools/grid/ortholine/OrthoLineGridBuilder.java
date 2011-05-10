/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.geotools.grid.ortholine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.grid.GridFeatureBuilder;

/**
 *
 * @author michael
 */
public class OrthoLineGridBuilder {
    private static final double TOL = 1.0e-8;

    private ReferencedEnvelope bounds;
    private boolean hasVerticals;
    private boolean hasHorizontals;
    private boolean densify;

    private SimpleFeatureBuilder featureBuilder;

    public void buildGrid(ReferencedEnvelope bounds, List<OrthoLineControl> controls,
            GridFeatureBuilder lineFeatureBuilder, double vertexSpacing, SimpleFeatureCollection fc) {
        
        init(bounds, controls, lineFeatureBuilder, vertexSpacing);
        
        List<OrthoLineControl> horizontal = new ArrayList<OrthoLineControl>();
        List<OrthoLineControl> vertical = new ArrayList<OrthoLineControl>();

        for (OrthoLineControl control : controls) {
            switch (control.getOrientation()) {
                case HORIZONTAL:
                    horizontal.add(control);
                    break;

                case VERTICAL:
                    vertical.add(control);
                    break;
            }
        } 

        doBuildLineFeatures(horizontal, LineOrientation.HORIZONTAL, 
                lineFeatureBuilder, densify, vertexSpacing, fc);
        doBuildLineFeatures(vertical, LineOrientation.VERTICAL, 
                lineFeatureBuilder, densify, vertexSpacing, fc);
    }

    private void doBuildLineFeatures(List<OrthoLineControl> controls, 
            LineOrientation orientation,
            GridFeatureBuilder lineFeatureBuilder, 
            boolean densify,
            double vertexSpacing,
            SimpleFeatureCollection fc) {

        final int NLINES = controls.size();
        if (NLINES > 0) {
            double minOrdinate, maxOrdinate;
            
            if (orientation == LineOrientation.HORIZONTAL) {
                minOrdinate = bounds.getMinY();
                maxOrdinate = bounds.getMaxY();
            } else {
                minOrdinate = bounds.getMinX();
                maxOrdinate = bounds.getMaxX();
            }
            
            double[] pos = new double[NLINES];
            boolean[] active = new boolean[NLINES];
            boolean[] atCurPos = new boolean[NLINES];
            boolean[] generate = new boolean[NLINES];

            Map<String, Object> attributes = new HashMap<String, Object>();
            String geomPropName = lineFeatureBuilder.getType().getGeometryDescriptor().getLocalName();
            
            for (int i = 0; i < NLINES; i++) {
                pos[i] = minOrdinate;
                active[i] = true;
            }
            
            int numActive = NLINES;
            while (numActive > 0) {
                /*
                 * Update scan position (curPos)
                 */
                double curPos = maxOrdinate;
                for (int i = 0; i < NLINES; i++) {
                    if (active[i] && pos[i] < curPos - TOL) {
                        curPos = pos[i];
                    }
                }

                /*
                 * Check which line elements are at the current scan position
                 */
                for (int i = 0; i < NLINES; i++) {
                    atCurPos[i] = active[i] && Math.abs(pos[i] - curPos) < TOL;
                }
                
                /*
                 * Get line with highest precedence for the current position
                 */
                System.arraycopy(atCurPos, 0, generate, 0, NLINES);
                for (int i = 0; i < NLINES - 1; i++) {
                    if (generate[i] && atCurPos[i]) {
                        for (int j = i + 1; j < NLINES; j++) {
                            if (generate[j] && atCurPos[j]) {
                                if (controls.get(i).getLevel() >= controls.get(j).getLevel()) {
                                    generate[j] = false;
                                } else {
                                    generate[i] = false;
                                    break;
                                }
                            }
                        }
                    } else {
                        generate[i] = false;
                    }
                }

                /*
                 * Create the line feature with highest precedence
                 */
                for (int i = 0; i < NLINES; i++) {
                    if (generate[i]) {
                        OrthoLine element = new OrthoLineImpl(
                                bounds, orientation, pos[i],
                                controls.get(i).getLevel(), new Double(pos[i]));

                        if (lineFeatureBuilder.getCreateFeature(element)) {
                            lineFeatureBuilder.setAttributes(element, attributes);

                        if (densify) {
                            featureBuilder.set(geomPropName, element.toDenseGeometry(vertexSpacing));
                        } else {
                            featureBuilder.set(geomPropName, element.toGeometry());
                        }

                        for (String propName : attributes.keySet()) {
                            featureBuilder.set(propName, attributes.get(propName));
                        }

                        fc.add(featureBuilder.buildFeature(lineFeatureBuilder.getFeatureID(element)));
                        }
                    }
                }

                /*
                 * Update line element positions 
                 */
                for (int i = 0; i < NLINES; i++) {
                    if (atCurPos[i]) {
                        pos[i] += controls.get(i).getSpacing();
                        if (pos[i] > maxOrdinate + TOL) {
                            active[i] = false;
                            numActive-- ;
                        }
                    }
                }
            }
        }
    }

    private boolean isValidDenseVertexSpacing(double v) {
        double minDim;

        if (hasVerticals) {
            if (hasHorizontals) {
                minDim = Math.min(bounds.getWidth(), bounds.getHeight());
            } else {
                minDim = bounds.getHeight();
            }
        } else {
            minDim = bounds.getWidth();
        }
        
        return v > 0 && v < minDim / 2;
    }

    private void init(ReferencedEnvelope bounds, 
            List<OrthoLineControl> controls,
            GridFeatureBuilder lineFeatureBuilder,
            double vertexSpacing) {

        if (bounds == null || bounds.isEmpty()) {
            throw new IllegalArgumentException("bounds must not be null or empty");
        }
        if (controls == null || controls.isEmpty()) {
            throw new IllegalArgumentException("required one or more line parameters");
        }
        
        this.bounds = bounds;
        
        for (OrthoLineControl param : controls) {
            if (param.getOrientation() == LineOrientation.HORIZONTAL) {
                hasHorizontals = true;
            } else if (param.getOrientation() == LineOrientation.VERTICAL) {
                hasVerticals = true;
            } else {
                throw new IllegalArgumentException(
                        "Only horizontal and vertical lines are supported");
            }
        }
        
        densify = isValidDenseVertexSpacing(vertexSpacing);
        featureBuilder = new SimpleFeatureBuilder(lineFeatureBuilder.getType());
    }
    
}

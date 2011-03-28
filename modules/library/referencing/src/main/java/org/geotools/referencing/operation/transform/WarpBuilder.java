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
package org.geotools.referencing.operation.transform;

import static java.lang.Math.*;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.jai.Warp;
import javax.media.jai.WarpAffine;
import javax.media.jai.WarpGrid;

import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.util.logging.Logging;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;

/**
 * Builds {@link Warp} objects that approximate a specified {@link MathTransform} in a certain
 * rectangular domain within the specified tolerance
 * 
 * @author Andrea Aime - GeoSolutions
 */
public class WarpBuilder {
    static final Logger LOGGER = Logging.getLogger(WarpBuilder.class);
    
    /**
     * A hint to dump grid to a property file for debugging and display purposes
     */
    static final boolean DUMP_GRIDS = Boolean.getBoolean("org.geotools.dump.warp.grid");
    
    /**
     * Used to compare numbers to 0 and integers in general
     */
    static final double EPS=1e-6;

    /**
     * The max distance tolerated between the actual projected point and the approximate version
     * built by the optimized warp transform
     */
    final double maxDistanceSquared;
    
    /**
     * The array used to perform all the reprojections
     */
    final double[] ordinates = new double[10];

    /**
     * Creates a new warp builder
     */
    public WarpBuilder(double tolerance) {
        if(tolerance >= 0) {
            this.maxDistanceSquared = tolerance * tolerance;
        } else {
            maxDistanceSquared = 0;
        }
    }

    /**
     * 
     * @param mt The math transform to be approximated
     * @param domain The domain in which the transform will be approximated
     * @param tolerance
     * @return
     */
    public Warp buildWarp(MathTransform2D mt, Rectangle domain) throws TransformException {
        // first simple case, the tx is affine
        if(mt instanceof AffineTransform2D) {
            return new WarpAffine((AffineTransform2D) mt);
        }
        
        // second simple case, the caller does not want any optimization
        if(maxDistanceSquared == 0) {
            return new WarpAdapter(null, mt);
        }
        
        // get the bounds and perform sanity check
        final double minx = domain.getMinX();
        final double maxx = domain.getMaxX();
        final double miny = domain.getMinY();
        final double maxy = domain.getMaxY();
        final int width = (int) (maxx - minx);
        final int heigth = (int) (maxy - miny);
        if(abs(width) == 0 || heigth == 0) {
            throw new IllegalArgumentException("The domain is empty!");
        }
        
        /* 
         * Prepare to build a warp grid. A warp grid requires a set of uniform cells, but the
         * number of rows and cols may differ. The following method will drill down using a 
         * recursive division algorithm to find the optimal number of divisions along the
         * x and y axis
         */
        int[] rowCols;
        try {
            rowCols = computeOptimalDepths(mt, minx, maxx, miny, maxy, 0, 0);
        } catch(ExcessiveDepthException e) {
            return new WarpAdapter(null, mt);
        }
        
        if(rowCols[0] == 0 && rowCols[1] == 0) {
            // we can use an affine transformation
            
            // the transformation is flat enough that we can use an affine transform
            // build the affine transform from the deltas over the whole window, since
            // this is how we evaluated the tolerance respect in computeOptimalDepths,
            // from the lower left corner to the others
            ordinates[0] = minx;
            ordinates[1] = miny;
            ordinates[2] = minx;
            ordinates[3] = maxy;
            ordinates[4] = maxx;
            ordinates[5] = miny;
            mt.transform(ordinates, 0, ordinates, 0, 3);
            
            
            // build the affine transform coefficients
            final double m00 = (ordinates[4] - ordinates[0]) / width;
            final double m10 = (ordinates[5] - ordinates[1]) / width;
            final double m01 = (ordinates[2] - ordinates[0]) / heigth;
            final double m11 = (ordinates[3] - ordinates[1]) / heigth;
            final double m02 = ordinates[0]; 
            final double m12 = ordinates[1];
            
            AffineTransform at = new AffineTransform(m00, m10, m01, m11, m02, m12);
            at.translate(-minx, -miny);
            XAffineTransform.round(at, 1e-6);
            LOGGER.log(Level.FINE, "Optimizing the warp into an affine transformation: {0}", at);
            return new WarpAffine(at);
        } else {
            // unfortunately the steps are integers, meaning we won't fit the grid exactly with them
            int stepx = (int) (width / pow(2, rowCols[1])); 
            int stepy = (int) (heigth / pow(2, rowCols[0]));

            // compute rows and cols
            int cols = (int) (width / stepx) ;
            int rows = (int) (heigth / stepy);
            int cmax = (int) (minx + cols * stepx);
            int rmax = (int) (miny + rows * stepy);
            
            // due to integer rounding we might not be covering the entire raster with the grid,
            // if so compensate by either adding a two/column or by adding a pixel to the step 
            // whatever makes the resulting grid be the smallest)
            if(cmax < maxx) {
                // use the better match between adding a column and adding a pixel to the step
                if((cmax + stepx) < cols * (stepx + 1)) {
                    cmax += stepx;
                    cols++;
                } else {
                    stepx++;
                    cmax = (int) (minx + cols * stepx); 
                }
            }
            if(rmax < maxy) {
                // use the better match between adding a row and adding a pixel to the step
                if((rmax + stepy) < rows * (stepy + 1)) {
                    rmax += stepy;
                    rows++;
                } else {
                    stepy++;
                    rmax = (int) (miny + rows * stepy);
                }
            }

            // fill in the original grid
            final float[] warpPositions = new float[(rows + 1) * (cols + 1) * 2];
            int r = (int) miny;
            int idx = 0;
            while(r <= rmax) {
                int c = (int) minx;
                while(c <= cmax) {
                    warpPositions[idx++] = c;
                    warpPositions[idx++] = r;
                    c += stepx;
                }
                r += stepy;
            }
            if(DUMP_GRIDS) {
                dumpPropertyFile(warpPositions, "original");
            }
            
            // transform it to target
            mt.transform(warpPositions, 0, warpPositions, 0, warpPositions.length / 2);
            if(DUMP_GRIDS) {
                dumpPropertyFile(warpPositions, "transformed");
            }
            
            LOGGER.log(Level.FINE, "Optimizing the warp into an grid warp {0} x {1}", new Object[] {rows, cols});
            
            return new WarpGrid((int) minx, stepx, cols, (int) miny, stepy, rows, warpPositions);
        }
    }

    /**
     * Performs recursive slicing of the area to find the optimal number of subdivisions
     * along the x and y axis.
     * 
     * @param mt
     * @param minx
     * @param maxx
     * @param miny
     * @param maxy
     */
    int[] computeOptimalDepths(MathTransform2D mt, double minx, double maxx, double miny,
            double maxy, int rowDepth, int colDepth) throws TransformException  {
        if(maxx - minx < 4 || maxy - miny < 4) {
            throw new ExcessiveDepthException("Warp grid getting as dense as the original data");
        } else if (rowDepth + colDepth > 20) {
            // this would take 2^(20) points, way too much already
            throw new ExcessiveDepthException("Warp grid getting too large to fit in memory, bailing out");
        }
        
        // center of this rectangle
        final double midx = (minx + maxx) / 2;
        final double midy = (miny + maxy) / 2;
        
        // test tolerance along the y axis
        boolean withinTolVertical = isWithinTolerance(mt, minx, miny, minx, midy, minx, maxy) &&
            isWithinTolerance(mt, maxx, miny, maxx, midy, maxx, maxy);
        // test tolerance along the x axis
        boolean withinTolHorizontal = isWithinTolerance(mt, minx, miny, midx, miny, maxx, miny) &&
            isWithinTolerance(mt, minx, maxy, midx, maxy, maxx, maxy);
        // if needed, check tolerance along the diagonal as well
        if(withinTolVertical && withinTolHorizontal) {
            if(!isWithinTolerance(mt, minx, miny, midx, midy, maxx, maxy) || 
                    !isWithinTolerance(mt, minx, maxy, midx, midy, maxx, miny)) {
                withinTolVertical = false;
                withinTolHorizontal = false;
            } 
        }
        
        // check what kind of split are we going to make
        // (and try not to get fooled by symmetrical projections)
        if((!withinTolHorizontal && !withinTolVertical)) {
            // quad split
            rowDepth++;
            colDepth++;
            int[] d1 = computeOptimalDepths(mt, minx, midx, miny, midy, rowDepth, colDepth);
            int[] d2 = computeOptimalDepths(mt, minx, midx, midy, maxy, rowDepth, colDepth);
            int[] d3 = computeOptimalDepths(mt, midx, maxx, miny, midy, rowDepth, colDepth);
            int[] d4 = computeOptimalDepths(mt, midx, maxx, midy, maxy, rowDepth, colDepth);
            return new int[] {max(max(d1[0], d2[0]), max(d3[0], d4[0])), 
                    max(max(d1[1], d2[1]), max(d3[1], d4[1]))};
        } else if(!withinTolHorizontal) {
            // slice in two at midx (creating two more colums)
            colDepth++;
            int[] d1 = computeOptimalDepths(mt, minx, midx, miny, maxy, rowDepth, colDepth);
            int[] d2 = computeOptimalDepths(mt, midx, maxx, miny, maxy, rowDepth, colDepth);
            return new int[] {max(d1[0], d2[0]), max(d1[1], d2[1])};
        } else if(!withinTolVertical){
            // slice in two at midy (creating two rows)
            rowDepth++;
            int[] d1 = computeOptimalDepths(mt, minx, maxx, miny, midy, rowDepth, colDepth);
            int[] d2 = computeOptimalDepths(mt, minx, maxx, midy, maxy, rowDepth, colDepth);
            return new int[] {max(d1[0], d2[0]), max(d1[1], d2[1])};
        }
        
        return new int[] {rowDepth, colDepth};
    }

    /**
     * Checks if the point predicted by a WarpGrid between the specified points
     * @param mt
     * @param minx
     * @param miny
     * @param minx2
     * @param midy
     * @param minx3
     * @param maxy
     * @return
     */
    boolean isWithinTolerance(MathTransform2D mt, double x1, double y1, double x2,
            double y2, double x3, double y3) throws TransformException {
        // transform the points (use two extra points at quarter distance to avoid being
        // fooled by symmetrical projections
        ordinates[0] = x1;
        ordinates[1] = y1;
        ordinates[2] = (x1 + x2) / 2;
        ordinates[3] = (y1 + y2) / 2;
        ordinates[4] = x2;
        ordinates[5] = y2;
        ordinates[6] = (x2 + x3) / 2;
        ordinates[7] = (y2 + y3) / 2;
        ordinates[8] = x3;
        ordinates[9] = y3;
        mt.transform(ordinates, 0, ordinates, 0, 5);
        
        boolean withinTolerance = true;
        for(int i = 1; i < 4 && withinTolerance; i++) {
            // apply to local variables for readability
            final double tx1 = ordinates[0];
            final double ty1 = ordinates[1];
            final double tx2 = ordinates[i * 2];
            final double ty2 = ordinates[i * 2 + 1];
            final double tx3 = ordinates[8];
            final double ty3 = ordinates[9];
            
            // check the differences
            double dx = 0;
            if(abs(x3 - x1) > EPS) {
                double xmid;
                if(i == 1) {
                    xmid = (x1 + x2) / 2;
                } else if(i == 2){
                    xmid = x2;
                } else {
                    xmid = (x2 + x3) / 2;
                }
                 
                dx = tx2 - (tx3 - tx1) / (x3 - x1) * (xmid - x1) - tx1;
            }
            double dy = 0;
            if(abs(y3 - y1) > EPS) {
                double ymid;
                if(i == 1) {
                    ymid = (y1 + y2) / 2;
                } else if(i == 2){
                    ymid = y2;
                } else {
                    ymid = (y2 + y3) / 2;
                }
                dy = ty2 - (ty3 - ty1) / (y3 - y1) * (ymid - y1) - ty1;
            }
            
            // see if the total distance between predicted and actual is lower than the tolerance
            final double distance = dx * dx + dy * dy;
            withinTolerance &= distance < maxDistanceSquared;
        }
        
        return withinTolerance;
    }
    
    /**
     * Convenience exception to bail out when the grid evaluation code gets too deep
     * @author Andrea Aime - GeoSolutions
     */
    class ExcessiveDepthException extends RuntimeException {
        private static final long serialVersionUID = -3533898904532522502L;

        public ExcessiveDepthException() {
            super();
        }

        public ExcessiveDepthException(String message, Throwable cause) {
            super(message, cause);
        }

        public ExcessiveDepthException(String message) {
            super(message);
        }

        public ExcessiveDepthException(Throwable cause) {
            super(cause);
        }
        
    }
    
    /**
     * A debugging aid that dumps the grids to a file
     * @param points
     * @param name
     */
    void dumpPropertyFile(float[] points, String name) {
        long start = System.currentTimeMillis();
        
        BufferedWriter writer = null;
        try {
            File output  = File.createTempFile(start + name, ".properties");
            writer = new BufferedWriter(new FileWriter(output));
            writer.write("_=geom:Point:srid=32632"); writer.newLine();
            for (int i = 0; i < points.length; i+=2) {
                writer.write("p." + (i / 2) + "=POINT(" + points[i] + " " + points[i + 1] + ")");
                writer.newLine();
            }
            LOGGER.info(name + " dumped as " + output.getName());
        } catch(IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to dump points: " + e.getMessage(), e);
        } finally {
            if(writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }
}

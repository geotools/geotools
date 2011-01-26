/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation.builder.algorithm;

import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;
import java.util.Map;

import javax.media.jai.RasterFactory;

import org.geotools.geometry.DirectPosition2D;
import org.opengis.coverage.Coverage;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.operation.TransformException;


/**
 * Super class for implementing interpolation algorithms. Subclass has to implement just the 
 * {@link #getValue(DirectPosition)} method.
 * 
 * @source $URL$
 * @version $Id$
 * @author jezekjan
 *
 */
public abstract class AbstractInterpolation {
	
	/** Known values at positions*/
    private final Map<DirectPosition, Float> positions;
    
    /** Grid spacing in x */    
    private double dx;
    
    /** grid spacing in y */
    private double dy;
    
    /** Envelope of interpolated area*/
    private final Envelope env;
    
    /** Number of rows*/
    private int xNumCells;
    
    /** Number of cells*/
    private int yNumCells;
    
    /**Calculated values in 1D array*/
    private float[] gridValues;
    
    /**Calculated values in 2D array*/
    private float[][] grid2D;
    
    /**Calculated values in raster*/
    private WritableRaster raster;

    /**
     *
     * @param positions keys - point (DirectPosition), values - point values
     */
    public AbstractInterpolation(Map <DirectPosition, Float> positions) {
        this.positions = positions;
        this.dx = 0;
        this.dy = 0;
        this.env = null;

        this.xNumCells = 0;
        this.yNumCells = 0;
    }

    public AbstractInterpolation(Map <DirectPosition, Float> positions, int xNumOfCells,
        int yNumOfCells, Envelope env) {
        this.positions = positions;
        this.xNumCells = xNumOfCells;
        this.yNumCells = yNumOfCells;
        this.env = env;

        dx = env.getLength(0) / xNumOfCells;
        dy = env.getLength(1) / yNumOfCells;

        //gridValues = new float[xNumCells*yNumCells];
    }

    /**
     * Sets the spacing between grid cells and rows.
     * @param dx Spacing between rows
     * @param dy Spacing between cells
     */
    public void setSpacing(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;

        this.xNumCells = (int) Math.floor(env.getLength(0) / dx);
        this.yNumCells = (int) Math.floor(env.getLength(1) / dy);
    }

    /**
     * Sets the number of rows and cells. The spacing is calculated by dividing the envelope
     * @param xNumOfCells Number of grid cells
     * @param yNumOfCells Number of grid rows
     */
    public void setDensity(int xNumOfCells, int yNumOfCells) {
        this.xNumCells = xNumOfCells;
        this.yNumCells = yNumOfCells;

        dx = env.getLength(0) / xNumOfCells;
        dy = env.getLength(1) / yNumOfCells;
    }

    /**
     * Returns array of float of interpolated grid values.
     * The values are in row order. The dimension
     * id number of columns * number of rows.
     * @return Values of grid coordinates
     */
    private float[] buildGrid() {
        gridValues = new float[(xNumCells + 1) * (yNumCells + 1)];

        for (int i = 0; i <= yNumCells; i++) {
            for (int j = 0; j <= xNumCells; j++) {
            	
            	DirectPosition dp = new DirectPosition2D(
            			            env.getLowerCorner().getOrdinate(0) + (j * dx),
            			            env.getUpperCorner().getOrdinate(1) - (i * dy));
            	int index = (i * (1 + xNumCells)) + j;
            	float value =  getValue(dp);
                gridValues[index] = value;
            }
        }

        return gridValues;
    }
    
    private void buildCoverage() {
        gridValues = new float[(xNumCells + 1) * (yNumCells + 1)];

        for (int i = 0; i <= yNumCells; i++) {
            for (int j = 0; j <= xNumCells; j++) {
                gridValues[(i * (1 + xNumCells)) + j] = getValue(new DirectPosition2D(env.getLowerCorner()
                                                                                         .getOrdinate(0)
                            + (j * dx), env.getLowerCorner().getOrdinate(1) + (i * dy)));
            }
        }       
    }


    /**
     *
     * @return
     */
    public float[] getGrid() {
        if (gridValues == null) {
            gridValues = buildGrid();
        }

        return gridValues;
    }

    /**
     *
     * @return grid in the form of WritableRaster
     */
    public WritableRaster getRaster() {
        if (raster == null) {
            final float[] gridPositions = getGrid();

            raster = RasterFactory.createBandedRaster(DataBuffer.TYPE_FLOAT, yNumCells + 1,
                    xNumCells + 1, 1, null);

            raster.setSamples(0, 0, yNumCells + 1, xNumCells + 1, 0, gridPositions);
        }

        return raster;
    }

    /**
     *
     * @return
     * @throws TransformException
     */
    public float[][] get2DGrid() {
        if ((grid2D == null) || (grid2D.length == 0)) {
            final float[] warpPositions = getGrid();

            grid2D = new float[yNumCells + 1][xNumCells + 1];

            for (int i = 0; i <= yNumCells; i++) {
                for (int j = 0; j <= xNumCells; j++) {
                	int index = (int) ((i * (xNumCells + 1)) + (j));
                	float value = getGrid()[index];
                    grid2D[i][j] = value;
                }
            }
        }

        return grid2D;
    }

    /**
     * Return interpolated value in position p
     * @param p position where we want to compute the value
     * @return the value at position p
     */
    public float intepolateValue(DirectPosition p) {
        return getValue(p);
    }

    /**
     * Real computation is performed here. Real algorithm has to be implemented her.
     * @param p position where we want to compute the value
     * @return the value at position p
     */
    abstract public float getValue(DirectPosition p);

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    public Envelope getEnv() {
        return env;
    }

    public int getXNumCells() {
        return xNumCells;
    }

    public int getYNumCells() {
        return yNumCells;
    }

    public Map<DirectPosition, Float> getPositions() {
        return positions;
    }
}

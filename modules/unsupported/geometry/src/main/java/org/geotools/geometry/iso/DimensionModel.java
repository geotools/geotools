/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.iso;

import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * The class Dimension represents the dimension type of a Feature Geometry. It
 * supports three dimension types:
 * 
 * 2D - Geometry objects in two dimensional euclidian space with the coordinates
 * x and y. Geometric objects in that dimension model do not store any
 * information about height in their geometry attributes.
 * 
 * 2.5D - Geometry objects in three dimensional euclidian space with the
 * coordinates x and y, and owns a height attribute z. Therefore, each
 * coordinate pair only holds one height information. This relation can be
 * defined by the bijectional function f(x,y) -> z.
 * 
 * 3D - Geometry objects in three dimensional euclidian space with the
 * coordinates x, y and z. Since we are in real three dimensional space,
 * coordinates with the same x and y values, but different z values are allowed.
 * This provides the representation of overlaying objects like bridges over a
 * river or tunels through a mountain.
 * 
 * The distinction between 2d (which is in 2 dimensional euclidian space) and
 * 2.5d (which is in 2 dimensional euclidian space) objects is essential,
 * because the simple disctinction between the coordiante dimension (euclidian
 * space) would make the seperation of 2d and 3d data impossible. In fact, there
 * is a huge discrepancy in algorithms and their runtime complexity in spatial
 * operation which treat 2d data and 3d data. 3D algorithms are usually
 * absolutely ineffecient for 2D data, so that a seperation between the two
 * dimensionalities must be done. However, 2d data algorithms can usually be
 * designed in a way to treat 2.5d data correclty as well without effecting the
 * runtime complexity.
 * 
 * @author Sanjay Dominik Jena
 *
 * @source $URL$
 */
public class DimensionModel {

	public static final int TWO_DIMENSIONIAL = 1;
	public static final int TWOoFIVE_DIMENSIONIAL = 2;
	public static final int THREE_DIMENSIONIAL = 3;

    /**
     * One of the DimensionModel constants.
     * <ul>
     * <li>1 2   Dimensional
     * <li>2 2.5 Dimensional
     * <li>2 3   Dimensional (Unsupported)
     * </ul>
     * @return value based on crs
     */
    public static int toD( CoordinateReferenceSystem crs ){
        if( crs == null ){
            return 2; // default to 2
        }
        int dimension = crs.getCoordinateSystem().getDimension();
        if( dimension == 2 ){
            return TWO_DIMENSIONIAL;
        }
        else if( dimension == 3 ){
            return THREE_DIMENSIONIAL;
        }
        else {
            return -1; // not supported
        }
    }
    
	private int dimensionModelType = 0;

	/**
	 * Creates a Dimension Model according to the desired dimensional type: 2D,
	 * 2.5D or 3D
	 * 
	 * @param dimensionType
	 */
	public DimensionModel(int dimensionType) {
		this.dimensionModelType = dimensionType;
	}

	/**
	 * Returns the Dimension type
	 * 
	 * @return dimension type
	 */
	public int getDimensionType() {
		return this.dimensionModelType;
	}

	/**
	 * Returns the coordiante dimension in euclidian space of the Dimension
	 * model
	 * 
	 * @return coordinate dimension in euclidian space
	 */
	public int getCoordinateDimension() {
		if (this.dimensionModelType == DimensionModel.TWO_DIMENSIONIAL) {
			return 2;
		} else if (this.dimensionModelType == DimensionModel.TWOoFIVE_DIMENSIONIAL) {
			return 3;
		} else if (this.dimensionModelType == DimensionModel.THREE_DIMENSIONIAL) {
			return 3;
		}

		return 0; // THIS CASE SHOULD NEVER BE REACHED
	}

	/**
	 * Tests whether the Dimension Model is of two dimensional type
	 * 
	 * @return TRUE if the Dimension Model is of two dimensional type
	 * @return FALSE if the Dimension Model is not of two dimensional type
	 */
	public boolean is2D() {
		return (this.dimensionModelType == DimensionModel.TWO_DIMENSIONIAL);
	}

	/**
	 * Tests whether the Dimension Model is of 2.5 dimensional type
	 * 
	 * @return TRUE if the Dimension Model is of 2.5 dimensional type
	 * @return FALSE if the Dimension Model is not of 2.5 dimensional type
	 */
	public boolean is2o5D() {
		return (this.dimensionModelType == DimensionModel.TWOoFIVE_DIMENSIONIAL);
	}

	/**
	 * Tests whether the Dimension Model is of three dimensional type
	 * 
	 * @return TRUE if the Dimension Model is of three dimensional type
	 * @return FALSE if the Dimension Model is not of three dimensional type
	 */
	public boolean is3D() {
		return (this.dimensionModelType == DimensionModel.THREE_DIMENSIONIAL);
	}
	
	public String toString() {
		String rString = "";
		if (this.dimensionModelType == DimensionModel.TWO_DIMENSIONIAL) {
			rString = "2D";
		} else if (this.dimensionModelType == DimensionModel.TWOoFIVE_DIMENSIONIAL) {
			rString = "2.5D";
		} else if (this.dimensionModelType == DimensionModel.THREE_DIMENSIONIAL) {
			rString = "3D";
		}
		return rString;
	}

}

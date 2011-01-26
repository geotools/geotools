/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 * Created on 31-dic-2004
 */
package org.geotools.geometry.jts.coordinatesequence;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.impl.PackedCoordinateSequence;
import com.vividsolutions.jts.geom.impl.PackedCoordinateSequenceFactory;

/**
 * An implementation of a JTS CSBuilder which uses a PackedCoordinateSequence.
 * 
 * @author wolf
 * @source $URL$
 */
public abstract class PackedCSBuilder implements CSBuilder {
	int size = -1;

	int dimensions = -1;

	/**
	 * @see org.geotools.geometry.coordinatesequence.CSBuilder#getSize()
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @see org.geotools.geometry.coordinatesequence.CSBuilder#getDimension()
	 */
	public int getDimension() {
		return dimensions;
	}

	public static class Double extends PackedCSBuilder {
		double[] ordinates;

		PackedCoordinateSequenceFactory factory = new PackedCoordinateSequenceFactory(PackedCoordinateSequenceFactory.DOUBLE);

		/**
		 * @see org.geotools.geometry.coordinatesequence.CSBuilder#start(int,
		 *      int)
		 */
		public void start(int size, int dimensions) {
			ordinates = new double[size * dimensions];
			this.size = size;
			this.dimensions = dimensions;
		}

		/**
		 * @see org.geotools.geometry.coordinatesequence.CSBuilder#end()
		 */
		public CoordinateSequence end() {
			CoordinateSequence cs = factory.create(ordinates, dimensions);
			ordinates = null;
			size = -1;
			dimensions = -1;
			return cs;
		}

		/**
		 * @see org.geotools.geometry.coordinatesequence.CSBuilder#setOrdinate(double,
		 *      int, int)
		 */
		public void setOrdinate(double value, int ordinateIndex,
				int coordinateIndex) {
			ordinates[coordinateIndex * dimensions + ordinateIndex] = value;
		}

		/**
		 * @see org.geotools.geometry.coordinatesequence.CSBuilder#getOrdinate(int,
		 *      int)
		 */
		public double getOrdinate(int ordinateIndex, int coordinateIndex) {
			return ordinates[coordinateIndex * dimensions + ordinateIndex];
		}

		/**
		 * @see org.geotools.geometry.coordinatesequence.CSBuilder#setOrdinate(com.vividsolutions.jts.geom.CoordinateSequence, double, int, int)
		 */
		public void setOrdinate(CoordinateSequence sequence, double value, int ordinateIndex, int coordinateIndex) {
			PackedCoordinateSequence pcs = (PackedCoordinateSequence) sequence;
			pcs.setOrdinate(coordinateIndex, ordinateIndex, value);
		}

	}
	
	public static class Float extends PackedCSBuilder {
		float[] ordinates;

		PackedCoordinateSequenceFactory factory = new PackedCoordinateSequenceFactory(PackedCoordinateSequenceFactory.FLOAT);

		/**
		 * @see org.geotools.geometry.coordinatesequence.CSBuilder#start(int,
		 *      int)
		 */
		public void start(int size, int dimensions) {
			ordinates = new float[size * dimensions];
			this.size = size;
			this.dimensions = dimensions;
		}

		/**
		 * @see org.geotools.geometry.coordinatesequence.CSBuilder#end()
		 */
		public CoordinateSequence end() {
			CoordinateSequence cs = factory.create(ordinates, dimensions);
			ordinates = null;
			size = -1;
			dimensions = -1;
			return cs;
		}

		/**
		 * @see org.geotools.geometry.coordinatesequence.CSBuilder#setOrdinate(double,
		 *      int, int)
		 */
		public void setOrdinate(double value, int ordinateIndex,
				int coordinateIndex) {
			ordinates[coordinateIndex * dimensions + ordinateIndex] = (float) value;
		}
		
		/**
		 * @see org.geotools.geometry.coordinatesequence.CSBuilder#setOrdinate(com.vividsolutions.jts.geom.CoordinateSequence, double, int, int)
		 */
		public void setOrdinate(CoordinateSequence sequence, double value, int ordinateIndex, int coordinateIndex) {
			PackedCoordinateSequence pcs = (PackedCoordinateSequence) sequence;
			pcs.setOrdinate(coordinateIndex, ordinateIndex, value);
		}

		/**
		 * @see org.geotools.geometry.coordinatesequence.CSBuilder#getOrdinate(int,
		 *      int)
		 */
		public double getOrdinate(int ordinateIndex, int coordinateIndex) {
			return ordinates[coordinateIndex * dimensions + ordinateIndex];
		}

	}

}

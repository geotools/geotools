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
package org.geotools.caching.grid.spatialindex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.geotools.caching.spatialindex.AbstractSpatialIndex;
import org.geotools.caching.spatialindex.NodeIdentifier;
import org.geotools.caching.spatialindex.Region;
import org.geotools.caching.spatialindex.RegionNodeIdentifier;
import org.geotools.caching.spatialindex.Shape;

/**
 * The root node of a grid, which has n GridNodes as children. As GridNodes do,
 * it can store data too.
 * 
 * @author Christophe Rousson, SoC 2007, CRG-ULAVAL
 * 
 *
 * @source $URL$
 */
public class GridRootNode extends GridNode {
	private static final long serialVersionUID = 4675163856302389522L;
	protected double tiles_size;

	protected int[] tiles_number;
	protected ArrayList<NodeIdentifier> children; // list of NodeIdentifiers
	protected int childrenCapacity = -1;

	/**
	 * No-arg constructor for serialization purpose. Deserialized nodes must
	 * call setIdentifier before using
	 * 
	 */
	GridRootNode() {
		super();
	}

	/**
	 * the mbr is read from the id;
	 */
	protected GridRootNode(int gridsize, RegionNodeIdentifier id) {
		super(id);
		init(gridsize);
	}

	private void init(int gridsize) {
		int dims = getShape().getDimension();
		Region mbr = (Region) getShape();
		tiles_number = new int[dims];

		double area = 1;

		for (int i = 0; i < dims; i++) {
			area *= (mbr.getHigh(i) - mbr.getLow(i));
		}

		tiles_size = Math.pow(area / gridsize, 1d / dims);
		int newcapacity = 1;

		for (int i = 0; i < dims; i++) {
			int tmp;
			double dtmp = (mbr.getHigh(i) - mbr.getLow(i)) / tiles_size;
			tmp = (int) dtmp;

			if (tmp < dtmp) {
				tmp += 1;
			}

			tiles_number[i] = tmp;
			newcapacity *= tmp;
		}
		assert (newcapacity >= gridsize);
		// capacity = newcapacity;
		childrenCapacity = newcapacity;
		children = new ArrayList<NodeIdentifier>(childrenCapacity);
	}

	/**
	 * Creates the grid by appending children to this node.
	 * 
	 */
	protected void split(GridSpatialIndex index) {
		int dims = tiles_number.length;
		double[] pos = new double[dims];
		double[] nextpos = new double[dims];
		Region mbr = (Region) getShape();
		// int id = 0;

		for (int i = 0; i < dims; i++) {
			pos[i] = mbr.getLow(i);
			nextpos[i] = pos[i] + tiles_size;
		}

		do {
			Region reg = new Region(pos, nextpos);
			RegionNodeIdentifier id = (RegionNodeIdentifier) index
					.findUniqueInstance(new RegionNodeIdentifier(reg));
			GridNode child = createNode(id);
			index.writeNode(child);
			this.children.add(child.getIdentifier());
			// id++;
		} while (increment(pos, nextpos));
	}

	public List<Integer> getChildren(Shape shape) {
		Region mbr = (Region) getShape();
		Region childMBR = shape.getMBR();
		int dims = mbr.getDimension();
		int[] gridminindex = new int[dims];
		for (int i = 0; i < gridminindex.length; i++) {
			if (childMBR.getLow(i) <= mbr.getLow(i)) {
				gridminindex[i] = 0;
			} else {
				gridminindex[i] = (int) Math.floor((childMBR.getLow(i) - mbr.getLow(i))/ tiles_size);
			}
		}
		int[] gridmaxindex = new int[dims];
		for (int i = 0; i < gridminindex.length; i++) {
			if (childMBR.getHigh(i) >= mbr.getHigh(i)) {
				gridmaxindex[i] = tiles_number[i] - 1;
			} else {
				gridmaxindex[i] = (int) Math.floor((childMBR.getHigh(i) - mbr.getLow(i))/ tiles_size);
			}
		}

		// want to iterate from gridminindex to gridmaxindex
		int current[] = gridminindex.clone();
		return iterate(current, gridminindex, gridmaxindex, 0);

	}

	private List<Integer> iterate(int current[], int min[], int max[], int index) {
		ArrayList<Integer> values = new ArrayList<Integer>();
		if (index >= min.length) {
			return Collections.singletonList(new Integer(this.gridIndexToNodeId(current)));
		}
		for (int i = min[index]; i <= max[index]; i++) {
			current[index] = i;
			values.addAll(iterate(current, min, max, index + 1));
		}
		return values;

	}

	protected GridNode createNode(RegionNodeIdentifier id) {
		// return new GridNode(grid, reg);
		return new GridNode(id);
	}

	/**
	 * Computes sequentially the corner position of each tile in the grid.
	 * 
	 * @param pos
	 * @param nextpos
	 * @return false if the upperright corner of the grid has been reached, true
	 *         otherwise
	 */
	boolean increment(double[] pos, double[] nextpos) {
		Region mbr = (Region) getShape();
		int dims = pos.length;

		if ((dims != tiles_number.length)
				|| (nextpos.length != tiles_number.length)) {
			throw new IllegalArgumentException(
					"Cursor has not the same dimension as grid.");
		}

		for (int i = 0; i < dims; i++) {
			if (((nextpos[i] - mbr.getHigh(i)) > 0)
					|| (Math.abs(nextpos[i] - mbr.getHigh(i)) < AbstractSpatialIndex.EPSILON)) {
				pos[i] = mbr.getLow(i);
				nextpos[i] = pos[i] + tiles_size;

				if (i == (dims - 1)) {
					return false;
				}
			} else {
				pos[i] = nextpos[i];
				nextpos[i] = pos[i] + tiles_size;

				break;
			}
		}

		return true;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		// sb.append("GridRootNode: capacity:" + capacity + ", MBR:" + mbr);
		sb.append("GridRootNode: MBR: " + this.getShape() + ", capacity: "
				+ getCapacity());

		return sb.toString();
	}

	public NodeIdentifier getChildIdentifier(int index)
			throws IndexOutOfBoundsException {
		return (NodeIdentifier) children.get(index);
	}

	public void setChildIdentifier(int index, NodeIdentifier id) {
		children.set(index, id);
	}

	public int getChildrenCount() {
		return children.size();
	}

	public int getLevel() {
		return 1;
	}

	public boolean isIndex() {
		if (children.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	public boolean isLeaf() {
		return !isIndex();
	}

	/**
	 * Converts an array of indexes into the id of a node.
	 * 
	 * @param index
	 * @return
	 */
	public int gridIndexToNodeId(int[] index) {
		if (index.length != tiles_number.length) {
			throw new IllegalArgumentException("Argument has " + index.length
					+ " dimensions whereas grid has " + tiles_number.length);
		} else {
			int result = 0;
			int offset = 1;

			for (int i = 0; i < index.length; i++) {
				result += (offset * index[i]);
				offset *= tiles_number[i];
			}

			return result;
		}
	}

	/**
	 * Only clears data the root node; does not clear the children.
	 */
	public void clear() {
		super.clear();
	}

	public String toReadableText() {
		StringBuffer sb = new StringBuffer();
		sb.append("RootNode *****");
		sb.append(super.toReadableText());

		return sb.toString();
	}

	/**
	 * The maximum of allowable nodes in the grid.
	 * 
	 * @return
	 */
	public int getCapacity() {
		return this.childrenCapacity;
	}

	/**
	 * The number of tiles/nodes in a given dimension.
	 * 
	 * @param dim
	 * @return
	 */
	public int getMaximumTileCount(int dim) {
		if (dim < 0 || dim > tiles_number.length) {
			return -1;
		}
		return tiles_number[dim];
	}

	/**
	 * @returns the size of the children tiles
	 */
	public double getTileSize() {
		return this.tiles_size;
	}
	
//	public void printGrid(){
//		System.out.println(makePolygon((Region)this.getShape()));
//		
//		for(int i = 0; i < getChildrenCount(); i ++){
//			NodeIdentifier id = this.getChildIdentifier(i);
//			System.out.println(makePolygon((Region)id.getShape()));
//		}
//	}
//	
//	public String makePolygon(Region r){
//		double minx = r.getLow(0);
//		double maxx = r.getHigh(0);
//		double miny = r.getLow(1);
//		double maxy = r.getHigh(1);
//		return "POLYGON(( " + minx + " " + miny + ", " + minx  + " " + maxy  + "," + maxx + " " + maxy + "," + maxx + " " + miny + "," + minx + " " + miny + "))"; 
//	}
}

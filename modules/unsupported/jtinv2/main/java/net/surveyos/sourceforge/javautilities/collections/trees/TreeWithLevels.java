/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package net.surveyos.sourceforge.javautilities.collections.trees;

import java.util.Iterator;
import java.util.List;

/**
 * A tree collection that stores the level, or distance of each node of the tree, 
 * from the root node.
 * 
 * Each node also stores a value defined by the creator of the tree.
 *
 */
public interface TreeWithLevels<E> 
{
	/**
	 * Returns the root node of the tree.
	 */
	public abstract TreeNode<E> getRootNode();
	
	/**
	 * Returns the node identified by the int.
	 */
	public abstract TreeNode<E> getNodeWithIdentifier(int argIdentifier);
	
	/**
	 * Returns a list of all the tree nodes on a level.
	 */
	public abstract Iterator<TreeNode<E>> getAllTreeNodesOnLevel(int argLevel);
	
	/**
	 * Returns the total number of nodes in the tree.
	 */
	public abstract int getNumberOfNodes();
	
	/**
	 * Returns the total number of levels.
	 */
	public abstract int getNumberOfLevels();
	
	/**
	 * Walks to (visits) every node in this tree, until the walker
	 * indicates it is done walking.
	 */
	public abstract void walkTree(TreeWalker argWalker);
	
	public abstract Iterator<TreeNode<E>> getSiblingNodes(TreeNode<E> argSubject);
}

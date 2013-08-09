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

public interface TreeWalker<E>
{
	public abstract void catchNode(TreeNode<E> argNode);
	
	public abstract void onNewLevel(int argLevel);
	
	public abstract void atLeafNode(TreeNode<E> argNode);
	
	public abstract void atBranchNode(TreeNode<E> argNode);	
	
	public abstract boolean keepWalking();
}

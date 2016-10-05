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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BasicTreeWithLevels<E> implements TreeWithLevels<E> 
{
	// Private member variables.
	private TreeNode<E> rootNode;
	private HashMap<Integer, LinkedList<TreeNode<E>>> levels;
	private int numberOfNodes;
	private int numberOfLevels;
	
	private HashMap<Integer, TreeNode<E>> idToNodeMap;
	
	// Constructors.
	public BasicTreeWithLevels(TreeNode<E> argRootNode, HashMap<Integer, LinkedList<TreeNode<E>>> argLevels, 
			int argNumberOfNodes, int argNumberOfLevels, HashMap<Integer, TreeNode<E>> argIdToNodeMap)
	{
		this.rootNode = argRootNode;
		this.levels = argLevels;
		this.numberOfNodes = argNumberOfNodes;
		this.numberOfLevels = argNumberOfLevels;
		this.idToNodeMap = argIdToNodeMap;
	}
	
	// Public methods.
	@Override
	public TreeNode<E> getRootNode()
	{
		return this.rootNode;
	}

	@Override
	public TreeNode<E> getNodeWithIdentifier(int argIdentifier) 
	{
		Integer idAsInteger = new Integer(argIdentifier);
		return this.idToNodeMap.get(idAsInteger);
	}

	@Override
	public Iterator<TreeNode<E>> getAllTreeNodesOnLevel(int argLevel) 
	{
		Integer levelAsInteger = new Integer(argLevel);
		return this.levels.get(levelAsInteger).iterator();
	}

	@Override
	public int getNumberOfNodes() 
	{
		return this.numberOfNodes;
	}

	@Override
	public int getNumberOfLevels() 
	{
		return this.numberOfLevels;
	}
	
	public void walkTree(TreeWalker argWalker)
	{
		// Walk all the nodes in each level.
		int counter = 1;
		
		while(counter <= this.numberOfLevels)
		{
			List<TreeNode<E>> nodesOnLevel = this.levels.get(counter);
			Iterator<TreeNode<E>> goOverEach = nodesOnLevel.iterator();
			
			while(goOverEach.hasNext() == true)
			{
				TreeNode<E> currentNode = goOverEach.next();
				argWalker.catchNode(currentNode);
				
				if(currentNode.isLeafNode() == true)
				{
					argWalker.atLeafNode(currentNode);
				}
				
				else
				{
					argWalker.atBranchNode(currentNode);
				}
			}
			
			counter++;
		}
	}

	@Override
	public Iterator<TreeNode<E>> getSiblingNodes(TreeNode<E> argSubject) 
	{
		TreeNode<E> parent = argSubject.getParent();
		List<TreeNode<E>> children = parent.getChildren();
		
		children.remove(argSubject);
		
		return children.iterator();
	}
}

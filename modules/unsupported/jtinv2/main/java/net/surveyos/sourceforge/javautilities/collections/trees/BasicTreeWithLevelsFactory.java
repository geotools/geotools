package net.surveyos.sourceforge.javautilities.collections.trees;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class BasicTreeWithLevelsFactory<E> 
{
	// Private member variables.
	private TreeNode<E> rootNode;
	private int numberOfNodes;
	private int numberOfLevels;
	private int currentIdentifier;
	
	private HashMap<Integer, TreeNode<E>> idToNodeMap;
	private HashMap<Integer, LinkedList<TreeNode<E>>> levels;
	
	// Constructor.
	public BasicTreeWithLevelsFactory(E argRootNodeValue)
	{
		this.numberOfNodes = 1;
		this.numberOfLevels = 1;
		this.currentIdentifier = 1;
		
		this.idToNodeMap = new HashMap<Integer, TreeNode<E>>();
		this.levels = new HashMap<Integer, LinkedList<TreeNode<E>>>();
		
		// Create the root node.
		LinkedList<TreeNode<E>> childrenHolder = new LinkedList<TreeNode<E>>();
		
		this.rootNode = new BasicTreeNode(1, 1, null, childrenHolder, argRootNodeValue, true, false);
		this.idToNodeMap.put(1, this.rootNode);
		
		// Add the root node to the level map.
		LinkedList<TreeNode<E>> nodesInLevel = new LinkedList<TreeNode<E>>();
		nodesInLevel.add(rootNode);
		this.levels.put(1, nodesInLevel);
	}
	
	// Public methods.
	public TreeNode<E> addNode(TreeNode<E> argParentNode, E argValue)
	{
		LinkedList<TreeNode<E>> childrenHolder = new LinkedList<TreeNode<E>>();
		
		int newLevel = argParentNode.getLevel() + 1;
		
		TreeNode<E> node = new BasicTreeNode<E>(this.currentIdentifier, newLevel, argParentNode, childrenHolder, argValue, false, true);
	
		// Add to our node id map.
		this.idToNodeMap.put(this.currentIdentifier, node);
				
		this.currentIdentifier++;
		
		// Add this node as a child to the parent node.
		argParentNode.addChildNode(node);
		
		
		
		this.numberOfNodes++;
		
		if(newLevel > this.numberOfLevels)
		{
			this.numberOfLevels++;
		}
		
		// Add to the levels map.
		Integer levelAsInteger = new Integer(newLevel);
		
		this.addNodeToLevelMap(levelAsInteger, node);
		
		return node;
	}
	
	public BasicTreeWithLevels<E> build()
	{
		return new BasicTreeWithLevels<E>(this.rootNode, this.levels, this.numberOfNodes, this.numberOfLevels,
				this.idToNodeMap);
	}
	
	public TreeNode<E> getRootNode()
	{
		return this.rootNode;
	}
	
	private void addNodeToLevelMap(Integer argLevel, TreeNode<E> argNode)
	{		
		boolean hasLevel = this.levels.containsKey(argLevel);
		
		if(hasLevel == false)
		{
			LinkedList<TreeNode<E>> nodesInLevel = new LinkedList<TreeNode<E>>();
			nodesInLevel.add(argNode);
			this.levels.put(argLevel, nodesInLevel);
		}
		
		else
		{
			List<TreeNode<E>> nodesInLevel = this.levels.get(argLevel);
			nodesInLevel.add(argNode);
		}
	}
}

package test.net.surveyos.sourceforge.javautilities.collections.trees;

import static org.junit.Assert.*;

import java.util.Iterator;

import net.surveyos.sourceforge.javautilities.collections.trees.BasicTreeWithLevels;
import net.surveyos.sourceforge.javautilities.collections.trees.BasicTreeWithLevelsFactory;
import net.surveyos.sourceforge.javautilities.collections.trees.TreeNode;
import net.surveyos.sourceforge.javautilities.collections.trees.TreeWalker;

import org.junit.Before;
import org.junit.Test;

public class BasicTreeWithLevelsTest implements TreeWalker
{
	// Private member variables.
	private BasicTreeWithLevels testSubject;
	private int nodeId;
	private TreeNode<Integer> sibling;
	private int visitedNodeCount;
	
	@Before
	public void setUp() throws Exception 
	{
		Integer rootValue = new Integer("1001");
		BasicTreeWithLevelsFactory<Integer> factory = new BasicTreeWithLevelsFactory<Integer>(rootValue);
		
		TreeNode<Integer> rootNode = factory.getRootNode();
		
		// Child in Branch #1.
		Integer value1 = new Integer("2001");
		TreeNode<Integer> childNode1 = factory.addNode(rootNode, value1);
		
		// Child in Branch #2.
		Integer value2 = new Integer("2002");
		TreeNode<Integer> childNode2 = factory.addNode(rootNode, value2);
		
		// Grandchildren in Branch #1.
		Integer value3 = new Integer("3001");
		TreeNode<Integer> returnedNode1 = factory.addNode(childNode1, value3);
		int value100 = returnedNode1.getValue();
		//System.err.println(value100);
		
		Integer value4 = new Integer("3002");
		TreeNode<Integer> returnedNode2 = factory.addNode(childNode1, value4);
		this.nodeId = returnedNode2.getIdentifier();
		int value101 = returnedNode2.getValue();
		//System.err.println(value101);
		
		// Grandchildren in Branch #2.
		Integer value5 = new Integer("3003");
		this.sibling = factory.addNode(childNode2, value5);
		
		Integer value6 = new Integer("3004");
		factory.addNode(childNode2, value6);
		
		this.testSubject = factory.build();
	}

	@Test
	public void testGetRootNode() 
	{
		TreeNode<Integer> rootNode = this.testSubject.getRootNode();
		Integer value = rootNode.getValue();
		
		if(value != 1001)
		{
			fail("The method did not return the correct root node.");
		}
	}

	@Test
	public void testGetNodeWithIdentifier() 
	{
		TreeNode<Integer> returnedNode = this.testSubject.getNodeWithIdentifier(this.nodeId);
		int value = returnedNode.getValue();
		
		if(value != 3002)
		{
			System.err.println(value);
			fail("The method didn't return the correct node.");
		}
	}

	@Test
	public void testGetAllTreeNodesOnLevel() 
	{
		Iterator<TreeNode<Integer>> goOverEach = this.testSubject.getAllTreeNodesOnLevel(3);
		
		int counter = 0;
		
		while(goOverEach.hasNext() == true)
		{
			counter++;
			goOverEach.next();
		}
		
		if(counter != 4)
		{
			fail("The method did not return all of the nodes on the level.");
		}
	}

	@Test
	public void testGetNumberOfNodes() 
	{
		int numberOfNodes = this.testSubject.getNumberOfNodes();
		
		if(numberOfNodes != 7)
		{
			fail("The method didn't return the correct number of nodes.");
		}
	}

	@Test
	public void testGetNumberOfLevels() 
	{
		int numberOfLevels = this.testSubject.getNumberOfLevels();
		
		if(numberOfLevels != 3)
		{
			fail("The method didn't return the correct number of levels.");
		}
	}

	@Test
	public void testWalkTree() 
	{
		this.testSubject.walkTree(this);
		
		if(this.visitedNodeCount != this.testSubject.getNumberOfNodes())
		{
			fail("Not all of the nodes were walked.");
		}
	}

	@Test
	public void testGetSiblingNodes() 
	{
		Iterator<TreeNode<Integer>> goOverEach = this.testSubject.getSiblingNodes(this.sibling);		
		int successCounter = 0;
			
		while(goOverEach.hasNext() == true)
		{
			TreeNode<Integer> currentNode = goOverEach.next();
			Integer value = currentNode.getValue();
			System.err.println(value);

			if(value == 3004)
			{
				successCounter++;
			}
		}
		
		if(successCounter != 1)
		{
			fail("The method did not return all the siblings.");
		}
	}

	// TreeWalker methods.

	@Override
	public void onNewLevel(int argLevel) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void atLeafNode(TreeNode argNode) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void atBranchNode(TreeNode argNode) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean keepWalking() 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void catchNode(TreeNode argNode) 
	{
		Integer value = (Integer) argNode.getValue();
		
		if(value == 1001)
		{
			this.visitedNodeCount++;
		}
		
		if(value == 2001)
		{
			this.visitedNodeCount++;
		}
		
		if(value == 2002)
		{
			this.visitedNodeCount++;
		}
		
		if(value == 3001)
		{
			this.visitedNodeCount++;
		}
		
		if(value == 3001)
		{
			this.visitedNodeCount++;
		}
		
		if(value == 3003)
		{
			this.visitedNodeCount++;
		}
		
		if(value == 3004)
		{
			this.visitedNodeCount++;
		}
	}
}

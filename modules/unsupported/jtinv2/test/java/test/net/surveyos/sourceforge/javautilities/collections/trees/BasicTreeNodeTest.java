package test.net.surveyos.sourceforge.javautilities.collections.trees;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import net.surveyos.sourceforge.javautilities.collections.trees.BasicTreeNode;
import net.surveyos.sourceforge.javautilities.collections.trees.TreeNode;

import org.junit.Before;
import org.junit.Test;

public class BasicTreeNodeTest 
{

	// Private member variables.
	private BasicTreeNode<Integer> testSubject;
	
	@Before
	public void setUp() throws Exception 
	{
		// Create the parent node of the test subject.
		Integer value1 = new Integer("1001");
		
		BasicTreeNode<Integer> parent = new BasicTreeNode<Integer>(1, 1, null, null,
				value1, true, false);
		
		Integer value2 = new Integer("1002");
		
		// Create the test subject.
		
		this.testSubject = new BasicTreeNode<Integer>(2, 2, parent, null,
				value2, false, false);
		
		Integer value3 = new Integer("1003");
		
		// Create the child nodes of the test subject.
		
		BasicTreeNode<Integer> child1 = new BasicTreeNode<Integer>(3, 3, this.testSubject, null,
				value3, false, true);
		
		Integer value4 = new Integer("1004");
		
		BasicTreeNode<Integer> child2 = new BasicTreeNode<Integer>(4, 3, this.testSubject, null,
				value4, false, true);
		
		Integer value5 = new Integer("1005");
		
		BasicTreeNode<Integer> child3 = new BasicTreeNode<Integer>(5, 3, this.testSubject, null,
				value5, false, true);
		
		// Add the child nodes to the test subject.
		testSubject.addChildNode(child1);
		testSubject.addChildNode(child2);
		testSubject.addChildNode(child3);
	}

	@Test
	public void testGetIdentifier() 
	{
		int id = this.testSubject.getIdentifier();
		
		if(id != 2)
		{
			System.err.println("ID = ");
			System.err.println(id);
			fail("The method didn't return the proper ID.");
		}
	}

	@Test
	public void testGetLevel() 
	{
		int level = this.testSubject.getLevel();
		
		if(level != 2)
		{
			fail("Not yet implemented");
		}
	}

	@Test
	public void testGetParent() 
	{
		TreeNode<Integer> parent = this.testSubject.getParent();
		int id = parent.getIdentifier();
		
		if(id != 1)
		{
			fail("The method did not return the proper parent.");
		}
	}

	@Test
	public void testGetChildren() 
	{
		List<TreeNode<Integer>> children = this.testSubject.getChildren();
		
		Iterator<TreeNode<Integer>> goOverEach = children.iterator();
		int counter = 3;
		
		while(goOverEach.hasNext() == true)
		{
			TreeNode<Integer> currentNode = goOverEach.next();
			int id = currentNode.getIdentifier();
			
			if(id != counter)
			{
				fail("The method didn't return the proper children.");
			}
			
			counter++;
		}
	}

	@Test
	public void testGetValue() 
	{
		Integer value = this.testSubject.getValue();
		int valueAsInt = value.intValue();
		
		if(valueAsInt != 1002)
		{
			fail("The method didn't return the proper value.");
		}
	}

	@Test
	public void testIsRootNode() 
	{
		boolean isRootNode1 = this.testSubject.isRootNode();
		
		if(isRootNode1 == true)
		{
			fail("The method improperly indicated a root node.");
		}
		
		TreeNode<Integer> parent = this.testSubject.getParent();
		boolean isRootNode2 = parent.isRootNode();
		
		if(isRootNode2 == false)
		{
			fail("The method failed to indicate a root node.");
		}
	}

	@Test
	public void testAddChildNode() 
	{
		Integer value = new Integer("1006");
		
		BasicTreeNode<Integer> child = new BasicTreeNode<Integer>(6, 3, this.testSubject, null,
				value, false, true);
		
		this.testSubject.addChildNode(child);
		
		List<TreeNode<Integer>> children = this.testSubject.getChildren();
		
		TreeNode<Integer> actualChild = children.get(3);
		int id = actualChild.getIdentifier();
		
		if(id != 6)
		{
			fail("The child node was not properly added or returned.");
		}
	}

	@Test
	public void testIsLeafNode() 
	{
		List<TreeNode<Integer>> children = this.testSubject.getChildren();
		
		TreeNode<Integer> child = children.get(0);
		
		boolean isLeafNode1 = child.isLeafNode();
		
		if(isLeafNode1 == false)
		{
			fail("The method failed to indicate a leaf node.");
		}
		
		TreeNode<Integer> parent = this.testSubject.getParent();
		boolean isLeafNode2 = parent.isLeafNode();
		
		if(isLeafNode2 == true)
		{
			fail("The method improperly indicated a leaf node.");
		}
	}

}

package net.surveyos.sourceforge.javautilities.collections.trees;

public interface TreeWalker<E>
{
	public abstract void catchNode(TreeNode<E> argNode);
	
	public abstract void onNewLevel(int argLevel);
	
	public abstract void atLeafNode(TreeNode<E> argNode);
	
	public abstract void atBranchNode(TreeNode<E> argNode);	
	
	public abstract boolean keepWalking();
}

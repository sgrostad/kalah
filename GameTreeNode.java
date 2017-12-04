/**
* A class for testing the Alpha-beta pruning.  
*/

public class GameTreeNode
{
  public int level;
  public int value;
  public boolean side; //True - our turn, false - enemy
  public int alpha;
  public int beta;
  public GameTreeNode left;
  public GameTreeNode right;
  
  
  public GameTreeNode(int givenLevel, int givenValue, boolean givenSide)
  {
    int level = givenLevel;
    int value = givenValue;
	boolean side = givenSide;
    alpha = -5000; //arbitrary values
    beta = 5000;
    //left;
    //right;
	
  }
  
  public boolean hasChildren(GameTreeNode givenNode)
  {
	  return (givenNode.getLeft() != null || givenNode.getRight() != null);
  }
  
  public boolean prune (int alpha, int beta)
  {
	return alpha >= beta;
  }
  
  
  public static void main(String [] args)
  {
//	GameTreeNode node1 = new GameTreeNode();
//	GameTreeNode node2 = new GameTreeNode();
//	GameTreeNode node3 = new GameTreeNode();
//	GameTreeNode node4 = new GameTreeNode();
//	GameTreeNode node5 = new GameTreeNode();
//	GameTreeNode node6 = new GameTreeNode();
//	GameTreeNode node7 = new GameTreeNode();
	  
  }
  
  
  // Fully traversing the whole tree by the root node
  // at the moment, it assumes that South is our turn, North is the enemy
  void fullTurn(GameTreeNode rootNode)
  {
	if (hasChildren(rootNode) && 
	    !hasChildren(rootNode.getLeft()) &&
		!hasChildren(rootNode.getRight())) //means the left side is a terminal node here
    {                           //we do not prune here, as it is the leftmost case, therefore first
	  //first, the children inherit parents' alpha and beta values
	  
	  rootNode.getLeft().setBeta(rootNode.getBeta());
	  rootNode.getLeft().setAlpha(rootNode.getAlpha());
	  
	  rootNode.getRight().setBeta(rootNode.getBeta());
	  rootNode.getRight().setAlpha(rootNode.getAlpha());
	  
	  if (rootNode.getLeft().getSide())  //if it's our turn
	  {
		if (rootNode.getBeta() <= rootNode.getLeft().getValue())
		{
		  rootNode.setBeta(rootNode.getLeft().getValue());
		  rootNode.setValue(rootNode.getLeft().getValue());
		}
	  }
	  else                //if it's the enemy's turn
	  {
		if (rootNode.getAlpha() >= rootNode.getLeft().getValue())
		{
		  rootNode.setAlpha(rootNode.getLeft().getValue());
		  rootNode.setValue(rootNode.getLeft().getValue());
		}
	  }
	  
	  //now, the Beta value is set on the parent node. Checking right child
	  
	  //before the right child can be assessed, we need to check the alpha and beta values. 
	  if (!prune(rootNode.getAlpha(), rootNode.getBeta()))
	  {
	    if (rootNode.getRight().getSide())  //if it's our turn
	    {
		  if (rootNode.getBeta() <= rootNode.getRight().getValue())
		    rootNode.setBeta(rootNode.getRight().getValue());
	    }
	    else                //if it's the enemy's turn
	    {
		  if (rootNode.getAlpha() >= rootNode.getRight().getValue())
		    rootNode.setAlpha(rootNode.getRight().getValue());
	    }
	  }//if not prune
	  
	  
	}// if terminal node
	
	else if (hasChildren(rootNode.getLeft())) //left side is not yet terminal
	{
		fullTurn(rootNode.getLeft());

	}
    
    //now that we have arrived at the child nodes, time to propagate the values of beta and alpha up.
	
  }	  
  
  
  public GameTreeNode getLeft()
  {
	  return this.left;
  }
  
  public void setLeft(GameTreeNode givenLeft)
  {
	  this.left = givenLeft;
  }
  
  public GameTreeNode getRight()
  {
	  return this.right;
  }
  
  public void setRight(GameTreeNode givenRight)
  {
	  this.right = givenRight;
  }
  
  public boolean getSide()
  {
	  return this.side;
  }
  
  public int getBeta()
  {
	  return this.beta;
  }
  
  public void setBeta( int givenBeta)
  {
	  this.beta = givenBeta;
  }
  
  public int getAlpha()
  {
	  return this.alpha;
  }
  
  public void setAlpha( int givenAlpha)
  {
	  this.alpha = givenAlpha;
  }
  
  public int getValue()
  {
	  return this.value;
  }
  
  public void setValue(int givenValue)
  {
	  this.value = givenValue;
  }
  
  public String toString()
  {
	return ("Level: " + level + "\n" + "Value: " + value + "\n" + "AlphaValue");
	  
  }
}// end class

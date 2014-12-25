/*
 * Class Name:    RBNode
 *
 * @author Thomas McKeesick
 * Creation Date: Monday, July 14 2014, 20:37 
 * Last Modified: Thursday, September 11 2014, 11:06
 * 
 * Class Description: The Red-Black Node class file,
 *                    all functionality javadoc'd
 */

public class RBNode<T extends Comparable< T >>
{
   /**
    * Final char variable declaring RED as 'R'
    */
   private final static char RED = 'R';
   
   /**
    * Final char variable declaring BLACK as 'B'
    */
   private final static char BLACK = 'B';

   /**
    * The data contained in this node, must extend
    * the java Comparable interface
    */
   private T data;

   /**
    * The colour of this node, either RED or BLACK
    */
   private char colour;
   
   /**
    * The right child of this node
    */
   private RBNode<T> rightChild;

   /**
    * The left child of this node
    */
   private RBNode<T> leftChild;

   /**
    * Boolean that is true if the node has been deleted, false otherwise
    */
   private boolean deleted;
   
   /**
    * Public constructor that creates a new RBNode, initiates the
    * colour as red
    * @param data The data to be stored in the node
    */
   public RBNode(T data)
   {
	   this.data = data;
	   
	   colour = RED;
	   rightChild = null;
	   leftChild = null;
	   deleted = false;
   }
   
   /************************************\
    *			SETTER METHODS              |
    ************************************/
   
   /**
    * Replaces the current node data with new data
    * 
    * @param data The new data to replace the old
    */
   public void setData(T data)
   {
	   this.data = data;
   }
   
   /**
    * Performs a colour switch
    * 
    * @param c The new colour to switch to
    * 
    * @return True if the switch is successful, false if c is invalid
    * or is the same as the current colour
    */
   public boolean setColour( char c )
   {
	   //If the colour is black or red, and it is also
	   //not the same as the current colour, change the colour
	   if( ( c == RED || c == BLACK ) && c != colour )
	   {
		   colour = c;
		   return true;
	   }
	   return false;
   }

   /**
    * Public method to set a node as the left child of this node
    * @param node The node to insert as the left child
    */
   public void setLeftChild( RBNode<T> node )
   {
      leftChild = node;
   }

   /**
    * Public method to set a node as the right child of this node
    * @param node The node to insert as the right child
    */
   public void setRightChild( RBNode<T> node )
   {
      rightChild = node;
   }

   /**
    * Public method to delete a node. Does not actually remove the
    * node, rather sets the 'deleted' boolean to true. If a deleted 
    * node is discovered by the RBTree contains method, null is returned
    */
   public void delete()
   {
      deleted = true;
   }

   /************************************\
    *			GETTER METHODS			|
    ************************************/
   
   /**
    * Public method to return the data stored in the node
    * 
    * @return The data stored in the node
    */
   public T getData()
   {
	   return data;
   }
   
   /**
    * Public method to return the colour of the node
    * @return The colour of the node
    */
   public char getColour()
   {
	   return colour;
   }
   
   /**
    * Public method to return the left child of the node
    * @return The left child of the node
    */
   public RBNode<T> getLeftChild()
   {
	   return leftChild;
   }
   
   /**
    * Public method to return the right child of the node
    * @return The right child of the node
    */
   public RBNode<T> getRightChild()
   {
	   return rightChild;
   }
   
   /**
    * Public method to return whether or not this node has been deleted.
    * Used by the RBTree contains method.
    * @return True if the node has been deleted, false otherwise
    */
   public boolean isDeleted()
   {
	   return deleted;
   }
   
   /**
    * Public method to display the node and its subtree.
	 * @param n The level of the node and used for the indentation
	 */
	public void display(int n)
	{
		String indent = "- ";
		for(int i = 1; i <= n; i++)
		{
			System.out.print(indent);
		}
		System.out.println("ROOT: " + data + ", colour: " + colour);

		for(int i = 1; i <= n; i++)
		{
			System.out.print(indent);
		}
		System.out.println("LEFT");
		if( leftChild == null)
		{ 
			for(int i = 1; i <= n+1; i++)
			{
				System.out.print(indent);
			}
			System.out.println("null");
		}
		else
		{
			leftChild.display(n+1);
		}

		for(int i = 1; i <= n; i++)
		{
			System.out.print(indent);
		}
		System.out.println("RIGHT");
		if( rightChild == null)
		{
			for(int i = 1; i <= n+1; i++)
			{
				System.out.print(indent);
			}
			System.out.println("null"); 
		}
		else
		{
			rightChild.display(n+1);
		}
	}
}

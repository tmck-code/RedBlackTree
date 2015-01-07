/******************************************************************************\
 * Class Name:    RBTree                                                       \
 *                                                                             \
 * @author Thomas McKeesick                                                    \
 * Creation Date: Monday, July 14 2014, 21:27                                  \
 * Last Modified:     Sunday, January 04 2015, 12:46
 *                                                                             \
 * Class Description: A recursive implementation of a Red-Black Tree,          \
 * fully javadoc'd                                                             \
 ******************************************************************************/

import java.io.PrintWriter;

public class RBTree<T extends Comparable<T>> {
    
    /** For use in debugging; The amount of rotations performed by the tree */
    public static int rotCount = 0;

    /** For use in debugging; The amount of nodes visited by the program */
    public static int hitCount = 0;

    /** Character representing the colour BLACK as 'B' */
	private static final char BLACK = 'B';

	/** Character representing the colour RED as 'R' */
	private static final char RED = 'R';

	/** The root node of this tree */
	private RBNode<T> root;

	/** Public constructor for the tree, initialises the root as null */
	public RBTree() {
		root = null;
	}

    /** 
     * Public method to return the root node of the tree 
     *  @return The root node of the tree
     */
    public RBNode<T> getRoot(){
      return root;
    }

    /**
     * Public method to call the recursice put method to insert data into the tree
     * @param data The data to insert
     */
   //TODO change general exception throw to a specific exception
    public void insert(T data) throws Exception {
        root = put( root, data );
        root.setColour( BLACK );
    }

    /**
     * Private recursive insertion method, searches down the tree until it
     * reaches the insertion point, then recursively rebalances the tree going
     * back up.
     * @param node The root node of the tree initially, then the node that the 
     * recursive method is at
     * @param data The data to insert
     * @return The node that has been inserted
     */
    //TODO throw better exception
    private RBNode<T> put( RBNode<T> node, T data ) throws Exception {
        if( node == null ) {
            RBNode<T> newNode = new RBNode<T>(data);
            return newNode;
        }
        int cmp = data.compareTo( node.getData() );

        if( cmp < 0 ) {
            hitCount++;
            node.setLeftChild( put( node.getLeftChild(), data ) );
        } else if( cmp > 0 ) {
            hitCount++;
            node.setRightChild( put( node.getRightChild(), data ) );
        } else {
            throw new Exception("Data already exists in tree: " + data.toString());
        }

        //Red-red conflict with outside grandchild
        if( isRed( node.getLeftChild() ) && isRed( node.getLeftChild().getLeftChild() ) ) {
            node.setColour( RED );
            node.getLeftChild().setColour( BLACK );
            node = rightRotation(node);
        }

        //Red-red conflict with right-outside grandchild
        if( isRed( node.getRightChild() ) && isRed( node.getRightChild().getRightChild() ) ) {
            node.setColour( RED );
            node.getRightChild().setColour( BLACK );
            node = leftRotation(node);
        }

        //Red-red conflict with left-right inside grandchild
        if( isRed( node.getLeftChild() ) && isRed( node.getLeftChild().getRightChild() ) ) {
            node.setColour( RED );
            node.getLeftChild().getRightChild().setColour( BLACK );
            node.setLeftChild( leftRotation(node.getLeftChild() ) );
            node = rightRotation( node );
        }

        //Red-red conflict with right-left inside grandchild
        if( isRed( node.getRightChild() && isRed( node.getRightChild().getLeftChild() ) ) {
            node.setColour( RED );
            node.getRightChild().getLeftChild().setColour( BLACK );
            node.setRightChild( rightRotation( node.getRightChild() ) );
            node = leftRotation( node );
        }

        colourFlip(node);
        hitCount++;
        return node;
    }

    public int getRotCount() {
        return rotCount;
    }

    public int getHitCount() {
        return hitCount;
    }

	/**
	 * Searches for the supplied data by in-order traversal
	 * @param data The data to search for
	 * @return The node that contains the data if it is found (and not marked as
     * deleted), null otherwise
	 */
    public RBNode<T> contains(T data) {
        RBNode<T> current = root;
        int count = 0;
		while( data.compareTo(current.getData()) != 0 ) {
			if( data.compareTo(current.getData()) < 0 ) {
				current = current.getLeftChild();
			} else {
				current = current.getRightChild();
			}
			if( current == null || current.isDeleted() ) {
				return null;
			}
        count++;
        }
        System.out.println("Depth is " + count);
        return current;
    }

    /**
     * Public method to remove a node from the tree. Calls the delete method
     * from the node which changes its deleted boolean to true, does not actually
     * perform a deletion
     * @param data The data to remove from the tree
     * @return True if the delete was successful (the element exits in the tree),
     * and false otherwise
     */
     //TODO throw better exception
    public boolean removeElement( T data ) throws Exception {
        RBNode<T> node = contains( data );

        if( node != null ) {
            node.delete();
            return true;
        } else {
            throw new Exception("Data does not exist: " + data.toString());
        }
    }

    /***************************************\
	 *         COLOUR METHODS               |
	 ***************************************/

    /**
     * Public method to determine whether or not a node is red
     * @param node The node to colour-check
     * @return false if BLACK, true if RED
     */
    public boolean isRed( RBNode<T> node ) {
      if( node == null ) {
         return false;
      }
      return node.getColour() == RED;
   }

    /**
     * Private method to perform color flip on a parent node and
     * its two children. Only called if the parent is black
     * and both children are red. If the parent node is the root node,
     * then it stays as black
     * @param parent The parent node to colour flip
     */
    private void colourFlip( RBNode<T> parent ) {
        if( parent.getRightChild() == null ||
        parent.getLeftChild() == null ) {
            return;
    }

        if( parent.getColour() == BLACK
        && parent.getRightChild().getColour() == RED
        && parent.getLeftChild().getColour() == RED ) {
            if( parent != root ) {
                parent.setColour( RED );
            }

            parent.getRightChild().setColour( BLACK );
            parent.getLeftChild().setColour( BLACK );
        }
    }

    /**
     * Private method to switch the colour of a node
     * @param node The node to switch the colour of
     */
    private void switchNodeColour( RBNode<T> node ) {
        if( node.getColour() == RED ) {
            node.setColour( BLACK );
        } else {
            node.setColour( RED );
        }
    }

    /***************************************\
	 *			ROTATION METHODS               |
	 ***************************************/

    /**
     * Private method to perform a right rotation around a supplied grandparent node
     * @param grandparent The node to rotate around
     * @return The node now in place of the original grandparent
     */
    private RBNode<T> rightRotation( RBNode<T> grandparent ) {
        RBNode<T> parent = grandparent.getLeftChild();
        RBNode<T> rightChildOfParent = parent.getRightChild();
        parent.setRightChild( grandparent );
      grandparent.setLeftChild( rightChildOfParent );

      rotCount++;
      return parent;
   }

    /**
     * Private method to perform a left rotation around a supplied grandparent
     * node
     * @param grandparent The node to rotate around
     * @return The node now in place of the original grandparent
     */
    private RBNode<T> leftRotation( RBNode<T> grandparent ) {
        RBNode<T> parent = grandparent.getRightChild();
        RBNode<T> leftChildOfParent = parent.getLeftChild();
        parent.setLeftChild( grandparent );
        grandparent.setRightChild( leftChildOfParent );
        rotCount++;
        return parent;
   }

   /***************************************\
	 *			TRAVERSAL METHODS              |
	 ***************************************/

	/**
	 * Public method called to display the tree
	 * @param p The PrintWriter object to write to
	 */
	public void displayElements(PrintWriter p) {
	   displaySubtreeInOrder(root, p);
	}

	/**
	 * Private in-order traversal method called by the displayElements()
	 * method.
	 * @param current The current node to print
	 * @param p The PrintWriter to write to
	 */
    private void displaySubtreeInOrder(RBNode<T> current, PrintWriter p) {
        if( current != null ) {
            displaySubtreeInOrder( current.getLeftChild(), p );
            p.println( "Data is " + current.getData()
        + "Node colour: " + current.getColour() );
            displaySubtreeInOrder( current.getRightChild(), p );
        }
    }

    /**
     * Public method to print the tree in human-readable form. Calls the display
     * method within the RBNode class and prints output to the console
     */
    public void printStructure() {
        if(root == null) {
            System.out.println("null");
        } else {
            System.out.println("*****************************************");
            root.display(0);
            System.out.println("*****************************************");
        }
        System.out.println();
    }

}

/*****************************************************************************\
 * Class Name:    RBTree                                                       \
 *                                                                             \
 * @author Thomas McKeesick                                                    \
 * Creation Date: Monday, July 14 2014, 21:27                                  \
 * Last Modified:     Friday, January 23 2015, 12:24
 *                                                                             \
 * Class Description: A recursive implementation of a Red-Black Tree,          \
 * fully javadoc'd                                                             \

 *
 * @version 1.0.0
 * See CHANGELOG
 *****************************************************************************/

import java.io.PrintWriter;

public class RBTree<T extends Comparable<T>> {

    private RBNode<T> root;

    public RBTree() {
        root = null;
    }

    public RBNode<T> getRoot() {
        return root;
    }

    /**
     * Public method to call the recursive put method to insert
     * data into the tree
     * @param data The data to insert
     * @return True if the insertion was successful, false if the data
     * is already in the tree
     */
    public boolean insert(T data) {
        try {
            root = put( root, data );
            root.setBlack();
        } catch(IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    /**
     * Private recursive insertion method, searches down the tree until it
     * reaches the insertion point, then recursively rebalances the tree going
     * back up.
     * @param node The root node of the tree initially, then the node that the
     * recursive method is at
     * @param data The data to insert
     * @return The node that has been inserted
     * @throws IllegalArgumentException if the data already exists in the tree
     */
    private RBNode<T> put( RBNode<T> node, T data )
        throws IllegalArgumentException {
        if( node == null ) {
            RBNode<T> newNode = new RBNode<T>(data);
            return newNode;
        }
        int cmp = data.compareTo( node.getData() );

        if( cmp < 0 ) {
            node.setLeftChild( put( node.getLeftChild(), data ) );
        } else if( cmp > 0 ) {
            node.setRightChild( put( node.getRightChild(), data ) );
        } else {
            throw new IllegalArgumentException("Data already exists in tree: "
                    + data.toString());
        }

        //Red-red conflict with outside grandchild
        if( isRed( node.getLeftChild() )
                && isRed( node.getLeftChild().getLeftChild()) ) {
            node.setRed();
            node.getLeftChild().setBlack();
            node = rightRotation(node);
        }

        //Red-red conflict with right-outside grandchild
        if( isRed( node.getRightChild() )
                && isRed( node.getRightChild().getRightChild()) ) {
            node.setRed();
            node.getRightChild().setBlack();
            node = leftRotation(node);
        }

        //Red-red conflict with left-right inside grandchild
        if( isRed( node.getLeftChild() )
                && isRed( node.getLeftChild().getRightChild()) ) {
            node.setRed();
            node.getLeftChild().getRightChild().setBlack();
            node.setLeftChild( leftRotation(node.getLeftChild() ) );
            node = rightRotation( node );
        }

        //Red-red conflict with right-left inside grandchild
        if( isRed( node.getRightChild() )
                && isRed( node.getRightChild().getLeftChild()) ) {
            node.setRed();
            node.getRightChild().getLeftChild().setBlack();
            node.setRightChild( rightRotation( node.getRightChild() ) );
            node = leftRotation( node );
        }

        colourFlip(node);
        return node;
    }

    /**
     * Searches for the supplied data by in-order traversal
     * @param data The data to search for
     * @return The node that contains the data if it is found (and not marked as
     * deleted), null otherwise
     */
    public RBNode<T> contains(T data) {
        RBNode<T> current = root;
        while( data.compareTo(current.getData()) != 0 ) {
            if( data.compareTo(current.getData()) < 0 ) {
                current = current.getLeftChild();
            } else {
                current = current.getRightChild();
            }
            if( current == null ) {
                return null;
            }
        }
        if( current.isDeleted() ) {
            return null;
        }
        return current;
    }

    /**
     * Public method to remove a node from the tree. Calls the delete method
     * from the node which changes its deleted boolean to true, does not
     * actually perform a deletion
     * @param data The data to remove from the tree
     * @return True if the delete was successful (the element exits
     * in the tree), and false otherwise
     */
    public boolean removeElement( T data ) {
        RBNode<T> node = contains( data );

        if( node != null ) {
            node.delete();
            return true;
        } else {
            return false;
        }
    }

    /**........\----------------------\
      *			COLOUR METHODS         |
      *.........\--------------------*/

    /**
     * Public method to determine whether or not a node is red. This method
     * is in the tree class to avoid null-pointer exceptions.
     * @param node The node to colour-check
     * @return false if BLACK or the node is null, true if RED
     */
    public boolean isRed( RBNode<T> node ) {
        if( node == null ) {
            return false;
        }
        return node.isRed();
    }

    /**
     * Private method to perform colour flip on a parent node and
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
        if( !isRed(parent) && isRed(parent.getRightChild())
            && isRed(parent.getLeftChild()) ) {
            if( parent != root ) {
                parent.setRed();
            }
            parent.getRightChild().setBlack();
            parent.getLeftChild().setBlack();
        }
    }

    /**........\----------------------\
      *		   ROTATION METHODS        |
      *.........\--------------------*/

    /**
     * Private method to perform a right rotation around a supplied grandparent
     * node
     * @param grandparent The node to rotate around
     * @return The node now in place of the original grandparent
     */
    private RBNode<T> rightRotation( RBNode<T> grandparent ) {
        RBNode<T> parent = grandparent.getLeftChild();
        RBNode<T> rightChildOfParent = parent.getRightChild();
        parent.setRightChild( grandparent );
        grandparent.setLeftChild( rightChildOfParent );
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
        return parent;
    }

    /**........\----------------------\
      *			TRAVERSAL METHODS      |
      *.........\--------------------*/

    /**
     * Public method called to display the tree, currently in-order traversal
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
                    + "Node colour: " + current.isRed() );
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

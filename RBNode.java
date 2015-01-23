/**
 * Class Name:    RBNode
 *
 * @author Thomas McKeesick
 * Creation Date: Monday, July 14 2014, 20:37
 * Last Modified:     Friday, January 23 2015, 12:08
 *
 * Class Description: The Red-Black Node class file,
 *                    all functionality javadoc'd
 *
 * @version 1.0.0
 * See CHANGELOG
 */

public class RBNode<T extends Comparable< T >> {

    public static final boolean RED = true;
    public static final boolean BLACK = false;

    private T data;
    private boolean colour = RED;
    private RBNode<T> rightChild;
    private RBNode<T> leftChild;
    private boolean isDeleted = false;

    /**
     * Public constructor that creates a new RBNode, initiates the colour as red
     * @param data The data to be stored in the node
     */
    public RBNode(T data) {
        this.data = data;
    }

    /**........\----------------------\
      *			SET METHODS            |
      *.........\--------------------*/

    /**
     * Replaces the current node data with new data
     * @param data The new data to replace the old
     */
    public void setData(T data){
        this.data = data;
    }

    public void setRed() {
        colour = RED;
    }

    public void setBlack() {
        colour = BLACK;
    }

    /**
     * Performs a colour switch
     * @param c The new colour to switch to
     * @return True if the switch is successful, false if the parameter
     * is the same as the current colour
     */
    public boolean setColour( boolean c ) {
        if(c != colour) {
            colour = c;
            return true;
        }
        return false;
    }

    public void setLeftChild( RBNode<T> node ) {
        leftChild = node;
    }

    public void setRightChild( RBNode<T> node ) {
        rightChild = node;
    }

    /**
     * Public method to delete a node. Does not actually remove the
     * node, rather sets the 'deleted' boolean to true. If a deleted
     * node is discovered by the RBTree contains method, null is returned
     */
    public void delete() {
        isDeleted = true;
    }

    /**........\----------------------\
     *			GET METHODS            |
     *.........\---------------------*/

    public T getData() {
        return data;
    }

    public boolean isRed() {
        return colour == true;
    }

    /**
     * Public method to return the left child of the node
     * @return The left child of the node
     */
    public RBNode<T> getLeftChild() {
        return leftChild;
    }


    public RBNode<T> getRightChild() {
        return rightChild;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    /**
     * Public method to display the node and its subtree.
     * @param n The level of the node and used for the indentation
     */
    public void display(int n) {
        String indent = "- ";

        //Print the indents for this level
        for(int i = 1; i <= n; i++) {
            System.out.print(indent);
        }
        //Print the node contents
        System.out.println("ROOT: " + data + ", colour: " + colour);
        //Indent
        for(int i = 1; i <= n; i++) {
            System.out.print(indent);
        }
        //Print the left child of the node
        System.out.println("LEFT");
        if( leftChild == null) {
            for(int i = 1; i <= n+1; i++) {
                System.out.print(indent);
            }
            System.out.println("null");
        } else {
            leftChild.display(n+1);
        }
        //Indent
        for(int i = 1; i <= n; i++) {
            System.out.print(indent);
        }

        //Print the right child of the node
        System.out.println("RIGHT");
        if( rightChild == null) {
            for(int i = 1; i <= n+1; i++) {
                System.out.print(indent);
             }
            System.out.println("null");
        } else {
            rightChild.display(n+1);
        }
    }
}

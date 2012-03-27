/** 
 * Represents a AVL Tree. It is self-balancing.
 * It uses rotation methods to self balance. 
 * It also includes functionality for printing,
 * as well as searching, inserting and deleting.
 * 
 * @author Ryan Seys
 */
public class AVLDictionary<E, K extends Sortable> implements Dictionary<E, K> {
    AVLNode<E, K> root; // the root of the AVL Tree.

    private static final int BALANCED = 2;
    private static final int MORELEFT = 1;
    private static final int MORERIGHT = 3;
    public boolean debugging = true;

    /**
     * Constructor for the AVL Tree.
     */
    public AVLDictionary() {
        this(null); //calls the other constructor.
    }

    /**
     * Constructor with a non-default root node.
     * @param root the non-default root value.
     */
    public AVLDictionary(AVLNode<E, K> root) {
    	//simply set the root to the passed parameter.
        this.root = root;
    }

    /**
	 * Balances the node using rotations if required.
	 * Returns the balanced node.
	 */
	public AVLNode<E, K> balance(AVLNode<E, K> node) {
		int balanceFactor = node.getBalance();
		if(balanceFactor > MORERIGHT) {
			//need to do a right rotate
			AVLNode<E, K> rightNode = copyNode(node.getRight());
			if(rightNode == null) return node;
			if(rightNode.getBalance() == MORELEFT) {
				//right-left rotate
				node = rotateRIGHTLEFT(copyNode(node));
				node = rotateRIGHTRIGHT(copyNode(node));
				node.setBalance(2); //set it to be balanced.
			}
			else if (rightNode.getBalance() >= MORERIGHT){
				//right-right rotate
				node = rotateRIGHTRIGHT(copyNode(node));
				node.setBalance(2); //set it to be balanced.
			}
		}
		else if(balanceFactor < MORELEFT) {
			//need to do a left rotate
			AVLNode<E, K> leftNode = copyNode(node.getLeft());
			if(leftNode == null) return node;
			if (leftNode.getBalance() == MORERIGHT) {
				//left-right rotate
				node = rotateLEFTRIGHT(copyNode(node));
				node = rotateLEFTLEFT(copyNode(node));
				node.setBalance(2); //set it to be balanced.
			}
			else if(leftNode.getBalance() <= MORELEFT) {
				//left-left rotate
				node = rotateLEFTLEFT(copyNode(node));
				node.setBalance(2); //set it to be balanced.
			}
		}
		return copyNode(node);
	}

    /**
	 * Copies a node to a new node.
	 * 
	 * @param node the node to copy
	 * @return the copied node.
	 */
	public AVLNode<E, K> copyNode(AVLNode<E, K>  node) {
		//simply calls the new constructor.
	    if(node != null) {
	        return new AVLNode<E, K>(node.getKey(), node.getElement(), node.getLeft(), node.getRight(), node.getBalance());
	    }
	    else return null;
	}

    /**
     * Delete an entry with key passed as the parameter.
     * @param key The key we wish to delete from this tree.
     */
    public void delete(K key) {
    	//calls the recursive delete recursive
        this.root = deleteRecursive(root, key);
    }
    
    /**
     * Recursive solution to deleting a double node. More details
     * are prevalent beside where this method is called.
     * 
     * @param node the node to the right of the node we are deleting.
     * @return the node to the right however re-created to not contain the minimum node.
     */
    public AVLNode<E, K> deleteDoubleNode(AVLNode<E, K> node) {
        if(node.getLeft() == null) {
            //at the bottom of the nodes.
            return node.getRight();
        }
        else {
            //set the left node as the right of the one we found at the bottom.
            node.setLeft(deleteDoubleNode(node.getLeft()));
        }
        return node;
    }

    /**
     * Recursive solution to deleting a node. Returns either a node without the 
     * key attached to it (via referencing) or the original node due to a lack
     * of finding the node with the key.
     * 
     * @param node node under which we will look for the key node to be deleted
     * @param key the key to the node which is being deleted
     * @return a copy of the node given, however with the deleted node missing.
     */
    public AVLNode<E, K> deleteRecursive(AVLNode<E,K> node, K key) {
        //you are at the node you want to delete
        if(key.compareTo(node.getKey()) == 0) {
            //if its a leaf
            if((node.getLeft() == null) && (node.getRight() == null)) {
                return null; 
            }
            //it is a node with one child on the right
            else if((node.getLeft() == null) && (node.getRight() != null)) {
                return node.getRight();
            }
            //it is a node with one child on the left
            else if((node.getLeft() != null) && (node.getRight() == null)) {
                return node.getLeft();
            }
            //It is a node with 2 children. This is more tricky.
            else if((node.getLeft() != null) && (node.getRight() != null)) {
                //the replacement node is the least node which is still greater than the one deleted.
                AVLNode<E, K> replacementNode = findMin(node.getRight()); 
                AVLNode<E, K> backupLeft = node.getLeft(); //save the left nodes
                //the min-value node's right is now set as all the right nodes minus itself.
                replacementNode.setRight(deleteDoubleNode(node.getRight())); 
                replacementNode.setLeft(backupLeft); //replace the left nodes as the regular left nodes
                return replacementNode; //return the new least "greater" node.
            }
        }
        //if the key is still less than the node we are at...
        else if(key.compareTo(node.getKey()) < 0) {
            if(node.getLeft() != null) {
                //keep looking but to the left of this node
                node.setLeft(copyNode(deleteRecursive(node.getLeft(), key)));
                node = balance(copyNode(node)); //rebalance tree from this node
            }
        }
        //if the key is still greater than the node we are at...
        else {
            if(node.getRight() != null) {
                //keep looking but to the right of this node
                node.setRight(copyNode(deleteRecursive(node.getRight(), key)));
                node = balance(copyNode(node)); //rebalance tree from this node
            }
        }
        return node; //return the regular node if all else fails (failsafe, most likely won't be reached).
    }

    /** 
     * Returns the depth of the root of this tree.
     * @return the depth of the tree in integer form.
     */
    public int depth() {
        return postorder_depth(root, 0); //calls the recursive method.
    }

    /**
     * Finds the minimum value from a particular node.
     * This simply involves traversing the left-most nodes
     * all the way down the tree until the left-most node is null.
     * 
     * @param node the node to start finding the min from
     * @return the node which contains the minimum value (the left most leaf)
     */
    public AVLNode<E, K> findMin(AVLNode<E, K> node) {
    	//continually gets the leftmost node until it can't any longer.
        while(node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }
    
	/** 
     * This is a recursive solution to printing out the tree
     * in the order of "inorder" processing.
     * @param node the node we are traversing from
     */
    public void inorder(AVLNode<E,K> node) {
        if(node != null) {
            inorder(node.getLeft()); //get the left keys first
            System.out.println("key: " + node.getKey().toString() + " element: " + node.getElement().toString());
            inorder(node.getRight()); //thing get the right keys.
        }
    }
	
	/** 
     * Insert a key-value pair into the AVL tree.
     * Uses the recursive solution as a helper method.
     */
    public void insert(K key, E element) {
        // there are no items yet in the AVL tree
        if(root == null) {
            root = new AVLNode<E, K>(key, element, null, null, BALANCED);
        }
        // there are items in the AVL tree
        // so we must find where to put the item. (by key)
        else {
            root = copyNode(insertBelow(root, key, element));
        }
    }

	/**
     * A recursive solution to inserting a node below a specific node.
     * 
     * @param node the node we wish to insert our new node underneath
     * @param key the key value of the new node to be inserted
     * @param element the element value of the node to be inserted
     */
    public AVLNode<E, K> insertBelow(AVLNode<E, K> node, K key, E element) {
    	if(node == null) {
    		return new AVLNode<E, K>(key, element, null, null, 2);
    	}
    	else if(key.compareTo(node.getKey()) == 0) {
    		return node; // they are equal.
    	}
    	else if(key.compareTo(node.getKey()) < 0) {
    		//key is less than
    		if(node.getLeft() == null) {
    			//there is no node to insert it into
    			node.setLeft(new AVLNode<E, K>(key, element, null, null, 2));
    		}
    		else {
    			//insert it into this node (recursive part)
    			node.setLeft(copyNode(insertBelow(copyNode(node.getLeft()), key, element)));
    		}
    		//set the new balance to be more left
    		node.setBalance(node.getBalance()-1);
    	}
    	else if(key.compareTo(node.getKey()) > 0) {
    		//key is greater than
    		if(node.getRight() == null) {
    			//there is no node to insert it into
    			node.setRight(new AVLNode<E, K>(key, element, null, null, 2));
    		}
    		else {
    			//recursive part
    			node.setRight(copyNode(insertBelow(copyNode(node.getRight()), key, element)));
    		}
    		//set the new balance to be more right
    		node.setBalance(node.getBalance()+1);
    	}
    	return copyNode(balance(node));
}

	/** 
     * Recursive counter to count the depth of the tree's node.
     * 
     * @param node the node we are starting to calculate the depth from
     * @param current_depth the current depth that has been calculated previously.
     * @return the depth of the tree in a given particular node.
     */
    int postorder_depth(AVLNode<E,K> node, int current_depth) {
        if(node != null) {
            //get the max between two sub-trees, recursively, finding the max between all the sub-trees, or the entire tree itself.
            return Math.max(postorder_depth(node.getLeft(), current_depth+1), postorder_depth(node.getRight(), current_depth+1));
        }
        else return current_depth;
    }
	
	/**
     * Print the Dictionary in sorted order (as determined by the keys)
     * to print in sorted order, we traverse and print the tree "inorder".
     */
    public void printTree() {
        System.out.println("\nPrinting the AVL Tree below...");
        inorder(root);
    }
	    
	/** 
	 * Rotate method for the LEFT-LEFT case.
	 * 
	 * @param node the node which we need to perform the rotate on.
	 * @return a new rotated node.
	 */
	public AVLNode<E, K> rotateLEFTLEFT(AVLNode<E, K> node) {
		//get the new nodes (as copies) to modify and rotate.
		AVLNode<E, K> newCenter = copyNode(node.getLeft()); //new center node
		AVLNode<E, K> newRight = copyNode(node); //new right node to center node
		//this rotates the new nodes around so that they
		//reflect the new arrangement which is more sorted.
		newRight.setLeft(copyNode(newCenter.getRight()));
		newRight.setBalance(2); //set it to be balanced.
		newCenter.setRight(copyNode(newRight)); //set the new right.
		newCenter.setBalance(2); //set it to be balanced.
		return newCenter; //return the newly rotated subtree
	}

    /** 
	* Rotate method for the LEFT-RIGHT case.
	* 
	* @param node the node which we need to perform the rotate on.
	* @return a new rotated node.
	*/
	public AVLNode<E, K> rotateLEFTRIGHT(AVLNode<E, K> node) {
		//get the new nodes (as copies) to modify and rotate.
		AVLNode<E, K> newTop = copyNode(node);
		AVLNode<E, K> newCenter = copyNode(node.getLeft().getRight());
		AVLNode<E, K> newBottom = copyNode(node.getLeft());
		//this rotates the new nodes around so that they
		//reflect the new arrangement which is more sorted.
		if(newCenter == null) return node;
		newBottom.setRight(copyNode(newCenter.getLeft()));
		newBottom.setBalance(2); //set it to be balanced.
		newCenter.setLeft(copyNode(newBottom));
		newCenter.setBalance(1); //set it to be more left
		newTop.setLeft(copyNode(newCenter));
		newTop.setBalance(0); //extra more left
		return newTop; //return the newly rotated subtree
	}
    
    /** 
	 * Rotate method for the RIGHT-LEFT case.
	 * 
	 * @param node the node which we need to perform the rotate on.
	 * @return a new rotated node.
	 */
	public AVLNode<E, K> rotateRIGHTLEFT(AVLNode<E, K> node) {
		AVLNode<E, K> newTop = copyNode(node);
		AVLNode<E, K> newCenter = copyNode(node.getRight().getLeft());
		AVLNode<E, K> newBottom = copyNode(node.getRight());
		//this rotates the new nodes around so that they
		//reflect the new arrangement which is more sorted.
		if(newCenter == null) return node; //make sure this isn't null
		newBottom.setLeft(copyNode(newCenter.getRight()));
		newBottom.setBalance(2); //set to balanced
		newCenter.setRight(copyNode(newBottom));
		newCenter.setBalance(3); //more right
		newTop.setRight(copyNode(newCenter)); //set the new right
		newTop.setBalance(4); // extra extra right
		return newTop; //return the newly rotated subtree
	}

    /**
	 * Rotate method for the RIGHTRIGHT case.
	 * 
	 * @param node the node which we need to perform the rotate on.
	 * @return a new rotated node.
	 */
	public AVLNode<E, K> rotateRIGHTRIGHT(AVLNode<E, K> node) {
		//get the new nodes (as copies) to modify and rotate.
		AVLNode<E, K> newCenter = copyNode(node.getRight());
		AVLNode<E, K> newLeft = copyNode(node);
		//this rotates the new nodes around so that they
		//reflect the new arrangement which is more sorted.
		if(newCenter == null) return node; //make sure its not null
		newLeft.setRight(copyNode(newCenter.getLeft()));
		newLeft.setBalance(2); //set to balanced
		newCenter.setLeft(copyNode(newLeft)); 
		newCenter.setBalance(2); //set to balanced
		return newCenter; //return the newly rotated subtree
	}

    /**
     * Returns the element of the node with the key value
     * of the specified value.
     */
    public E search(K key) {
        AVLNode<E, K> nodeFound;
        nodeFound = searchNode(key);
        if(nodeFound == null) {
            return null; //not found
        }
        else return searchNode(key).getElement(); //call another helper method.
    }

    /**
     * A recursive solution to finding a specific key in the binary tree.
     * 
     * @param node the node we wish to start searching below from.
     * @param key the key value of the node we are looking for.
     * @return the node which has the key value we are looking for.
     */
    public AVLNode<E,K> searchBelow(AVLNode<E, K> node, K key) {
        if(node == null) {
            return null; //nothing correctly passed.
        }
        // if the key we are looking for was found
        if(key.compareTo(node.getKey()) == 0) {
            return node;
        }
        // if the key we are looking for is greater than the key
        // at the node we are at, then we must go down the right child
        // to continue the search.
        else if (key.compareTo(node.getKey()) > 0) {
            return searchBelow(node.getRight(), key);
        }
        // if the key we are looking for is less than the key
        // at the node we are at, then we must go down the left child
        // to continue the search.
        else if(key.compareTo(node.getKey()) < 0) {
            return searchBelow(node.getLeft(), key);
        }
        else return null;
    }

    /**
     * Search for an entry with key KEY and return the node object
     * @param key the key value of the node we are looking for.
     * @return null if no such key was found, the node it was found in if it was found.
     */
    public AVLNode<E, K> searchNode(K key) {
        if(key == null) {
            return null; //looking for nothing
        }
        if(root == null) {
            return null; //nothing in the root, empty tree.
        }
        //if the keys are equal
        if(key.compareTo(root.getKey()) == 0) {
            return root;
        }
        //if this key is less than the root's key
        else if(key.compareTo(root.getKey()) < 0) {
            return searchBelow(root.getLeft(), key); //call the recursive search method.
        }
        //if this key is greater than the root's key
        else if(key.compareTo(root.getKey()) > 0) {
            return searchBelow(root.getRight(), key); //call the recursive search method.
        }
        else {
            //this case should never be reached.
            System.out.println("Something went wrong. ERROR #1");
            return null;
        }
    }
}


/** 
 * This class represents a binary search tree dictionary.
 * The binary search tree is not always balanced.
 * All children that are LEFT of the parent are LESS than the parent.
 * All children that are RIGHT of the parent are MORE than the parent.
 * 
 * @author Ryan Seys
 *
 * @param <E> 
 * @param <K>
 */
public class BSTDictionary<E, K extends Sortable> implements Dictionary<E, K> {
	BSTNode<E, K> root; // the root of the binary search tree.

	/**
	 * Constructor for the binary search tree.
	 */
	public BSTDictionary() {
		this(null);
	}
	/**
	 * Constructor with a non-default root node.
	 * @param root the non-default root value.
	 */
	public BSTDictionary(BSTNode<E, K> root) {
		this.root = root;
	}

	/**
	 * Returns the element of the node with the key value
	 * of the specified value.
	 */
	public E search(K key) {
		BSTNode<E, K> nodeFound;
		nodeFound = searchNode(key);
		if(nodeFound == null) {
			return null;
		}
		else return searchNode(key).getElement();
	}

	/**
	 * Search for an entry with key KEY and return the node object
	 * returns 
	 * @param key the key value of the node we are looking for.
	 * @return null if no such key was found, the node it was found in if it was found.
	 */
	public BSTNode<E, K> searchNode(K key) {
		if(key == null) {
			return null;
		}
		if(root == null) {
			return null;
		}
		//if the keys are equal
		if(key.compareTo(root.getKey()) == 0) {
			return root;
		}
		//if this key is less than the root's key
		else if(key.compareTo(root.getKey()) < 0) {
			return searchBelow(root.getLeft(), key);
		}
		//if this key is greater than the root's key
		else if(key.compareTo(root.getKey()) > 0) {
			return searchBelow(root.getRight(), key);
		}
		else {
			//this case should never be reached.
			System.out.println("Something went wrong. ERROR #1");
			return null;
		}
	}

	/**
	 * A recursive solution to finding a specific key in the binary tree.
	 * 
	 * @param node the node we wish to start searching below from.
	 * @param key the key value of the node we are looking for.
	 * @return the node which has the key value we are looking for.
	 */
	public BSTNode<E,K> searchBelow(BSTNode<E, K> node, K key) {
		if(node == null) {
			return null;
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
	 * Insert a key-value pair into the binary search tree.
	 * 
	 */
	public void insert(K key, E element) {
		// there are no items yet in the binary search tree.
		if(root == null) {
			root = new BSTNode<E, K>(key, element, null, null);
		}
		// there are items in the binary search tree
		// so we must find where to put the item. (by key)
		else {
			//System.out.println("Inserting... key: " + key + " element: " + element);
			insertBelow(root, key, element);
		}
	}

	/**
	 * A recursive solution to inserting a node below a specific node.
	 * 
	 * @param node the node we wish to insert our new node underneath
	 * @param key the key value of the new node to be inserted
	 * @param element the element value of the node to be inserted
	 */
	public void insertBelow(BSTNode<E, K> node, K key, E element) {
		//if they are equal
		if(key.compareTo(node.getKey()) == 0) {
			//cannot have duplicates
		}
		//else if key is greater than the node's key
		else if(key.compareTo(node.getKey()) > 0) {
			//greater goes to the right
			if(node.getRight() == null) {
				node.setRight(new BSTNode<E, K>(key, element, null, null));
			}
			else {
				insertBelow(node.getRight(), key, element);
			}
		}
		//else if key is less than the node's key
		else if(key.compareTo(node.getKey()) < 0) {
			//less goes to the left
			if(node.getLeft() == null) {
				node.setLeft(new BSTNode<E, K>(key, element, null, null));
			}
			else insertBelow(node.getLeft(), key, element);
		}
		else {
			// this case should never be reached.
			System.out.println("Something seriously went wrong. ERROR #2");
		}
	}

	/**
	 * Delete an entry with key passed as the parameter.
	 * @param key The key we wish to delete from this tree.
	 */
	public void delete(K key) {
		//if the keys are equal to root
		//if(key.compareTo(root.getKey()) == 0) {
		//System.out.println("Want to delete key: " + key + ". Root's key is: " + root.getKey());
		//root = null;
		//}
		//System.out.println("deleting " + key);
		this.root = deleteRecursive(root, key);
		//System.out.println("Done deleting " + key);
	}

	/**
	 * @param node
	 * @param key
	 * @return
	 */
	public BSTNode<E, K> deleteRecursive(BSTNode<E,K> node, K key) {
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
			else if((node.getLeft() != null) && (node.getRight() != null)) {
				BSTNode<E, K> replacementNode = findMin(node.getRight());
				BSTNode<E, K> backupLeft = node.getLeft();
				replacementNode.setRight(deleteDoubleNode(node.getRight()));
				replacementNode.setLeft(backupLeft);
				return replacementNode;
			}
			else {
				return node;
			}
		}
		else if(key.compareTo(node.getKey()) < 0) {
			if(node.getLeft() != null) {
				node.left = deleteRecursive(node.getLeft(), key);
				return node;
			}
		}
		else {
			if(node.getRight() != null) {
				node.right = deleteRecursive(node.getRight(), key);
				return node;
			}
		}
		return node;
	}

	/**
	 * 
	 * @param node
	 * @return
	 */
	public BSTNode<E, K> deleteDoubleNode(BSTNode<E, K> node) {
		if(node.getLeft() == null) {
			//at the bottom
			return node.getRight();
		}
		else {
			node.setLeft(deleteDoubleNode(node.getLeft()));
		}
		return node;

	}

	/**
	 * This was an intermediate method written to perform a different
	 * way of moving the nodes around when deleting a node with 2 children.
	 * The result is a deeper tree rather than a more shallow tree. (BAD)
	 * It is left here for informational purposes only, and is not to be used.
	 * 
	 * @param right
	 * @param left
	 * @return
	 */
	public BSTNode<E, K> attachToMin(BSTNode<E, K> right, BSTNode<E, K> left) {
		if(right.getLeft() == null) {
			right.setLeft(left);
			return right;
		}
		else {
			right.setLeft(attachToMin(right.getLeft(), left));
			return right;
		}
	}

	/**
	 * Finds the minimum value from a particular node.
	 * This simply involves traversing the left-most nodes
	 * all the way down the tree until the left-most node is null.
	 * 
	 * @param node the node to start finding the min from
	 * @return the node which contains the minimum value (the left most leaf)
	 */
	public BSTNode<E, K> findMin(BSTNode<E, K> node) {
		while(node.getLeft() != null) {
			node = node.getLeft();
		}
		return node;
	}

	/**
	 * Print the Dictionary in sorted order (as determined by the keys)
	 * to print in sorted order, we traverse and print the tree "inorder".
	 */
	public void printTree() {
		System.out.println("Printing...");
		inorder(root);
	}

	/**	
	 * This code is a variation of the code found in the textbook.
	 * It was re-modified to suit the purposes of this assignment.
	 * @param node
	 */
	public void inorder(BSTNode<E,K> node) {
		if(node != null) {
			inorder(node.getLeft());
			System.out.println("key: " + node.getKey().toString() + " element: " + node.getElement().toString());
			inorder(node.getRight());
		}
	}

	/** 
	 * Recursive counter to count the depth of the tree's node.
	 * 
	 * @param node the node we are starting to calculate the depth from
	 * @param current_depth the current depth that has been calculated previously.
	 * @return the depth of the tree in a given particular node.
	 */
	int postorder_depth(BSTNode<E,K> node, int current_depth) {
		if(node != null) {
			return Math.max(postorder_depth(node.getLeft(), current_depth+1), postorder_depth(node.getRight(), current_depth+1));

		}
		else return current_depth;
	}

	/** 
	 * Returns the depth of the root of this tree.
	 * @return the depth of the tree in integer form.
	 */
	public int depth() {
		return postorder_depth(root, 0);
	}
}

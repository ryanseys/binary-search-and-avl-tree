
/** 
 * This class represents a binary search tree dictionary.
 * The binary search tree is always balanced.
 * All children that are LEFT of the parent are LESS than the parent.
 * All children that are RIGHT of the parent are MORE than the parent.
 * 
 * @author Ryan Seys
 */
public class BSTDictionary<E, K extends Sortable> implements Dictionary<E, K> {
	BSTNode<E, K> root;
	//constructor
	public BSTDictionary() {
		this(null);
	}
	//constructor with a non-default root node.
	public BSTDictionary(BSTNode<E, K> root) {
		this.root = root;
	}
	
	//returns the element of the node with the key value
	//of the specified value.
	public E search(K key) {
		BSTNode<E, K> nodeFound;
		nodeFound = searchNode(key);
		if(nodeFound == null) {
			return null;
		}
		else return searchNode(key).getElement();
	}
	
	// search for an entry with key KEY and return the node object
	// returns null if no such key was found.
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
	
	//A recursive solution to finding a specific key in the binary tree.
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

	// insert a key-value pair into the dictionary
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
	
	//A recursive solution to inserting a node below a specific node.
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

	// delete an entry with key KEY
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
	
	public BSTNode<E, K> findMin(BSTNode<E, K> node) {
		while(node.getLeft() != null) {
			node = node.getLeft();
		}
		return node;
	}

	// print the Dictionary in sorted order (as determined by the keys)
	// to print in sorted order, we traverse and print the tree "inorder".
	public void printTree() {
		System.out.println("Printing...");
		inorder(root);
	}
	
	//This code is a variation of the code found in the textbook.
	//It was re-modified to suit the purposes of this assignment.
	public void inorder(BSTNode<E,K> node) {
		if(node != null) {
			inorder(node.getLeft());
			System.out.println("key: " + node.getKey().toString() + " element: " + node.getElement().toString());
			inorder(node.getRight());
		}
	}

	//I don't even know if this works.
	int postorder_depth(BSTNode<E,K> node, int current_depth) {
		if(node != null) {
			return Math.max(postorder_depth(node.getLeft(), current_depth+1), postorder_depth(node.getRight(), current_depth+1));
			
		}
		else return current_depth;
	}
	// return the max depth of the underlying tree
	public int depth() {
		return postorder_depth(root, 0);
	}
}

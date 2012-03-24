
/** Represents a AVL Tree. It is self-balancing.
 * 
 * @author Ryan Seys
 */
public class AVLDictionary<E, K extends Sortable> implements Dictionary<E, K> {
		AVLNode<E, K> root; // the root of the AVL Tree.
		
		private static final int MORELEFT = 1;
		private static final int MORERIGHT = 3;
		private static final int BALANCED = 2;
	
		/**
		 * Constructor for the AVL Tree.
		 */
		public AVLDictionary() {
			this(null);
		}
		
		/**
		 * Constructor with a non-default root node.
		 * @param root the non-default root value.
		 */
		public AVLDictionary(AVLNode<E, K> root) {
			this.root = root;
		}
		
		// search for an entry with key KEY and return the object
		public E search(K key) {
			return null;
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
		public void insertBelow(AVLNode<E, K> node, K key, E element) {
			//if they are equal
			if(key.compareTo(node.getKey()) == 0) {
				//cannot have duplicates
			}
			//else if key is greater than the node's key
			else if(key.compareTo(node.getKey()) > 0) {
				//greater goes to the right
				if(node.getRight() == null) {
					node.setRight(new AVLNode<E, K>(key, element, null, null, BALANCED));
					node.setBalance(node.getBalance()+1);
				}
				else {
					insertBelow(node.getRight(), key, element);
					node.setBalance(node.getBalance()+1);
				}
			}
			//else if key is less than the node's key
			else if(key.compareTo(node.getKey()) < 0) {
				//less goes to the left
				if(node.getLeft() == null) {
					node.setLeft(new AVLNode<E, K>(key, element, null, null, BALANCED));
					node.setBalance(node.getBalance()-1);
				}
				else {
					insertBelow(node.getLeft(), key, element);
					node.setBalance(node.getBalance()-1);
				}
			}
			else {
				// this case should never be reached.
				System.out.println("Something seriously went wrong. ERROR #2");
			}
		}

		// delete an entry with key KEY
		public void delete(K key) {
			
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
		 * This is a recursive solution to printing out the tree
		 * in the order of "inorder" processing.
		 * @param node the node we are traversing from
		 */
		public void inorder(AVLNode<E,K> node) {
			if(node != null) {
				inorder(node.getLeft()); //get the left keys first
				System.out.println("key: " + node.getKey().toString() + " element: " + node.getElement().toString() + " balance: " + node.getBalance());
				inorder(node.getRight()); //thing get the right keys.
			}
		}

		/** 
		 * Returns the depth of the root of this tree.
		 * @return the depth of the tree in integer form.
		 */
		public int depth() {
			return postorder_depth(root, 0);
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
}

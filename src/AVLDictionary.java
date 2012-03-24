
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
		 * Insert a key-value pair into the binary search tree.
		 * Uses the recursive solution as a helper method.
		 */
		public void insert(K key, E element) {
			int var;
			// there are no items yet in the AVL tree
			if(root == null) {
				root = new AVLNode<E, K>(key, element, null, null, BALANCED);
				System.out.println("root " + root.getKey() + " set: it's balance is: " + root.getBalance());
			}
			// there are items in the AVL tree
			// so we must find where to put the item. (by key)
			else {
				//System.out.println("Inserting... key: " + key + " element: " + element);
				var = insertBelow(root, key, element);
				root.setBalance(root.getBalance()+var);
				System.out.println(key + " inserted: root balance is: " + root.getBalance());
			}
		}

		/**
		 * A recursive solution to inserting a node below a specific node.
		 * 
		 * @param node the node we wish to insert our new node underneath
		 * @param key the key value of the new node to be inserted
		 * @param element the element value of the node to be inserted
		 */
		public int insertBelow(AVLNode<E, K> node, K key, E element) {
			int var;
			//if they are equal
			if(key.compareTo(node.getKey()) == 0) {
				return 0; //cannot have duplicates
			}
			
			//else if key is greater than the node's key
			else if(key.compareTo(node.getKey()) > 0) {
				//greater goes to the right
				System.out.println(key + " inserting to the right of " + node.getKey());
				if(node.getRight() == null) {
					node.setRight(new AVLNode<E, K>(key, element, null, null, 2));
					if(node.getBalance() == MORELEFT) {
						node.setBalance(BALANCED);
						System.out.println(node.getKey() + " 2balance = " + node.getBalance());
						return 0; //increase balance by 1 on the previous node
					}
					else {
						node.setBalance(MORERIGHT);
						System.out.println(node.getKey() + " 3balance = " + node.getBalance());
						return 0; //increase balance by 1 on the previous node
					}
				}
				else {
					var = insertBelow(node.getRight(), key, element);
					node.setBalance(node.getBalance()+var);
					System.out.println(node.getRight().getKey() + " 4balance now = " + node.getRight().getBalance());
					//System.out.println(key + " inserting to the right of " + node.getRight().getKey());
					return 1; //increase balance by 1 on the previous node
				}
			}
			
			//else if key is less than the node's key
			else if(key.compareTo(node.getKey()) < 0) {
				//less goes to the left
				System.out.println(key + " inserting to the left of " + node.getKey());
				if(node.getLeft() == null) {
					node.setLeft(new AVLNode<E, K>(key, element, null, null, 2));
					if(node.getBalance() == MORERIGHT) {
						node.setBalance(BALANCED);
						System.out.println(node.getKey() + " 5balance = " + node.getBalance());
						return 0;
					}
					else {
						node.setBalance(MORELEFT);
						System.out.println(node.getKey() + " 6balance = " + node.getBalance());
						return 0;
					}
				}
				else {
					var = insertBelow(node.getLeft(), key, element);
					node.setBalance(node.getBalance()+var);
					System.out.println(node.getLeft().getKey() + " 7balance now = " + node.getLeft().getBalance());
					//System.out.println(key + " inserting to the left of " + node.getLeft().getKey());
					return -1; //reduce balance by 1 on the previous node
				}
			}
			else {
				// this case should never be reached.
				System.out.println("Something seriously went wrong. ERROR #2");
				return -1;
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

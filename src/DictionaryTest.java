// The "DictionaryTest" class.
// This class tests two implementations of the Dictionary 
// interface by inserting 676 different entries, removing 
// half of them, and inserting 156 more entries. It also
// prints the initial dictionaries (i.e., the ones after
// inserting the first 676 entries) and searches for 6 
// randomly chosen entries in both dictionaries. Obviously,
// the search result should be the same for both dictionaries.
public class DictionaryTest {
	protected static String[] entries = new String[26 * 26];

	protected static void fill() {
		// Insert 26 * 26 entries
		for (int i = 0; i < 26; i++)
			for (int j = 0; j < 26; j++) {
				StringBuffer s = new StringBuffer();
				s.append((char) ((int) 'A' + i));
				s.append((char) ((int) 'A' + j));
				entries[i * 26 + j] = s.toString();
			}
	} // fill method

	public static void main(String[] args) {
		BSTDictionary<String, SortableString> dict1 = new BSTDictionary<String, SortableString>();
		AVLDictionary<String, SortableString> dict2 = new AVLDictionary<String, SortableString>();

		//String[] st = {"AA","BB","AC","AG","AI","AJ","BE","BI","AR","AS","AV","BJ","AK","AM","AP","AQ","AZ","BA"};
		//String[] st = {"AA","BB","AC","AG","AI","AJ"};
		String[] st = {"AA", "BB", "CC", "BC", "ZZ", "DZ", "OO"};
		for(int w = 0; w<st.length;w++){
			dict1.insert(new SortableString(st[w]), st[w]);
			dict2.insert(new SortableString(st[w]), st[w]);
			
		}



System.out.println("\nIn-order traversal of BST nodes after insertion: ");
		dict1.printTree();

System.out.println("\nIn-order traversal of AVL nodes after insertion: ");
		dict2.printTree();
		
		// print the depth
		
		System.out.println("The initial BST tree has a maxi nmum depth of "
				+ dict1.depth());
		System.out.println("The initial AVL tree has a maximum depth of "
				+ dict2.depth());
	} // main method
} /* DictionaryTest class */

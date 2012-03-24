// The "SortableString" class.
// A "String-holding" class which implements the Sortable interface.
class SortableString implements Sortable {
	protected String s;

	public SortableString(String x) {
		s = x;
	} // SortableString constructor

	public int compareTo(Sortable other) {
		return s.compareTo(((SortableString) other).s);
	} // compareTo method

	public String toString() {
		return s;
	} // toString method
} /* SortableString class */
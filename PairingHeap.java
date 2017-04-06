import java.util.HashMap;
import java.util.Map;

public class PairingHeap extends Heap {
	private PairNode root;
	private PairNode[] treeArray;
	private int heapSize;

	/* Constructor */
	public PairingHeap(HashMap<String, Integer> freq_table) {
		treeArray = new PairNode[freq_table.size()];
		root = null;
		heapSize = 0;
		for (Map.Entry<String, Integer> entry : freq_table.entrySet()) {
			Node temp = new Node(entry.getKey(), entry.getValue());
			add(temp);
		}
	}

	public int getSize() {
		return heapSize;
	}

	/* Check if heap is empty */
	public boolean isEmpty() {
		return root == null;
	}

	/* Make heap logically empty */
	public void makeEmpty() {
		root = null;
	}

	/* Function to add data */
	public void add(Node node) {
		heapSize++;
		PairNode newNode = new PairNode(node);
		if (root == null)
			root = newNode;
		else
			root = meld(root, newNode);
	}

	private PairNode meld(PairNode first, PairNode second) {
		if (second == null)
			return first;

		/*
		 * Compare-Link Operation
		 * Compare roots. 
		 * Tree with smaller root becomes leftmost subtree.
		 */
		if (second.getFreq() < first.getFreq()) {
			second.leftSibling = first.leftSibling;
			first.leftSibling = second;
			first.rightSibling = second.child;
			if (first.rightSibling != null)
				first.rightSibling.leftSibling = first;
			second.child = first;
			return second;
		} else {
			second.leftSibling = first;
			first.rightSibling = second.rightSibling;
			if (first.rightSibling != null)
				first.rightSibling.leftSibling = first;
			second.rightSibling = first.child;
			if (second.rightSibling != null)
				second.rightSibling.leftSibling = second;
			first.child = second;
			return first;
		}
	}

	private PairNode twoPassMeld(PairNode firstSibling) {
		if (firstSibling.rightSibling == null)
			return firstSibling;
		/* Store the subtrees in an array */
		int numSiblings = 0;
		for (; firstSibling != null; numSiblings++) {
			treeArray = doubleIfFull(treeArray, numSiblings);
			treeArray[numSiblings] = firstSibling;
			/* break links */
			firstSibling.leftSibling.rightSibling = null;
			firstSibling = firstSibling.rightSibling;
		}
		treeArray = doubleIfFull(treeArray, numSiblings);
		treeArray[numSiblings] = null;
		/*
		 * Pass 1. Examine subtrees from left to right. Meld pairs of subtrees,
		 * reducing the number of subtrees to half the original number. If #
		 * subtrees was odd, meld remaining original subtree with last newly
		 * generated subtree.
		 */
		int i = 0;
		for (; i + 1 < numSiblings; i += 2)
			treeArray[i] = meld(treeArray[i], treeArray[i + 1]);
		int j = i - 2;
		/* j has the result of last meld */
		/* If an odd number of trees, get the last one */
		if (j == numSiblings - 3)
			treeArray[j] = meld(treeArray[j], treeArray[j + 2]);
		/*
		 * Pass 2. Start with rightmost subtree of Pass 1. Call this the working
		 * tree. Meld remaining subtrees, one at a time, from right to left,
		 * into the working tree.
		 */
		for (; j >= 2; j -= 2)
			treeArray[j - 2] = meld(treeArray[j - 2], treeArray[j]);
		return treeArray[0];
	}

	private PairNode[] doubleIfFull(PairNode[] array, int index) {
		if (index == array.length) {
			PairNode[] oldArray = array;
			array = new PairNode[index * 2];
			for (int i = 0; i < index; i++)
				array[i] = oldArray[i];
		}
		return array;
	}

	/* Delete min element */
	public Node pop() {
		if (isEmpty())
			return null;
		heapSize--;
		PairNode x = root;
		if (root.child == null)
			root = null;
		else
			root = twoPassMeld(root.child);
		return x.getNode();
	}

	/* Get min element */
	public PairNode peek() {
		if (isEmpty())
			return null;
		return root;
	}
}

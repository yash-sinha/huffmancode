import java.util.ArrayList;

//Java program to demonstrate insert operation in binary search tree
class HuffmanTrie {

	/* Class containing left and right child of current node and key value */
	class BNode {
		String key;
		BNode left, right;

		public BNode(String item) {
			resString = new ArrayList<String>();
			key = item;
			left = right = null;
		}
	}

	// Root of BST
	BNode root, topRoot;
	ArrayList<String> resString;
	static int pos;

	// Constructor
	HuffmanTrie() {
		root = null;
	}

	void insert(String key) {
		root = insertRec(root, key, 0);
	}

	BNode insertRec(BNode root, String key, int level) {

		/* If the tree is empty, return a new node */
		if (level == key.length() - 1) {
			BNode temp = new BNode(key);
			if (key.charAt(level) == '1') {
				root.right = temp;
			} else
				root.left = temp;
			root = temp;
			return root;
		}

		if (key.charAt(level) == '1') {
			BNode temp = new BNode("$");
			if (root == null && level != key.length() - 1) {
				root = new BNode("$");
			}
			if (root.right == null)
				root.right = temp;
			else
				temp = root.right;
			insertRec(temp, key, level + 1);
		} else if (key.charAt(level) == '0') {
			BNode temp = new BNode("$");
			if (root == null && level != key.length() - 1) {
				root = new BNode("$");
			}
			if (root.left == null)
				root.left = temp;
			else
				temp = root.left;
			insertRec(temp, key, level + 1);
		}
		return root;
	}

	public int traverseTree(String key) {
		pos = 0;
		int lastSuccessFullTrace = 0;
		while (pos != key.length()) {
			String temp = traverseTreeRec(root, key);
			if (temp != null) {
				resString.add(temp);
				lastSuccessFullTrace = pos;
			}
		}
		return lastSuccessFullTrace;
	}

	String traverseTreeRec(BNode root, String key) {
		String res;
		if (isLeaf(root)) {
			return root.key;
		}
		if(key.length()==pos){
			return null;
		}
		

		if (key.charAt(pos) == '1') {
			pos++;
			res = traverseTreeRec(root.right, key);
		} else {
			pos++;
			res = traverseTreeRec(root.left, key);
		}
		return res;
	}

	public boolean isLeaf(BNode node) {
		return (!node.key.equals("$"));
	}

	void inorder() {
		inorderRec(root);
	}

	void inorderRec(BNode root) {
		if (root != null) {
			inorderRec(root.left);
			System.out.print(root.key + "\t");
			inorderRec(root.right);
		}
	}
}

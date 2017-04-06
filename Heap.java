public abstract class Heap {
	public abstract Node peek();

	public abstract int getSize();

	public abstract Node pop();

	public abstract void add(Node internal);

	public abstract boolean isEmpty();

	public Node getHuffmanTree(BinaryHeap resHeap) {
		/*
		 * Start with a collection of external nodes, each with one of the given weights.
		 * Each external node defines a different tree. 
		 * Reduce number of trees by 1. 
		 * Select 2 trees with minimum weight. 
		 * Combine them by making them children of a new root node. 
		 * The weight of the new tree is the sum of the weights of the individual trees. 
		 * Add new tree to tree collection. 
		 * Repeat reduce step until only 1 tree remains. 
		 */
		while (getSize() > 1) {
			Node left = pop();
			Node right = pop();
			int internalFreq = left.getFreq() + right.getFreq();
			Node internal = new Node("$" + internalFreq, internalFreq);
			internal.setLeft(left);
			internal.setRight(right);
			add(internal);
		}

		return pop();
	}

}

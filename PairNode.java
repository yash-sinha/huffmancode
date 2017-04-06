

public class PairNode extends Node{
	PairNode child;
	PairNode rightSibling;
	PairNode leftSibling;
	Node node;

	public PairNode getchild() {
		return child;
	}

	public void setchild(PairNode child) {
		this.child = child;
	}

	public PairNode getNextSibling() {
		return rightSibling;
	}

	public void setNextSibling(PairNode rightSibling) {
		this.rightSibling = rightSibling;
	}

	public PairNode getPrev() {
		return leftSibling;
	}

	public void setPrev(PairNode leftSibling) {
		this.leftSibling = leftSibling;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	/* Constructor */
	public PairNode(Node node) {
		super(node.getKey(),node.getFreq());
		this.node = node;
		child = null;
		rightSibling = null;
		leftSibling = null;
	}
}

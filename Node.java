public class Node {
	private Node parent;
    private Node left;
    private Node right;
    private String key = null;
    private int freq = -1;
    private boolean isLeaf = false;
    
    public Node(String i, int freq) {  
            this.key = i;
            this.freq = freq;
    }
    
    public Node getParent() {
            return parent;
    }

    public void setParent(Node parent) {
            this.parent = parent;
    }

    public Node getLeft() {
            return left;
    }

    public void setLeft(Node left) {
            this.left = left;
    }

    public Node getRight() {
            return right;
    }

    public void setRight(Node right) {
            this.right = right;
    }

    public String getKey() {
            return key;
    }

    public void setKey(String key) {
            this.key = key;
    }

    public void setFreq(int freq) {
            this.freq = freq;
    }

    public int getFreq() {
            return freq;
    }
    
    public void setToLeaf() {
            isLeaf = true;
    }
    
    public boolean isLeaf() {
            return isLeaf;
    }

	public Node getNode() {
		// TODO Auto-generated method stub
		return null;
	}
}

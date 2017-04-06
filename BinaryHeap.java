import java.util.HashMap;
import java.util.Map;

public class BinaryHeap extends Heap {
	private Node[] Heap;
	private int size;
	private int capacity;

	public int getCapacity() {
		return capacity;
	}

	private static final int FRONT = 1;

	public BinaryHeap(HashMap<String, Integer> freq_table) {
		this.Heap = new Node[freq_table.size() + 1];
		Heap[FRONT] = null;
		this.size = 0;
		capacity = freq_table.size();
		for (Map.Entry<String, Integer> entry : freq_table.entrySet()) {
			Node temp = new Node(entry.getKey(), entry.getValue());
			add(temp);
		}
		minHeap();
	}

	public BinaryHeap(int size) {
		this.Heap = new Node[size + 1];
		Heap[FRONT] = null;
		this.size = 0;
		capacity = size;
	}

	public int getSize() {
		return size;
	}

	private int parent(int pos) {
		return pos / 2;
	}

	private int leftChild(int pos) {
		return (2 * pos);
	}

	private int rightChild(int pos) {
		return (2 * pos) + 1;
	}

	private void swap(int fpos, int spos) {
		Node tmp;
		tmp = Heap[fpos];
		Heap[fpos] = Heap[spos];
		Heap[spos] = tmp;
	}

	public void add(Node element) {
		if (size < capacity) {
			Heap[++size] = element;
			int current = size;
			while (isLeaf(current) && isLeaf(parent(current))
					&& Heap[parent(current)].getFreq() > Heap[current].getFreq()) {
				swap(parent(current), current);
				current = current / 2;
			}
		} else {
			System.out.println("cannot add : no space left");
		}
	}

	public Node peek() {
		if (size != 0)
			return Heap[1];
		return null;
	}

	public Node pop() {
		Node result = Heap[FRONT];
		Heap[FRONT] = Heap[size];
		Heap[size] = null;
		size--;
		minHeapify(FRONT);
		return result;
	}

	public void minHeap(int pos) {
		for (int i = parent(pos); i >= FRONT; i--) {
			minHeapify(i);
		}
	}

	public void minHeap() {
		for (int pos = (size / 2); pos >= 1; pos--) {
			minHeapify(pos);
		}
	}

	public void minHeapify(int pos) {
		int leftChildIdx = leftChild(pos);
		int rightChildIdx = rightChild(pos);
		int smallestIdx = leftChildIdx;

		if (isLeaf(rightChildIdx)) {
			if (Heap[rightChildIdx].getFreq() < Heap[leftChildIdx].getFreq()) {
				smallestIdx = rightChildIdx;
			}
		}

		if (isLeaf(smallestIdx) && Heap[smallestIdx].getFreq() < Heap[pos].getFreq()) {
			swap(smallestIdx, pos);
			minHeapify(smallestIdx);
		}
	}

	private boolean isLeaf(int pos) {
		return pos >= FRONT && pos <= size;
	}

	@Override
	public boolean isEmpty() {
		return getSize() == 0;
	}
}

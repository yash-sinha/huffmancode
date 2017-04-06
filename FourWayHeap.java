
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class FourWayHeap extends Heap{
	  /** The number of children each node has **/
    private int d=4;
    private int heapSize;
    private int capacity;
    private Node[] heap;
    static int FRONT = 3;
    
 
    /** Constructor **/    
    public FourWayHeap(HashMap<String, Integer> freq_table)
    {
        heapSize = FRONT;
        this.capacity=freq_table.size();
        heap = new Node[capacity+3];
        for (Map.Entry<String, Integer> entry : freq_table.entrySet()) {
			Node temp = new Node(entry.getKey(), entry.getValue());
			add(temp);
		}
    }
 
    /** Function to check if heap is empty **/
    public boolean isEmpty()
    {
        return heapSize-FRONT == 0;
    }
    public int getSize(){
    	return heapSize-FRONT;
    }
    public int getCapacity(){
    	return capacity;
    }
    
    /** Check if heap is full **/
    public boolean isFull()
    {
        return heapSize - FRONT == heap.length;
    }
 
    /** Clear heap */
    public void clear()
    {
        heapSize = FRONT;
    }
 
    /** Function to  get index parent of i **/
    private int parent(int i) 
    {
        return (i - 1-FRONT)/d + FRONT;
    }
 
    /** Function to get index of k th child of i **/
    private int kthChild(int i, int k) 
    {
        return d * (i-FRONT) + k+FRONT;
    }
 
    /** Function to add element */
    public void add(Node x)
    {
        if (isFull())
            throw new NoSuchElementException("Overflow Exception");
        /** Percolate up **/
        heap[heapSize++] = x;
        heapifyUp(heapSize - 1);
    }
 
    /** Function to find least element **/
    public Node peek()
    {
        if (isEmpty())
            throw new NoSuchElementException("Underflow Exception");           
        return heap[FRONT];
    }
 
    /** Function to delete element at an index **/
    public Node delete(int ind)
    {
        if (isEmpty())
            throw new NoSuchElementException("Underflow Exception");
        Node keyItem = heap[ind];
        heap[ind] = heap[heapSize - 1];
        heapSize--;
        heapifyDown(ind);        
        return keyItem;
    }
 
    /** Function heapifyUp  **/
    private void heapifyUp(int childInd)
    {
    	Node tmp = heap[childInd];    
        while (childInd > FRONT && tmp.getFreq() < heap[parent(childInd)].getFreq())
        {
            heap[childInd] = heap[ parent(childInd) ];
            childInd = parent(childInd);
        }                   
        heap[childInd] = tmp;
    }
 
    /** Function heapifyDown **/
    private void heapifyDown(int ind)
    {
        int child;
        Node tmp = heap[ind];
        while (kthChild(ind, 1) < heapSize)
        {
            child = minChild(ind);
            if (heap[child].getFreq() < tmp.getFreq())
                heap[ind] = heap[child];
            else
                break;
            ind = child;
        }
        heap[ind] = tmp;
    }
 
    /** Function to get smallest child **/
    private int minChild(int ind) 
    {
        int bestChild = kthChild(ind, 1);
        int k = 2;
        int pos = kthChild(ind, k);
        while ((k <= d) && (pos < heapSize)) 
        {
            if (heap[pos].getFreq() < heap[bestChild].getFreq()) 
                bestChild = pos;
            pos = kthChild(ind, ++k);
        }    
        return bestChild;
    }
    
    public Node pop(){
    	Node result = delete(FRONT);		
		return result;
    }
 
}

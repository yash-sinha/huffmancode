import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class encoder {
	static HashMap<String, Integer> freq_table;
	static HashMap<String, String> codeTable = new HashMap<>();
	String inpFile;
	private int treeType; // 1: BinaryHeap 2: 4-way 3: Pairing Heap
	static String outputFileName = "encoded.bin";
	static String codeTableFile = "code_table.txt";

	public encoder(String fileName, int treeType) {
		this.inpFile = fileName;
		this.treeType = treeType;
	}

	public static void main(String[] args) throws Exception {
		String file = "";
		if (args.length == 1) {
			// encoding
			file = args[0];
			encoder encoder = new encoder(file, 2);
			encoder.startEncoding();
		} else if (args.length == 0) {
			file = "sample_input_large.txt";
			encoder encoder1 = new encoder(file, 2);
			encoder1.testMethods();
		} else if (args.length == 2) {
			file = "sample_input_small.txt";
			if (args[0].equals("-test")) {
				encoder encoder = new encoder(file, Integer.parseInt(args[1]));
				encoder.testMethods();
			} else if (args[0].equals("-type")) {
				encoder encoder = new encoder(file, Integer.parseInt(args[1]));
				encoder.startEncoding();
			}
		} else if (args.length == 3) {
			file = args[2];
			if (args[0].equals("-test")) {
				encoder encoder = new encoder(file, Integer.parseInt(args[1]));
				encoder.testMethods();
			} else if (args[0].equals("-type")) {
				encoder encoder = new encoder(file, Integer.parseInt(args[1]));
				encoder.startEncoding();
			} else if (args[0].equals("-testall")) {
				file = args[1];
				encoder encoder1 = new encoder(file, 1);
				encoder encoder2 = new encoder(file, 2);
				encoder encoder3 = new encoder(file, 3);
				encoder1.testMethods();
				encoder2.testMethods();
				encoder3.testMethods();
			}
		} else {
			throw new Exception("Invalid Arguments");
		}
	}

	public void startEncoding() throws Exception {
		// Create huffman tree with input
		long start = System.currentTimeMillis();

		System.out.println("Building Frequency Table..");
		// create frequency table for the input
		freq_table = getFreqTable(inpFile);
		//

		System.out.println("Building Huffman Tree..");
		// // build Huffman tree
		Node node = buildHuffmanTree();
		//

		System.out.println("Generating code table..");
		// // build code table and print to code_table.txt
		buildCodeTable(node, freq_table.size());

		System.out.println("Encoding input file..");
		// writeToFile
		prepareBytes(inpFile);

		freq_table.clear();
		codeTable.clear();
		System.out.println("Total time taken: " + (System.currentTimeMillis() - start) + "ms");
	}

	public Node buildHuffmanTree() {
		Node huffTree = null;
		BinaryHeap heap = new BinaryHeap(freq_table.size());
		switch (treeType) {
		case 1:
			huffTree = (new BinaryHeap(freq_table)).getHuffmanTree(heap);
			break;
		case 2:
			huffTree = (new FourWayHeap(freq_table)).getHuffmanTree(heap);
			break;
		case 3:
			huffTree = (new PairingHeap(freq_table)).getHuffmanTree(heap);
			break;
		default:
			System.out.println("Unknown Tree Type");
			break;
		}
		return huffTree;

	}

	public Node buildHuffmanTree(int type) {
		Node huffTree = null;
		BinaryHeap heap = new BinaryHeap(freq_table.size());
		switch (type) {
		case 1:
			huffTree = (new BinaryHeap(freq_table)).getHuffmanTree(heap);
			break;
		case 2:
			huffTree = (new FourWayHeap(freq_table)).getHuffmanTree(heap);
			break;
		case 3:
			huffTree = (new PairingHeap(freq_table)).getHuffmanTree(heap);
			break;
		default:
			System.out.println("Unknown Tree Type");
			break;
		}
		return huffTree;

	}

	public HashMap<String, Integer> getFreqTable(String file) throws FileNotFoundException, IOException {
		HashMap<String, Integer> freq_table = new HashMap<>();
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			int lineNo = 1;
			while ((line = br.readLine()) != null) {
				if (!line.trim().equals("")) {
					try {
						if (Integer.parseInt(line.trim()) < 1000000) {
							freq_table.put(line, freq_table.getOrDefault(line, 0) + 1);
						}
						lineNo++;

					} catch (Exception e) {
						System.out.println(lineNo);
					}
				}
			}
		}
		return freq_table;
	}

	public HashMap<String, String> buildCodeTable(Node node, int capacity)
			throws UnsupportedEncodingException, FileNotFoundException, IOException {
		codeTable.clear();
		// treeRoot = heap.extractMin();
		getHeapCodes(node, new int[capacity], 0);
		try (BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream("code_table.txt"), "utf-8"))) {
			for (Map.Entry<String, String> entry : codeTable.entrySet()) {
				writer.write(entry.getKey() + " " + entry.getValue());
				writer.newLine();
			}
		}
		return codeTable;
	}

	public void getHeapCodes(Node root, int arr[], int top) {
		// Assign 0 to left edge and recur
		if (root.getLeft() != null) {
			arr[top] = 0;
			getHeapCodes(root.getLeft(), arr, top + 1);
		}

		// Assign 1 to right edge and recur
		if (root.getRight() != null) {
			arr[top] = 1;
			getHeapCodes(root.getRight(), arr, top + 1);
		}

		// If this is a leaf node, then it contains one of the input
		// characters, print the character and its code from arr[]
		if (root.getLeft() == null && root.getRight() == null) {
			codeTable.put(root.getKey(), getCode(arr, top));
		}
	}

	private String getCode(int[] arr, int n) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++)
			sb.append(arr[i]);
		return sb.toString();
	}

	public void prepareBytes(String file) throws Exception {
		StringBuilder byteString = new StringBuilder();
		deleteInpFileIfExist();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		long numBits = 0;
		@SuppressWarnings("unused")
		long totalLength = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (!line.trim().equals("")) {
					String appendStr = codeTable.get(line.trim());
					if (appendStr == null) {
						throw new Exception("Value not found in codetable");
					}
					byteString.append(appendStr);

					while (byteString.length() >= 8) {
						numBits = numBits + 8;
						byte[] byteArray = getByteByString(byteString.substring(0, 8).toString());
						outputStream.write(byteArray);
						byteString = byteString.delete(0, 8);
					}

				}
			}
		}
		byte[] byteArray = outputStream.toByteArray();
		writeToBinaryFile(byteArray, outputFileName);
	}

	public void deleteInpFileIfExist() {
		Path fileToDeletePath = Paths.get(outputFileName);
		try {
			Files.delete(fileToDeletePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// System.out.println("encoded.bin not yet created");
		}
	}

	public static byte[] getByteByString(String binaryString) {
		int splitSize = 8;

		if (binaryString.length() % splitSize == 0) {
			int index = 0;
			int position = 0;

			byte[] resultByteArray = new byte[binaryString.length() / splitSize];
			StringBuilder text = new StringBuilder(binaryString);

			while (index < text.length()) {
				String binaryStringChunk = text.substring(index, Math.min(index + splitSize, text.length()));
				Integer byteAsInt = Integer.parseInt(binaryStringChunk, 2);
				resultByteArray[position] = byteAsInt.byteValue();
				index += splitSize;
				position++;
			}
			return resultByteArray;
		} else {
			System.out.println("Invalid string length");
			return null;
		}
	}

	public void writeToBinaryFile(byte[] aInput, String aOutputFileName) {
		try {
			OutputStream output = null;
			try {
				output = new BufferedOutputStream(new FileOutputStream(aOutputFileName, true));
				output.write(aInput);
			} finally {
				output.close();
				aInput = null;
			}
		} catch (FileNotFoundException ex) {
			System.out.println("File not found.");
		} catch (IOException ex) {
			System.out.println(ex);
		}
	}

	public void testMethods() throws FileNotFoundException, IOException {
		freq_table = getFreqTable(inpFile);
		long start = System.currentTimeMillis();
		double time;
		switch (treeType) {
		case 1:
			for (int i = 0; i < 20; i++) {
				buildHuffmanTree(1);
			}
			break;
		case 2:
			start = System.currentTimeMillis();
			for (int i = 0; i < 20; i++) {
				buildHuffmanTree(2);
			}
			break;
		case 3:
			start = System.currentTimeMillis();
			for (int i = 0; i < 20; i++) {
				buildHuffmanTree(3);
			}
			break;
		}
		time = System.currentTimeMillis() - start;
		System.out.println(time / 20);
	}
}

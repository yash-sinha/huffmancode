import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class decoder {
	String encodedFile;
	String codeTableFile;
	
	public decoder(String encodedFile, String codeTableFile) {
		this.encodedFile = encodedFile;
		this.codeTableFile = codeTableFile;
	}
	
	public static void main(String args[]) throws Exception{
		String encodedFile="";
		String codeFile="";
		if (args.length == 2) {
			// encoding
			encodedFile = args[0];
			codeFile = args[1];
			decoder decoder = new decoder(encodedFile,codeFile);
			decoder.startDecoding();
		} 
		else if (args.length == 0){
			encodedFile = "encoded.bin";
			codeFile = "code_table.txt";
			decoder decoder = new decoder(encodedFile,codeFile);
			decoder.startDecoding();
		}else{
			throw new Exception("Invalid Arguments");
		}
	}
	
	public void startDecoding() throws FileNotFoundException, IOException {
		long start;
		start = System.currentTimeMillis();
		
		System.out.println("Preparing Huffman Tree..");
		// preapre codetable from code_table.txt
		HashMap<String, String> inpCodeMap = prepareCodeHashMap();

		// create huffman tree from codetable
		HuffmanTrie bst = prepareHuffmanTree(inpCodeMap);

		System.out.println("Preparing Byte Array..");
		// Prepare Byte array
		prepareByteArray(bst, inpCodeMap);

		//traverseTreeBST(bst, res, inpCodeMap);
		
		System.out.println("Total time taken: "+ (System.currentTimeMillis()-start)+ "ms");

	}

	public boolean prepareByteArray(HuffmanTrie bst, HashMap<String, String> inpCodeMap) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		byte[] res = read(encodedFile);
		StringBuffer sb = new StringBuffer();
		int resLength = res.length;
		for (byte b : res) {
			sb.append(Integer.toBinaryString(b & 255 | 256).substring(1));
			if(sb.length()>=resLength*8/100){
				int pos = traverseTreeBST(bst, sb.toString(), inpCodeMap);
				sb.delete(0, pos);
			}
		}
		if(sb.length()!=0){
			traverseTreeBST(bst, sb.toString(), inpCodeMap);
		}
		System.out.println("Writing to file..");
		createAndWriteDecoded(bst.resString, inpCodeMap);
		return true;
	}

	public HashMap<String, String> prepareCodeHashMap() throws FileNotFoundException, IOException {
		HashMap<String, String> inpCodeMap = new HashMap<>();
		try (BufferedReader br = new BufferedReader(new FileReader(codeTableFile))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (!line.trim().equals(""))
					inpCodeMap.put(line.split(" ")[1], line.split(" ")[0]);
			}
		}
		return inpCodeMap;
	}

	public HuffmanTrie prepareHuffmanTree(HashMap<String, String> inpCodeMap) {
		HuffmanTrie bst = new HuffmanTrie();
		for (Map.Entry<String, String> entry : inpCodeMap.entrySet()) {
			bst.insert(entry.getKey());
		}
		return bst;
	}

	public int traverseTreeBST(HuffmanTrie bst, String res, HashMap<String, String> inpCodeMap)
			throws FileNotFoundException, IOException {
		int pos = bst.traverseTree(res);
		return pos;
	}
	
	public void createAndWriteDecoded(ArrayList<String> resList, HashMap<String, String> inpCodeMap) throws UnsupportedEncodingException, FileNotFoundException, IOException{
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("decoded.txt"), "utf-8"))) {
			for (String s : resList) {
				writer.write(inpCodeMap.get(s));
				writer.newLine();
			}
		}
	}

	public byte[] read(String aInputFileName) {
		File file = new File(aInputFileName);
		byte[] result = new byte[(int) file.length()];
		try {
			InputStream input = null;
			try {
				int totalBytesRead = 0;
				input = new BufferedInputStream(new FileInputStream(file));
				while (totalBytesRead < result.length) {
					int bytesRemaining = result.length - totalBytesRead;
					// input.read() returns -1, 0, or more :
					int bytesRead = input.read(result, totalBytesRead, bytesRemaining);
					if (bytesRead > 0) {
						totalBytesRead = totalBytesRead + bytesRead;
					}
				}
			} finally {
				input.close();
			}
		} catch (FileNotFoundException ex) {
			System.out.println("File not found.");
		} catch (IOException ex) {
			System.out.println(ex);
		}
		return result;
	}
}

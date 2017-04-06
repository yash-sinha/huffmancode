Huffman coding is a lossless data compression algorithm. The idea is to assign variable-length codes to input characters where the lengths are based on the frequencies of corresponding characters. The most frequent character gets the smallest code and the least frequent character gets the largest code. The Huffman Tree built has the minimum WEPL (Weighted External Path Length).
The variable-length codes assigned to input characters are Prefix Codes, means the codes (bit sequences) are assigned in such a way that the code assigned to one character is not prefix of code assigned to any other character. This is how Huffman Coding makes sure that there is no ambiguity when decoding the generated bit stream.

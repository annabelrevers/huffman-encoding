import java.io.*;
import java.util.*;

/**
 * Compresses and decompresses files by implementing Huffman techniques
 *
 * @author Annabel Revers, Ashkaan Mahjoob, Dartmouth CS 10, Spring 2020
 */


public class Huffman {

    public Huffman() {

    }

    public Map<Character,Integer> loadFreqMap(File file) throws FileNotFoundException, IOException {
        Map<Character,Integer> charFreq = new TreeMap<>();

        // Loop over lines
        BufferedReader in = new BufferedReader(new FileReader(file));
        int c;
        while ((c = in.read()) != -1) {
            // Loop over all the characters, adding to map or incrementing count

            if (charFreq.containsKey((char)c)) {
                // Increment the count
                charFreq.put((char)c, charFreq.get((char)c)+1);
            }
            else {
                // Add the new word
                charFreq.put((char)c, 1);
            }
        }

        in.close();

        return charFreq;

    }

    public BinaryTree<Data> makeTree(Map<Character,Integer> myMap) {
        Comparator<BinaryTree<Data>> freqCompare = new TreeComparator();
        PriorityQueue<BinaryTree<Data>> myQueue = new PriorityQueue<>(freqCompare);

        for (Map.Entry<Character,Integer> entry : myMap.entrySet()) {
            char myChar = entry.getKey();
            int myFreq = entry.getValue();

            Data myData = new Data(myChar, myFreq);

            BinaryTree<Data> myTree = new BinaryTree<>(myData);

            myQueue.add(myTree);
        }

        while (myQueue.size() > 1) {
            BinaryTree<Data> T1 = myQueue.remove();
            BinaryTree<Data> T2 = myQueue.remove();

            Data newData = new Data(' ', (T1.getData().getFreq() + T2.getData().getFreq()));

            BinaryTree<Data> T = new BinaryTree<>(newData, T1, T2);

            myQueue.add(T);
        }


        return myQueue.remove();
    }

    public Map<Character,String> makeCodeMapHelper(Map<Character,String> map, String pathSoFar, BinaryTree<Data> freqTree) {

        if (freqTree.isLeaf()) {
            char character = freqTree.getData().getChar();
            map.put(character, pathSoFar);
        }

        // traverse tree, completing map in one traversal
        if (freqTree.hasLeft()) {
            makeCodeMapHelper(map, pathSoFar + 0, freqTree.getLeft());
        }

        if (freqTree.hasRight()) {
            pathSoFar += "1";
            makeCodeMapHelper(map, pathSoFar, freqTree.getRight());

        }

        return map;
    }

    public Map<Character,String> makeCodeMap(String pathSoFar, BinaryTree<Data> freqTree) {
        Map<Character,String> codeMap = new TreeMap<>();
        return makeCodeMapHelper(codeMap, pathSoFar, freqTree);

    }

    public void compress(Map<Character,String> map, String file, String compressedPathName) throws FileNotFoundException, IOException {

        BufferedReader in = new BufferedReader(new FileReader(file));
        BufferedBitWriter bitOutput = new BufferedBitWriter(compressedPathName);

        int c;
        while ((c = in.read()) != -1) {
            Character myChar = (char)c;
            String myStr = map.get(myChar);

            for (int i = 0; i < myStr.length(); i++) {

                if (myStr.charAt(i) == '0') {
                    bitOutput.writeBit(false);
                }

                else if (myStr.charAt(i) == '1') {
                    bitOutput.writeBit(true);

                }

        }



        }

        in.close();
        bitOutput.close();

    }

    public void decompress(BinaryTree<Data> tree, String compressedPathName, String decompressedPathName) throws FileNotFoundException, IOException {

        BufferedBitReader bitInput = new BufferedBitReader(compressedPathName);
        BufferedWriter output = new BufferedWriter(new FileWriter(decompressedPathName));

        BinaryTree<Data> current = tree;

        while (bitInput.hasNext()) {
            boolean bit = bitInput.readBit();

            if (bit) {
                current = current.getRight();
            }

            else {
                current = current.getLeft();
            }

            if (current.isLeaf()) {
                output.write(current.getData().getChar());
                current = tree;

            }



        }

        bitInput.close();
        output.close();



    }

    public static void main(String[] args) {
        // throws FileNotFoundException, IOException

        try {
            // create Huffman object
            Huffman testHuff = new Huffman();

            // get reference to file
            String pathName1 = "ps3/test.txt";
            String pathName2 = "ps3/USConstitution.txt";
            File file1 = new File(pathName1);
            File file2 = new File(pathName2);

            // create frequency map
            Map<Character, Integer> testFreqMap1 = testHuff.loadFreqMap(file1);
            Map<Character, Integer> testFreqMap2 = testHuff.loadFreqMap(file2);
            //System.out.println(testFreqMap);

            // create binary tree
            BinaryTree<Data> testTree1 = testHuff.makeTree(testFreqMap1);
            BinaryTree<Data> testTree2 = testHuff.makeTree(testFreqMap2);
            //System.out.println(testTree);

            // create codes map
            Map<Character, String> testCodeMap1 = testHuff.makeCodeMap("", testTree1);
            Map<Character, String> testCodeMap2 = testHuff.makeCodeMap("", testTree2);
            //System.out.println(testCodeMap);

            // compress and decompress
            testHuff.compress(testCodeMap1, pathName1, "testCom1.txt");
            testHuff.decompress(testTree1, "testCom1.txt", "testDec1.txt");
            testHuff.compress(testCodeMap2, pathName2, "testCom2.txt");
            testHuff.decompress(testTree2, "testCom2.txt", "testDec2.txt");

        }

        catch(Exception e) {
            e.printStackTrace();
        }

    }
}

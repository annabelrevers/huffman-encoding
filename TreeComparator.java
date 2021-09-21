import java.util.Comparator;

/**
 * Class to compare the frequencies of two binary trees that hold "Data" as their data type
 *
 * @author Annabel Revers, Ashkaan Mahjoob, Dartmouth CS 10, Spring 2020
 */

public class TreeComparator implements Comparator<BinaryTree<Data>> {

    public int compare(BinaryTree<Data> tree1, BinaryTree<Data> tree2) {
        int freq1 = tree1.getData().getFreq();
        int freq2 = tree2.getData().getFreq();

        return freq1 - freq2;

    }
}

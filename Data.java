/**
 * Class to hold character and frequency variables to be used as Binary Tree data type
 *
 * @author Annabel Revers, Ashkaan Mahjoob, Dartmouth CS 10, Spring 2020
 */

public class Data {

    private char character;
    private int frequency;

    public Data(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
    }

    public char getChar() {
        return character;
    }

    public int getFreq() {
        return frequency;
    }

    @Override
    public String toString() {
        return character + ":" + frequency;
    }

}

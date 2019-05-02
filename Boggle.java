import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Boggle {
    private MyTrie lex;
    private Square[][]board;
    private MyTrie foundWords;
    private MyTrie guesses;
    private String[] dice;
    
    public Boggle(String fileName) {
	lex = new MyTrie();
	Scanner readLex = null;
	try {
	    readLex = new Scanner(new File(fileName));
	} catch (FileNotFoundException ex) {
	    System.out.println("the file " + fileName + " is not found");
	    System.exit(1);
	}
	while (readLex.hasNextLine()) {
	    lex.add(readLex.nextLine());
	}
	this.fillDice();
    }


    /*
     *Construct the dice from the file dice.txt.
     *Each line in the file contains the contents of the 6 sides of the die.
     *That is, die 0 has the sides L,R,Y,T,T, and E, and so on.
     *This method should store each line of the file into a different entry of the length-16 array dice.
     */
    private void fillDice() {
	board = new Square[4][4];
	Scanner readDice = null;
	try {
	    readDice = new Scanner(new File("dice.txt"));
	} catch (FileNotFoundException ex) {
	    System.out.println("the file " + "\"dice.txt\"" + " is not found");
	    System.exit(1);
	}
	for (int i = 0; i < 4; i++) {
	    for (int j = 0; j < 4; j++) {
		int rand = (int)Math.random()*6;
		board[i][j] = new Square(i,j,readDice.nextLine().substring(rand, rand+1));
	    }
   	}
    } 
}

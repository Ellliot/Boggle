import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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
     * Return the boggle board
     */
    public Square[][] getBoard(){
	return board;
    }
    
    /*
     * Return the number of guesses
     */
    public int numGuesses() {
	return guesses.size();
    }
    
    /*
     * Return the squares of the board, 
     * one row per line of the string
     */
    public String toString() {
	String toBeReturned = null;
	for(Square[] row: board){
	    for(Square col: row) {
		toBeReturned = toBeReturned + col.toString() + " ";
	    }
	    toBeReturned = toBeReturned + "\n";
	}
	return toBeReturned;
    }
    
    /*
     * Return true if the board contains the word word and false otherwise.
     */
    public boolean contains(String word) {
	return foundWords.contains(word);
    }
    
    /*
     * Add guess to the list of guesses, if it is in foundWords.
     * Return true if it was a valid guess and false otherwise
     */
    public boolean addGuess(String guess) {
	if (this.contains(guess)) {
	    guesses.add(guess);
	    return true;
	}
	return false;
    }
    
    /*
     * Rebuild the foundWords and guesses tries.
     * Roll the dice and fill the board with new squares accordingly.
     * Construct a trie for the dictionary words found in the board.
     */
    public void newGame() {
	foundWords = new MyTrie();
	guesses = new MyTrie();
	this.fillBoardFromDice();
	this.fillFoundWords();
    }
    
    /*
     * Returns a list of valid Squares on the board that form the word w
     */
    public ArrayList<Square> squaresForWord(String w){
	ArrayList<Square> temp = new ArrayList<Square>();
	for (Square[] row: board) {
	    for (Square col: row) {
		temp = squaresForWord(col,w);
		if (!temp.isEmpty()) {
		    return temp;
		}
	    }
	}
	return temp;
    }

    /*
     *Construct the dice from the file dice.txt.
     *Each line in the file contains the contents of the 6 sides of the die.
     *That is, die 0 has the sides L,R,Y,T,T, and E, and so on.
     *This method should store each line of the file into a different entry of the length-16 array dice.
     */
    private void fillDice() {
	dice = new String[16];
	Scanner readDice = null;
	try {
	    readDice = new Scanner(new File("dice.txt"));
	} catch (FileNotFoundException ex) {
	    System.out.println("the file " + "\"dice.txt\"" + " is not found");
	    System.exit(1);
	}
	for (int i = 0; i < 16; i++) {
	    dice[i] = readDice.nextLine();
	}
	readDice.close();
    } 
    
    /*
     * Construct a new board randomly out of the 16 die.
     * if it rolls a "Q" on a dice, it should construct a Square with the String "Qu".
     */
    private void fillBoardFromDice() {
	ArrayList<String> initialSeq = new ArrayList<String>(17);
	ArrayList<String> putDice = new ArrayList<String>(17);
	int location = 0;
	
	for (int i = 0; i < 16; i++) {
	    initialSeq.add(dice[i]);
	}
	for (int j = 16; j > 0; j++) {
	    putDice.add(initialSeq.remove((int)Math.random()*j).substring((int)Math.random() * 6, (int)Math.random() * 6 + 1).toLowerCase());
	}
	for (int i = 0; i < 4; i++) {
	    for (int j = 0; j < 4; j++) {
		if (putDice.get(location).hashCode()=="q".hashCode())
		{
		    putDice.set(location, "qu"); 
		}
		board[i][j] = new Square(i,j,putDice.get(location));
		location++;
	    }
   	}
	initialSeq =null;
	putDice = null;
    }
    
    
    //TODO
    private void search(Square sq, String prefix) {
	int row, col;
	//   if (prefix.substring(0, 1).compareTo(sq.toString()) == 0 ||(prefix.substring(0, 2).compareTo("qu") == 0 && sq.toString().compareTo("qu") == 0)) {
	if (lex.containsPrefix(prefix)) {
	    row = sq.getX();
	    col = sq.getY();
	    String l = sq.toString();
	    sq.mark();
	    String subPre = prefix + l;
	    if (row - 1 >= 0 && col - 1 >= 0 && !board[row - 1][col - 1].isMarked()) {
		search(board[row - 1][col - 1], subPre);
		sq.unmark();
	    }
	    if (row - 1 >= 0 && !board[row - 1][col].isMarked()) {
		search(board[row - 1][col], subPre);
		sq.unmark();
	    }
	    if (row - 1 >= 0 && col + 1 < 4 && !board[row - 1][col + 1].isMarked()) {
		search(board[row - 1][col + 1], subPre);
		sq.unmark();
	    }
	    if (col + 1 < 4 && !board[row][col + 1].isMarked()) {
		search(board[row][col + 1], subPre);
		sq.unmark();
	    }
	    if (row + 1 < 4 && col + 1 < 4 && !board[row + 1][col + 1].isMarked()) {
		search(board[row + 1][col + 1], subPre);
		sq.unmark();
	    }
	    if (row + 1 < 4 && !board[row + 1][col].isMarked()) {
		search(board[row + 1][col], subPre);
		sq.unmark();
	    }
	    if (row + 1 < 4 && col - 1 >= 0 && !board[row + 1][col - 1].isMarked()) {
		search(board[row + 1][col - 1], subPre);
		sq.unmark();
	    }
	    if (col - 1 >= 0 && !board[row][col - 1].isMarked()) {
		search(board[row][col - 1], subPre);
		sq.unmark();
	    }
	}

	//}
    }

    private void fillFoundWords() {
	for(Square[] row: board){
	    for(Square col: row) {
		search(col,"");
	    }
	}
    }

    //TODO
    private ArrayList<Square> squaresForWord(Square sq, String w){
	return null;
    }

    public static void main (String args[]) {
	Boggle boggle = new Boggle( args[0] );
	BoggleFrame bFrame = new BoggleFrame( boggle );
	bFrame.pack();
	bFrame.setLocationRelativeTo(null);
	bFrame.setVisible(true);
    }
}

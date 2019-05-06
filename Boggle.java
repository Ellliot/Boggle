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
	board = new Square[4][4];
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
	System.out.println(foundWords);
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
	    initialSeq.add(dice[(int) (Math.random()*15)]);
	}
	for (int j = 16; j > 0; j--) {
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
    /**search(Square sq, String prefix) 
    // check to see if we have found a word on the current path
    if the current path represents a word in the dictionary
        add the word to the wordlist
    // continue searching on all possible paths from this square
    if there are any words possible from this prefix (use containsPrefix())
        let l = the letter in sq
        for each unmarked square s adjacent to sq
            mark it
            recursively search for words starting at square s with 
                   prefix prefix+sq.letter, and add these to wordlist

            unmark it*/

    private void help(Square sq, String prefix, MyTrie wordlist) {
	if(lex.contains(prefix) == true) {
	    wordlist.add(prefix);
	}
	if(lex.containsPrefix(prefix)) {
	    String l = sq.toString();
	    for(int i = sq.getX()-1; i<=sq.getX()+1; i++) {   
		for(int j = -1; j<=1; j++) {
		    if(i == 0 && j==0) {
			continue; 
		    }
		    if(i == -1 | j == -1) {
			continue;
		    }
		    if(i == 4 | j == 4) {
			continue;
		    }
		    if(board[i][j]!= null && !board[i][j].isMarked()) {
			Square s = board[i][j];
			s.mark();
			this.help(s, prefix+l, wordlist);
			s.unmark();
		    }
		    
		}
	    }  
	}

	
    }
    //TODO
    private void search(Square sq, String prefix) {
	for(int i = 0; i<4; i++) {
	    for(int j = 0; i<4; i++) {
		this.help(board[i][j], board[i][j].toString(), foundWords);
	    }
	}
    }

    private void fillFoundWords() {
	for(Square[] row: board){
	    for(Square col: row) {
		search(col,"");
	    }
	}
    }
    
    /*
     * Returns a list of valid Squares on the board that form the word w
     */
    public ArrayList<Square> squaresForWord(String w){
	for(int i = 0; i<4; i++) {
		for(int j = 0; j<4; j++) {
		    return this.squaresForWord(board[i][j], w);
		}
	    }
	return null;
    }
    //TODO
    private ArrayList<Square> squaresForWord(Square sq, String w){
	String l = sq.toString();
	if( l == w) {
	    ArrayList<Square> novel = new ArrayList();
	    for(int i = 0; i<4; i++) {
		for(int j = 0; j<4; j++) {
		    if(board[i][j].isMarked()) {
			novel.add(board[i][j]);
		    }
		}
	    }
	    novel.add(sq);
	    return novel;
	}
	if(w.length()>1) {
	    if(l == w.substring(w.length()-1)) {
		    for(int i = sq.getX()-1; i<=sq.getX()+1; i++) {   
			for(int j = -1; j<=1; j++) {
			    if(i == -1 | j == -1) {
				continue;
			    }
			    if(i == 4 | j == 4) {
				continue;
			    }
			    if(board[i][j]!= null && !board[i][j].isMarked()) {
				Square s = board[i][j];
				s.mark();
				this.squaresForWord(s,w.substring(1));
			    }
			    
			}
		    }
	    }
  
	}
	return null;
    }

    public static void main (String args[]) {
	Boggle boggle = new Boggle("enable.txt");
	BoggleFrame bFrame = new BoggleFrame( boggle );
	bFrame.pack();
	bFrame.setLocationRelativeTo(null);
	bFrame.setVisible(true);
    }
}

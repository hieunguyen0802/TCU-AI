package cls;

import java.awt.*;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import javax.swing.*;

import cls.Tile.State;

/**
 * Author:
 * Cuong Nguyen
 * Hieu Nguyen
 */
public class Board extends JFrame {
	public static final int SIZE = 8;
	
	//Tiles for GUI
	Tile[][] tiles = new Tile[SIZE][SIZE];

	//directions for refrencing the CCR
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int UP = 2;
	public static final int DOWN = 3;
	public static final int UPLEFT = 4;
	public static final int UPRIGHT = 5;
	public static final int DOWNLEFT = 6;
	public static final int DOWNRIGHT = 7;

	private boolean[] CCR = new boolean[8];

	//The set of all black or white tiles
	Set<Tile> playerTiles = new HashSet<Tile>();
	
	private int blankTiles = 0;
	private int blackTiles = 0;
	private int whiteTiles = 0;
	private Game game;
	
	public Board(Board board) {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				Tile.State tileState = board.getTile(i,j).getState();
				tiles[i][j] = new Tile(tileState,this,i,j);
				placeTile(i,j,tileState);
			}
		}
	}
	
	public Board(Game game){	
		this.game = game;
		//GUI setup
		setSize(430, 430);
		setResizable(true);
		setLayout(new GridLayout(SIZE,SIZE));
		getContentPane().setBackground(Color.white);
		
		//Layout tiles
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				tiles[i][j] = new Tile(this,i,j);
				add(tiles[i][j]);
			}
		}
		
		//Middle of Board
		final int middle = SIZE / 2;
		
		//Setup initial tiles
		placeTile(middle - 1,middle - 1, Tile.State.WHITE);
		placeTile(middle - 1,middle, Tile.State.BLACK);
		placeTile(middle,middle, Tile.State.WHITE);
		placeTile(middle,middle - 1, Tile.State.BLACK);
				
		clearCCR();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
		
	public void resetBoard() {
		setVisible(false);
	}
	
	
	//display game instruction thru help button.
	public void helpBoard() {
		final JFrame frame = new JFrame("Help");
        JTextArea ta = new JTextArea("Setup\n" + 
        		"\n" + 
        		"The board will start with 2 black discs and 2 white discs at the centre of the board.\n" + 
        		"\n" + 
        		"They are arranged with black forming a North-East to South-West direction.\n" + 
        		"\n" + 
        		"White is forming a North-West to South-East direction.\n" + 
        		"\n" + 
        		"Object of the Game\n" + 
        		"The goal is to get the majority of colour discs on the board at the end of the game.\n" + 
        		"\n" + 
        		"\n" + 
        		"\n" + 
        		"Game Play\n" + 
        		"Othello is a strategy board game played between 2 players. One player plays black and the other white.\n" + 
        		"\n" + 
        		"Each player gets 32 discs and black always starts the game.\n" + 
        		"\n" + 
        		"Then the game alternates between white and black until:\n" + 
        		"\n" + 
        		"one player can not make a valid move to outflank the opponent.\n" + 
        		"both players have no valid moves.\n" + 
        		"When a player has no valid moves, he pass his turn and the opponent continues.\n" + 
        		"\n" + 
        		"A player can not voluntarily forfeit his turn.\n" + 
        		"\n" + 
        		"When both players can not make a valid move the gane ends.\n" + 
        		"\n" + 
        		"\n" + 
        		"Valid Moves\n" + 
        		"Black always moves first.\n" + 
        		"\n" + 
        		"A move is made by placing a disc of the player's color on the board in a position that \"out-flanks\" one or more of the opponent's discs.\n" + 
        		"A disc or row of discs is outflanked when it is surrounded at the ends by discs of the opposite color.\n" + 
        		"\n" + 
        		"A disc may outflank any number of discs in one or more rows in any direction (horizontal, vertical, diagonal).\n" + 
        		"\n" + 
        		"For example: a white piece is being placed on the board that creates a straight line made up of a white piece at either end and only black pieces in between.\n" + 
        		"\n" + 
        		"\n" + 
        		"White places in the illustration a disc on E3.\n" + 
        		"\n" + 
        		"The black discs on E4, E5 and E6 are now outflanked by the white disc on E3 and the white disc on E7.\n" + 
        		"\n" + 
        		"The black discs will be flipped to white.\n" + 
        		"\n" + 
        		"\n" + 
        		"All the discs which are outflanked will be flipped, even if it is to the player's advantage not to flip them.\n" + 
        		"\n" + 
        		"Discs may only be outflanked as a direct result of a move and must fall in the direct line of the disc being played.\n" + 
        		"\n" + 
        		"If you can't outflank and flip at least one opposing disc, you must pass your turn. However, if a move is available to you, you can't forfeit your turn.\n" + 
        		"\n" + 
        		"Once a disc has been placed on a square, it can never be moved to another square later in the game.\n" + 
        		"\n" + 
        		"When a player runs out of discs, but still has opportunities to outflank an opposing disc, then the opponent must give the player a disc to use.\n" + 
        		"\n" + 
        		"\n" + 
        		"End of the Game\n" + 
        		"When it is no longer possible for either player to move, the game is over.\n" + 
        		"\n" + 
        		"The discs are now counted and the player with the majority of his or her color discs on the board is the winner.\n" + 
        		"\n" + 
        		"A tie is possible.\n" + 
        		"\n" + 
        		"\n" + 
        		"Time Limit\n" + 
        		"Players can start with a preset time limit for their total number of moves. This timing element adds more pressure to the game.\n" + 
        		"\n" + 
        		"The clock will start counting down at the beginning of a player's first move and be paused each time they complete a turn whilst the opposing player's clock is ticking down.\n" + 
        		"\n" + 
        		"There are varying time limits ranging from 5 minutes up to 30 minutes as seen in the world championship rules.\n" + 
        		"\n" + 
        		"When one player's clock runs out of time, no matter what the position or number of chips on the board, that player looses the game.\n" + 
        		"\n" + 
        		"\n" + 
        		"Handicaps\n" + 
        		"Because there is an advantage in starting first, the more experienced player should give this advantage to the less experienced player.\n" + 
        		"\n" + 
        		"When a very skilled player is playing against an unskilled player, then the skilled player may take on a handicap by setting up the board to give his weaker opponent a four corner advantage.\n" + 
        		"\n" + 
        		"If the difference in skill is not so big, then one, two or three corner advantages can be given.");
 
        JScrollPane sp = new JScrollPane(ta);
 
        frame.setLayout(new FlowLayout());
        frame.setSize(2000, 2000);
        frame.getContentPane().add(sp);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        	frame.setVisible(true);

	}
	
	
	//Adds a black white or blank tile to the board
	public void addTile(State tile) {
		//Increment tile counts
		switch (tile) {
			case BLACK:
				blackTiles++;
				break;
			case WHITE:
				whiteTiles++;
				break;
			default:
				blankTiles++;
				break;
			}
	}
	
	//Removes a tile from the board
	public void removeTile(State tile) {
		//Decrement tile counts
		switch (tile) {
			case BLACK:
				blackTiles--;
				break;
			case WHITE:
				whiteTiles--;
				break;
			default:
				blankTiles--;
				break;
			}
	}
	
	//Number of black tiles
	public int getBlackTiles() {
		return blackTiles;
	}
	
	//Number of white tiles
	public int getWhiteTiles() {
		return whiteTiles;
	}
	
	//Number of blank tiles
	public int getBlankTiles() {
		return blankTiles;
	}
	
	//Simulates a click on tile [x,y]
	public void tileClick(int x, int y) throws FileNotFoundException {
		game.tileClick(x, y);
	}

	//Removes the highlights on all tiles
	public void unHighLightTiles() {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				tiles[i][j].lowlight();
			}
		}
	}
	
	//True blank tiles exist on the board
	public boolean hasBlankTiles() {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (tiles[i][j].isBlank()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public String convertBoard()
	{
		String s=null;
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if(tiles[i][j].getState().equals(State.BLANK))
				{
					if(s==null)s="*";
					else s+="*";
				}
				if(tiles[i][j].getState().equals(State.BLACK))
				{
					if(s==null)s="*";
					else s+="X";
				}
				if(tiles[i][j].getState().equals(State.WHITE))
				{
					if(s==null)s="*";
					else s+="0";
				}
			}
		}
		return s;
	}
	/**
	 * 
	 * @param x x position of tile to return
	 * @param y y position of tile to return
	 * @return
	 */
	public Tile getTile(int x, int y){
		return tiles[x][y];
	}
	
	//Forces a tile with a given color onto [x,y]
	public void placeTile(int x, int y, State color) {
		if (tiles[x][y].getState() == State.BLANK) {
			setTile(x,y,color);
		}
		
		playerTiles.add(tiles[x][y]);
	}
	
	//The set of all tiles on the board
	public Set<Tile> getTileSet() {
		return playerTiles;
	}
	
	//Sets the state of a tile at position [x,y]
	public void setTile(int x, int y, State color){
		tiles[x][y].setState(color);
	}
	
	//Prints the board on the terminal
	public void debugPrint(){
		for (int x = 0; x < SIZE; x++) {
		    for (int y = 0; y < SIZE; y++) {
		    	if(tiles[y][x].getState().equals(State.BLANK))
					System.out.print("*");
				if(tiles[y][x].getState().equals(State.BLACK))
					System.out.print("X");
				if(tiles[y][x].getState().equals(State.WHITE))
				    System.out.print("O");
		    }
		    System.out.println();
		}
	}
	
	/**
	 * flips a tile at the given location
	 * @param x x position of tile to flip
	 * @param y y position of tile to flip
	 */
	public void flipTile(int x, int y){
		tiles[x][y].flip();
	}
	
	//Returns an array of all tiles
	public Tile[][] getTiles() {
		return tiles;
	}
	
	//Resets the CCR array
	private void clearCCR(){
		for(int i = 0; i < Board.SIZE; i++){
			CCR[i] = false;
		}
	}

	
	//The following functions are used to determine if a tile
	//can be placed at a given coordinate [x,y] by checking all
	//tiles horizontally, vertically, and diagonally.
	private boolean moveFromLeft(int x, int y, State col){
		if(x-1 >= 0){
			if(getTile(x-1, y).isBlank())
				return false;
			if(getTile(x-1, y).getState().equals(col)){
				if(getTile(x, y).isBlank())
					return false;
				if(getTile(x, y).getState().equals(col))// may not be needed
					return false;
				return true;
			}
			else{
				return moveFromLeft(x-1, y, col);
			}
		}
		return false;
	}
	
	private boolean moveFromRight(int x, int y, State col){
		if(x+1 <= 5){
			if(getTile(x+1, y).isBlank())
				return false;
			if(getTile(x+1, y).getState().equals(col)){
				if(getTile(x, y).isBlank())
					return false;
				if(getTile(x, y).getState().equals(col))// may not be needed
					return false;
				return true;
			}
			else{
				return moveFromRight(x+1, y, col);
			}
		}
		return false;
	}
	
	private boolean moveFromUp(int x, int y, State col){
		if(y-1 >= 0){
			if(getTile(x, y-1).isBlank())
				return false;
			if(getTile(x, y-1).getState().equals(col)){
				if(getTile(x, y).isBlank())
					return false;
				if(getTile(x, y).getState().equals(col))// may not be needed
					return false;
				return true;
			}
			else{
				return moveFromUp(x, y-1, col);
			}
		}
		return false;
	}
	
	private boolean moveFromUpRight(int x, int y, State col){
		if(y-1 >= 0 && x+1 <=5){
			if(getTile(x+1, y-1).isBlank())
				return false;
			if(getTile(x+1, y-1).getState().equals(col)){
				if(getTile(x, y).isBlank())
					return false;
				if(getTile(x, y).getState().equals(col))// may not be needed
					return false;
				return true;
			}
			else{
				return moveFromUpRight(x+1, y-1, col);
			}
		}
		return false;
	}
	
	private boolean moveFromUpLeft(int x, int y, State col){
		if(y-1 >= 0 && x-1 >=0){
			if(getTile(x-1, y-1).isBlank())
				return false;
			if(getTile(x-1, y-1).getState().equals(col)){
				if(getTile(x, y).isBlank())
					return false;
				if(getTile(x, y).getState().equals(col))// may not be needed
					return false;
				return true;
			}
			else{
				return moveFromUpLeft(x-1, y-1, col);
			}
		}
		return false;
	}
	
	private boolean moveFromDown(int x, int y, State col){
		if(y+1 <= 5){
			if(getTile(x, y+1).isBlank())
				return false;
			if(getTile(x, y+1).getState().equals(col)){
				if(getTile(x, y).isBlank())
					return false;
				if(getTile(x, y).getState().equals(col))// may not be needed
					return false;
				return true;
			}
			else{
				return moveFromDown(x, y+1, col);
			}
		}
		return false;
	}
	
	private boolean moveFromDownRight(int x, int y, State col){
		if(y+1 <= 5 && x+1 <= 5){
			if(getTile(x+1, y+1).isBlank())
				return false;
			if(getTile(x+1, y+1).getState().equals(col)){
				if(getTile(x, y).isBlank())
					return false;
				if(getTile(x, y).getState().equals(col))// may not be needed
					return false;
				return true;
			}
			else{
				return moveFromDownRight(x+1, y+1, col);
			}
		}
		return false;
	}
	
	private boolean moveFromDownLeft(int x, int y, State col){
		if(y+1 <= 5 && x-1 >= 0){
			if(getTile(x-1, y+1).isBlank())
				return false;
			if(getTile(x-1, y+1).getState().equals(col)){
				if(getTile(x, y).isBlank())
					return false;
				if(getTile(x, y).getState().equals(col))// may not be needed
					return false;
				return true;
			}
			else{
				return moveFromDownLeft(x-1, y+1, col);
			}
		}
		return false;
	}
	
	//Determines if a tile of a given color can be placed at [x,y]
	public boolean isValidMove(int x, int y, State player){
		clearCCR();
		boolean u = false;
		boolean	d = false;
		if(!getTile(x, y).isBlank())
			return false;
		if(x != 0){
			if(moveFromLeft(x,y,player)) //check left
				CCR[LEFT] = true;
			if(y != 0){
				if(moveFromUp(x,y,player))//check up
					CCR[UP] = true;
				u = true;
				//check up-left
				if(moveFromUpLeft(x,y,player))
					CCR[UPLEFT] = true;
			}
			if(y != 5){
				if(moveFromDown(x,y,player))//check down
					CCR[DOWN] = true;
				d = true;
				//check down-left
				if(moveFromDownLeft(x,y,player))
					CCR[DOWNLEFT] = true;
			}
		}
		if(x != 5){
			//check right
			if(moveFromRight(x,y,player))
				CCR[RIGHT] = true;
			if(y != 0){
				//check up-right
				if(moveFromUpRight(x,y,player))
					CCR[UPRIGHT] = true;
				if(!u){
					//check up
					if(moveFromUp(x,y,player))
						CCR[UP] = true;
				}
				
			}
			if(y != 5){
				if(!d){
					//check down
					if(moveFromDown(x,y,player))
						CCR[DOWN] = true;
				}
				
				//check down-right
				if(moveFromDownRight(x,y,player))
					CCR[DOWNRIGHT] = true;
			}
		}
		for(boolean dir : CCR){
			if(dir) {
				return true;
			}
		}
		return false;
		
	}
	
	//Flips all enemy tiles that surround the position [x,y]
	public void flipTiles(int x, int y, Tile.State player) {
		if(CCR[LEFT]){
			flipLeft(x, y, player);
		}
		if(CCR[RIGHT]){
			flipRight(x, y, player);
		}
		if(CCR[UP]){
			flipUp(x, y, player);
		}
		if(CCR[DOWN]){
			flipDown(x, y, player);
		}
		if(CCR[DOWNLEFT]){
			flipDownLeft(x, y, player);
		}
		if(CCR[DOWNRIGHT]){
			flipDownRight(x, y, player);
		}
		if(CCR[UPLEFT]){
			flipUpLeft(x, y, player);
		}
		if(CCR[UPRIGHT]){
			flipUpRight(x, y, player);
		}
	}
	
	/**
	 * The following functions flips tiles horizontally, vertically, and diagonally.
	 * @pre the x,y must have a tile in it
	 * @param x 
	 * @param y
	 * @param player the player who's turn it is
	 */
	private void flipLeft(int x, int y, State player){
		if(getTile(x, y).getState().equals(Tile.getOppositeState(player)))
		{
			flipTile(x, y);
		}
		if(x-1 >= 0){
			if(getTile(x-1, y).getState().equals(Tile.getOppositeState(player)))
				flipLeft(x-1, y, player);
		}
	}
	private void flipRight(int x, int y, State player){
		if(getTile(x, y).getState().equals(Tile.getOppositeState(player)))
		{
			flipTile(x, y);
		}
		if(x+1 <= 5){
			if(getTile(x+1, y).getState().equals(Tile.getOppositeState(player)))
				flipRight(x+1, y, player);
		}
	}
	private void flipUp(int x, int y, State player){
		if(getTile(x, y).getState().equals(Tile.getOppositeState(player)))
		{
			flipTile(x, y);
		}
		if(y-1 >= 0){
			if(getTile(x, y-1).getState().equals(Tile.getOppositeState(player)))
				flipUp(x, y-1, player);
		}
	}
	private void flipUpRight(int x, int y, State player){
		if(getTile(x, y).getState().equals(Tile.getOppositeState(player)))
		{
			flipTile(x, y);
		}
		if(y-1 >= 0 && x+1 <= 5){
			if(getTile(x+1, y-1).getState().equals(Tile.getOppositeState(player)))
				flipUpRight(x+1, y-1, player);
		}
	}
	private void flipUpLeft(int x, int y, State player){
		if(getTile(x, y).getState().equals(Tile.getOppositeState(player)))
		{
			flipTile(x, y);
		}
		if(y-1 >= 0 && x-1 >= 0){
			if(getTile(x-1, y-1).getState().equals(Tile.getOppositeState(player)))
				flipUpLeft(x-1, y-1, player);
		}
	}
	private void flipDown(int x, int y, State player){
		if(getTile(x, y).getState().equals(Tile.getOppositeState(player)))
		{
			flipTile(x, y);
		}
		if(y+1 <= 5){
			if(getTile(x, y+1).getState().equals(Tile.getOppositeState(player)))
				flipDown(x, y+1, player);
		}
	}
	private void flipDownLeft(int x, int y, State player){
		if(getTile(x, y).getState().equals(Tile.getOppositeState(player)))
		{
			flipTile(x, y);
		}
		if(y+1 <= 5 && x-1 >= 0){
			if(getTile(x-1, y+1).getState().equals(Tile.getOppositeState(player)))
				flipDownLeft(x-1, y+1, player);
		}
	}
	private void flipDownRight(int x, int y, State player){
		if(getTile(x, y).getState().equals(Tile.getOppositeState(player)))
		{
			flipTile(x, y);
		}
		if(y+1 <= 5 && x+1 <= 5){
			if(getTile(x+1, y+1).getState().equals(Tile.getOppositeState(player)))
				flipDownRight(x+1, y+1, player);
		}
	}
}

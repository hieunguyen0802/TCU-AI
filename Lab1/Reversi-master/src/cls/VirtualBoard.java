package cls;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;
import cls.Tile.State;

/**
 * Author:
 * Cuong Nguyen
 * Hieu Nguyen
 */
public class VirtualBoard{// implements BoardForm{
public static final int SIZE = 8;
	
	//Tiles for GUI
	VirtualTile[][] tiles = new VirtualTile[SIZE][SIZE];

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
	Set<VirtualTile> playerTiles = new HashSet<VirtualTile>();
	
	private int blankTiles = 0;
	private int blackTiles = 0;
	private int whiteTiles = 0;
	private Game game;
	
	/**
	 * creates a board from another board with no GUI
	 */
	public VirtualBoard(Board board) {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				State tileState = board.getTile(i,j).getState();
				tiles[i][j] = new VirtualTile(tileState,this,i,j);
				placeTile(i,j,tileState);
			}
		}
	}
	public VirtualBoard(VirtualBoard board) {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				State tileState = board.getTile(i,j).getState();
				tiles[i][j] = new VirtualTile(tileState,this,i,j);
				placeTile(i,j,tileState);
			}
		}
	}
		
	public void addTile(State tile) {
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
	
	public void removeTile(State tile) {
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
	
	public int getBlackTiles() {
		return blackTiles;
	}
	
	public int getWhiteTiles() {
		return whiteTiles;
	}
	
	public int getBlankTiles() {
		return blankTiles;
	}
	
	public void tileClick(int x, int y) {
		try {
			game.tileClick(x, y);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

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
	
	/**
	 * 
	 * @param x x position of tile to return
	 * @param y y position of tile to return
	 * @return
	 */
	public VirtualTile getTile(int x, int y){
		return tiles[x][y];
	}
	
	public void placeTile(int x, int y, State color) {
		if (tiles[x][y].getState() == State.BLANK) {
			setTile(x,y,color);
		}
		
		if (color != State.BLANK) {
			playerTiles.add(tiles[x][y]);
		}
	}
	
	public Set<VirtualTile> getTileSet() {
		return playerTiles;
	}
	
	public boolean hasAdjacentBlanks(VirtualTile tile) {
		int x = tile.getXCoord();
		int y = tile.getYCoord();
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				return hasAdjacentBlank(x+i,y+j);
			}
		}
		return false;
	}
	
	private boolean hasAdjacentBlank(int x, int y) {
		//Don't walk off the matrix
		if (x > -1 && x < SIZE && y > -1 && y < SIZE) {
			return getTile(x,y).getState() == State.BLANK;
		}
		return false;
	}
	
	public void setTile(int x, int y, State color){
		tiles[x][y].setState(color);
	}
	
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
	
	public VirtualTile[][] getTiles() {
		return tiles;
	}
	
	
	private void clearCCR(){
		for(int i = 0; i < Board.SIZE; i++){
			CCR[i] = false;
		}
	}

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
	
	//need to change null checks to checks for blank state
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
	
	public void flipTiles(int x, int y, State player) {
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

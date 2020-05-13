package cls;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Author:
 * Cuong Nguyen
 * Hieu Nguyen
 */
public class Tile extends JButton implements ActionListener, TileForm{
	//The state of a tile can be blank, white, or blank.
	public enum State {
		BLACK, WHITE, BLANK;
		public static State getOppositeState(State state){
			if(state == State.BLANK)
				return State.BLANK;
			if(state == State.BLACK)
				return State.WHITE;
			else {
				return State.BLACK;
			}
		}
	}
	
	//Locations for tile images
	public static final String BLACK_IMG = "../resources/black_tile.png";
	public static final String WHITE_IMG = "../resources/white_tile.png";
	public static final String BLACK_HI_IMG = "../resources/black_tile_highlight.png";
	public static final String WHITE_HI_IMG = "../resources/white_tile_highlight.png";
	public static final String BLANK_IMG = "../resources/blank_tile.png";

	private State state = State.BLANK;
	private Board board;
	private int x;
	private int y;
	
	//Creates a Tile on a board at [x,y]
	public Tile(Board board, int x, int y){
		this(State.BLANK, board, x, y);
	}
	
	//Creates a Tile with a state on a board at [x,y]
	public Tile(State state, Board board, int x, int y) {
		this.board = board;
		this.x = x;
		this.y = y;
		setState(state);
		setImage(state);
		board.addTile(state);
        addActionListener(this); 
        
        setHorizontalTextPosition(JButton.CENTER);
        setVerticalTextPosition(JButton.CENTER);
        
		String column = getCharForNumber(y+1);
        setText(column+(x+1));
	}
	
	//Returns the char value for an integer
	private String getCharForNumber(int i) {
	    return i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null;
	}

	public int getXCoord() {
		return x;
	}
	
	public int getYCoord() {
		return y;
	}
	
	//Responds to the clicking of a tile and passes to board.
	public void actionPerformed(ActionEvent e) {
    	//Only accepts clicks on blank tiles
    	if (state == State.BLANK) {
    		//Check if game is ready to continue
    		try {
				board.tileClick(x,y);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	}
    }

	//Flips a tile's state and then highlights
	public void flip(){
		if (state == State.BLACK) {
			setState(State.WHITE);
		}
		else if (state == State.WHITE) {
			setState(State.BLACK);
		}
		highlight();
	}

	//Is the tile blank?
	public boolean isBlank(){
		return state.equals(State.BLANK);
	}
	
	//Black to white. Vice versa
	public State getReverseState(){
		if(state == State.BLACK)
			return State.WHITE;
		else {
			return State.BLACK;
		}
	}
	
	//Black to white. Vice versa. Blank to blank
	public static State getOppositeState(State state){
		if(state == State.BLANK)
			return State.BLANK;
		if(state == State.BLACK)
			return State.WHITE;
		else {
			return State.BLACK;
		}
	}

	//Sets the state of a tile and adds it to the board.
	public void setState(State state){
		if (this.state != state) {
			board.removeTile(getState());
			board.addTile(state);
			setImage(state);
			this.state = state;
		}
	}
	
	//Changes the image of a tile based off of the state.
	private void setImage(State state) {
		//Change image
		Image img;
		switch (state) {
		case BLACK:
			img = Toolkit.getDefaultToolkit().getImage(getClass().getResource(BLACK_IMG));
			break;
		case WHITE:
			img = Toolkit.getDefaultToolkit().getImage(getClass().getResource(WHITE_IMG));
			break;
		default:
			img = Toolkit.getDefaultToolkit().getImage(getClass().getResource(BLANK_IMG));
			break;
		}
		Image newimg = img.getScaledInstance( 50, 50,  java.awt.Image.SCALE_SMOOTH ) ;  
		ImageIcon icon = new ImageIcon(newimg);
		setIcon(icon);
	}

	//Highlights a tile by swapping out images
	public void highlight(){
		Image img;
		switch (state) {
		case BLACK:
			img = Toolkit.getDefaultToolkit().getImage(getClass().getResource(BLACK_HI_IMG));
			break;
		case WHITE:
			img = Toolkit.getDefaultToolkit().getImage(getClass().getResource(WHITE_HI_IMG));
			break;
		default:
			img = Toolkit.getDefaultToolkit().getImage(getClass().getResource(BLANK_IMG));
			break;
		}
		Image newimg = img.getScaledInstance( 50, 50,  java.awt.Image.SCALE_SMOOTH ) ;  
		ImageIcon icon = new ImageIcon(newimg);
		setIcon(icon);
	}
	
	//Un-highlights a tile
	public void lowlight(){
		setImage(state);
	}

	//The state of the tile. Black, white, blank
	public State getState(){
		return state;
	}
	
	public String toString(){
		return state.toString();
	}
}

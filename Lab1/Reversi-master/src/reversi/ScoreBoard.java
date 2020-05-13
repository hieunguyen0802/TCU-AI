package reversi;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Author:
 * Cuong Nguyen
 * Hieu Nguyen
 */
public class ScoreBoard extends JFrame implements ActionListener {

	private Game game;
	private JLabel playerOneScore = new JLabel();
	private JLabel playerTwoScore = new JLabel();
	private JLabel lastMove = new JLabel();
	private JButton resetB = new JButton("Reset");
	private JButton helpB = new JButton("Help");
	private JButton skipB = new JButton("Skip Turn :(");
	
	public ScoreBoard(reversi.Game game2) {
		this.game = game2;
		setSize(350, 200);
		setResizable(true);
		setLayout(new GridLayout(2,3));
		getContentPane().setBackground(Color.white);

		//Set default scores to 0
		setPlayerOneScore(0);
		setPlayerTwoScore(0);
		add(playerOneScore);
		add(playerTwoScore);
		
		lastMove.setText("Last Move: None");
		add(lastMove);
		
		//learning check
		//add(learnCB);
		
		//add more buttons
		//JButton skip = new JButton("Skip Turn :(");
		skipB.addActionListener(this);
		add(skipB);
		
		//JButton reset = new JButton("Reset");
		resetB.addActionListener(this);
		add(resetB);
		
		//JButton help = new JButton("Help");
		helpB.addActionListener(this);
		add(helpB);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	//Action listener for the skip turn button
		public void actionPerformed(ActionEvent e) {
			JButton btn = (JButton) e.getSource();
			if (btn == skipB) {
				game.skipTurn();
			} else if (btn == resetB){
				game.reset();
			} else {
				game.help();
			}
	    }

	//reset ScoreBoard
		public void resetScoreBoard() {
			setVisible(false);
		}
	//Sets player 1's score
	public void setPlayerOneScore(int score) {
		playerOneScore.setText("Player 1: "+score);
	}
	
	//Sets player 2's score
	public void setPlayerTwoScore(int score) {
		playerTwoScore.setText("Player 2: "+score);
	}
	
	//Display a move on the scoreboard
	public void showMove(int x, int y) {
		String column = getCharForNumber(y+1);
		lastMove.setText("Last Move: "+(x+1)+","+column);
	}
	
	//Convert a integer into its corresponding char value
	private String getCharForNumber(int i) {
	    return i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null;
	}
}

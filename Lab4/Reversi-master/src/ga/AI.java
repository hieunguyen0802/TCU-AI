package ga;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import ga.Tile.State;

/**
 * Author:
 * Cuong Nguyen
 * Hieu Nguyen
 */

public class AI {
	static final int population = 50;
	static final int GENERATION_COUNT = 30;
	static final int[][] dist = {{500,-240,85,69,69,85,-240,500},
			{-240,-130,49,23,23,49,-130,-240},
			{85,49,1,9,9,1,49,85},
			{69,23,9,32,32,9,23,69},
			{69,23,9,32,32,9,23,69},
			{85,49,1,9,9,1,49,85},
			{-240,-130,49,23,23,49,-130,-240},
			{500,-150,30,10,10,30,-150,500}};
	private int color;
	static final int FITNESS_COUNT = 10;
	static double[] values;
	static double fitness=1.0;
	private static Set<Move> getScores (Board board, Tile.State player) {
		values = new double[9];
		for (int j = 0; j < 9; j++)
			values[j] = Math.random();	
		Set<Move> possibleMoves = getPossibleMoves(board, player);
		int prevSameMoves = possibleMoves.size();
		int prevDiffMoves = getPossibleMoves(board,Tile.getOppositeState(player)).size();
		for (Move currentMove : possibleMoves) {
			VirtualBoard temp;
			temp = new VirtualBoard(board);
			int currSameMoves = getPossibleMoves(temp,player).size();
			int currDiffMoves = getPossibleMoves(temp,Tile.getOppositeState(player)).size();
			double score = 1;
			for (int x = 0; x < 8; x++)
				for (int y = 0; y < 8; y++) {
					if(board.tiles[x][y].getState().equals(player))
						score += values[dist[x][y]];
					else if(board.tiles[x][y].getState().equals(Tile.getOppositeState(player)))
						score -= values[dist[x][y]];
				}
			currentMove.setScore(score + Math.abs(values[6]) * (prevDiffMoves - currDiffMoves) + Math.abs(values[7]) * (currSameMoves - prevSameMoves) 
					+ Math.abs(values[8]) *  (temp.getBlackTiles() + temp.getWhiteTiles() - board.getBlackTiles() - board.getWhiteTiles()));
		}
		return possibleMoves;
	}
	/**
	 * 
	 * @param board
	 * @param player
	 * @return the best move the given player can make on this board
	 */
	public static Move getBestMove(Board board, Tile.State player){
		Move bestMove;
		bestMove = new Move(-1, -1, player, -9999.99);
		Set<Move> possibleMoves = getPossibleMoves(board, player);
		for (Move currentMove : possibleMoves) {
			if (bestMove.getScore() < currentMove.getScore())
			{
				bestMove=currentMove;
			}
		}
		System.out.println("move prob "+ bestMove.getScore());
		return bestMove;
	}
		
	/**
	 * 
	 * @param board
	 * @param player
	 * @return set of all possible moves for given player
	 */
	private static Set<Move> getPossibleMoves(Board board, State player) {
		Set<Move> possibleMoves = new HashSet<Move>();
		
		for (int i = 0; i < VirtualBoard.SIZE; i++) {
			for (int j = 0; j < VirtualBoard.SIZE; j++) {
				if (board.getTile(i, j).getState() == Tile.State.BLANK) {
					boolean ValidMove = board.isValidMove(i,j,player);
					
					if (ValidMove) {
						Move possibleMove = new Move(i,j,player);
						possibleMoves.add(possibleMove);
					}
				}
			}
		}
		
		return possibleMoves;
	}
	private static Set<Move> getPossibleMoves(VirtualBoard board, State player) {
		Set<Move> possibleMoves = new HashSet<Move>();
		
		for (int i = 0; i < VirtualBoard.SIZE; i++) {
			for (int j = 0; j < VirtualBoard.SIZE; j++) {
				if (board.getTile(i, j).getState() == Tile.State.BLANK) {

					boolean ValidMove = board.isValidMove(i,j,player);
										
					if (ValidMove) {
						Move possibleMove = new Move(i,j,player);
						possibleMoves.add(possibleMove);
					}
				}
			}
		}
		
		return possibleMoves;
	}
	public static double calcFitness (int gameRes) throws IOException {
		for (int i = 0; i < FITNESS_COUNT; i++) {
				if (gameRes > 0)
					fitness += 0.4;
				else fitness -= 0.2;
		}
		fitness /= FITNESS_COUNT;
		return fitness;
	}
}

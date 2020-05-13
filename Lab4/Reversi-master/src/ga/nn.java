package ga;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Vector;
import java.awt.event.*;
import javax.swing.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set; 
import ga.Tile.State;
import objects.*;
/**
 * Author:
 * Cuong Nguyen
 * Hieu Nguyen
 */

public class nn {
	public static double FIXED_REWARD = 0.0;
    public static double REWARD_WIN = 100;
    public static double REWARD_LOSE = -100;
    public static double DISCOUNT_FACTOR = 0.5; // gamma
    public static double LEARNING_RATE = 0.5; // alpha
    private static double alpha=0.5;
    public static Board oldb=null;
    static String fileName = "weights";
    private static double  beta=0.35;
    static double sum;
	static double error[] = new double[64];
	static double errorAxon[] = new double[64];
    static double[] iA= new double[64];
    static double [] oA = new double[64];
    static double [][] ddo = new double[64][32];
    static double [][] ddh = new double[32][64];
    static double [] errorInt = new double[32];
    static PrintWriter out;
	Random random = new Random();
	final static int numInput = 64;
	final static int numAxon = 32;
	final static int numOutput = 64;
	public static HiddenNeuron[]  hiddenNeuron;
	public static OutputNeuron[]  outputNeuron;
    private String curState;
    public static Vector<NeuralPattern>  nnPatterns = new Vector();
    private String des;
    private static NNRD nn;
    //public Vector<NeuralPattern>  nnPatterns = new Vector();
	/**
	 * 
	 * @param board
	 * @param player
	 * @return the best move the given player can make on this board
	 * @throws FileNotFoundException 
	 */
    
    public static void neuralTraining(Board inputState, Board outputState) {
		  // propagation
		 String binp=inputState.convertBoard();
		 String bout=outputState.convertBoard();
		 System.out.println ( " IP "+ binp + " OP " + bout );
		  sum=0;
	      for(int i=0; i< hiddenNeuron.length; i++) {
	    	  sum=0;
	    	  
	    	  for(int j=0; j< 64 ; j++) sum = sum+hiddenNeuron[i].den[j]*iA[j]; 
	    	  hiddenNeuron[i].axonValue = 1/(1+Math.exp(-(sum+hiddenNeuron[i].th)));
	    	  if (sum>hiddenNeuron[i].th) hiddenNeuron[i].axon=1;else hiddenNeuron[i].axon=0; 
	    	  System.out.print(" HIDDEN " + hiddenNeuron[i].axonValue + " " +sum);
	      }
	      System.out.print("\n");
	      for(int i=0; i< outputNeuron.length; i++) {
	    	  sum=0;
	    	  for(int j=0; j< 32 ; j++) sum = sum+outputNeuron[i].den[j]*hiddenNeuron[j].axon; 
	    	  outputNeuron[i].axonValue = 1/(1+Math.exp(-(sum+outputNeuron[i].th)));
	    	  if (sum>outputNeuron[i].th) outputNeuron[i].axon=1; else outputNeuron[i].axon=0; 
	     
	    	  System.out.print(" OUTPUT " + outputNeuron[i].axonValue + " " +sum);
	      }  System.out.print("\n");	  
	     
	     // Backpropagation
	     for(int i=0; i< outputNeuron.length; i++)
	     { error[i] =(double)(oA[i]- outputNeuron[i].axonValue) ;
	       errorAxon[i] = outputNeuron[i].axonValue*(1-outputNeuron[i].axonValue)*error[i];
	        System.out.println(" for output state " + i + " output " + oA[i] +  " General correction "+  errorAxon[i] + " for " + i + " " + outputNeuron[i].axonValue );
	        
	         for(int j=0; j< 32; j++)   errorInt[j]=0;
	         for(int j=0; j< 32; j++)
	        	{ddo[i][j] = errorAxon[i]*outputNeuron[i].den[j];
	        	 errorInt[j] = ddo[i][j]*hiddenNeuron[j].axonValue*(1-hiddenNeuron[j].axonValue)+ errorInt[j];
	        	 outputNeuron[i].den[j] = outputNeuron[i].den[j] + alpha*ddo[i][j];
	        	 System.out.println(  "for output dendrite j" + j + " new dendrite  "+   outputNeuron[i].den[j]  );
	             }
	          outputNeuron[i].th = outputNeuron[i].th + alpha*errorAxon[i];
	          System.out.println(  "for output " + i + " threshold " +  outputNeuron[i].th  );
	         for(int j=0; j< 32; j++)
	        	     System.out.println(" for hidden " + j + " intermediate axon correction  " +errorInt[j]  );      
	     }
	     
	                 
	     
	      for(int i=0; i< 32; i++)
	    	  
	        {for(int j=0; j< 64; j++)
	     	{
	         ddh[i][j] = errorInt[i]*hiddenNeuron[i].den[j];
	         
	         hiddenNeuron[i].den[j] = hiddenNeuron[i].den[j] + alpha*ddh[i][j];
	         System.out.println(  "for hidden dendrite j" + j + " new dendrite  "+   hiddenNeuron[i].den[j]  );
	     	}
	        hiddenNeuron[i].th = hiddenNeuron[i].th + alpha*errorInt[i];
	        System.out.println(" for hidden " + i + " threshold " +  hiddenNeuron[i].th  );}
	      //saveWeightText(true);
		//	saveWeightFiles(); 
	      nn = new NNRD(iA, hiddenNeuron, outputNeuron, oA);	     
	  }
    
    public static void extractCurrentState(Board board,double[] iA) {
    	String b=board.convertBoard();
		for (int i = 0; i < b.length(); i++)
			if(b.charAt(i)=='*')iA[i] = 0;
			else if(b.charAt(i)=='X')iA[i] = 1;
			else iA[i]=2;
	}
	// Initialize random weights
	public static void initialize(int hn, int ot) {
		System.out.println("Initialize weights.");
		hiddenNeuron = new HiddenNeuron[hn];
		for (int i = 0; i < hn; i++)
			hiddenNeuron[i] = new HiddenNeuron(numInput);
		outputNeuron = new OutputNeuron[ot];
		for (int i = 0; i < ot; i++)
			outputNeuron[i] = new OutputNeuron(numAxon);
		saveWeightText(false);
	}
	public static void saveWeightText(boolean isTrained) {
		String fileName;
		if (isTrained)
			fileName = "trainedWeight";
		else
			fileName = "nottrainedWeight";

		try {
			out = new PrintWriter(new FileOutputStream(new File(System.getProperty("user.dir") +"/src/data/" + fileName + ".txt"), true));
			if (isTrained)
				for (int i = 0; i < outputNeuron.length; i++) {
					if (isTrained)
						out.println(" for output state " + i + " output " + oA[i] + " General correction "
								+ errorAxon[i] + " for " + i + " " + outputNeuron[i].axonValue);
					for (int j = 0; j < numAxon; j++)
						out.println("for output dendrite j" + j + " new dendrite  " + outputNeuron[i].den[j]);
					out.println("for output " + i + " threshold " + outputNeuron[i].th);
					if (isTrained)
						for (int j = 0; j < numAxon; j++)
							out.println(" for hidden " + j + " intermediate axon correction  " + errorInt[j]);
				}

			for (int i = 0; i < numAxon; i++) {
				for (int j = 0; j < 64; j++) {
					out.println("for hidden dendrite j" + j + " new dendrite  " + hiddenNeuron[i].den[j]);
				}
				out.println(" for hidden " + i + " threshold " + hiddenNeuron[i].th);
			}

			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
	public static void saveWeightFiles() {
		try {
			FileOutputStream fo = new FileOutputStream(new File(System.getProperty("user.dir") +"/src/data/weights.nnw"));
			ObjectOutputStream oo = new ObjectOutputStream(fo);
			// save objects
			oo.writeObject(hiddenNeuron);
			oo.writeObject(outputNeuron);
			oo.close();
			fo.close();
			System.out.println("Training done. Save new weights at data/weights.nnw");

		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			System.out.println("Error saving stream");
		}
	}

	public static void loadWeightFiles() {
		try {
			// Open given file in append mode. 
            //BufferedWriter out = new BufferedWriter(new FileWriter(file, true)); 
			FileInputStream fi = new FileInputStream(new File(System.getProperty("user.dir") +"/src/data/weights.nnw"));
			ObjectInputStream oi = new ObjectInputStream(fi);
			// Read objects
			hiddenNeuron = (HiddenNeuron[]) oi.readObject();
			outputNeuron = (OutputNeuron[]) oi.readObject();
			if (hiddenNeuron.length != numAxon || outputNeuron.length != numOutput)
				initialize(numAxon, numOutput);

			oi.close();
			fi.close();
			System.out.println("Load weights at data/weights.nnw");

		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			initialize(numAxon, numOutput);
		} catch (IOException e) {
			System.out.println("Error initializing stream");
			initialize(numAxon, numOutput);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Move getBestMove(Board board, Tile.State player) throws FileNotFoundException{
		//Initialize the best move to zero
		loadWeightFiles();
		Move bestMove;
		extractCurrentState(board,iA);
		int m=0;
		int k=0;
		String b=board.convertBoard();
		if(oldb!=null && oldb.convertBoard()!=null && board.convertBoard()!=null) neuralTraining(oldb,board);
		System.out.println(b);
		bestMove = new Move(-1, -1, player, 0.00);
		//The set of all possible moves for a given player
		Set<Move> possibleMoves = getPossibleMoves(board, player);
		//The temporary board used for visualizing possible move sequences
		//VirtualBoard temp;
		int nsize = possibleMoves.size();
		double mprob = -999.999;
		long tmp=findBoard(b);
		double arr[] = new double[100];
		for (Move currentMove : possibleMoves) 
		{
			bestMove=currentMove;
			break;
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		long stt=cal.getTimeInMillis();
		System.out.println("move prob "+ bestMove.getScore());
		saveFileBoard(stt,b);
		saveFileGame(stt,k);
		saveWeightText(true);
		saveWeightFiles(); 
		//saveFileSTM(stt,m,arr);
		oldb =board;
		return bestMove;
	}
	private static long findBoard(String board) throws FileNotFoundException {
		File file =  new File(System.getProperty("user.dir") +"/src/data/game.board"); 
		Scanner sc = new Scanner(file);
		long pos=-1,res=-1;
		while (sc.hasNext()) 
		{
			pos=sc.nextLong();
			String b = sc.next();
			if(b==board)
			{
				res=pos;
				break;
			}
		}
		sc.close();
		return res;
	}
	
	private static double[] findSTM(long tmp) throws FileNotFoundException {
		File file =  new File(System.getProperty("user.dir") +"/src/data/game.stm"); 
		Scanner sc = new Scanner(file);
		double ar[] = new double[100];
		long p;
		String b = null;
		double res[] = new double[100];
		while (sc.hasNextLine()) 
		{
			b=sc.nextLine();
			Scanner sc1 = new Scanner(b);
			int i=0;
			p=sc1.nextLong();
			while (sc1.hasNext()) 
			{
				double k=sc1.nextDouble();
				ar[i]=k;
				i++;
			}
			if(p==tmp)res=ar;
			sc1.close();
		}
		sc.close();
		return res;
	}
	
	private static void saveFileBoard(long stt, String b) 
	{
		//System.out.println("Save file board");
		try { 
			 File file = new File(System.getProperty("user.dir") +"/src/data/game.board");
            // Open given file in append mode. 
            BufferedWriter out = new BufferedWriter(new FileWriter(file, true)); 
            out.write(Long.toString(stt));
            out.write(" ");
            out.write(b);
            out.write("\n");
            out.close(); 
        } 
        catch (IOException e) { 
            System.out.println("exception occoured" + e); 
        } 
	}
	
	private static void saveFileGame(long stt, int b) 
	{
		//System.out.println("Save file game");
		try { 
			 File file = new File(System.getProperty("user.dir") +"/src/data/game.play");
            // Open given file in append mode. 
            BufferedWriter out = new BufferedWriter(new FileWriter(file, true)); 
            out.write(Long.toString(stt));
            out.write(" ");
            out.write(Integer.toString(b));
            out.write("\n");
            out.close(); 
        } 
        catch (IOException e) { 
            System.out.println("exception occoured" + e); 
        } 
		 
	}
	
	public static void saveFileSTM(long stt, int move, double[] prob) 
	{
		//System.out.println("Save file STM");
		try { 
			 File file = new File(System.getProperty("user.dir") +"/src/data/game.stm");
            // Open given file in append mode. 
            BufferedWriter out = new BufferedWriter(new FileWriter(file, true)); 
            out.write(Long.toString(stt));
            out.write(" ");
            out.write(Integer.toString(move));
            out.write(" ");
            for (int i=0; i<85 ; i++)
			{
            	out.write(Double.toString(prob[i]));
            	out.write(" ");
			}
            out.write("\n");
            out.close(); 
        } 
        catch (IOException e) { 
            System.out.println("exception occoured" + e); 
        } 
	}
	//store prescriptive data
	public static void savefilepres(long stt, int move, double[] prob) 
	{
		//System.out.println("Save file pres");
		try { 
			 File file = new File(System.getProperty("user.dir") +"/src/data/stmknowledge.txt");
            // Open given file in append mode. 
            BufferedWriter out = new BufferedWriter(new FileWriter(file, true)); 
            out.write(Long.toString(stt));
            out.write(" ");
            out.write(Integer.toString(move));
            out.write(" ");
            for (int i=0; i<85 ; i++)
			{
            	out.write(Double.toString(prob[i]));
            	out.write(" ");
			}
            out.write("\n");
            out.close(); 
        } 
        catch (IOException e) { 
            System.out.println("exception occoured" + e); 
        } 
		
		 
	}
	/**
	 * 
	 * @param board
	 * @param player
	 * @return set of all possible moves for given player
	 */
	private static Set<Move> getPossibleMoves(Board board, State player) 
	{
		//Set of all possible moves that white or black can make at this turn.
		Set<Move> possibleMoves = new HashSet<Move>();
		
		//Check each tile on the board.
		for (int i = 0; i < VirtualBoard.SIZE; i++) {
			for (int j = 0; j < VirtualBoard.SIZE; j++) {
				//Look at only blank tiles.
				if (board.getTile(i, j).getState() == Tile.State.BLANK) {
					//Is it a valid move for player
					boolean ValidMove = board.isValidMove(i,j,player);
					//boolean blackValidMove = board.isValidMove(i,j,Tile.State.BLACK);
					
					if (ValidMove) {
						//Create the move for white and add to the set of moves.
						Move possibleMove = new Move(i,j,player);
						possibleMoves.add(possibleMove);
					}
				}
			}
		}
		
		return possibleMoves;
	}
}

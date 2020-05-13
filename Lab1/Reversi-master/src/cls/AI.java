package cls;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set; 
import cls.Tile.State;

/**
 * Author:
 * Cuong Nguyen
 * Hieu Nguyen
 */

public class AI {
	private static double  beta=0.35; 
	/**
	 * 
	 * @param board
	 * @param player
	 * @return the best move the given player can make on this board
	 * @throws FileNotFoundException 
	 */
	public static Move getBestMove(Board board, Tile.State player) throws FileNotFoundException{
		//Initialize the best move to zero
		Move bestMove;
		int m=0;
		int k=0;
		String b=board.convertBoard();
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
		if(tmp!=-1)
		{
			System.out.println("find old board");
			arr=findSTM(tmp);
			m=(int)arr[0];
			int i=1;
			for (Move currentMove : possibleMoves) 
			{
				currentMove.setScore(arr[i]);
				//System.out.println("cur move prob "+ currentMove.getScore());
				i++;
				if (mprob < currentMove.getScore())
				{
					mprob=currentMove.getScore();
					bestMove=currentMove;
					m=i;
				}
			}
		}
		else
		{
			System.out.println("cannot find");
			int i=0;
			for (Move currentMove : possibleMoves) 
			{
				if(nsize!=0)
				{
					currentMove.setScore(1.00/(double)nsize);
					arr[i]=1.00/(double)nsize;
				}
				else
				{
					currentMove.setScore(0.00);
					arr[i]=0.00;
				}
				i++;
				if (mprob < currentMove.getScore())
				{
					mprob=currentMove.getScore();
					bestMove=currentMove;
					m=i;
				}
			}
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		long stt=cal.getTimeInMillis();
		System.out.println("move prob "+ bestMove.getScore());
		saveFileBoard(stt,b);
		saveFileGame(stt,k);
		saveFileSTM(stt,m,arr); 
		return bestMove;
	}
	
	public static void learning(boolean res) throws FileNotFoundException
   {
		File file1 =  new File(System.getProperty("user.dir") +"/src/data/game.play");
		Scanner sc1 = new Scanner(file1);
		long stt;
		int pos;
		long tmp;
		int tmp1;
		String b = null;
		String c=null;
		double rew, pun, rewOthers, punOthers;
		double ar[] = new double[100];
		if(res)
		{
			while (sc1.hasNextLine()) 
			{
				b=sc1.nextLine();
				Scanner sc = new Scanner(b);
				stt=sc.nextLong();
				pos=sc.nextInt();
				sc.close();
				File file2 =  new File(System.getProperty("user.dir") +"/src/data/game.stm");
				Scanner sc2 = new Scanner(file2);
				while (sc2.hasNextLine()) 
				{
					c=sc2.nextLine();
					Scanner ss = new Scanner(c);
					tmp=ss.nextLong();
					tmp1=(int)ss.nextInt();
					double arr[] = new double[100];
					int ii=0;
					while (ss.hasNext()) 
					{
						double k=ss.nextDouble();
						arr[ii]=k;
						ii++;
					}
					ss.close();
					if(tmp==stt)
					{
						ar=arr;
						break;
					}
				}
				sc2.close();
				for(int j=0;j<ar.length;j++)
				{
					rew = beta * (1 - ar[j]); 
					if(rew != 0.0 ) rewOthers = beta*(1 - ar[j])/ar.length;
					else rewOthers = 0;
					pun = beta*ar[j]/2; 
					if(pos!=1) punOthers = beta*ar[j]/(2*ar.length); 
					else punOthers = 0;
					if(j==pos)
					{
						ar[j]+=rew;
					}
					else if(ar[j]!=0)ar[j]-=rewOthers;
				}
				saveFileSTM(stt,pos,ar);
				//savefilepres(stt,pos,ar);
			}
			sc1.close();
		}
		else
		{
			while (sc1.hasNextLine()) 
			{
				b=sc1.nextLine();
				Scanner sc = new Scanner(b);
				stt=sc.nextLong();
				pos=sc.nextInt();
				sc.close();
				File file2 =  new File(System.getProperty("user.dir") +"/src/data/game.stm");
				Scanner sc2 = new Scanner(file2);
				while (sc2.hasNextLine()) 
				{
					c=sc2.nextLine();
					Scanner ss = new Scanner(c);
					tmp=ss.nextLong();
					tmp1=(int)ss.nextDouble();
					double arr[] = new double[100];
					int i=0;
					while (ss.hasNext()) 
					{
						double k=ss.nextDouble();
						arr[i]=k;
						i++;
					}
					ss.close();
					if(tmp==stt)
					{
						ar=arr;
						break;
					}
				}
				sc2.close();
				for(int j=0;j<ar.length;j++)
				{
					rew = beta * (1 - ar[j]); 
					if(rew != 0.0 ) rewOthers = beta*(1 - ar[j])/ar.length;
					else rewOthers = 0;
					pun = beta*ar[j]/2; 
					if(pos!=1) punOthers = beta*ar[j]/(2*ar.length); 
					else punOthers = 0;
					if(j==pos)
					{
						ar[j]-=pun;
					}
					else if(ar[j]!=0)ar[j]+=punOthers;
				}
				saveFileSTM(stt,pos,ar);
				//savefilepres(stt,pos,ar);
			}
			sc1.close();
		}
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

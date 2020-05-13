 package ga; 
//***************************************************************/ 
  /*   Program Name:     NN for the Reversi game                 */ 
  /*                                                             */ 
  /*   Author Cuong Nguyen                                       */ 
  /*                                                             */ 
  /*                                                             */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;

import javax.swing.*;
 
import objects.*;
public class NNRD extends JFrame 
	{   double[] iA; 
	    HiddenNeuron[] hn;
	    OutputNeuron[] on;
	    double[] oA;
	   

	   JTextField[] inputAxon= new JTextField[64];  //input axon
	   JTextField[] hiddenAxon= new JTextField[32]; ;
	   
	   JTextField[] outputAxon = new JTextField[64]; 
	   JTextField[] outputAxonT = new JTextField[64]; 
	   JRadioButton[] inputNeuron = new JRadioButton[64];  
	   JRadioButton[] hiddenNeuron= new JRadioButton[32];;
	   JRadioButton[] outputNeuron= new JRadioButton[64];;
	   public Color     TCUColors   = new Color(77,25,121);
	 
	   boolean verbose = true;
	   public DecimalFormat decimal = new DecimalFormat("###,###.####");
	   public JPanel nn = new JPanel(new GridLayout(65,7));
        
		
		public static void main(String args[])
		  {
		    // Construct the frame
		    new NNRD();
		  }
		public NNRD(double[] iAfm,HiddenNeuron[] hnfm, OutputNeuron[] onfm, double[] oAfm) 
		  {   setTitle("Neural Network for Reversi Game");
		      setFont(new Font("Helvetica",Font.BOLD,7));
		      iA = iAfm;
		      hn = hnfm;
		      on = onfm;
		      oA = oAfm;
		      displayNet();
		      populateNet();
		      setBounds(550,450,500,975);
			  setVisible(true); 
			  validate();
			  System.out.println( "here");
			 
		   }
         public NNRD()
		  {   setTitle("Neural Network for Reversi Game");
		      setFont(new Font("Helvetica",Font.BOLD,7));
		   
		      displayNet();
		      
		      setBounds(550,450,500,975);
			  setVisible(true); 
			  validate();
			  System.out.println( "here");
			 
		   }
         
        public void displayNet() 
        { for ( int i = 0; i< 64; i++)
        	{inputAxon[i] = new JTextField();
             outputAxon[i] = new JTextField();
             outputAxonT[i] = new JTextField();
             if(i==0) inputNeuron[i] = new JRadioButton("A1");
             if(i==0) outputNeuron[i] = new JRadioButton("A1");
             if(i==1) inputNeuron[i] = new JRadioButton("A2");
             if(i==1) outputNeuron[i] = new JRadioButton("A2");
             if(i==2) inputNeuron[i] = new JRadioButton("A3");
             if(i==2) outputNeuron[i] = new JRadioButton("A3");
             if(i==3) inputNeuron[i] = new JRadioButton("A4");
             if(i==3) outputNeuron[i] = new JRadioButton("A4");
             if(i==4) inputNeuron[i] = new JRadioButton("A5");
             if(i==4) outputNeuron[i] = new JRadioButton("A5");
             if(i==5) inputNeuron[i] = new JRadioButton("A6");
             if(i==5) outputNeuron[i] = new JRadioButton("A6");
             if(i==6) inputNeuron[i] = new JRadioButton("A7");
             if(i==6) outputNeuron[i] = new JRadioButton("A7");
             if(i==7) inputNeuron[i] = new JRadioButton("A8");
             if(i==7) outputNeuron[i] = new JRadioButton("A8");
             if(i==8) inputNeuron[i] = new JRadioButton("B1");
             if(i==8) outputNeuron[i] = new JRadioButton("B1");
             if(i==9) inputNeuron[i] = new JRadioButton("B2");
             if(i==9) outputNeuron[i] = new JRadioButton("B2");
             if(i==10) inputNeuron[i] = new JRadioButton("B3");
             if(i==10) outputNeuron[i] = new JRadioButton("B3");
             if(i==11) inputNeuron[i] = new JRadioButton("B4");
             if(i==11) outputNeuron[i] = new JRadioButton("B4");
             if(i==12) inputNeuron[i] = new JRadioButton("B5");
             if(i==12) outputNeuron[i] = new JRadioButton("B5");
             if(i==13) inputNeuron[i] = new JRadioButton("B6");
             if(i==13) outputNeuron[i] = new JRadioButton("B6");
             if(i==14) inputNeuron[i] = new JRadioButton("B7");
             if(i==14) outputNeuron[i] = new JRadioButton("B7");
             if(i==15) inputNeuron[i] = new JRadioButton("B8");
             if(i==15) outputNeuron[i] = new JRadioButton("B8");
             if(i==16) inputNeuron[i] = new JRadioButton("C1");
             if(i==16) outputNeuron[i] = new JRadioButton("C1");
             if(i==17) inputNeuron[i] = new JRadioButton("C2");
             if(i==17) outputNeuron[i] = new JRadioButton("C2");
             if(i==18) inputNeuron[i] = new JRadioButton("C3");
             if(i==18) outputNeuron[i] = new JRadioButton("C3");
             if(i==19) inputNeuron[i] = new JRadioButton("C4");
             if(i==19) outputNeuron[i] = new JRadioButton("C4");
             if(i==20) inputNeuron[i] = new JRadioButton("C5");
             if(i==20) outputNeuron[i] = new JRadioButton("C5");
             if(i==21) inputNeuron[i] = new JRadioButton("C6");
             if(i==21) outputNeuron[i] = new JRadioButton("C6");
             if(i==22) inputNeuron[i] = new JRadioButton("C7");
             if(i==22) outputNeuron[i] = new JRadioButton("C7");
             if(i==23) inputNeuron[i] = new JRadioButton("C8");
             if(i==23) outputNeuron[i] = new JRadioButton("C8");
             if(i==24) inputNeuron[i] = new JRadioButton("D1");
             if(i==24) outputNeuron[i] = new JRadioButton("D1");
             if(i==25) inputNeuron[i] = new JRadioButton("D2");
             if(i==25) outputNeuron[i] = new JRadioButton("D2");
             if(i==26) inputNeuron[i] = new JRadioButton("D3");
             if(i==26) outputNeuron[i] = new JRadioButton("D3");
             if(i==27) inputNeuron[i] = new JRadioButton("D4");
             if(i==27) outputNeuron[i] = new JRadioButton("D4");
             if(i==28) inputNeuron[i] = new JRadioButton("D5");
             if(i==28) outputNeuron[i] = new JRadioButton("D5");
             if(i==29) inputNeuron[i] = new JRadioButton("D6");
             if(i==29) outputNeuron[i] = new JRadioButton("D6");
             if(i==30) inputNeuron[i] = new JRadioButton("D7");
             if(i==30) outputNeuron[i] = new JRadioButton("D7");
             if(i==31) inputNeuron[i] = new JRadioButton("D8");
             if(i==31) outputNeuron[i] = new JRadioButton("D8");
             if(i==32) inputNeuron[i] = new JRadioButton("E1");
             if(i==32) outputNeuron[i] = new JRadioButton("E1");
             if(i==33) inputNeuron[i] = new JRadioButton("E2");
             if(i==33) outputNeuron[i] = new JRadioButton("E2");
             if(i==34) inputNeuron[i] = new JRadioButton("E3");
             if(i==34) outputNeuron[i] = new JRadioButton("E3");
             if(i==35) inputNeuron[i] = new JRadioButton("E4");
             if(i==35) outputNeuron[i] = new JRadioButton("E4");
             if(i==36) inputNeuron[i] = new JRadioButton("E5");
             if(i==36) outputNeuron[i] = new JRadioButton("E5");
             if(i==37) inputNeuron[i] = new JRadioButton("E6");
             if(i==37) outputNeuron[i] = new JRadioButton("E6");
             if(i==38) inputNeuron[i] = new JRadioButton("E7");
             if(i==38) outputNeuron[i] = new JRadioButton("E7");
             if(i==39) inputNeuron[i] = new JRadioButton("E8");
             if(i==39) outputNeuron[i] = new JRadioButton("E8");
             if(i==40) inputNeuron[i] = new JRadioButton("F1");
             if(i==40) outputNeuron[i] = new JRadioButton("F1");
             if(i==41) inputNeuron[i] = new JRadioButton("F2");
             if(i==41) outputNeuron[i] = new JRadioButton("F2");
             if(i==42) inputNeuron[i] = new JRadioButton("F3");
             if(i==42) outputNeuron[i] = new JRadioButton("F3");
             if(i==43) inputNeuron[i] = new JRadioButton("F4");
             if(i==43) outputNeuron[i] = new JRadioButton("F4");
             if(i==44) inputNeuron[i] = new JRadioButton("F5");
             if(i==44) outputNeuron[i] = new JRadioButton("F5");
             if(i==45) inputNeuron[i] = new JRadioButton("F6");
             if(i==45) outputNeuron[i] = new JRadioButton("F6");
             if(i==46) inputNeuron[i] = new JRadioButton("F7");
             if(i==46) outputNeuron[i] = new JRadioButton("F7");
             if(i==47) inputNeuron[i] = new JRadioButton("F8");
             if(i==47) outputNeuron[i] = new JRadioButton("F8");
             if(i==48) inputNeuron[i] = new JRadioButton("G1");
             if(i==48) outputNeuron[i] = new JRadioButton("G1");
             if(i==49) inputNeuron[i] = new JRadioButton("G2");
             if(i==49) outputNeuron[i] = new JRadioButton("G2");
             if(i==50) inputNeuron[i] = new JRadioButton("G3");
             if(i==50) outputNeuron[i] = new JRadioButton("G3");
             if(i==51) inputNeuron[i] = new JRadioButton("G4");
             if(i==51) outputNeuron[i] = new JRadioButton("G4");
             if(i==52) inputNeuron[i] = new JRadioButton("G5");
             if(i==52) outputNeuron[i] = new JRadioButton("G5");
             if(i==53) inputNeuron[i] = new JRadioButton("G6");
             if(i==53) outputNeuron[i] = new JRadioButton("G6");
             if(i==54) inputNeuron[i] = new JRadioButton("G7");
             if(i==54) outputNeuron[i] = new JRadioButton("G7");
             if(i==55) inputNeuron[i] = new JRadioButton("G8");
             if(i==55) outputNeuron[i] = new JRadioButton("G8");
             if(i==56) inputNeuron[i] = new JRadioButton("H1");
             if(i==56) outputNeuron[i] = new JRadioButton("H1");
             if(i==57) inputNeuron[i] = new JRadioButton("H2");
             if(i==57) outputNeuron[i] = new JRadioButton("H2");
             if(i==58) inputNeuron[i] = new JRadioButton("H3");
             if(i==58) outputNeuron[i] = new JRadioButton("H3");
             if(i==59) inputNeuron[i] = new JRadioButton("H4");
             if(i==59) outputNeuron[i] = new JRadioButton("H4");
             if(i==60) inputNeuron[i] = new JRadioButton("H5");
             if(i==60) outputNeuron[i] = new JRadioButton("H5");
             if(i==61) inputNeuron[i] = new JRadioButton("H6");
             if(i==61) outputNeuron[i] = new JRadioButton("H6");
             if(i==62) inputNeuron[i] = new JRadioButton("H7");
             if(i==62) outputNeuron[i] = new JRadioButton("H7");
             if(i==63) inputNeuron[i] = new JRadioButton("H8");
             if(i==63) outputNeuron[i] = new JRadioButton("H8");
             
             inputNeuron[i].setForeground(Color.black);
             outputNeuron[i].setForeground(Color.black);
             
             
              }
        for ( int i = 0; i< 32; i++)
    	{hiddenAxon[i] = new JTextField("");
         if(i==0) hiddenNeuron[i] = new JRadioButton("h 0");
         if(i==1) hiddenNeuron[i] = new JRadioButton("h 1");
         if(i==2) hiddenNeuron[i] = new JRadioButton("h 2");
         if(i==3) hiddenNeuron[i] = new JRadioButton("h 3");
         if(i==4) hiddenNeuron[i] = new JRadioButton("h 4");
         if(i==5) hiddenNeuron[i] = new JRadioButton("h 5");
         if(i==6) hiddenNeuron[i] = new JRadioButton("h 6");
         if(i==7) hiddenNeuron[i] = new JRadioButton("h 7");
         if(i==8) hiddenNeuron[i] = new JRadioButton("h 8");
         if(i==9) hiddenNeuron[i] = new JRadioButton("h 9");
         if(i==10) hiddenNeuron[i] = new JRadioButton("h 10");
         if(i==11) hiddenNeuron[i] = new JRadioButton("h 11");
         if(i==12) hiddenNeuron[i] = new JRadioButton("h 12");
         if(i==13) hiddenNeuron[i] = new JRadioButton("h 13");
         if(i==14) hiddenNeuron[i] = new JRadioButton("h 14");
         if(i==15) hiddenNeuron[i] = new JRadioButton("h 15");
         if(i==16) hiddenNeuron[i] = new JRadioButton("h 16");
         if(i==17) hiddenNeuron[i] = new JRadioButton("h 17");
         if(i==18) hiddenNeuron[i] = new JRadioButton("h 18");
         if(i==19) hiddenNeuron[i] = new JRadioButton("h 19");
         if(i==20) hiddenNeuron[i] = new JRadioButton("h 20");
         if(i==21) hiddenNeuron[i] = new JRadioButton("h 21");
         if(i==22) hiddenNeuron[i] = new JRadioButton("h 22");
         if(i==23) hiddenNeuron[i] = new JRadioButton("h 23");
         if(i==24) hiddenNeuron[i] = new JRadioButton("h 24");
         if(i==25) hiddenNeuron[i] = new JRadioButton("h 25");
         if(i==26) hiddenNeuron[i] = new JRadioButton("h 26");
         if(i==27) hiddenNeuron[i] = new JRadioButton("h 27");
         if(i==28) hiddenNeuron[i] = new JRadioButton("h 28");
         if(i==29) hiddenNeuron[i] = new JRadioButton("h 29");
         if(i==30) hiddenNeuron[i] = new JRadioButton("h 30");
         if(i==31) hiddenNeuron[i] = new JRadioButton("h 31");
         hiddenNeuron[i].setForeground(Color.black);
         }
         nn.setForeground(Color.black);
         nn.setBackground(TCUColors);
         add(nn); 
         JLabel li = new JLabel("Input");JLabel lh = new JLabel("Hidden");JLabel lo = new JLabel("Output");
         
         JLabel vA = new JLabel("True");
         
         li.setForeground(Color.WHITE);lh.setForeground(Color.WHITE);lo.setForeground(Color.WHITE);vA.setForeground(Color.WHITE);
         nn.add(li); nn.add(new JLabel("")); nn.add(lh);
         nn.add(new JLabel("")); nn.add(lo); nn.add(new JLabel(""));nn.add(vA);
         for ( int i = 0; i< 64; i++)
        	     
         {      
        	     if(i>31 ) 
        		 {nn.add(inputAxon[i]);nn.add(inputNeuron[i]); nn.add(new JLabel("")); nn.add(new JLabel(""));
        		  nn.add(outputAxon[i]);nn.add(outputNeuron[i]); nn.add(outputAxonT[i]);}
        		 else { nn.add(inputAxon[i]); nn.add(inputNeuron[i]); 
        		        nn.add(hiddenAxon[i]); nn.add(hiddenNeuron[i]);
        		        nn.add(outputAxon[i]);nn.add(outputNeuron[i]); nn.add(outputAxonT[i]);
        		 }
         }
         
        }
        
        
    public void populateNet() 
        { for ( int i = 0; i< 64; i++)
        	{inputAxon[i].setText(""+iA[i]); 
        	System.out.println(""+iA[i]);
        	 outputAxonT[i].setText(""+oA[i]); 
             outputAxon[i].setText(decimal.format(on[i].axonValue));
             }
        for ( int i = 0; i< 32; i++) 
        	  {if(hn[i].axon==1) hiddenNeuron[i].setSelected(true);
        	   hiddenAxon[i].setText(decimal.format(hn[i].axonValue));
        	  }
        for ( int i = 0; i< 64; i++)
    	 {
          if(iA[i]==1) inputNeuron[i].setSelected(true);
          if(oA[i]==1) outputNeuron[i].setSelected(true);
         
	     }
	   }
        
	}

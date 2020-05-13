package objects;

import java.io.Serializable;

public class NeuralPattern implements Serializable{
public String inputPattern="";
public String outputPattern="";
public char[] inputNeuron;
public int[] inputAxon;
public char[] outputNeuron;
public int[] outputAxon;

public int nStates;
public NeuralPattern(String ip, String op)
{ inputPattern = ip; outputPattern = op;
   inputNeuron = new char[64];
     outputNeuron = new char[64];
     inputAxon  = new int[64];
     outputAxon  = new int[64];
     for (int i=0; i < inputNeuron.length; i++) {
    	 inputNeuron[i] = '*'; outputNeuron[i] = '*';
    	 inputAxon[i] = 0; outputAxon[i] = 0;
     }
     inpat(ip);
     outpat(op);
     //inpat(ip.charAt(0));inpat(ip.charAt(1));inpat(ip.charAt(2));inpat(ip.charAt(3));
     //outpat(op.charAt(0));outpat(op.charAt(1));outpat(op.charAt(2));outpat(op.charAt(3));
    
     
   }
public void inpat(String r)
{  
	for(int i=0;i<r.length();i++)
	{
		 if(r.charAt(i)=='*')
		 {
		 	inputNeuron[i]='*';  
		 	inputAxon[i]=0;
		 }
		 else if(r.charAt(i)=='X')
		 {
		 	inputNeuron[i]='X';  
		 	inputAxon[i]=1;
		 }
		 else
		 {
			 inputNeuron[i]='0';  
			 inputAxon[i]=1;
		 }
	}
}

public void outpat(String r)
{  
	for(int i=0;i<r.length();i++)
	{
		 if(r.charAt(i)=='*')
		 {
		 	outputNeuron[i]='*';  
		 	outputAxon[i]=0;
		 }
		 else if(r.charAt(i)=='X')
		 {
		 	outputNeuron[i]='X';  
		 	outputAxon[i]=1;
		 }
		 else
		 {
			 outputNeuron[i]='0';  
			 outputAxon[i]=1;
		 }
	}
}
}



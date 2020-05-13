package objects;

import java.io.Serializable;

public class OutputNeuron implements Serializable{
public double[] den = new double[33];
public double th  = Math.random();
public double axonValue;
public int axon;

public OutputNeuron()
   { for(int i = 0; i< 33; i++) den[i]  = Math.random();
   }
public OutputNeuron(int n)
{ for(int i = 0; i< n; i++) den[i]  = Math.random();
}
}

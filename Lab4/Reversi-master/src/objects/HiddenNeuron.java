package objects;

import java.io.Serializable;

public class HiddenNeuron implements Serializable{
public double[] den = new double[64];
public double th = Math.random();
public double axonValue;
public int axon;

public HiddenNeuron()
   {  for(int i = 0; i< 64; i++) den[i]  = Math.random();
   }

public HiddenNeuron(int n)
{  for(int i = 0; i< n; i++) den[i]  = Math.random();
}
}

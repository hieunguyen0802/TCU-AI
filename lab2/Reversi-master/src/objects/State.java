package objects;

import java.io.Serializable;

public class State implements Serializable{
public String[] state;
public double[] prob;
public int nStates;
public State()
   { state = new String[64];prob =  new double[64];
   }
}

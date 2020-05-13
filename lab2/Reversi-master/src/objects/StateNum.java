package objects;

import java.io.Serializable;

public class StateNum implements Serializable{
private int idState;
private String state;
private String description;
public StateNum() {}
public int getId() {return idState; }
public void setId(int n) {  idState = n; }
public String getState() {return state; }
public void setState(String n) {  state = n; }
public String getDescription() {return description; }
public void setDescription(String n) {  description = n; }
  
}

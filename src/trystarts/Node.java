package trystarts;

import java.util.ArrayList;


public class Node implements Comparable<Node>, Cloneable {
	
    int no;
    int color;
	float x,y
	  ,z
	  ;
		
	boolean visi=false;
	boolean colored=false;
	ArrayList <Node> nb= new  ArrayList<Node>();	
	
	int oriDegree;
	int degDeleted;

	ArrayList <Integer> nnb= new ArrayList<Integer>();

	  //Node Constructor
	  Node(float myX, float myY
	   ,float myZ
	  ){
	    super();
	   x=myX;
	   y=myY;
	   z=myZ;
	  }
	  

	  public Node(int xc, int yc) {
		  super();
		  x=xc;
		  y=yc;
		  
	}


	public int getDegree(){
			
			return nb.size();
		}
	  
	  public float getX(){
		   return x; 
		  }
	  public float getY(){
		   return y; 
		  }
	  public float getZ(){
		   return z; 
		  }
	  public void setNo(int i){
		  this.no=i;
	  }
	  

	@Override
	public int compareTo(Node compareX) {
		// TODO Auto-generated method stub
		float compX=(compareX.getX()); 
	    return (int) ((int)this.x-compX);
	}
	@Override
	public String toString(){
		String number;
		number= "Node "+no;
		return number;
		
	}

}

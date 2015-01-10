package trystarts;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Queue;
import java.util.Set;

import processing.core.PApplet;


public class Trystarts extends PApplet {
	
	float rotY = TWO_PI/25;	
	int radius=250;

	float R=(float) 0.11;
	float Areas=1/R;
	
	int n=80000;
	int dimension=3;
	int edges=0;
    
	boolean picline=false;
	boolean picpoint=false;
	boolean highlightdegree=false;
	boolean highlightmaxGroupmin=false;
	
	boolean backbone=false;
	boolean showslo=false;
	boolean writedata= true;
	boolean drawcolor= false;
	boolean highlightcolor=false;
	boolean hemisphere =false;
	boolean maxGroupcomponent= false;
	boolean multiplebb=false;
	
	boolean c=true;
	
	
	ArrayList<Node> nodes;
	ArrayList<Node> nodes2;
	int pivots;
	int [] pivotx;
	int [] arrayPivots;
	NullPointerException ne;
	ArrayList <LinkedList> degreelist ;
	Node[] arraySlo;
	
	public static ArrayList<Node> order = new ArrayList();
	static Queue<Node> q= new LinkedList<Node>();
	static Set set = new HashSet();
	static Set set2 = new HashSet();
	static int bee=0;
	
	public void setup() {
/*		if(c==true){
			backbone=true;
			drawcolor=true;
			writedata=false;
		}
		else{
			backbone=false;
			drawcolor=false;
			writedata=true;
			
		}
		*/
		background(0,20,20);
	//	stroke(10,10,80);
		stroke(20,204,215);
		size(600,600,P3D);
		frameRate(25);
	

// Part A. create nodes and connect lines for them 
		
// 1. generate nodes for display		
	//	randomSeed(0);
		if(dimension!=2){
			if(picpoint==false)
				nodes=create3Dnodes(n,radius,0);
			else{
		    	nodes=create3Dnodes(n,radius,1);
		     	display3Dnodes(nodes);
			}

		}
		else{
			if(picpoint==false){
				nodes=create2Dnodes(n,0);        // argument: number of nodes, 1 for display
			}
			else{
				nodes=create2Dnodes(n,1); 
				display2Dnodes(nodes);

			}
	       System.out.println("2d create nodes excuted");
		}
		nodes2=nodes;                    // used to color the nodes
		
		saveFrame("nodes-#.png");

	    
	    
// 2. generate the x coordinates for the pivots and store them in an array
		if(dimension==2)
	    arrayPivots=twoDpivots(R);
	//	  for(int i=0;i<a.length;i++){
		//	  System.out.print(a[i]+" ");
		 // }
		else
			arrayPivots=threeDpivots(R,radius);     //proven no problem, with R and radius
	
// 3. given the x coordinates of pivots, output the indices of nodes that are the last nodes in each area		
		int[] pivotx=getpivotnodes(arrayPivots,nodes);		
	
    	
// 4. with the last nodes in each area, connect them and display optionally		
/*  	for(int pivotxi=0;pivotxi<pivotx.length;pivotxi++){
  		System.out.print(pivotx[pivotxi]+" ");
  		
  	}
  */	
		if(dimension==2){
			if(picline)
				connect2Dnodes(pivotx,nodes,arrayPivots,1);
			else
				connect2Dnodes(pivotx,nodes,arrayPivots,0);
		}
		else{
			if(picline){
				System.out.println("picline "+picline);
				connect3Dnodes(pivotx,nodes,arrayPivots,1);
		}
			else
				connect3Dnodes(pivotx,nodes,arrayPivots,0);
		}
	
		
		saveFrame("oline-##.png");
//	averagedegree(nodes);
		getoriginaldegrees(nodes2);
	
// 5. sort the nodes according to the degrees they have so far and highlight the node with maxGroupimum and minimum degree	
	
//	Collections.shuffle(nodes);
	
		Collections.sort(nodes, degreecomparator.getInstance());
	//display3Dnodes(nodes);
		if(highlightdegree==true)
			highlight(nodes);    
//	testempNbstext(nodes);
		if(highlightmaxGroupmin)
			textnotes();

		saveFrame("ohiline-##.png");
	
	
//Part B. Remove nodes using smallest last ordering	
	   
// 1. create a degree list	      	   
		
//		System.out.println("the size is "+createDegreelist(nodes).size());	

		degreelist = createDegreelist(nodes);
				 
		int[] d= degreedist(degreelist);
		 
		 
// 2.----- find the node with the least degree and then remove, along with changing the degree of their neighbors 
		 

		int[] degreesdeleted= new int[n];
		 if(writedata==true){
			 arraySlo=slo(degreelist,nodes,degreesdeleted);
	//	 displayremaining(s);

		}

	/*	for(int j=0;j<s.length;j++){
		System.out.println(s[j]);
		}
		System.out.println(degreelist);
	*/	
	
	
	
//Part C. Color the nodes.

	
// 1. For each node, get its color and put them into colorarray, index represents the index in nodes2, content represents color number
	
		 int[] colorarray=new int[n];
	
	// at the same time create a colorlist for backbone use. 
		 int colorsize= degreelist.size();		 
		 ArrayList <LinkedList> colorlist = new ArrayList<LinkedList>(colorsize);
		 

		
		 if(drawcolor==true)
		 colorarray= coloring(nodes2, colorlist);
		 plotcolor(colorlist);
	//	 for(int nc=0;nc<colorlist.size();nc++){
	//		 System.out.println("color "+nc+": "+colorlist.get(nc));
	 
	//	 }
	 
// 2. Get the color rgb for each color, first index represents the color, second index represents r,g,b	
		 
		 
		 float[][] cls= givecolors(colorarray);
// 3. With the rgb, draw each node
		 
	//	 if(drawcolor==true){	 
	//		if(dimension==2)
    // 	 display2Dcolornodes(nodes2,cls);
	//		else 
	//	 display3Dcolornodes(nodes2,cls);
//	 }	 
		 
//Part D. Plot the degrees when deleted and original degrees
	//	 Collections.sort(nodes2);
		 
		 
		if(writedata==true) 
   
		plot(d,arraySlo,degreesdeleted);

	 
//Part E. Backbones and display
		if (backbone==true){
// 1. create the backbone nodes in ArrayList 		 		 		 
		  ArrayList<Node> bb1=choosebackbone(1,colorlist);  // the integer represents 1 of 6 backbone selections 
	//	 System.out.println("bb1 "+bb1);
		 //get the number of colors
		  int ncolor= getnc (colorlist);
		 

// 2. draw the edges as backbones	
		 
		  int BE;
		  if(dimension==2)
			  BE= drawbackbone(bb1)/2;	 
		  else 
			  BE= draw3backbone(bb1)/2;
		
		
		  if(multiplebb==true)
			  showsets(colorlist);
		 		 
// 3. get the maxGroup color class 1 size and class 2 size, then largest component size		 
		 int mc1=colorlist.get(1).size();
		 int mc2=colorlist.get(2).size();
		 System.out.println("NC="+ncolor+", MC1="+mc1+", MC2="+mc2+", LC="+(mc1+mc2));
// 4. count the coverage of backbones 
		 
		 double cov=coverage(colorlist.get(1),colorlist.get(2),n);
		 System.out.println("coverage is "+cov);
		 
		 
		 if(maxGroupcomponent==true){
			 ArrayList<Node> maxGroup= maxGroupconnect(colorlist.get(1),colorlist.get(2));
			 System.out.println(maxGroup);
			
			 System.out.println("maxGroup size is "+maxGroup.size()+", coverage is "+(double)((double)maxGroup.size()/(double)(mc1+mc2)));
			 System.out.println("be is "+bee/2);
		 }
		}
		 
}

    

	int looptimes= n;
	int j=1;

	public void draw() {		
	/*	
		if(j<=looptimes){
			for(int i=0;i<j;i++){
			//	System.out.println(s[i].x+" "+s[i].y);
				point(s[i].x,s[i].y);
				
				
				for(int k=0;k<i;k++){
					if(distance2(s[i],s[k])<height*height*R*R){
						stroke(155,155,0);
						line(s[i].x,s[i].y,s[k].x,s[k].y);
					}
				}
				
		
				if(s[i].x<15){
					text(s[i].no,s[i].x+2,s[i].y+8);
				}
				else{
				text(s[i].no, s[i].x-6,s[i].y+1);
				}
			}
		
			
		}
		else{
			noLoop();
		}
	//	System.out.println("----");
		saveFrame("pic-#####.png");
			j++;
	*/
	}
    
    
    // create the nodes in the arraylist, if we want to display, then display=1.	
	public ArrayList<Node> create2Dnodes(int n,int display){

	    	ArrayList<Node> nodes= new ArrayList <Node>();
	    	randomSeed(0); 
	    	for(int i=0;i<n;i++){ 
	    	int x=(int)(random(0,height));
	    	int y=(int)(random(0,width));
	    	 nodes.add(new Node(x,y));
	    	 if (display==1)
	    	   point(x,y);    	 
	    	 }
	  	  Collections.sort(nodes);
	  	  //give the nodes' identifiers low to high according to their x coordinates.
	  	  for(int i=0;i<n;i++){
	  		  ( nodes.get(i)).setNo(i);
	  	  }
		return nodes;
	    }
    // create the nodes in the arraylist, if we want to display, then display=1.
	public ArrayList<Node> create3Dnodes(int n,int ra,int display){

    	ArrayList<Node> nodes= new ArrayList <Node>();
    	randomSeed(0); 
    	float myX,myY,myZ;
    	translate(width/2,height/2);
    	for(int i=0;i<n;i++){ 

    	float u=random(-1,1);
   	//    float angleA = random(0, PI);
   	    float angleB = random(0, TWO_PI);
   	      
   	    myX = (float) (ra*sqrt(1-u*u)*cos(angleB));
   	    myY = (float)(ra*sqrt(1-u*u)*sin(angleB));
   	    myZ = (float) (ra*u);
   	    
   	    nodes.add (new Node (myX, myY, myZ));
    	 if (display==1)
    	   point(myX,myY,myZ);      	 
	 
    	 }
  	  Collections.sort(nodes);
  	  //give the nodes' identifiers low to high according to their x coordinates.
  	  for(int i=0;i<n;i++){
  		   nodes.get(i).setNo(i);
  	  }
	return nodes;
    }
	public void display2Dnodes( ArrayList <Node> nodes){
		for (int i=0;i<nodes.size();i++){
			point(nodes.get(i).x,nodes.get(i).y);
		}
		
	}
	public void display3Dnodes(ArrayList <Node> nodes){
	//	translate(width/2,height/2,0);
	//	  rotateY(rotY);
		  background(0,30,20);
		//  stroke(100,205,215);
		  
		  
		  
		for (int i=0;i<nodes.size();i++){
			if(hemisphere==true){
				if(nodes.get(i).z>0){
					point(nodes.get(i).x,nodes.get(i).y,nodes.get(i).z);
				}
			}
			else{
				point(nodes.get(i).x,nodes.get(i).y,nodes.get(i).z);
			}
		}
	//	  rotY = rotY + TWO_PI/250;

	}
	
		
	
	//In 2D environment, create an array of pivots, recording the x coordinates for the lines.
	public int[] twoDpivots(float R){
		
		float Areas=1/R;
	
		if(floor(Areas)<Areas){       //if 1 is not divisible by R, then we need (Areas+1) pivots;
			System.out.println("Not divisible");
			pivots=floor(Areas);   
			
			int[] coordOfPivots = new int[pivots+1];    //create an array for different pivots and their x coordinate
			int range= (int) (height*R);     //within which range we need to constrain the nodes to compare from each other
			
			coordOfPivots[0]=0;
			coordOfPivots[1]=width%range;
			for(int i=2;i<pivots+1;i++)
				coordOfPivots[i]=coordOfPivots[i-1]+range;
			
			return coordOfPivots;
				
		}
		else{
			System.out.println("Divisible");
			pivots= (int) (Areas-1);   // if 1 is divisible by R, then we need (Areas-1) pivots;
			
			int[] coordOfPivots = new int[pivots+1];    //create an array for different pivots and their x coordinate
			int range= floor(width/(pivots+1));     //within which range we need to constrain the nodes to compare from each other
			
			coordOfPivots[0]=0;
			coordOfPivots[1]=range;
			for(int i=2;i<pivots+1;i++)
				coordOfPivots[i]=coordOfPivots[i-1]+range;
			
			return coordOfPivots;
		}			
		
	}
	
	//In 3D environment, create an array of pivots, recording the x coordinates for the lines.
	public int[] threeDpivots(float R, int radius){
		
		float Areas=1*2/R;		
		if(floor(Areas)<Areas)  { 
		    pivots=floor(Areas)+1;  //if 1 is not divisible by R
		    int[] coordOfPivots = new int[pivots]; 
		    int range= floor(radius*2/pivots);     //within which range we need to constrain the nodes to compare from each other
		
		    int startingline = width/2-radius;    //where those areas actually start, the boundary coordinate for the sphere
		    coordOfPivots[0]=startingline;
		    coordOfPivots[1]=startingline+width%pivots;
		    for(int i=1;i<pivots;i++)
		    	coordOfPivots[i]=coordOfPivots[i-1]+range;
		
		    return coordOfPivots;
		}
		
		
		else{     // if 1 is divisible by R
			pivots = (int) (Areas);
			int[] coordOfPivots = new int[pivots];
			int range = radius*2/(pivots+1);			
			int startingline = width/2-radius;    //where those areas actually start, the boundary coordinate for the sphere	
			coordOfPivots[0]=startingline;
			coordOfPivots[1]=startingline+range;
	
			for(int i=2;i<pivots;i++)
				coordOfPivots[i]=coordOfPivots[i-1]+range;
			
			return coordOfPivots;
		}
					
	}

	// input the array of coordinates for pivots, and the nodes to process
	public int[] getpivotnodes (int [] c, ArrayList <Node> nodes){
		
		Node t;
		// an array to store the maxGroupimum x-coordinate node for each area i, denoted by nmaxGroup[i]
		
		int [] nmaxGroup= new int[c.length];		
		
//		System.out.println("c.length is "+c.length);
	outer:for(int i=0;i<nodes.size();i++){
			
		 t=nodes.get(i);
		 
	inner: for(int j=1;j<c.length;j++){
		 if(t.x<=c[j]){
			// System.out.println(t.x);
			 nmaxGroup[j]=t.no;
		//	 System.out.println("j is "+j+" nmaxGroup[j] is "+nmaxGroup[j]);
		 	}
		 else{
		//	 System.out.println("t.x is "+t.x+" continue");
			 continue ;
		 	}
		 }
		
		}
		return nmaxGroup;
	}
	public void testPivotIndex(int[] pivotx, int[] a){  //pivotx stands for the x coordinates for pivots, a stands for the indices for those nodes
		for(int i=0;i<pivotx.length;i++){
		//	System.out.println("pivotx length is "+pivotx.length);
			System.out.println("pivotx[i] is "+pivotx[i]+" a[i] is "+a[i]);
			point(nodes.get(pivotx[i]).x,nodes.get(pivotx[i]).y);
			ellipseMode(CENTER);
			fill(100,100,100);  // Set fill to gray
			ellipse(nodes.get(pivotx[i]).x,nodes.get(pivotx[i]).y, 5, 5); 
			// Draw gray ellipse using CENTER mode
		}
		
	}
	public void testwithxlines(int[] a){ // create lines to see if the pivots are placed correctly
			for(int i=0;i<a.length;i++){
				line(a[i],0,a[i],height);
				}
			}
	
	public void connect2Dnodes(int[]nmaxGroup, ArrayList <Node> nodes,int [] twoDpivots, int display){
		
		int range=twoDpivots[2]-twoDpivots[1];
		System.out.println("range "+range);
		for(int k=1;k<nmaxGroup.length-1;k++){   //iterate through these pivots
			
	    	for(int i=nmaxGroup[k-1];i<nmaxGroup[k];i++){      // choose from a starting index
			  for(int j=i+1;j<nmaxGroup[k+1];j++){
				 				  
				  if(distance2(nodes.get(i),nodes.get(j))<range*range){
					 
						nodes.get(i).nb.add(nodes.get(j));
						nodes.get(j).nb.add(nodes.get(i));
						 
						edges++; 
					  if(display==1)	
					//	  stroke(80,112,240);
					   line(nodes.get(i).x,nodes.get(i).y,nodes.get(i).z,nodes.get(j).x,nodes.get(j).y,nodes.get(j).z);
				  
				  }
			  }
	    	}
			
		}
		
	    for(int i=nmaxGroup[nmaxGroup.length-2];i< nodes.size();i++ ){
		
		   for(int j=i+1;j< nodes.size();j++ ){
			
			if(distance2(nodes.get(i),nodes.get(j))<range*range){
				 
				nodes.get(i).nb.add(nodes.get(j));
				nodes.get(j).nb.add(nodes.get(i));
				edges++; 

				if(display==1)	
				//	stroke(80,112,240);
				  line(nodes.get(i).x,nodes.get(i).y,nodes.get(i).z,nodes.get(j).x,nodes.get(j).y,nodes.get(j).z);
				  
			  }
		   }
		}
		
	}
	public void testempNbstext(ArrayList<Node> nodes){     //test if the neighbors are correctly displayed using number text on each node
		for(int i=0;i<n;i++){
			fill(0,255,255);
			text(nodes.get(i).no,nodes.get(i).x,nodes.get(i).y);
		/*	if(nodes.get(i).nb.size()==0){
				System.out.println("No is "+nodes.get(i).no+" x is "+nodes.get(i).x+" degree is "+nodes.get(i).nb.size());
			}
			else{
				for(int j=0;j<nodes.get(i).nb.size();j++)
			System.out.println("No is "+nodes.get(i).no+" "+" x is "+nodes.get(i).x+" degree is "+nodes.get(i).nb.size()
					+" the nb is "+nodes.get(i).nb.get(j).no );
				}
		*/	}
			
		}
	
	public void connect3Dnodes(int[]nmaxGroup, ArrayList <Node> nodes,int [] twoDpivots, int display){
	//	translate(width/2,height/2,0);
		//  rotateY(rotY);
	//	System.out.println("connect 3d works");
		background(0,30,20);
		stroke(100,205,215);
		int range=twoDpivots[2]-twoDpivots[1];

		for(int k=1;k<nmaxGroup.length-1;k++){   //iterate through these pivots
			
	    	for(int i=nmaxGroup[k-1];i<nmaxGroup[k];i++){      // choose from a starting index
			  for(int j=i+1;j<nmaxGroup[k+1];j++){
				 				  
				  if(distance2(nodes.get(i),nodes.get(j))<range*range){
					  
						nodes.get(i).nb.add(nodes.get(j));
						nodes.get(j).nb.add(nodes.get(i));
					
						edges++;
						
						if(display==1){		
							if(hemisphere==true){
								if(nodes.get(j).z>0){
						  line(nodes.get(i).x,nodes.get(i).y,nodes.get(i).z,nodes.get(j).x,nodes.get(j).y,nodes.get(j).z);
			//					System.out.println("distance is "+sqrt(distance2(nodes.get(i),nodes.get(j)))/radius);
								
								}
							}
							else
							{
								System.out.println("distance is "+sqrt(distance2(nodes.get(i),nodes.get(j)))/radius);
							

								  line(nodes.get(i).x,nodes.get(i).y,nodes.get(i).z,nodes.get(j).x,nodes.get(j).y,nodes.get(j).z);
							}
						}
				  }
			  }
	    	}
			
		}
		
	    for(int i=nmaxGroup[nmaxGroup.length-2];i< nodes.size();i++ ){
		
		   for(int j=i+1;j< nodes.size();j++ ){
			
			if(distance2(nodes.get(i),nodes.get(j))<range*range){
				 
				nodes.get(i).nb.add(nodes.get(j));
				nodes.get(j).nb.add(nodes.get(i));

				if(display==1){		
					if(hemisphere==true){
						if(nodes.get(j).z>0)
				  line(nodes.get(i).x,nodes.get(i).y,nodes.get(i).z,nodes.get(j).x,nodes.get(j).y,nodes.get(j).z);
					}
					else{
						  line(nodes.get(i).x,nodes.get(i).y,nodes.get(i).z,nodes.get(j).x,nodes.get(j).y,nodes.get(j).z);
			
					}
					  
				}
			  }
		   }
		}
		
	//	  rotY = rotY + TWO_PI/250;

	}
	
	public void highlight(ArrayList <Node> nodes){
		   fill(200,200,100,50); 
		  
	//	   ellipse(nodes.get(0).x,nodes.get(0).y,2*R*width,2*R*width);
	//	   ellipse(nodes.get(nodes.size()-1).x,nodes.get(nodes.size()-1).y,2*R*width,2*R*width);
		   stroke(255,0,10);
		   ellipse(nodes.get(0).x,nodes.get(0).y,3,3);
		   
		   stroke(250,240,10);
		   fill(255,240,10);
		   ellipse(nodes.get(nodes.size()-1).x,nodes.get(nodes.size()-1).y,5,5);
		   int k=1;
		   int j=nodes.size()-2;
	/*	   while(nodes.get(k).nb.size()==nodes.get(0).nb.size()){
			   ellipse(nodes.get(k).x,nodes.get(k).y,2*R*width,2*R*width);
			   k++;
		   }
		   while(nodes.get(nodes.size()-1).nb.size()==nodes.get(j).nb.size()){
			   ellipse(nodes.get(j).x,nodes.get(j).y,2*R*width,2*R*width);		   
			   j--;
		   }
		*/   
		   
		   while(nodes.get(k).nb.size()==nodes.get(0).nb.size()){
			   stroke(255,0,10);
			   fill(200,200,100,50); 
			   ellipse(nodes.get(k).x,nodes.get(k).y,3,3);
			   k++;
		   }
		   while(nodes.get(nodes.size()-1).nb.size()==nodes.get(j).nb.size()){
			   System.out.println("j="+j+", "+"degree is"+nodes.get(j).nb.size());
			   fill(255,0,10);
			   ellipse(nodes.get(j).x,nodes.get(j).y,4,4);		   
			   j--;
		   }
			   
		 
		   
		   for(int i=0;i<nodes.get(0).nb.size();i++){
			   stroke(0,200,50);
	//		   line(nodes.get(0).x,nodes.get(0).y,nodes.get(0).nb.get(i).x,nodes.get(0).nb.get(i).y);
			   
		   }
		   
		   for(int i=0;i<nodes.get(nodes.size()-1).nb.size();i++){
			   stroke(0,200,50);
	//		   line(nodes.get(nodes.size()-1).x,nodes.get(nodes.size()-1).y,nodes.get(nodes.size()-1).nb.get(i).x,nodes.get(nodes.size()-1).nb.get(i).y);
			   
		   }
	}

	public void textnotes(){
		fill(255,240,10);
		stroke(255,240,10);
	    ellipse(255,-275,3,3);
		text("MAX",260,-270,0);
		
		fill(255,0,10);
		stroke(255,0,10);
	    ellipse(255,-265,3,3);
		text("MIN",260,-260,0);
		
		
		
	}
	
	public ArrayList<LinkedList> createDegreelist (ArrayList <Node> nodes){   //proven no problem
		int	maxGroupdegree=nodes.get(nodes.size()-1).nb.size();  //how many degrees there are in the nodes
	//	System.out.println(maxGroupdegree);
		
	ArrayList <LinkedList> deglist =new ArrayList (maxGroupdegree+1);	
	for(int i=0;i<maxGroupdegree+1;i++){
		 LinkedList shell =new LinkedList();
		deglist.add(null);   //so the size will be (maxGroupdegree+1);
	}

		for(int i=0;i<nodes.size();i++){
			
			
			
		//	connect2Dnodes(pivotx, nodes, a, 1);
			
			int degreeofi =nodes.get(i).nb.size();
			
		//	System.out.println("the index now is "+i +" and the degree now is "+degreeofi);

			if (degreeofi!=0){ //it it has a degree more than zero
				
				// if there is no list there, create a list and add to the degree-list
				if(deglist.get(degreeofi)==null){  //indicates that it does not have any node here
					LinkedList <Node> deg= new LinkedList <Node>();
				
				    deg.add(nodes.get(i));     //the list has added an element
				    deglist.set(degreeofi, deg);					  //add the list to the degree-array
		//		 System.out.println("the list generated is to "+deg.get(0).no+" and is set to the index of "+degreeofi);
				}
				
				// if there is already a list there, just add elements
				else{ 	 
					deglist.get(degreeofi).add(nodes.get(i));
				}
		/*		for(int j=0;j<deglist.get(degreeofi).size();j++){
					System.out.print(deglist.get(degreeofi).get(j).toString()+ "->");
					
				}
			*/	
				
				if((i<nodes.size()-1)){
						if(nodes.get(i+1).nb.size()!=nodes.get(i).nb.size()){
		//		System.out.println("degree "+ degreeofi+" " + deglist.get(degreeofi));
				
			//	System.out.println(" ");
				}

				}
	//			else
	//				System.out.println("degree "+ degreeofi+" " + deglist.get(degreeofi));
					
			}	
			else
			
			if(deglist.get(0)==null){  //indicates that it does not have any node here
				
				LinkedList <Node> deg= new LinkedList <Node>();

			    deg.add(nodes.get(i));     //the list has added an element
			    deglist.set(0, deg);					  //add the list to the degree-array
	//		 System.out.println("the list generated is to "+deg.get(0).no+" and is set to the index of "+degreeofi);
			}
			
			// if there is already a list there, just add elements
			else{ 	 
				deglist.get(degreeofi).add(nodes.get(i));
	//			System.out.println("the list add an element "+ nodes.get(i).no+ " as the index of "+ Degarray[degreeofi].indexOf(nodes.get(i)));
			}

		}
		
		return deglist;
		
		
	}

	public static Node[] slo(ArrayList<LinkedList> dl, ArrayList <Node> nodes, int[] degreesdeleted){
	
	Node[] n =new Node[nodes.size()];   // the array to put SLOed nodes
	
		int j=0;
		int maxGroupdegree= nodes.get(nodes.size()-1).nb.size();
		
		for(int i=0;i<nodes.size();i++){	
	//  System.out.println("index i="+i+"j= "+j);
	// if(!dl.get(i).isEmpty()){                       // if the list of degree i is not empty   
			if(j< maxGroupdegree){	
				if((dl.get(j)==null)){   // list for degree j is empty
	//	  System.out.println("degree "+j+" is set to null as empty");
					if(j< nodes.get(nodes.size()-1).nb.size()){
						j++;
						i--;
					}
					else break;
				}	  
	  
				else if(dl.get(j).isEmpty()){
	//	  System.out.println("degree "+j+" is removed to empty");
					j++;
					i--;
					
				}
				else{	  // list for degree j is not empty
		//	System.out.println("Step "+(i+1)+":");
					int itoremove=0;
					//(int) (Math.random()*dl.get(j).size());
		//	System.out.println(itoremove);
					Collections.shuffle(dl.get(j));
					Node tempnode=(Node) dl.get(j).get(0);	  // j is the index in dl, which is the degree for now
					degreesdeleted[n.length-i-1]=tempnode.nb.size();     // put the degree when deleted into the node
					n[n.length-i-1]= tempnode;                  //put the smallest degree node so far, tnode, into the array
	     
	//     System.out.println(n[n.length-i-1]+" with degree "+n[n.length-i-1].nb.size()+" is being removed to n["+(n.length-i-1) + "]; it has nb of "+n[n.length-i-1].nb);
	     
					ArrayList<Node> tempNbs=tempnode.nb;       //get its neighbors and put them into an ArrayList tempNbs
	     
					dl.get(j).remove(0);
	     
					for(int k=tempNbs.size()-1;k>=0;k-- ){
						Node tempNbsNode = tempNbs.get(k);                  // tempNbsNode is tnode's each neighbor, tempNbsNode is got from the neighbor of tempNbs, so its degree is not 0
						int indexindl =tempNbsNode.nb.size(); 	   // get the each neighbor's degree 
	    	 
						dl.get(indexindl).remove(tempNbsNode);     // remove tnode's neighbor from DegreeList
						tempNbsNode.nb.remove(tempnode);              // decrease tnode's neighbor－－tempNbsNode's degree by removing the tnode object
    	 
	    	 
	//    	 System.out.println(tempNbsNode+" with degree "+tempNbsNode.nb.size()+" is removed from dl index "+indexindl+"; "+tempnode+" is also removed from "+tempNbsNode+" neighbor");
	    	 
	   // 	try{ 
	    //	 System.out.println("indexindl   in dl is degree "+ indexindl +" "+dl.get(indexindl));
	    	 
	    	// after removing and decreasing the neighbors' degrees, we need to add them into (degree-1) LinkedList in dl
	    	 
						if(dl.get(indexindl-1)==null){  // if the LinkedList to add in is set to null (newer smallest degree found)
    	
							LinkedList nll=new LinkedList();    // create a new LinkedList
							nll.add(tempNbsNode);                     // put the newly decreased neighbor to this LinkedList
							dl.set(indexindl-1, nll);
							if(tempNbsNode.nb.size()<j)    // if the newly decreased degree has a smaller degree than j
								j--;                                //
	    //     System.out.println(" new indexindl-1 in dl is degree "+(indexindl-1)+" "+dl.get(indexindl-1)); 
						} 
	    		
						else if (dl.get(indexindl-1).isEmpty()){
							dl.get(indexindl-1).add(tempNbsNode);
							j--;
						}
	    	
						else{                              //
							dl.get(indexindl-1).add(tempNbsNode);
	    		    	 
						}
						// 	 System.out.println("indexindl-1 in dl is degree "+(indexindl-1)+" "+dl.get(indexindl-1));

						//	catch(NullPointerException e){
						//   	 System.out.println("indexindl-1 is empty");
						//	}
    	 
					}
	     
					//     for(int d=0;d<dl.size();d++){
					//   	  System.out.println("Degree "+d+": "+dl.get(d));
	    	  
					//     }
    
	   
				}
	  
			}
	  
			//  System.out.println("-----------------------------------------------------");
	  
	 }
	 
		//	 else {                                       //if the list of
		 
		//	 }
	 
		
		
		//		System.out.println("null pointer "+dl.get(j))	;
		//	}
	  
	 
		
	//	if(!dl.get(i).isEmpty())    // if the element has a list with node 
		
	//	dl.get(j).remove();
		
	//	n[nodes.size()-i-1]=dl.get(i);
		
	/*	
		ArrayList <Node>nblist =new ArrayList<Node>();
		
		
		int i=0;
	//	dl stores the arraylist of degrees
		while(!dl.get(i+1).isEmpty()){   //while the nedl.get(i) is the First Element of the list in dl.get(i)
			while(!dl.get(i).isEmpty()){   //
				dl.get(i).remove();
				nl.get(i)
			}
			i++;
			
		}
		
		*/
		return n;
	
	}
	
	
	public void drawnodes(Node[] nodes, int n){
		for (int i=0; i<n;i++){
			for(int j=0;j<i;j++){
				if(distance2(nodes[i],nodes[j])<R*R){
					line(nodes[i].x,nodes[i].y,nodes[j].x,nodes[j].y);
				}
			}
		}
	}

	public int getmincolor(Node n, ArrayList<Node> nodes){
		
		int[] colors =new int[nodes.get(nodes.size()-1).nb.size()+1];
		ArrayList nb= n.nb;
		for (int i=0;i<nb.size();i++){   // check all neighbors
			Node check=(Node) nb.get(i);
	//		System.out.println(check);
			if(check.colored==true){   // if it is colored, store the color in colors[]
			//	System.out.println("Node "+n.no+"'s nb Node "+check.no+" has been colored with color "+check.color);
				colors[check.color]=check.color;
	//			System.out.println("neighbor "+check + " color "+check.color);
			  
			}
			
		}
	//	for (int k=0;k<nb.size();k++)
	//	System.out.println(colors[k]+" ");
	//	System.out.println("---");

		
		int j=1;  // default color to check on
		
			while(colors[j]!=0){
				j++;    // add by one until an available place found
			}
				
		return j;
		
	}
		
	
	public int[] coloring( ArrayList<Node> nodes, ArrayList <LinkedList> cl){
		
		 for(int j=0;j<degreelist.size();j++){
			 LinkedList shell =new LinkedList();
			cl.add(null);   //so the size will be (maxGroupdegree+1);
		}		 
	
	    int[] colors = new int[nodes.size()];
		for(int i=0;i<nodes.size();i++){
		
		   int c= getmincolor(nodes.get(i),nodes);   // take the color, also the index for colorlist
		   nodes.get(i).color=c; //give color into each node in Node Object
	//	   System.out.println("Node "+nodes.get(i).no+" gets the color of "+c);
		   
		   nodes.get(i).colored=true;
		   
  		//	connect2Dnodes(pivotx, nodes, a, 1);
									
				//	System.out.println("the index now is "+i +" and the degree now is "+degreeofi);
						
						// if there is no list there, create a list and add to the degree-list
						if(cl.get(c)==null){  //indicates that it does not have any node here
							LinkedList <Node> deg= new LinkedList <Node>();
						
						    deg.add(nodes.get(i));     //the list has added an element
						    cl.set(c, deg);					  //add the list to the degree-array
				//		 System.out.println("the list generated is to "+deg.get(0).no+" and is set to the index of "+degreeofi);
						}
						
						// if there is already a list there, just add elements
						else{ 	 
							cl.get(c).add(nodes.get(i));
						}
				/*		for(int j=0;j<deglist.get(degreeofi).size();j++){
							System.out.print(deglist.get(degreeofi).get(j).toString()+ "->");
							
						}
					*/	
		   
		   colors[i]=c;   // index i gets color c
		}
		return colors;   
	}
	
	
	
	public void shownodes(ArrayList<Node> nodes){
		for (int i=0;i<nodes.size();i++){
			System.out.println(nodes.get(i) +" has nb "+nodes.get(i).nb);
		}
	}

	
    public float[][] givecolors(int[] colors){
    	float[][] rgb = new float[colors.length][4];
    	float plus=255*9/colors.length;
    	float a=0,b=0,c=0;
    	Random randomno = new Random();
    	for(int i=0;i<colors.length;i++){
    		
    				  rgb[i][0]=random(255);
    				  rgb[i][1]=random(255);
    				  rgb[i][2]=random(255);
    				  rgb[i][3]=colors[i];
    //				  System.out.println(rgb[i][0]+" "+rgb[i][1]+" "+rgb[i][2]);
    				    			    		
    	}
	
		return rgb;	
    	
    }

    public void display2Dcolornodes(ArrayList<Node> nodes, float[][]colors){
		for (int i=0;i<nodes.size();i++){
			//stroke(colors[i][0],colors[i][1],colors[i][2]);
			Node tempnode=nodes.get(i);
			stroke((float)colors[tempnode.color][0],colors[tempnode.color][1],colors[tempnode.color][2]);
			fill((float)colors[tempnode.color][0],colors[tempnode.color][1],colors[tempnode.color][2]);
			if(highlightcolor==true){
				if(tempnode.y<20)
					text(tempnode.color,nodes.get(i).x,nodes.get(i).y+10);			
				else if(tempnode.x>(width-20))
					text(tempnode.color,nodes.get(i).x-10,nodes.get(i).y);
				else 
					text(tempnode.color,nodes.get(i).x,nodes.get(i).y);
			
					ellipse(nodes.get(i).x,nodes.get(i).y,3,3);
				}		
			else
			ellipse(nodes.get(i).x,nodes.get(i).y,1,1);
						
			
		}    	
    	
    	
    }
    
    

    public void display3Dcolornodes(ArrayList<Node> nodes, float[][]colors){
    //	translate(width/2,height/2,0);
		for (int i=0;i<nodes.size();i++){
			//stroke(colors[i][0],colors[i][1],colors[i][2]);
			Node tempnode=nodes.get(i);
			stroke((float)colors[tempnode.color][0],colors[tempnode.color][1],colors[tempnode.color][2]);
			if(highlightcolor==true){
			
	//		fill((float)colors[tempnode.color][0],colors[tempnode.color][1],colors[tempnode.color][2]);
	/*		if(tempnode.y<20)
				text(tempnode.color,nodes.get(i).x,nodes.get(i).y+10);			
			else if(tempnode.x>(width-20))
				text(tempnode.color,nodes.get(i).x-10,nodes.get(i).y);
			else 
				text(tempnode.color,nodes.get(i).x,nodes.get(i).y);
		*/	
			}
			point(nodes.get(i).x,nodes.get(i).y,nodes.get(i).z);
		}    	
    	
    	
    }
    
    
    
    ArrayList <Node> choosebackbone(int n, ArrayList<LinkedList > cl){
    	// n=[1,6], representing 6 combination of 2 independent sets out of 4 sets 
    	int size1=cl.get(1).size();
    	int size2=cl.get(2).size();
  //  	System.out.println("cl length is "+cl.size());
    	ArrayList<Node> bb= new ArrayList<Node>(size1+size2);
    	
    	if(n==1){ // 1 and 2
    		Node temp;
			for(int i=0;i<cl.get(1).size();i++){
				temp = (Node) cl.get(1).get(i);
				bb.add(temp);
			}
			for(int i=0;i<cl.get(2).size();i++){
				temp = (Node) cl.get(2).get(i);
	    		bb.add(temp);
			}
						
    	}
    	
    	if(n==2){ // 1 and 3
    		Node temp;
			for(int i=0;i<cl.get(1).size();i++){
				temp = (Node) cl.get(1).get(i);
				bb.add(temp);
			}
			for(int i=0;i<cl.get(3).size();i++){
	    		temp = (Node) cl.get(3).get(i);
	    		bb.add(temp);
			}
						
    	}
    	if(n==3){ // 1 and 4
    		Node temp;
			for(int i=0;i<cl.get(1).size();i++){
				temp = (Node) cl.get(1).get(i);
				bb.add(temp);
			}
			for(int i=0;i<cl.get(4).size();i++){
				temp = (Node) cl.get(4).get(i);
	    		bb.add(temp);
			}
						
    	}
    	if(n==4){ // 2 and 3
    		Node temp;
			for(int i=0;i<cl.get(2).size();i++){
				temp = (Node) cl.get(2).get(i);
				bb.add(temp);
			}
			for(int i=0;i<cl.get(3).size();i++){
				temp = (Node) cl.get(3).get(i);
	    		bb.add(temp);
			}
						
    	}
    	if(n==5){ // 2 and 4
    		Node temp;
			for(int i=0;i<cl.get(2).size();i++){
				temp = (Node) cl.get(2).get(i);
				bb.add(temp);
			}
			for(int i=0;i<cl.get(4).size();i++){
				temp = (Node) cl.get(4).get(i);
	    		bb.add(temp);
			}
						
    	}
    	if(n==6){ // 3 and 4
    		Node temp;
    		for(int i=0;i<cl.get(3).size();i++){
    			temp = (Node) cl.get(3).get(i);
    			bb.add(temp);
    		}
			for(int i=0;i<cl.get(4).size();i++){
	    		temp = (Node) cl.get(4).get(i);
	    		bb.add(temp);
			}
						
    	}
 	
    	return bb;
    }
    
    
	public int drawbackbone(ArrayList<Node> bbs){
		int be=0;
		background(0,20,20);

		for(int i=0;i<bbs.size();i++){
			for(int j=0;j<bbs.get(i).nb.size();j++){
				Node tempnb=bbs.get(i).nb.get(j);
				
				if(bbs.contains(tempnb)){
					 stroke(0,200,50);
					 line(bbs.get(i).x,bbs.get(i).y,bbs.get(i).nb.get(j).x,bbs.get(i).nb.get(j).y);
					 be++;
				}
			}
		}
		saveFrame("backbone.png");
		return be;
		
	}
	
	 
	public int draw3backbone(ArrayList<Node> bbs){
		int be=0;
		background(0,20,20);

		//	translate(width/2,height/2,0);
			for(int i=0;i<bbs.size();i++){
				for(int j=0;j<bbs.get(i).nb.size();j++){
					Node tempnb=bbs.get(i).nb.get(j);
					
					if(bbs.contains(tempnb)){
					  stroke(0,200,50);
					  line(bbs.get(i).x,bbs.get(i).y,bbs.get(i).z,bbs.get(i).nb.get(j).x,bbs.get(i).nb.get(j).y,bbs.get(i).nb.get(j).z);
					  be++;
					}
				}
			}
			saveFrame("backbone.png");
			return be;
		}
	
	
    public void getoriginaldegrees(ArrayList<Node> nodes){
    	for(int i=0;i<nodes.size();i++){
    	//	System.out.println( nodes.get(i)+" method get original degree "+nodes.get(i).nb.size());
    		nodes.get(i).oriDegree=nodes.get(i).nb.size();
    	}
    }
    
    public void displayremaining(Node[]s){
    	for (int i=0;i<s.length;i++){
    		System.out.println(s[i]);
    	//	point(s[i].x,s[i].y);
    	}
    }
  
    
    public float distance2(Node n1, Node n2){
		
		float d=
		(n1.x-n2.x)*(n1.x-n2.x)+(n1.y-n2.y)*(n1.y-n2.y)
		   +(n1.z-n2.z)*(n1.z-n2.z);
	
	   return d;
	}	
    
 
    public void plot(int[]d, Node[] s, int[] degreesdeleted ){
    	PrintWriter writer;
		try {
			writer = new PrintWriter("file-name.txt", "UTF-8");
			 for(int i=0;i<s.length;i++){
				 if(i<d.length)
				 writer.println(s[i].no+", "+s[i].oriDegree+", "+degreesdeleted[i]+", "+i+", "+d[i]);

				 else
				 writer.println(s[i].no+", "+s[i].oriDegree+", "+degreesdeleted[i]);
			 }
			 
			 writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 
//	System.out.println("edges="+edges);
    }
    

    public void plotcolor(ArrayList<LinkedList>colorlist ){
    	PrintWriter writer;
		try {
			writer = new PrintWriter("colors-name.txt", "UTF-8");
			 for(int i=1;i<colorlist.size();i++){
				 if(colorlist.get(i)!=null)
				 writer.println(i+", "+colorlist.get(i).size());

			 }
			 
			 writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 
	System.out.println("edges="+edges);
    }
        
    
    
   public void draw2Dslo(Node[] s){
	   if(j<=looptimes){
			for(int i=0;i<j;i++){
			//	System.out.println(s[i].x+" "+s[i].y);
				point(s[i].x,s[i].y);
				
				
				for(int k=0;k<i;k++){
					if(distance2(s[i],s[k])<height*height*R*R){
						stroke(155,155,0);
						line(s[i].x,s[i].y,s[k].x,s[k].y);
					}
				}
				
		
				if(s[i].x<15){
					text(s[i].no,s[i].x+2,s[i].y+8);
				}
				else{
				text(s[i].no, s[i].x-6,s[i].y+1);
				}
			}
		
			
		}
		else{
			noLoop();
		}
	//	System.out.println("----");
		saveFrame("pic-#####.png");
			j++;
	   
   }
   
   public void draw3Dslo(Node[] s){
	   if(j<=looptimes){
			for(int i=0;i<j;i++){
			//	System.out.println(s[i].x+" "+s[i].y);
				point(s[i].x,s[i].y,s[i].z);
				
				
				for(int k=0;k<i;k++){
					if(distance2(s[i],s[k])<height*height*R*R){
						stroke(155,155,0);
						line(s[i].x,s[i].y,s[i].z,s[k].x,s[k].y,s[k].z);
					}
				}
				
			}		
			
		}
		else{
			noLoop();
		}
	//	System.out.println("----");
		saveFrame("pic-#####.png");
			j++;
	   
   }
   
   public int[] degreedist(ArrayList<LinkedList> degreelist){
	   int[]d =new int[degreelist.size()];
	   for (int i=0;i<degreelist.size();i++){
		   if(degreelist.get(i)==null)
			   d[i]=0;
			   
		   else{ 
		   d[i]=degreelist.get(i).size();
		//   System.out.println("i="+i+" =="+d[i]);
		   }
	   }
	return d;
	   
   }
   public int getnc (ArrayList<LinkedList> colorlist){
	   int ncolor=0;
		 for(int nc=1;nc<colorlist.size();nc++){
		//	 System.out.println("colorlist :"+colorlist.get(nc));
			 
			 if(colorlist.get(nc)!=null){
				 ncolor=nc;
			 }
		 }
		 return ncolor;
		 
		 }
   public double averagedegree(ArrayList <Node> nodes){
		int sum=0;
		for (int i=0; i<nodes.size();i++){
			sum+=nodes.get(i).nb.size();
		}
		double ave=sum/nodes.size();
		System.out.println("average degree is "+ave);
		return ave;	

	}
   
   public double coverage(LinkedList<Node> l1, LinkedList<Node> l2, int n){
	   Set<Node> set = new HashSet<Node>();
	   
//	   System.out.println("l1 is "+l1);
//	   System.out.println("l2 is "+l2);
	   for(int i=0;i<l1.size();i++){
		   for(int k=0;k<l1.get(i).nb.size();k++){
			 // tint(255,228);
			  //noFill();
			  //stroke(2);
			//  fill(12,40,12);
		//	   ellipse( l1.get(i).nb.get(k).x,l1.get(i).nb.get(k).y,2*height*R,2*width*R);
			  set.add(l1.get(i).nb.get(k));
		   }
		   
	   }
	   for(int j=0;j<l2.size();j++){
		   for(int k=0;k<l2.get(j).nb.size();k++){
			  set.add(l2.get(j).nb.get(k));
  
		   }
		   
	   }
	   System.out.println("set size "+set.size());
	   double cov=(double)set.size()/(double)n;

	   return cov;
   }
   
   
   
   
   public ArrayList bfsmerge(LinkedList<Node> l1, LinkedList<Node> l2){
	   
	   ArrayList<Node> nodes=new ArrayList();
	   for(int i=0;i<l1.size();i++){
		   nodes.add(l1.get(i));
	   }
	   
	   for(int i=0;i<l2.size();i++){
		   nodes.add(l2.get(i));
   
	   }
	   return nodes;
   }
   
   
   public static ArrayList walk(ArrayList<Node> nodes){
		ArrayList<Node> maxGroup= new ArrayList();
		bfs(nodes.get(0));
	//	System.out.println("after first walk of 0, the order is "+order);
		for(int i=0;i<order.size();i++)
			maxGroup.add(order.get(i));

		order.removeAll(nodes);
		//System.out.println("after first walk of 0, the maxGroup is "+maxGroup);

		for(int i=1;i<nodes.size();i++){
	//		System.out.println("index "+i+" beginning loop, the order is "+order);

		//	System.out.println("index "+i+" beginning loop, the maxGroup is "+maxGroup);
			
			if(nodes.get(i).visi==false){
			//	System.out.println("index "+i+" not visited");
				bfs(nodes.get(i));
				//System.out.println("index "+i+" after bfs then order is "+order);
			if (order.size()>maxGroup.size()){
				maxGroup.clear();
				for(int j=0;j<order.size();j++)
					
				maxGroup.add(order.get(j));
			}
			
			order.clear();
			}
			
		}
	//	System.out.println("maxGroup is "+maxGroup);
		return maxGroup;
	}
   
   public static void bfs(Node n){
		q.add(n);
	//	order.add(n);
		set.add(n);
		n.visi=true;	
		
		while(!q.isEmpty()){
			Node t=q.peek();
		//			System.out.println("Peek is "+q.peek());
				for(int j=0;j<t.nb.size();j++){
	//				System.out.println("Node n's nb is "+t.nb+" and it has visi "+t.nb.get(j).visi);
					if(t.nb.get(j).visi==false && set2.contains(t.nb.get(j))){  // if its neighbors not visited before
						//put the neighbors in the queue
						q.add(t.nb.get(j));
						
						t.nb.get(j).visi=true;
			//			System.out.println(q);
					}
				}
				
				Node temp=q.poll();
				order.add(temp);
				set.add(temp);
	//			System.out.println(" 2 "+q);
	//			System.out.println("set "+set);
						
			//		set.add(n.nb.get(j));
		/*				if(!order.contains(t.nb.get(j))){
					order.add(t.nb.get(j));
					System.out.println(" added to order"+order);
						}
					System.out.println(q);
					
					}
					
				}
			*/	
		}
   }
   public ArrayList maxGroupconnect(LinkedList <Node> l1, LinkedList <Node> l2){
	   ArrayList <Node> maxGroup=new ArrayList();
	   ArrayList <Node> nodes=new ArrayList();
	   nodes=bfsmerge(l1,l2);
	//   System.out.println(nodes);
	   set.clear();
	   for(int i=0;i<nodes.size();i++){
		   set2.add(nodes.get(i));
	   }
	 
	//   System.out.println("set2"+set2);
   
	   maxGroup=walk(nodes);
	   
	   showmaxGroup(maxGroup);
	   return maxGroup;
 
	   
   }
   public void showmaxGroup(ArrayList<Node> maxGroup){
	   clear();
	   background(0,0,0);
	   for(int i=0;i<maxGroup.size();i++){
		 for(int j=0;j<maxGroup.get(i).nb.size();j++){
			 if (maxGroup.contains(maxGroup.get(i).nb.get(j))){
				 line(maxGroup.get(i).x,maxGroup.get(i).y,maxGroup.get(i).z,maxGroup.get(i).nb.get(j).x,maxGroup.get(i).nb.get(j).y,maxGroup.get(i).nb.get(j).z);
				 bee++;
				// bee/=2;
			 }
		 }
	   }
	   saveFrame("connected.png");
   }
  public void showsets(ArrayList<LinkedList> colorlist){
	// 1. create the backbone nodes in ArrayList 		 		 		 
			 ArrayList<Node> bb1=choosebackbone(1,colorlist);  // the integer represents 1 of 6 backbone selections 
			 ArrayList<Node> bb2=choosebackbone(2,colorlist);  // the integer represents 1 of 6 backbone selections 
			 ArrayList<Node> bb3=choosebackbone(3,colorlist);  // the integer represents 1 of 6 backbone selections 
			 ArrayList<Node> bb4=choosebackbone(4,colorlist);  // the integer represents 1 of 6 backbone selections 
			 ArrayList<Node> bb5=choosebackbone(5,colorlist);  // the integer represents 1 of 6 backbone selections 
			 ArrayList<Node> bb6=choosebackbone(6,colorlist);  // the integer represents 1 of 6 backbone selections 

	// 2. draw the edges as backbones	
			 
			int BE;
			if(dimension==2){
				drawbackbone(bb1);		          
				saveFrame("colorset-1.png");
	          	clear();
	          
	          	drawbackbone(bb2);	
	          	saveFrame("colorset-2.png");
	          	clear();
	          
	          	drawbackbone(bb3);	 	          
	          	saveFrame("colorset-3.png");
	          	clear();
	          
	          	drawbackbone(bb4);	 
	          	saveFrame("colorset-4.png");
	          	clear();


	          	drawbackbone(bb5);	 
	          	saveFrame("colorset-5.png");
	          	clear();
	          	drawbackbone(bb6);	 
	         

	          	saveFrame("colorset-6.png");
	          	clear();
			}
		    else {
		    	draw3backbone(bb1);		          
		        saveFrame("colorset-1.png");
		        clear();
		          
		        draw3backbone(bb2);	
		        saveFrame("colorset-2.png");
		        clear();
		          
		        draw3backbone(bb3);	 	          
		        saveFrame("colorset-3.png");
		        clear();
		          
		        draw3backbone(bb4);	 
		        saveFrame("colorset-4.png");
		        clear();


		        draw3backbone(bb5);	 
		        saveFrame("colorset-5.png");
		        clear();
		          
		          
		        draw3backbone(bb6);	 
		        saveFrame("colorset-6.png");
		        clear();
		    }
  }

}

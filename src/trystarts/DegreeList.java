package trystarts;

import java.util.List;

public class DegreeList {
	List nblist;
	DegreeList previous, next;
	
	DegreeList(List list){
		this.nblist=list;		
	}
	public void add(DegreeList list){
		
		list.next=next;
		list.previous=this;
		next=list;
	}
}

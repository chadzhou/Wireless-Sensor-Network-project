package trystarts;

import java.util.Comparator;

public class degreecomparator implements  Comparator<Node>  {
		
	private static final degreecomparator instance = 
			new degreecomparator();

		  public static degreecomparator getInstance() {
		    return instance;
		  }

		  private degreecomparator() {
		  }
	@Override
	public int compare(Node a0, Node a1) {
		// TODO Auto-generated method stub
		return a0.nb.size()-a1.nb.size();
	}

}

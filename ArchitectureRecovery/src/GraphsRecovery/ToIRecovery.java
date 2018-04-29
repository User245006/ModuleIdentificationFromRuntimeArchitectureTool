package GraphsRecovery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ToIRecovery {
	
	public static List<String> affectationsLeftParts = new ArrayList<>();
	public static List<String> affectationsRightParts = new ArrayList<>();
	public static List<String> nonRepeatedAffectations = new ArrayList<>();
	
	public static HashMap<String, String> objectCreators = new HashMap<>();
 	
	public static void TreeOfCreationRecovery(List<String> affectations)
	{
		for(String affectation : affectations)
		  {  if(!nonRepeatedAffectations.contains(affectation))
		      {
			  nonRepeatedAffectations.add(affectation);
		      }
		  }
		  
		  for(String a : nonRepeatedAffectations)
		  {
			  String[] affectationParts = a.split(" = ");
			  //System.out.println(a);
			  affectationsLeftParts.add(affectationParts[0]);
			  affectationsRightParts.add(affectationParts[1]);
		  }
		  
		  
		  for(int s = 0; s < affectationsRightParts.size(); s++)
          {
			  String elem = affectationsRightParts.get(s);
			  String[] splittedElem = elem.split("\\.");
			  int size = splittedElem.length-1;
			  
			  if(splittedElem[size].equals("this") && splittedElem[size-2].contains(splittedElem[size-1]))
		      {
				  if(!objectCreators.containsKey(splittedElem[size-2]))
				  { objectCreators.put(splittedElem[size-2], affectationsLeftParts.get(s));}
				  
		      }
          }
	}
	
	
	public static Boolean compareToIwithToCS(HashMap<String, String> creators, 
			HashMap<String, List<String>> dominators)
	{   int comptDifferences = 0;
		Boolean equals = true;
		
		Set Keys = dominators.keySet();
		Iterator it = Keys.iterator();
		while (it.hasNext()){
		   String key = (String) it.next(); 
		   List<String> values = dominators.get(key); 
		   for(String v : values)
		   {
			   if(!creators.get(v).contains(key))
			   {
				   equals = false;
				   comptDifferences++;
			   }
		   }
		}
		System.out.println("number of differences: "+comptDifferences);
		return equals;
	}

}

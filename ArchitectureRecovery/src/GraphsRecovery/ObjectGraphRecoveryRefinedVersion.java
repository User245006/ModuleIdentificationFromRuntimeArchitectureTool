package GraphsRecovery;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import InstrumentationPhase.FieldsofUserDefinedClassType;
import LanguarTarjanDominatorsAlgorithm.JsonEdge;
import LanguarTarjanDominatorsAlgorithm.JsonGraph;
import LanguarTarjanDominatorsAlgorithm.JsonNode;

public class ObjectGraphRecoveryRefinedVersion {
	
public static List<String> fieldsOutSets = new ArrayList<>();
	
	public static List<String> affectationsLeftParts = new ArrayList<>();
	public static List<String> affectationsRightParts = new ArrayList<>();
	public static List<String> nonRepeatedAffectations = new ArrayList<>();
	
	public static List<String> nonRepeatedUncertainAffectations = new ArrayList<>();
	public static List<String> uncertainAffectationsLeftParts = new ArrayList<>();
	public static List<String> uncertainAffectationsRightParts = new ArrayList<>();
	
	public static List<String> outPutsOfFields = new ArrayList<>();
	public static Collection<String> fields = FieldsofUserDefinedClassType.varNames;
 
	public static int numberOfNodes =1; 
	
	public static List<String> LeftPartNodes = new ArrayList<>();
	public static List<String> RightPartNodes = new ArrayList<>();
	
	public static JsonGraph graph = new JsonGraph();
	public static List<String> graphNodesNames = new ArrayList<>();
	
	
	public static JsonGraph draw(List<String> affectations, HashMap<String, String> positions)
	{
		
	  for(String affectation : affectations)
	  {  if(!nonRepeatedAffectations.contains(affectation))
	     {
		  nonRepeatedAffectations.add(affectation);
	     }
	  }
	  
	  for(String a : affectations)
	  {
		  String[] affectationParts = a.split(" = ");
		  affectationsLeftParts.add(affectationParts[0]);
		  affectationsRightParts.add(affectationParts[1]);
	  }

	 int NumberR;
	 int NumberL;
	 int k = 0;
	 for(String rightPart: affectationsRightParts)
	 {   
		 NumberR = getNumberInRightPart(rightPart);
		 
		 //test if the right part is an instance
		 String[] splitRightPart = rightPart.split("\\.");
		 int size = (splitRightPart.length)-1;
		 
		 if(size > 0)
		 {  
			 
			 if((splitRightPart[size].trim()).equals("this"))
	          {  if( (splitRightPart[size-2].trim()).contains(splitRightPart[size-1].trim()))
	            {
					 
					 // the right part is an instance
			        
		             List<Integer> indexes = getIndexesInRightPart(rightPart, k);
		            System.out.println("instance: "+rightPart);
			         for(int index : indexes)
			         { System.out.println("1");
			           search(index, splitRightPart[size-2]);
			           
	                 }
			     }
	          }
		 }
		 else
		 {
			 System.out.println("the right part is a null");
		 }
			
		k++; 
	 }
	  
	  ConstructGraph(outPutsOfFields, positions);
      return graph;
	     
	}
	
	
	
	public static int getNumberInRightPart(String affectationPart)
	{  int n = 0;
		if(affectationsRightParts.contains(affectationPart))
		{
			for(String rp : affectationsRightParts)
			{
				if(rp.equals(affectationPart))
				{
					n++;
				}
			}
		}
		return n;
	}
	
	public static List<Integer> getIndexesInRightPart(String affectationPart, int io)
	{  List<Integer> n= new ArrayList<>();
		int i = 0;
		
		for(i=0; i<affectationsRightParts.size(); i++)
		{ //System.out.println("rp: "+rp+" size: "+rp.length());
		  
				if(affectationsRightParts.get(i).equals(affectationPart))
				{  
				   if(i >= io)
				   {
					n.add(i);
				   }
				}

		}
		if(n.size()>1)
		{  
			List<Integer> newN= new ArrayList<>();
			newN.add(n.get(0));
			n = newN;
		}
		
		return n;
	}
	
	public static int getNumberInLeftPart(String affectationPart)
	{  int n = 0;
		if(affectationsLeftParts.contains(affectationPart))
		{
			for(String lp : affectationsLeftParts)
			{
				if(lp.equals(affectationPart))
				{
					n++;
				}
			}
		}
		return n;
	}

	
	public static void search(int index, String orp)
	{   String outPutName = "";
	    Boolean found = false;
		String correspondLeftPart = affectationsLeftParts.get(index);
		
   	    // delete all number in the string
   	    // we consider that methods names and variables names do not contain numbers
		
		System.out.println(correspondLeftPart + " = "+affectationsRightParts.get(index));
   	     String tmpcorrespondLeftPart = correspondLeftPart.replaceAll("[\\d]", "");
   	   // System.out.println(fields);
   	    if(fields.contains(tmpcorrespondLeftPart.trim()))
	   	 {   
   	    	 String[] fieldSplit = correspondLeftPart.split("\\.");
	   	     int fieldSize = (fieldSplit.length)-1;
	   	     outPutName = fieldSplit[fieldSize-1]+"."+fieldSplit[fieldSize]+" = "+orp;
	   	     outPutsOfFields.add(outPutName);
	   	     
	   	 }
	   	 else
	   	 {  
	   		List<Integer> indexRP = new ArrayList<>();
	   	    indexRP = getIndexesInRightPart(correspondLeftPart, index);
	   	    if(indexRP.size() > 0)
	   	    { 
		   	    for(int ii : indexRP)
		   	    {   System.out.println("2");
		   	    	search(ii, orp); 
		   	    }
	   	    }
	   	    else
	   	    { 
	   	    }
	   	 }
	}
	

	public static void ConstructGraph(List<String> outputs, HashMap<String, String> positions)
	{
		for(String a : outputs)
		  {
			  String[] affectationParts = a.split(" = ");
			  
			  String[] labelNode = affectationParts[0].split("\\.");
			  LeftPartNodes.add(labelNode[0]);
			  RightPartNodes.add(affectationParts[1]);
			  
			  JsonEdge edge = new JsonEdge();
		      JsonNode source;
		      JsonNode target;
		    	
		    	if(!graphNodesNames.contains(labelNode[0]))
		    	{ 
		    	  source = new JsonNode(labelNode[0]);
		    	  graph.nodes.add(source);
		    	  source.id = numberOfNodes++;
    		      source.label = " ";
    		      if(positions.containsKey(labelNode[0]))
    		      {
    		      source.position = positions.get(labelNode[0]);
    		      }
    		      else
    		      {
    		      source.position = ""; 
    		      }
    		      source.show = "true";
    		      source.internalStructure = " ";
    		      source.showInternalStructure = "true";
		    	  graphNodesNames.add(source.name);
		    	  edge.source = source;
		    	}
		    	else
		    	{
		    		for(JsonNode nd : graph.nodes)
		    		{
		    			if(nd.name.equals(labelNode[0]))
		    			{
		    			  source = nd;
	          		      edge.source = source;
		    			}
		    		}
		    	}
		    	
		    	
		    	
		    	if(!graphNodesNames.contains(affectationParts[1]))
		    	{ target = new JsonNode(affectationParts[1]);
		    	  graph.nodes.add(target);
		    	  target.id = numberOfNodes++;
	  		      target.label = " ";
	  		      if(positions.containsKey(affectationParts[1]))
	  		      {
	  		      target.position = positions.get(affectationParts[1]);
	  		      }
	  		      else
	  		      {
	  		    	target.position = ""; 
	  		      }
	  		      target.show = "true";
	  		      target.internalStructure = " ";
	  		      target.showInternalStructure = "true";
		    	  graphNodesNames.add(target.name);
		    	  edge.target = target;
		    	  edge.type = labelNode[1];
                graph.edges.add(edge);
		    	}
		    	else
		    	{
		    		for(JsonNode nd : graph.nodes)
		    		{
		    			if(nd.name.equals(affectationParts[1]))
		    			{
		    			  target = nd;
	          		      edge.target = target;
	          		      edge.type = labelNode[1];
                          graph.edges.add(edge);
		    			}
		    		}
		    	}
		  }
		
		
		// remove duplicated edges with different types(labels)
		for(int i=0; i<graph.edges.size(); i++)
		{ JsonEdge e = graph.edges.get(i);
			for(int j=0;j<graph.edges.size();j++)
			{  if(i != j)
			  {
				JsonEdge ee = graph.edges.get(j);
				if(ee.source == e.source && ee.target == e.target)
				{
					ee.type = ee.type.concat(", "+e.type);
					graph.edges.remove(i);
				}
				
			  }
			}
		}
		
	}

}

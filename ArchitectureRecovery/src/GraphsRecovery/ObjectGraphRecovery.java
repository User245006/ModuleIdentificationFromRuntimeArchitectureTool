package GraphsRecovery;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import InstrumentationPhase.FieldsofUserDefinedClassType;
import LanguarTarjanDominatorsAlgorithm.JsonEdge;
import LanguarTarjanDominatorsAlgorithm.JsonGraph;
import LanguarTarjanDominatorsAlgorithm.JsonNode;

public class ObjectGraphRecovery {
	
public static List<String> fieldsOutSets = new ArrayList<>();
	
	public static List<String> affectationsLeftParts = new ArrayList<>();
	public static List<String> affectationsRightParts = new ArrayList<>();
	public static List<String> nonRepeatedAffectations = new ArrayList<>();
	
	public static List<String> nonRepeatedUncertainAffectations = new ArrayList<>();
	public static List<String> uncertainAffectationsLeftParts = new ArrayList<>();
	public static List<String> uncertainAffectationsRightParts = new ArrayList<>();
 
	public static int numberOfNodes =0; 
	
	public static JsonGraph graph = new JsonGraph();
	public static List<String> graphNodesNames = new ArrayList<>();
	
	
	public static JsonGraph draw(List<String> affectations)
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
	 
	 
	  Boolean found = false;
	  PrintWriter out;
      File fich = new File("/home/soumia/Bureau/OutPut/OG.dot");
      try {
    	  
    	  System.out.println("Drawing ...");
          out = new PrintWriter(new FileWriter(fich));
          String bgcolor = "bgcolor=\"antiquewhite3\"";
          out.write("digraph OFG {"+bgcolor+"");
          out.println();
          
          Collection<String> fields = FieldsofUserDefinedClassType.varNames;
      
          System.out.println("fields: "+fields);
          for(int s = 0; s < affectationsLeftParts.size(); s++)
          { 
          	 for(String field : fields)
          	 {  
          		 String[] splittedLeftPart = affectationsLeftParts.get(s).split("\\.");
          		 String[] splittedField = field.split("\\.");
          		 List<String> edges = new ArrayList<>();
          		 
          		 int sizeRightPart = splittedLeftPart.length-1;
          		 int sizeSplittedField = splittedField.length-1;
          		 // search fields in right parts list
          		 String elem = affectationsRightParts.get(s);
   			     String[] splittedElem = elem.split("\\.");
   			    int size = splittedElem.length-1;
          		 if((splittedLeftPart.length == splittedField.length)
          				 && (splittedLeftPart[sizeRightPart].equals(splittedField[sizeSplittedField]))
          				 && (splittedLeftPart[sizeRightPart-1].contains(splittedField[sizeSplittedField-1])))
          		    {
          			   if((splittedElem[size].equals("this"))
              				 && (splittedElem[size-2].contains(splittedElem[size-1])))
              		    { found = true;}
              		       
          				else
          				{ if(affectationsLeftParts.contains(elem))
          				  {
	          				 while(!splittedElem[size].equals("this") || !splittedElem[size-2].contains(splittedElem[size-1]))
	    			    	 {  
	          					for(int x = 0; x < affectationsLeftParts.size(); x++)
	          					{   
	          						if(affectationsLeftParts.get(x).equals(elem))
	          						{  
	          						    elem = affectationsRightParts.get(x);
	          						    String[] temp = elem.split("\\.");
	          							int size02 = temp.length-1; 
	          							splittedElem[size] = temp[size02];
	          							splittedElem[size-2] = temp[size02-2];
	          							splittedElem[size-1] = temp[size02-1];
	       							    found = true;
	       							    
	          						}
	          						
	          					}
	          					
	          					if(found == false)
	     			    	   {   
	     			    		   break;
	     			    	   } 
	    			    	 }
          				 }
          				}
          		 
          		      if(found == true)
          		      { String edgeLabel = "";
          		        String edgeLab = "";
          		    	if(uncertainAffectationsLeftParts.contains(affectationsLeftParts.get(s)) && uncertainAffectationsRightParts.contains(affectationsRightParts.get(s)))
        		        { String tailLabel = ", dir=backward, headlabel = \" ?  \"";
        		         
          		    	  edgeLabel =" [label=\""+splittedField[sizeSplittedField]+"\""+tailLabel+"] ";
          		    	  edgeLab = splittedField[sizeSplittedField]+"\""+tailLabel;
          		    	  
        		        }
          		        else
          		        {
          		          edgeLabel =" [label=\""+splittedField[sizeSplittedField]+" \"] ";
          		          edgeLab = splittedField[sizeSplittedField];
          		        }
          		    	
          		    	
          		    	JsonEdge edge = new JsonEdge();
          		    	JsonNode source;
          		    	JsonNode target;
          		    	if(!graphNodesNames.contains(splittedLeftPart[sizeRightPart-1]))
          		    	{  source = new JsonNode(splittedLeftPart[sizeRightPart-1]);
          		    	  
          		    	  graph.nodes.add(source);
          		    	  source.id = numberOfNodes++;
	          		      source.label = " ";
	          		      source.position = " ";
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
          		    			if(nd.name.equals(splittedLeftPart[sizeRightPart-1]))
          		    			{
          		    			  source = nd;
          	          		      edge.source = source;
          		    			}
          		    		}
          		    	}
          		    	
          		    	
          		    	
          		    	if(!graphNodesNames.contains(splittedElem[size-2]))
          		    	{ target = new JsonNode(splittedElem[size-2]);
          		    	  graph.nodes.add(target);
          		    	  target.id = numberOfNodes++;
            		      target.label = " ";
            		      target.position = " ";
            		      target.show = "true";
            		      target.internalStructure = " ";
            		      target.showInternalStructure = "true";
          		    	  graphNodesNames.add(target.name);
          		    	  edge.target = target;
          		    	  edge.type = edgeLab;
                          graph.edges.add(edge);
          		    	}
          		    	else
          		    	{
          		    		for(JsonNode nd : graph.nodes)
          		    		{
          		    			if(nd.name.equals(splittedElem[size-2]))
          		    			{
          		    			  target = nd;
          	          		      edge.target = target;
          	          		      edge.type = edgeLab;
                                  graph.edges.add(edge);
          		    			}
          		    		}
          		    	}
          		    	
					    String v = "\""+splittedLeftPart[sizeRightPart-1]+ "\"->\""+splittedElem[size-2]+"\"";
					    String v2 = v+edgeLabel;
					   
					    if(!edges.contains(v))
					    {
					    edges.add(v);
						out.write(v2);
		                out.println();
					    }
          		     }
          		    found = false;
          	 }
          	  
          }
          }

          out.write("}");
          out.close();
          
          
      }  catch (IOException ex) {
          System.out.println("Erreur : "+ex.getMessage());
      }

      return graph;
	     
	}

	

}

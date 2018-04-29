package LanguarTarjanDominatorsAlgorithm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class JsonGraph {
	
	public List<JsonNode> nodes = new ArrayList<>();
	
	public List<JsonEdge> edges = new ArrayList<>();
	
	public void generateJSONEntries()
	{
		System.out.println("generating entries");
		List<String> nodesNames = new ArrayList<>();
		   
		try { 
			  File jsonFile = new File("./OutPuts/OG.txt");
			  PrintWriter out;
			  
	          out = new PrintWriter(new FileWriter(jsonFile));
	          out.write("{");
	          out.println();
	          out.write("\"nodes\": [");
	          out.println();
	         
	          for(JsonNode node : nodes)
	          {   
	        	 
	        	  out.write("{");
		          out.println();
		 
		         out.write("\"name\": "); 
		         out.write(node.name+"");
		         out.println();
		         
		         out.write("\"label\": "); 
		         out.write("["+node.label+"]");
		         out.println();
		         
		         out.write("\"position\": "); 
		         out.write("["+node.position+"]");
		         out.println();
		         
		         out.write("\"id\": "); 
		         out.write(String.valueOf(node.id));
		         out.println();
		         
		          out.write("\"show\": "); 
		         out.write(node.show+"");
		         out.println();
		         
		         out.write("\"internalStructure\": "); 
		         out.write(node.internalStructure+"");
		         out.println();
		         
		         out.write("\"showInternalStructure\": "); 
		         out.write(node.showInternalStructure+"");
		         out.println();
		         
		         if(nodes.indexOf(node) == nodes.size()-1)
		         {
		         out.write("}");
		         }
		         else
		         {
		         out.write("},");
		         }
		         out.println();
		         
		         nodesNames.add(node.name);
	        	 
	          }
	          
	          out.write("],");
	          out.println();
	          out.write("\"links\": [");
	          out.println();
	          
	          for(JsonEdge edge : edges)
	          {
	        	  out.write("{");
		          out.println();
		 
		         out.write("\"source\": "); 
		         out.write(Integer.toString(edge.source.id));
		         out.println();
		         
		         out.write("\"target\": "); 
		         out.write(Integer.toString(edge.target.id));
		         out.println();
		         
		         out.write("\"type\": "); 
		         out.write(edge.type);
		         out.println();
		         
		         if(edges.indexOf(edge) == edges.size()-1)
		         {
		         out.write("}");
		         }
		         else
		         {
		         out.write("},");
		         }
		         out.println();
	          }
	          
	          out.write("]}");
	          out.close();
		}
		catch(IOException ex)
		{
			System.out.println("Error when generating json file");
		}
		
	}

}

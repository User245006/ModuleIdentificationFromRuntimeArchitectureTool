package LanguarTarjanDominatorsAlgorithm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

public class Dominators {
	
	public static void calculateDominators(List<JsonNode> nodes, List<JsonEdge> edges)throws NonValidGraphException {
    	
		
		System.out.println("Dominance calculation...");
		List<JsonNode> tempNodes = new ArrayList<>();
		List<String> nodesNames = new ArrayList<>();
		
		File dominatorsFile = new File("./OutPuts/dominators.txt");
    	PrintWriter out;
    	//try{
    	//out = new PrintWriter(new FileWriter(dominatorsFile));
    	
        tempNodes = nodes;
       
    	
    	for(JsonEdge edge : edges)
    	{  
    		JsonNode.link(edge.getSource(), edge.getTarget());
    	}
    	
    	System.out.println();
	    List<JsonNode> nodesWithParent = new ArrayList<>();
	    for(JsonNode node : tempNodes)
	    {  if(node.getParents().size()!=0)
	        {  
	    	   nodesWithParent.add(node);
	        }
	    }
        nodesWithParent.add(0, tempNodes.get(0));
        //nodes = Lists.newArrayList(nodesWithParent);
        edges = new ArrayList<JsonEdge>();
        System.out.println("nodes with parents: "+nodesWithParent);
        
    	/*JsonNode a = new JsonNode("a");
    	JsonNode b = new JsonNode("b");
    	JsonNode c = new JsonNode("c");
    	JsonNode d = new JsonNode("d");
    	
    	JsonNode.link(a,b);
    	JsonNode.link(c,d);*/
    	//nodes = Lists.newArrayList(a,b,c,d);
       // edges = new ArrayList<JsonEdge>();
    	
	    final DirectedGraph<JsonNode, JsonEdge> graph = new DirectedGraph<JsonNode, JsonEdge>(tempNodes, edges);

	    final Tree<JsonNode> tree = LengauerTarjan.calculate(graph, tempNodes.get(0)).first();

	    
	    for(JsonNode node : tempNodes)
	    {  System.out.println("#####################################################################");
	    	int i = 0;
	        System.out.println("node: "+node);
	        System.out.println("tree root: "+tree.getRootNode());
	    	ITreeNode<JsonNode> bnode = findNode(tree.getRootNode(), node);
	    	//out.write(" ");
	    	System.out.println("bnode: "+bnode);
	    	System.out.println("#####################################################################");
	  
	    }
	    	/*out.write("node: "+bnode+" dominates: "+bnode.getChildren());
	    	node.internalStructure = node.internalStructure.concat("[ ");
	    	 
	    	 JsonNode actualNode = null;
	    	 for(ITreeNode<JsonNode> n : bnode.getChildren())
	    	   {    String nodeN = n.toString().substring(n.toString().indexOf("<")+1, n.toString().indexOf(">"));
	    	        for(JsonNode an : tempNodes)
	    	        {
	    	        	if(an.name.equals(nodeN))
	    	        	{
	    	        		actualNode = an;
	    	        	}
	    	        }
	    	        System.out.println("actual Node: "+actualNode);
	    	        if(actualNode != null)
	    	        {
		    		    //System.out.println(".... :"+bnode.toString().substring(bnode.toString().indexOf("<")+1, bnode.toString().indexOf(">")));
			    		node.internalStructure = node.internalStructure.concat(String.valueOf(actualNode.id));
			    		if(i!=bnode.getChildren().size()-1)
			    		{
			    		  node.internalStructure = node.internalStructure.concat(", ");
			    		}
			    	  	i++;
	    	        }
	    	   }
	    	
	    	node.internalStructure = node.internalStructure.concat("]");
	    	System.out.println("node internal structure: "+node.internalStructure.toString());
	    	out.println();
	    	i = 0;
	    }
	    
	    out.close();
    	}
    	catch(IOException ex)
		{
			System.out.println("Error in dominators class");
		}*/
	    
	}  


  private static ITreeNode<JsonNode> findNode(final ITreeNode<JsonNode> treeNode, final JsonNode b) {
	  
    if (treeNode.getObject() == b) {
      return treeNode;
    }

    for (final ITreeNode<JsonNode> child : treeNode.getChildren()) {
    	
      final ITreeNode<JsonNode> m = findNode(child, b);

      if (m != null) {
        return m;
      }
    }

    return null;
}
}

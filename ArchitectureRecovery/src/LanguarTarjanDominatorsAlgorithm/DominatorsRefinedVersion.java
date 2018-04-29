package LanguarTarjanDominatorsAlgorithm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

public class DominatorsRefinedVersion {
	
	public static HashMap<String, List<String>> dominators = new HashMap<>();
	
public static void calculateDominators(List<JsonNode> nodes, List<JsonEdge> edges)throws NonValidGraphException {
    	
		
		System.out.println("Dominance calculation...");
		List<JsonNode> tempNodes = new ArrayList<>();
		List<String> nodesNames = new ArrayList<>();
		
		File dominatorsFile = new File("./OutPuts/dominators.txt");
    	PrintWriter out;
    	try{
    	out = new PrintWriter(new FileWriter(dominatorsFile));
    	
        tempNodes = nodes;
       
    	
    	for(JsonEdge edge : edges)
    	{  
    		JsonNode.link(edge.getSource(), edge.getTarget());
    	}
    	
    	System.out.println();
	    List<JsonNode> nodesWithoutParent = new ArrayList<>();
	    for(JsonNode node : tempNodes)
	    {  if(node.getParents().size()==0 || (node.getParents().size() ==1.&& node.getParents().get(0).equals(node)))
	        {  
	    	   nodesWithoutParent.add(node);
	        }
	    }
	    
	    if(nodesWithoutParent.size()>1)
	    {
	    	JsonNode mainNode = new JsonNode("MainClass");
	        JsonGraph newGraph;
	        mainNode.id = 0;
	        mainNode.label = " ";
	        mainNode.position = " ";
	        mainNode.show = "true";
	        mainNode.internalStructure = " ";
	        mainNode.showInternalStructure = "true";
	        
	        tempNodes.add(0,mainNode);
	        for(JsonNode n : nodesWithoutParent)
	        {
	          JsonEdge edge = new JsonEdge();
	          edge.source = mainNode;
	          edge.target = n;
	          edge.type = "";
	          edges.add(edge);
	          JsonNode.link(edge.getSource(), edge.getTarget());
	        }
	        
	    }
	    for(JsonEdge edge : edges)
	    {
	    System.out.println(edge.getSource()+" -> "+edge.getTarget());
	    }
	    //nodesWithoutParent.add(0, tempNodes.get(0));
        nodes = Lists.newArrayList(tempNodes);
        edges = new ArrayList<JsonEdge>();
        
    	/*JsonNode a = new JsonNode("a");
    	JsonNode b = new JsonNode("b");
    	JsonNode c = new JsonNode("c");
    	JsonNode d = new JsonNode("d");
    	
    	JsonNode.link(a,b);
    	JsonNode.link(c,d);*/
    	//nodes = Lists.newArrayList(a,b,c,d);
       // edges = new ArrayList<JsonEdge>();
    	
	    final DirectedGraph<JsonNode, JsonEdge> graph = new DirectedGraph<JsonNode, JsonEdge>(nodes, edges);

	    final Tree<JsonNode> tree = LengauerTarjan.calculate(graph, nodes.get(0)).first();
	    System.out.println("tree: "+tree.getRootNode());
        System.out.println(nodes);
	    for(JsonNode node : nodes)
	    {  
	    	dominators.put(node.name, new ArrayList());
	    	int i = 0;
	    	System.out.println("treeNode: "+tree.getRootNode());
	    	ITreeNode<JsonNode> bnode = findNode(tree.getRootNode(), node);
	    	//out.write(" ");
	    	System.out.println(node.name);
	    	if(bnode!=null){
	    	out.write("node: "+bnode+" dominates: "+bnode.getChildren());
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
	    	        
	    	        if(actualNode != null)
	    	        {
		    		    //System.out.println(".... :"+bnode.toString().substring(bnode.toString().indexOf("<")+1, bnode.toString().indexOf(">")));
			    		node.internalStructure = node.internalStructure.concat(String.valueOf(actualNode.id));
			    		dominators.get(node.name).add(actualNode.name);
			    		if(i!=bnode.getChildren().size()-1)
			    		{
			    		  node.internalStructure = node.internalStructure.concat(", ");
			    		}
			    	  	i++;
	    	        }
	    	   }
	    	
	    	node.internalStructure = node.internalStructure.concat("]");
	    	
	    	out.println();
	    	i = 0;
	    }
    	}
	    out.close();
    	}
    	catch(IOException ex)
		{
			System.out.println("Error in dominators class");
		}
     
	    
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

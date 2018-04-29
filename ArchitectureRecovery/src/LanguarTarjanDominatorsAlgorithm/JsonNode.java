package LanguarTarjanDominatorsAlgorithm;

import java.util.ArrayList;
import java.util.List;

public class JsonNode implements IGraphNode<JsonNode>{
	
	public String name;
	
	public int id;
	
	public String label;
	
	public String position ;
	
	public String show;
  	
	public String internalStructure;
  	
	public String showInternalStructure;
  	
  	
  	// for dominance calculation
  	private final List<JsonNode> children = new ArrayList<JsonNode>();
	private final List<JsonNode> parents = new ArrayList<JsonNode>();

  	
  	public JsonNode(final String text) {
	    this.name = text;
	  }

	  public static void link(final JsonNode parent, final JsonNode child) {
		
	        parent.children.add(child);
		  
	    child.parents.add(parent);
		
	   
	  }

	  @Override
	  public List<JsonNode> getChildren() {
	    return children;
	  }
	  
	  public ArrayList<String> getChildrenNames()
	  {
		  ArrayList<String> childNames = new ArrayList<String>();
		  for(JsonNode node : children)
		  {
			  childNames.add(node.name);
			  childNames.add(" ,");
		  }
		  
		  return childNames;
	  }

	  @Override
	  public List<JsonNode> getParents() {
	    return parents;
	  }

	  @Override
	  public String toString() {
	    return name;
	  }
}

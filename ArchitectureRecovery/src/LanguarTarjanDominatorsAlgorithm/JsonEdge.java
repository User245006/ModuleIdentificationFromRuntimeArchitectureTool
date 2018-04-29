package LanguarTarjanDominatorsAlgorithm;

public class JsonEdge implements IGraphEdge<JsonNode>{
	
	public JsonNode source;
	
  	public JsonNode target;
  	
  	public String type;

  	public JsonEdge() {
	    this(null, null);
	  }

	  public JsonEdge(final JsonNode source, final JsonNode target) {
		  this.source = source;
		  this.target = target;
	  }

	  
	  public JsonNode getSource() {
	    return source;
	  }

	  
	  public JsonNode getTarget() {
	    return target;
	  }
}

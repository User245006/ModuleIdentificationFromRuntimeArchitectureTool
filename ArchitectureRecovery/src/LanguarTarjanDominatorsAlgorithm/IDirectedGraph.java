package LanguarTarjanDominatorsAlgorithm;

import java.util.List;

public interface IDirectedGraph<NodeType, EdgeType> extends Iterable<NodeType>{
	
	int edgeCount();

	  
	  List<EdgeType> getEdges();

	  
	  List<NodeType> getNodes();

	  
	  int nodeCount();

}

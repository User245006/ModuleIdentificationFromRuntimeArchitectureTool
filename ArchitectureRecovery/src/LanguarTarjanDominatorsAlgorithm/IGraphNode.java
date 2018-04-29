package LanguarTarjanDominatorsAlgorithm;

import java.util.List;

public interface IGraphNode <T> {
	  
	  List<? extends T> getChildren();

	  List<? extends T> getParents();
	}

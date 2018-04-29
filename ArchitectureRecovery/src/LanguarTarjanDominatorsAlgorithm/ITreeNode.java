package LanguarTarjanDominatorsAlgorithm;

import java.util.ArrayList;
import java.util.List;

public interface ITreeNode <ObjectType> {
	 
	
	  void addChild(ITreeNode<ObjectType> child);

	 
	  List<? extends ITreeNode<ObjectType>> getChildren();

	  
	  ObjectType getObject();

	 
	  ITreeNode<ObjectType> getParent();

	  void removeChild(ITreeNode<ObjectType> child);

	  
	  void setParent(ITreeNode<ObjectType> parent);

	  ArrayList<String> getChildrenNames();
	}
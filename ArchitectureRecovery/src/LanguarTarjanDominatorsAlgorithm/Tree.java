package LanguarTarjanDominatorsAlgorithm;

public class Tree <ObjectType> {
	  
	  private final ITreeNode<ObjectType> m_rootNode;

	  
	  public Tree(final ITreeNode<ObjectType> rootNode) {
	    m_rootNode = rootNode;
	  }

	  public ITreeNode<ObjectType> getRootNode() {
	    return m_rootNode;
	  }
	}

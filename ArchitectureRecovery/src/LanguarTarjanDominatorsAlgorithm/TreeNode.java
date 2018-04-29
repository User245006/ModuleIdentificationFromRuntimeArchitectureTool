package LanguarTarjanDominatorsAlgorithm;

import java.util.ArrayList;
import java.util.List;

public class TreeNode <ObjectType> implements ITreeNode<ObjectType> {
	  
	  private ITreeNode<ObjectType> m_parent = null;

	  
	  private final List<ITreeNode<ObjectType>> m_children = new ArrayList<ITreeNode<ObjectType>>();

	  private final ObjectType m_object;

	  public TreeNode(final ObjectType object) {
	    m_object = object;
	  }

	  @Override
	  public void addChild(final ITreeNode<ObjectType> child) {
	    m_children.add(child);
	  }

	  @Override
	  public List<ITreeNode<ObjectType>> getChildren() {
	    return new ArrayList<ITreeNode<ObjectType>>(m_children);
	  }

	  @Override
	  public ObjectType getObject() {
	    return m_object;
	  }

	  @Override
	  public ITreeNode<ObjectType> getParent() {
	    return m_parent;
	  }

	  @Override
	  public void removeChild(final ITreeNode<ObjectType> node) {
	    m_children.remove(node);
	  }

	  @Override
	  public void setParent(final ITreeNode<ObjectType> node) {
	    m_parent = node;
	  }

	  @Override
	  public String toString() {
	    return "<" + m_object.toString() + ">";
	  }

	@Override
	public ArrayList<String> getChildrenNames() {
		// TODO Auto-generated method stub
		return null;
	}
	}


package LanguarTarjanDominatorsAlgorithm;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;


import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public final class LengauerTarjan {
	
	  private static <NodeType extends IGraphNode<NodeType>> int depthFirstSearch(
	      final NodeType parent, final NodeType rootNode, final HashMap<NodeType, Integer> dfnums,
	      final HashMap<Integer, NodeType> vertex, final HashMap<NodeType, NodeType> parents, int dfnum) {
	    
		   parents.put(rootNode, parent);
	       vertex.put(dfnum, rootNode);
	       dfnums.put(rootNode, dfnum);
	       dfnum++;

	    final Stack<Pair<NodeType, NodeType>> nodeStack = new Stack<Pair<NodeType, NodeType>>();
	    for (final NodeType child : Lists.reverse(rootNode.getChildren())) {
	      nodeStack.push(new Pair<NodeType, NodeType>(child, rootNode));
	    }
	    while (!nodeStack.empty()) {
	      final Pair<NodeType, NodeType> currentElement = nodeStack.pop();
	      final NodeType currentNode = currentElement.first();
	      final NodeType currentParent = currentElement.second();

	      if (dfnums.get(currentNode) == -1) {
	        dfnums.put(currentNode, dfnum);
	        vertex.put(dfnum, currentNode);
	        parents.put(currentNode, currentParent);

	        dfnum++;

	        for (final NodeType child : Lists.reverse(currentNode.getChildren())) {
	          nodeStack.push(new Pair<NodeType, NodeType>(child, currentNode));
	        }
	      }
	    }
	    return dfnum - 1;
	  }

	  private static <NodeType extends IGraphNode<NodeType>> NodeType getAncestorWithLowestSemi(
	      final NodeType node, final HashMap<NodeType, Integer> dfnum,
	      final HashMap<NodeType, NodeType> semi, final HashMap<NodeType, NodeType> ancestor,
	      final HashMap<NodeType, NodeType> best) {
	    final NodeType a = ancestor.get(node);

	    if (ancestor.get(a) != null) {
	      final NodeType b = getAncestorWithLowestSemi(a, dfnum, semi, ancestor, best);

	      ancestor.put(node, ancestor.get(a));

	      if (dfnum.get(semi.get(b)) < dfnum.get(semi.get(best.get(node)))) {
	        best.put(node, b);
	      }
	    }

	    return best.get(node);
	  }


	  private static <NodeType extends IGraphNode<NodeType>> void link(final NodeType parent,
	      final NodeType node, final HashMap<NodeType, NodeType> ancestor,
	      final HashMap<NodeType, NodeType> best) {
	    ancestor.put(node, parent);
	    best.put(node, node);
	  }


	  public static <NodeType extends 
	  IGraphNode<NodeType>> Pair<Tree<NodeType>, HashMap<NodeType, ITreeNode<NodeType>>> calculate(
	      final Collection<NodeType> nodes, final NodeType rootNode) throws NonValidGraphException {
	    Preconditions.checkNotNull(nodes, "Error: Nodes argument can not be null");

	    if (nodes.size() == 0) {
	      return new Pair<Tree<NodeType>, HashMap<NodeType, ITreeNode<NodeType>>>(null, null);
	    }

	    Preconditions.checkNotNull(rootNode, "Error: Root node argument can not be null");
	    Preconditions.checkArgument(nodes.contains(rootNode),
	        "Error: Root node is not part of the graph");

	    int entryNodes = 0;

	    for (final NodeType node : nodes) {
	    
	      if (node.getParents().size() == 0) {
	    	entryNodes++;
	      }
	    }
        System.out.println("entry nodes number: "+entryNodes);
	    if (entryNodes > 1) {
	      throw new NonValidGraphException(
	          "Error: Can not calculate dominator trees for graphs with more than one entry node");
	    }

	    final HashMap<NodeType, Set<NodeType>> bucket = new HashMap<NodeType, Set<NodeType>>();
	    final HashMap<NodeType, NodeType> idom = new HashMap<NodeType, NodeType>();
	    final HashMap<NodeType, NodeType> best = new HashMap<NodeType, NodeType>();
	    final HashMap<NodeType, NodeType> semi = new HashMap<NodeType, NodeType>();
	    final HashMap<NodeType, NodeType> ancestor = new HashMap<NodeType, NodeType>();
	    final HashMap<NodeType, NodeType> samedom = new HashMap<NodeType, NodeType>();
	    final HashMap<Integer, NodeType> vertex = new HashMap<Integer, NodeType>();
	    final HashMap<NodeType, NodeType> parents = new HashMap<NodeType, NodeType>();
	    final HashMap<NodeType, Integer> dfnum = new HashMap<NodeType, Integer>();

	    for (final NodeType node : nodes) {
	      bucket.put(node, new HashSet<NodeType>());
	      dfnum.put(node, -1);
	      semi.put(node, null);
	      ancestor.put(node, null);
	      idom.put(node, null);
	      samedom.put(node, null);
	    }

	    final int n = depthFirstSearch(null, rootNode, dfnum, vertex, parents, 0);

	    for (int i = n; i >= 1; i--) {
	      final NodeType node = vertex.get(i);
	      final NodeType parent = parents.get(node);
	      NodeType s = parent;

	      for (final NodeType v : node.getParents()) {
	        NodeType temps = null;

	        if (dfnum.get(v) <= dfnum.get(node)) {
	          temps = v;
	        } else {
	          temps = semi.get(getAncestorWithLowestSemi(v, dfnum, semi, ancestor, best));
	        }

	        if (dfnum.get(temps) < dfnum.get(s)) {
	          s = temps;
	        }
	      }

	      semi.put(node, s);
	      bucket.get(s).add(node);

	      link(parent, node, ancestor, best);

	      for (final NodeType v : bucket.get(parent)) {
	        final NodeType y = getAncestorWithLowestSemi(v, dfnum, semi, ancestor, best);

	        if (semi.get(y) == semi.get(v)) {
	          idom.put(v, parent);
	        } else {
	          samedom.put(v, y);
	        }
	      }

	      bucket.get(parent).clear();
	    }

	    for (int i = 1; i <= n; i++) {
	      final NodeType node = vertex.get(i);

	      if (samedom.get(node) != null) {
	        idom.put(node, idom.get(samedom.get(node)));
	      }
	    }

	    ITreeNode<NodeType> treeRoot = null;

	    final HashMap<NodeType, ITreeNode<NodeType>> nodeMap =
	        new HashMap<NodeType, ITreeNode<NodeType>>();

	    for (final Entry<NodeType, NodeType> node : idom.entrySet()) {
	      final TreeNode<NodeType> treeNode = new TreeNode<NodeType>(node.getKey());

	      nodeMap.put(node.getKey(), treeNode);

	      if (node.getValue() == null) {
	        treeRoot = treeNode;
	      }
	    }

	    for (final Entry<NodeType, NodeType> node : idom.entrySet()) {
	      final NodeType dominatedNode = node.getKey();
	      final NodeType dominatorNode = node.getValue();

	      if (dominatorNode != null) {
	        nodeMap.get(dominatorNode).addChild(nodeMap.get(dominatedNode));
	        nodeMap.get(dominatedNode).setParent(nodeMap.get(dominatorNode));
	      }
	    }

	    return new Pair<Tree<NodeType>, HashMap<NodeType, ITreeNode<NodeType>>>(new Tree<NodeType>(
	        treeRoot), nodeMap);
	  }

	  public static <NodeType extends IGraphNode<NodeType>> Pair<Tree<NodeType>, HashMap<NodeType, ITreeNode<NodeType>>> calculate(
	      final IDirectedGraph<NodeType, ?> graph, final NodeType rootNode)
	      throws NonValidGraphException {
	    Preconditions.checkNotNull(graph, "Error: Graph argument can not be null");
	    Preconditions.checkNotNull(rootNode, "Error: Root node argument can not be null");

	    if (graph.nodeCount() == 0) {
	      return new Pair<Tree<NodeType>, HashMap<NodeType, ITreeNode<NodeType>>>(null, null);
	    }

	    return calculate(graph.getNodes(), rootNode);
	  }
	}

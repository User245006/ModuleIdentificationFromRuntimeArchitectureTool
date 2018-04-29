package LanguarTarjanDominatorsAlgorithm;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;

public class DirectedGraph <NodeType extends IGraphNode<NodeType>, EdgeType extends IGraphEdge<NodeType>>
implements Iterable<NodeType>, IDirectedGraph<NodeType, EdgeType> {
	
	private final List<NodeType> m_nodes;
	
	
	private final List<EdgeType> m_edges;
	
	
	private final HashMultimap<NodeType, EdgeType> m_incomingEdges = HashMultimap.create();
	
	
	private final HashMultimap<NodeType, EdgeType> m_outgoingEdges = HashMultimap.create();
	
	
	public DirectedGraph(final List<NodeType> nodes, final List<EdgeType> edges) {
		
	Preconditions.checkNotNull(nodes, "Error: Nodes argument can not be null");
	Preconditions.checkNotNull(edges, "Error: Edges argument can not be null");
	
	for (final NodeType node : nodes) {
	  Preconditions.checkNotNull(node, "Error: Node list contains null-nodes");
	}
	
	for (final EdgeType edge : edges) {
	  m_outgoingEdges.put(edge.getSource(), edge);
	  m_incomingEdges.put(edge.getTarget(), edge);
	}
	
	m_nodes = nodes;
	m_edges = edges;
	}
	
	@Override
	public int edgeCount() {
	return m_edges.size();
	}
	
	@Override
	public List<EdgeType> getEdges() {
	return Collections.unmodifiableList(m_edges);
	}
	
	public Set<EdgeType> getIncomingEdges(final NodeType node) {
	return m_incomingEdges.get(node);
	}
	
	@Override
	public List<NodeType> getNodes() {
	return Collections.unmodifiableList(m_nodes);
	}
	
	public Set<EdgeType> getOutgoingEdges(final NodeType node) {
	return m_outgoingEdges.get(node);
	}
	
	@Override
	public Iterator<NodeType> iterator() {
	return m_nodes.iterator();
	}
	
	@Override
	public int nodeCount() {
	return m_nodes.size();
	}
}
package org.geotools.coverage.io.metadata;

import java.util.List;
import java.util.Map;

public class MetadataNode {
	
	public List<MetadataNode> getNodes() {
		return nodes;
	}

	public void setNodes(List<MetadataNode> nodes) {
		this.nodes = nodes;
	}

	public Map<String, MetadataAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, MetadataAttribute> attributes) {
		this.attributes = attributes;
	}

	private List<MetadataNode> nodes;
	
	private Map<String, MetadataAttribute> attributes;

}

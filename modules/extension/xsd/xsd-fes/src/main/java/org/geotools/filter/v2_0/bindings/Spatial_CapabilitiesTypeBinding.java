package org.geotools.filter.v2_0.bindings;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.geotools.filter.v2_0.FES;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.capability.GeometryOperand;
import org.opengis.filter.capability.SpatialCapabilities;
import org.opengis.filter.capability.SpatialOperator;
import org.opengis.filter.capability.SpatialOperators;

public class Spatial_CapabilitiesTypeBinding extends AbstractComplexBinding {
	
	private FilterFactory factory;
	
	public Spatial_CapabilitiesTypeBinding(FilterFactory filterFactory) {
		factory = filterFactory;
	}
	
	@Override
	public QName getTarget() {
		return FES.Spatial_CapabilitiesType;
	}

	@Override
	public Class getType() {
		return SpatialCapabilities.class;
	}
	
	@Override
	public Object parse(ElementInstance instance, Node node, Object value)
			throws Exception {
		GeometryOperand[] geometryOperands = parseGeometryOperands(node);
		
		List<SpatialOperator> spatialOperators = new ArrayList<SpatialOperator>();
		Node nodeSpatialOperators = node.getChild("SpatialOperators");
		List<Node> spatialOperatorNodes = nodeSpatialOperators.getChildren("SpatialOperator");
		
		for (Node spatialOperatorNode : spatialOperatorNodes) {
			String name = (String)spatialOperatorNode.getAttributeValue("name");
			
			GeometryOperand[] gops = parseGeometryOperands(spatialOperatorNode);
			SpatialOperator op = factory.spatialOperator(name, gops);
			
			spatialOperators.add(op);
		}
		
		SpatialOperators spatial = factory.spatialOperators(spatialOperators.toArray(new SpatialOperator[spatialOperators.size()]));
		
		return factory.spatialCapabilities(geometryOperands, spatial);
	}

	private GeometryOperand[] parseGeometryOperands(Node node) {
		Node nodeGeometryOperands = node.getChild("GeometryOperands");
		if (nodeGeometryOperands == null) return null;
		
		List<GeometryOperand> parsedGeometryOperands = new ArrayList<GeometryOperand>();
		List<Object> rawGeometryOperands = nodeGeometryOperands.getChildValues("GeometryOperand");
		if (rawGeometryOperands != null) {
			for (Object o : rawGeometryOperands) {
				if (o instanceof QName) {
					QName qname = (QName) o;
					GeometryOperand op = GeometryOperand.get(qname.getNamespaceURI(), qname.getLocalPart());
					parsedGeometryOperands.add(op);
				}
			}
		}
		GeometryOperand [] geometryOperands = parsedGeometryOperands.toArray(new GeometryOperand[parsedGeometryOperands.size()]);
		return geometryOperands;
	}

}

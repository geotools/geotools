package org.geotools.filter.v2_0.bindings;

import java.util.List;

import javax.xml.namespace.QName;

import org.geotools.filter.v2_0.FES;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.AbstractComplexEMFBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.capability.IdCapabilities;

public class Id_CapabilitiesTypeBinding extends AbstractComplexBinding {

	private FilterFactory factory;
	
	public Id_CapabilitiesTypeBinding(FilterFactory filterFactory) {
		factory = filterFactory;
	}
	
	@Override
	public QName getTarget() {
		return FES.Id_CapabilitiesType;
	}

	@Override
	public Class getType() {
		return IdCapabilities.class;
	}
	
	@Override
	public Object parse(ElementInstance instance, Node node, Object value)
			throws Exception {
		boolean eid = false; // RecordID (as in catalog)
		boolean fid = false; // FeatureID 

		List<Node> children = node.getChildren();
		for (Node child : children) {
			Object o = child.getValue();

			if (!(o instanceof QName)) continue;
			QName name = (QName)o;
			
			if (FES.ResourceId.equals(name)) {
				fid = true;
			} else if (name.getNamespaceURI().startsWith("http://www.opengis.net/cat/csw/") &&
					name.getLocalPart().equals("RecordId")) {
				// FES 2.0 is very unclear about this. See 09-026r1 FES 2.0 7.14.3
				eid = true;
			}
		}
		return factory.idCapabilities(eid, fid);
	}
	
}

package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;

import net.opengis.fes20.Fes20Factory;
import net.opengis.fes20.ResourceIdentifierType;

import org.geotools.filter.v2_0.FES;
import org.geotools.xml.AbstractComplexEMFBinding;

public class ResourceIdentifierTypeBinding extends AbstractComplexEMFBinding {

	public ResourceIdentifierTypeBinding(Fes20Factory factory) {
		super(factory);
	}

	@Override
	public QName getTarget() {
		return FES.ResourceIdentifierType;
	}
	
	@Override
	public Class getType() {
		return ResourceIdentifierType.class;
	}
	
}

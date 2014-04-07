package org.geotools.filter.v2_0.bindings;

import java.util.List;

import javax.xml.namespace.QName;

import net.opengis.fes20.Fes20Factory;
import net.opengis.fes20.IdCapabilitiesType;

import org.geotools.filter.v2_0.FES;
import org.geotools.xml.AbstractComplexEMFBinding;

public class Id_CapabilitiesTypeBinding extends AbstractComplexEMFBinding {

	public Id_CapabilitiesTypeBinding(Fes20Factory factory) {
		super(factory);
	}
	
	@Override
	public QName getTarget() {
		return FES.Id_CapabilitiesType;
	}

	@Override
	public Class getType() {
		return IdCapabilitiesType.class;
	}
}

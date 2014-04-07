package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;

import net.opengis.fes20.ConformanceType;
import net.opengis.fes20.Fes20Factory;

import org.geotools.filter.v2_0.FES;
import org.geotools.xml.AbstractComplexEMFBinding;

public class ConformanceTypeBinding extends AbstractComplexEMFBinding {

	public ConformanceTypeBinding(Fes20Factory factory) {
		super(factory);
	}
	
	@Override
	public QName getTarget() {
		return FES.ConformanceType;
	}
	
	@Override
	public Class getType() {
		return ConformanceType.class;
	}

}

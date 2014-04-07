package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;

import net.opengis.fes20.AvailableFunctionsType;
import net.opengis.fes20.Fes20Factory;

import org.eclipse.emf.ecore.EObject;
import org.geotools.filter.v2_0.FES;
import org.geotools.xml.AbstractComplexEMFBinding;

public class AvailableFunctionsTypeBinding extends
		AbstractComplexEMFBinding {

	public AvailableFunctionsTypeBinding(Fes20Factory factory) {
		super(factory);
	}
	
	@Override
	public QName getTarget() {
		return FES.AvailableFunctionsType;
	}

	@Override
	public Class getType() {
		return AvailableFunctionsType.class;
	}
}

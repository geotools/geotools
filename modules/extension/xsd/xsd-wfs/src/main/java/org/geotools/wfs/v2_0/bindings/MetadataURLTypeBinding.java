package org.geotools.wfs.v2_0.bindings;

import javax.xml.namespace.QName;

import net.opengis.wfs20.FeatureTypeType;
import net.opengis.wfs20.MetadataURLType;
import net.opengis.wfs20.Wfs20Factory;

import org.geotools.wfs.v2_0.WFS;
import org.geotools.xml.AbstractComplexEMFBinding;

public class MetadataURLTypeBinding extends AbstractComplexEMFBinding {

	public MetadataURLTypeBinding(Wfs20Factory factory) {
		super(factory);
	}
	
	@Override
	public Class getType() {
		return MetadataURLType.class;
	}
	
	@Override
	public QName getTarget() {
		return WFS.MetadataURLType;
	}

}

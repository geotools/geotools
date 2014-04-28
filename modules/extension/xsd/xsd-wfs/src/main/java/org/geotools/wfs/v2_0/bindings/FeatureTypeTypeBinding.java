/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */


package org.geotools.wfs.v2_0.bindings;

import java.net.URI;

import javax.xml.namespace.QName;

import net.opengis.wfs20.FeatureTypeType;
import net.opengis.wfs20.OutputFormatListType;
import net.opengis.wfs20.Wfs20Factory;

import org.eclipse.emf.ecore.EObject;
import org.geotools.wfs.v2_0.WFS;
import org.geotools.xml.AbstractComplexEMFBinding;

public class FeatureTypeTypeBinding extends AbstractComplexEMFBinding {
	private Wfs20Factory factory;
	
	public FeatureTypeTypeBinding(Wfs20Factory factory) {
		super(factory);
		this.factory = factory;
	}
	
	@Override
	public QName getTarget() {
		return WFS.FeatureTypeType;
	}

	@Override
	public Class getType() {
		return FeatureTypeType.class;
	}
	
	@Override
	protected void setProperty(EObject object, String property, Object value, boolean lax) {
		if ("OtherCRS".equals(property)) {
			String stringValue = null;
			if (value instanceof String) {
				stringValue = (String)value;
			} else if (value instanceof URI) {
				stringValue = ((URI)value).toString();
			}
			
			if (stringValue != null) {
				((FeatureTypeType)object).getOtherCRS().add(stringValue);
				return;
			}
		} else if ("OutputFormats".equals(property)) {
			String outputFormatValue = null;
			if (value instanceof String) {
				outputFormatValue = (String)value;
			}
			
			if (outputFormatValue != null) {
				OutputFormatListType oflt = ((FeatureTypeType)object).getOutputFormats();
				
				if (oflt == null) {
					oflt = factory.createOutputFormatListType();
				}
				
				oflt.getFormat().add(outputFormatValue);
				
				return;
			}
		}
		
		super.setProperty(object, property, value, lax);
	}
}

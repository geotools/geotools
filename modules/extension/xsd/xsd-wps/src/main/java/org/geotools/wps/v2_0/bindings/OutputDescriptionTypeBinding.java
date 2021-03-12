/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wps.v2_0.bindings;

import javax.xml.namespace.QName;
import net.opengis.wps20.DataDescriptionType;
import net.opengis.wps20.OutputDescriptionType;
import net.opengis.wps20.Wps20Factory;
import net.opengis.wps20.Wps20Package;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap.Entry;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.geotools.wps.v2_0.WPS;
import org.geotools.xsd.AbstractComplexEMFBinding;

/**
 * Binding object for the type http://www.opengis.net/wps/2.0:OutputDescriptionType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;complexType name="OutputDescriptionType" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *
 *  		&lt;annotation&gt;
 *
 *  			&lt;documentation&gt;Description of a process Output. &lt;/documentation&gt;
 *
 *  		&lt;/annotation&gt;
 *
 *  		&lt;complexContent&gt;
 *
 *  			&lt;extension base="wps:DescriptionType"&gt;
 *
 *  				&lt;annotation&gt;
 *
 *  					&lt;documentation&gt;
 *
 *  						In this use, the DescriptionType shall describe a process output.
 *
 *  					&lt;/documentation&gt;
 *
 *  				&lt;/annotation&gt;
 *
 *  				&lt;choice&gt;
 *
 *  					&lt;element ref="wps:DataDescription"/&gt;
 *
 *  					&lt;!-- Nested output --&gt;
 *
 *  					&lt;element maxOccurs="unbounded" minOccurs="1" name="Output" type="wps:OutputDescriptionType"/&gt;
 *
 *  				&lt;/choice&gt;
 *
 *  			&lt;/extension&gt;
 *
 *  		&lt;/complexContent&gt;
 *
 *  	&lt;/complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class OutputDescriptionTypeBinding extends AbstractComplexEMFBinding {

    public OutputDescriptionTypeBinding(Wps20Factory factory) {
        super(factory);
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return WPS.OutputDescriptionType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Class getType() {
        return OutputDescriptionType.class;
    }

    @Override
    protected void setProperty(EObject eObject, String property, Object value, boolean lax) {
        if (DataDescriptionType.class.isAssignableFrom(value.getClass())) {
            Entry entry =
                    FeatureMapUtil.createEntry(
                            Wps20Package.Literals.OUTPUT_DESCRIPTION_TYPE__DATA_DESCRIPTION, value);
            super.setProperty(eObject, "dataDescriptionGroup", entry, lax);
        }
        super.setProperty(eObject, property, value, lax);
    }
}

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg.wps.xml;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.geotools.geopkg.wps.GeoPackageProcessRequest;
import org.geotools.geopkg.wps.GeoPackageProcessRequest.Layer;
import org.geotools.xml.*;
import org.geotools.xs.bindings.XSQNameBinding;
import org.opengis.filter.Filter;


import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/gpkg:geopkgtype_features.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;xs:complexType name="geopkgtype_features" xmlns:xs="http://www.w3.org/2001/XMLSchema"&gt;
 *            &lt;xs:complexContent&gt;
 *              &lt;xs:extension base="layertype"&gt;
 *                &lt;xs:sequence&gt;
 *                  &lt;xs:element name="featuretype" type="xs:string"/&gt;
 *                  &lt;xs:element minOccurs="0" name="propertynames" type="xs:string"/&gt;
 *                  &lt;xs:element minOccurs="0" name="filter" type="fes:FilterType"/&gt;
 *                &lt;/xs:sequence&gt;
 *              &lt;/xs:extension&gt;
 *            &lt;/xs:complexContent&gt;
 *          &lt;/xs:complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class Geopkgtype_featuresBinding extends LayertypeBinding {
    
        NamespaceContext namespaceContext;
        
        public Geopkgtype_featuresBinding(NamespaceContext namespaceContext) {
            this.namespaceContext = namespaceContext;
        }

	/**
	 * @generated
	 */
	public QName getTarget() {
		return GPKG.geopkgtype_features;
	}
	
	@Override
        public Layer parseLayer(ElementInstance instance, Node node, Object value) throws Exception {
	    XSQNameBinding nameBinding = new XSQNameBinding(namespaceContext);
	    
            GeoPackageProcessRequest.FeaturesLayer layer = new GeoPackageProcessRequest.FeaturesLayer();
            layer.setFeatureType((QName) nameBinding.parse(null, (String) node.getChildValue("featuretype")));
            String pns = (String) node.getChildValue("propertynames");
            if (pns != null) {               
                Set<QName> qnames = new HashSet<QName>();
                for (String pn : Arrays.asList(pns.split(","))) {
                    qnames.add( (QName) nameBinding.parse(null, pn.trim() ));
                }
                layer.setPropertyNames( qnames);
            }
            layer.setFilter((Filter) node.getChildValue("filter"));
            Boolean indexed = (Boolean) node.getChildValue("indexed");
            if (indexed != null) {
                layer.setIndexed(indexed);
            }
            return layer;
        }

}
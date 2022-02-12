/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package org.geotools.wmts.bindings;

import java.util.List;
import java.util.stream.Collectors;
import javax.xml.namespace.QName;
import net.opengis.ows11.OnlineResourceType;
import net.opengis.ows11.OperationsMetadataType;
import net.opengis.ows11.ServiceIdentificationType;
import net.opengis.ows11.ServiceProviderType;
import net.opengis.wmts.v_1.CapabilitiesType;
import net.opengis.wmts.v_1.ContentsType;
import net.opengis.wmts.v_1.ThemesType;
import net.opengis.wmts.v_1.wmtsv_1Factory;
import org.geotools.wmts.WMTS;
import org.geotools.xsd.AbstractComplexEMFBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the element http://www.opengis.net/wmts/1.0:Capabilities.
 *
 * <p>
 *
 * <pre>
 *       <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;element name="Capabilities" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *  		&lt;annotation&gt;
 *  			&lt;documentation&gt;XML defines the WMTS GetCapabilities operation response.
 *  			ServiceMetadata document provides clients with service metadata about a specific service
 *  			instance, including metadata about the tightly-coupled data served. If the server
 *  			does not implement the updateSequence parameter, the server SHALL always
 *  			return the complete Capabilities document, without the updateSequence parameter.
 *  			When the server implements the updateSequence parameter and the
 *  			GetCapabilities operation request included the updateSequence parameter
 *  			with the current value, the server SHALL return this element with only the
 *  			"version" and "updateSequence" attributes. Otherwise, all optional elements
 *  			SHALL be included or not depending on the actual value of the Contents
 *  			parameter in the GetCapabilities operation request.
 *  			&lt;/documentation&gt;
 *  		&lt;/annotation&gt;
 *  		&lt;complexType&gt;
 *  			&lt;complexContent&gt;
 *  				&lt;extension base="ows:CapabilitiesBaseType"&gt;
 *  					&lt;sequence&gt;
 *  						&lt;element minOccurs="0" name="Contents" type="wmts:ContentsType"&gt;
 *  							&lt;annotation&gt;
 *  								&lt;documentation&gt;Metadata about the data served by this server.
 *  								For WMTS, this section SHALL contain data about layers and
 *  								TileMatrixSets&lt;/documentation&gt;
 *  							&lt;/annotation&gt;
 *  						&lt;/element&gt;
 *  						&lt;element maxOccurs="unbounded" minOccurs="0" ref="wmts:Themes"&gt;
 *  							&lt;annotation&gt;
 *  								&lt;documentation&gt;
 *  								Metadata describing a theme hierarchy for the layers
 *  								&lt;/documentation&gt;
 *  							&lt;/annotation&gt;
 *  						&lt;/element&gt;
 *  						&lt;element maxOccurs="unbounded" minOccurs="0" name="WSDL" type="ows:OnlineResourceType"&gt;
 *  							&lt;annotation&gt;
 *  								&lt;documentation&gt;Reference to a WSDL resource&lt;/documentation&gt;
 *  							&lt;/annotation&gt;
 *  						&lt;/element&gt;
 *  						&lt;element maxOccurs="unbounded" minOccurs="0" name="ServiceMetadataURL" type="ows:OnlineResourceType"&gt;
 *  							&lt;annotation&gt;
 *  								&lt;documentation&gt;
 *  								Reference to a ServiceMetadata resource on resource
 *  								oriented architectural style
 *  								&lt;/documentation&gt;
 *  							&lt;/annotation&gt;
 *  						&lt;/element&gt;
 *  					&lt;/sequence&gt;
 *  				&lt;/extension&gt;
 *  			&lt;/complexContent&gt;
 *  		&lt;/complexType&gt;
 *  	&lt;/element&gt;
 *
 *   </code>
 *     </pre>
 *
 * @generated
 */
public class CapabilitiesBinding extends AbstractComplexEMFBinding {
    wmtsv_1Factory factory;

    public CapabilitiesBinding(wmtsv_1Factory factory) {
        super();
        this.factory = factory;
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return WMTS.Capabilities;
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
        return net.opengis.wmts.v_1.CapabilitiesType.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        CapabilitiesType capabilities = factory.createCapabilitiesType();

        capabilities.setContents(node.getChildValue(ContentsType.class));
        capabilities.setOperationsMetadata(node.getChildValue(OperationsMetadataType.class));
        capabilities.setServiceIdentification(node.getChildValue(ServiceIdentificationType.class));
        capabilities.setServiceProvider(node.getChildValue(ServiceProviderType.class));
        capabilities.setUpdateSequence((String) node.getChildValue("UpdateSequence"));

        List<Node> themesChildren = node.getChildren(ThemesType.class);
        for (Node c : themesChildren) {
            capabilities.getThemes().add((ThemesType) c.getValue());
        }

        List<Node> children = node.getChildren("ServiceMetadataURL");
        for (Node c : children) {
            capabilities.getServiceMetadataURL().add((OnlineResourceType) c.getValue());
        }
        capabilities
                .getWSDL()
                .addAll(
                        node.getChildren("WSDL").stream()
                                .map(n -> (OnlineResourceType) n.getValue())
                                .collect(Collectors.toList()));
        return capabilities;
    }
}

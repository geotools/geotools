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

package org.geotools.wfs.v1_0;

import java.util.List;
import javax.xml.namespace.QName;
import net.opengis.ows10.DCPType;
import net.opengis.ows10.HTTPType;
import net.opengis.ows10.Ows10Factory;
import net.opengis.ows10.RequestMethodType;
import org.geotools.xsd.AbstractComplexEMFBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

public class DCPTypeBinding extends AbstractComplexEMFBinding {

    @Override
    public QName getTarget() {
        return WFSCapabilities.DCPType;
    }

    @Override
    public Class getType() {
        return DCPType.class;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        Ows10Factory ows10Factory = Ows10Factory.eINSTANCE;

        DCPType dcpType = ows10Factory.createDCPType();
        HTTPType httpType = ows10Factory.createHTTPType();
        dcpType.setHTTP(httpType);

        List<Node> httpChildren = node.getChildren("HTTP");
        for (Node http : httpChildren) {
            Node get = http.getChild("Get");
            if (get != null) {
                RequestMethodType methodType = createRequestMethodType(ows10Factory, get);
                httpType.getGet().add(methodType);
            }
            Node post = http.getChild("Post");
            if (post != null) {
                RequestMethodType methodType = createRequestMethodType(ows10Factory, post);
                httpType.getPost().add(methodType);
            }
        }

        return dcpType;
    }

    private RequestMethodType createRequestMethodType(
            Ows10Factory ows10Factory, Node getOrPostNode) {
        RequestMethodType methodType = ows10Factory.createRequestMethodType();
        String href = (String) getOrPostNode.getAttributeValue("onlineResource");
        methodType.setHref(href);
        return methodType;
    }
}

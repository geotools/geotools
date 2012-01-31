package org.geotools.wfs.v1_0;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import javax.xml.namespace.QName;

import net.opengis.ows10.DCPType;
import net.opengis.ows10.HTTPType;
import net.opengis.ows10.Ows10Factory;
import net.opengis.ows10.RequestMethodType;
import net.opengis.ows10.WGS84BoundingBoxType;

import org.geotools.xml.AbstractComplexEMFBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

public class DCPTypeBinding extends AbstractComplexEMFBinding {

    @Override
    public QName getTarget() {
        return WFSCapabilities.DCPType;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class getType() {
        return DCPType.class;
    }

    @SuppressWarnings("unchecked")
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

    private RequestMethodType createRequestMethodType(Ows10Factory ows10Factory, Node getOrPostNode) {
        RequestMethodType methodType = ows10Factory.createRequestMethodType();
        String href = (String) getOrPostNode.getAttributeValue("onlineResource");
        methodType.setHref(href);
        return methodType;
    }
}

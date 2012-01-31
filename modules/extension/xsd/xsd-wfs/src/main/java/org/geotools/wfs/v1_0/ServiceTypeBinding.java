package org.geotools.wfs.v1_0;

import javax.xml.namespace.QName;

import net.opengis.ows10.CodeType;
import net.opengis.ows10.KeywordsType;
import net.opengis.ows10.Ows10Factory;
import net.opengis.ows10.ServiceIdentificationType;

import org.geotools.xml.AbstractComplexEMFBinding;
import org.geotools.xml.AttributeInstance;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

public class ServiceTypeBinding extends AbstractComplexEMFBinding {

    @Override
    public QName getTarget() {
        return WFSCapabilities.Service;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class getType() {
        return ServiceIdentificationType.class;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        Ows10Factory ows10Factory = Ows10Factory.eINSTANCE;
        ServiceIdentificationType service = ows10Factory.createServiceIdentificationType();

        String name = (String) node.getChildValue("Name");
        String title = (String) node.getChildValue("Title");
        String keywords = (String) node.getChildValue("Keywords");
        if (keywords != null) {
            KeywordsType kwd = ows10Factory.createKeywordsType();
            String[] split = (keywords).split(",");
            for (int i = 0; i < split.length; i++) {
                String kw = split[i].trim();
                kwd.getKeyword().add(kw);
            }
            service.getKeywords().add(kwd);
        }

        String abstract_ = (String) node.getChildValue("Abstract");
        String accessConstraints = (String) node.getChildValue("AccessConstraints");
        String fees = (String) node.getChildValue("Fees");
        // OnlineResource

        CodeType serviceType = ows10Factory.createCodeType();
        serviceType.setValue(name);
        service.setServiceType(serviceType);
        
        service.setServiceTypeVersion("1.0.0");
        
        service.setTitle(title);
        service.setAbstract(abstract_);
        service.setAccessConstraints(accessConstraints);
        service.setFees(fees);

        // service.setServiceType(value)
        // service.setServiceTypeVersion(serviceTypeVersion);

        return service;
    }
}

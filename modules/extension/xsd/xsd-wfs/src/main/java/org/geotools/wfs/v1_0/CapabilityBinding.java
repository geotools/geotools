package org.geotools.wfs.v1_0;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import net.opengis.ows10.DCPType;
import net.opengis.ows10.DomainType;
import net.opengis.ows10.OperationType;
import net.opengis.ows10.OperationsMetadataType;
import net.opengis.ows10.Ows10Factory;

import org.geotools.xml.AbstractComplexEMFBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.InstanceComponent;
import org.geotools.xml.Node;

public class CapabilityBinding extends AbstractComplexEMFBinding {

    @Override
    public QName getTarget() {
        return WFSCapabilities.Capability;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Class getType() {
        return OperationsMetadataType.class;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        Ows10Factory ows10Factory = Ows10Factory.eINSTANCE;

        OperationsMetadataType om = ows10Factory.createOperationsMetadataType();

        Node request = node.getChild("Request");

        OperationType operation;

        operation = getCapabilities(request.getChild("GetCapabilities"), ows10Factory);
        addOperation(om, operation);

        operation = dft(request.getChild("DescribeFeatureType"), ows10Factory);
        addOperation(om, operation);

        operation = getFeature(request.getChild("GetFeature"), ows10Factory);
        addOperation(om, operation);

        operation = createOperation("Transaction", node, ows10Factory);
        addOperation(om, operation);

        operation = createOperation("LockFeature", node, ows10Factory);
        addOperation(om, operation);

        operation = createOperation("GetFeatureWithLock", node, ows10Factory);
        addOperation(om, operation);

        operation = createOperation("Transaction", node, ows10Factory);
        addOperation(om, operation);

        return om;
    }

    private OperationType getFeature(Node node, Ows10Factory ows10Factory) {
        OperationType operationType = createOperation("GetFeature", node, ows10Factory);
        addParameter(node, ows10Factory, operationType, "ResultFormat");
        return operationType;
    }

    private OperationType dft(Node node, Ows10Factory ows10Factory) {
        OperationType operationType = createOperation("DescribeFeatureType", node, ows10Factory);

        addParameter(node, ows10Factory, operationType, "SchemaDescriptionLanguage");
        return operationType;
    }

    private OperationType createOperation(String opetationName, Node node, Ows10Factory ows10Factory) {
        if (node == null) {
            return null;
        }
        OperationType operationType = ows10Factory.createOperationType();
        operationType.setName(opetationName);
        addDCPTypes(node, operationType);
        return operationType;
    }

    @SuppressWarnings("unchecked")
    private void addParameter(Node node, Ows10Factory ows10Factory, OperationType operationType,
            String parameterName) {
        Node paramParentNode = node.getChild(parameterName);
        List<String> paramValues = childNames(paramParentNode);

        DomainType domain = ows10Factory.createDomainType();
        domain.setName(parameterName);

        for (String paramValue : paramValues) {
            domain.getValue().add(paramValue);
        }
        operationType.getParameter().add(domain);
    }

    @SuppressWarnings("unchecked")
    private List<String> childNames(Node node) {
        if (null == node) {
            return Collections.emptyList();
        }
        List<Node> children = node.getChildren();
        List<String> names = new ArrayList<String>(children.size());
        for (Node child : children) {
            InstanceComponent component = child.getComponent();
            String paramValue = component.getName();
            names.add(paramValue);
        }
        return names;
    }

    private OperationType getCapabilities(Node node, Ows10Factory ows10Factory) {
        if (node == null) {
            return null;
        }
        OperationType operationType = ows10Factory.createOperationType();
        operationType.setName("GetCapabilities");
        addDCPTypes(node, operationType);
        return operationType;
    }

    @SuppressWarnings("unchecked")
    private void addDCPTypes(Node node, OperationType operationType) {
        List<Node> dcpNodes = node.getChildren(DCPType.class);
        for (Node dcpNode : dcpNodes) {
            DCPType dcp = (DCPType) dcpNode.getValue();
            operationType.getDCP().add(dcp);
        }
    }

    @SuppressWarnings("unchecked")
    private void addOperation(OperationsMetadataType om, OperationType operation) {
        if (operation != null) {
            om.getOperation().add(operation);
        }
    }

}

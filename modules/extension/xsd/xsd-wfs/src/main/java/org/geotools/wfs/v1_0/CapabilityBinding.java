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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.namespace.QName;
import net.opengis.ows10.DCPType;
import net.opengis.ows10.DomainType;
import net.opengis.ows10.OperationType;
import net.opengis.ows10.OperationsMetadataType;
import net.opengis.ows10.Ows10Factory;
import org.geotools.xsd.AbstractComplexEMFBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.InstanceComponent;
import org.geotools.xsd.Node;

public class CapabilityBinding extends AbstractComplexEMFBinding {

    @Override
    public QName getTarget() {
        return WFSCapabilities.Capability;
    }

    @Override
    public Class getType() {
        return OperationsMetadataType.class;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        Ows10Factory ows10Factory = Ows10Factory.eINSTANCE;

        OperationsMetadataType om = ows10Factory.createOperationsMetadataType();

        Node request = node.getChild("Request");

        OperationType operation =
                getCapabilities(request.getChild("GetCapabilities"), ows10Factory);
        addOperation(om, operation);

        operation = dft(request.getChild("DescribeFeatureType"), ows10Factory);
        addOperation(om, operation);

        operation = getFeature(request.getChild("GetFeature"), ows10Factory);
        addOperation(om, operation);

        Node nodeOp = request.getChild("Transaction");
        if (nodeOp != null) {
            operation = createOperation("Transaction", nodeOp, ows10Factory);
            addOperation(om, operation);
        }

        nodeOp = request.getChild("LockFeature");
        if (nodeOp != null) {
            operation = createOperation("LockFeature", nodeOp, ows10Factory);
            addOperation(om, operation);
        }

        nodeOp = request.getChild("GetFeatureWithLock");
        if (nodeOp != null) {
            operation = createOperation("GetFeatureWithLock", nodeOp, ows10Factory);
            addOperation(om, operation);
        }

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

    private OperationType createOperation(
            String opetationName, Node node, Ows10Factory ows10Factory) {
        if (node == null) {
            return null;
        }
        OperationType operationType = ows10Factory.createOperationType();
        operationType.setName(opetationName);
        addDCPTypes(node, operationType);
        return operationType;
    }

    private void addParameter(
            Node node,
            Ows10Factory ows10Factory,
            OperationType operationType,
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

    private List<String> childNames(Node node) {
        if (null == node) {
            return Collections.emptyList();
        }
        List<Node> children = node.getChildren();
        List<String> names = new ArrayList<>(children.size());
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

    private void addDCPTypes(Node node, OperationType operationType) {
        List<Node> dcpNodes = node.getChildren(DCPType.class);
        for (Node dcpNode : dcpNodes) {
            DCPType dcp = (DCPType) dcpNode.getValue();
            operationType.getDCP().add(dcp);
        }
    }

    private void addOperation(OperationsMetadataType om, OperationType operation) {
        if (operation != null) {
            om.getOperation().add(operation);
        }
    }
}

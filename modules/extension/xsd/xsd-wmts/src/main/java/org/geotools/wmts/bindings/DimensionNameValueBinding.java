package org.geotools.wmts.bindings;

import javax.xml.namespace.QName;
import net.opengis.wmts.v_1.DimensionNameValueType;
import net.opengis.wmts.v_1.wmtsv_1Factory;
import org.geotools.wmts.WMTS;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the element http://www.opengis.net/wmts/1.0:DimensionNameValue.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;element name="DimensionNameValue" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *  		&lt;complexType&gt;
 *  			&lt;simpleContent&gt;
 *  				&lt;extension base="string"&gt;
 *  					&lt;annotation&gt;
 *  						&lt;documentation&gt;Dimension value&lt;/documentation&gt;
 *  					&lt;/annotation&gt;
 *  					&lt;attribute name="name" type="string" use="required"&gt;
 *  						&lt;annotation&gt;
 *  							&lt;documentation&gt;Dimension name&lt;/documentation&gt;
 *  						&lt;/annotation&gt;
 *  					&lt;/attribute&gt;
 *  				&lt;/extension&gt;
 *  			&lt;/simpleContent&gt;
 *  		&lt;/complexType&gt;
 *  	&lt;/element&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class DimensionNameValueBinding extends AbstractComplexBinding {

    wmtsv_1Factory factory;

    public DimensionNameValueBinding(wmtsv_1Factory factory) {
        super();
        this.factory = factory;
    }

    /** @generated */
    public QName getTarget() {
        return WMTS.DimensionNameValue;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return DimensionNameValueType.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        DimensionNameValueType name = factory.createDimensionNameValueType();
        name.setName((String) node.getChildValue("name"));
        name.setValue((String) node.getChildValue("value"));
        return name;
    }
}

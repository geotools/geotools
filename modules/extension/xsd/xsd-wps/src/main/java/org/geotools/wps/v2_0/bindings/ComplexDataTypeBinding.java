package org.geotools.wps.v2_0.bindings;

import javax.xml.namespace.QName;
import net.opengis.wps20.ComplexDataType;
import net.opengis.wps20.Wps20Factory;
import org.geotools.wps.v2_0.WPS;
import org.geotools.xsd.AbstractComplexEMFBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the type http://www.opengis.net/wps/2.0:ComplexDataType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;complexType name="ComplexDataType" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *
 *  		&lt;complexContent&gt;
 *
 *  			&lt;extension base="wps:DataDescriptionType"&gt;
 *
 *  				&lt;sequence&gt;
 *
 *  					&lt;annotation&gt;
 *
 *  						&lt;documentation&gt;
 *
 *  							Placeholder for schema extensions to WPS complex data.
 *
 *  						&lt;/documentation&gt;
 *
 *  					&lt;/annotation&gt;
 *
 *  					&lt;any maxOccurs="unbounded" minOccurs="0" namespace="##other" processContents="lax"/&gt;
 *
 *  				&lt;/sequence&gt;
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
public class ComplexDataTypeBinding extends AbstractComplexEMFBinding {

    public ComplexDataTypeBinding(Wps20Factory factory) {
        super(factory);
    }

    /** @generated */
    public QName getTarget() {
        return WPS.ComplexDataType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return ComplexDataType.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        // TODO: implement and remove call to super
        return super.parse(instance, node, value);
    }
}

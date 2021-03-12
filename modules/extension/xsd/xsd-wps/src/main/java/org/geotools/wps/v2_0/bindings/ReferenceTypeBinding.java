package org.geotools.wps.v2_0.bindings;

import javax.xml.namespace.QName;
import net.opengis.wps20.ReferenceType;
import net.opengis.wps20.Wps20Factory;
import org.geotools.wps.v2_0.WPS;
import org.geotools.xsd.AbstractComplexEMFBinding;

/**
 * Binding object for the type http://www.opengis.net/wps/2.0:ReferenceType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;complexType name="ReferenceType" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *
 *  		&lt;annotation&gt;
 *
 *  			&lt;documentation&gt;
 *
 *  				Reference to an input (output) value that is a web accessible resource.
 *
 *  			&lt;/documentation&gt;
 *
 *  		&lt;/annotation&gt;
 *
 *  		&lt;choice minOccurs="0"&gt;
 *
 *  			&lt;element name="Body" type="anyType"&gt;
 *
 *  				&lt;annotation&gt;
 *
 *  					&lt;documentation&gt;
 *
 *  						The contents of this element to be used as the body of the HTTP request
 *
 *  						message to be sent to the service identified in ../Reference/@href.
 *
 *  						For example, it could be an XML encoded WFS request using HTTP/POST.
 *
 *  					&lt;/documentation&gt;
 *
 *  				&lt;/annotation&gt;
 *
 *  			&lt;/element&gt;
 *
 *  			&lt;element name="BodyReference"&gt;
 *
 *  				&lt;annotation&gt;
 *
 *  					&lt;documentation&gt;
 *
 *  						Reference to a remote document to be used as the body of the an HTTP/POST request message
 *
 *  						to the service identified in the href element in the Reference structure.
 *
 *  					&lt;/documentation&gt;
 *
 *  				&lt;/annotation&gt;
 *
 *  				&lt;complexType name="ReferenceType_BodyReference"&gt;
 *
 *  					&lt;attribute ref="xlink:href" use="required"&gt;
 *
 *  						&lt;annotation&gt;
 *
 *  							&lt;documentation&gt;
 *
 *  								HTTP URI that points to the remote resource where the request body may be retrieved.
 *
 *  							&lt;/documentation&gt;
 *
 *  						&lt;/annotation&gt;
 *
 *  					&lt;/attribute&gt;
 *
 *  				&lt;/complexType&gt;
 *
 *  			&lt;/element&gt;
 *
 *  		&lt;/choice&gt;
 *
 *  		&lt;attribute ref="xlink:href" use="required"&gt;
 *
 *  			&lt;annotation&gt;
 *
 *  				&lt;documentation&gt;
 *
 *  					HTTP URI that points to the remote resource where the data may be retrieved.
 *
 *  				&lt;/documentation&gt;
 *
 *  			&lt;/annotation&gt;
 *
 *  		&lt;/attribute&gt;
 *
 *  		&lt;attributeGroup ref="wps:dataEncodingAttributes"/&gt;
 *
 *  	&lt;/complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class ReferenceTypeBinding extends AbstractComplexEMFBinding {

    public ReferenceTypeBinding(Wps20Factory factory) {
        super(factory);
    }
    /** @generated */
    public QName getTarget() {
        return WPS.ReferenceType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return ReferenceType.class;
    }
}

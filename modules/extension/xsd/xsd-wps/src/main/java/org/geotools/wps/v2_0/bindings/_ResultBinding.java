package org.geotools.wps.v2_0.bindings;

import javax.xml.namespace.QName;
import net.opengis.wps20.Wps20Factory;
import org.geotools.wps.v2_0.WPS;
import org.geotools.xsd.AbstractComplexEMFBinding;

/**
 * Binding object for the type http://www.opengis.net/wps/2.0:_Result.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;complexType name="_Result" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *
 *  			&lt;sequence&gt;
 *
 *  				&lt;element minOccurs="0" ref="wps:JobID"&gt;
 *
 *  					&lt;annotation&gt;
 *
 *  						&lt;documentation&gt;
 *
 *  							Include if required. A JobId is usually required for
 *
 *  							a) asynchronous execution
 *
 *  							b) the Dismiss operation extension, where the client is allowed to
 *
 *  							   actively free server-side resources
 *
 *  						&lt;/documentation&gt;
 *
 *  					&lt;/annotation&gt;
 *
 *  				&lt;/element&gt;
 *
 *  				&lt;element minOccurs="0" ref="wps:ExpirationDate"&gt;
 *
 *  					&lt;annotation&gt;
 *
 *  						&lt;documentation&gt;
 *
 *  							Identifier of the Process that was executed.
 *
 *  							This Process identifier shall be as listed in the ProcessOfferings
 *
 *  							section of the WPS Capabilities document. &lt;/documentation&gt;
 *
 *  					&lt;/annotation&gt;
 *
 *  				&lt;/element&gt;
 *
 *  				&lt;element maxOccurs="unbounded" name="Output" type="wps:DataOutputType"/&gt;
 *
 *  			&lt;/sequence&gt;
 *
 *  		&lt;/complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class _ResultBinding extends AbstractComplexEMFBinding {

    public _ResultBinding(Wps20Factory factory) {
        super(factory);
    }

    /** @generated */
    public QName getTarget() {
        return WPS._Result;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return super.getType();
    }
}

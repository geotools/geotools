package org.geotools.wps.v2_0.bindings;

import javax.xml.namespace.QName;
import net.opengis.wps20.DataInputType;
import net.opengis.wps20.Wps20Factory;
import org.geotools.wps.v2_0.WPS;
import org.geotools.xsd.AbstractComplexEMFBinding;

/**
 * Binding object for the type http://www.opengis.net/wps/2.0:DataInputType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;complexType name="DataInputType" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *
 *  		&lt;annotation&gt;
 *
 *  			&lt;documentation&gt;
 *
 *  				This structure contains information elements to supply input data for process execution.
 *
 *  			&lt;/documentation&gt;
 *
 *  		&lt;/annotation&gt;
 *
 *  		&lt;sequence&gt;
 *
 *  			&lt;choice&gt;
 *
 *  				&lt;element ref="wps:Data"/&gt;
 *
 *  				&lt;element ref="wps:Reference"/&gt;
 *
 *  				&lt;element maxOccurs="unbounded" name="Input" type="wps:DataInputType"/&gt;
 *
 *  			&lt;/choice&gt;
 *
 *  		&lt;/sequence&gt;
 *
 *  		&lt;attribute name="id" type="anyURI" use="required"&gt;
 *
 *  			&lt;annotation&gt;
 *
 *  				&lt;documentation&gt;
 *
 *  					Identifier of this input.
 *
 *  				&lt;/documentation&gt;
 *
 *  			&lt;/annotation&gt;
 *
 *  		&lt;/attribute&gt;
 *
 *  	&lt;/complexType&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 */
public class DataInputTypeBinding extends AbstractComplexEMFBinding {

    public DataInputTypeBinding(Wps20Factory factory) {
        super(factory);
    }

    /** @generated */
    public QName getTarget() {
        return WPS.DataInputType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return DataInputType.class;
    }
}

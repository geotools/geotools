package org.geotools.wps.v2_0.bindings;

import javax.xml.namespace.QName;
import net.opengis.wps20.GenericProcessType;
import net.opengis.wps20.Wps20Factory;
import org.geotools.wps.v2_0.WPS;
import org.geotools.xsd.AbstractComplexEMFBinding;

/**
 * Binding object for the type http://www.opengis.net/wps/2.0:GenericProcessType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;complexType name="GenericProcessType" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *
 *  		&lt;complexContent&gt;
 *
 *  			&lt;extension base="wps:DescriptionType"&gt;
 *
 *  				&lt;annotation&gt;
 *
 *  					&lt;documentation&gt;
 *
 *  						In this use, the DescriptionType shall describe process properties.
 *
 *  					&lt;/documentation&gt;
 *
 *  				&lt;/annotation&gt;
 *
 *  				&lt;sequence&gt;
 *
 *  					&lt;element maxOccurs="unbounded" minOccurs="0" name="Input" type="wps:GenericInputType"&gt;
 *
 *  						&lt;annotation&gt;
 *
 *  							&lt;documentation&gt;
 *
 *  								A process can have zero or more inputs.
 *
 *  							&lt;/documentation&gt;
 *
 *  						&lt;/annotation&gt;
 *
 *  					&lt;/element&gt;
 *
 *  					&lt;element maxOccurs="unbounded" minOccurs="1" name="Output" type="wps:GenericOutputType"&gt;
 *
 *  						&lt;annotation&gt;
 *
 *  							&lt;documentation&gt;
 *
 *  								A process can have one or more outputs.
 *
 *  							&lt;/documentation&gt;
 *
 *  						&lt;/annotation&gt;
 *
 *  					&lt;/element&gt;
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
public class GenericProcessTypeBinding extends AbstractComplexEMFBinding {

    public GenericProcessTypeBinding(Wps20Factory factory) {
        super(factory);
    }

    /** @generated */
    public QName getTarget() {
        return WPS.GenericProcessType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return GenericProcessType.class;
    }
}

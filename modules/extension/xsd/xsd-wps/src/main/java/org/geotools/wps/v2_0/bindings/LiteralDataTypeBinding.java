package org.geotools.wps.v2_0.bindings;

import javax.xml.namespace.QName;
import net.opengis.wps20.LiteralDataType;
import net.opengis.wps20.Wps20Factory;
import org.geotools.wps.v2_0.WPS;
import org.geotools.xsd.AbstractComplexEMFBinding;

/**
 * Binding object for the type http://www.opengis.net/wps/2.0:LiteralDataType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;complexType name="LiteralDataType" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *
 *  		&lt;complexContent&gt;
 *
 *  			&lt;extension base="wps:DataDescriptionType"&gt;
 *
 *  				&lt;sequence&gt;
 *
 *  					&lt;element maxOccurs="unbounded" name="LiteralDataDomain"&gt;
 *
 *  						&lt;annotation&gt;
 *
 *  							&lt;documentation&gt;
 *
 *  								Literal Data inputs and outputs may be specified for several domains, e.g. distance units in meters,
 *
 *  								kilometers and feet. One of these must be the default domain.
 *
 *  							&lt;/documentation&gt;
 *
 *  						&lt;/annotation&gt;
 *
 *  						&lt;complexType name="LiteralDataType_LiteralDataDomain"&gt;
 *
 *  							&lt;complexContent&gt;
 *
 *  								&lt;extension base="wps:LiteralDataDomainType"&gt;
 *
 *  									&lt;attribute name="default" type="boolean" use="optional"&gt;
 *
 *  										&lt;annotation&gt;
 *
 *  											&lt;documentation&gt;
 *
 *  												Indicates that this LiteralDataDomain is the default domain.
 *
 *  											&lt;/documentation&gt;
 *
 *  										&lt;/annotation&gt;
 *
 *  									&lt;/attribute&gt;
 *
 *  								&lt;/extension&gt;
 *
 *  							&lt;/complexContent&gt;
 *
 *  						&lt;/complexType&gt;
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
 * </pre>
 *
 * @generated
 */
public class LiteralDataTypeBinding extends AbstractComplexEMFBinding {

    public LiteralDataTypeBinding(Wps20Factory factory) {
        super(factory);
    }

    /** @generated */
    public QName getTarget() {
        return WPS.LiteralDataType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return LiteralDataType.class;
    }
}

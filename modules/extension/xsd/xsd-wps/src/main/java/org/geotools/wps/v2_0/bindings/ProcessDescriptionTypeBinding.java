package org.geotools.wps.v2_0.bindings;

import javax.xml.namespace.QName;
import net.opengis.wps20.ProcessDescriptionType;
import net.opengis.wps20.Wps20Factory;
import org.geotools.wps.v2_0.WPS;
import org.geotools.xsd.AbstractComplexEMFBinding;

/**
 * Binding object for the type http://www.opengis.net/wps/2.0:ProcessDescriptionType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;complexType name="ProcessDescriptionType" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *
 *  		&lt;annotation&gt;
 *
 *  			&lt;documentation&gt;Full description of a process. &lt;/documentation&gt;
 *
 *  		&lt;/annotation&gt;
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
 *  					&lt;element maxOccurs="unbounded" minOccurs="0" name="Input" type="wps:InputDescriptionType"&gt;
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
 *  					&lt;element maxOccurs="unbounded" minOccurs="1" name="Output" type="wps:OutputDescriptionType"&gt;
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
 *  				&lt;attribute ref="xml:lang"&gt;
 *
 *  					&lt;!-- (Definition adopted from OWS Common 2.0) --&gt;
 *
 *  					&lt;annotation&gt;
 *
 *  						&lt;documentation&gt;
 *
 *  							Identifier of a language used by the data(set) contents.
 *
 *  							This language identifier shall be as specified in IETF RFC 4646. The
 *
 *  							language tags shall be either complete 5 character codes (e.g. "en-CA"),
 *
 *  							or abbreviated 2 character codes (e.g. "en"). In addition to the RFC
 *
 *  							4646 codes, the server shall support the single special value "*" which
 *
 *  							is used to indicate "any language".
 *
 *  						&lt;/documentation&gt;
 *
 *  					&lt;/annotation&gt;
 *
 *  				&lt;/attribute&gt;
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
public class ProcessDescriptionTypeBinding extends AbstractComplexEMFBinding {

    public ProcessDescriptionTypeBinding(Wps20Factory factory) {
        super(factory);
    }

    /** @generated */
    public QName getTarget() {
        return WPS.ProcessDescriptionType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return ProcessDescriptionType.class;
    }
}

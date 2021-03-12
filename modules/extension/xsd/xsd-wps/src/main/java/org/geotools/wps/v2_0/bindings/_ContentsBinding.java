package org.geotools.wps.v2_0.bindings;

import javax.xml.namespace.QName;
import net.opengis.wps20.Wps20Factory;
import org.geotools.wps.v2_0.WPS;
import org.geotools.xsd.AbstractComplexEMFBinding;

/**
 * Binding object for the type http://www.opengis.net/wps/2.0:_Contents.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;complexType name="_Contents" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *
 *  			&lt;sequence&gt;
 *
 *  				&lt;element maxOccurs="unbounded" name="ProcessSummary" type="wps:ProcessSummaryType"&gt;
 *
 *  					&lt;annotation&gt;
 *
 *  						&lt;documentation&gt;Unordered list of one or more brief descriptions of all the processes offered by this WPS server. &lt;/documentation&gt;
 *
 *  					&lt;/annotation&gt;
 *
 *  				&lt;/element&gt;
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
public class _ContentsBinding extends AbstractComplexEMFBinding {

    public _ContentsBinding(Wps20Factory factory) {
        super(factory);
    }

    /** @generated */
    public QName getTarget() {
        return WPS._Contents;
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

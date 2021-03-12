package org.geotools.wps.v2_0.bindings;

import javax.xml.namespace.QName;
import net.opengis.wps20.ProcessSummaryType;
import net.opengis.wps20.Wps20Factory;
import org.geotools.wps.v2_0.WPS;
import org.geotools.xsd.AbstractComplexEMFBinding;

/**
 * Binding object for the type http://www.opengis.net/wps/2.0:ProcessSummaryType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;complexType name="ProcessSummaryType" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *
 *  		&lt;annotation&gt;
 *
 *  			&lt;documentation&gt;
 *
 *  				The process summary consists of descriptive elements at the process level,
 *
 *  				the process profiles and the service-specific properties.
 *
 *  				The process summary is not specific about process inputs and outputs.
 *
 *  			&lt;/documentation&gt;
 *
 *  		&lt;/annotation&gt;
 *
 *  		&lt;complexContent&gt;
 *
 *  			&lt;extension base="wps:DescriptionType"&gt;
 *
 *  				&lt;attributeGroup ref="wps:processPropertiesAttributes"/&gt;
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
public class ProcessSummaryTypeBinding extends AbstractComplexEMFBinding {

    public ProcessSummaryTypeBinding(Wps20Factory factory) {
        super(factory);
    }

    /** @generated */
    public QName getTarget() {
        return WPS.ProcessSummaryType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return ProcessSummaryType.class;
    }
}

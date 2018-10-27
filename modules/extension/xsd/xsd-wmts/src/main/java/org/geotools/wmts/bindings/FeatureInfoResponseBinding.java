package org.geotools.wmts.bindings;

import javax.xml.namespace.QName;
import net.opengis.wmts.v_1.BinaryPayloadType;
import net.opengis.wmts.v_1.FeatureInfoResponseType;
import net.opengis.wmts.v_1.TextPayloadType;
import net.opengis.wmts.v_1.wmtsv_1Factory;
import org.eclipse.emf.ecore.EObject;
import org.geotools.wmts.WMTS;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the element http://www.opengis.net/wmts/1.0:FeatureInfoResponse.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;element name="FeatureInfoResponse" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *  		&lt;complexType&gt;
 *  			&lt;choice&gt;
 *  				&lt;element ref="gml:_FeatureCollection"&gt;
 *  					&lt;annotation&gt;
 *  						&lt;documentation&gt;
 *  							This allows to define any FeatureCollection that is a substitutionGroup
 *  							of gml:_GML and use it here. A Geography Markup Language GML
 *  							Simple Features Profile level 0 response format is strongly
 *  							recommended as a FeatureInfo response.
 *  						&lt;/documentation&gt;
 *  					&lt;/annotation&gt;
 *  				&lt;/element&gt;
 *  				&lt;element ref="wmts:TextPayload"&gt;
 *  					&lt;annotation&gt;
 *  						&lt;documentation&gt;
 *  							This allows to include any text format that is not a gml:_FeatureCollection
 *  							like HTML, TXT, etc
 *  						&lt;/documentation&gt;
 *  					&lt;/annotation&gt;
 *  				&lt;/element&gt;
 *  				&lt;element ref="wmts:BinaryPayload"&gt;
 *  					&lt;annotation&gt;
 *  						&lt;documentation&gt;
 *  							This allows to include any binary format. Binary formats are not
 *  							common response for a GeFeatureInfo requests but possible for
 *  							some imaginative implementations.
 *  						&lt;/documentation&gt;
 *  					&lt;/annotation&gt;
 *  				&lt;/element&gt;
 *  				&lt;element name="AnyContent" type="anyType"&gt;
 *  					&lt;annotation&gt;
 *  						&lt;documentation&gt;
 *  							This allows to include any XML content that it is not any of
 *  							the previous ones.
 *  						&lt;/documentation&gt;
 *  					&lt;/annotation&gt;
 *  				&lt;/element&gt;
 *  			&lt;/choice&gt;
 *  		&lt;/complexType&gt;
 *  	&lt;/element&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class FeatureInfoResponseBinding extends AbstractComplexBinding {

    wmtsv_1Factory factory;

    public FeatureInfoResponseBinding(wmtsv_1Factory factory) {
        super();
        this.factory = factory;
    }

    /** @generated */
    public QName getTarget() {
        return WMTS.FeatureInfoResponse;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return FeatureInfoResponseType.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        // TODO: check if this is right
        FeatureInfoResponseType fir = factory.createFeatureInfoResponseType();
        fir.setTextPayload((TextPayloadType) node.getChildValue("TextPayload"));
        fir.setBinaryPayload((BinaryPayloadType) node.getChildValue("BinaryPayload"));
        fir.setAnyContent((EObject) node.getChildValue("AnyContent"));
        return fir;
    }
}

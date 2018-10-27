package org.geotools.wmts.bindings;

import java.math.BigInteger;
import javax.xml.namespace.QName;
import net.opengis.wmts.v_1.GetFeatureInfoType;
import net.opengis.wmts.v_1.GetTileType;
import net.opengis.wmts.v_1.wmtsv_1Factory;
import org.geotools.wmts.WMTS;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the element http://www.opengis.net/wmts/1.0:GetFeatureInfo.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;element name="GetFeatureInfo" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *  		&lt;complexType&gt;
 *  			&lt;sequence&gt;
 *  				&lt;element ref="wmts:GetTile"&gt;
 *  					&lt;annotation&gt;
 *  						&lt;documentation&gt;The corresponding GetTile request parameters&lt;/documentation&gt;
 *  					&lt;/annotation&gt;
 *  				&lt;/element&gt;
 *  				&lt;element name="J" type="nonNegativeInteger"&gt;
 *  					&lt;annotation&gt;
 *  						&lt;documentation&gt;Row index of a pixel in the tile&lt;/documentation&gt;
 *  					&lt;/annotation&gt;
 *  				&lt;/element&gt;
 *  				&lt;element name="I" type="nonNegativeInteger"&gt;
 *  					&lt;annotation&gt;
 *  						&lt;documentation&gt;Column index of a pixel in the tile&lt;/documentation&gt;
 *  					&lt;/annotation&gt;
 *  				&lt;/element&gt;
 *  				&lt;element name="InfoFormat" type="ows:MimeType"&gt;
 *  					&lt;annotation&gt;
 *  						&lt;documentation&gt;Output MIME type format of the
 *  						retrieved information&lt;/documentation&gt;
 *  					&lt;/annotation&gt;
 *  				&lt;/element&gt;
 *  			&lt;/sequence&gt;
 *  			&lt;attribute fixed="WMTS" name="service" type="string" use="required"/&gt;
 *  			&lt;attribute fixed="1.0.0" name="version" type="string" use="required"/&gt;
 *  		&lt;/complexType&gt;
 *  	&lt;/element&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 */
public class GetFeatureInfoBinding extends AbstractComplexBinding {

    wmtsv_1Factory factory;

    public GetFeatureInfoBinding(wmtsv_1Factory factory) {
        super();
        this.factory = factory;
    }

    /** @generated */
    public QName getTarget() {
        return WMTS.GetFeatureInfo;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return GetFeatureInfoType.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        GetFeatureInfoType fti = factory.createGetFeatureInfoType();
        fti.setService((String) node.getChildValue("service"));
        fti.setVersion((String) node.getChildValue("version"));
        fti.setI((BigInteger) node.getChildValue("I"));
        fti.setJ((BigInteger) node.getChildValue("J"));
        fti.setInfoFormat((String) node.getChildValue("InfoFormat"));
        fti.setGetTile((GetTileType) node.getChildValue("GetTile"));

        return fti;
    }
}

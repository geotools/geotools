package org.geotools.wmts.bindings;

import java.math.BigInteger;
import javax.xml.namespace.QName;
import net.opengis.wmts.v_1.LegendURLType;
import net.opengis.wmts.v_1.wmtsv_1Factory;
import org.geotools.wmts.WMTS;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the element http://www.opengis.net/wmts/1.0:LegendURL.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;element name="LegendURL" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *  		&lt;annotation&gt;
 *  			&lt;documentation&gt;
 *          Zero or more LegendURL elements may be provided, providing an
 *          image(s) of a legend relevant to each Style of a Layer.  The Format
 *          element indicates the MIME type of the legend. minScaleDenominator
 *          and maxScaleDenominator attributes may be provided to indicate to
 *          the client which scale(s) (inclusive) the legend image is appropriate
 *          for.  (If provided, these values must exactly match the scale
 *          denominators of available TileMatrixes.)  width and height
 *          attributes may be provided to assist client applications in laying
 *          out space to display the legend.
 *        &lt;/documentation&gt;
 *  		&lt;/annotation&gt;
 *  		&lt;complexType&gt;
 *  			&lt;complexContent&gt;
 *  				&lt;extension base="ows:OnlineResourceType"&gt;
 *  					&lt;annotation&gt;
 *  						&lt;documentation&gt;The URL from which the legend image can be retrieved&lt;/documentation&gt;
 *  					&lt;/annotation&gt;
 *  					&lt;attribute name="format" type="ows:MimeType"&gt;
 *  						&lt;annotation&gt;
 *  							&lt;documentation&gt;A supported output format for the legend image&lt;/documentation&gt;
 *  						&lt;/annotation&gt;
 *  					&lt;/attribute&gt;
 *  					&lt;attribute name="minScaleDenominator" type="double"&gt;
 *  						&lt;annotation&gt;
 *  							&lt;documentation&gt;Denominator of the minimum scale (inclusive) for which this legend image is valid&lt;/documentation&gt;
 *  						&lt;/annotation&gt;
 *  					&lt;/attribute&gt;
 *  					&lt;attribute name="maxScaleDenominator" type="double"&gt;
 *  						&lt;annotation&gt;
 *  							&lt;documentation&gt;Denominator of the maximum scale (exclusive) for which this legend image is valid&lt;/documentation&gt;
 *  						&lt;/annotation&gt;
 *  					&lt;/attribute&gt;
 *  					&lt;attribute name="width" type="positiveInteger"&gt;
 *  						&lt;annotation&gt;
 *  							&lt;documentation&gt;Width (in pixels) of the legend image&lt;/documentation&gt;
 *  						&lt;/annotation&gt;
 *  					&lt;/attribute&gt;
 *  					&lt;attribute name="height" type="positiveInteger"&gt;
 *  						&lt;annotation&gt;
 *  							&lt;documentation&gt;Height (in pixels) of the legend image&lt;/documentation&gt;
 *  						&lt;/annotation&gt;
 *  					&lt;/attribute&gt;
 *  				&lt;/extension&gt;
 *  				&lt;!--/attributeGroup--&gt;
 *  			&lt;/complexContent&gt;
 *  		&lt;/complexType&gt;
 *  	&lt;/element&gt;
 *
 *   </code>
 *    </pre>
 *
 * @generated
 */
public class LegendURLBinding extends AbstractComplexBinding {
    wmtsv_1Factory factory;

    public LegendURLBinding(wmtsv_1Factory factory) {
        super();
        this.factory = factory;
    }

    /** @generated */
    public QName getTarget() {
        return WMTS.LegendURL;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return LegendURLType.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        LegendURLType legendURL = factory.createLegendURLType();

        legendURL.setFormat((String) node.getChildValue("format"));
        legendURL.setHeight((BigInteger) node.getChildValue("height"));
        legendURL.setWidth((BigInteger) node.getChildValue("width"));
        legendURL.setHref((String) node.getChildValue("Href"));
        Object childValue = node.getChildValue("maxScaleDenominator");
        if (childValue != null) {
            legendURL.setMaxScaleDenominator(((Double) childValue).doubleValue());
        }
        childValue = node.getChildValue("minScaleDenominator");
        if (childValue != null) {
            legendURL.setMinScaleDenominator(((Double) childValue).doubleValue());
        }

        return legendURL;
    }
}

package org.geotools.wmts.bindings;

import javax.xml.namespace.QName;
import net.opengis.ows11.CodeType;
import net.opengis.wmts.v_1.DimensionType;
import net.opengis.wmts.v_1.wmtsv_1Factory;
import org.geotools.wmts.WMTS;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Binding object for the element http://www.opengis.net/wmts/1.0:Dimension.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;element name="Dimension" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *  		&lt;annotation&gt;
 *  			&lt;documentation&gt;
 *  				Metadata about a particular dimension that the tiles of
 *  				a layer are available.
 *  			&lt;/documentation&gt;
 *  		&lt;/annotation&gt;
 *  		&lt;complexType&gt;
 *  			&lt;complexContent&gt;
 *  				&lt;extension base="ows:DescriptionType"&gt;
 *  					&lt;sequence&gt;
 *  						&lt;element ref="ows:Identifier"&gt;
 *  							&lt;annotation&gt;
 *  								&lt;documentation&gt;A name of dimensional axis&lt;/documentation&gt;
 *  							&lt;/annotation&gt;
 *  						&lt;/element&gt;
 *  						&lt;element minOccurs="0" ref="ows:UOM"&gt;
 *  							&lt;annotation&gt;
 *  								&lt;documentation&gt;Units of measure of dimensional axis.&lt;/documentation&gt;
 *  							&lt;/annotation&gt;
 *  						&lt;/element&gt;
 *  						&lt;element minOccurs="0" name="UnitSymbol" type="string"&gt;
 *  							&lt;annotation&gt;
 *  								&lt;documentation&gt;Symbol of the units.&lt;/documentation&gt;
 *  							&lt;/annotation&gt;
 *  						&lt;/element&gt;
 *  						&lt;element minOccurs="0" name="Default" type="string"&gt;
 *  							&lt;annotation&gt;
 *  								&lt;documentation&gt;
 *  									Default value that will be used if a tile request does
 *  									not specify a value or uses the keyword 'default'.
 *  								&lt;/documentation&gt;
 *  							&lt;/annotation&gt;
 *  						&lt;/element&gt;
 *  						&lt;element minOccurs="0" name="Current" type="boolean"&gt;
 *  							&lt;annotation&gt;
 *  								&lt;documentation&gt;
 *  									A value of 1 (or 'true') indicates (a) that temporal data are
 *  									normally kept current and (b) that the request value of this
 *  									dimension accepts the keyword 'current'.
 *  								&lt;/documentation&gt;
 *  							&lt;/annotation&gt;
 *  						&lt;/element&gt;
 *  						&lt;element maxOccurs="unbounded" name="Value" type="string"&gt;
 *  							&lt;annotation&gt;
 *  								&lt;documentation&gt;Available value for this dimension.&lt;/documentation&gt;
 *  							&lt;/annotation&gt;
 *  						&lt;/element&gt;
 *  					&lt;/sequence&gt;
 *  				&lt;/extension&gt;
 *  			&lt;/complexContent&gt;
 *  		&lt;/complexType&gt;
 *  	&lt;/element&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class DimensionBinding extends AbstractComplexBinding {

    wmtsv_1Factory factory;

    public DimensionBinding(wmtsv_1Factory factory) {
        super();
        this.factory = factory;
    }

    /** @generated */
    public QName getTarget() {
        return WMTS.Dimension;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return DimensionType.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        DimensionType dimension = factory.createDimensionType();

        dimension.setCurrent((boolean) node.getChildValue("Current", false));
        dimension.setDefault((String) node.getChildValue("Default"));
        dimension.setIdentifier((CodeType) node.getChildValue("Identifier"));
        dimension.setUnitSymbol((String) node.getChildValue("UnitSymbol", null));
        // dimension.setUOM((DomainMetadataType) node.getChildValue("UOM"));
        dimension.getValue().addAll(node.getChildValues("Value"));
        return dimension;
    }
}

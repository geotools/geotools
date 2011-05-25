package org.geotools.wcs.bindings;


import org.geotools.wcs.WCS;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/wcs:RangeSetType.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;complexType name="RangeSetType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Defines the properties (categories, measures, or values) assigned to each location in the domain. Any such property may be a scalar (numeric or text) value, such as population density, or a compound (vector or tensor) value, such as incomes by race, or radiances by wavelength. The semantic of the range set is typically an observable and is referenced by a URI. A rangeSet also has a reference system that is reffered by the URI in the refSys attribute. The refSys is either qualitative (classification) or quantitative (uom). The three attributes can be included either here and in each axisDescription. If included in both places, the values in the axisDescription over-ride those included in the RangeSet. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="wcs:AbstractDescriptionType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="wcs:axisDescription"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Defines a range provided by a coverage. Multiple occurences are used for compound observations, to descibe an additional parameter (that is, an independent variable besides space and time), plus the valid values of this parameter (which GetCoverage requests can use to select subsets of a coverage offering). &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *                  &lt;element minOccurs="0" name="nullValues" type="wcs:valueEnumType"&gt;
 *                      &lt;annotation&gt;
 *                          &lt;documentation&gt;Values used when valid values are not available. (The coverage encoding may specify a fixed value for null (e.g. “–99999” or “N/A”), but often the choice is up to the provider and must be communicated to the client outside of the coverage itself.) &lt;/documentation&gt;
 *                      &lt;/annotation&gt;
 *                  &lt;/element&gt;
 *              &lt;/sequence&gt;
 *              &lt;attribute ref="wcs:semantic" use="optional"/&gt;
 *              &lt;attribute name="refSys" type="anyURI" use="optional"&gt;
 *                  &lt;annotation&gt;
 *                      &lt;documentation&gt;Pointer to the reference system in which values are expressed. This attribute shall be included either here or in each AxisDescriptionType. &lt;/documentation&gt;
 *                  &lt;/annotation&gt;
 *              &lt;/attribute&gt;
 *              &lt;attribute name="refSysLabel" type="string" use="optional"&gt;
 *                  &lt;annotation&gt;
 *                      &lt;documentation&gt;Short human-readable label denoting the reference system, for human interface display. This attribute shall be included either here or in each AxisDescriptionType. &lt;/documentation&gt;
 *                  &lt;/annotation&gt;
 *              &lt;/attribute&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-wcs/src/main/java/org/geotools/wcs/bindings/RangeSetTypeBinding.java $
 */
public class RangeSetTypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return WCS.RangeSetType;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Class getType() {
		return null;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Object parse(ElementInstance instance, Node node, Object value) 
		throws Exception {
		
		//TODO: implement and remove call to super
		return super.parse(instance,node,value);
	}

}

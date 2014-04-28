package org.geotools.geopkg.wps.xml;


import java.math.BigDecimal;

import org.geotools.xml.*;

import com.vividsolutions.jts.geom.Envelope;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/gpkg:bboxtype.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;xs:complexType name="bboxtype" xmlns:xs="http://www.w3.org/2001/XMLSchema"&gt;
 *      &lt;xs:sequence&gt;
 *        &lt;xs:element name="minx" type="xs:decimal"/&gt;
 *        &lt;xs:element name="miny" type="xs:decimal"/&gt;
 *        &lt;xs:element name="maxx" type="xs:decimal"/&gt;
 *        &lt;xs:element name="maxy" type="xs:decimal"/&gt;
 *      &lt;/xs:sequence&gt;
 *    &lt;/xs:complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class BboxtypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return GPKG.bboxtype;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Class getType() {
		return Envelope.class;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Object parse(ElementInstance instance, Node node, Object value) 
		throws Exception {

            double minx = ((BigDecimal) node.getChildValue("minx")).doubleValue();
            double miny = ((BigDecimal) node.getChildValue("miny")).doubleValue();
            double maxx = ((BigDecimal) node.getChildValue("maxx")).doubleValue();
            double maxy = ((BigDecimal) node.getChildValue("maxy")).doubleValue();            

            return new Envelope(minx, maxx, miny, maxy);
	}

}
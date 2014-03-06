package org.geotools.geopkg.wps.parser;


import java.util.List;

import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/gpkg:gridsettype.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;xs:complexType name="gridsettype" xmlns:xs="http://www.w3.org/2001/XMLSchema"&gt;
 *      &lt;xs:choice&gt;
 *        &lt;xs:element name="name" type="xs:string"/&gt;
 *        &lt;xs:element name="grids"&gt;
 *          &lt;xs:complexType name="gridsettype_grids"&gt;
 *            &lt;xs:sequence&gt;
 *              &lt;xs:element maxOccurs="unbounded" name="grid" type="gridtype"/&gt;
 *            &lt;/xs:sequence&gt;
 *          &lt;/xs:complexType&gt;
 *        &lt;/xs:element&gt;
 *      &lt;/xs:choice&gt;
 *    &lt;/xs:complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class GridsettypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return GPKG.gridsettype;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Class getType() {
		return Object.class;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Object parse(ElementInstance instance, Node node, Object value) 
		throws Exception {
		
	    String name = (String) node.getChildValue("name");
	    if (name != null){
	        return name;
	    } else {
	        List list = (List) node.getChildValue("grids");
	        if (list != null && !list.isEmpty()){
	            return list;
	        }
	    }
	    
	    return null;
	}

}
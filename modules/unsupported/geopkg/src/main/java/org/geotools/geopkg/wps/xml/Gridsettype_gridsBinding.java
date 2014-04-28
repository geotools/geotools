package org.geotools.geopkg.wps.xml;


import java.util.ArrayList;
import java.util.List;

import org.geotools.geopkg.TileMatrix;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/gpkg:gridsettype_grids.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;xs:complexType name="gridsettype_grids" xmlns:xs="http://www.w3.org/2001/XMLSchema"&gt;
 *            &lt;xs:sequence&gt;
 *              &lt;xs:element maxOccurs="unbounded" name="grid" type="gridtype"/&gt;
 *            &lt;/xs:sequence&gt;
 *          &lt;/xs:complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class Gridsettype_gridsBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return GPKG.gridsettype_grids;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Class getType() {
		return List.class;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Object parse(ElementInstance instance, Node node, Object value) 
		throws Exception {	    
	    List<TileMatrix> matrices = new ArrayList<TileMatrix>();
            
            for (Object child : node.getChildValues("grid")){
                matrices.add((TileMatrix) child);
            }
            
            return matrices;  	    
	}

}
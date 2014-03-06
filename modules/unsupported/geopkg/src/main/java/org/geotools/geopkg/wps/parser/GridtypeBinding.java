package org.geotools.geopkg.wps.parser;


import java.math.BigDecimal;
import java.math.BigInteger;

import org.geotools.geopkg.TileMatrix;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/gpkg:gridtype.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;xs:complexType name="gridtype" xmlns:xs="http://www.w3.org/2001/XMLSchema"&gt;
 *      &lt;xs:sequence&gt;
 *        &lt;xs:element name="zoomlevel" type="xs:nonNegativeInteger"/&gt;
 *        &lt;xs:element name="tilewidth" type="xs:positiveInteger"/&gt;
 *        &lt;xs:element name="tileheight" type="xs:positiveInteger"/&gt;
 *        &lt;xs:element name="matrixwidth" type="xs:positiveInteger"/&gt;
 *        &lt;xs:element name="matrixheight" type="xs:positiveInteger"/&gt;
 *        &lt;xs:element name="pixelxsize" type="xs:decimal"/&gt;
 *        &lt;xs:element name="pixelysize" type="xs:decimal"/&gt;
 *      &lt;/xs:sequence&gt;
 *    &lt;/xs:complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class GridtypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return GPKG.gridtype;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Class getType() {
		return TileMatrix.class;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Object parse(ElementInstance instance, Node node, Object value) 
		throws Exception {	
	    
            int zoomlevel = (Integer) node.getChildValue("zoomlevel");
            int tilewidth = ((BigInteger) node.getChildValue("tilewidth")).intValue();
            int tilheight = ((BigInteger) node.getChildValue("tileheight")).intValue();
            int matrixwidth = ((BigInteger) node.getChildValue("matrixwidth")).intValue();
            int matrixheight = ((BigInteger) node.getChildValue("matrixheight")).intValue();  
            double xpixelsize = ((BigDecimal) node.getChildValue("pixelxsize")).doubleValue();
            double ypixelsize = ((BigDecimal) node.getChildValue("pixelysize")).doubleValue();
            
            return new TileMatrix(zoomlevel, matrixwidth, matrixheight, tilewidth, tilheight, xpixelsize, ypixelsize);
	}

}
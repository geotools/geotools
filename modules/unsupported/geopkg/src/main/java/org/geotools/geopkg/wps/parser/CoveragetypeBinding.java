package org.geotools.geopkg.wps.parser;


import org.geotools.geopkg.wps.GeoPackageProcessRequest;
import org.geotools.xml.*;


import javax.xml.namespace.QName;

/**
 * Binding object for the type http://www.opengis.net/gpkg:coveragetype.
 *
 * <p>
 *	<pre>
 *	 <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;xs:complexType name="coveragetype" xmlns:xs="http://www.w3.org/2001/XMLSchema"&gt;
 *      &lt;xs:sequence&gt;
 *        &lt;xs:element minOccurs="0" name="minZoom" type="xs:nonNegativeInteger"/&gt;
 *        &lt;xs:element minOccurs="0" name="maxZoom" type="xs:nonNegativeInteger"/&gt;
 *        &lt;xs:element minOccurs="0" name="minColumn" type="xs:nonNegativeInteger"/&gt;
 *        &lt;xs:element minOccurs="0" name="maxColumn" type="xs:nonNegativeInteger"/&gt;
 *        &lt;xs:element minOccurs="0" name="minRow" type="xs:nonNegativeInteger"/&gt;
 *        &lt;xs:element minOccurs="0" name="maxRow" type="xs:nonNegativeInteger"/&gt;
 *      &lt;/xs:sequence&gt;
 *    &lt;/xs:complexType&gt; 
 *		
 *	  </code>
 *	 </pre>
 * </p>
 *
 * @generated
 */
public class CoveragetypeBinding extends AbstractComplexBinding {

	/**
	 * @generated
	 */
	public QName getTarget() {
		return GPKG.coveragetype;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Class getType() {
		return GeoPackageProcessRequest.TilesLayer.TilesCoverage.class;
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *	
	 * @generated modifiable
	 */	
	public Object parse(ElementInstance instance, Node node, Object value) 
		throws Exception {
	    
	    GeoPackageProcessRequest.TilesLayer.TilesCoverage coverage = new GeoPackageProcessRequest.TilesLayer.TilesCoverage();
            
            coverage.setMinZoom((Integer) node.getChildValue("minZoom"));
            coverage.setMaxZoom((Integer) node.getChildValue("maxZoom"));            
            coverage.setMinRow((Integer) node.getChildValue("minRow"));
            coverage.setMaxRow((Integer) node.getChildValue("maxRow"));
            coverage.setMinColumn((Integer) node.getChildValue("minColumn"));
            coverage.setMaxColumn((Integer) node.getChildValue("maxColumn"));
            
            return coverage;      
	
	}

}
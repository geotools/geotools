/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.gml;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Geometry Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * All geometry elements are derived directly or indirectly from this abstract supertype. A geometry element may have an identifying attribute ("id"), a name (attribute "name") and a description (attribute "description"). It may be associated with a spatial reference system (attribute "srsName"). The following rules shall be adhered: - Every geometry type shall derive from this abstract type. - Every geometry element (i.e. an element of a geometry type) shall be directly or indirectly in the substitution group of _Geometry.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.gml.AbstractGeometryType#getSrsName <em>Srs Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.gml.GmlPackage#getAbstractGeometryType()
 * @model abstract="true"
 *        extendedMetaData="name='AbstractGeometryType' kind='empty'"
 * @generated
 */
public interface AbstractGeometryType extends AbstractGeometryBaseType {
    /**
	 * Returns the value of the '<em><b>Srs Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * No gid attribute added.
	 * In general srsName points to a CRS instance of CoordinateReferenceSystemType (see coordinateReferenceSystems.xsd). For well known references it is not required that the CRS description exists at the location the URI points to (Note: These "WKCRS"-ids still have to be specified).  If no srsName attribute is given, the CRS must be specified as part of the larger context this geometry element is part of, e.g. a geometric aggregate.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Srs Name</em>' attribute.
	 * @see #setSrsName(String)
	 * @see net.opengis.gml.GmlPackage#getAbstractGeometryType_SrsName()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='srsName'"
	 * @generated
	 */
    String getSrsName();

    /**
	 * Sets the value of the '{@link net.opengis.gml.AbstractGeometryType#getSrsName <em>Srs Name</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Srs Name</em>' attribute.
	 * @see #getSrsName()
	 * @generated
	 */
    void setSrsName(String value);

} // AbstractGeometryType

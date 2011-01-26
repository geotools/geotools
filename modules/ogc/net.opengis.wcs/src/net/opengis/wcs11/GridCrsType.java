/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Grid Crs Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Definition of a coordinate reference system (CRS) for a quadrilateral grid that is defined in another CRS, where this grid is defined by its coordinate Conversion from the other CRS. This GridCRS is not a ProjectedCRS. However, like a ProjectedCRS, the coordinate system used is Cartesian. This GridCRS can use any type of baseCRS, including GeographicCRS, ProjectedCRS, ImageCRS, or a different GridCRS. 
 * This GridCRS is a simplification and specialization of a gml:DerivedCRS. All elements and attributes not required to define this GridCRS are optional. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs11.GridCrsType#getSrsName <em>Srs Name</em>}</li>
 *   <li>{@link net.opengis.wcs11.GridCrsType#getGridBaseCRS <em>Grid Base CRS</em>}</li>
 *   <li>{@link net.opengis.wcs11.GridCrsType#getGridType <em>Grid Type</em>}</li>
 *   <li>{@link net.opengis.wcs11.GridCrsType#getGridOrigin <em>Grid Origin</em>}</li>
 *   <li>{@link net.opengis.wcs11.GridCrsType#getGridOffsets <em>Grid Offsets</em>}</li>
 *   <li>{@link net.opengis.wcs11.GridCrsType#getGridCS <em>Grid CS</em>}</li>
 *   <li>{@link net.opengis.wcs11.GridCrsType#getId <em>Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs11.Wcs111Package#getGridCrsType()
 * @model extendedMetaData="name='GridCrsType' kind='elementOnly'"
 * @generated
 */
public interface GridCrsType extends EObject {
    /**
     * Returns the value of the '<em><b>Srs Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Srs Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Srs Name</em>' attribute.
     * @see #setSrsName(Object)
     * @see net.opengis.wcs11.Wcs111Package#getGridCrsType_SrsName()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType"
     *        extendedMetaData="kind='element' name='srsName' namespace='http://www.opengis.net/gml'"
     * @generated
     */
    Object getSrsName();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.GridCrsType#getSrsName <em>Srs Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Srs Name</em>' attribute.
     * @see #getSrsName()
     * @generated
     */
    void setSrsName(Object value);

    /**
     * Returns the value of the '<em><b>Grid Base CRS</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Association to the coordinate reference system (CRS) in which this Grid CRS is specified. A GridCRS can use any type of baseCRS, including GeographicCRS, ProjectedCRS, ImageCRS, or a different GridCRS. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Grid Base CRS</em>' attribute.
     * @see #setGridBaseCRS(String)
     * @see net.opengis.wcs11.Wcs111Package#getGridCrsType_GridBaseCRS()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI" required="true"
     *        extendedMetaData="kind='element' name='GridBaseCRS' namespace='##targetNamespace'"
     * @generated
     */
    String getGridBaseCRS();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.GridCrsType#getGridBaseCRS <em>Grid Base CRS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Grid Base CRS</em>' attribute.
     * @see #getGridBaseCRS()
     * @generated
     */
    void setGridBaseCRS(String value);

    /**
     * Returns the value of the '<em><b>Grid Type</b></em>' attribute.
     * The default value is <code>"urn:ogc:def:method:WCS:1.1:2dSimpleGrid"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * When this GridType reference is omitted, the OperationMethod shall be the most commonly used method in a GridCRS, which is referenced by the default URN "urn:ogc:def:method:WCS:1.1:2dSimpleGrid". 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Grid Type</em>' attribute.
     * @see #isSetGridType()
     * @see #unsetGridType()
     * @see #setGridType(String)
     * @see net.opengis.wcs11.Wcs111Package#getGridCrsType_GridType()
     * @model default="urn:ogc:def:method:WCS:1.1:2dSimpleGrid" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='element' name='GridType' namespace='##targetNamespace'"
     * @generated
     */
    String getGridType();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.GridCrsType#getGridType <em>Grid Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Grid Type</em>' attribute.
     * @see #isSetGridType()
     * @see #unsetGridType()
     * @see #getGridType()
     * @generated
     */
    void setGridType(String value);

    /**
     * Unsets the value of the '{@link net.opengis.wcs11.GridCrsType#getGridType <em>Grid Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetGridType()
     * @see #getGridType()
     * @see #setGridType(String)
     * @generated
     */
    void unsetGridType();

    /**
     * Returns whether the value of the '{@link net.opengis.wcs11.GridCrsType#getGridType <em>Grid Type</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Grid Type</em>' attribute is set.
     * @see #unsetGridType()
     * @see #getGridType()
     * @see #setGridType(String)
     * @generated
     */
    boolean isSetGridType();

    /**
     * Returns the value of the '<em><b>Grid Origin</b></em>' attribute.
     * The default value is <code>"0 0"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * When this GridOrigin position is omitted, the origin defaults be the most commonly used origin in a GridCRS used in the output part of a GetCapabilities operation request, namely "0 0". 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Grid Origin</em>' attribute.
     * @see #isSetGridOrigin()
     * @see #unsetGridOrigin()
     * @see #setGridOrigin(Object)
     * @see net.opengis.wcs11.Wcs111Package#getGridCrsType_GridOrigin()
     * @model default="0 0" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType"
     *        extendedMetaData="kind='element' name='GridOrigin' namespace='##targetNamespace'"
     * @generated
     */
    Object getGridOrigin();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.GridCrsType#getGridOrigin <em>Grid Origin</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Grid Origin</em>' attribute.
     * @see #isSetGridOrigin()
     * @see #unsetGridOrigin()
     * @see #getGridOrigin()
     * @generated
     */
    void setGridOrigin(Object value);

    /**
     * Unsets the value of the '{@link net.opengis.wcs11.GridCrsType#getGridOrigin <em>Grid Origin</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetGridOrigin()
     * @see #getGridOrigin()
     * @see #setGridOrigin(Object)
     * @generated
     */
    void unsetGridOrigin();

    /**
     * Returns whether the value of the '{@link net.opengis.wcs11.GridCrsType#getGridOrigin <em>Grid Origin</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Grid Origin</em>' attribute is set.
     * @see #unsetGridOrigin()
     * @see #getGridOrigin()
     * @see #setGridOrigin(Object)
     * @generated
     */
    boolean isSetGridOrigin();

    /**
     * Returns the value of the '<em><b>Grid Offsets</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Two or more grid position offsets from the grid origin in the GridBaseCRS of this GridCRS. Example: For the grid2dIn2dCRS OperationMethod, this Offsets element shall contain four values, the first two values shall specify the grid offset for the first grid axis in the 2D base CRS, and the second pair of values shall specify the grid offset for the second grid axis. In this case, the middle two values are zero for un-rotated and un-skewed grids. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Grid Offsets</em>' attribute.
     * @see #setGridOffsets(Object)
     * @see net.opengis.wcs11.Wcs111Package#getGridCrsType_GridOffsets()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType" required="true"
     *        extendedMetaData="kind='element' name='GridOffsets' namespace='##targetNamespace'"
     * @generated
     */
    Object getGridOffsets();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.GridCrsType#getGridOffsets <em>Grid Offsets</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Grid Offsets</em>' attribute.
     * @see #getGridOffsets()
     * @generated
     */
    void setGridOffsets(Object value);

    /**
     * Returns the value of the '<em><b>Grid CS</b></em>' attribute.
     * The default value is <code>"urn:ogc:def:cs:OGC:0.0:Grid2dSquareCS"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * When this GridCS reference is omitted, the GridCS defaults to the most commonly used grid coordinate system, which is referenced by the URN "urn:ogc:def:cs:OGC:0.0:Grid2dSquareCS". 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Grid CS</em>' attribute.
     * @see #isSetGridCS()
     * @see #unsetGridCS()
     * @see #setGridCS(String)
     * @see net.opengis.wcs11.Wcs111Package#getGridCrsType_GridCS()
     * @model default="urn:ogc:def:cs:OGC:0.0:Grid2dSquareCS" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='element' name='GridCS' namespace='##targetNamespace'"
     * @generated
     */
    String getGridCS();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.GridCrsType#getGridCS <em>Grid CS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Grid CS</em>' attribute.
     * @see #isSetGridCS()
     * @see #unsetGridCS()
     * @see #getGridCS()
     * @generated
     */
    void setGridCS(String value);

    /**
     * Unsets the value of the '{@link net.opengis.wcs11.GridCrsType#getGridCS <em>Grid CS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetGridCS()
     * @see #getGridCS()
     * @see #setGridCS(String)
     * @generated
     */
    void unsetGridCS();

    /**
     * Returns whether the value of the '{@link net.opengis.wcs11.GridCrsType#getGridCS <em>Grid CS</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Grid CS</em>' attribute is set.
     * @see #unsetGridCS()
     * @see #getGridCS()
     * @see #setGridCS(String)
     * @generated
     */
    boolean isSetGridCS();

    /**
     * Returns the value of the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Id</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Id</em>' attribute.
     * @see #setId(Object)
     * @see net.opengis.wcs11.Wcs111Package#getGridCrsType_Id()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType"
     *        extendedMetaData="kind='attribute' name='id' namespace='http://www.opengis.net/gml'"
     * @generated
     */
    Object getId();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.GridCrsType#getId <em>Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Id</em>' attribute.
     * @see #getId()
     * @generated
     */
    void setId(Object value);

} // GridCrsType

/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Coordinate System Axis Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Definition of a coordinate system axis. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.CoordinateSystemAxisType#getAxisID <em>Axis ID</em>}</li>
 *   <li>{@link net.opengis.gml311.CoordinateSystemAxisType#getRemarks <em>Remarks</em>}</li>
 *   <li>{@link net.opengis.gml311.CoordinateSystemAxisType#getAxisAbbrev <em>Axis Abbrev</em>}</li>
 *   <li>{@link net.opengis.gml311.CoordinateSystemAxisType#getAxisDirection <em>Axis Direction</em>}</li>
 *   <li>{@link net.opengis.gml311.CoordinateSystemAxisType#getUom <em>Uom</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getCoordinateSystemAxisType()
 * @model extendedMetaData="name='CoordinateSystemAxisType' kind='elementOnly'"
 * @generated
 */
public interface CoordinateSystemAxisType extends CoordinateSystemAxisBaseType {
    /**
     * Returns the value of the '<em><b>Axis ID</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.IdentifierType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Set of alternative identifications of this coordinate system axis. The first axisID, if any, is normally the primary identification code, and any others are aliases. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Axis ID</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getCoordinateSystemAxisType_AxisID()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='axisID' namespace='##targetNamespace'"
     * @generated
     */
    EList<IdentifierType> getAxisID();

    /**
     * Returns the value of the '<em><b>Remarks</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Comments on or information about this coordinate system axis, including data source information. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Remarks</em>' containment reference.
     * @see #setRemarks(StringOrRefType)
     * @see net.opengis.gml311.Gml311Package#getCoordinateSystemAxisType_Remarks()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='remarks' namespace='##targetNamespace'"
     * @generated
     */
    StringOrRefType getRemarks();

    /**
     * Sets the value of the '{@link net.opengis.gml311.CoordinateSystemAxisType#getRemarks <em>Remarks</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Remarks</em>' containment reference.
     * @see #getRemarks()
     * @generated
     */
    void setRemarks(StringOrRefType value);

    /**
     * Returns the value of the '<em><b>Axis Abbrev</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The abbreviation used for this coordinate system axis. This abbreviation can be used to identify the ordinates in a coordinate tuple. Examples are X and Y. The codeSpace attribute can reference a source of more information on a set of standardized abbreviations, or on this abbreviation. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Axis Abbrev</em>' containment reference.
     * @see #setAxisAbbrev(CodeType)
     * @see net.opengis.gml311.Gml311Package#getCoordinateSystemAxisType_AxisAbbrev()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='axisAbbrev' namespace='##targetNamespace'"
     * @generated
     */
    CodeType getAxisAbbrev();

    /**
     * Sets the value of the '{@link net.opengis.gml311.CoordinateSystemAxisType#getAxisAbbrev <em>Axis Abbrev</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Axis Abbrev</em>' containment reference.
     * @see #getAxisAbbrev()
     * @generated
     */
    void setAxisAbbrev(CodeType value);

    /**
     * Returns the value of the '<em><b>Axis Direction</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Direction of this coordinate system axis (or in the case of Cartesian projected coordinates, the direction of this coordinate system axis at the origin). Examples: north or south, east or west, up or down. Within any set of coordinate system axes, only one of each pair of terms can be used. For earth-fixed CRSs, this direction is often approximate and intended to provide a human interpretable meaning to the axis. When a geodetic datum is used, the precise directions of the axes may therefore vary slightly from this approximate direction. Note that an EngineeringCRS can include specific descriptions of the directions of its coordinate system axes. For example, the path of a linear CRS axis can be referenced in another document, such as referencing a GML feature that references or includes a curve geometry. The codeSpace attribute can reference a source of more information on a set of standardized directions, or on this direction. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Axis Direction</em>' containment reference.
     * @see #setAxisDirection(CodeType)
     * @see net.opengis.gml311.Gml311Package#getCoordinateSystemAxisType_AxisDirection()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='axisDirection' namespace='##targetNamespace'"
     * @generated
     */
    CodeType getAxisDirection();

    /**
     * Sets the value of the '{@link net.opengis.gml311.CoordinateSystemAxisType#getAxisDirection <em>Axis Direction</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Axis Direction</em>' containment reference.
     * @see #getAxisDirection()
     * @generated
     */
    void setAxisDirection(CodeType value);

    /**
     * Returns the value of the '<em><b>Uom</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifier of the unit of measure used for this coordinate system axis. The value of this coordinate in a coordinate tuple shall be recorded using this unit of measure, whenever those coordinates use a coordinate reference system that uses a coordinate system that uses this axis.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uom</em>' attribute.
     * @see #setUom(String)
     * @see net.opengis.gml311.Gml311Package#getCoordinateSystemAxisType_Uom()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI" required="true"
     *        extendedMetaData="kind='attribute' name='uom' namespace='##targetNamespace'"
     * @generated
     */
    String getUom();

    /**
     * Sets the value of the '{@link net.opengis.gml311.CoordinateSystemAxisType#getUom <em>Uom</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uom</em>' attribute.
     * @see #getUom()
     * @generated
     */
    void setUom(String value);

} // CoordinateSystemAxisType

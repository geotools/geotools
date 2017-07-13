/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Coordinate System Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A coordinate system (CS) is the set of coordinate system axes that spans a given coordinate space. A CS is derived from a set of (mathematical) rules for specifying how coordinates in a given space are to be assigned to points. The coordinate values in a coordinate tuple shall be recorded in the order in which the coordinate system axes associations are recorded, whenever those coordinates use a coordinate reference system that uses this coordinate system. This abstract complexType shall not be used, extended, or restricted, in an Application Schema, to define a concrete subtype with a meaning equivalent to a concrete subtype specified in this document. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.AbstractCoordinateSystemType#getCsID <em>Cs ID</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractCoordinateSystemType#getRemarks <em>Remarks</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractCoordinateSystemType#getUsesAxis <em>Uses Axis</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getAbstractCoordinateSystemType()
 * @model abstract="true"
 *        extendedMetaData="name='AbstractCoordinateSystemType' kind='elementOnly'"
 * @generated
 */
public interface AbstractCoordinateSystemType extends AbstractCoordinateSystemBaseType {
    /**
     * Returns the value of the '<em><b>Cs ID</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.IdentifierType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Set of alternative identifications of this coordinate system. The first csID, if any, is normally the primary identification code, and any others are aliases. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Cs ID</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getAbstractCoordinateSystemType_CsID()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='csID' namespace='##targetNamespace'"
     * @generated
     */
    EList<IdentifierType> getCsID();

    /**
     * Returns the value of the '<em><b>Remarks</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Comments on or information about this coordinate system, including data source information. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Remarks</em>' containment reference.
     * @see #setRemarks(StringOrRefType)
     * @see net.opengis.gml311.Gml311Package#getAbstractCoordinateSystemType_Remarks()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='remarks' namespace='##targetNamespace'"
     * @generated
     */
    StringOrRefType getRemarks();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractCoordinateSystemType#getRemarks <em>Remarks</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Remarks</em>' containment reference.
     * @see #getRemarks()
     * @generated
     */
    void setRemarks(StringOrRefType value);

    /**
     * Returns the value of the '<em><b>Uses Axis</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.CoordinateSystemAxisRefType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Ordered sequence of associations to the coordinate system axes included in this coordinate system. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uses Axis</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getAbstractCoordinateSystemType_UsesAxis()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='usesAxis' namespace='##targetNamespace'"
     * @generated
     */
    EList<CoordinateSystemAxisRefType> getUsesAxis();

} // AbstractCoordinateSystemType

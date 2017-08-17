/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Extent Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Information about the spatial, vertical, and/or temporal extent of a reference system object. Constraints: At least one of the elements "description", "boundingBox", "boundingPolygon", "verticalExtent", and temporalExtent" must be included, but more that one can be included when appropriate. Furthermore, more than one "boundingBox", "boundingPolygon", "verticalExtent", and/or temporalExtent" element can be included, with more than one meaning the union of the individual domains.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.ExtentType#getDescription <em>Description</em>}</li>
 *   <li>{@link net.opengis.gml311.ExtentType#getBoundingBox <em>Bounding Box</em>}</li>
 *   <li>{@link net.opengis.gml311.ExtentType#getBoundingPolygon <em>Bounding Polygon</em>}</li>
 *   <li>{@link net.opengis.gml311.ExtentType#getVerticalExtent <em>Vertical Extent</em>}</li>
 *   <li>{@link net.opengis.gml311.ExtentType#getTemporalExtent <em>Temporal Extent</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getExtentType()
 * @model extendedMetaData="name='ExtentType' kind='elementOnly'"
 * @generated
 */
public interface ExtentType extends EObject {
    /**
     * Returns the value of the '<em><b>Description</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Description of spatial and/or temporal extent of this object.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Description</em>' containment reference.
     * @see #setDescription(StringOrRefType)
     * @see net.opengis.gml311.Gml311Package#getExtentType_Description()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='description' namespace='##targetNamespace'"
     * @generated
     */
    StringOrRefType getDescription();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ExtentType#getDescription <em>Description</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Description</em>' containment reference.
     * @see #getDescription()
     * @generated
     */
    void setDescription(StringOrRefType value);

    /**
     * Returns the value of the '<em><b>Bounding Box</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.EnvelopeType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of bounding boxes (or envelopes) whose union describes the spatial domain of this object.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Bounding Box</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getExtentType_BoundingBox()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='boundingBox' namespace='##targetNamespace'"
     * @generated
     */
    EList<EnvelopeType> getBoundingBox();

    /**
     * Returns the value of the '<em><b>Bounding Polygon</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.PolygonType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of bounding polygons whose union describes the spatial domain of this object.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Bounding Polygon</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getExtentType_BoundingPolygon()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='boundingPolygon' namespace='##targetNamespace'"
     * @generated
     */
    EList<PolygonType> getBoundingPolygon();

    /**
     * Returns the value of the '<em><b>Vertical Extent</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.EnvelopeType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of vertical intervals whose union describes the spatial domain of this object.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Vertical Extent</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getExtentType_VerticalExtent()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='verticalExtent' namespace='##targetNamespace'"
     * @generated
     */
    EList<EnvelopeType> getVerticalExtent();

    /**
     * Returns the value of the '<em><b>Temporal Extent</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.TimePeriodType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of time periods whose union describes the spatial domain of this object.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Temporal Extent</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getExtentType_TemporalExtent()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='temporalExtent' namespace='##targetNamespace'"
     * @generated
     */
    EList<TimePeriodType> getTemporalExtent();

} // ExtentType

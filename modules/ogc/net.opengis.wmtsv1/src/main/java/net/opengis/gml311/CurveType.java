/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Curve Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Curve is a 1-dimensional primitive. Curves are continuous, connected, and have a measurable length in terms of the coordinate system. 
 * 				A curve is composed of one or more curve segments. Each curve segment within a curve may be defined using a different interpolation method. The curve segments are connected to one another, with the end point of each segment except the last being the start point of the next segment in the segment list.
 * 				The orientation of the curve is positive.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.CurveType#getSegments <em>Segments</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getCurveType()
 * @model extendedMetaData="name='CurveType' kind='elementOnly'"
 * @generated
 */
public interface CurveType extends AbstractCurveType {
    /**
     * Returns the value of the '<em><b>Segments</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This element encapsulates the segments of the curve.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Segments</em>' containment reference.
     * @see #setSegments(CurveSegmentArrayPropertyType)
     * @see net.opengis.gml311.Gml311Package#getCurveType_Segments()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='segments' namespace='##targetNamespace'"
     * @generated
     */
    CurveSegmentArrayPropertyType getSegments();

    /**
     * Sets the value of the '{@link net.opengis.gml311.CurveType#getSegments <em>Segments</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Segments</em>' containment reference.
     * @see #getSegments()
     * @generated
     */
    void setSegments(CurveSegmentArrayPropertyType value);

} // CurveType

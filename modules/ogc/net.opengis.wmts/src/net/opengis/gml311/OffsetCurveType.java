/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Offset Curve Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * An offset curve is a curve at a constant
 * 		 distance from the basis curve. They can be useful as a cheap
 * 		 and simple alternative to constructing curves that are offsets	
 * 		 by definition.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.OffsetCurveType#getOffsetBase <em>Offset Base</em>}</li>
 *   <li>{@link net.opengis.gml311.OffsetCurveType#getDistance <em>Distance</em>}</li>
 *   <li>{@link net.opengis.gml311.OffsetCurveType#getRefDirection <em>Ref Direction</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getOffsetCurveType()
 * @model extendedMetaData="name='OffsetCurveType' kind='elementOnly'"
 * @generated
 */
public interface OffsetCurveType extends AbstractCurveSegmentType {
    /**
     * Returns the value of the '<em><b>Offset Base</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * offsetBase is a reference to thecurve from which this
     * 							 curve is define	as an offset.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Offset Base</em>' containment reference.
     * @see #setOffsetBase(CurvePropertyType)
     * @see net.opengis.gml311.Gml311Package#getOffsetCurveType_OffsetBase()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='offsetBase' namespace='##targetNamespace'"
     * @generated
     */
    CurvePropertyType getOffsetBase();

    /**
     * Sets the value of the '{@link net.opengis.gml311.OffsetCurveType#getOffsetBase <em>Offset Base</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Offset Base</em>' containment reference.
     * @see #getOffsetBase()
     * @generated
     */
    void setOffsetBase(CurvePropertyType value);

    /**
     * Returns the value of the '<em><b>Distance</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * distance is the distance at which the
     * 							 offset curve is generated from the basis curve. In 2D systems, positive distances
     * 							 are to be to the left of the basis curve, and the negative distances are to be to the 
     * 							 right of the basis curve.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Distance</em>' containment reference.
     * @see #setDistance(LengthType)
     * @see net.opengis.gml311.Gml311Package#getOffsetCurveType_Distance()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='distance' namespace='##targetNamespace'"
     * @generated
     */
    LengthType getDistance();

    /**
     * Sets the value of the '{@link net.opengis.gml311.OffsetCurveType#getDistance <em>Distance</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Distance</em>' containment reference.
     * @see #getDistance()
     * @generated
     */
    void setDistance(LengthType value);

    /**
     * Returns the value of the '<em><b>Ref Direction</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * refDistance is used to define the vector
     *        direction of the offset curve from the basis curve. It can
     *        be omitted in the 2D case, where the distance can be 
     *        positive or negative. In that case, distance defines left
     *        side (positive distance) or right side (negative distance)
     *        with respect to the tangent to the basis curve.
     * 
     *        In 3D the basis curve shall have a well defined tangent 
     *        direction for every point. The offset curve at any point 
     *        in 3D, the basis curve shall have a well-defined tangent
     *        direction for every point. The offset curve at any point
     *        (parameter) on the basis curve c is in the direction
     *        -   -   -         -               
     *        s = v x t  where  v = c.refDirection()  
     *        and
     *        -
     *        t = c.tangent()
     *                                                     -
     *        For the offset direction to be well-defined, v shall not
     *        on any point of the curve be in the same, or opposite, 
     *        direction as
     *        - 
     *        t.
     * 
     *        The default value of the refDirection shall be the local
     *        co-ordinate axis vector for elevation, which indicates up for
     *        the curve in a geographic sense.
     * 
     *        NOTE! If the refDirection is the positive tangent to the
     *        local elevation axis ("points upward"), then the offset
     *        vector points to the left of the curve when viewed from
     *        above.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Ref Direction</em>' containment reference.
     * @see #setRefDirection(VectorType)
     * @see net.opengis.gml311.Gml311Package#getOffsetCurveType_RefDirection()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='refDirection' namespace='##targetNamespace'"
     * @generated
     */
    VectorType getRefDirection();

    /**
     * Sets the value of the '{@link net.opengis.gml311.OffsetCurveType#getRefDirection <em>Ref Direction</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Ref Direction</em>' containment reference.
     * @see #getRefDirection()
     * @generated
     */
    void setRefDirection(VectorType value);

} // OffsetCurveType

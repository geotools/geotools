/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Related Time Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.RelatedTimeType#getRelativePosition <em>Relative Position</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getRelatedTimeType()
 * @model extendedMetaData="name='RelatedTimeType' kind='elementOnly'"
 * @generated
 */
public interface RelatedTimeType extends TimePrimitivePropertyType {
    /**
     * Returns the value of the '<em><b>Relative Position</b></em>' attribute.
     * The literals are from the enumeration {@link net.opengis.gml311.RelativePositionType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Relative Position</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Relative Position</em>' attribute.
     * @see net.opengis.gml311.RelativePositionType
     * @see #isSetRelativePosition()
     * @see #unsetRelativePosition()
     * @see #setRelativePosition(RelativePositionType)
     * @see net.opengis.gml311.Gml311Package#getRelatedTimeType_RelativePosition()
     * @model unsettable="true"
     *        extendedMetaData="kind='attribute' name='relativePosition'"
     * @generated
     */
    RelativePositionType getRelativePosition();

    /**
     * Sets the value of the '{@link net.opengis.gml311.RelatedTimeType#getRelativePosition <em>Relative Position</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Relative Position</em>' attribute.
     * @see net.opengis.gml311.RelativePositionType
     * @see #isSetRelativePosition()
     * @see #unsetRelativePosition()
     * @see #getRelativePosition()
     * @generated
     */
    void setRelativePosition(RelativePositionType value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.RelatedTimeType#getRelativePosition <em>Relative Position</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetRelativePosition()
     * @see #getRelativePosition()
     * @see #setRelativePosition(RelativePositionType)
     * @generated
     */
    void unsetRelativePosition();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.RelatedTimeType#getRelativePosition <em>Relative Position</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Relative Position</em>' attribute is set.
     * @see #unsetRelativePosition()
     * @see #getRelativePosition()
     * @see #setRelativePosition(RelativePositionType)
     * @generated
     */
    boolean isSetRelativePosition();

} // RelatedTimeType

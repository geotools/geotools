/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Orientable Surface Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * OrientableSurface consists of a surface and an orientation. If the orientation is "+", then the OrientableSurface is identical to the baseSurface. If the orientation is "-", then the OrientableSurface is a reference to a Surface with an up-normal that reverses the direction for this OrientableSurface, the sense of "the top of the surface".
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.OrientableSurfaceType#getBaseSurface <em>Base Surface</em>}</li>
 *   <li>{@link net.opengis.gml311.OrientableSurfaceType#getOrientation <em>Orientation</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getOrientableSurfaceType()
 * @model extendedMetaData="name='OrientableSurfaceType' kind='elementOnly'"
 * @generated
 */
public interface OrientableSurfaceType extends AbstractSurfaceType {
    /**
     * Returns the value of the '<em><b>Base Surface</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * References or contains the base surface (positive orientation).
     * <!-- end-model-doc -->
     * @return the value of the '<em>Base Surface</em>' containment reference.
     * @see #setBaseSurface(SurfacePropertyType)
     * @see net.opengis.gml311.Gml311Package#getOrientableSurfaceType_BaseSurface()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='baseSurface' namespace='##targetNamespace'"
     * @generated
     */
    SurfacePropertyType getBaseSurface();

    /**
     * Sets the value of the '{@link net.opengis.gml311.OrientableSurfaceType#getBaseSurface <em>Base Surface</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Base Surface</em>' containment reference.
     * @see #getBaseSurface()
     * @generated
     */
    void setBaseSurface(SurfacePropertyType value);

    /**
     * Returns the value of the '<em><b>Orientation</b></em>' attribute.
     * The default value is <code>"+"</code>.
     * The literals are from the enumeration {@link net.opengis.gml311.SignType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * If the orientation is "+", then the OrientableSurface is identical to the baseSurface. If the orientation is "-", then the OrientableSurface is a reference to a Surface with an up-normal that reverses the direction for this OrientableSurface, the sense of "the top of the surface". "+" is the default value.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Orientation</em>' attribute.
     * @see net.opengis.gml311.SignType
     * @see #isSetOrientation()
     * @see #unsetOrientation()
     * @see #setOrientation(SignType)
     * @see net.opengis.gml311.Gml311Package#getOrientableSurfaceType_Orientation()
     * @model default="+" unsettable="true"
     *        extendedMetaData="kind='attribute' name='orientation'"
     * @generated
     */
    SignType getOrientation();

    /**
     * Sets the value of the '{@link net.opengis.gml311.OrientableSurfaceType#getOrientation <em>Orientation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Orientation</em>' attribute.
     * @see net.opengis.gml311.SignType
     * @see #isSetOrientation()
     * @see #unsetOrientation()
     * @see #getOrientation()
     * @generated
     */
    void setOrientation(SignType value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.OrientableSurfaceType#getOrientation <em>Orientation</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetOrientation()
     * @see #getOrientation()
     * @see #setOrientation(SignType)
     * @generated
     */
    void unsetOrientation();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.OrientableSurfaceType#getOrientation <em>Orientation</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Orientation</em>' attribute is set.
     * @see #unsetOrientation()
     * @see #getOrientation()
     * @see #setOrientation(SignType)
     * @generated
     */
    boolean isSetOrientation();

} // OrientableSurfaceType

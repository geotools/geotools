/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Multi Surface Domain Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.MultiSurfaceDomainType#getMultiSurface <em>Multi Surface</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getMultiSurfaceDomainType()
 * @model extendedMetaData="name='MultiSurfaceDomainType' kind='elementOnly'"
 * @generated
 */
public interface MultiSurfaceDomainType extends DomainSetType {
    /**
     * Returns the value of the '<em><b>Multi Surface</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Multi Surface</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Multi Surface</em>' containment reference.
     * @see #setMultiSurface(MultiSurfaceType)
     * @see net.opengis.gml311.Gml311Package#getMultiSurfaceDomainType_MultiSurface()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='MultiSurface' namespace='##targetNamespace'"
     * @generated
     */
    MultiSurfaceType getMultiSurface();

    /**
     * Sets the value of the '{@link net.opengis.gml311.MultiSurfaceDomainType#getMultiSurface <em>Multi Surface</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Multi Surface</em>' containment reference.
     * @see #getMultiSurface()
     * @generated
     */
    void setMultiSurface(MultiSurfaceType value);

} // MultiSurfaceDomainType

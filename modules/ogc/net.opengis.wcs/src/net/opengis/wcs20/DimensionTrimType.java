/**
 */
package net.opengis.wcs20;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dimension Trim Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs20.DimensionTrimType#getTrimLow <em>Trim Low</em>}</li>
 *   <li>{@link net.opengis.wcs20.DimensionTrimType#getTrimHigh <em>Trim High</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs20.Wcs20Package#getDimensionTrimType()
 * @model extendedMetaData="name='DimensionTrimType' kind='elementOnly'"
 * @generated
 */
public interface DimensionTrimType extends DimensionSubsetType {
    /**
     * Returns the value of the '<em><b>Trim Low</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Trim Low</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Trim Low</em>' attribute.
     * @see #setTrimLow(String)
     * @see net.opengis.wcs20.Wcs20Package#getDimensionTrimType_TrimLow()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='TrimLow' namespace='##targetNamespace'"
     * @generated
     */
    String getTrimLow();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.DimensionTrimType#getTrimLow <em>Trim Low</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Trim Low</em>' attribute.
     * @see #getTrimLow()
     * @generated
     */
    void setTrimLow(String value);

    /**
     * Returns the value of the '<em><b>Trim High</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Trim High</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Trim High</em>' attribute.
     * @see #setTrimHigh(String)
     * @see net.opengis.wcs20.Wcs20Package#getDimensionTrimType_TrimHigh()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='TrimHigh' namespace='##targetNamespace'"
     * @generated
     */
    String getTrimHigh();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.DimensionTrimType#getTrimHigh <em>Trim High</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Trim High</em>' attribute.
     * @see #getTrimHigh()
     * @generated
     */
    void setTrimHigh(String value);

} // DimensionTrimType

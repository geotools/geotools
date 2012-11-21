/**
 */
package net.opengis.wcs20;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dimension Slice Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs20.DimensionSliceType#getSlicePoint <em>Slice Point</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs20.Wcs20Package#getDimensionSliceType()
 * @model extendedMetaData="name='DimensionSliceType' kind='elementOnly'"
 * @generated
 */
public interface DimensionSliceType extends DimensionSubsetType {
    /**
     * Returns the value of the '<em><b>Slice Point</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Slice Point</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Slice Point</em>' attribute.
     * @see #setSlicePoint(String)
     * @see net.opengis.wcs20.Wcs20Package#getDimensionSliceType_SlicePoint()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='element' name='SlicePoint' namespace='##targetNamespace'"
     * @generated
     */
    String getSlicePoint();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.DimensionSliceType#getSlicePoint <em>Slice Point</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Slice Point</em>' attribute.
     * @see #getSlicePoint()
     * @generated
     */
    void setSlicePoint(String value);

} // DimensionSliceType

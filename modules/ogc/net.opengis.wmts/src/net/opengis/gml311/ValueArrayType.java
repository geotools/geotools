/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Value Array Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A Value Array is used for homogeneous arrays of primitive and aggregate values.  The member values may be scalars, composites, arrays or lists.  ValueArray has the same content model as CompositeValue, but the member values must be homogeneous.  The element declaration contains a Schematron constraint which expresses this restriction precisely.            Since the members are homogeneous, the referenceSystem (uom, codeSpace) may be specified on the ValueArray itself and implicitly inherited by all the members if desired.    Note that a_ScalarValueList is preferred for arrays of Scalar Values since this is a more efficient encoding.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.ValueArrayType#getCodeSpace <em>Code Space</em>}</li>
 *   <li>{@link net.opengis.gml311.ValueArrayType#getUom <em>Uom</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getValueArrayType()
 * @model extendedMetaData="name='ValueArrayType' kind='elementOnly'"
 * @generated
 */
public interface ValueArrayType extends CompositeValueType {
    /**
     * Returns the value of the '<em><b>Code Space</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Code Space</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Code Space</em>' attribute.
     * @see #setCodeSpace(String)
     * @see net.opengis.gml311.Gml311Package#getValueArrayType_CodeSpace()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='codeSpace'"
     * @generated
     */
    String getCodeSpace();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ValueArrayType#getCodeSpace <em>Code Space</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Code Space</em>' attribute.
     * @see #getCodeSpace()
     * @generated
     */
    void setCodeSpace(String value);

    /**
     * Returns the value of the '<em><b>Uom</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Uom</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Uom</em>' attribute.
     * @see #setUom(String)
     * @see net.opengis.gml311.Gml311Package#getValueArrayType_Uom()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='uom'"
     * @generated
     */
    String getUom();

    /**
     * Sets the value of the '{@link net.opengis.gml311.ValueArrayType#getUom <em>Uom</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uom</em>' attribute.
     * @see #getUom()
     * @generated
     */
    void setUom(String value);

} // ValueArrayType

/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Datum Base Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Basic encoding for datum objects, simplifying and restricting the DefinitionType as needed. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.AbstractDatumBaseType#getDatumName <em>Datum Name</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getAbstractDatumBaseType()
 * @model abstract="true"
 *        extendedMetaData="name='AbstractDatumBaseType' kind='elementOnly'"
 * @generated
 */
public interface AbstractDatumBaseType extends DefinitionType {
    /**
     * Returns the value of the '<em><b>Datum Name</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The name by which this datum is identified. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Datum Name</em>' containment reference.
     * @see #setDatumName(CodeType)
     * @see net.opengis.gml311.Gml311Package#getAbstractDatumBaseType_DatumName()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='datumName' namespace='##targetNamespace'"
     * @generated
     */
    CodeType getDatumName();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractDatumBaseType#getDatumName <em>Datum Name</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Datum Name</em>' containment reference.
     * @see #getDatumName()
     * @generated
     */
    void setDatumName(CodeType value);

} // AbstractDatumBaseType

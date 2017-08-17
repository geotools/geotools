/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Reference System Base Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Basic encoding for reference system objects, simplifying and restricting the DefinitionType as needed.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.AbstractReferenceSystemBaseType#getSrsName <em>Srs Name</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getAbstractReferenceSystemBaseType()
 * @model abstract="true"
 *        extendedMetaData="name='AbstractReferenceSystemBaseType' kind='elementOnly'"
 * @generated
 */
public interface AbstractReferenceSystemBaseType extends DefinitionType {
    /**
     * Returns the value of the '<em><b>Srs Name</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The name by which this reference system is identified.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Srs Name</em>' containment reference.
     * @see #setSrsName(CodeType)
     * @see net.opengis.gml311.Gml311Package#getAbstractReferenceSystemBaseType_SrsName()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='srsName' namespace='##targetNamespace'"
     * @generated
     */
    CodeType getSrsName();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractReferenceSystemBaseType#getSrsName <em>Srs Name</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Srs Name</em>' containment reference.
     * @see #getSrsName()
     * @generated
     */
    void setSrsName(CodeType value);

} // AbstractReferenceSystemBaseType

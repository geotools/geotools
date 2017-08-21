/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Coordinate System Base Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Basic encoding for coordinate system objects, simplifying and restricting the DefinitionType as needed. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.AbstractCoordinateSystemBaseType#getCsName <em>Cs Name</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getAbstractCoordinateSystemBaseType()
 * @model abstract="true"
 *        extendedMetaData="name='AbstractCoordinateSystemBaseType' kind='elementOnly'"
 * @generated
 */
public interface AbstractCoordinateSystemBaseType extends DefinitionType {
    /**
     * Returns the value of the '<em><b>Cs Name</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The name by which this coordinate system is identified. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Cs Name</em>' containment reference.
     * @see #setCsName(CodeType)
     * @see net.opengis.gml311.Gml311Package#getAbstractCoordinateSystemBaseType_CsName()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='csName' namespace='##targetNamespace'"
     * @generated
     */
    CodeType getCsName();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractCoordinateSystemBaseType#getCsName <em>Cs Name</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Cs Name</em>' containment reference.
     * @see #getCsName()
     * @generated
     */
    void setCsName(CodeType value);

} // AbstractCoordinateSystemBaseType

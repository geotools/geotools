/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Geometry Style Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * [complexType of] The style descriptor for geometries of a feature.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.GeometryStyleType#getSymbol <em>Symbol</em>}</li>
 *   <li>{@link net.opengis.gml311.GeometryStyleType#getStyle <em>Style</em>}</li>
 *   <li>{@link net.opengis.gml311.GeometryStyleType#getLabelStyle <em>Label Style</em>}</li>
 *   <li>{@link net.opengis.gml311.GeometryStyleType#getGeometryProperty <em>Geometry Property</em>}</li>
 *   <li>{@link net.opengis.gml311.GeometryStyleType#getGeometryType <em>Geometry Type</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getGeometryStyleType()
 * @model extendedMetaData="name='GeometryStyleType' kind='elementOnly'"
 * @generated
 */
public interface GeometryStyleType extends BaseStyleDescriptorType {
    /**
     * Returns the value of the '<em><b>Symbol</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The symbol property. Extends the gml:AssociationType to allow for remote referencing of symbols.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Symbol</em>' containment reference.
     * @see #setSymbol(SymbolType)
     * @see net.opengis.gml311.Gml311Package#getGeometryStyleType_Symbol()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='symbol' namespace='##targetNamespace'"
     * @generated
     */
    SymbolType getSymbol();

    /**
     * Sets the value of the '{@link net.opengis.gml311.GeometryStyleType#getSymbol <em>Symbol</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Symbol</em>' containment reference.
     * @see #getSymbol()
     * @generated
     */
    void setSymbol(SymbolType value);

    /**
     * Returns the value of the '<em><b>Style</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Deprecated in GML version 3.1.0. Use symbol with inline content instead.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Style</em>' attribute.
     * @see #setStyle(String)
     * @see net.opengis.gml311.Gml311Package#getGeometryStyleType_Style()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='style' namespace='##targetNamespace'"
     * @generated
     */
    String getStyle();

    /**
     * Sets the value of the '{@link net.opengis.gml311.GeometryStyleType#getStyle <em>Style</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Style</em>' attribute.
     * @see #getStyle()
     * @generated
     */
    void setStyle(String value);

    /**
     * Returns the value of the '<em><b>Label Style</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Label Style</em>' containment reference.
     * @see #setLabelStyle(LabelStylePropertyType)
     * @see net.opengis.gml311.Gml311Package#getGeometryStyleType_LabelStyle()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='labelStyle' namespace='##targetNamespace'"
     * @generated
     */
    LabelStylePropertyType getLabelStyle();

    /**
     * Sets the value of the '{@link net.opengis.gml311.GeometryStyleType#getLabelStyle <em>Label Style</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Label Style</em>' containment reference.
     * @see #getLabelStyle()
     * @generated
     */
    void setLabelStyle(LabelStylePropertyType value);

    /**
     * Returns the value of the '<em><b>Geometry Property</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Geometry Property</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Geometry Property</em>' attribute.
     * @see #setGeometryProperty(String)
     * @see net.opengis.gml311.Gml311Package#getGeometryStyleType_GeometryProperty()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='geometryProperty'"
     * @generated
     */
    String getGeometryProperty();

    /**
     * Sets the value of the '{@link net.opengis.gml311.GeometryStyleType#getGeometryProperty <em>Geometry Property</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Geometry Property</em>' attribute.
     * @see #getGeometryProperty()
     * @generated
     */
    void setGeometryProperty(String value);

    /**
     * Returns the value of the '<em><b>Geometry Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Geometry Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Geometry Type</em>' attribute.
     * @see #setGeometryType(String)
     * @see net.opengis.gml311.Gml311Package#getGeometryStyleType_GeometryType()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='geometryType'"
     * @generated
     */
    String getGeometryType();

    /**
     * Sets the value of the '{@link net.opengis.gml311.GeometryStyleType#getGeometryType <em>Geometry Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Geometry Type</em>' attribute.
     * @see #getGeometryType()
     * @generated
     */
    void setGeometryType(String value);

} // GeometryStyleType

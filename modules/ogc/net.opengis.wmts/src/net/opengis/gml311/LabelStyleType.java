/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Label Style Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * [complexType of] The style descriptor for labels of a feature, geometry or topology.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.LabelStyleType#getStyle <em>Style</em>}</li>
 *   <li>{@link net.opengis.gml311.LabelStyleType#getLabel <em>Label</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getLabelStyleType()
 * @model extendedMetaData="name='LabelStyleType' kind='elementOnly'"
 * @generated
 */
public interface LabelStyleType extends BaseStyleDescriptorType {
    /**
     * Returns the value of the '<em><b>Style</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Style</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Style</em>' attribute.
     * @see #setStyle(String)
     * @see net.opengis.gml311.Gml311Package#getLabelStyleType_Style()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='element' name='style' namespace='##targetNamespace'"
     * @generated
     */
    String getStyle();

    /**
     * Sets the value of the '{@link net.opengis.gml311.LabelStyleType#getStyle <em>Style</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Style</em>' attribute.
     * @see #getStyle()
     * @generated
     */
    void setStyle(String value);

    /**
     * Returns the value of the '<em><b>Label</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Label</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Label</em>' containment reference.
     * @see #setLabel(LabelType)
     * @see net.opengis.gml311.Gml311Package#getLabelStyleType_Label()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='label' namespace='##targetNamespace'"
     * @generated
     */
    LabelType getLabel();

    /**
     * Sets the value of the '{@link net.opengis.gml311.LabelStyleType#getLabel <em>Label</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Label</em>' containment reference.
     * @see #getLabel()
     * @generated
     */
    void setLabel(LabelType value);

} // LabelStyleType

/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Label Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Label is mixed -- composed of text and XPath expressions used to extract the useful information from the feature.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.LabelType#getMixed <em>Mixed</em>}</li>
 *   <li>{@link net.opengis.gml311.LabelType#getLabelExpression <em>Label Expression</em>}</li>
 *   <li>{@link net.opengis.gml311.LabelType#getTransform <em>Transform</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getLabelType()
 * @model extendedMetaData="name='LabelType' kind='mixed'"
 * @generated
 */
public interface LabelType extends EObject {
    /**
     * Returns the value of the '<em><b>Mixed</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Mixed</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Mixed</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getLabelType_Mixed()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='elementWildcard' name=':mixed'"
     * @generated
     */
    FeatureMap getMixed();

    /**
     * Returns the value of the '<em><b>Label Expression</b></em>' attribute list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Label Expression</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Label Expression</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getLabelType_LabelExpression()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='LabelExpression' namespace='##targetNamespace'"
     * @generated
     */
    EList<String> getLabelExpression();

    /**
     * Returns the value of the '<em><b>Transform</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Defines the geometric transformation of entities. There is no particular grammar defined for this value.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Transform</em>' attribute.
     * @see #setTransform(String)
     * @see net.opengis.gml311.Gml311Package#getLabelType_Transform()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='transform' namespace='##targetNamespace'"
     * @generated
     */
    String getTransform();

    /**
     * Sets the value of the '{@link net.opengis.gml311.LabelType#getTransform <em>Transform</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Transform</em>' attribute.
     * @see #getTransform()
     * @generated
     */
    void setTransform(String value);

} // LabelType

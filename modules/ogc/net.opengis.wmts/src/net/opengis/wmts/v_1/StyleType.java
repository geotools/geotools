/**
 */
package net.opengis.wmts.v_1;

import net.opengis.ows11.CodeType;
import net.opengis.ows11.DescriptionType;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Style Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.StyleType#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.StyleType#getLegendURL <em>Legend URL</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.StyleType#isIsDefault <em>Is Default</em>}</li>
 * </ul>
 *
 * @see net.opengis.wmts.v_1.wmtsv_1Package#getStyleType()
 * @model extendedMetaData="name='Style_._type' kind='elementOnly'"
 * @generated
 */
public interface StyleType extends DescriptionType {
    /**
     * Returns the value of the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * 									An unambiguous reference to this style, identifying 
     * 									a specific version when needed, normally used by software
     * 								
     * <!-- end-model-doc -->
     * @return the value of the '<em>Identifier</em>' containment reference.
     * @see #setIdentifier(CodeType)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getStyleType_Identifier()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Identifier' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    CodeType getIdentifier();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.StyleType#getIdentifier <em>Identifier</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Identifier</em>' containment reference.
     * @see #getIdentifier()
     * @generated
     */
    void setIdentifier(CodeType value);

    /**
     * Returns the value of the '<em><b>Legend URL</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wmts.v_1.LegendURLType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Description of an image that represents 
     * 								the legend of the map
     * <!-- end-model-doc -->
     * @return the value of the '<em>Legend URL</em>' containment reference list.
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getStyleType_LegendURL()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='LegendURL' namespace='##targetNamespace'"
     * @generated
     */
    EList<LegendURLType> getLegendURL();

    /**
     * Returns the value of the '<em><b>Is Default</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This style is used when no style is specified
     * <!-- end-model-doc -->
     * @return the value of the '<em>Is Default</em>' attribute.
     * @see #isSetIsDefault()
     * @see #unsetIsDefault()
     * @see #setIsDefault(boolean)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getStyleType_IsDefault()
     * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
     *        extendedMetaData="kind='attribute' name='isDefault'"
     * @generated
     */
    boolean isIsDefault();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.StyleType#isIsDefault <em>Is Default</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Is Default</em>' attribute.
     * @see #isSetIsDefault()
     * @see #unsetIsDefault()
     * @see #isIsDefault()
     * @generated
     */
    void setIsDefault(boolean value);

    /**
     * Unsets the value of the '{@link net.opengis.wmts.v_1.StyleType#isIsDefault <em>Is Default</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetIsDefault()
     * @see #isIsDefault()
     * @see #setIsDefault(boolean)
     * @generated
     */
    void unsetIsDefault();

    /**
     * Returns whether the value of the '{@link net.opengis.wmts.v_1.StyleType#isIsDefault <em>Is Default</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Is Default</em>' attribute is set.
     * @see #unsetIsDefault()
     * @see #isIsDefault()
     * @see #setIsDefault(boolean)
     * @generated
     */
    boolean isSetIsDefault();

} // StyleType

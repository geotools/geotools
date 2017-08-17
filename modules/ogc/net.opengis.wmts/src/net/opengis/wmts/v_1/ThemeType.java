/**
 */
package net.opengis.wmts.v_1;

import net.opengis.ows11.CodeType;
import net.opengis.ows11.DescriptionType;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Theme Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.ThemeType#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.ThemeType#getTheme <em>Theme</em>}</li>
 *   <li>{@link net.opengis.wmts.v_1.ThemeType#getLayerRef <em>Layer Ref</em>}</li>
 * </ul>
 *
 * @see net.opengis.wmts.v_1.wmtsv_1Package#getThemeType()
 * @model extendedMetaData="name='Theme_._type' kind='elementOnly'"
 * @generated
 */
public interface ThemeType extends DescriptionType {
    /**
     * Returns the value of the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Name of the theme
     * <!-- end-model-doc -->
     * @return the value of the '<em>Identifier</em>' containment reference.
     * @see #setIdentifier(CodeType)
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getThemeType_Identifier()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Identifier' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    CodeType getIdentifier();

    /**
     * Sets the value of the '{@link net.opengis.wmts.v_1.ThemeType#getIdentifier <em>Identifier</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Identifier</em>' containment reference.
     * @see #getIdentifier()
     * @generated
     */
    void setIdentifier(CodeType value);

    /**
     * Returns the value of the '<em><b>Theme</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wmts.v_1.ThemeType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * 									Metadata describing the child (subordinate) themes 
     * 									of this theme where layers available on this server 
     * 									can be classified
     * 								
     * <!-- end-model-doc -->
     * @return the value of the '<em>Theme</em>' containment reference list.
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getThemeType_Theme()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Theme' namespace='##targetNamespace'"
     * @generated
     */
    EList<ThemeType> getTheme();

    /**
     * Returns the value of the '<em><b>Layer Ref</b></em>' attribute list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Reference to layer
     * <!-- end-model-doc -->
     * @return the value of the '<em>Layer Ref</em>' attribute list.
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getThemeType_LayerRef()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='element' name='LayerRef' namespace='##targetNamespace'"
     * @generated
     */
    EList<String> getLayerRef();

} // ThemeType

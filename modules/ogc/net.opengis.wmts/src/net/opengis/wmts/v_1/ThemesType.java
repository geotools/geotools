/**
 */
package net.opengis.wmts.v_1;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Themes Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wmts.v_1.ThemesType#getTheme <em>Theme</em>}</li>
 * </ul>
 *
 * @see net.opengis.wmts.v_1.wmtsv_1Package#getThemesType()
 * @model extendedMetaData="name='Themes_._type' kind='elementOnly'"
 * @generated
 */
public interface ThemesType extends EObject {
    /**
     * Returns the value of the '<em><b>Theme</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wmts.v_1.ThemeType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * 							Metadata describing the top-level themes where 
     * 							layers available on this server can be classified.
     * 						
     * <!-- end-model-doc -->
     * @return the value of the '<em>Theme</em>' containment reference list.
     * @see net.opengis.wmts.v_1.wmtsv_1Package#getThemesType_Theme()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Theme' namespace='##targetNamespace'"
     * @generated
     */
    EList<ThemeType> getTheme();

} // ThemesType

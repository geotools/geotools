/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows11;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Sections Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Unordered list of zero or more names of requested sections in complete service metadata document. Each Section value shall contain an allowed section name as specified by each OWS specification. See Sections parameter subclause for more information.  
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows11.SectionsType#getSection <em>Section</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows11.Ows11Package#getSectionsType()
 * @model extendedMetaData="name='SectionsType' kind='elementOnly'"
 * @generated
 */
public interface SectionsType extends EObject {
    /**
     * Returns the value of the '<em><b>Section</b></em>' attribute list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Section</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Section</em>' attribute list.
     * @see net.opengis.ows11.Ows11Package#getSectionsType_Section()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='Section' namespace='##targetNamespace'"
     * @generated
     */
    EList getSection();

} // SectionsType

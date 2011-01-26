/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Languages Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Identifies a list of languages supported by this service.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.LanguagesType#getLanguage <em>Language</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getLanguagesType()
 * @model extendedMetaData="name='LanguagesType' kind='elementOnly'"
 * @generated
 */
public interface LanguagesType extends EObject {
    
    /**
     * 
     * @model type="java.lang.String"
     */
    EList getLanguage();

} // LanguagesType

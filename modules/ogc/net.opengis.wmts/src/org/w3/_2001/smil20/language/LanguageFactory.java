/**
 */
package org.w3._2001.smil20.language;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.w3._2001.smil20.language.LanguagePackage
 * @generated
 */
public interface LanguageFactory extends EFactory {
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    LanguageFactory eINSTANCE = org.w3._2001.smil20.language.impl.LanguageFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Animate Color Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Animate Color Type</em>'.
     * @generated
     */
    AnimateColorType createAnimateColorType();

    /**
     * Returns a new object of class '<em>Animate Motion Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Animate Motion Type</em>'.
     * @generated
     */
    AnimateMotionType createAnimateMotionType();

    /**
     * Returns a new object of class '<em>Animate Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Animate Type</em>'.
     * @generated
     */
    AnimateType createAnimateType();

    /**
     * Returns a new object of class '<em>Document Root</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Document Root</em>'.
     * @generated
     */
    DocumentRoot createDocumentRoot();

    /**
     * Returns a new object of class '<em>Set Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Set Type</em>'.
     * @generated
     */
    SetType createSetType();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    LanguagePackage getLanguagePackage();

} //LanguageFactory

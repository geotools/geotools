/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.w3.xlink;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.w3.xlink.XlinkPackage
 * @generated
 */
public interface XlinkFactory extends EFactory {
    /**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    XlinkFactory eINSTANCE = org.w3.xlink.impl.XlinkFactoryImpl.init();

    /**
	 * Returns a new object of class '<em>Document Root</em>'.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return a new object of class '<em>Document Root</em>'.
	 * @generated
	 */
    DocumentRoot createDocumentRoot();

    /**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
    XlinkPackage getXlinkPackage();

} //XlinkFactory

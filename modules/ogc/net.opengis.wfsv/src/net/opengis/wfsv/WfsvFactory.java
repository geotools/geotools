/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfsv;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see net.opengis.wfsv.WfsvPackage
 * @generated
 */
public interface WfsvFactory extends EFactory {
    /**
     * The singleton instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    WfsvFactory eINSTANCE = net.opengis.wfsv.impl.WfsvFactoryImpl.init();

    /**
     * Returns a new object of class '<em>Describe Versioned Feature Type Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Describe Versioned Feature Type Type</em>'.
     * @generated
     */
    DescribeVersionedFeatureTypeType createDescribeVersionedFeatureTypeType();

    /**
     * Returns a new object of class '<em>Difference Query Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Difference Query Type</em>'.
     * @generated
     */
    DifferenceQueryType createDifferenceQueryType();

    /**
     * Returns a new object of class '<em>Document Root</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Document Root</em>'.
     * @generated
     */
    DocumentRoot createDocumentRoot();

    /**
     * Returns a new object of class '<em>Get Diff Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Get Diff Type</em>'.
     * @generated
     */
    GetDiffType createGetDiffType();

    /**
     * Returns a new object of class '<em>Get Log Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Get Log Type</em>'.
     * @generated
     */
    GetLogType createGetLogType();

    /**
     * Returns a new object of class '<em>Get Versioned Feature Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Get Versioned Feature Type</em>'.
     * @generated
     */
    GetVersionedFeatureType createGetVersionedFeatureType();

    /**
     * Returns a new object of class '<em>Rollback Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Rollback Type</em>'.
     * @generated
     */
    RollbackType createRollbackType();

    /**
     * Returns a new object of class '<em>Versioned Delete Element Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Versioned Delete Element Type</em>'.
     * @generated
     */
    VersionedDeleteElementType createVersionedDeleteElementType();

    /**
     * Returns a new object of class '<em>Versioned Feature Collection Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Versioned Feature Collection Type</em>'.
     * @generated
     */
    VersionedFeatureCollectionType createVersionedFeatureCollectionType();

    /**
     * Returns a new object of class '<em>Versioned Update Element Type</em>'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return a new object of class '<em>Versioned Update Element Type</em>'.
     * @generated
     */
    VersionedUpdateElementType createVersionedUpdateElementType();

    /**
     * Returns the package supported by this factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the package supported by this factory.
     * @generated
     */
    WfsvPackage getWfsvPackage();

} //WfsvFactory

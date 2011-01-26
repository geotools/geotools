/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfsv.impl;

import net.opengis.wfsv.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class WfsvFactoryImpl extends EFactoryImpl implements WfsvFactory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static WfsvFactory init() {
        try {
            WfsvFactory theWfsvFactory = (WfsvFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.opengis.net/wfsv"); 
            if (theWfsvFactory != null) {
                return theWfsvFactory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new WfsvFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public WfsvFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
            case WfsvPackage.DESCRIBE_VERSIONED_FEATURE_TYPE_TYPE: return createDescribeVersionedFeatureTypeType();
            case WfsvPackage.DIFFERENCE_QUERY_TYPE: return createDifferenceQueryType();
            case WfsvPackage.DOCUMENT_ROOT: return createDocumentRoot();
            case WfsvPackage.GET_DIFF_TYPE: return createGetDiffType();
            case WfsvPackage.GET_LOG_TYPE: return createGetLogType();
            case WfsvPackage.GET_VERSIONED_FEATURE_TYPE: return createGetVersionedFeatureType();
            case WfsvPackage.ROLLBACK_TYPE: return createRollbackType();
            case WfsvPackage.VERSIONED_DELETE_ELEMENT_TYPE: return createVersionedDeleteElementType();
            case WfsvPackage.VERSIONED_FEATURE_COLLECTION_TYPE: return createVersionedFeatureCollectionType();
            case WfsvPackage.VERSIONED_UPDATE_ELEMENT_TYPE: return createVersionedUpdateElementType();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DescribeVersionedFeatureTypeType createDescribeVersionedFeatureTypeType() {
        DescribeVersionedFeatureTypeTypeImpl describeVersionedFeatureTypeType = new DescribeVersionedFeatureTypeTypeImpl();
        return describeVersionedFeatureTypeType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DifferenceQueryType createDifferenceQueryType() {
        DifferenceQueryTypeImpl differenceQueryType = new DifferenceQueryTypeImpl();
        return differenceQueryType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public DocumentRoot createDocumentRoot() {
        DocumentRootImpl documentRoot = new DocumentRootImpl();
        return documentRoot;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GetDiffType createGetDiffType() {
        GetDiffTypeImpl getDiffType = new GetDiffTypeImpl();
        return getDiffType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GetLogType createGetLogType() {
        GetLogTypeImpl getLogType = new GetLogTypeImpl();
        return getLogType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public GetVersionedFeatureType createGetVersionedFeatureType() {
        GetVersionedFeatureTypeImpl getVersionedFeatureType = new GetVersionedFeatureTypeImpl();
        return getVersionedFeatureType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public RollbackType createRollbackType() {
        RollbackTypeImpl rollbackType = new RollbackTypeImpl();
        return rollbackType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public VersionedDeleteElementType createVersionedDeleteElementType() {
        VersionedDeleteElementTypeImpl versionedDeleteElementType = new VersionedDeleteElementTypeImpl();
        return versionedDeleteElementType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public VersionedFeatureCollectionType createVersionedFeatureCollectionType() {
        VersionedFeatureCollectionTypeImpl versionedFeatureCollectionType = new VersionedFeatureCollectionTypeImpl();
        return versionedFeatureCollectionType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public VersionedUpdateElementType createVersionedUpdateElementType() {
        VersionedUpdateElementTypeImpl versionedUpdateElementType = new VersionedUpdateElementTypeImpl();
        return versionedUpdateElementType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public WfsvPackage getWfsvPackage() {
        return (WfsvPackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    public static WfsvPackage getPackage() {
        return WfsvPackage.eINSTANCE;
    }

} //WfsvFactoryImpl

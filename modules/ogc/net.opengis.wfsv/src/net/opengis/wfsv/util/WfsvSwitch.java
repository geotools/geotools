/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfsv.util;

import java.util.List;

import net.opengis.wfs.BaseRequestType;
import net.opengis.wfs.DeleteElementType;
import net.opengis.wfs.DescribeFeatureTypeType;
import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.NativeType;
import net.opengis.wfs.UpdateElementType;

import net.opengis.wfsv.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see net.opengis.wfsv.WfsvPackage
 * @generated
 */
public class WfsvSwitch {
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static WfsvPackage modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public WfsvSwitch() {
        if (modelPackage == null) {
            modelPackage = WfsvPackage.eINSTANCE;
        }
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    public Object doSwitch(EObject theEObject) {
        return doSwitch(theEObject.eClass(), theEObject);
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    protected Object doSwitch(EClass theEClass, EObject theEObject) {
        if (theEClass.eContainer() == modelPackage) {
            return doSwitch(theEClass.getClassifierID(), theEObject);
        }
        else {
            List eSuperTypes = theEClass.getESuperTypes();
            return
                eSuperTypes.isEmpty() ?
                    defaultCase(theEObject) :
                    doSwitch((EClass)eSuperTypes.get(0), theEObject);
        }
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    protected Object doSwitch(int classifierID, EObject theEObject) {
        switch (classifierID) {
            case WfsvPackage.ABSTRACT_VERSIONED_FEATURE_TYPE: {
                AbstractVersionedFeatureType abstractVersionedFeatureType = (AbstractVersionedFeatureType)theEObject;
                Object result = caseAbstractVersionedFeatureType(abstractVersionedFeatureType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case WfsvPackage.DESCRIBE_VERSIONED_FEATURE_TYPE_TYPE: {
                DescribeVersionedFeatureTypeType describeVersionedFeatureTypeType = (DescribeVersionedFeatureTypeType)theEObject;
                Object result = caseDescribeVersionedFeatureTypeType(describeVersionedFeatureTypeType);
                if (result == null) result = caseDescribeFeatureTypeType(describeVersionedFeatureTypeType);
                if (result == null) result = caseBaseRequestType(describeVersionedFeatureTypeType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case WfsvPackage.DIFFERENCE_QUERY_TYPE: {
                DifferenceQueryType differenceQueryType = (DifferenceQueryType)theEObject;
                Object result = caseDifferenceQueryType(differenceQueryType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case WfsvPackage.DOCUMENT_ROOT: {
                DocumentRoot documentRoot = (DocumentRoot)theEObject;
                Object result = caseDocumentRoot(documentRoot);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case WfsvPackage.GET_DIFF_TYPE: {
                GetDiffType getDiffType = (GetDiffType)theEObject;
                Object result = caseGetDiffType(getDiffType);
                if (result == null) result = caseBaseRequestType(getDiffType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case WfsvPackage.GET_LOG_TYPE: {
                GetLogType getLogType = (GetLogType)theEObject;
                Object result = caseGetLogType(getLogType);
                if (result == null) result = caseBaseRequestType(getLogType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case WfsvPackage.GET_VERSIONED_FEATURE_TYPE: {
                GetVersionedFeatureType getVersionedFeatureType = (GetVersionedFeatureType)theEObject;
                Object result = caseGetVersionedFeatureType(getVersionedFeatureType);
                if (result == null) result = caseGetFeatureType(getVersionedFeatureType);
                if (result == null) result = caseBaseRequestType(getVersionedFeatureType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case WfsvPackage.ROLLBACK_TYPE: {
                RollbackType rollbackType = (RollbackType)theEObject;
                Object result = caseRollbackType(rollbackType);
                if (result == null) result = caseNativeType(rollbackType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case WfsvPackage.VERSIONED_DELETE_ELEMENT_TYPE: {
                VersionedDeleteElementType versionedDeleteElementType = (VersionedDeleteElementType)theEObject;
                Object result = caseVersionedDeleteElementType(versionedDeleteElementType);
                if (result == null) result = caseDeleteElementType(versionedDeleteElementType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case WfsvPackage.VERSIONED_FEATURE_COLLECTION_TYPE: {
                VersionedFeatureCollectionType versionedFeatureCollectionType = (VersionedFeatureCollectionType)theEObject;
                Object result = caseVersionedFeatureCollectionType(versionedFeatureCollectionType);
                if (result == null) result = caseFeatureCollectionType(versionedFeatureCollectionType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case WfsvPackage.VERSIONED_UPDATE_ELEMENT_TYPE: {
                VersionedUpdateElementType versionedUpdateElementType = (VersionedUpdateElementType)theEObject;
                Object result = caseVersionedUpdateElementType(versionedUpdateElementType);
                if (result == null) result = caseUpdateElementType(versionedUpdateElementType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            default: return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Abstract Versioned Feature Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Abstract Versioned Feature Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseAbstractVersionedFeatureType(AbstractVersionedFeatureType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Describe Versioned Feature Type Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Describe Versioned Feature Type Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseDescribeVersionedFeatureTypeType(DescribeVersionedFeatureTypeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Difference Query Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Difference Query Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseDifferenceQueryType(DifferenceQueryType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Document Root</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Document Root</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseDocumentRoot(DocumentRoot object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Get Diff Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Get Diff Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseGetDiffType(GetDiffType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Get Log Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Get Log Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseGetLogType(GetLogType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Get Versioned Feature Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Get Versioned Feature Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseGetVersionedFeatureType(GetVersionedFeatureType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Rollback Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Rollback Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseRollbackType(RollbackType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Versioned Delete Element Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Versioned Delete Element Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseVersionedDeleteElementType(VersionedDeleteElementType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Versioned Feature Collection Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Versioned Feature Collection Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseVersionedFeatureCollectionType(VersionedFeatureCollectionType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Versioned Update Element Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Versioned Update Element Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseVersionedUpdateElementType(VersionedUpdateElementType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Base Request Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Base Request Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseBaseRequestType(BaseRequestType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Describe Feature Type Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Describe Feature Type Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseDescribeFeatureTypeType(DescribeFeatureTypeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Get Feature Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Get Feature Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseGetFeatureType(GetFeatureType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Native Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Native Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseNativeType(NativeType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Delete Element Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Delete Element Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseDeleteElementType(DeleteElementType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Feature Collection Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Feature Collection Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseFeatureCollectionType(FeatureCollectionType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Update Element Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Update Element Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public Object caseUpdateElementType(UpdateElementType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch, but this is the last case anyway.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject)
     * @generated
     */
    public Object defaultCase(EObject object) {
        return null;
    }

} //WfsvSwitch

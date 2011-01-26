/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfsv.util;

import net.opengis.wfs.BaseRequestType;
import net.opengis.wfs.DeleteElementType;
import net.opengis.wfs.DescribeFeatureTypeType;
import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.NativeType;
import net.opengis.wfs.UpdateElementType;

import net.opengis.wfsv.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see net.opengis.wfsv.WfsvPackage
 * @generated
 */
public class WfsvAdapterFactory extends AdapterFactoryImpl {
    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static WfsvPackage modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public WfsvAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = WfsvPackage.eINSTANCE;
        }
    }

    /**
     * Returns whether this factory is applicable for the type of the object.
     * <!-- begin-user-doc -->
     * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
     * <!-- end-user-doc -->
     * @return whether this factory is applicable for the type of the object.
     * @generated
     */
    public boolean isFactoryForType(Object object) {
        if (object == modelPackage) {
            return true;
        }
        if (object instanceof EObject) {
            return ((EObject)object).eClass().getEPackage() == modelPackage;
        }
        return false;
    }

    /**
     * The switch the delegates to the <code>createXXX</code> methods.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected WfsvSwitch modelSwitch =
        new WfsvSwitch() {
            public Object caseAbstractVersionedFeatureType(AbstractVersionedFeatureType object) {
                return createAbstractVersionedFeatureTypeAdapter();
            }
            public Object caseDescribeVersionedFeatureTypeType(DescribeVersionedFeatureTypeType object) {
                return createDescribeVersionedFeatureTypeTypeAdapter();
            }
            public Object caseDifferenceQueryType(DifferenceQueryType object) {
                return createDifferenceQueryTypeAdapter();
            }
            public Object caseDocumentRoot(DocumentRoot object) {
                return createDocumentRootAdapter();
            }
            public Object caseGetDiffType(GetDiffType object) {
                return createGetDiffTypeAdapter();
            }
            public Object caseGetLogType(GetLogType object) {
                return createGetLogTypeAdapter();
            }
            public Object caseGetVersionedFeatureType(GetVersionedFeatureType object) {
                return createGetVersionedFeatureTypeAdapter();
            }
            public Object caseRollbackType(RollbackType object) {
                return createRollbackTypeAdapter();
            }
            public Object caseVersionedDeleteElementType(VersionedDeleteElementType object) {
                return createVersionedDeleteElementTypeAdapter();
            }
            public Object caseVersionedFeatureCollectionType(VersionedFeatureCollectionType object) {
                return createVersionedFeatureCollectionTypeAdapter();
            }
            public Object caseVersionedUpdateElementType(VersionedUpdateElementType object) {
                return createVersionedUpdateElementTypeAdapter();
            }
            public Object caseBaseRequestType(BaseRequestType object) {
                return createBaseRequestTypeAdapter();
            }
            public Object caseDescribeFeatureTypeType(DescribeFeatureTypeType object) {
                return createDescribeFeatureTypeTypeAdapter();
            }
            public Object caseGetFeatureType(GetFeatureType object) {
                return createGetFeatureTypeAdapter();
            }
            public Object caseNativeType(NativeType object) {
                return createNativeTypeAdapter();
            }
            public Object caseDeleteElementType(DeleteElementType object) {
                return createDeleteElementTypeAdapter();
            }
            public Object caseFeatureCollectionType(FeatureCollectionType object) {
                return createFeatureCollectionTypeAdapter();
            }
            public Object caseUpdateElementType(UpdateElementType object) {
                return createUpdateElementTypeAdapter();
            }
            public Object defaultCase(EObject object) {
                return createEObjectAdapter();
            }
        };

    /**
     * Creates an adapter for the <code>target</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param target the object to adapt.
     * @return the adapter for the <code>target</code>.
     * @generated
     */
    public Adapter createAdapter(Notifier target) {
        return (Adapter)modelSwitch.doSwitch((EObject)target);
    }


    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfsv.AbstractVersionedFeatureType <em>Abstract Versioned Feature Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfsv.AbstractVersionedFeatureType
     * @generated
     */
    public Adapter createAbstractVersionedFeatureTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfsv.DescribeVersionedFeatureTypeType <em>Describe Versioned Feature Type Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfsv.DescribeVersionedFeatureTypeType
     * @generated
     */
    public Adapter createDescribeVersionedFeatureTypeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfsv.DifferenceQueryType <em>Difference Query Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfsv.DifferenceQueryType
     * @generated
     */
    public Adapter createDifferenceQueryTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfsv.DocumentRoot <em>Document Root</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfsv.DocumentRoot
     * @generated
     */
    public Adapter createDocumentRootAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfsv.GetDiffType <em>Get Diff Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfsv.GetDiffType
     * @generated
     */
    public Adapter createGetDiffTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfsv.GetLogType <em>Get Log Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfsv.GetLogType
     * @generated
     */
    public Adapter createGetLogTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfsv.GetVersionedFeatureType <em>Get Versioned Feature Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfsv.GetVersionedFeatureType
     * @generated
     */
    public Adapter createGetVersionedFeatureTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfsv.RollbackType <em>Rollback Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfsv.RollbackType
     * @generated
     */
    public Adapter createRollbackTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfsv.VersionedDeleteElementType <em>Versioned Delete Element Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfsv.VersionedDeleteElementType
     * @generated
     */
    public Adapter createVersionedDeleteElementTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfsv.VersionedFeatureCollectionType <em>Versioned Feature Collection Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfsv.VersionedFeatureCollectionType
     * @generated
     */
    public Adapter createVersionedFeatureCollectionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfsv.VersionedUpdateElementType <em>Versioned Update Element Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfsv.VersionedUpdateElementType
     * @generated
     */
    public Adapter createVersionedUpdateElementTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs.BaseRequestType <em>Base Request Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs.BaseRequestType
     * @generated
     */
    public Adapter createBaseRequestTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs.DescribeFeatureTypeType <em>Describe Feature Type Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs.DescribeFeatureTypeType
     * @generated
     */
    public Adapter createDescribeFeatureTypeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs.GetFeatureType <em>Get Feature Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs.GetFeatureType
     * @generated
     */
    public Adapter createGetFeatureTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs.NativeType <em>Native Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs.NativeType
     * @generated
     */
    public Adapter createNativeTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs.DeleteElementType <em>Delete Element Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs.DeleteElementType
     * @generated
     */
    public Adapter createDeleteElementTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs.FeatureCollectionType <em>Feature Collection Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs.FeatureCollectionType
     * @generated
     */
    public Adapter createFeatureCollectionTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link net.opengis.wfs.UpdateElementType <em>Update Element Type</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see net.opengis.wfs.UpdateElementType
     * @generated
     */
    public Adapter createUpdateElementTypeAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for the default case.
     * <!-- begin-user-doc -->
     * This default implementation returns null.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @generated
     */
    public Adapter createEObjectAdapter() {
        return null;
    }

} //WfsvAdapterFactory

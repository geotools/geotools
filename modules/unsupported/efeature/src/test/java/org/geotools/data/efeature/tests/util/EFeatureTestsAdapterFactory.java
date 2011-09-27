/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.geotools.data.efeature.tests.util;

import com.vividsolutions.jts.geom.Geometry;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.geotools.data.efeature.EFeature;

import org.geotools.data.efeature.tests.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.geotools.data.efeature.tests.EFeatureTestsPackage
 * @generated
 *
 * @source $URL$
 */
public class EFeatureTestsAdapterFactory extends AdapterFactoryImpl {
    /**
     * The cached model package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static EFeatureTestsPackage modelPackage;

    /**
     * Creates an instance of the adapter factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EFeatureTestsAdapterFactory() {
        if (modelPackage == null) {
            modelPackage = EFeatureTestsPackage.eINSTANCE;
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
    @Override
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
     * The switch that delegates to the <code>createXXX</code> methods.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected EFeatureTestsSwitch<Adapter> modelSwitch =
        new EFeatureTestsSwitch<Adapter>() {
            @Override
            public <A, G extends Geometry> Adapter caseEFeatureData(EFeatureData<A, G> object) {
                return createEFeatureDataAdapter();
            }
            @Override
            public Adapter caseNonGeoEObject(NonGeoEObject object) {
                return createNonGeoEObjectAdapter();
            }
            @Override
            public <A, G extends Geometry> Adapter caseEFeatureCompatibleData(EFeatureCompatibleData<A, G> object) {
                return createEFeatureCompatibleDataAdapter();
            }
            @Override
            public Adapter caseEFeature(EFeature object) {
                return createEFeatureAdapter();
            }
            @Override
            public Adapter defaultCase(EObject object) {
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
    @Override
    public Adapter createAdapter(Notifier target) {
        return modelSwitch.doSwitch((EObject)target);
    }


    /**
     * Creates a new adapter for an object of class '{@link org.geotools.data.efeature.tests.EFeatureData <em>EFeature Data</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.geotools.data.efeature.tests.EFeatureData
     * @generated
     */
    public Adapter createEFeatureDataAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.geotools.data.efeature.tests.NonGeoEObject <em>Non Geo EObject</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.geotools.data.efeature.tests.NonGeoEObject
     * @generated
     */
    public Adapter createNonGeoEObjectAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.geotools.data.efeature.tests.EFeatureCompatibleData <em>EFeature Compatible Data</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.geotools.data.efeature.tests.EFeatureCompatibleData
     * @generated
     */
    public Adapter createEFeatureCompatibleDataAdapter() {
        return null;
    }

    /**
     * Creates a new adapter for an object of class '{@link org.geotools.data.efeature.EFeature <em>EFeature</em>}'.
     * <!-- begin-user-doc -->
     * This default implementation returns null so that we can easily ignore cases;
     * it's useful to ignore a case when inheritance will catch all the cases anyway.
     * <!-- end-user-doc -->
     * @return the new adapter.
     * @see org.geotools.data.efeature.EFeature
     * @generated
     */
    public Adapter createEFeatureAdapter() {
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

} //EFeatureTestsAdapterFactory

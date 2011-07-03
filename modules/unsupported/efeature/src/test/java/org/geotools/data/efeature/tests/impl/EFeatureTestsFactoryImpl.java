/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.geotools.data.efeature.tests.impl;

import com.vividsolutions.jts.geom.Geometry;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.geotools.data.efeature.tests.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class EFeatureTestsFactoryImpl extends EFactoryImpl implements EFeatureTestsFactory {
    /**
     * Creates the default factory implementation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static EFeatureTestsFactory init() {
        try {
            EFeatureTestsFactory theEFeatureTestsFactory = (EFeatureTestsFactory)EPackage.Registry.INSTANCE.getEFactory("http://geotools.org/data/efeature/efeature-tests.ecore/1.0"); //$NON-NLS-1$ 
            if (theEFeatureTestsFactory != null) {
                return theEFeatureTestsFactory;
            }
        }
        catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new EFeatureTestsFactoryImpl();
    }

    /**
     * Creates an instance of the factory.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EFeatureTestsFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
            case EFeatureTestsPackage.EFEATURE_DATA: return createEFeatureData();
            case EFeatureTestsPackage.NON_GEO_EOBJECT: return createNonGeoEObject();
            case EFeatureTestsPackage.EFEATURE_COMPATIBLE_DATA: return createEFeatureCompatibleData();
            default:
                throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier"); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public <A, G extends Geometry> EFeatureData<A, G> createEFeatureData() {
        EFeatureDataImpl<A, G> eFeatureData = new EFeatureDataImpl<A, G>();
        return eFeatureData;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NonGeoEObject createNonGeoEObject() {
        NonGeoEObjectImpl nonGeoEObject = new NonGeoEObjectImpl();
        return nonGeoEObject;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public <A, G extends Geometry> EFeatureCompatibleData<A, G> createEFeatureCompatibleData() {
        EFeatureCompatibleDataImpl<A, G> eFeatureCompatibleData = new EFeatureCompatibleDataImpl<A, G>();
        return eFeatureCompatibleData;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public EFeatureTestsPackage getEFeatureTestsPackage() {
        return (EFeatureTestsPackage)getEPackage();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @deprecated
     * @generated
     */
    @Deprecated
    public static EFeatureTestsPackage getPackage() {
        return EFeatureTestsPackage.eINSTANCE;
    }

} //EFeatureTestsFactoryImpl

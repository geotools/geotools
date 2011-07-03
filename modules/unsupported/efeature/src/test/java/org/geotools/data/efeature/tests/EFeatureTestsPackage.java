/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.geotools.data.efeature.tests;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import org.geotools.data.efeature.EFeaturePackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.geotools.data.efeature.tests.EFeatureTestsFactory
 * @model kind="package"
 * @generated
 */
public interface EFeatureTestsPackage extends EPackage {
    /**
     * The package name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNAME = "efeature"; //$NON-NLS-1$

    /**
     * The package namespace URI.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_URI = "http://geotools.org/data/efeature/efeature-tests.ecore/1.0"; //$NON-NLS-1$

    /**
     * The package namespace name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    String eNS_PREFIX = "efeature-tests"; //$NON-NLS-1$

    /**
     * The singleton instance of the package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EFeatureTestsPackage eINSTANCE = org.geotools.data.efeature.tests.impl.EFeatureTestsPackageImpl.init();

    /**
     * The meta object id for the '{@link org.geotools.data.efeature.tests.impl.EFeatureDataImpl <em>EFeature Data</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.geotools.data.efeature.tests.impl.EFeatureDataImpl
     * @see org.geotools.data.efeature.tests.impl.EFeatureTestsPackageImpl#getEFeatureData()
     * @generated
     */
    int EFEATURE_DATA = 0;

    /**
     * The feature id for the '<em><b>ID</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EFEATURE_DATA__ID = EFeaturePackage.EFEATURE__ID;

    /**
     * The feature id for the '<em><b>Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EFEATURE_DATA__DATA = EFeaturePackage.EFEATURE__DATA;

    /**
     * The feature id for the '<em><b>SRID</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EFEATURE_DATA__SRID = EFeaturePackage.EFEATURE__SRID;

    /**
     * The feature id for the '<em><b>Default</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EFEATURE_DATA__DEFAULT = EFeaturePackage.EFEATURE__DEFAULT;

    /**
     * The feature id for the '<em><b>Structure</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EFEATURE_DATA__STRUCTURE = EFeaturePackage.EFEATURE__STRUCTURE;

    /**
     * The feature id for the '<em><b>Attribute</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EFEATURE_DATA__ATTRIBUTE = EFeaturePackage.EFEATURE_FEATURE_COUNT + 0;

    /**
     * The feature id for the '<em><b>Geometry</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EFEATURE_DATA__GEOMETRY = EFeaturePackage.EFEATURE_FEATURE_COUNT + 1;

    /**
     * The number of structural features of the '<em>EFeature Data</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EFEATURE_DATA_FEATURE_COUNT = EFeaturePackage.EFEATURE_FEATURE_COUNT + 2;


    /**
     * The meta object id for the '{@link org.geotools.data.efeature.tests.impl.NonGeoEObjectImpl <em>Non Geo EObject</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.geotools.data.efeature.tests.impl.NonGeoEObjectImpl
     * @see org.geotools.data.efeature.tests.impl.EFeatureTestsPackageImpl#getNonGeoEObject()
     * @generated
     */
    int NON_GEO_EOBJECT = 1;

    /**
     * The feature id for the '<em><b>Non Geo Attribute</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NON_GEO_EOBJECT__NON_GEO_ATTRIBUTE = 0;

    /**
     * The number of structural features of the '<em>Non Geo EObject</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int NON_GEO_EOBJECT_FEATURE_COUNT = 1;

    /**
     * The meta object id for the '{@link org.geotools.data.efeature.tests.impl.EFeatureCompatibleDataImpl <em>EFeature Compatible Data</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see org.geotools.data.efeature.tests.impl.EFeatureCompatibleDataImpl
     * @see org.geotools.data.efeature.tests.impl.EFeatureTestsPackageImpl#getEFeatureCompatibleData()
     * @generated
     */
    int EFEATURE_COMPATIBLE_DATA = 2;

    /**
     * The feature id for the '<em><b>ID</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EFEATURE_COMPATIBLE_DATA__ID = 0;

    /**
     * The feature id for the '<em><b>Attribute</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EFEATURE_COMPATIBLE_DATA__ATTRIBUTE = 1;

    /**
     * The feature id for the '<em><b>Geometry</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EFEATURE_COMPATIBLE_DATA__GEOMETRY = 2;

    /**
     * The feature id for the '<em><b>SRID</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EFEATURE_COMPATIBLE_DATA__SRID = 3;

    /**
     * The feature id for the '<em><b>Default</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EFEATURE_COMPATIBLE_DATA__DEFAULT = 4;

    /**
     * The number of structural features of the '<em>EFeature Compatible Data</em>' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     * @ordered
     */
    int EFEATURE_COMPATIBLE_DATA_FEATURE_COUNT = 5;

    /**
     * Returns the meta object for class '{@link org.geotools.data.efeature.tests.EFeatureData <em>EFeature Data</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>EFeature Data</em>'.
     * @see org.geotools.data.efeature.tests.EFeatureData
     * @generated
     */
    EClass getEFeatureData();

    /**
     * Returns the meta object for the attribute '{@link org.geotools.data.efeature.tests.EFeatureData#getAttribute <em>Attribute</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Attribute</em>'.
     * @see org.geotools.data.efeature.tests.EFeatureData#getAttribute()
     * @see #getEFeatureData()
     * @generated
     */
    EAttribute getEFeatureData_Attribute();

    /**
     * Returns the meta object for the attribute '{@link org.geotools.data.efeature.tests.EFeatureData#getGeometry <em>Geometry</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Geometry</em>'.
     * @see org.geotools.data.efeature.tests.EFeatureData#getGeometry()
     * @see #getEFeatureData()
     * @generated
     */
    EAttribute getEFeatureData_Geometry();

    /**
     * Returns the meta object for class '{@link org.geotools.data.efeature.tests.NonGeoEObject <em>Non Geo EObject</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>Non Geo EObject</em>'.
     * @see org.geotools.data.efeature.tests.NonGeoEObject
     * @generated
     */
    EClass getNonGeoEObject();

    /**
     * Returns the meta object for the attribute '{@link org.geotools.data.efeature.tests.NonGeoEObject#getNonGeoAttribute <em>Non Geo Attribute</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Non Geo Attribute</em>'.
     * @see org.geotools.data.efeature.tests.NonGeoEObject#getNonGeoAttribute()
     * @see #getNonGeoEObject()
     * @generated
     */
    EAttribute getNonGeoEObject_NonGeoAttribute();

    /**
     * Returns the meta object for class '{@link org.geotools.data.efeature.tests.EFeatureCompatibleData <em>EFeature Compatible Data</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for class '<em>EFeature Compatible Data</em>'.
     * @see org.geotools.data.efeature.tests.EFeatureCompatibleData
     * @generated
     */
    EClass getEFeatureCompatibleData();

    /**
     * Returns the meta object for the attribute '{@link org.geotools.data.efeature.tests.EFeatureCompatibleData#getAttribute <em>Attribute</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Attribute</em>'.
     * @see org.geotools.data.efeature.tests.EFeatureCompatibleData#getAttribute()
     * @see #getEFeatureCompatibleData()
     * @generated
     */
    EAttribute getEFeatureCompatibleData_Attribute();

    /**
     * Returns the meta object for the attribute '{@link org.geotools.data.efeature.tests.EFeatureCompatibleData#getGeometry <em>Geometry</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Geometry</em>'.
     * @see org.geotools.data.efeature.tests.EFeatureCompatibleData#getGeometry()
     * @see #getEFeatureCompatibleData()
     * @generated
     */
    EAttribute getEFeatureCompatibleData_Geometry();

    /**
     * Returns the meta object for the attribute '{@link org.geotools.data.efeature.tests.EFeatureCompatibleData#getSRID <em>SRID</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>SRID</em>'.
     * @see org.geotools.data.efeature.tests.EFeatureCompatibleData#getSRID()
     * @see #getEFeatureCompatibleData()
     * @generated
     */
    EAttribute getEFeatureCompatibleData_SRID();

    /**
     * Returns the meta object for the attribute '{@link org.geotools.data.efeature.tests.EFeatureCompatibleData#getDefault <em>Default</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>Default</em>'.
     * @see org.geotools.data.efeature.tests.EFeatureCompatibleData#getDefault()
     * @see #getEFeatureCompatibleData()
     * @generated
     */
    EAttribute getEFeatureCompatibleData_Default();

    /**
     * Returns the meta object for the attribute '{@link org.geotools.data.efeature.tests.EFeatureCompatibleData#getID <em>ID</em>}'.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the meta object for the attribute '<em>ID</em>'.
     * @see org.geotools.data.efeature.tests.EFeatureCompatibleData#getID()
     * @see #getEFeatureCompatibleData()
     * @generated
     */
    EAttribute getEFeatureCompatibleData_ID();

    /**
     * Returns the factory that creates the instances of the model.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the factory that creates the instances of the model.
     * @generated
     */
    EFeatureTestsFactory getEFeatureTestsFactory();

    /**
     * <!-- begin-user-doc -->
     * Defines literals for the meta objects that represent
     * <ul>
     *   <li>each class,</li>
     *   <li>each feature of each class,</li>
     *   <li>each enum,</li>
     *   <li>and each data type</li>
     * </ul>
     * <!-- end-user-doc -->
     * @generated
     */
    interface Literals {
        /**
         * The meta object literal for the '{@link org.geotools.data.efeature.tests.impl.EFeatureDataImpl <em>EFeature Data</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.geotools.data.efeature.tests.impl.EFeatureDataImpl
         * @see org.geotools.data.efeature.tests.impl.EFeatureTestsPackageImpl#getEFeatureData()
         * @generated
         */
        EClass EFEATURE_DATA = eINSTANCE.getEFeatureData();

        /**
         * The meta object literal for the '<em><b>Attribute</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EFEATURE_DATA__ATTRIBUTE = eINSTANCE.getEFeatureData_Attribute();

        /**
         * The meta object literal for the '<em><b>Geometry</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EFEATURE_DATA__GEOMETRY = eINSTANCE.getEFeatureData_Geometry();

        /**
         * The meta object literal for the '{@link org.geotools.data.efeature.tests.impl.NonGeoEObjectImpl <em>Non Geo EObject</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.geotools.data.efeature.tests.impl.NonGeoEObjectImpl
         * @see org.geotools.data.efeature.tests.impl.EFeatureTestsPackageImpl#getNonGeoEObject()
         * @generated
         */
        EClass NON_GEO_EOBJECT = eINSTANCE.getNonGeoEObject();

        /**
         * The meta object literal for the '<em><b>Non Geo Attribute</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute NON_GEO_EOBJECT__NON_GEO_ATTRIBUTE = eINSTANCE.getNonGeoEObject_NonGeoAttribute();

        /**
         * The meta object literal for the '{@link org.geotools.data.efeature.tests.impl.EFeatureCompatibleDataImpl <em>EFeature Compatible Data</em>}' class.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @see org.geotools.data.efeature.tests.impl.EFeatureCompatibleDataImpl
         * @see org.geotools.data.efeature.tests.impl.EFeatureTestsPackageImpl#getEFeatureCompatibleData()
         * @generated
         */
        EClass EFEATURE_COMPATIBLE_DATA = eINSTANCE.getEFeatureCompatibleData();

        /**
         * The meta object literal for the '<em><b>Attribute</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EFEATURE_COMPATIBLE_DATA__ATTRIBUTE = eINSTANCE.getEFeatureCompatibleData_Attribute();

        /**
         * The meta object literal for the '<em><b>Geometry</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EFEATURE_COMPATIBLE_DATA__GEOMETRY = eINSTANCE.getEFeatureCompatibleData_Geometry();

        /**
         * The meta object literal for the '<em><b>SRID</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EFEATURE_COMPATIBLE_DATA__SRID = eINSTANCE.getEFeatureCompatibleData_SRID();

        /**
         * The meta object literal for the '<em><b>Default</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EFEATURE_COMPATIBLE_DATA__DEFAULT = eINSTANCE.getEFeatureCompatibleData_Default();

        /**
         * The meta object literal for the '<em><b>ID</b></em>' attribute feature.
         * <!-- begin-user-doc -->
         * <!-- end-user-doc -->
         * @generated
         */
        EAttribute EFEATURE_COMPATIBLE_DATA__ID = eINSTANCE.getEFeatureCompatibleData_ID();

    }

} //EFeatureTestsPackage

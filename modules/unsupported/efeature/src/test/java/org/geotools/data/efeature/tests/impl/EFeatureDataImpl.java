/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.geotools.data.efeature.tests.impl;

import org.eclipse.emf.ecore.EClass;
import org.geotools.data.efeature.impl.EFeatureImpl;
import org.geotools.data.efeature.tests.EFeatureData;
import org.geotools.data.efeature.tests.EFeatureTestsPackage;

import com.vividsolutions.jts.geom.Geometry;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EFeature Data</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.geotools.data.efeature.tests.impl.EFeatureDataImpl#getAttribute <em>Attribute</em>}</li>
 *   <li>{@link org.geotools.data.efeature.tests.impl.EFeatureDataImpl#getGeometry <em>Geometry</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EFeatureDataImpl<A, G extends Geometry> extends EFeatureImpl implements EFeatureData<A, G> {
        
    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------
    
    /**
     * <!-- begin-user-doc -->
     * Default constructor.
     * <!-- end-user-doc -->
     * @generated NOT
     */
    protected EFeatureDataImpl() {
        super();
    }

    // ----------------------------------------------------- 
    //  EFeatureData implementation
    // -----------------------------------------------------
    
    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return EFeatureTestsPackage.Literals.EFEATURE_DATA;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public A getAttribute() {
        return (A)eGet(EFeatureTestsPackage.Literals.EFEATURE_DATA__ATTRIBUTE, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setAttribute(A newAttribute) {
        eSet(EFeatureTestsPackage.Literals.EFEATURE_DATA__ATTRIBUTE, newAttribute);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public G getGeometry() {
        return (G)eGet(EFeatureTestsPackage.Literals.EFEATURE_DATA__GEOMETRY, true);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void setGeometry(G newGeometry) {
        eSet(EFeatureTestsPackage.Literals.EFEATURE_DATA__GEOMETRY, newGeometry);
    }

} //EFeatureDataImpl

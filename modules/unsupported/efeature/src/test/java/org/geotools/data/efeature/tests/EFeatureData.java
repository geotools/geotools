/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.geotools.data.efeature.tests;

import com.vividsolutions.jts.geom.Geometry;

import org.geotools.data.efeature.EFeature;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EFeature Data</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.geotools.data.efeature.tests.EFeatureData#getAttribute <em>Attribute</em>}</li>
 *   <li>{@link org.geotools.data.efeature.tests.EFeatureData#getGeometry <em>Geometry</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.geotools.data.efeature.tests.EFeatureTestsPackage#getEFeatureData()
 * @model GBounds="org.geotools.data.efeature.Geometry"
 * @generated
 */
public interface EFeatureData<A, G extends Geometry> extends EFeature {
    
    /**
     * Returns the value of the '<em><b>Attribute</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Attribute</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Attribute</em>' attribute.
     * @see #setAttribute(Object)
     * @see org.geotools.data.efeature.tests.EFeatureTestsPackage#getEFeatureData_Attribute()
     * @model required="true"
     * @generated
     */
    A getAttribute();

    /**
     * Sets the value of the '{@link org.geotools.data.efeature.tests.EFeatureData#getAttribute <em>Attribute</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Attribute</em>' attribute.
     * @see #getAttribute()
     * @generated
     */
    void setAttribute(A value);

    /**
     * Returns the value of the '<em><b>Geometry</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Geometry</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Geometry</em>' attribute.
     * @see #setGeometry(Geometry)
     * @see org.geotools.data.efeature.tests.EFeatureTestsPackage#getEFeatureData_Geometry()
     * @model required="true"
     * @generated
     */
    G getGeometry();

    /**
     * Sets the value of the '{@link org.geotools.data.efeature.tests.EFeatureData#getGeometry <em>Geometry</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Geometry</em>' attribute.
     * @see #getGeometry()
     * @generated
     */
    void setGeometry(G value);

} // EFeatureData

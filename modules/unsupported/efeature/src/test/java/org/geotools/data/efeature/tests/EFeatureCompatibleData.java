/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.geotools.data.efeature.tests;

import com.vividsolutions.jts.geom.Geometry;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EFeature Compatible Data</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.geotools.data.efeature.tests.EFeatureCompatibleData#getID <em>ID</em>}</li>
 *   <li>{@link org.geotools.data.efeature.tests.EFeatureCompatibleData#getAttribute <em>Attribute</em>}</li>
 *   <li>{@link org.geotools.data.efeature.tests.EFeatureCompatibleData#getGeometry <em>Geometry</em>}</li>
 *   <li>{@link org.geotools.data.efeature.tests.EFeatureCompatibleData#getSRID <em>SRID</em>}</li>
 *   <li>{@link org.geotools.data.efeature.tests.EFeatureCompatibleData#getDefault <em>Default</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.geotools.data.efeature.tests.EFeatureTestsPackage#getEFeatureCompatibleData()
 * @model GBounds="org.geotools.data.efeature.Geometry"
 * @generated
 */
public interface EFeatureCompatibleData<A, G extends Geometry> extends EObject {
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
     * @see org.geotools.data.efeature.tests.EFeatureTestsPackage#getEFeatureCompatibleData_Attribute()
     * @model required="true"
     * @generated
     */
    A getAttribute();

    /**
     * Sets the value of the '{@link org.geotools.data.efeature.tests.EFeatureCompatibleData#getAttribute <em>Attribute</em>}' attribute.
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
     * @see org.geotools.data.efeature.tests.EFeatureTestsPackage#getEFeatureCompatibleData_Geometry()
     * @model required="true"
     * @generated
     */
    G getGeometry();

    /**
     * Sets the value of the '{@link org.geotools.data.efeature.tests.EFeatureCompatibleData#getGeometry <em>Geometry</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Geometry</em>' attribute.
     * @see #getGeometry()
     * @generated
     */
    void setGeometry(G value);

    /**
     * Returns the value of the '<em><b>SRID</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>SRID</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>SRID</em>' attribute.
     * @see #setSRID(String)
     * @see org.geotools.data.efeature.tests.EFeatureTestsPackage#getEFeatureCompatibleData_SRID()
     * @model required="true"
     * @generated
     */
    String getSRID();

    /**
     * Sets the value of the '{@link org.geotools.data.efeature.tests.EFeatureCompatibleData#getSRID <em>SRID</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>SRID</em>' attribute.
     * @see #getSRID()
     * @generated
     */
    void setSRID(String value);

    /**
     * Returns the value of the '<em><b>Default</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Default</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Default</em>' attribute.
     * @see #setDefault(String)
     * @see org.geotools.data.efeature.tests.EFeatureTestsPackage#getEFeatureCompatibleData_Default()
     * @model required="true"
     * @generated
     */
    String getDefault();

    /**
     * Sets the value of the '{@link org.geotools.data.efeature.tests.EFeatureCompatibleData#getDefault <em>Default</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Default</em>' attribute.
     * @see #getDefault()
     * @generated
     */
    void setDefault(String value);

    /**
     * Returns the value of the '<em><b>ID</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>ID</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>ID</em>' attribute.
     * @see #setID(String)
     * @see org.geotools.data.efeature.tests.EFeatureTestsPackage#getEFeatureCompatibleData_ID()
     * @model id="true" required="true"
     * @generated
     */
    String getID();

    /**
     * Sets the value of the '{@link org.geotools.data.efeature.tests.EFeatureCompatibleData#getID <em>ID</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>ID</em>' attribute.
     * @see #getID()
     * @generated
     */
    void setID(String value);

} // EFeatureCompatibleData

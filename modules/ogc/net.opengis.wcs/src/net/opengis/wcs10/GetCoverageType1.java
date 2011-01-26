/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Get Coverage Type1</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.GetCoverageType1#getDCPType <em>DCP Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getGetCoverageType1()
 * @model extendedMetaData="name='GetCoverage_._1_._type' kind='elementOnly'"
 * @generated
 */
public interface GetCoverageType1 extends EObject {
    /**
	 * Returns the value of the '<em><b>DCP Type</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.wcs10.DCPTypeType}.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>DCP Type</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>DCP Type</em>' containment reference list.
	 * @see net.opengis.wcs10.Wcs10Package#getGetCoverageType1_DCPType()
	 * @model type="net.opengis.wcs10.DCPTypeType" containment="true" required="true"
	 *        extendedMetaData="kind='element' name='DCPType' namespace='##targetNamespace'"
	 * @generated
	 */
    EList getDCPType();

} // GetCoverageType1

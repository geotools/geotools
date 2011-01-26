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
 * A representation of the model object '<em><b>Supported CR Ss Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Unordered list(s) of identifiers of Coordinate Reference Systems (CRSs) supported in server operation requests and responses.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.SupportedCRSsType#getRequestResponseCRSs <em>Request Response CR Ss</em>}</li>
 *   <li>{@link net.opengis.wcs10.SupportedCRSsType#getRequestCRSs <em>Request CR Ss</em>}</li>
 *   <li>{@link net.opengis.wcs10.SupportedCRSsType#getResponseCRSs <em>Response CR Ss</em>}</li>
 *   <li>{@link net.opengis.wcs10.SupportedCRSsType#getNativeCRSs <em>Native CR Ss</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getSupportedCRSsType()
 * @model extendedMetaData="name='SupportedCRSsType' kind='elementOnly'"
 * @generated
 */
public interface SupportedCRSsType extends EObject {
    /**
	 * Returns the value of the '<em><b>Request Response CR Ss</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.gml.CodeListType}.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Unordered list of identifiers of the CRSs in which the server can both accept requests and deliver responses for this data. These CRSs should include the native CRSs defined below.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Request Response CR Ss</em>' containment reference list.
	 * @see net.opengis.wcs10.Wcs10Package#getSupportedCRSsType_RequestResponseCRSs()
	 * @model type="net.opengis.gml.CodeListType" containment="true"
	 *        extendedMetaData="kind='element' name='requestResponseCRSs' namespace='##targetNamespace'"
	 * @generated
	 */
    EList getRequestResponseCRSs();

    /**
	 * Returns the value of the '<em><b>Request CR Ss</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.gml.CodeListType}.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Unordered list of identifiers of the CRSs in which the server can accept requests for this data. These CRSs should include the native CRSs defined below.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Request CR Ss</em>' containment reference list.
	 * @see net.opengis.wcs10.Wcs10Package#getSupportedCRSsType_RequestCRSs()
	 * @model type="net.opengis.gml.CodeListType" containment="true"
	 *        extendedMetaData="kind='element' name='requestCRSs' namespace='##targetNamespace'"
	 * @generated
	 */
    EList getRequestCRSs();

    /**
	 * Returns the value of the '<em><b>Response CR Ss</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.gml.CodeListType}.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Unordered list of identifiers of the CRSs in which the server can deliver responses for this data. These CRSs should include the native CRSs defined below.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Response CR Ss</em>' containment reference list.
	 * @see net.opengis.wcs10.Wcs10Package#getSupportedCRSsType_ResponseCRSs()
	 * @model type="net.opengis.gml.CodeListType" containment="true"
	 *        extendedMetaData="kind='element' name='responseCRSs' namespace='##targetNamespace'"
	 * @generated
	 */
    EList getResponseCRSs();

    /**
	 * Returns the value of the '<em><b>Native CR Ss</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.gml.CodeListType}.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Unordered list of identifiers of the CRSs in which the server stores this data, that is, the CRS(s) in which data can be obtained without any distortion or degradation.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Native CR Ss</em>' containment reference list.
	 * @see net.opengis.wcs10.Wcs10Package#getSupportedCRSsType_NativeCRSs()
	 * @model type="net.opengis.gml.CodeListType" containment="true"
	 *        extendedMetaData="kind='element' name='nativeCRSs' namespace='##targetNamespace'"
	 * @generated
	 */
    EList getNativeCRSs();

} // SupportedCRSsType

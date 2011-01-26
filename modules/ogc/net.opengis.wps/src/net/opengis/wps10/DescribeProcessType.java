/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Describe Process Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.DescribeProcessType#getIdentifier <em>Identifier</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getDescribeProcessType()
 * @model extendedMetaData="name='DescribeProcess_._type' kind='elementOnly'"
 * @generated
 */
public interface DescribeProcessType extends RequestBaseType {
    /**
     * Returns the value of the '<em><b>Identifier</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows11.CodeType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of one or more identifiers of the processes for which the client is requesting detailed descriptions. This element shall be repeated for each process for which a description is requested. These Identifiers are unordered, but the WPS shall return the process descriptions in the order in which they were requested.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Identifier</em>' containment reference list.
     * @see net.opengis.wps10.Wps10Package#getDescribeProcessType_Identifier()
     * @model type="net.opengis.ows11.CodeType" containment="true" required="true"
     *        extendedMetaData="kind='element' name='Identifier' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    EList getIdentifier();

} // DescribeProcessType

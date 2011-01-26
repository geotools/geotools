/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows11;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Exception Report Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows11.ExceptionReportType#getException <em>Exception</em>}</li>
 *   <li>{@link net.opengis.ows11.ExceptionReportType#getLang <em>Lang</em>}</li>
 *   <li>{@link net.opengis.ows11.ExceptionReportType#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows11.Ows11Package#getExceptionReportType()
 * @model extendedMetaData="name='ExceptionReport_._type' kind='elementOnly'"
 * @generated
 */
public interface ExceptionReportType extends EObject {
    /**
     * Returns the value of the '<em><b>Exception</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows11.ExceptionType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of one or more Exception elements that each describes an error. These Exception elements shall be interpreted by clients as being independent of one another (not hierarchical). 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Exception</em>' containment reference list.
     * @see net.opengis.ows11.Ows11Package#getExceptionReportType_Exception()
     * @model type="net.opengis.ows11.ExceptionType" containment="true" required="true"
     *        extendedMetaData="kind='element' name='Exception' namespace='##targetNamespace'"
     * @generated
     */
    EList getException();

    /**
     * Returns the value of the '<em><b>Lang</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifier of the language used by all included exception text values. These language identifiers shall be as specified in IETF RFC 4646. When this attribute is omitted, the language used is not identified. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Lang</em>' attribute.
     * @see #setLang(String)
     * @see net.opengis.ows11.Ows11Package#getExceptionReportType_Lang()
     * @model dataType="org.eclipse.emf.ecore.xml.namespace.LangType"
     *        extendedMetaData="kind='attribute' name='lang' namespace='http://www.w3.org/XML/1998/namespace'"
     * @generated
     */
    String getLang();

    /**
     * Sets the value of the '{@link net.opengis.ows11.ExceptionReportType#getLang <em>Lang</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Lang</em>' attribute.
     * @see #getLang()
     * @generated
     */
    void setLang(String value);

    /**
     * Returns the value of the '<em><b>Version</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Specification version for OWS operation. The string value shall contain one x.y.z "version" value (e.g., "2.1.3"). A version number shall contain three non-negative integers separated by decimal points, in the form "x.y.z". The integers y and z shall not exceed 99. Each version shall be for the Implementation Specification (document) and the associated XML Schemas to which requested operations will conform. An Implementation Specification version normally specifies XML Schemas against which an XML encoded operation response must conform and should be validated. See Version negotiation subclause for more information. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Version</em>' attribute.
     * @see #setVersion(String)
     * @see net.opengis.ows11.Ows11Package#getExceptionReportType_Version()
     * @model dataType="net.opengis.ows11.VersionType1" required="true"
     *        extendedMetaData="kind='attribute' name='version'"
     * @generated
     */
    String getVersion();

    /**
     * Sets the value of the '{@link net.opengis.ows11.ExceptionReportType#getVersion <em>Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Version</em>' attribute.
     * @see #getVersion()
     * @generated
     */
    void setVersion(String value);

} // ExceptionReportType

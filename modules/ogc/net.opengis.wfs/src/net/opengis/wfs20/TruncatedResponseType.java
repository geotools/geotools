/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20;

import net.opengis.ows11.ExceptionReportType;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Truncated Response Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.TruncatedResponseType#getExceptionReport <em>Exception Report</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getTruncatedResponseType()
 * @model extendedMetaData="name='truncatedResponse_._type' kind='elementOnly'"
 * @generated
 */
public interface TruncatedResponseType extends EObject {
    /**
     * Returns the value of the '<em><b>Exception Report</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Report message returned to the client that requested any OWS operation when the server detects an error while processing that operation request. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Exception Report</em>' containment reference.
     * @see #setExceptionReport(ExceptionReportType)
     * @see net.opengis.wfs20.Wfs20Package#getTruncatedResponseType_ExceptionReport()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='ExceptionReport' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    ExceptionReportType getExceptionReport();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.TruncatedResponseType#getExceptionReport <em>Exception Report</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Exception Report</em>' containment reference.
     * @see #getExceptionReport()
     * @generated
     */
    void setExceptionReport(ExceptionReportType value);

} // TruncatedResponseType

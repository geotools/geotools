/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10;

import net.opengis.ows11.ExceptionReportType;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Process Failed Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Indicator that the process has failed to execute successfully. The reason for failure is given in the exception report.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.ProcessFailedType#getExceptionReport <em>Exception Report</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getProcessFailedType()
 * @model extendedMetaData="name='ProcessFailedType' kind='elementOnly'"
 * @generated
 */
public interface ProcessFailedType extends EObject {
    /**
     * Returns the value of the '<em><b>Exception Report</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Report message returned to the client that requested any OWS operation when the server detects an error while processing that operation request.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Exception Report</em>' containment reference.
     * @see #setExceptionReport(ExceptionReportType)
     * @see net.opengis.wps10.Wps10Package#getProcessFailedType_ExceptionReport()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='ExceptionReport' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    ExceptionReportType getExceptionReport();

    /**
     * Sets the value of the '{@link net.opengis.wps10.ProcessFailedType#getExceptionReport <em>Exception Report</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Exception Report</em>' containment reference.
     * @see #getExceptionReport()
     * @generated
     */
    void setExceptionReport(ExceptionReportType value);

} // ProcessFailedType

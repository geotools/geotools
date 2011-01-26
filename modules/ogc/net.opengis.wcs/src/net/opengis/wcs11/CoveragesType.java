/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Coverages Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Group of coverages that can be used as the response from the WCS GetCoverage operation, allowing each coverage to include or reference multiple files. This Coverages element may also be used for outputs from, or inputs to, other OWS operations. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs11.CoveragesType#getCoverage <em>Coverage</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs11.Wcs111Package#getCoveragesType()
 * @model extendedMetaData="name='CoveragesType' kind='elementOnly'"
 * @generated
 */
public interface CoveragesType extends EObject {
    /**
     * Returns the value of the '<em><b>Coverage</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows11.ReferenceGroupType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Complete data for one coverage, referencing each coverage file either remotely or locally in the same message. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Coverage</em>' containment reference list.
     * @see net.opengis.wcs11.Wcs111Package#getCoveragesType_Coverage()
     * @model type="net.opengis.ows11.ReferenceGroupType" containment="true" required="true"
     *        extendedMetaData="kind='element' name='Coverage' namespace='##targetNamespace'"
     * @generated
     */
    EList getCoverage();

} // CoveragesType

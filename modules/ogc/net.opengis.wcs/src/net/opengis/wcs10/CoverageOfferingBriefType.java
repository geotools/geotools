/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Coverage Offering Brief Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Brief description of one coverage avaialble from a WCS.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.CoverageOfferingBriefType#getLonLatEnvelope <em>Lon Lat Envelope</em>}</li>
 *   <li>{@link net.opengis.wcs10.CoverageOfferingBriefType#getKeywords <em>Keywords</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getCoverageOfferingBriefType()
 * @model extendedMetaData="name='CoverageOfferingBriefType' kind='elementOnly'"
 * @generated
 */
public interface CoverageOfferingBriefType extends AbstractDescriptionType {
    /**
	 * Returns the value of the '<em><b>Lon Lat Envelope</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Lon Lat Envelope</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Lon Lat Envelope</em>' containment reference.
	 * @see #setLonLatEnvelope(LonLatEnvelopeType)
	 * @see net.opengis.wcs10.Wcs10Package#getCoverageOfferingBriefType_LonLatEnvelope()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='lonLatEnvelope' namespace='##targetNamespace'"
	 * @generated
	 */
    LonLatEnvelopeType getLonLatEnvelope();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.CoverageOfferingBriefType#getLonLatEnvelope <em>Lon Lat Envelope</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lon Lat Envelope</em>' containment reference.
	 * @see #getLonLatEnvelope()
	 * @generated
	 */
    void setLonLatEnvelope(LonLatEnvelopeType value);

    /**
	 * Returns the value of the '<em><b>Keywords</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.wcs10.KeywordsType}.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Unordered list of one or more commonly used or formalised word(s) or phrase(s) used to describe the subject. When needed, the optional "type" can name the type of the associated list of keywords that shall all have the same type. Also when needed, the codeSpace attribute of that "type" can also reference the type name authority and/or thesaurus. (Largely based on MD_Keywords class in ISO 19115.)
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Keywords</em>' containment reference list.
	 * @see net.opengis.wcs10.Wcs10Package#getCoverageOfferingBriefType_Keywords()
	 * @model type="net.opengis.wcs10.KeywordsType" containment="true"
	 *        extendedMetaData="kind='element' name='keywords' namespace='##targetNamespace'"
	 * @generated
	 */
    EList getKeywords();

} // CoverageOfferingBriefType

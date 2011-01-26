/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Coverage Offering Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Full description of one coverage available from a WCS instance.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.CoverageOfferingType#getDomainSet <em>Domain Set</em>}</li>
 *   <li>{@link net.opengis.wcs10.CoverageOfferingType#getRangeSet <em>Range Set</em>}</li>
 *   <li>{@link net.opengis.wcs10.CoverageOfferingType#getSupportedCRSs <em>Supported CR Ss</em>}</li>
 *   <li>{@link net.opengis.wcs10.CoverageOfferingType#getSupportedFormats <em>Supported Formats</em>}</li>
 *   <li>{@link net.opengis.wcs10.CoverageOfferingType#getSupportedInterpolations <em>Supported Interpolations</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getCoverageOfferingType()
 * @model extendedMetaData="name='CoverageOfferingType' kind='elementOnly'"
 * @generated
 */
public interface CoverageOfferingType extends CoverageOfferingBriefType {
    /**
	 * Returns the value of the '<em><b>Domain Set</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Domain Set</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Domain Set</em>' containment reference.
	 * @see #setDomainSet(DomainSetType)
	 * @see net.opengis.wcs10.Wcs10Package#getCoverageOfferingType_DomainSet()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='domainSet' namespace='##targetNamespace'"
	 * @generated
	 */
    DomainSetType getDomainSet();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.CoverageOfferingType#getDomainSet <em>Domain Set</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Domain Set</em>' containment reference.
	 * @see #getDomainSet()
	 * @generated
	 */
    void setDomainSet(DomainSetType value);

    /**
	 * Returns the value of the '<em><b>Range Set</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * GML property containing one RangeSet GML object.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Range Set</em>' containment reference.
	 * @see #setRangeSet(RangeSetType1)
	 * @see net.opengis.wcs10.Wcs10Package#getCoverageOfferingType_RangeSet()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='rangeSet' namespace='##targetNamespace'"
	 * @generated
	 */
    RangeSetType1 getRangeSet();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.CoverageOfferingType#getRangeSet <em>Range Set</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Range Set</em>' containment reference.
	 * @see #getRangeSet()
	 * @generated
	 */
    void setRangeSet(RangeSetType1 value);

    /**
	 * Returns the value of the '<em><b>Supported CR Ss</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Supported CR Ss</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Supported CR Ss</em>' containment reference.
	 * @see #setSupportedCRSs(SupportedCRSsType)
	 * @see net.opengis.wcs10.Wcs10Package#getCoverageOfferingType_SupportedCRSs()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='supportedCRSs' namespace='##targetNamespace'"
	 * @generated
	 */
    SupportedCRSsType getSupportedCRSs();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.CoverageOfferingType#getSupportedCRSs <em>Supported CR Ss</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Supported CR Ss</em>' containment reference.
	 * @see #getSupportedCRSs()
	 * @generated
	 */
    void setSupportedCRSs(SupportedCRSsType value);

    /**
	 * Returns the value of the '<em><b>Supported Formats</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Supported Formats</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Supported Formats</em>' containment reference.
	 * @see #setSupportedFormats(SupportedFormatsType)
	 * @see net.opengis.wcs10.Wcs10Package#getCoverageOfferingType_SupportedFormats()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='supportedFormats' namespace='##targetNamespace'"
	 * @generated
	 */
    SupportedFormatsType getSupportedFormats();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.CoverageOfferingType#getSupportedFormats <em>Supported Formats</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Supported Formats</em>' containment reference.
	 * @see #getSupportedFormats()
	 * @generated
	 */
    void setSupportedFormats(SupportedFormatsType value);

    /**
	 * Returns the value of the '<em><b>Supported Interpolations</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Specifies whether and how the server can interpolate coverage values over the spatial domain, when a GetCoverage request requires resampling, reprojection, or other generalization. If supportedInterpolations is absent or empty with no default, then clients should assume nearest-neighbor interpolation. If the only interpolation method listed is ‘none’, clients can only retrieve coverages from this layer in its native CRS and at its native resolution.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Supported Interpolations</em>' containment reference.
	 * @see #setSupportedInterpolations(SupportedInterpolationsType)
	 * @see net.opengis.wcs10.Wcs10Package#getCoverageOfferingType_SupportedInterpolations()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='supportedInterpolations' namespace='##targetNamespace'"
	 * @generated
	 */
    SupportedInterpolationsType getSupportedInterpolations();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.CoverageOfferingType#getSupportedInterpolations <em>Supported Interpolations</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Supported Interpolations</em>' containment reference.
	 * @see #getSupportedInterpolations()
	 * @generated
	 */
    void setSupportedInterpolations(SupportedInterpolationsType value);

} // CoverageOfferingType

/**
 */
package net.opengis.wcs20;

import java.lang.Object;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Coverage Description Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs20.CoverageDescriptionType#getCoverageId <em>Coverage Id</em>}</li>
 *   <li>{@link net.opengis.wcs20.CoverageDescriptionType#getCoverageFunction <em>Coverage Function</em>}</li>
 *   <li>{@link net.opengis.wcs20.CoverageDescriptionType#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link net.opengis.wcs20.CoverageDescriptionType#getDomainSet <em>Domain Set</em>}</li>
 *   <li>{@link net.opengis.wcs20.CoverageDescriptionType#getRangeType <em>Range Type</em>}</li>
 *   <li>{@link net.opengis.wcs20.CoverageDescriptionType#getServiceParameters <em>Service Parameters</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs20.Wcs20Package#getCoverageDescriptionType()
 * @model extendedMetaData="name='CoverageDescriptionType' kind='elementOnly'"
 * @generated
 */
public interface CoverageDescriptionType extends EObject {
    /**
     * Returns the value of the '<em><b>Coverage Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This element represents coverage identifiers. It uses the same type as gml:id to allow for identifier values to be used in both contexts.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Coverage Id</em>' attribute.
     * @see #setCoverageId(String)
     * @see net.opengis.wcs20.Wcs20Package#getCoverageDescriptionType_CoverageId()
     * @model dataType="org.eclipse.emf.ecore.xml.type.NCName" required="true"
     *        extendedMetaData="kind='element' name='CoverageId' namespace='##targetNamespace'"
     * @generated
     */
    String getCoverageId();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.CoverageDescriptionType#getCoverageId <em>Coverage Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coverage Id</em>' attribute.
     * @see #getCoverageId()
     * @generated
     */
    void setCoverageId(String value);

    /**
     * Returns the value of the '<em><b>Coverage Function</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The gml:coverageFunction property describes the mapping function from the domain to the range of the coverage.
     * The value of the CoverageFunction is one of gml:CoverageMappingRule and gml:GridFunction.
     * If the gml:coverageFunction property is omitted for a gridded coverage (including rectified gridded coverages) the gml:startPoint is assumed to be the value of the gml:low property in the gml:Grid geometry, and the gml:sequenceRule is assumed to be linear and the gml:axisOrder property is assumed to be "+1 +2".
     * <!-- end-model-doc -->
     * @return the value of the '<em>Coverage Function</em>' containment reference.
     * @see #setCoverageFunction(Object)
     * @see net.opengis.wcs20.Wcs20Package#getCoverageDescriptionType_CoverageFunction()
     * @model type="java.lang.Object" containment="true"
     *        extendedMetaData="kind='element' name='coverageFunction' namespace='http://www.opengis.net/gml/3.2'"
     */
    Object getCoverageFunction();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.CoverageDescriptionType#getCoverageFunction <em>Coverage Function</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coverage Function</em>' containment reference.
     * @see #getCoverageFunction()
     * @generated
     */
    void setCoverageFunction(Object value);

    /**
     * Returns the value of the '<em><b>Metadata</b></em>' containment reference list.
     * The list contents are of type {@link java.lang.Object}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This is a hook for adding any further information to a coverage, such as domain-specific metadata. Recommended use is to use the XML extension mechanism, such as in a WCS extension or Application Profile, to define the desired metadata structure.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Metadata</em>' containment reference list.
     * @see net.opengis.wcs20.Wcs20Package#getCoverageDescriptionType_Metadata()
     * @model type="net.opengis.wcs20.Object" containment="true"
     *        extendedMetaData="kind='element' name='metadata' namespace='http://www.opengis.net/gmlcov/1.0'"
     * @generated
     */
    EList<Object> getMetadata();

    /**
     * Returns the value of the '<em><b>Domain Set</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The gml:domainSet property element describes the spatio-temporal region of interest, within which the coverage is defined. Its content model is given by gml:DomainSetType.
     * The value of the domain is thus a choice between a gml:AbstractGeometry and a gml:AbstractTimeObject.  In the instance these abstract elements will normally be substituted by a geometry complex or temporal complex, to represent spatial coverages and time-series, respectively.
     * The presence of the gml:AssociationAttributeGroup means that domainSet follows the usual GML property model and may use the xlink:href attribute to point to the domain, as an alternative to describing the domain inline. Ownership semantics may be provided using the gml:OwnershipAttributeGroup.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Domain Set</em>' containment reference.
     * @see #setDomainSet(Object)
     * @see net.opengis.wcs20.Wcs20Package#getCoverageDescriptionType_DomainSet()
     * @model type="net.opengis.wcs20.Object" containment="true" required="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='domainSet' namespace='http://www.opengis.net/gml/3.2' group='http://www.opengis.net/gml/3.2#domainSet:group'"
     * @generated
     */
    Object getDomainSet();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.CoverageDescriptionType#getDomainSet <em>Domain Set</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Domain Set</em>' containment reference.
     * @see #getDomainSet()
     * @generated
     */
    void setDomainSet(Object value);

    /**
     * Returns the value of the '<em><b>Range Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The rangeType element describes the structure of a coverage's range values, introduced for coverage definitions used, e.g., by WCS 2.0.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Range Type</em>' containment reference.
     * @see #setRangeType(Object)
     * @see net.opengis.wcs20.Wcs20Package#getCoverageDescriptionType_RangeType()
     * @model type="net.opengis.wcs20.Object" containment="true" required="true"
     *        extendedMetaData="kind='element' name='rangeType' namespace='http://www.opengis.net/gmlcov/1.0'"
     * @generated
     */
    Object getRangeType();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.CoverageDescriptionType#getRangeType <em>Range Type</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Range Type</em>' containment reference.
     * @see #getRangeType()
     * @generated
     */
    void setRangeType(Object value);

    /**
     * Returns the value of the '<em><b>Service Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * ServiceParameters further define how the corresponding coverage is accessible. CoverageSubtype helps identifying the type of coverage on hand, in particular with respect to the potential size of its domainSet and rangeSet components. Extension elements allow WCS extensions to plug in their particular coverage-specific service information.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Service Parameters</em>' containment reference.
     * @see #setServiceParameters(ServiceParametersType)
     * @see net.opengis.wcs20.Wcs20Package#getCoverageDescriptionType_ServiceParameters()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='ServiceParameters' namespace='##targetNamespace'"
     * @generated
     */
    ServiceParametersType getServiceParameters();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.CoverageDescriptionType#getServiceParameters <em>Service Parameters</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Service Parameters</em>' containment reference.
     * @see #getServiceParameters()
     * @generated
     */
    void setServiceParameters(ServiceParametersType value);

} // CoverageDescriptionType

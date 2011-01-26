/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs11;

import net.opengis.ows11.BoundingBoxType;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Domain Subset Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Definition of the desired subset of the domain of the coverage. Contains a spatial BoundingBox and optionally a TemporalSubset. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs11.DomainSubsetType#getBoundingBoxGroup <em>Bounding Box Group</em>}</li>
 *   <li>{@link net.opengis.wcs11.DomainSubsetType#getBoundingBox <em>Bounding Box</em>}</li>
 *   <li>{@link net.opengis.wcs11.DomainSubsetType#getTemporalSubset <em>Temporal Subset</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs11.Wcs111Package#getDomainSubsetType()
 * @model extendedMetaData="name='DomainSubsetType' kind='elementOnly'"
 * @generated
 */
public interface DomainSubsetType extends EObject {
    /**
     * Returns the value of the '<em><b>Bounding Box Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Definition of desired spatial subset of a coverage domain. When the entire spatial extent of this coverage is desired, this BoundingBox can be copied from the Domain part of the Coverage Description. However, the entire spatial extent may be larger than a WCS server can output, in which case the server shall respond with an error message. Notice that WCS use of this BoundingBox is further specified in specification Subclause 7.5. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Bounding Box Group</em>' attribute list.
     * @see net.opengis.wcs11.Wcs111Package#getDomainSubsetType_BoundingBoxGroup()
     * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" required="true" many="false"
     *        extendedMetaData="kind='group' name='BoundingBox:group' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    FeatureMap getBoundingBoxGroup();

    /**
     * Returns the value of the '<em><b>Bounding Box</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Definition of desired spatial subset of a coverage domain. When the entire spatial extent of this coverage is desired, this BoundingBox can be copied from the Domain part of the Coverage Description. However, the entire spatial extent may be larger than a WCS server can output, in which case the server shall respond with an error message. Notice that WCS use of this BoundingBox is further specified in specification Subclause 7.5. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Bounding Box</em>' containment reference.
     * @see #setBoundingBox(BoundingBoxType)
     * @see net.opengis.wcs11.Wcs111Package#getDomainSubsetType_BoundingBox()
     * @model containment="true" required="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='BoundingBox' namespace='http://www.opengis.net/ows/1.1' group='http://www.opengis.net/ows/1.1#BoundingBox:group'"
     * @generated
     */
    BoundingBoxType getBoundingBox();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.DomainSubsetType#getBoundingBox <em>Bounding Box</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Bounding Box</em>' containment reference.
     * @see #getBoundingBox()
     * @generated
     */
    void setBoundingBox(BoundingBoxType value);

    /**
     * Returns the value of the '<em><b>Temporal Subset</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Optional definition of desired temporal subset of a coverage domain. If this data structure is omitted, the entire Temporal domain shall be output. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Temporal Subset</em>' containment reference.
     * @see #setTemporalSubset(TimeSequenceType)
     * @see net.opengis.wcs11.Wcs111Package#getDomainSubsetType_TemporalSubset()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='TemporalSubset' namespace='##targetNamespace'"
     * @generated
     */
    TimeSequenceType getTemporalSubset();

    /**
     * Sets the value of the '{@link net.opengis.wcs11.DomainSubsetType#getTemporalSubset <em>Temporal Subset</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Temporal Subset</em>' containment reference.
     * @see #getTemporalSubset()
     * @generated
     */
    void setTemporalSubset(TimeSequenceType value);

} // DomainSubsetType

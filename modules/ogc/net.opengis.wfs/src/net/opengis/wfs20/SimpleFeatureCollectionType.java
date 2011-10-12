/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;
import org.geotools.feature.FeatureCollection;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Simple Feature Collection Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.SimpleFeatureCollectionType#getBoundedBy <em>Bounded By</em>}</li>
 *   <li>{@link net.opengis.wfs20.SimpleFeatureCollectionType#getMember <em>Member</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getSimpleFeatureCollectionType()
 * @model extendedMetaData="name='SimpleFeatureCollectionType' kind='elementOnly'"
 * @generated
 */
public interface SimpleFeatureCollectionType extends EObject {
    /**
     * Returns the value of the '<em><b>Bounded By</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Bounded By</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Bounded By</em>' containment reference.
     * @see #setBoundedBy(EnvelopePropertyType)
     * @see net.opengis.wfs20.Wfs20Package#getSimpleFeatureCollectionType_BoundedBy()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='boundedBy' namespace='##targetNamespace'"
     * @generated
     */
    EnvelopePropertyType getBoundedBy();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.SimpleFeatureCollectionType#getBoundedBy <em>Bounded By</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Bounded By</em>' containment reference.
     * @see #getBoundedBy()
     * @generated
     */
    void setBoundedBy(EnvelopePropertyType value);

    /**
     * Returns the value of the '<em><b>Member</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wfs20.MemberPropertyType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Member</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Member</em>' containment reference list.
     * @see net.opengis.wfs20.Wfs20Package#getSimpleFeatureCollectionType_Member()
     * @model 
     */
    EList<FeatureCollection> getMember();

} // SimpleFeatureCollectionType

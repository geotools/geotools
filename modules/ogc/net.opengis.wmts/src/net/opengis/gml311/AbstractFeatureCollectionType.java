/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Feature Collection Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A feature collection contains zero or more features.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.AbstractFeatureCollectionType#getFeatureMember <em>Feature Member</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractFeatureCollectionType#getFeatureMembers <em>Feature Members</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getAbstractFeatureCollectionType()
 * @model abstract="true"
 *        extendedMetaData="name='AbstractFeatureCollectionType' kind='elementOnly'"
 * @generated
 */
public interface AbstractFeatureCollectionType extends AbstractFeatureType {
    /**
     * Returns the value of the '<em><b>Feature Member</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.FeaturePropertyType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Feature Member</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Feature Member</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getAbstractFeatureCollectionType_FeatureMember()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='featureMember' namespace='##targetNamespace'"
     * @generated
     */
    EList<FeaturePropertyType> getFeatureMember();

    /**
     * Returns the value of the '<em><b>Feature Members</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Feature Members</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Feature Members</em>' containment reference.
     * @see #setFeatureMembers(FeatureArrayPropertyType)
     * @see net.opengis.gml311.Gml311Package#getAbstractFeatureCollectionType_FeatureMembers()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='featureMembers' namespace='##targetNamespace'"
     * @generated
     */
    FeatureArrayPropertyType getFeatureMembers();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractFeatureCollectionType#getFeatureMembers <em>Feature Members</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Feature Members</em>' containment reference.
     * @see #getFeatureMembers()
     * @generated
     */
    void setFeatureMembers(FeatureArrayPropertyType value);

} // AbstractFeatureCollectionType

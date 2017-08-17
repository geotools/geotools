/**
 */
package net.opengis.gml311;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Feature Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * An abstract feature provides a set of common properties, including id, metaDataProperty, name and description inherited from AbstractGMLType, plus boundedBy.    A concrete feature type must derive from this type and specify additional  properties in an application schema. A feature must possess an identifying attribute ('id' - 'fid' has been deprecated).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.AbstractFeatureType#getBoundedBy <em>Bounded By</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractFeatureType#getLocationGroup <em>Location Group</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractFeatureType#getLocation <em>Location</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getAbstractFeatureType()
 * @model abstract="true"
 *        extendedMetaData="name='AbstractFeatureType' kind='elementOnly'"
 * @generated
 */
public interface AbstractFeatureType extends AbstractGMLType {
    /**
     * Returns the value of the '<em><b>Bounded By</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Bounded By</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Bounded By</em>' containment reference.
     * @see #setBoundedBy(BoundingShapeType)
     * @see net.opengis.gml311.Gml311Package#getAbstractFeatureType_BoundedBy()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='boundedBy' namespace='##targetNamespace'"
     * @generated
     */
    BoundingShapeType getBoundedBy();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractFeatureType#getBoundedBy <em>Bounded By</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Bounded By</em>' containment reference.
     * @see #getBoundedBy()
     * @generated
     */
    void setBoundedBy(BoundingShapeType value);

    /**
     * Returns the value of the '<em><b>Location Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * deprecated in GML version 3.1
     * <!-- end-model-doc -->
     * @return the value of the '<em>Location Group</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getAbstractFeatureType_LocationGroup()
     * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="false"
     *        extendedMetaData="kind='group' name='location:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getLocationGroup();

    /**
     * Returns the value of the '<em><b>Location</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * deprecated in GML version 3.1
     * <!-- end-model-doc -->
     * @return the value of the '<em>Location</em>' containment reference.
     * @see #setLocation(LocationPropertyType)
     * @see net.opengis.gml311.Gml311Package#getAbstractFeatureType_Location()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='location' namespace='##targetNamespace' group='location:group'"
     * @generated
     */
    LocationPropertyType getLocation();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractFeatureType#getLocation <em>Location</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Location</em>' containment reference.
     * @see #getLocation()
     * @generated
     */
    void setLocation(LocationPropertyType value);

} // AbstractFeatureType

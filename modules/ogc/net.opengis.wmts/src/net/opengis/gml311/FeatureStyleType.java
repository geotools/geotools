/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Feature Style Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * [complexType of] The style descriptor for features.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.FeatureStyleType#getFeatureConstraint <em>Feature Constraint</em>}</li>
 *   <li>{@link net.opengis.gml311.FeatureStyleType#getGeometryStyle <em>Geometry Style</em>}</li>
 *   <li>{@link net.opengis.gml311.FeatureStyleType#getTopologyStyle <em>Topology Style</em>}</li>
 *   <li>{@link net.opengis.gml311.FeatureStyleType#getLabelStyle <em>Label Style</em>}</li>
 *   <li>{@link net.opengis.gml311.FeatureStyleType#getBaseType <em>Base Type</em>}</li>
 *   <li>{@link net.opengis.gml311.FeatureStyleType#getFeatureType <em>Feature Type</em>}</li>
 *   <li>{@link net.opengis.gml311.FeatureStyleType#getQueryGrammar <em>Query Grammar</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getFeatureStyleType()
 * @model extendedMetaData="name='FeatureStyleType' kind='elementOnly'"
 * @generated
 */
public interface FeatureStyleType extends AbstractGMLType {
    /**
     * Returns the value of the '<em><b>Feature Constraint</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Feature Constraint</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Feature Constraint</em>' attribute.
     * @see #setFeatureConstraint(String)
     * @see net.opengis.gml311.Gml311Package#getFeatureStyleType_FeatureConstraint()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='featureConstraint' namespace='##targetNamespace'"
     * @generated
     */
    String getFeatureConstraint();

    /**
     * Sets the value of the '{@link net.opengis.gml311.FeatureStyleType#getFeatureConstraint <em>Feature Constraint</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Feature Constraint</em>' attribute.
     * @see #getFeatureConstraint()
     * @generated
     */
    void setFeatureConstraint(String value);

    /**
     * Returns the value of the '<em><b>Geometry Style</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.GeometryStylePropertyType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Geometry Style</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getFeatureStyleType_GeometryStyle()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='geometryStyle' namespace='##targetNamespace'"
     * @generated
     */
    EList<GeometryStylePropertyType> getGeometryStyle();

    /**
     * Returns the value of the '<em><b>Topology Style</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.TopologyStylePropertyType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Topology Style</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getFeatureStyleType_TopologyStyle()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='topologyStyle' namespace='##targetNamespace'"
     * @generated
     */
    EList<TopologyStylePropertyType> getTopologyStyle();

    /**
     * Returns the value of the '<em><b>Label Style</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Label Style</em>' containment reference.
     * @see #setLabelStyle(LabelStylePropertyType)
     * @see net.opengis.gml311.Gml311Package#getFeatureStyleType_LabelStyle()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='labelStyle' namespace='##targetNamespace'"
     * @generated
     */
    LabelStylePropertyType getLabelStyle();

    /**
     * Sets the value of the '{@link net.opengis.gml311.FeatureStyleType#getLabelStyle <em>Label Style</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Label Style</em>' containment reference.
     * @see #getLabelStyle()
     * @generated
     */
    void setLabelStyle(LabelStylePropertyType value);

    /**
     * Returns the value of the '<em><b>Base Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Base Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Base Type</em>' attribute.
     * @see #setBaseType(String)
     * @see net.opengis.gml311.Gml311Package#getFeatureStyleType_BaseType()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='baseType'"
     * @generated
     */
    String getBaseType();

    /**
     * Sets the value of the '{@link net.opengis.gml311.FeatureStyleType#getBaseType <em>Base Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Base Type</em>' attribute.
     * @see #getBaseType()
     * @generated
     */
    void setBaseType(String value);

    /**
     * Returns the value of the '<em><b>Feature Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Feature Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Feature Type</em>' attribute.
     * @see #setFeatureType(String)
     * @see net.opengis.gml311.Gml311Package#getFeatureStyleType_FeatureType()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='featureType'"
     * @generated
     */
    String getFeatureType();

    /**
     * Sets the value of the '{@link net.opengis.gml311.FeatureStyleType#getFeatureType <em>Feature Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Feature Type</em>' attribute.
     * @see #getFeatureType()
     * @generated
     */
    void setFeatureType(String value);

    /**
     * Returns the value of the '<em><b>Query Grammar</b></em>' attribute.
     * The literals are from the enumeration {@link net.opengis.gml311.QueryGrammarEnumeration}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Query Grammar</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Query Grammar</em>' attribute.
     * @see net.opengis.gml311.QueryGrammarEnumeration
     * @see #isSetQueryGrammar()
     * @see #unsetQueryGrammar()
     * @see #setQueryGrammar(QueryGrammarEnumeration)
     * @see net.opengis.gml311.Gml311Package#getFeatureStyleType_QueryGrammar()
     * @model unsettable="true"
     *        extendedMetaData="kind='attribute' name='queryGrammar'"
     * @generated
     */
    QueryGrammarEnumeration getQueryGrammar();

    /**
     * Sets the value of the '{@link net.opengis.gml311.FeatureStyleType#getQueryGrammar <em>Query Grammar</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Query Grammar</em>' attribute.
     * @see net.opengis.gml311.QueryGrammarEnumeration
     * @see #isSetQueryGrammar()
     * @see #unsetQueryGrammar()
     * @see #getQueryGrammar()
     * @generated
     */
    void setQueryGrammar(QueryGrammarEnumeration value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.FeatureStyleType#getQueryGrammar <em>Query Grammar</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetQueryGrammar()
     * @see #getQueryGrammar()
     * @see #setQueryGrammar(QueryGrammarEnumeration)
     * @generated
     */
    void unsetQueryGrammar();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.FeatureStyleType#getQueryGrammar <em>Query Grammar</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Query Grammar</em>' attribute is set.
     * @see #unsetQueryGrammar()
     * @see #getQueryGrammar()
     * @see #setQueryGrammar(QueryGrammarEnumeration)
     * @generated
     */
    boolean isSetQueryGrammar();

} // FeatureStyleType

/**
 */
package net.opengis.gml311;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dynamic Feature Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A dynamic feature may possess a history and/or a timestamp.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.DynamicFeatureType#getValidTime <em>Valid Time</em>}</li>
 *   <li>{@link net.opengis.gml311.DynamicFeatureType#getHistoryGroup <em>History Group</em>}</li>
 *   <li>{@link net.opengis.gml311.DynamicFeatureType#getHistory <em>History</em>}</li>
 *   <li>{@link net.opengis.gml311.DynamicFeatureType#getDataSource <em>Data Source</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getDynamicFeatureType()
 * @model extendedMetaData="name='DynamicFeatureType' kind='elementOnly'"
 * @generated
 */
public interface DynamicFeatureType extends AbstractFeatureType {
    /**
     * Returns the value of the '<em><b>Valid Time</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Valid Time</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Valid Time</em>' containment reference.
     * @see #setValidTime(TimePrimitivePropertyType)
     * @see net.opengis.gml311.Gml311Package#getDynamicFeatureType_ValidTime()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='validTime' namespace='##targetNamespace'"
     * @generated
     */
    TimePrimitivePropertyType getValidTime();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DynamicFeatureType#getValidTime <em>Valid Time</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Valid Time</em>' containment reference.
     * @see #getValidTime()
     * @generated
     */
    void setValidTime(TimePrimitivePropertyType value);

    /**
     * Returns the value of the '<em><b>History Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>History Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>History Group</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getDynamicFeatureType_HistoryGroup()
     * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="false"
     *        extendedMetaData="kind='group' name='history:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getHistoryGroup();

    /**
     * Returns the value of the '<em><b>History</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>History</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>History</em>' containment reference.
     * @see #setHistory(HistoryPropertyType)
     * @see net.opengis.gml311.Gml311Package#getDynamicFeatureType_History()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='history' namespace='##targetNamespace' group='history:group'"
     * @generated
     */
    HistoryPropertyType getHistory();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DynamicFeatureType#getHistory <em>History</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>History</em>' containment reference.
     * @see #getHistory()
     * @generated
     */
    void setHistory(HistoryPropertyType value);

    /**
     * Returns the value of the '<em><b>Data Source</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Data Source</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Data Source</em>' containment reference.
     * @see #setDataSource(StringOrRefType)
     * @see net.opengis.gml311.Gml311Package#getDynamicFeatureType_DataSource()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='dataSource' namespace='##targetNamespace'"
     * @generated
     */
    StringOrRefType getDataSource();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DynamicFeatureType#getDataSource <em>Data Source</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Data Source</em>' containment reference.
     * @see #getDataSource()
     * @generated
     */
    void setDataSource(StringOrRefType value);

} // DynamicFeatureType

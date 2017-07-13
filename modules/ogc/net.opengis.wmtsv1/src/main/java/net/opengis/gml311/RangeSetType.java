/**
 */
package net.opengis.gml311;

import java.util.List;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Range Set Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.RangeSetType#getValueArray <em>Value Array</em>}</li>
 *   <li>{@link net.opengis.gml311.RangeSetType#getScalarValueList <em>Scalar Value List</em>}</li>
 *   <li>{@link net.opengis.gml311.RangeSetType#getBooleanList <em>Boolean List</em>}</li>
 *   <li>{@link net.opengis.gml311.RangeSetType#getCategoryList <em>Category List</em>}</li>
 *   <li>{@link net.opengis.gml311.RangeSetType#getQuantityList <em>Quantity List</em>}</li>
 *   <li>{@link net.opengis.gml311.RangeSetType#getCountList <em>Count List</em>}</li>
 *   <li>{@link net.opengis.gml311.RangeSetType#getDataBlock <em>Data Block</em>}</li>
 *   <li>{@link net.opengis.gml311.RangeSetType#getFile <em>File</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getRangeSetType()
 * @model extendedMetaData="name='RangeSetType' kind='elementOnly'"
 * @generated
 */
public interface RangeSetType extends EObject {
    /**
     * Returns the value of the '<em><b>Value Array</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.ValueArrayType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * each member _Value holds a tuple or "row" from the equivalent table
     * <!-- end-model-doc -->
     * @return the value of the '<em>Value Array</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getRangeSetType_ValueArray()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='ValueArray' namespace='##targetNamespace'"
     * @generated
     */
    EList<ValueArrayType> getValueArray();

    /**
     * Returns the value of the '<em><b>Scalar Value List</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * each list holds the complete set of one scalar component from the values - i.e. a "column" from the equivalent table
     * <!-- end-model-doc -->
     * @return the value of the '<em>Scalar Value List</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getRangeSetType_ScalarValueList()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='ScalarValueList:1'"
     * @generated
     */
    FeatureMap getScalarValueList();

    /**
     * Returns the value of the '<em><b>Boolean List</b></em>' attribute list.
     * The list contents are of type {@link java.util.List}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * XML List based on XML Schema boolean type.  An element of this type contains a space-separated list of boolean values {0,1,true,false}
     * <!-- end-model-doc -->
     * @return the value of the '<em>Boolean List</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getRangeSetType_BooleanList()
     * @model unique="false" dataType="net.opengis.gml311.BooleanOrNullList" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='BooleanList' namespace='##targetNamespace' group='#ScalarValueList:1'"
     * @generated
     */
    EList<List> getBooleanList();

    /**
     * Returns the value of the '<em><b>Category List</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.CodeOrNullListType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A space-separated list of terms or nulls.  A single XML attribute codeSpace may be provided, which authorises all the terms in the list.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Category List</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getRangeSetType_CategoryList()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CategoryList' namespace='##targetNamespace' group='#ScalarValueList:1'"
     * @generated
     */
    EList<CodeOrNullListType> getCategoryList();

    /**
     * Returns the value of the '<em><b>Quantity List</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.MeasureOrNullListType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A space separated list of amounts or nulls.  The amounts use the XML Schema type double.  A single XML attribute uom (unit of measure) is required, whose value is a URI which identifies the definition of the scale or units by which all the amounts in the list must be multiplied.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Quantity List</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getRangeSetType_QuantityList()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='QuantityList' namespace='##targetNamespace' group='#ScalarValueList:1'"
     * @generated
     */
    EList<MeasureOrNullListType> getQuantityList();

    /**
     * Returns the value of the '<em><b>Count List</b></em>' attribute list.
     * The list contents are of type {@link java.util.List}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A space-separated list of integers or nulls.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Count List</em>' attribute list.
     * @see net.opengis.gml311.Gml311Package#getRangeSetType_CountList()
     * @model unique="false" dataType="net.opengis.gml311.IntegerOrNullList" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='CountList' namespace='##targetNamespace' group='#ScalarValueList:1'"
     * @generated
     */
    EList<List> getCountList();

    /**
     * Returns the value of the '<em><b>Data Block</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Its tuple list holds the values as space-separated tuples each of which contains comma-separated components, and the tuple structure is specified using the rangeParameters property.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Data Block</em>' containment reference.
     * @see #setDataBlock(DataBlockType)
     * @see net.opengis.gml311.Gml311Package#getRangeSetType_DataBlock()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='DataBlock' namespace='##targetNamespace'"
     * @generated
     */
    DataBlockType getDataBlock();

    /**
     * Sets the value of the '{@link net.opengis.gml311.RangeSetType#getDataBlock <em>Data Block</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Data Block</em>' containment reference.
     * @see #getDataBlock()
     * @generated
     */
    void setDataBlock(DataBlockType value);

    /**
     * Returns the value of the '<em><b>File</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * a reference to an external source for the data, together with a description of how that external source is structured
     * <!-- end-model-doc -->
     * @return the value of the '<em>File</em>' containment reference.
     * @see #setFile(FileType)
     * @see net.opengis.gml311.Gml311Package#getRangeSetType_File()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='File' namespace='##targetNamespace'"
     * @generated
     */
    FileType getFile();

    /**
     * Sets the value of the '{@link net.opengis.gml311.RangeSetType#getFile <em>File</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>File</em>' containment reference.
     * @see #getFile()
     * @generated
     */
    void setFile(FileType value);

} // RangeSetType

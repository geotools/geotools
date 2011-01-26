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
 * A representation of the model object '<em><b>Range Set Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Defines the properties (categories, measures, or values) assigned to each location in the domain. Any such property may be a scalar (numeric or text) value, such as population density, or a compound (vector or tensor) value, such as incomes by race, or radiances by wavelength. The semantic of the range set is typically an observable and is referenced by a URI. A rangeSet also has a reference system that is reffered by the URI in the refSys attribute. The refSys is either qualitative (classification) or quantitative (uom). The three attributes can be included either here and in each axisDescription. If included in both places, the values in the axisDescription over-ride those included in the RangeSet.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.RangeSetType#getAxisDescription <em>Axis Description</em>}</li>
 *   <li>{@link net.opengis.wcs10.RangeSetType#getNullValues <em>Null Values</em>}</li>
 *   <li>{@link net.opengis.wcs10.RangeSetType#getRefSys <em>Ref Sys</em>}</li>
 *   <li>{@link net.opengis.wcs10.RangeSetType#getRefSysLabel <em>Ref Sys Label</em>}</li>
 *   <li>{@link net.opengis.wcs10.RangeSetType#getSemantic <em>Semantic</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getRangeSetType()
 * @model extendedMetaData="name='RangeSetType' kind='elementOnly'"
 * @generated
 */
public interface RangeSetType extends AbstractDescriptionType {
    /**
	 * Returns the value of the '<em><b>Axis Description</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.wcs10.AxisDescriptionType1}.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Defines a range provided by a coverage. Multiple occurences are used for compound observations, to descibe an additional parameter (that is, an independent variable besides space and time), plus the valid values of this parameter (which GetCoverage requests can use to select subsets of a coverage offering).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Axis Description</em>' containment reference list.
	 * @see net.opengis.wcs10.Wcs10Package#getRangeSetType_AxisDescription()
	 * @model type="net.opengis.wcs10.AxisDescriptionType1" containment="true"
	 *        extendedMetaData="kind='element' name='axisDescription' namespace='##targetNamespace'"
	 * @generated
	 */
    EList getAxisDescription();

    /**
	 * Returns the value of the '<em><b>Null Values</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Values used when valid values are not available. (The coverage encoding may specify a fixed value for null (e.g. าะ99999ำ or าN/Aำ), but often the choice is up to the provider and must be communicated to the client outside of the coverage itself.)
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Null Values</em>' containment reference.
	 * @see #setNullValues(ValueEnumType)
	 * @see net.opengis.wcs10.Wcs10Package#getRangeSetType_NullValues()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='nullValues' namespace='##targetNamespace'"
	 * @generated
	 */
    ValueEnumType getNullValues();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.RangeSetType#getNullValues <em>Null Values</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Null Values</em>' containment reference.
	 * @see #getNullValues()
	 * @generated
	 */
    void setNullValues(ValueEnumType value);

    /**
	 * Returns the value of the '<em><b>Ref Sys</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Pointer to the reference system in which values are expressed. This attribute shall be included either here or in each AxisDescriptionType.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Ref Sys</em>' attribute.
	 * @see #setRefSys(String)
	 * @see net.opengis.wcs10.Wcs10Package#getRangeSetType_RefSys()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='refSys'"
	 * @generated
	 */
    String getRefSys();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.RangeSetType#getRefSys <em>Ref Sys</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ref Sys</em>' attribute.
	 * @see #getRefSys()
	 * @generated
	 */
    void setRefSys(String value);

    /**
	 * Returns the value of the '<em><b>Ref Sys Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Short human-readable label denoting the reference system, for human interface display. This attribute shall be included either here or in each AxisDescriptionType.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Ref Sys Label</em>' attribute.
	 * @see #setRefSysLabel(String)
	 * @see net.opengis.wcs10.Wcs10Package#getRangeSetType_RefSysLabel()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='refSysLabel'"
	 * @generated
	 */
    String getRefSysLabel();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.RangeSetType#getRefSysLabel <em>Ref Sys Label</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ref Sys Label</em>' attribute.
	 * @see #getRefSysLabel()
	 * @generated
	 */
    void setRefSysLabel(String value);

    /**
	 * Returns the value of the '<em><b>Semantic</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Definition of the semantics or meaning of the values in the XML element it belongs to. The value of this "semantic" attribute can be a RDF Property or Class of a taxonomy or ontology.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Semantic</em>' attribute.
	 * @see #setSemantic(String)
	 * @see net.opengis.wcs10.Wcs10Package#getRangeSetType_Semantic()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='semantic' namespace='##targetNamespace'"
	 * @generated
	 */
    String getSemantic();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.RangeSetType#getSemantic <em>Semantic</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Semantic</em>' attribute.
	 * @see #getSemantic()
	 * @generated
	 */
    void setSemantic(String value);

} // RangeSetType

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.geotools.data.efeature;

import com.vividsolutions.jts.geom.Geometry;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.geotools.data.Transaction;
import org.geotools.referencing.CRS;

import org.opengis.feature.Feature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>EFeature</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.geotools.data.efeature.EFeature#getID <em>ID</em>}</li>
 *   <li>{@link org.geotools.data.efeature.EFeature#getData <em>Data</em>}</li>
 *   <li>{@link org.geotools.data.efeature.EFeature#getSRID <em>SRID</em>}</li>
 *   <li>{@link org.geotools.data.efeature.EFeature#getDefault <em>Default</em>}</li>
 *   <li>{@link org.geotools.data.efeature.EFeature#getStructure <em>Structure</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.geotools.data.efeature.EFeaturePackage#getEFeature()
 * @model abstract="true"
 * @generated
 *
 * @source $URL$
 */
public interface EFeature extends EObject {
    /**
     * Returns the value of the '<em><b>ID</b></em>' attribute.
     * The default value is <code>""</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>ID</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>ID</em>' attribute.
     * @see #setID(String)
     * @see org.geotools.data.efeature.EFeaturePackage#getEFeature_ID()
     * @model default="" id="true" required="true" volatile="true"
     * @generated
     */
    String getID();

    /**
     * Sets the value of the '{@link org.geotools.data.efeature.EFeature#getID <em>ID</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>ID</em>' attribute.
     * @see #getID()
     * @generated
     */
    void setID(String value);

    /**
     * Returns the value of the '<em><b>SRID</b></em>' attribute.
     * The default value is <code>"EPSG:4326"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * The spatial reference ID (SRID) is an Coordinate Reference System authority code. The code
     * has the following format:
     * 
     * <pre>
     * code 		= &lt;AUTHORITY&gt;:&lt;ID&gt;
     * AUTHORITY	= The CRS Authority. Is used to determine applicable CRS factories.
     * ID		= The CRS ID. Is passed to the CRS factory chosen by the {@link CRS CRS utility class}.
     * 	
     * f.ex: "EPSG:4326"
     * </pre>
     * 
     * <strong>NOTE</strong>: This is a convenience method for calling
     * <p>
     * <code>
     * getStructure().setSRID(srid).	
     * </code>
     * </p>
     * Hence, changing the SRID affects ALL {@link EFeature}s with the same {@link #getStructure()
     * structure}. All {@link Feature#getBounds() bounds} of all {@link #getData() feature}s is
     * invalidated when the SRID is changed, forcing a bound calculation the first time
     * {@link Feature#getBounds()} is called on every feature with same structure as this. </p>
     * 
     * @see {@link CRS#decode(String)} - Decodes SRID into {@link CoordinateReferenceSystem}
     *      instance
     * @see {@link CRS#decode(String, boolean)} - Decodes SRID to {@link CoordinateReferenceSystem}
     *      instance where axis order is forced to (longitude, latitude). <!-- end-user-doc -->
     * @return the value of the '<em>SRID</em>' attribute.
     * @see #setSRID(String)
     * @see org.geotools.data.efeature.EFeaturePackage#getEFeature_SRID()
     * @model default="EPSG:4326" required="true" volatile="true"
     * @generated
     */
    String getSRID();

    /**
     * Sets the value of the '{@link org.geotools.data.efeature.EFeature#getSRID <em>SRID</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>SRID</em>' attribute.
     * @see #getSRID()
     * @generated
     */
    void setSRID(String value);

    /**
     * Returns the value of the '<em><b>Data</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Data</em>' attribute isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Data</em>' attribute.
     * @see #setData(Feature)
     * @see org.geotools.data.efeature.EFeaturePackage#getEFeature_Data()
     * @model dataType="org.geotools.data.efeature.Feature" required="true" transient="true" derived="true"
     * @generated
     */
    Feature getData();

    /**
     * Sets the value of the '{@link org.geotools.data.efeature.EFeature#getData <em>Data</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Data</em>' attribute.
     * @see #getData()
     * @generated
     */
    void setData(Feature value);

    /**
     * Returns the value of the '<em><b>Default</b></em>' attribute.
     * The default value is <code>"geom"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Default</em>' attribute isn't clear, there really should be more
     * of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Default</em>' attribute.
     * @see #setDefault(String)
     * @see org.geotools.data.efeature.EFeaturePackage#getEFeature_Default()
     * @model default="geom" required="true" volatile="true"
     *        annotation="http://www.eclipse.org/emf/2002/GenModel Doumentation='Name of default EFeatureGeometry'"
     * @generated
     */
    String getDefault();

    /**
     * Sets the value of the '{@link org.geotools.data.efeature.EFeature#getDefault <em>Default</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @param value the new value of the '<em>Default</em>' attribute.
     * @see #getDefault()
     * @generated
     */
    void setDefault(String value);

    /**
     * Returns the value of the '<em><b>Structure</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Structure</em>' attribute isn't clear, there really should be more
     * of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Structure</em>' attribute.
     * @see #setStructure(EFeatureInfo)
     * @see org.geotools.data.efeature.EFeaturePackage#getEFeature_Structure()
     * @model dataType="org.geotools.data.efeature.EFeatureInfo" required="true" transient="true" volatile="true" derived="true"
     * @generated
     */
    EFeatureInfo getStructure();

    /**
     * Sets the value of the '{@link org.geotools.data.efeature.EFeature#getStructure <em>Structure</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Structure</em>' attribute.
     * @see #getStructure()
     * @generated
     */
    void setStructure(EFeatureInfo value);

    /**
     * <!-- begin-user-doc --> Get all {@link EFeatureAttribute attributes} not containing
     * {@link Geometry geometry data}.
     * <p>
     * 
     * @param valueType - attribute {@link EFeatureAttribute#getValueType() value type} filter.
     *        Passing {@link Object} will return all {@link EFeatureAttribute attributes}. <!--
     *        end-user-doc -->
     * @model dataType="org.geotools.data.efeature.List<org.geotools.data.efeature.EFeatureAttribute<V>>" required="true" many="false"
     * @generated
     */
    <V> List<EFeatureAttribute<V>> getAttributeList(Class<V> valueType);

    /**
     * <!-- begin-user-doc --> Get all {@link EFeatureGeometry geometry attributes}.
     * <p>
     * 
     * @param valueType - geometry {@link EFeatureGeometry#getValueType() value type} filter.
     *        Passing {@link Geometry} will return all {@link EFeatureGeometry geometry attributes}.
     *        <!-- end-user-doc -->
     * @model dataType="org.geotools.data.efeature.List<org.geotools.data.efeature.EFeatureGeometry<V>>" required="true" many="false" VBounds="org.geotools.data.efeature.Geometry"
     * @generated
     */
    <V extends Geometry> List<EFeatureGeometry<V>> getGeometryList(Class<V> valueType);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model dataType="org.geotools.data.efeature.Feature" required="true" transactionDataType="org.geotools.data.efeature.Transaction" transactionRequired="true"
     * @generated
     */
    Feature getData(Transaction transaction);

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @model dataType="org.geotools.data.efeature.Feature" required="true" newDataDataType="org.geotools.data.efeature.Feature" newDataRequired="true" transactionDataType="org.geotools.data.efeature.Transaction" transactionRequired="true"
     * @generated
     */
    Feature setData(Feature newData, Transaction transaction);

} // EFeature

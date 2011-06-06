/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.geotools.data.efeature;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.opengis.feature.Property;

/**
 * Generic interface for accessing {@link Property feature property} data.
 * 
 * <p>
 * The following members are supported:
 * <ul>
 * <li>{@link EFeatureProperty#getValue <em>Value</em>}</li>
 * <li>{@link EFeatureProperty#getValueType <em>Value Type</em>}</li>
 * <li>{@link EFeatureProperty#getData <em>Data</em>}</li>
 * <li>{@link EFeatureProperty#getDataType <em>Data Type</em>}</li>
 * </ul>
 * </p>
 * 
 * @param <V> - Actual {@link Property#getValue() property value} class.
 * @param <T> - Actual {@link Property property} class.
 * 
 * @see EFeaturePackage#getEFeatureProperty()
 * 
 * @author kengu
 */
public interface EFeatureProperty<V, T extends Property> {

    /**
     * Get the {@link Property feature property} instance wrapped by this class instance.
     * 
     * @return the value of the '<em>Data</em>' attribute.
     * @see #setData(Property)
     */
    public T getData();

    /**
     * Set {@link Property feature property} instance wrapped by this class. </p>
     * 
     * @param value - new value of the '<em>Data</em>' attribute.
     * @throws NullPointerException If value is <code>null</code>.
     * @see #getData()
     */
    public void setData(T value);

    /**
     * Get the actual {@link Property feature property} class </p>
     * 
     * @return the value of the '<em>Data Type</em>' attribute.
     */
    public Class<T> getDataType();

    /**
     * Get the {@link Property#getValue() feature property value}.
     * 
     * @return the value of the '<em>Value</em>' attribute.
     * @see #setValue(Object)
     */
    public V getValue();

    /**
     * Set {@link Property feature property} instance wrapped by this class. </p>
     * 
     * @param value - new value of the '<em>Value</em>' attribute.
     * @throws NullPointerException If value is <code>null</code>.
     * @see #getValue()
     */
    public void setValue(V value);

    /**
     * Get the actual {@link Property#getValue() feature property value} class </p>
     * 
     * @return the value of the '<em>Value Type</em>' attribute.
     */
    public Class<V> getValueType();

    /**
     * Get the property {@link EStructureInfo structure} instance.
     * 
     * @return the value of the '<em>Structure</em>' attribute.
     */
    public EStructureInfo<?> getStructure();

    /**
     * Get the {@link EObject} with the {@link EStructuralFeature structural feature} containing the
     * {@link #getData() feature property}
     * 
     * @return the value of the '<em>Container</em>' attribute.
     */
    public EObject getContainer();

    /**
     * Get the {@link EStructuralFeature structural feature} containing the {@link #getData()
     * feature property}
     * 
     * @return the value of the '<em>StructuralFeature</em>' attribute.
     */
    public EStructuralFeature getStructuralFeature();

} // EFeatureProperty

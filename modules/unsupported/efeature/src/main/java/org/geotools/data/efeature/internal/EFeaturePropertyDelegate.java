/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.geotools.data.efeature.internal;

import java.lang.ref.WeakReference;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.geotools.data.efeature.EFeatureProperty;
import org.geotools.data.efeature.EStructureInfo;
import org.opengis.feature.Property;

/**
 * An abstract implementation of the model data type {@link EFeatureProperty}.
 * 
 * @author kengu, 22. apr. 2011
 */
public abstract class EFeaturePropertyDelegate<V, T extends Property, S extends EStructuralFeature>
        implements EFeatureProperty<V, T> {

    /**
     * Cached {@link EObject} instance containing the {@link Property#getValue() property value}.
     * <p>
     * The {@link #eContainer delegate} is stored as a {@link WeakReference weak reference} so it is
     * not prevented from being finalized and garbage collected. This ensure that no memory leakage
     * occur each time a delegate is removed from the implementing EMF model.
     */
    protected WeakReference<EObject> eContainer;

    /**
     * Cached {@link Property property data} instance
     */
    protected T data;

    /**
     * The actual data class.
     */
    protected Class<T> dataType;

    /**
     * The actual value class.
     */
    protected Class<V> valueType;

    /**
     * Cached {@link EStructureInfo property structure}.
     */
    protected EStructureInfo<?> eStructure;

    /**
     * The id of the {@link EStructuralFeature structural feature} of the {@link #eContainer
     * delegate} which contains the {@link #getData() property data}
     */
    protected WeakReference<S> eStructuralFeature;

    /**
     * Default constructor.
     * <p>
     * 
     * @param eContainer - {@link EObject} instance owning the {@link #getStructuralFeature()
     *        feature property}.
     * @param eStructuralFeature - {@link EStructuralFeature} containing the feature
     *        {@link #getData() property data}.
     * @param valueType - {@link #getData() data} type.
     * @param valueType - {@link #getValue() value} type.
     */
    protected EFeaturePropertyDelegate(EObject eContainer, S eStructuralFeature, Class<T> dataType,
            Class<V> valueType, EStructureInfo<?> eStructure) {
        super();
        this.dataType = dataType;
        this.valueType = valueType;
        this.eContainer = new WeakReference<EObject>(eContainer);
        this.eStructuralFeature = new WeakReference<S>(eStructuralFeature);
    }

    /**
     * Check if delegate is disposed.
     * <p>
     * 
     * @return <code>true</code> if delegate is disposed.
     */
    public final boolean isDisposed() {
        return eContainer.get() == null || eStructuralFeature.get() == null;
    }

    /**
     * @throws IllegalStateException If delegate is {@link #isDisposed() disposed}.
     */
    public final T getData() {
        if (isDisposed()) {
            throw new NullPointerException("Data can not be set to null");
        }

        if (data == null) {
            data = create();
        }
        return data;
    }

    /**
     * @throws IllegalStateException If delegate is {@link #isDisposed() disposed}.
     */
    public final void setData(T newData) {
        // Sanity checks
        //
        if (newData == null) {
            throw new NullPointerException("Data can not be set to null");
        } else if (isDisposed()) {
            throw new NullPointerException("Delegate is disposed");
        }

        // Validate data and return new value
        //
        V newValue = validate(newData);

        //
        // Set new value
        //
        eContainer.get().eSet(eStructuralFeature.get(), newValue);

    }

    public final Class<T> getDataType() {
        return dataType;
    }

    public final Class<V> getValueType() {
        return valueType;
    }

    /**
     * @throws IllegalStateException If delegate is {@link #isDisposed() disposed}.
     */
    public final V getValue() {
        return valueType.cast(eContainer().eGet(eStructuralFeature()));
    }

    /**
     * @throws IllegalStateException If delegate is {@link #isDisposed() disposed}.
     * @throws NullPointerException If new value is <code>null</code>.
     */
    public final void setValue(V newValue) {
        if (newValue == null && !getData().isNillable()) {
            throw new NullPointerException("Value can not be set to null");
        }
        V value = valueType.cast(newValue);
        eContainer().eSet(eStructuralFeature(), value);
    }

    /**
     * @throws IllegalStateException If delegate is {@link #isDisposed() disposed}.
     */
    public final EObject getContainer() {
        return eContainer();
    }

    /**
     * @throws IllegalStateException If delegate is {@link #isDisposed() disposed}.
     */
    public final EStructuralFeature getStructuralFeature() {
        return eStructuralFeature();
    }

    public EStructureInfo<?> getStructure() {
        return eStructure;
    }

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (valueType: ");
        result.append(valueType);
        result.append(", dataType: ");
        result.append(dataType);
        result.append(')');
        return result.toString();
    }

    // ----------------------------------------------------- 
    //  Abstract EFeaturePropertyDelegate methods 
    // -----------------------------------------------------

    /**
     * Create new {@link Property property} instance.
     */
    protected abstract T create();

    /**
     * Validate {@link Property property data} instance.
     * 
     * @throws IllegalArgumentException If data is not valid.
     * @return property value casted to {@link #getValueType() value type} if valid.
     */
    protected abstract V validate(T data) throws IllegalArgumentException;

    /**
     * Validate {@link Property#getValue() property value}.
     * 
     * @throws IllegalArgumentException If value is not valid.
     * @return property value casted to {@link #getValueType() value type} if valid.
     */
    protected abstract V validate(Object value) throws IllegalArgumentException;

    // ----------------------------------------------------- 
    //  EFeaturePropertyDelegate helper methods
    // -----------------------------------------------------


    /**
     * @throws IllegalStateException If delegate is {@link #isDisposed() disposed}.
     */
    protected final EObject eContainer() {
        if (isDisposed()) {
            throw new IllegalStateException("Delegate is disposed");
        }
        return eContainer.get();
    }

    /**
     * @throws IllegalStateException If delegate is {@link #isDisposed() disposed}.
     */
    protected final EStructuralFeature eStructuralFeature() {
        if (isDisposed()) {
            throw new IllegalStateException("Delegate is disposed");
        }
        return eStructuralFeature.get();
    }

} // EFeaturePropertyDelegate

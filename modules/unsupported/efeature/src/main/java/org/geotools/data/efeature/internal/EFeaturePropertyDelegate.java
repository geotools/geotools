/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.geotools.data.efeature.internal;

import java.lang.ref.WeakReference;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.geotools.data.Transaction;
import org.geotools.data.efeature.EFeatureAttributeInfo;
import org.geotools.data.efeature.EFeatureProperty;
import org.geotools.data.efeature.EStructureInfo;
import org.opengis.feature.Property;

/**
 * An abstract implementation of the model data type {@link EFeatureProperty}.
 * 
 * @author kengu, 22. apr. 2011
 *
 * @source $URL$
 */
public abstract class EFeaturePropertyDelegate<V, T extends Property, S extends EStructuralFeature>
        implements EFeatureProperty<V, T> {
    
    /**
     * Cached {@link EFeatureInternal} instance containing the {@link Property#getValue() property value}.
     * <p>
     * The {@link #eObject delegate} is stored as a {@link WeakReference weak reference} so it is
     * not prevented from being finalized and garbage collected.
     */
    protected WeakReference<EFeatureInternal> eInternal;
    
    /**
     * Cached {@link Property property data} instance
     */
    protected T eData;

    /**
     * The actual data class.
     */
    protected Class<T> eDataType;

    /**
     * The actual value class.
     */
    protected Class<V> eValueType;
    
    /**
     * Cached {@link EFeatureAttributeInfo}
     */
    protected EFeatureAttributeInfo eStructure;

    /**
     * The id of the {@link EStructuralFeature structural feature} 
     * of the {@link #eObject implementation} which contains 
     * the {@link #getData() property data}
     */
    protected WeakReference<EStructuralFeature> eStructuralFeature;
    
    /**
     * Cached value when {@link #isDetached() detached} 
     */
    protected V eValue;
    
    /**
     * Flag indication that detached value should be updated
     */
    protected Boolean eInitDetached = true;
    
    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------

    /**
     * Default constructor.
     * <p>
     * @param eInternal
     * @param eStructuralFeature
     * @param dataType - {@link #getData() data} type.
     * @param valueType - {@link #getValue() value} type.
     */
    protected EFeaturePropertyDelegate(EFeatureInternal eInternal, 
            String eName, Class<T> dataType, Class<V> valueType) {
        //
        // Forward
        //
        super();        
        //
        // Get EFeatureAttribute structure
        //
        this.eStructure = eInternal.eStructure.eGetAttributeInfo(eName, true);
        //
        // EAttribute not found?
        //
        if (this.eStructure == null) {
            throw new IllegalArgumentException("EStructuralFeature '" + eName + "'" + " not found");
        }
        //
        // Do value type sanity check 
        //
        EAttribute eAttribute = eStructure.eAttribute();
        Class<?> actualType = eAttribute.getEAttributeType().getInstanceClass();
        if (!valueType.isAssignableFrom(actualType)) {
            //
            // Not correct type
            //
            throw new IllegalArgumentException("Value type '" + 
                    valueType.getName() + "'" + " mismatch");
        }
        //
        // Construct delegate
        //            
        this.eDataType = dataType;
        this.eValueType = valueType;
        this.eInternal = new WeakReference<EFeatureInternal>(eInternal);
        this.eStructuralFeature = new WeakReference<EStructuralFeature>(eAttribute);
        
    }

    // ----------------------------------------------------- 
    //  EFeatureProperty implementation
    // -----------------------------------------------------

    @Override
    public String getName() {
        return getStructuralFeature().getName();
    }
        
    /**
     * Check if EFeaturePropertyDelegate is disposed.
     * <p>
     * 
     * @return <code>true</code> if EFeaturePropertyDelegate is disposed.
     */
    public final boolean isDisposed() {
        return eInternal.get() == null 
            || eStructuralFeature.get() == null;
    }

    /**
     * @throws IllegalStateException If delegate is {@link #isDisposed() disposed}.
     */
    @Override
    public final T getData() {
        if (isDisposed()) {
            throw new NullPointerException("Data can not be set to null");
        }

        if (eData == null) {
            eData = create();
        }
        return eData;
    }

    /**
     * @throws IllegalStateException If delegate is {@link #isDisposed() disposed}.
     */
    @Override
    public final void setData(T newData) {
        // Sanity checks
        //
        if (newData == null) {
            throw new NullPointerException("Data can not be set to null");
        } else if (isDisposed()) {
            throw new NullPointerException("EFeaturePropertyDelegate is disposed");
        }

        // Validate data and return new value
        //
        V newValue = validate(newData);

        //
        // Set new value
        //
        eObject().eSet(eStructuralFeature.get(), newValue);
    }

    @Override
    public final Class<T> getDataType() {
        return eDataType;
    }

    @Override
    public final Class<V> getValueType() {
        return eValueType;
    }
    
    @Override
    public boolean isDetached() {
        return eInternal().eHints.eValuesDetached();
    }

    /**
     * @throws IllegalStateException If delegate is {@link #isDisposed() disposed}.
     */
    @Override
    public final V getValue() {
        //
        // Detached?
        //
        if(isDetached()) {
            //
            // Initialize detached value?
            //
            if(eInitDetached) {
                return read();
            } 
            //
            // Finished
            //    
            return eValue;
            
        } else {
            //
            // Release value
            //
            eValue = null;
            //
            // Set trigger for detached initialization
            //
            eInitDetached = true;
            //
            // Value is attached, read it from structure
            //
            return eValueType.cast(eObject().eGet(eStructuralFeature()));
        }
    }

    /**
     * @throws IllegalStateException If delegate is {@link #isDisposed() disposed}.
     * @throws NullPointerException If new value is <code>null</code>.
     */
    @Override
    public final void setValue(V newValue) {
        if (newValue == null && !getData().isNillable()) {
            throw new NullPointerException("Value can not be set to null");
        }
        V value = eValueType.cast(newValue);
        if(isDetached()) {
            eValue = newValue;
        } else {
            eObject().eSet(eStructuralFeature(), value);
        }
    }
    
    @Override
    public V read() throws IllegalStateException {
        return read(eInternal().eTx);
    }

    @Override
    public V read(Transaction transaction) throws IllegalStateException {
        //
        // Check if detached 
        //
        if(isDetached()) {
            //
            // TODO Implement read lock check
            //
            //
            // Read value from object only when detached
            //
            eValue = eValueType.cast(eObject().eGet(eStructuralFeature()));
            //
            // Flag that detached initialization is completed
            //
            eInitDetached = false;
        }
        //
        // Finished
        //
        return getValue();
    }

    @Override
    public V write() throws IllegalStateException {
        return write(eInternal().eTx);
    }

    @Override
    public V write(Transaction transaction) throws IllegalStateException {            
        //
        // Decide if value is allowed to be updated from backing store
        //
        if(!isDetached()) {
            throw new IllegalStateException("EFeatureProperty " 
                    + getName() + " is not detached");
        }
        //
        // TODO Implement write lock check
        //
        eObject().eSet(eStructuralFeature(), eValue);
        //
        // Finished
        //
        return eValue;
    }

    /**
     * @throws IllegalStateException 
     *  If {@link EFeatureInternal internal implementation} 
     *  is {@link #isDisposed() disposed}.
     */
    @Override
    public final EObject eObject() {        
        return eInternal().eImpl();
    }

    /**
     * @throws IllegalStateException If delegate is {@link #isDisposed() disposed}.
     */
    @Override
    public final EStructuralFeature getStructuralFeature() {
        return eStructuralFeature();
    }

    @Override
    public EStructureInfo<?> getStructure() {
        return eStructure;
    }

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (valueType: ");
        result.append(eValueType);
        result.append(", dataType: ");
        result.append(eDataType);
        result.append(')');
        return result.toString();
    }
    
    // ----------------------------------------------------- 
    //  EFeaturePropertyDelegate methods 
    // -----------------------------------------------------
        
    /**
     * @throws IllegalStateException 
     *  If {@link EFeatureInternal internal implementation} 
     *  is {@link #isDisposed() disposed}.
     */
    public final EFeatureInternal eInternal() {
        if (isDisposed()) {
            throw new IllegalStateException("EFeatureInternal instance is disposed");
        }
        return eInternal.get();
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

    protected final void eSetInitDetachedValues() {
        eInitDetached=true;
    }
    
    /**
     * @throws IllegalStateException If delegate is {@link #isDisposed() disposed}.
     */
    protected final EStructuralFeature eStructuralFeature() {
        if (isDisposed()) {
            throw new IllegalStateException("EFeaturePropertyDelegate is disposed");
        }
        return eStructuralFeature.get();
    }
    

} // EFeaturePropertyDelegate

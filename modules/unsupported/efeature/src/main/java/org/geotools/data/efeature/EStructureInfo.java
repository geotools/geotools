package org.geotools.data.efeature;

import java.lang.ref.WeakReference;
import java.util.logging.Logger;

import org.geotools.util.WeakHashSet;
import org.geotools.util.logging.Logging;

/**
 * 
 * @author kengu
 *
 */
public abstract class EStructureInfo<T extends EStructureInfo<?>> {
    
    /** The logger for the {@link EStructureInfo} class */
    protected static final Logger LOGGER = Logging.getLogger(EStructureInfo.class);
    
    protected boolean isValid = false;

    protected boolean isDisposed = false;

    protected boolean isAvailable = true;
    
    /**
     * Set of weakly referenced {@link EStructureInfo} listeners
     */
    @SuppressWarnings("rawtypes")
    protected WeakHashSet<EFeatureListener> 
        eListeners = new WeakHashSet<EFeatureListener>(
            EFeatureListener.class);

    /**
     * Cached {@link EFeatureStatus#SUCCESS success status}.
     */
    protected final EFeatureStatus SUCCESS = EFeatureUtils.newStatus(this, EFeatureStatus.SUCCESS,
            null, null);

    /** Cached hints */
    protected EFeatureHints eHints;
    
    /** Cached {@link EFeatureContext} id */
    protected String eContextID;
    
    /** Weak reference to {@link EFeatureContextFactory} instance */
    protected WeakReference<EFeatureContextFactory> eFactory;

    /** Weak reference to {@link EFeatureContext} instance */
    protected WeakReference<EFeatureContext> eContext;
    
    // ----------------------------------------------------- 
    //  Constructors
    // -----------------------------------------------------

    /**
     * Default constructor
     */
    protected EStructureInfo() { /*NOP*/ }
    
    /**
     * Structure copy constructor.
     * <p>
     * This method copies the structure into given context. 
     * </p>
     * <b>NOTE</b>: This method only adds a one-way reference from 
     * copied instance to given {@link EFeatureContext context}. 
     * </p>  
     * @param eStructureInfo - copy from this {@link EStructureInfo} instance
     * @param eContextInfo - copy into context of this structure 
     */
    protected EStructureInfo(EStructureInfo<T> eStructureInfo, EStructureInfo<?> eContextInfo) {        
        //
        // --------------------------------------------------
        //  Copy states
        // --------------------------------------------------
        //  NOTE: State 'isDisposed' is not copied to allow  
        //        recreation. 
        // --------------------------------------------------
        //
        this.isValid = eStructureInfo.isValid;
        this.isAvailable = eStructureInfo.isAvailable;
        //
        // Copy creation hints
        //
        this.eHints = eStructureInfo.eHints;
        //
        // Copy context
        //
        this.eContext = eContextInfo.eContext;
        this.eFactory = eContextInfo.eFactory;
        this.eContextID = eContextInfo.eContextID;        
    }
    // ----------------------------------------------------- 
    //  EStructureInfo methods
    // -----------------------------------------------------
    
    /**
     * Get {@link EFeatureContext#eContextID() context ID}.
     * 
     * @return a {@link EFeatureContext#eContextID() context ID}.
     */
    public final String eContextID() {
        return eContextID;
    }
    
    /**
     * Get {@link EFeatureContext context} which this structure belongs.
     * 
     * @throws IllegalStateException If {@link #isValid() invalid}, 
     * {@link #isDisposed() disposed} or not found.
     */
    public final EFeatureContext eContext()
    {
        return eContext(true);
    }
    
    /**
     * Check if this is a <i>prototype</i>.
     * <p>
     * A prototype have an {@link EFeatureContextInfo} as it's
     * {@link EStructureInfo#eParentInfo() parent structure}.
     */
    public final boolean isPrototype() {
        //
        // If context is an prototype, all structures are prototypes,
        // else, only EFeatureInfo structure can be prototypes when
        // the parent structure is the context structure.
        //
        return eContext(true).isPrototype() || 
            (this instanceof EFeatureInfo) &&
            (eParentInfo(true) instanceof EFeatureContextInfo);
    }
    
    /**
     * Get {@link EFeatureContextFactory} instance.
     * @throws IllegalStateException If {@link #isValid() invalid}, 
     * {@link #isDisposed() disposed} or not found.
     */
    public final EFeatureContextFactory eFactory()
    {
        return eFactory(true); 
    }
    
    /**
     * Get parent structure.
     */        
    public final T eParentInfo() {
        return eParentInfo(true);
    }
    
    /**
     * Check if {@link EStructureInfo structure} is available.
     * <p>
     * A structure is only available if it is valid, not disposed 
     * and explicitly made available.
     * </p>
     * @return <code>true</code> if available.
     * 
     * @see {@link #setAvailable(boolean)}
     */
    public boolean isAvailable() {
        return isAvailable && isValid();
    }

    /**
     * Set available state.
     * <p>
     * 
     * @param isAvailable - next available state
     */
    public final void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    /**
     * Check if {@link EStructureInfo structure} is valid.
     * <p>
     * 
     * @return <code>true</code> if valid.
     */
    public final boolean isValid() {
        return isValid && !isDisposed;
    }
    
    /**
     * Invalidate the structure.
     * <p>
     * @param deep - if <code>true</code>, this and all it's 
     *  children is invalidated.
     */
    public final void invalidate(boolean deep){
        isValid = false;
        doInvalidate(deep);
    }
    
    /**
     * Check if {@link EStructureInfo structure} is disposed.
     * @return <code>true</code> if disposed.
     */
    public boolean isDisposed()
    {
        return isDisposed;
    }

    /**
     * Dispose this {@link EStructureInfo structure} and all it's children.
     */
    protected void dispose() {
        isDisposed = true;
        doDispose();
        eFactory = null;
        eContext = null;
        eListeners.clear();
        eListeners=null;
    }

    // ----------------------------------------------------- 
    //  EStructureInfoListener support methods
    // -----------------------------------------------------

    /**
     * Add {@link EFeatureListener} instance.
     * <p>
     * <strong>NOTE</strong>: This listener is stored as a {@link WeakReference weak reference},
     * allowing the garbage collector to reclaim it when no hard references to it exist. At this
     * point, the listener is automatically removed from this object.
     * </p>
     */
    public final void addListener(EFeatureListener<EStructureInfo<?>> eListener) {
        eListeners.add(eListener);
    }

    /**
     * Remove {@link EFeatureListener} instance.
     * <p>
     * <strong>NOTE</strong>: This listener is stored as a {@link WeakReference weak reference},
     * allowing the garbage collector to reclaim it when no hard references to it exist. At this
     * point, the listener is automatically removed from this object.
     * </p>
     */
    public final void removeListener(EFeatureListener<EStructureInfo<?>> eListener) {
        eListeners.remove(eListener);
    }

    // ----------------------------------------------------- 
    //  EStructureInfo implementation methods
    // -----------------------------------------------------
    
    protected abstract void doDispose();
    
    protected abstract T eParentInfo(boolean checkIsValid);
    
    protected abstract void doInvalidate(boolean deep);
    
    protected void doDetach() { /*NOP*/ }
    
    protected void doAdapt() { /*NOP*/ }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected void fireOnChange(int property, Object oldValue, Object newValue) {
        for (EFeatureListener it : eListeners) {
            it.onChange(this, property, oldValue, newValue);
        }
    }
    
    /**
     * Check if a {@link EFeatureContext} instance with given 
     * {@link EFeatureContext#eContextID() registry id} is cached by this, or 
     * the EFeatureContext {@link #eFactory() factory}.
     * </p>
     * @param eContextID - the {@link EFeatureContext#eContextID() registry id}
     * @return <code>true</code> if cached.
     */
    protected boolean isCached(String eContextID) {
        EFeatureContext eThis = null;
        if( !(eContext==null || eContext.get()==null) )
        {
            eThis = eContext.get();
        }
        if(eThis!=null)
        {
            return eThis.eContextID().equals(eContextID);
        }
        return eFactory(false).contains(eContextID);
    }
        
    /**
     * Get {@link EFeatureContextFactory} instance.
     * <p>
     * @param checkIsValid - if <code>true</code>, the method verifies that
     *  this {@link EStructureInfo structure} is valid. If invalid,
     *  an {@link IllegalStateException} is thrown.
     * </p> 
     * @throws IllegalStateException If {@link #isDisposed() disposed} or not found.
     */
    protected EFeatureContextFactory eFactory(boolean checkIsValid)
    {
        verify(checkIsValid);
        if(eFactory==null || eFactory.get()==null)
        {
            eFactory = new WeakReference<EFeatureContextFactory>(EFeatureContextFactory.eDefault());
            if(eFactory.get()==null) {
                invalidate(true);
                throw new IllegalStateException("EFeatureContextFactory '" + "' not found");
            }                
        }
        return eFactory.get();
    }      
        
    /**
     * Get {@link EFeatureContext} instance counterpart.
     * <p>
     * @param checkIsValid - if <code>true</code>, the method verifies that
     *  this {@link EFeatureDataStoreInfo structure} is valid. If invalid,
     *  an {@link IllegalStateException} is thrown.
     * </p> 
     * @throws IllegalStateException If {@link #isDisposed() disposed} or not found.
     */
    protected EFeatureContext eContext(boolean checkIsValid)
    {
        verify(checkIsValid);
        if(eContext==null || eContext.get()==null)
        {
            eContext = new WeakReference<EFeatureContext>(
                    eFactory(false).eContext(eContextID));
            if(eContext.get()==null) {
                invalidate(true);
                throw new IllegalStateException("EFeatureContext '" 
                        + eContextID + "' not found");
            }                
        }
        return eContext.get();
    }    
    
    protected void eAdapt(EStructureInfo<?> eStructure) {
        //
        // Detach from current context?
        //
        if(eContext(false)!=eStructure.eContext(false)) {
            //
            // Forward to implementation
            //
            doDetach();
        }
        //
        //
        // Update context information 
        //
        eContext = eStructure.eContext;
        eContextID = eStructure.eContextID;
        //
        // Forward
        //
        doAdapt();
    }

    // ----------------------------------------------------- 
    //  Static EStructureInfo helper methods
    // -----------------------------------------------------
    
    /**
     * Verify that state is available
     */
    protected void verify() throws IllegalStateException
    {
        verify(false);
    }
    
    /**
     * Verify that state is available
     */
    protected void verify(boolean checkIsValid) throws IllegalStateException
    {
        if(isDisposed)
            throw new IllegalStateException(this + " is disposed");
        if(checkIsValid && !isValid)
            throw new IllegalStateException(this + " is not valid. Please validate the structure.");
    }    

    protected static EFeatureStatus failure(Object source, String context, String message) {
        StackTraceElement[] stack = EFeatureUtils.getStackTrace(1);
        StackTraceElement trace = stack[0];
        message = trace.getClassName() + "[" + context+ "]#" + trace.getMethodName() + "(): " + message;
        LOGGER.warning(message);
        return EFeatureUtils.newStatus(source, EFeatureStatus.FAILURE, message, stack);

    }

    protected static final EFeatureStatus failure(Object source, 
            String context, String message, Throwable cause) {
        StackTraceElement[] stack = EFeatureUtils.getStackTrace(1);
        StackTraceElement trace = stack[0];
        message = trace.getClassName() + "[" + context+ "]#" +  trace.getMethodName() + "(): " + message;
        LOGGER.warning(message);
        if(cause==null) {
            return EFeatureUtils.newStatus(source, EFeatureStatus.FAILURE, message, stack);            
        }
        return EFeatureUtils.newStatus(source, EFeatureStatus.FAILURE, message, cause);
        
    }
    
    protected final EFeatureStatus structureIsValid(String context) {
        isValid = true;
        StackTraceElement trace = EFeatureUtils.getStackTraceElement(0,1);
        String msg = "Structure is valid: " + trace.getClassName() 
            + "[" + context + "]#" + trace.getMethodName()+"()";
        return success(msg);
    }

    protected final EFeatureStatus success(String message) {
        LOGGER.fine(message);
        return SUCCESS.clone(message);
    }

}

package org.geotools.data.efeature;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.geotools.data.efeature.query.EFeatureFilter;

/**
 * EFeature {@link TreeIterator} class.
 * <p>
 * This class implements a EObject Tree iterator that:
 * <ol>
 * <li>support lazy loading</li>
 * <li>support spatial filtering</li>
 * </ol>
 * </p>
 * 
 * @author kengu
 * 
 */
public class EFeatureIterator implements TreeIterator<EObject> {

    private static final long serialVersionUID = 1L;

    private EObject eNext = null;

    private EFeatureFilter where;

    private TreeIterator<EObject> it;

    public EFeatureIterator(TreeIterator<EObject> from, EFeatureFilter where) {
        // Initialize
        //
        this.where = where;
        //
        // Get EMF tree iterator
        //
        this.it = from;
    }

    public boolean hasNext() {
        if (eNext == null || it.hasNext()) {
            eNext = next();
        }
        return eNext != null;
    }

    public EObject peek() {
        //
        // Already matched by hasNext()?
        //
        if (eNext != null) {
            return eNext;
        }

        // Prepare
        //
        EObject eObject = null;

        // Loop until next match or end of collection
        //
        while (it.hasNext() && eObject == null) {
            // Get next object
            //
            eObject = it.next();
            //
            // Prune?
            //
            if (where.shouldPrune(eObject)) {
                it.prune();
            }
            //
            // Not a match?
            //
            if (!where.matches(eObject)) {
                eObject = null;
            }
        } // while
        
        //
        // Set as next
        //
        eNext = eObject;
        //
        // Finished
        //
        return eObject;
    }

    public EObject next() {
        try {
            return eNext != null ? eNext : peek();
        } finally {
            eNext = null;
        }

    }

    /**
     * Remove is not supported by this iterator
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }

    public void prune() {
        it.prune();
    }

}

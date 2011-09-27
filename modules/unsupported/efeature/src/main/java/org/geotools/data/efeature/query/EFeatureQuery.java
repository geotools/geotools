package org.geotools.data.efeature.query;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.geotools.data.efeature.EFeatureIterator;

/**
 * EFeature Query class.
 * <p>
 * This class returns a EObject Tree iterator that:
 * <ol>
 * <li>support lazy loading (memory efficient)</li>
 * <li>support spatial filtering</li>
 * </ol>
 * </p>
 * 
 * @author kengu
 * 
 *
 * @source $URL$
 */
public class EFeatureQuery {

    public static final int UNBOUNDED = 0;

    private EFeatureFilter eWhere;

    private TreeIterator<EObject> from;

    public EFeatureQuery(TreeIterator<EObject> from, EFeatureFilter eWhere) {
        this.from = from;
        this.eWhere = eWhere;
    }

    public EFeatureFilter getFilter() {
        return eWhere;
    }

    public EFeatureIterator iterator() {
        return new EFeatureIterator(from, eWhere);
    }

    public void dispose() {
        this.from = null;
        this.eWhere = null;
    }

}

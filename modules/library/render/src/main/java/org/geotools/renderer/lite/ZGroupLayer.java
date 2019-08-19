/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.renderer.lite;

import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.NoninvertibleTransformException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.geotools.data.FeatureSource;
import org.geotools.data.sort.SortedFeatureReader;
import org.geotools.data.util.DefaultProgressListener;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.renderer.style.SLDStyleFactory;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Style;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.ProgressListener;

/**
 * A special layer owning multiple feature sources and styles, all in the same z-group
 *
 * @author Andrea Aime - GeoSolutions
 */
class ZGroupLayer extends Layer {

    private String groupId;

    private List<Layer> layers = new ArrayList<>();

    private boolean compositingBase = false;

    private Composite composite;

    public ZGroupLayer(String groupId, FeatureLayer layer) {
        this.groupId = groupId;
        addLayer(layer);
    }

    public void drawFeatures(Graphics2D graphics, final StreamingRenderer renderer, String layerId)
            throws IOException, FactoryException, NoninvertibleTransformException, SchemaException,
                    TransformException {
        // 1) init all the readers and the lfts associated to them (one at a time to avoid deadlock)
        // and create one RenderableFeature for each
        // 2) process all the features one z-level at a time, backtracking if there are multiple
        // fts for a certain layer.

        // a listener passed around to stop data reading/painting if rendering stop request is
        // issued
        ProgressListener cancellationListener =
                new DefaultProgressListener() {
                    public boolean isCanceled() {
                        return renderer.renderingStopRequested;
                    };
                };

        List<ZGroupLayerPainter> painters = null;
        try {
            painters = buildLayerPainters(graphics, renderer, layerId, cancellationListener);
            if (painters.isEmpty()) {
                return;
            }

            // get a comparator to find the first key to paint
            Comparator<SortKey> comparator = SortKey.buildComparator(painters.get(0).sortBy);

            // paint all the features as we can
            SortKey previousKey = null;
            while (!painters.isEmpty()) {
                SortKey smallestKey = getSmallestKey(painters, comparator);
                if (previousKey == null) {
                    previousKey = smallestKey;
                } else if (comparator.compare(previousKey, smallestKey) >= 0) {
                    throw new IllegalStateException(
                            "The sorted rendering moved from a set of "
                                    + "sort attributes, to one that's equal or greater, this is unexpected, "
                                    + "bailing out to avoid an infinite loop");
                } else {
                    previousKey = smallestKey;
                }

                for (Iterator it = painters.iterator(); it.hasNext(); ) {
                    ZGroupLayerPainter painter = (ZGroupLayerPainter) it.next();
                    painter.paintKey(smallestKey);
                    // if the painter is done, close it
                    if (painter.complete()) {
                        painter.close();
                        it.remove();
                    }
                }
            }
        } finally {
            if (painters != null) {
                for (ZGroupLayerPainter painter : painters) {
                    painter.close();
                }
            }
        }
    }

    private SortKey getSmallestKey(
            List<ZGroupLayerPainter> painters, Comparator<SortKey> comparator) {
        SortKey smallest = null;
        for (ZGroupLayerPainter painter : painters) {
            SortKey key = painter.getCurrentKey();
            if (smallest == null) {
                smallest = key;
            } else if (comparator.compare(key, smallest) < 0) {
                smallest = key;
            }
        }

        return new SortKey(smallest);
    }

    private List<ZGroupLayerPainter> buildLayerPainters(
            Graphics2D graphics,
            StreamingRenderer renderer,
            String layerId,
            ProgressListener cancellationListener)
            throws IOException, FactoryException, NoninvertibleTransformException, SchemaException,
                    TransformException {
        List<ZGroupLayerPainter> painters = new ArrayList<>();
        boolean closePainters = true;
        try {
            for (Layer layer : layers) {
                // get the LiteFeatureTypeStyle for this layer
                final FeatureSource featureSource = layer.getFeatureSource();
                if (featureSource == null) {
                    throw new IllegalArgumentException(
                            "The layer does not contain a feature source");
                }
                final FeatureType schema = featureSource.getSchema();

                final ArrayList<LiteFeatureTypeStyle> lfts =
                        renderer.createLiteFeatureTypeStyles(layer, graphics, false);
                if (lfts.isEmpty()) {
                    continue;
                } else {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine(
                                "Processing " + lfts.size() + " stylers for " + schema.getName());
                    }
                }

                // get the feature iterator we need
                FeatureCollection features = renderer.getFeatures(layer, schema, lfts);
                // While we could use a non mark feature iterator for single fts layers,
                // that would cause multiple connections to be open at the same time,
                // which in turn could cause deadlocks against connection pools, so we
                // are going to build a MarkFeatureIterator regardless
                // TODO: we could optimize down to simple streaming painting if we end up
                // with a single painter with a single fts (due to scale dependencies)
                // but we'd have to delay opening the MarkFeatureIterator to recognize the
                // situation
                int maxFeatures = SortedFeatureReader.getMaxFeaturesInMemory(layer.getQuery());
                MarkFeatureIterator fi =
                        MarkFeatureIterator.create(features, maxFeatures, cancellationListener);
                if (fi.hasNext()) {
                    ZGroupLayerPainter painter =
                            new ZGroupLayerPainter(fi, lfts, renderer, layerId);
                    painters.add(painter);
                } else {
                    fi.close();
                }
            }

            validateSortBy(painters);

            // got to the end cleanly, no need to close the painters accumulated so far
            closePainters = false;
        } finally {
            if (closePainters) {
                for (ZGroupLayerPainter painter : painters) {
                    try {
                        painter.close();
                    } catch (Exception e) {
                        LOGGER.log(
                                Level.FINE, "Failed to close cleanly layer painter " + painter, e);
                    }
                }
            }
        }

        return painters;
    }

    /**
     * Ensures that all SortBy are meaningful for a cross layer z-order. We need the SortKey for all
     * the layers to have the same structure, be comparable, and be class compatible with each other
     * (and of course, exist in the first place)
     *
     * @param painters
     */
    private void validateSortBy(List<ZGroupLayerPainter> painters) {
        Class[] referenceClasses = null;
        SortOrder[] referenceOrders = null;
        LiteFeatureTypeStyle reference = null;
        for (ZGroupLayerPainter painter : painters) {
            for (LiteFeatureTypeStyle style : painter.lfts) {
                Class[] styleClasses = getSortByAttributeClasses(style);
                SortOrder[] styleOrders = getSortOrders(style);
                if (referenceClasses == null) {
                    referenceClasses = styleClasses;
                    referenceOrders = styleOrders;
                    reference = style;
                    for (int i = 0; i < referenceClasses.length; i++) {
                        if (!Comparable.class.isAssignableFrom(referenceClasses[i])) {
                            throw new IllegalArgumentException(
                                    "Found non comparable attribute in z group "
                                            + groupId
                                            + ": "
                                            + sortByToString(
                                                    style, getSortByAttributeClasses(style))
                                            + " at position "
                                            + (i + 1));
                        }
                    }
                } else {
                    if (styleClasses.length != referenceClasses.length) {
                        throw new IllegalArgumentException(
                                "Found two sortBy clauses with different number "
                                        + "of attributes in group "
                                        + groupId
                                        + ": "
                                        + sortByToString(reference, referenceClasses)
                                        + " vs "
                                        + sortByToString(style, styleClasses));
                    } else {
                        for (int i = 0; i < styleClasses.length; i++) {
                            Class currClass = styleClasses[i];
                            Class referenceClass = referenceClasses[i];
                            if (!currClass.equals(referenceClass)
                                    && !currClass.isAssignableFrom(referenceClass)
                                    && !referenceClass.isAssignableFrom(currClass)) {
                                throw new IllegalArgumentException(
                                        "Found two incompatible classes at position "
                                                + (i + 1)
                                                + " of the sortBy clauses in group "
                                                + groupId
                                                + ": "
                                                + sortByToString(reference, referenceClasses)
                                                + " vs "
                                                + sortByToString(style, styleClasses));
                            }
                            SortOrder currOrder = styleOrders[i];
                            SortOrder referenceOrder = referenceOrders[i];
                            if (!currOrder.equals(referenceOrder)) {
                                throw new IllegalArgumentException(
                                        "Found two different sort orders at position "
                                                + (i + 1)
                                                + " of the sortBy clauses in group "
                                                + groupId
                                                + ": "
                                                + sortByToString(reference, referenceClasses)
                                                + " vs "
                                                + sortByToString(style, styleClasses));
                            }
                        }
                    }
                }
            }
        }
    }

    private Class[] getSortByAttributeClasses(LiteFeatureTypeStyle style) {
        SortBy[] sb = style.sortBy;
        FeatureType schema = style.layer.getFeatureSource().getSchema();
        Class[] classes = new Class[sb.length];
        for (int i = 0; i < classes.length; i++) {
            PropertyName property = sb[i].getPropertyName();
            if (property == null) {
                // natural sorts
                classes[i] = String.class;
            } else {
                PropertyDescriptor pd = property.evaluate(schema, null);
                if (pd == null) {
                    throw new IllegalArgumentException(
                            "Property "
                                    + property
                                    + " could not be found in feature type "
                                    + schema.getName()
                                    + " in layer "
                                    + style.layer.getTitle());
                }
                classes[i] = pd.getType().getBinding();
            }
        }

        return classes;
    }

    private SortOrder[] getSortOrders(LiteFeatureTypeStyle style) {
        SortBy[] sb = style.sortBy;
        SortOrder[] orders = new SortOrder[sb.length];
        for (int i = 0; i < orders.length; i++) {
            orders[i] = sb[i].getSortOrder();
        }

        return orders;
    }

    private String sortByToString(LiteFeatureTypeStyle style, Class[] classes) {
        StringBuilder sb = new StringBuilder("Layer ").append(style.layer.getTitle()).append("[");
        SortBy[] sortBy = style.sortBy;
        for (int i = 0; i < sortBy.length; i++) {
            SortBy curr = sortBy[i];
            if (curr == SortBy.NATURAL_ORDER) {
                sb.append("NaturalOrder");
            } else if (curr == SortBy.REVERSE_ORDER) {
                sb.append("ReverseNaturalOrder");
            } else {
                sb.append(curr.getPropertyName().getPropertyName());
                sb.append("(").append(classes[i].getSimpleName()).append(")");
                if (curr.getSortOrder() == SortOrder.DESCENDING) {
                    sb.append(" D");
                }
            }
            if (i < sortBy.length) {
                sb.append(", ");
            }
        }
        sb.append("]");

        return sb.toString();
    }

    @Override
    public ReferencedEnvelope getBounds() {
        // no bounds to report
        return null;
    }

    public boolean isCompositingBase() {
        return compositingBase;
    }

    public Composite getComposite() {
        return composite;
    }

    public String getGroupId() {
        return groupId;
    }

    public void addLayer(FeatureLayer layer) {
        List<FeatureTypeStyle> featureTypeStyles = layer.getStyle().featureTypeStyles();
        boolean cleanupStyle = false;
        for (FeatureTypeStyle fts : featureTypeStyles) {
            Map<String, String> options = fts.getOptions();
            String compositingBaseDefinition = options.get(FeatureTypeStyle.COMPOSITE_BASE);
            if ("true".equalsIgnoreCase(compositingBaseDefinition)) {
                this.compositingBase = true;
            }
            // cannot really rely on equals here, we use a simple "last one wins" logic
            Composite composite = SLDStyleFactory.getComposite(options);
            if (composite != null) {
                this.composite = composite;
                cleanupStyle = true;
            }
        }
        // compositing is now handled at the ZGroupLayer level, remove it from the
        // inner layer
        if (cleanupStyle) {
            DuplicatingStyleVisitor cleaner =
                    new DuplicatingStyleVisitor() {
                        @Override
                        public void visit(FeatureTypeStyle fts) {
                            super.visit(fts);
                            FeatureTypeStyle copy = (FeatureTypeStyle) pages.peek();
                            copy.getOptions().remove(FeatureTypeStyle.COMPOSITE);
                            copy.getOptions().remove(FeatureTypeStyle.COMPOSITE_BASE);
                        }
                    };
            layer.getStyle().accept(cleaner);
            Style cleaned = (Style) cleaner.getCopy();
            layer.setStyle(cleaned);
        }

        layers.add(layer);
    }
}

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.metadata.math;

import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;
import org.geotools.api.util.Cloneable;
import org.geotools.metadata.i18n.DescriptionKeys;
import org.geotools.metadata.i18n.Descriptions;
import org.geotools.util.TableWriter;

/**
 * Holds some statistics about a series of sample values. Given a series of sample values <var>s<sub>0</sub></var>,
 * <var>s<sub>1</sub></var>, <var>s<sub>2</sub></var>, <var>s<sub>3</sub></var>..., this class computes
 * {@linkplain #minimum minimum}, {@linkplain #maximum maximum}, {@linkplain #mean mean}, {@linkplain #rms root mean
 * square} and {@linkplain #standardDeviation standard deviation}. Statistics are computed on the fly; the sample values
 * are never stored in memory.
 *
 * <p>An instance of {@code Statistics} is initially empty (i.e. all statistical values are set to {@link Double#NaN
 * NaN}). The statistics are updated every time an {@link #add(double)} method is invoked with a
 * non-{@linkplain Double#NaN NaN} value. A typical usage of this class is:
 *
 * <blockquote>
 *
 * <pre>
 * double[] data = new double[1000];
 * // (Compute some data values here...)
 *
 * Statistics stats = new Statistics();
 * for (int i=0; i&lt;data.length; i++) {
 *     stats.add(data[i]);
 * }
 * System.out.println(stats);
 * </pre>
 *
 * </blockquote>
 *
 * @since 2.0
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class Statistics implements Cloneable, Serializable {
    /** Serial number for compatibility with different versions. */
    private static final long serialVersionUID = -22884277805533726L;

    /**
     * Valeur minimale qui aie été transmise à la méthode {@link #add(double)}. Lors de la construction, ce champs est
     * initialisé à NaN.
     */
    private double min = Double.NaN;

    /**
     * Valeur maximale qui aie été transmise à la méthode {@link #add(double)}. Lors de la construction, ce champs est
     * initialisé à NaN.
     */
    private double max = Double.NaN;

    /**
     * Somme de toutes les valeurs qui ont été transmises à la méthode {@link #add(double)}. Lors de la construction, ce
     * champs est initialisé à 0.
     */
    private double sum = 0;

    /**
     * Somme des carrés de toutes les valeurs qui ont été transmises à la méthode {@link #add(double)}. Lors de la
     * construction, ce champs est initialisé à 0.
     */
    private double sum2 = 0;

    /**
     * Nombre de données autres que NaN qui ont été transmises à la méthode {@link #add(double)}. Lors de la
     * construction, ce champs est initialisé à 0.
     */
    private int n = 0;

    /**
     * Nombre de données égales à NaN qui ont été transmises à la méthode {@link #add(double)}. Les NaN sont ingorés
     * lors du calcul des statistiques, mais on les compte quand même au passage. Lors de la construction ce champs est
     * initialisé à 0.
     */
    private int nNaN = 0;

    /**
     * Constructs an initially empty set of statistics. All statistical values are initialized to {@link Double#NaN}.
     */
    public Statistics() {}

    /**
     * Resets the statistics to their initial {@link Double#NaN NaN} values. This method reset this object state as if
     * it was just created.
     */
    public void reset() {
        min = Double.NaN;
        max = Double.NaN;
        sum = 0;
        sum2 = 0;
        n = 0;
        nNaN = 0;
    }

    /**
     * Updates statistics for the specified sample. This {@code add} method is usually invoked inside a {@code for}
     * loop.
     *
     * @param sample The sample value. {@link Double#NaN NaN} values are ignored.
     * @see #add(long)
     * @see #add(Statistics)
     */
    public void add(final double sample) {
        if (!Double.isNaN(sample)) {
            /*
             *  Les deux prochaines lignes utilisent !(a>=b) au
             *  lieu de (a<b) afin de prendre en compte les NaN.
             */
            if (!(min <= sample)) min = sample;
            if (!(max >= sample)) max = sample;
            sum2 += sample * sample;
            sum += sample;
            n++;
        } else {
            nNaN++;
        }
    }

    /**
     * Updates statistics for the specified sample. This {@code add} method is usually invoked inside a {@code for}
     * loop.
     *
     * @param sample The sample value.
     * @see #add(double)
     * @see #add(Statistics)
     */
    public void add(final long sample) {
        final double fdatum = sample;
        if (!(min <= fdatum)) min = fdatum;
        if (!(max >= fdatum)) max = fdatum;
        sum2 += fdatum * fdatum;
        sum += fdatum;
        n++;
    }

    /**
     * Updates statistics with all samples from the specified {@code stats}. Invoking this method is equivalent (except
     * for rounding errors) to invoking {@link #add(double) add} for all samples that were added to {@code stats}.
     *
     * @param stats The statistics to be added to {@code this}, or {@code null} if none.
     */
    public void add(final Statistics stats) {
        if (stats != null) {
            // "if (a<b)" équivaut à "if (!isNaN(a) && a<b)".
            if (Double.isNaN(min) || stats.min < min) min = stats.min;
            if (Double.isNaN(max) || stats.max > max) max = stats.max;
            sum2 += stats.sum2;
            sum += stats.sum;
            n += stats.n;
            nNaN += stats.nNaN;
        }
    }

    /**
     * Returns the number of {@link Double#NaN NaN} samples. {@code NaN} samples are ignored in all other statitical
     * computation. This method count them for information purpose only.
     */
    public int countNaN() {
        return Math.max(nNaN, 0);
    }

    /** Returns the number of samples, excluding {@link Double#NaN NaN} values. */
    public int count() {
        return n;
    }

    /**
     * Returns the minimum sample value, or {@link Double#NaN NaN} if none.
     *
     * @see #maximum
     */
    public double minimum() {
        return min;
    }

    /**
     * Returns the maximum sample value, or {@link Double#NaN NaN} if none.
     *
     * @see #minimum
     */
    public double maximum() {
        return max;
    }

    /**
     * Returns the range of sample values. This is equivalent to <code>{@link #maximum maximum} -
     * {@link #minimum minimum}</code>, except for rounding error. If no samples were added, then returns
     * {@link Double#NaN NaN}.
     *
     * @see #minimum
     * @see #maximum
     */
    public double range() {
        return max - min;
    }

    /** Returns the mean value, or {@link Double#NaN NaN} if none. */
    public double mean() {
        return sum / n;
    }

    /** Returns the root mean square, or {@link Double#NaN NaN} if none. */
    public double rms() {
        return Math.sqrt(sum2 / n);
    }

    /**
     * Retourne l'écart type des échantillons par rapport à la moyenne. Si les données fournies aux différentes méthodes
     * {@code add(...)} se distribuent selon une loi normale, alors l'écart type est la distance de part et d'autre de
     * la moyenne dans lequel se trouveraient environ 84% des données. Le tableau ci-dessous donne le pourcentage
     * approximatif des données que l'on trouve de part et d'autre de la moyenne à des distances telles que 2 ou 3 fois
     * l'écart-type.
     *
     * <table align=center>
     *   <tr><td>&nbsp;0.5&nbsp;</td><td>&nbsp;69.1%&nbsp;</td></tr>
     *   <tr><td>&nbsp;1.0&nbsp;</td><td>&nbsp;84.2%&nbsp;</td></tr>
     *   <tr><td>&nbsp;1.5&nbsp;</td><td>&nbsp;93.3%&nbsp;</td></tr>
     *   <tr><td>&nbsp;2.0&nbsp;</td><td>&nbsp;97.7%&nbsp;</td></tr>
     *   <tr><td>&nbsp;3.0&nbsp;</td><td>&nbsp;99.9%&nbsp;</td></tr>
     * </table>
     *
     * @param allPopulation La valeur {@code true} indique que les données fournies aux différentes méthodes
     *     {@code add(...)} représentent l'ensemble de la polulation. La valeur {@code false} indique que ces données ne
     *     représentent qu'un échantillon de la population, ce qui est généralement le cas. Si le nombre de données est
     *     élevé, alors les valeurs {@code true} et {@code false} donneront sensiblement les mêmes résultats.
     */
    public double standardDeviation(final boolean allPopulation) {
        return Math.sqrt((sum2 - sum * sum / n) / (allPopulation ? n : n - 1));
    }

    /** Returns a clone of this statistics. */
    @Override
    public Statistics clone() {
        try {
            return (Statistics) super.clone();
        } catch (CloneNotSupportedException exception) {
            // Should not happen since we are cloneable
            throw new AssertionError(exception);
        }
    }

    /** Tests this statistics with the specified object for equality. */
    @Override
    public boolean equals(final Object obj) {
        if (obj != null && getClass().equals(obj.getClass())) {
            final Statistics cast = (Statistics) obj;
            return n == cast.n
                    && Double.doubleToLongBits(min) == Double.doubleToLongBits(cast.min)
                    && Double.doubleToLongBits(max) == Double.doubleToLongBits(cast.max)
                    && Double.doubleToLongBits(sum) == Double.doubleToLongBits(cast.sum)
                    && Double.doubleToLongBits(sum2) == Double.doubleToLongBits(cast.sum2);
        }
        return false;
    }

    /** Returns a hash code value for this statistics. */
    @Override
    public int hashCode() {
        final long code = Double.doubleToLongBits(min)
                + 37
                        * (Double.doubleToLongBits(max)
                                + 37 * (Double.doubleToLongBits(sum) + 37 * Double.doubleToLongBits(sum2)));
        return (int) code ^ (int) (code >>> 32) ^ n;
    }

    /**
     * Returns a string representation of this statistics. This method invokes {@link #toString(Locale, boolean)} using
     * the default locale and spaces separator.
     */
    @Override
    public final String toString() {
        return toString(null, false);
    }

    /**
     * Returns a localized string representation of this statistics. This string will span multiple lines, one for each
     * statistical value. For example:
     *
     * <blockquote>
     *
     * <pre>
     *     Compte:      8726
     *     Minimum:    6.853
     *     Maximum:    8.259
     *     Moyenne:    7.421
     *     RMS:        7.846
     *     Écart-type: 6.489
     * </pre>
     *
     * </blockquote>
     *
     * If {@code tabulations} is true, then labels (e.g. "Minimum") and values (e.g. "6.853") are separated by
     * tabulations. Otherwise, they are separated by spaces.
     */
    public String toString(final Locale locale, final boolean tabulations) {
        String text = Descriptions.getResources(locale)
                .getString(
                        DescriptionKeys.STATISTICS_TO_STRING_$6,
                        new Number[] {count(), minimum(), maximum(), mean(), rms(), standardDeviation(false)});
        if (!tabulations) {
            try (final TableWriter tmp = new TableWriter(null, 1)) {
                tmp.write(text);
                tmp.setColumnAlignment(1, TableWriter.ALIGN_RIGHT);
                text = tmp.toString();
            } catch (IOException ignore) {
                // from implicit close
            }
        }
        return text;
    }

    /**
     * Holds some statistics about a series of sample values and the difference between them. Given a series of sample
     * values <var>s<sub>0</sub></var>, <var>s<sub>1</sub></var>, <var>s<sub>2</sub></var>, <var>s<sub>3</sub></var>...,
     * this class computes statistics in the same way than {@link Statistics} and additionnaly computes statistics for
     * <var>s<sub>1</sub></var>-<var>s<sub>0</sub></var>, <var>s<sub>2</sub></var>-<var>s<sub>1</sub></var>,
     * <var>s<sub>3</sub></var>-<var>s<sub>2</sub></var>..., which are stored in a {@link #getDeltaStatistics delta}
     * statistics object.
     *
     * @version $Id$
     * @author Martin Desruisseaux (IRD)
     */
    public static class Delta extends Statistics {
        /** Serial number for compatibility with different versions. */
        private static final long serialVersionUID = 3464306833883333219L;

        /** Statistics about the differences between consecutive sample values. */
        private Statistics delta;

        /**
         * Last value given to an {@link #add(double) add} method as a {@code double}, or {@link Double#NaN NaN} if
         * none.
         */
        private double last = Double.NaN;

        /** Last value given to an {@link #add(long) add} method as a {@code long}, or 0 if none. */
        private long lastAsLong;

        /**
         * Constructs an initially empty set of statistics. All statistical values are initialized to
         * {@link Double#NaN}.
         */
        public Delta() {
            delta = new Statistics();
            delta.nNaN = -1; // Do not count the first NaN, which will always be the first value.
        }

        /**
         * Constructs an initially empty set of statistics using the specified object for {@link #getDeltaStatistics
         * delta} statistics. This method allows chaining different kind of statistics objects. For example, one could
         * write:
         *
         * <blockquote>
         *
         * <pre>
         * new Statistics.Delta(new Statistics.Delta());
         * </pre>
         *
         * </blockquote>
         *
         * Which would compute statistics of sample values, statistics of difference between consecutive sample values,
         * and statistics of difference of difference between consecutive sample values. Other kinds of
         * {@link Statistics} object could be chained as well.
         */
        public Delta(final Statistics delta) {
            this.delta = delta;
            delta.reset();
            delta.nNaN = -1; // Do not count the first NaN, which will always be the first value.
        }

        /**
         * Returns the statistics about difference between consecutives values. Given a series of sample values
         * <var>s<sub>0</sub></var>, <var>s<sub>1</sub></var>, <var>s<sub>2</sub></var>, <var>s<sub>3</sub></var>...,
         * this is statistics for <var>s<sub>1</sub></var>-<var>s<sub>0</sub></var>,
         * <var>s<sub>2</sub></var>-<var>s<sub>1</sub></var>, <var>s<sub>3</sub></var>-<var>s<sub>2</sub></var>...,
         */
        public Statistics getDeltaStatistics() {
            return delta;
        }

        /**
         * Resets the statistics to their initial {@link Double#NaN NaN} values. This method reset this object state as
         * if it was just created.
         */
        @Override
        public void reset() {
            super.reset();
            delta.reset();
            delta.nNaN = -1; // Do not count the first NaN, which will always be the first value.
            last = Double.NaN;
            lastAsLong = 0;
        }

        /**
         * Updates statistics for the specified sample. The {@link #getDeltaStatistics delta} statistics are updated
         * with <code>sample - sample<sub>last</sub></code> value, where <code>sample<sub>last</sub></code> is the last
         * value given to the previous call of an {@code add(...)} method.
         */
        @Override
        public void add(final double sample) {
            super.add(sample);
            delta.add(sample - last);
            last = sample;
            lastAsLong = (long) sample;
        }

        /**
         * Updates statistics for the specified sample. The {@link #getDeltaStatistics delta} statistics are updated
         * with <code>sample - sample<sub>last</sub></code> value, where <code>sample<sub>last</sub></code> is the last
         * value given to the previous call of an {@code add(...)} method.
         */
        @Override
        public void add(final long sample) {
            super.add(sample);
            if (last == lastAsLong) {
                // 'lastAsLong' may have more precision than 'last' since the cast to the
                // 'double' type may loose some digits. Invoke the 'delta.add(long)' version.
                delta.add(sample - lastAsLong);
            } else {
                // The sample value is either fractional, outside 'long' range,
                // infinity or NaN. Invoke the 'delta.add(double)' version.
                delta.add(sample - last);
            }
            last = sample;
            lastAsLong = sample;
        }

        /**
         * Update statistics with all samples from the specified {@code stats}. Invoking this method is equivalent
         * (except for rounding errors) to invoking {@link #add(double) add} for all samples that were added to
         * {@code stats}. The {@code stats} argument must be an instance of {@code Statistics.Delta}.
         *
         * @param stats The statistics to be added to {@code this}, or {@code null} if none.
         * @throws ClassCastException If {@code stats} is not an instance of {@code Statistics.Delta}.
         */
        @Override
        public void add(final Statistics stats) throws ClassCastException {
            if (stats != null) {
                final Delta toAdd = (Delta) stats;
                if (toAdd.delta.nNaN >= 0) {
                    delta.add(toAdd.delta);
                    last = toAdd.last;
                    lastAsLong = toAdd.lastAsLong;
                    super.add(stats);
                }
            }
        }

        /** Returns a clone of this statistics. */
        @Override
        public Delta clone() {
            Delta copy = (Delta) super.clone();
            copy.delta = copy.delta.clone();
            return copy;
        }

        /** Tests this statistics with the specified object for equality. */
        @Override
        public boolean equals(final Object obj) {
            return super.equals(obj) && delta.equals(((Delta) obj).delta);
        }

        /** Returns a hash code value for this statistics. */
        @Override
        public int hashCode() {
            return super.hashCode() + 37 * delta.hashCode();
        }
    }
}

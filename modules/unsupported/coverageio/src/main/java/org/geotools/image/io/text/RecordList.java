/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.io.text;

import java.util.Arrays;
import javax.imageio.IIOException;

import org.geotools.resources.Classes;
import org.geotools.resources.XArray;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;


/**
 * List of data records in an image. One instance of this class is created by
 * {@link TextRecordImageReader} for every image in a file. A {@code RecordList}
 * contains a list of records where each record contains data for one pixel. A record
 * contains usually the following information:
 *
 * <ul>
 *   <li>Pixel's x and y coordinate.</li>
 *   <li>Pixel's values for each band.</li>
 * </li>
 *
 * Those information can appear in arbitrary columns, providing that the column order
 * stay the same for every record in a particular {@code RecordList} instance.
 * Records can appear in arbitrary order.
 * <p>
 * Data can be floating point value ({@code float} type). Current implementation
 * expects pixels distributed on a regular grid. The grid interval will be automatically
 * computed when needed. The interval computation should be accurate even if there is
 * missing and/or duplicated records.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
final class RecordList {
    /**
     * Valeurs minimales des colonnes, ou {@code null} si
     * ces valeurs ne sont pas encore connues.  La longueur de
     * de ce tableau est égale à {@link #dataColumnCount}.
     */
    private double[] min;

    /**
     * Valeurs maximales des colonnes, ou {@code null} si
     * ces valeurs ne sont pas encore connues.  La longueur de
     * de ce tableau est égale à {@link #dataColumnCount}.
     */
    private double[] max;

    /**
     * Intervals entre les données, ou {@code null} si ces valeurs
     * n'ont pas encore été calculées. La valeur 0 signifie que l'interval
     * pour une colonne en particulier n'a pas encore été calculée.
     */
    private float[] interval;

    /**
     * Tableau des valeurs lues,  ou {@code null} si les
     * valeurs n'ont pas encore été lues. Ce tableau contient
     * une suite de lignes qui ont chacun un nombre de colonnes
     * égal à {@link #dataColumnCount}.
     */
    private float[] data;

    /**
     * Nombre de colonnes retenues lors de la lecture, ou -1 si ce nombre
     * n'est pas encore connu. Ce nombre de colonnes peut être égal ou
     * inférieur à {@code min.length} et {@code max.length}.
     */
    private int columnCount = -1;

    /**
     * Index suivant celui du dernier élément valide de {@link #data}.
     * Ce champ sera augmenté à chaque ajout d'une nouvelle ligne. Sa
     * valeur doit être un multiple entier de {@link #dataColumnCount}.
     */
    private int upper;

    /**
     * Nombre de lignes attendues. Cette information n'est qu'à titre
     * indicative, mais accelerera la lecture si elle est exacte.
     */
    private int expectedLineCount = 1024;

    /**
     * Construit un {@code ImageData} initiallement vide.
     * La première ligne de données lue déterminera le nombre
     * de colonnes qui seront retenus pour toutes les lignes
     * suivantes.
     */
    public RecordList() {
    }

    /**
     * Construit un {@code ImageData} initiallement vide.
     * Pour chaque ligne lue, seule les {@code columnCount}
     * premières colonnes seront retenus.
     *
     * @param columnCount Nombre de colonnes à retenir lors de la lecture.
     * @param expectedLineCount Nombre de lignes attendues. Cette information
     *        n'est qu'à titre indicative, mais accelerera la lecture si elle
     *        est exacte.
     */
    public RecordList(final int columnCount, final int expectedLineCount) {
        this.columnCount       = columnCount;
        this.expectedLineCount = expectedLineCount;
    }

    /**
     * Ajoute une ligne de données.  Si la ligne est plus courte que la longueur
     * attendues, les colonnes manquantes seront considérées comme contenant des
     * {@code NaN}.   Si elle est plus longue que la longueur attendue, les
     * colonnes en trop seront ignorées.
     */
    public void add(final double[] line) {
        if (data==null) {
            if (columnCount<0) columnCount=line.length;
            min  = new double[columnCount]; Arrays.fill(min, Double.POSITIVE_INFINITY);
            max  = new double[columnCount]; Arrays.fill(max, Double.NEGATIVE_INFINITY);
            data = new float [columnCount*expectedLineCount];
        }
        final int limit=Math.min(columnCount, line.length);
        final int nextUpper = upper+columnCount;
        if (nextUpper >= data.length) {
            data = XArray.resize(data, Math.max(nextUpper, data.length+Math.min(data.length, 65536)));
        }
        for (int i=0; i<limit; i++) {
            final double value = line[i];
            if (value < min[i]) min[i] = value;
            if (value > max[i]) max[i] = value;
            data[upper+i] = (float)value;
        }
        Arrays.fill(data, upper+limit, nextUpper, Float.NaN);
        upper = nextUpper;
    }

    /**
     * Libère la mémoire réservée en trop. Cette méthode peut être appelée
     * lorsqu'on a terminé de lire les données et qu'on veut les conserver
     * en mémoire pendant encore quelque temps.
     */
    public void trimToSize() {
        if (data != null) {
            data = XArray.resize(data, upper);
        }
    }

    /**
     * Retourne une référence directe vers les données mémorisées par cet objet.
     * NE PAS MODIFIER CES DONNEES! Les index valides vont de 0 inclusivement
     * jusqu'à {@link #getDataCount} exclusivement.
     */
    final float[] getData() {
        return data;
    }

    /**
     * Retourne le nombre de données qui ont été mémorisées.
     */
    final int getDataCount() {
        return upper;
    }

    /**
     * Retourne le nombre de lignes qui ont été mémorisées.
     */
    public int getLineCount() {
        if (columnCount <= 0) {
            return 0;
        }
        assert (upper % columnCount) == 0;
        return upper / columnCount;
    }

    /**
     * Retourne le nombre de colonnes, ou
     * -1 si ce nombre n'est pas connu.
     */
    public int getColumnCount() {
        return columnCount;
    }

    /**
     * Retourne la valeur minimale de la colonne spécifiée,
     * ou {@link Double#NaN} si cette valeur n'est pas connue.
     */
    public double getMinimum(final int column) {
        return (min != null && min[column] <= max[column]) ? min[column] : Double.NaN;
    }

    /**
     * Retourne la valeur maximale de la colonne spécifiée,
     * ou {@link Double#NaN} si cette valeur n'est pas connue.
     */
    public double getMaximum(final int column) {
        return (max != null && max[column] >= min[column]) ? max[column] : Double.NaN;
    }

    /**
     * Retourne l'interval entre les points de la colonne spécifiée, en supposant que les
     * points se trouvent à un interval régulier. Si ce n'est pas le cas, une exception
     * sera lancée.
     *
     * @param  column Colonne dont on veut l'interval entre les points.
     * @param  eps Petit facteur de tolérance (par exemple 1E-6).
     * @throws IIOException si les points de la colonne spécifiée
     *         ne sont pas distribués à un interval régulier.
     */
    private float getInterval(final int column, final float eps) throws IIOException {
        if (interval == null) {
            if (columnCount <= 0) {
                return Float.NaN;
            }
            interval = new float[columnCount];
        }
        if (interval[column] != 0) {
            return interval[column];
        }
        /*
         * Obtient toutes les valeurs de la colonne
         * spécifiée en ordre croissant.
         */
        int count=0;
        final float[] array = new float[getLineCount()];
        for (int i=column; i<upper; i+=columnCount) {
            array[count++] = data[i];
        }
        assert count == array.length;
        Arrays.sort(array);
        /*
         * Elimine les doublons. Lorsque des doublons seront trouvés, ils iront de
         * {@code lower} à {@code upper} <strong>inclusivement</strong>.
         */
        int upper = count-1;
        int lower = count;
        while (--lower>=1) {
            if (array[upper] != array[lower-1]) {
                if (upper != lower) {
                    System.arraycopy(array, upper, array, lower, count-upper);
                    final int oldCount = count;
                    count -= (upper-lower);
                    Arrays.fill(array, count, oldCount, Float.NaN); // Par prudence.
                }
                upper = lower-1;
            }
        }
        if (upper != lower) {
            System.arraycopy(array, upper, array, lower, count-upper);
            final int oldCount = count;
            count -= (upper-lower);
            Arrays.fill(array, count, oldCount, Float.NaN); // Par prudence.
        }
        /*
         * Recherche le plus petit interval entre deux points. Vérifie ensuite que
         * l'interval entre tous les points est un multiple entier de cet interval
         * minimal (on tient compte ainsi des éventuels données manquantes).
         */
        float delta = Float.POSITIVE_INFINITY;
        for (int i=1; i<count; i++) {
            final float d = array[i] - array[i-1];
            assert d>0;
            if (d < delta) {
                delta = d;
            }
        }
        for (int i=1; i<count; i++) {
            float e = (array[i] - array[i-1]) / delta;
            if (Math.abs(e-Math.rint(e)) > eps) {
                throw new IIOException(Errors.format(ErrorKeys.NOT_A_GRID));
            }
        }
        return interval[column] = Float.isInfinite(delta) ? Float.NaN : delta;
    }

    /**
     * Retourne le nombre de points distincts dans la colonne spécifiée. Cette méthode
     * élimine d'abord tous les doublons avant d'effectuer le comptage. Elle vérifie
     * aussi que les points restants sont espacés à un interval régulier, et lancera
     * une exception si ce n'est pas le cas. S'il y a des trous dans les données, il
     * seront pris en compte comme si un point s'y était trouvé.
     *
     * @param  column Colonne dont on veut le nombre de points distincts.
     * @param  eps Petit facteur de tolérance (par exemple 1E-6).
     * @throws IIOException si les points de la colonne spécifiée
     *         ne sont pas distribués à un interval régulier.
     */
    public int getPointCount(final int column, final float eps) throws IIOException {
        return (int)Math.round((getMaximum(column) - getMinimum(column)) / getInterval(column, eps)) +1;
    }

    /**
     * Retourne un résumé des informations que contient cet objet. Le résumé contiendra
     * notamment les valeurs minimales et maximales de chaque colonnes.
     *
     * @param  xColumn Colonne des <var>x</var>, ou -1 s'il n'est pas connu.
     * @param  yColumn Colonne des <var>y</var>, ou -1 s'il n'est pas connu.
     * @param  eps Petit facteur de tolérance (par exemple 1E-6).
     * @return Chaîne de caractères résumant l'état des données.
     */
    public String toString(final int xColumn, final int yColumn, final float eps) {
        float xCount = Float.NaN;
        float yCount = Float.NaN;
        if (xColumn >= 0) try {
            xCount = getPointCount(xColumn, eps);
        } catch (IIOException exception) {
            // Ignore.
        }
        if (yColumn >= 0) try {
            yCount = getPointCount(yColumn, eps);
        } catch (IIOException exception) {
            // Ignore.
        }
        return Vocabulary.format(VocabularyKeys.POINT_COUNT_IN_GRID_$3, upper, xCount, yCount);
    }

    /**
     * Retourne une chaîne de caractères représentant cet objet.
     * Cette chaîne indiquera le nombre de lignes et de colonnes
     * mémorisées.
     */
    @Override
    public String toString() {
        return Classes.getShortClassName(this) +
                '[' + getLineCount() + "\u00A0\u00D7\u00A0" + getColumnCount() + ']';
    }
}

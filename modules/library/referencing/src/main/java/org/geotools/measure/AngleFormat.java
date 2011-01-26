/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 1999-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.measure;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.resources.Classes;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.util.logging.LoggedFormat;
import org.geotools.util.Utilities;
import org.geotools.math.XMath;


/**
 * Parses and formats angles according a specified pattern. The pattern is a string
 * containing any characters, with a special meaning for the following characters:
 * <p>
 * <blockquote><table cellpadding="3">
 *     <tr><td>{@code D}</td><td>&nbsp;&nbsp;The integer part of degrees</td></tr>
 *     <tr><td>{@code d}</td><td>&nbsp;&nbsp;The fractional part of degrees</td></tr>
 *     <tr><td>{@code M}</td><td>&nbsp;&nbsp;The integer part of minutes</td></tr>
 *     <tr><td>{@code m}</td><td>&nbsp;&nbsp;The fractional part of minutes</td></tr>
 *     <tr><td>{@code S}</td><td>&nbsp;&nbsp;The integer part of seconds</td></tr>
 *     <tr><td>{@code s}</td><td>&nbsp;&nbsp;The fractional part of seconds</td></tr>
 *     <tr><td>{@code .}</td><td>&nbsp;&nbsp;The decimal separator</td></tr>
 * </table></blockquote>
 * <p>
 * Upper-case letters {@code D}, {@code M} and {@code S} are for the integer
 * parts of degrees, minutes and seconds respectively. They must appear in this order (e.g.
 * "<code>M'D</code>" is illegal because "M" and "S" are inverted; "<code>D°S</code>" is
 * illegal too because there is no "M" between "D" and "S"). Lower-case letters {@code d},
 * {@code m} and {@code s} are for fractional parts of degrees, minutes and seconds
 * respectively. Only one of those may appears in a pattern, and it must be the last special
 * symbol (e.g. "<code>D.dd°MM'</code>" is illegal because "d" is followed by "M";
 * "{@code D.mm}" is illegal because "m" is not the fractional part of "D").
 * <p>
 * The number of occurrence of {@code D}, {@code M}, {@code S} and their
 * lower-case counterpart is the number of digits to format. For example, "DD.ddd" will
 * format angle with two digits for the integer part and three digits for the fractional
 * part (e.g. 4.4578 will be formatted as "04.458"). Separator characters like <code>°</code>,
 * <code>'</code> and <code>"</code> and inserted "as-is" in the formatted string (except the
 * decimal separator dot ("{@code .}"), which is replaced by the local-dependent decimal
 * separator). Separator characters may be completely omitted; {@code AngleFormat} will
 * still differentiate degrees, minutes and seconds fields according the pattern. For example,
 * "{@code 0480439}" with the pattern "{@code DDDMMmm}" will be parsed as 48°04.39'.
 * <p>
 * The following table gives some examples of legal patterns.
 *
 * <blockquote><table cellpadding="3">
 * <tr><th>Pattern                </th>  <th>Example   </th></tr>
 * <tr><td><code>DD°MM'SS" </code></td>  <td>48°30'00" </td></tr>
 * <tr><td><code>DD°MM'    </code></td>  <td>48°30'    </td></tr>
 * <tr><td>{@code DD.ddd    }</td>  <td>48.500    </td></tr>
 * <tr><td>{@code DDMM      }</td>  <td>4830      </td></tr>
 * <tr><td>{@code DDMMSS    }</td>  <td>483000    </td></tr>
 * </table></blockquote>
 *
 * @see Angle
 * @see Latitude
 * @see Longitude
 *
 * @since 2.0
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (PMO, IRD)
 */
public class AngleFormat extends Format {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 4320403817210439764L;

    /**
     * Caractère représentant l'hémisphère nord.
     * Il doit obligatoirement être en majuscule.
     */
    private static final char NORTH = 'N';

    /**
     * Caractère représentant l'hémisphère sud.
     * Il doit obligatoirement être en majuscule.
     */
    private static final char SOUTH = 'S';

    /**
     * Caractère représentant l'hémisphère est.
     * Il doit obligatoirement être en majuscule.
     */
    private static final char EAST = 'E';

    /**
     * Caractère représentant l'hémisphère ouest.
     * Il doit obligatoirement être en majuscule.
     */
    private static final char WEST = 'W';

    /**
     * Constante indique que l'angle
     * à formater est une longitude.
     */
    static final int LONGITUDE = 0;

    /**
     * Constante indique que l'angle
     * à formater est une latitude.
     */
    static final int LATITUDE = 1;

    /**
     * Constante indique que le nombre
     * à formater est une altitude.
     */
    static final int ALTITUDE = 2;

    /**
     * A constant for the symbol to appears before the degrees fields.
     * Fields PREFIX, DEGREES, MINUTES and SECONDS <strong>must</strong>
     * have increasing values (-1, 0, +1, +2, +3).
     */
    private static final int PREFIX_FIELD = -1;

    /**
     * Constant for degrees field. When formatting a string, this value may be
     * specified to the {@link java.text.FieldPosition} constructor in order to
     * get the bounding index where degrees have been written.
     */
    public static final int DEGREES_FIELD = 0;

    /**
     * Constant for minutes field. When formatting a string, this value may be
     * specified to the {@link java.text.FieldPosition} constructor in order to
     * get the bounding index where minutes have been written.
     */
    public static final int MINUTES_FIELD = 1;

    /**
     * Constant for seconds field. When formatting a string, this value may be
     * specified to the {@link java.text.FieldPosition} constructor in order to
     * get the bounding index where seconds have been written.
     */
    public static final int SECONDS_FIELD = 2;

    /**
     * Constant for hemisphere field. When formatting a string, this value may be
     * specified to the {@link java.text.FieldPosition} constructor in order to
     * get the bounding index where the hemisphere synbol has been written.
     */
    public static final int HEMISPHERE_FIELD = 3;

    /**
     * Symboles représentant les degrés (0),
     * minutes (1) et les secondes (2).
     */
    private static final char[] SYMBOLS = {'D', 'M', 'S'};

    /**
     * Nombre minimal d'espaces que doivent occuper les parties
     * entières des degrés (0), minutes (1) et secondes (2). Le
     * champs {@code widthDecimal} indique la largeur fixe
     * que doit avoir la partie décimale. Il s'appliquera au
     * dernier champs non-zero dans {@code width0..2}.
     */
    private int width0=1, width1=2, width2=0, widthDecimal=0;

    /**
     * Caractères à insérer au début ({@code prefix}) et à la
     * suite des degrés, minutes et secondes ({@code suffix0..2}).
     * Ces champs doivent être {@code null} s'il n'y a rien à insérer.
     */
    private String prefix=null, suffix0="\u00B0", suffix1="'", suffix2="\"";

    /**
     * Indique s'il faut utiliser le séparateur décimal pour séparer la partie
     * entière de la partie fractionnaire. La valeur {@code false} indique
     * que les parties entières et fractionnaires doivent être écrites ensembles
     * (par exemple 34867 pour 34.867). La valeur par défaut est {@code true}.
     */
    private boolean decimalSeparator = true;

    /**
     * Format à utiliser pour écrire les nombres
     * (degrés, minutes ou secondes) à l'intérieur
     * de l'écriture d'un angle.
     */
    private final DecimalFormat numberFormat;

    /**
     * Objet à transmetre aux méthodes {@code DecimalFormat.format}.
     * Ce paramètre existe simplement pour éviter de créer cet objet trop
     * souvent, alors qu'on ne s'y intéresse pas.
     */
    private transient FieldPosition dummy = new FieldPosition(0);

    /**
     * Restore fields after deserialization.
     */
    private void readObject(final ObjectInputStream in)
        throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        dummy = new FieldPosition(0);
    }

    /**
     * Returns the width of the specified field.
     */
    private int getWidth(final int index) {
        switch (index) {
            case DEGREES_FIELD:  return width0;
            case MINUTES_FIELD:  return width1;
            case SECONDS_FIELD:  return width2;
            default:             return 0; // Must be 0 (important!)
        }
    }

    /**
     * Set the width for the specified field.
     * All folowing fields will be set to 0.
     */
    @SuppressWarnings("fallthrough")
    private void setWidth(final int index, int width) {
        switch (index) {
            case DEGREES_FIELD: width0=width; width=0; // fall through
            case MINUTES_FIELD: width1=width; width=0; // fall through
            case SECONDS_FIELD: width2=width;          // fall through
        }
    }

    /**
     * Returns the suffix for the specified field.
     */
    private String getSuffix(final int index) {
        switch (index) {
            case  PREFIX_FIELD: return prefix;
            case DEGREES_FIELD: return suffix0;
            case MINUTES_FIELD: return suffix1;
            case SECONDS_FIELD: return suffix2;
            default:            return null;
        }
    }

    /**
     * Sets the suffix for the specified field. Suffix
     * for all following fields will be set to their
     * default value.
     */
    @SuppressWarnings("fallthrough")
    private void setSuffix(final int index, String s) {
        switch (index) {
            case  PREFIX_FIELD:  prefix=s; s="\u00B0";  // fall through
            case DEGREES_FIELD: suffix0=s; s="'";       // fall through
            case MINUTES_FIELD: suffix1=s; s="\"";      // fall through
            case SECONDS_FIELD: suffix2=s;              // fall through
        }
    }

    /**
     * Constructs a new {@code AngleFormat} for the specified locale.
     *
     * @param locale The locale.
     * @return An angle format in the given locale.
     */
    public static AngleFormat getInstance(final Locale locale) {
        return new AngleFormat("D\u00B0MM.m'", locale);
    }

    /**
     * Set the rounding method to use when the last significant digit
     * in a value is 5.
     * <p>
     * Options are:
     * <ul>
     * <li> {@linkplain AngleFormat.RoundingMethod#ROUND_HALF_EVEN}
     * <li> {@linkplain AngleFormat.RoundingMethod#ROUND_HALF_UP}
     * <li> {@linkplain AngleFormat.RoundingMethod#ROUND_HALF_DOWN}
     * </ul>
     */
    public static enum RoundingMethod {
        /**
         * Round towards the even neighbour: e.g. {@code 2.5 => 2, 3.5 => 4}.
         * This method minimizes cumulative error over many values.
         */
        ROUND_HALF_EVEN,

        /**
         * Always round upwards: e.g. {@code 2.5 => 3, 3.5 => 4}.
         */
        ROUND_HALF_UP,

        /**
         * Always round downwards: e.g. {@code 2.5 => 2, 3.5 => 3}.
         */
        ROUND_HALF_DOWN;
    }

    /**
     * The default rounding method ({@linkplain AngleFormat.RoundingMethod#ROUND_HALF_EVEN}).
     */
    public static final RoundingMethod DEFAULT_ROUNDING_METHOD = RoundingMethod.ROUND_HALF_EVEN;

    /**
     * Global rounding method
     */
    private static RoundingMethod defaultRoundingMethod = DEFAULT_ROUNDING_METHOD;

    /**
     * Per-instance rounding method. If this differs to {@code defaultRoundingMethod}
     * it will be used instead.
     */
    private RoundingMethod instanceRoundingMethod = defaultRoundingMethod;

    /**
     * Set the default rounding method for all instances of this class to use when
     * the last significant digit of a value is 5.
     * <p>
     * Note: this will not affect the rounding method being used by instances of this
     * class created previously.
     *
     * @param method one of {@linkplain AngleFormat.RoundingMethod} constants
     *
     * @see #setRoundingMethod
     * @see #DEFAULT_ROUNDING_METHOD
     */
    public static synchronized void setDefaultRoundingMethod(RoundingMethod method) {
        defaultRoundingMethod = method;
    }

    /**
     * Get the default rounding method.
     *
     * @return the default rounding method.
     */
    public static synchronized RoundingMethod getDefaultRoundingMethod() {
        return defaultRoundingMethod;
    }

    /**
     * Set the rounding method for this instance to use when the last significant digit
     * of a value is 5. If the rounding method has previously been set globally with the
     * static {@linkplain #setDefaultRoundingMethod} method, setting a different method
     * here will take precedence for this instance.
     *
     * @param method one of {@linkplain AngleFormat.RoundingMethod} constants
     *
     * @see #setDefaultRoundingMethod
     * @see #DEFAULT_ROUNDING_METHOD
     */
    public synchronized void setRoundingMethod(RoundingMethod method) {
        instanceRoundingMethod = method;
    }

    /**
     * Get the rounding method being used by this instance.
     *
     * @return the rounding method for this instance
     */
    public static synchronized RoundingMethod getRoundingMethod() {
        return defaultRoundingMethod;
    }

    /**
     * Round a value according to the currently set rounding method for this class.
     *
     * @param value the value to round
     * @param precision the number of decimal places to retain
     *
     * @return the rounded value
     * @see AngleFormat.RoundingMethod
     */
    private synchronized double doRounding(double value, int precision) {
        final double scale = XMath.pow10(precision);
        final double eps = XMath.pow10(-precision - 2);
        double scaledValue = scale * (value + eps);
        long rounded;

        if (Double.compare(scaledValue, Long.MAX_VALUE) < 0) {
            RoundingMethod rm = instanceRoundingMethod;

            if (rm == RoundingMethod.ROUND_HALF_EVEN) {
                double d = scaledValue / 10d;
                boolean even = ((int)((d - (int)d) * 10)) % 2 == 0;
                rm = even ? RoundingMethod.ROUND_HALF_DOWN : RoundingMethod.ROUND_HALF_UP;
            }
                    
            switch (rm) {
                case ROUND_HALF_DOWN:
                    rounded = Math.round(scaledValue - 0.5d);
                    return rounded / scale;
                    
                case ROUND_HALF_UP:
                    rounded = Math.round(scaledValue);
                    return rounded / scale;
            }
        }

        Logger logger = Logger.getLogger(AngleFormat.class.getName());
        logger.log(Level.WARNING, String.format(
                "Can't round the value %s to the given precision %d",
                String.valueOf(value), precision));

        return value;
    }

    /**
     * Constructs a new {@code AngleFormat} using
     * the current default locale and a default pattern.
     */
    public AngleFormat() {
        this("D\u00B0MM.m'");
    }

    /**
     * Constructs a new {@code AngleFormat} using the
     * current default locale and the specified pattern.
     *
     * @param  pattern Pattern to use for parsing and formatting angle.
     *         See class description for an explanation of how this pattern work.
     * @throws IllegalArgumentException If the specified pattern is not legal.
     */
    public AngleFormat(final String pattern) throws IllegalArgumentException {
        this(pattern, new DecimalFormatSymbols());
    }

    /**
     * Constructs a new {@code AngleFormat}
     * using the specified pattern and locale.
     *
     * @param  pattern Pattern to use for parsing and formatting angle.
     *         See class description for an explanation of how this pattern work.
     * @param  locale Locale to use.
     * @throws IllegalArgumentException If the specified pattern is not legal.
     */
    public AngleFormat(final String pattern, final Locale locale) throws IllegalArgumentException {
        this(pattern, new DecimalFormatSymbols(locale));
    }

    /**
     * Constructs a new {@code AngleFormat}
     * using the specified pattern and decimal symbols.
     *
     * @param  pattern Pattern to use for parsing and formatting angle.
     *         See class description for an explanation of how this pattern work.
     * @param  symbols The symbols to use for parsing and formatting numbers.
     * @throws IllegalArgumentException If the specified pattern is not legal.
     */
    public AngleFormat(final String pattern, final DecimalFormatSymbols symbols) {
        // NOTE: pour cette routine, il ne faut PAS que DecimalFormat
        //       reconnaisse la notation exponentielle, parce que ça
        //       risquerait d'être confondu avec le "E" de "Est".
        numberFormat = new DecimalFormat("#0", symbols);
        applyPattern(pattern);
    }

    /**
     * Sets the pattern to use for parsing and formatting angle.
     * See class description for an explanation of how patterns work.
     *
     * @param  pattern Pattern to use for parsing and formatting angle.
     * @throws IllegalArgumentException If the specified pattern is not legal.
     */
    @SuppressWarnings("fallthrough")
    public synchronized void applyPattern(final String pattern) throws IllegalArgumentException {
        widthDecimal = 0;
        decimalSeparator = true;
        int startPrefix = 0;
        int symbolIndex = 0;
        boolean parseFinished = false;
        final int length = pattern.length();
        for (int i=0; i<length; i++) {
            /*
             * On examine un à un tous les caractères du patron en
             * sautant ceux qui ne sont pas réservés ("D", "M", "S"
             * et leur équivalents en minuscules). Les caractères
             * non-reservés seront mémorisés comme suffix plus tard.
             */
            final char c = pattern.charAt(i);
            final char upperCaseC = Character.toUpperCase(c);
            for (int field=DEGREES_FIELD; field<SYMBOLS.length; field++) {
                if (upperCaseC == SYMBOLS[field]) {
                    /*
                     * Un caractère réservé a été trouvé. Vérifie maintenant
                     * s'il est valide. Par exemple il serait illegal d'avoir
                     * comme patron "MM.mm" sans qu'il soit précédé des degrés.
                     * On attend les lettres "D", "M" et "S" dans l'ordre. Si
                     * le caractère est en lettres minuscules, il doit être le
                     * même que le dernier code (par exemple "DD.mm" est illegal).
                     */
                    if (c == upperCaseC) {
                        symbolIndex++;
                    }
                    if (field!=symbolIndex-1 || parseFinished) {
                        setWidth(DEGREES_FIELD, 1);
                        setSuffix(PREFIX_FIELD, null);
                        widthDecimal = 0;
                        decimalSeparator = true;
                        throw new IllegalArgumentException(Errors.format(
                                  ErrorKeys.ILLEGAL_ANGLE_PATTERN_$1, pattern));
                    }
                    if (c == upperCaseC) {
                        /*
                         * Mémorise les caractères qui précédaient ce code comme suffix
                         * du champs précédent. Puis on comptera le nombre de fois que le
                         * code se répète, en mémorisant cette information comme largeur
                         * de ce champ.
                         */
                        setSuffix(field-1, (i>startPrefix) ? pattern.substring(startPrefix, i) : null);
                        int w=1; while (++i<length && pattern.charAt(i)==c) w++;
                        setWidth(field, w);
                    } else {
                        /*
                         * Si le caractère est une minuscule, ce qui le précédait sera le
                         * séparateur décimal plutôt qu'un suffix. On comptera le nombre
                         * d'occurences du caractères pour obtenir la précision.
                         */
                        switch (i - startPrefix) {
                            case 0: {
                                decimalSeparator = false;
                                break;
                            }
                            case 1: {
                                if (pattern.charAt(startPrefix) == '.') {
                                    decimalSeparator = true;
                                    break;
                                }
                                // fall through
                            }
                            default: {
                                throw new IllegalArgumentException(Errors.format(
                                         ErrorKeys.ILLEGAL_ANGLE_PATTERN_$1, pattern));
                            }
                        }
                        int w=1; while (++i<length && pattern.charAt(i)==c) w++;
                        widthDecimal=w;
                        parseFinished = true;
                    }
                    startPrefix = i--;
                    break; // Break 'j' and continue 'i'.
                }
            }
        }
        setSuffix(symbolIndex-1, (startPrefix<length) ? pattern.substring(startPrefix) : null);
    }

    /**
     * Returns the pattern used for parsing and formatting angles.
     * See class description for an explanation of how patterns work.
     *
     * @return The formatting pattern.
     */
    public synchronized String toPattern() {
        char symbol = '#';
        final StringBuilder buffer = new StringBuilder();
        for (int field=DEGREES_FIELD; field<=SYMBOLS.length; field++) {
            final String previousSuffix = getSuffix(field-1);
            int w = getWidth(field);
            if (w > 0) {
                /*
                 * Procède à l'écriture de la partie entière des degrés,
                 * minutes ou secondes. Le suffix du champs précédent
                 * sera écrit avant les degrés, minutes ou secondes.
                 */
                if (previousSuffix != null) {
                    buffer.append(previousSuffix);
                }
                symbol = SYMBOLS[field];
                do buffer.append(symbol);
                while (--w > 0);
            } else {
                /*
                 * Procède à l'écriture de la partie décimale des
                 * degrés, minutes ou secondes. Le suffix du ce
                 * champs sera écrit après cette partie fractionnaire.
                 */
                w = widthDecimal;
                if (w > 0) {
                    if (decimalSeparator) buffer.append('.');
                    symbol = Character.toLowerCase(symbol);
                    do buffer.append(symbol);
                    while (--w > 0);
                }
                if (previousSuffix != null) {
                    buffer.append(previousSuffix);
                }
                break;
            }
        }
        return buffer.toString();
    }

    /**
     * Format an angle. The string will be formatted according
     * the pattern set in the last call to {@link #applyPattern}.
     *
     * @param  angle Angle to format, in degrees.
     * @return The formatted string.
     */
    public final String format(final double angle) {
        return format(angle, new StringBuffer(), null).toString();
    }

    /**
     * Formats an angle and appends the resulting text to a given string buffer.
     * The string will be formatted according the pattern set in the last call
     * to {@link #applyPattern}.
     *
     * @param  angle      Angle to format, in degrees.
     * @param  toAppendTo Where the text is to be appended.
     * @param  pos        An optional {@link FieldPosition} identifying a field
     *                    in the formatted text, or {@code null} if this
     *                    information is not wanted. This field position shall
     *                    be constructed with one of the following constants:
     *                    {@link #DEGREES_FIELD},
     *                    {@link #MINUTES_FIELD},
     *                    {@link #SECONDS_FIELD} or
     *                    {@link #HEMISPHERE_FIELD}.
     *
     * @return The string buffer passed in as {@code toAppendTo}, with formatted text appended.
     */
    public synchronized StringBuffer format(final double angle, StringBuffer toAppendTo,
                                            final FieldPosition pos)
    {
        if (Double.isNaN(angle) || Double.isInfinite(angle)) {
            return numberFormat.format(angle, toAppendTo,
                    (pos != null) ? pos : new FieldPosition(DecimalFormat.INTEGER_FIELD));
        }
        double degrees = angle;
        /*
         * Calcule à l'avance les minutes et les secondes. Si les minutes et secondes
         * ne doivent pas être écrits, on mémorisera NaN. Notez que pour extraire les
         * parties entières, on utilise (int) au lieu de 'Math.floor' car (int) arrondie
         * vers 0 (ce qui est le comportement souhaité) alors que 'floor' arrondie vers
         * l'entier inférieur.
         */
        double minutes  = Double.NaN;
        double secondes = Double.NaN;
        if (width1!=0 && !Double.isNaN(angle)) {
            int tmp = (int) degrees; // Arrondie vers 0 même si négatif.
            minutes = Math.abs(degrees - tmp) * 60;
            degrees = tmp;
            if (minutes<0 || minutes>60) {
                // Erreur d'arrondissement (parce que l'angle est trop élevé)
                throw new IllegalArgumentException(Errors.format(ErrorKeys.ANGLE_OVERFLOW_$1, angle));
            }
            if (width2 != 0) {
                tmp      = (int) minutes; // Arrondie vers 0 même si négatif.
                secondes = (minutes - tmp) * 60;
                minutes  = tmp;
                if (secondes<0 || secondes>60) {
                    // Erreur d'arrondissement (parce que l'angle est trop élevé)
                    throw new IllegalArgumentException(Errors.format(ErrorKeys.ANGLE_OVERFLOW_$1, angle));
                }
                tmp = (int) (secondes/60);
                secondes -= 60*tmp;
                minutes += tmp;
            }
            tmp = (int) (minutes/60); // Arrondie vers 0 même si négatif.
            minutes -= 60*tmp;
            degrees += tmp;
        }
        /*
         * Les variables 'degrés', 'minutes' et 'secondes' contiennent
         * maintenant les valeurs des champs à écrire, en principe épurés
         * des problèmes d'arrondissements. Procède maintenant à l'écriture
         * de l'angle.
         */
        if (prefix != null) {
            toAppendTo.append(prefix);
        }
        final int field;
        if (pos != null) {
            field = pos.getField();
            pos.setBeginIndex(0);
            pos.setEndIndex(0);
        } else {
            field=PREFIX_FIELD;
        }
        toAppendTo = formatField(degrees, toAppendTo,
                                 field == DEGREES_FIELD ? pos : null,
                                 width0, width1==0, suffix0);
        if (!Double.isNaN(minutes)) {
            toAppendTo = formatField(minutes, toAppendTo,
                                   field == MINUTES_FIELD ? pos : null,
                                   width1, width2==0, suffix1);
        }
        if (!Double.isNaN(secondes)) {
            toAppendTo = formatField(secondes, toAppendTo,
                                   field == SECONDS_FIELD ? pos : null,
                                   width2, true, suffix2);
        }
        return toAppendTo;
    }

    /**
     * Procède à l'écriture d'un champ de l'angle.
     *
     * @param value Valeur à écrire.
     * @param toAppendTo Buffer dans lequel écrire le champs.
     * @param pos Objet dans lequel mémoriser les index des premiers
     *        et derniers caractères écrits, ou {@code null}
     *        pour ne pas mémoriser ces index.
     * @param w Nombre de minimal caractères de la partie entière.
     * @param last {@code true} si ce champs est le dernier,
     *        et qu'il faut donc écrire la partie décimale.
     * @param s Suffix à écrire après le nombre (peut être nul).
     */
    private StringBuffer formatField(double value,
                                     StringBuffer toAppendTo, final FieldPosition pos,
                                     final int w, final boolean last, final String s)
    {
        final int startPosition=toAppendTo.length();
        if (!last) {
            numberFormat.setMinimumIntegerDigits(w);
            numberFormat.setMaximumFractionDigits(0);
            toAppendTo = numberFormat.format(value, toAppendTo, dummy);

        } else {
            value = doRounding(value, widthDecimal);
            
            if (decimalSeparator) {
                numberFormat.setMinimumIntegerDigits(w);
                numberFormat.setMinimumFractionDigits(widthDecimal);
                numberFormat.setMaximumFractionDigits(widthDecimal);
                toAppendTo = numberFormat.format(value, toAppendTo, dummy);
            } else {
                value *= XMath.pow10(widthDecimal);
                numberFormat.setMaximumFractionDigits(0);
                numberFormat.setMinimumIntegerDigits(w + widthDecimal);
                toAppendTo = numberFormat.format(value, toAppendTo, dummy);
            }
        }
        if (s!=null) {
            toAppendTo.append(s);
        }
        if (pos!=null) {
            pos.setBeginIndex(startPosition);
            pos.setEndIndex(toAppendTo.length()-1);
        }
        return toAppendTo;
    }

    /**
     * Formats an angle, a latitude or a longitude and appends the resulting text
     * to a given string buffer. The string will be formatted according the pattern
     * set in the last call to {@link #applyPattern}. The argument {@code obj}
     * shall be an {@link Angle} object or one of its derived class ({@link Latitude},
     * {@link Longitude}). If {@code obj} is a {@link Latitude} object, then a
     * symbol "N" or "S" will be appended to the end of the string (the symbol will
     * be choosen according the angle's sign). Otherwise, if {@code obj} is a
     * {@link Longitude} object, then a symbol "E" or "W" will be appended to the
     * end of the string. Otherwise, no hemisphere symbol will be appended.
     * <br><br>
     * Strictly speaking, formatting ordinary numbers is not the
     * {@code AngleFormat}'s job. Nevertheless, this method
     * accept {@link Number} objects. This capability is provided
     * only as a convenient way to format altitude numbers together
     * with longitude and latitude angles.
     *
     * @param  obj        {@link Angle} or {@link Number} object to format.
     * @param  toAppendTo Where the text is to be appended.
     * @param  pos        An optional {@link FieldPosition} identifying a field
     *                    in the formatted text, or {@code null} if this
     *                    information is not wanted. This field position shall
     *                    be constructed with one of the following constants:
     *                    {@link #DEGREES_FIELD},
     *                    {@link #MINUTES_FIELD},
     *                    {@link #SECONDS_FIELD} or
     *                    {@link #HEMISPHERE_FIELD}.
     *
     * @return The string buffer passed in as {@code toAppendTo}, with
     *         formatted text appended.
     * @throws IllegalArgumentException if {@code obj} if not an object
     *         of class {@link Angle} or {@link Number}.
     */
    public synchronized StringBuffer format(final Object obj,
                                            StringBuffer toAppendTo,
                                            final FieldPosition pos)
        throws IllegalArgumentException
    {
        if (obj instanceof Latitude) {
            return format(((Latitude) obj).degrees(), toAppendTo, pos, NORTH, SOUTH);
        }
        if (obj instanceof Longitude) {
            return format(((Longitude) obj).degrees(), toAppendTo, pos, EAST, WEST);
        }
        if (obj instanceof Angle) {
            return format(((Angle) obj).degrees(), toAppendTo, pos);
        }
        if (obj instanceof Number) {
            numberFormat.setMinimumIntegerDigits(1);
            numberFormat.setMinimumFractionDigits(0);
            numberFormat.setMaximumFractionDigits(2);
            return numberFormat.format(obj, toAppendTo, (pos!=null) ? pos : dummy);
        }
        throw new IllegalArgumentException(Errors.format(ErrorKeys.NOT_AN_ANGLE_OBJECT_$1,
                Classes.getClass(obj)));
    }

    /**
     * Procède à l'écriture d'un angle, d'une latitude ou d'une longitude.
     *
     * @param  number     Angle ou nombre à écrire.
     * @param  type       Type de l'angle ou du nombre:
     *                    {@link #LONGITUDE},
     *                    {@link #LATITUDE} ou
     *                    {@link #ALTITUDE}.
     * @param  toAppendTo Buffer dans lequel écrire l'angle.
     * @param  pos        En entré, le code du champs dont on désire les index
     *                    ({@link #DEGREES_FIELD},
     *                     {@link #MINUTES_FIELD},
     *                     {@link #SECONDS_FIELD} ou
     *                     {@link #HEMISPHERE_FIELD}).
     *                    En sortie, les index du champs demandé. Ce paramètre
     *                    peut être nul si cette information n'est pas désirée.
     *
     * @return Le buffer {@code toAppendTo} par commodité.
     */
    synchronized StringBuffer format(final double number, final int type,
                                     StringBuffer toAppendTo,
                                     final FieldPosition pos)
    {
        switch (type) {
            default:        throw new IllegalArgumentException(Integer.toString(type)); // Should not happen.
            case LATITUDE:  return format(number, toAppendTo, pos, NORTH, SOUTH);
            case LONGITUDE: return format(number, toAppendTo, pos, EAST,  WEST );
            case ALTITUDE: {
                numberFormat.setMinimumIntegerDigits(1);
                numberFormat.setMinimumFractionDigits(0);
                numberFormat.setMaximumFractionDigits(2);
                return numberFormat.format(number, toAppendTo, (pos!=null) ? pos : dummy);
            }
        }
    }

    /**
     * Procède à l'écriture d'un angle suivit d'un suffix 'N','S','E' ou 'W'.
     * L'angle sera formaté en utilisant comme modèle le patron spécifié lors
     * du dernier appel de la méthode {@link #applyPattern}.
     *
     * @param  angle      Angle à écrire, en degrés.
     * @param  toAppendTo Buffer dans lequel écrire l'angle.
     * @param  pos        En entré, le code du champs dont on désire les index
     *                    ({@link #DEGREES_FIELD},
     *                     {@link #MINUTES_FIELD},
     *                     {@link #SECONDS_FIELD} ou
     *                     {@link #HEMISPHERE_FIELD}).
     *                    En sortie, les index du champs demandé. Ce paramètre
     *                    peut être nul si cette information n'est pas désirée.
     * @param north       Caractères à écrire si l'angle est positif ou nul.
     * @param south       Caractères à écrire si l'angle est négatif.
     *
     * @return Le buffer {@code toAppendTo} par commodité.
     */
    private StringBuffer format(final double angle,
                                StringBuffer toAppendTo,
                                final FieldPosition pos,
                                final char north, final char south)
    {
        toAppendTo = format(Math.abs(angle), toAppendTo, pos);
        final int start = toAppendTo.length();
        toAppendTo.append(angle<0 ? south : north);
        if (pos!=null && pos.getField()==HEMISPHERE_FIELD) {
            pos.setBeginIndex(start);
            pos.setEndIndex(toAppendTo.length()-1);
        }
        return toAppendTo;
    }

    /**
     * Ignore le suffix d'un nombre. Cette méthode est appellée par la méthode
     * {@link #parse} pour savoir quel champs il vient de lire. Par exemple si
     * l'on vient de lire les degrés dans "48°12'", alors cette méthode extraira
     * le "°" et retournera 0 pour indiquer que l'on vient de lire des degrés.
     *
     * Cette méthode se chargera d'ignorer les espaces qui précèdent le suffix.
     * Elle tentera ensuite de d'abord interpréter le suffix selon les symboles
     * du patron (spécifié avec {@link #applyPattern}. Si le suffix n'a pas été
     * reconnus, elle tentera ensuite de le comparer aux symboles standards
     * (° ' ").
     *
     * @param  source Chaîne dans laquelle on doit sauter le suffix.
     * @param  pos En entré, l'index du premier caractère à considérer dans la
     *         chaîne {@code pos}. En sortie, l'index du premier caractère
     *         suivant le suffix (c'est-à-dire index à partir d'où continuer la
     *         lecture après l'appel de cette méthode). Si le suffix n'a pas été
     *         reconnu, alors cette méthode retourne par convention {@code SYMBOLS.length}.
     * @param  field Champs à vérifier de préférences. Par exemple la valeur 1 signifie que les
     *         suffix des minutes et des secondes devront être vérifiés avant celui des degrés.
     * @return Le numéro du champs correspondant au suffix qui vient d'être extrait:
     *         -1 pour le préfix de l'angle, 0 pour le suffix des degrés, 1 pour le
     *         suffix des minutes et 2 pour le suffix des secondes. Si le texte n'a
     *         pas été reconnu, retourne {@code SYMBOLS.length}.
     */
    private int skipSuffix(final String source, final ParsePosition pos, int field) {
        /*
         * Essaie d'abord de sauter les suffix qui
         * avaient été spécifiés dans le patron.
         */
        final int length = source.length();
        int start = pos.getIndex();
        for (int j=SYMBOLS.length; j>=0; j--) { // C'est bien j>=0 et non j>0.
            int index = start;
            final String toSkip = getSuffix(field);
            if (toSkip != null) {
                final int toSkipLength = toSkip.length();
                do {
                    if (source.regionMatches(index, toSkip, 0, toSkipLength)) {
                        pos.setIndex(index + toSkipLength);
                        return field;
                    }
                }
                while (index<length && Character.isSpaceChar(source.charAt(index++)));
            }
            if (++field >= SYMBOLS.length) field = -1;
        }
        /*
         * Le texte trouvé ne correspondant à aucun suffix du patron,
         * essaie maintenant de sauter un des suffix standards (après
         * avoir ignoré les espaces qui le précédaient).
         */
        char c;
        do {
            if (start >= length) {
                return SYMBOLS.length;
            }
        }
        while (Character.isSpaceChar(c = source.charAt(start++)));
        switch (c) {
            case '\u00B0' : pos.setIndex(start); return DEGREES_FIELD;
            case '\''     : pos.setIndex(start); return MINUTES_FIELD;
            case '"'      : pos.setIndex(start); return SECONDS_FIELD;
            default       : return SYMBOLS.length; // Unknow field.
        }
    }

    /**
     * Parses a string as an angle. This method can parse an angle even if it
     * doesn't comply exactly to the expected pattern. For example, this method
     * will parse correctly string "<code>48°12.34'</code>" even if the expected
     * pattern was "{@code DDMM.mm}" (i.e. the string should have been
     * "{@code 4812.34}"). Spaces between degrees, minutes and secondes
     * are ignored. If the string ends with an hemisphere symbol "N" or "S",
     * then this method returns an object of class {@link Latitude}. Otherwise,
     * if the string ends with an hemisphere symbol "E" or "W", then this method
     * returns an object of class {@link Longitude}. Otherwise, this method
     * returns an object of class {@link Angle}.
     *
     * @param source A String whose beginning should be parsed.
     * @param pos    Position where to start parsing.
     * @return       The parsed string as an {@link Angle}, {@link Latitude}
     *               or {@link Longitude} object.
     */
    public Angle parse(final String source, final ParsePosition pos) {
        return parse(source, pos, false);
    }

    /**
     * Interprète une chaîne de caractères représentant un angle. Les règles
     * d'interprétation de cette méthode sont assez souples. Par exemple cettte
     * méthode interprétera correctement la chaîne "48°12.34'" même si le patron
     * attendu était "DDMM.mm" (c'est-à-dire que la chaîne aurait du être "4812.34").
     * Les espaces entre les degrés, minutes et secondes sont acceptés. Si l'angle
     * est suivit d'un symbole "N" ou "S", alors l'objet retourné sera de la classe
     * {@link Latitude}. S'il est plutot suivit d'un symbole "E" ou "W", alors l'objet
     * retourné sera de la classe {@link Longitude}. Sinon, il sera de la classe
     * {@link Angle}.
     *
     * @param source           Chaîne de caractères à lire.
     * @param pos              Position à partir d'où interpréter la chaîne.
     * @param spaceAsSeparator Indique si l'espace est accepté comme séparateur
     *                         à l'intérieur d'un angle. La valeur {@code true}
     *                         fait que l'angle "45 30" sera interprété comme "45°30".
     * @return L'angle lu.
     */
    @SuppressWarnings("fallthrough")
    private synchronized Angle parse(final String source,
                                     final ParsePosition pos,
                                     final boolean spaceAsSeparator)
    {
        double degrees   = Double.NaN;
        double minutes  = Double.NaN;
        double secondes = Double.NaN;
        final int length=source.length();
        ///////////////////////////////////////////////////////////////////////////////
        // BLOC A: Analyse la chaîne de caractères 'source' et affecte aux variables //
        //         'degrés', 'minutes' et 'secondes' les valeurs appropriées.        //
        //         Les premières accolades ne servent qu'à garder locales            //
        //         les variables sans intérêt une fois la lecture terminée.          //
        ///////////////////////////////////////////////////////////////////////////////
        {
            /*
             * Extrait le préfix, s'il y en avait un. Si on tombe sur un symbole des
             * degrés, minutes ou secondes alors qu'on n'a pas encore lu de nombre,
             * on considèrera que la lecture a échouée.
             */
            final int indexStart = pos.getIndex();
            int index = skipSuffix(source, pos, PREFIX_FIELD);
            if (index>=0 && index<SYMBOLS.length) {
                pos.setErrorIndex(indexStart);
                pos.setIndex(indexStart);
                return null;
            }
            /*
             * Saute les espaces blancs qui
             * précèdent le champs des degrés.
             */
            index = pos.getIndex();
            while (index<length && Character.isSpaceChar(source.charAt(index))) index++;
            pos.setIndex(index);
            /*
             * Lit les degrés. Notez que si aucun séparateur ne séparait les degrés
             * des minutes des secondes, alors cette lecture pourra inclure plusieurs
             * champs (exemple: "DDDMMmmm"). La séparation sera faite plus tard.
             */
            Number fieldObject = numberFormat.parse(source, pos);
            if (fieldObject == null) {
                pos.setIndex(indexStart);
                if (pos.getErrorIndex() < indexStart) {
                    pos.setErrorIndex(index);
                }
                return null;
            }
            degrees = fieldObject.doubleValue();
            int indexEndField = pos.getIndex();
            boolean swapDM = true;
BigBoss:    switch (skipSuffix(source, pos, DEGREES_FIELD)) {
                /* ----------------------------------------------
                 * ANALYSE DU SYMBOLE SUIVANT LES PRÉSUMÉS DEGRÉS
                 * ----------------------------------------------
                 * Les degrés étaient suivit du préfix d'un autre angle. Le préfix sera donc
                 * retourné dans le buffer pour un éventuel traitement par le prochain appel
                 * à la méthode 'parse' et on n'ira pas plus loin dans l'analyse de la chaîne.
                 */
                case PREFIX_FIELD: {
                    pos.setIndex(indexEndField);
                    break BigBoss;
                }
                /* ----------------------------------------------
                 * ANALYSE DU SYMBOLE SUIVANT LES PRÉSUMÉS DEGRÉS
                 * ----------------------------------------------
                 * On a trouvé le symbole des secondes au lieu de celui des degrés. On fait
                 * la correction dans les variables 'degrés' et 'secondes' et on considère
                 * que la lecture est terminée.
                 */
                case SECONDS_FIELD: {
                    secondes = degrees;
                    degrees = Double.NaN;
                    break BigBoss;
                }
                /* ----------------------------------------------
                 * ANALYSE DU SYMBOLE SUIVANT LES PRÉSUMÉS DEGRÉS
                 * ----------------------------------------------
                 * Aucun symbole ne suit les degrés. Des minutes sont-elles attendues?
                 * Si oui, on fera comme si le symbole des degrés avait été là. Sinon,
                 * on considèrera que la lecture est terminée.
                 */
                default: {
                    if (width1 == 0)       break BigBoss;
                    if (!spaceAsSeparator) break BigBoss;
                    // fall through
                }
                /* ----------------------------------------------
                 * ANALYSE DU SYMBOLE SUIVANT LES PRÉSUMÉS DEGRÉS
                 * ----------------------------------------------
                 * Un symbole des degrés a été explicitement trouvé. Les degrés sont peut-être
                 * suivit des minutes. On procèdera donc à la lecture du prochain nombre, puis
                 * à l'analyse du symbole qui le suit.
                 */
                case DEGREES_FIELD: {
                    final int indexStartField = index = pos.getIndex();
                    while (index<length && Character.isSpaceChar(source.charAt(index))) {
                        index++;
                    }
                    if (!spaceAsSeparator && index!=indexStartField) {
                        break BigBoss;
                    }
                    pos.setIndex(index);
                    fieldObject=numberFormat.parse(source, pos);
                    if (fieldObject==null) {
                        pos.setIndex(indexStartField);
                        break BigBoss;
                    }
                    indexEndField = pos.getIndex();
                    minutes = fieldObject.doubleValue();
                    switch (skipSuffix(source, pos, (width1!=0) ? MINUTES_FIELD : PREFIX_FIELD)) {
                        /* ------------------------------------------------
                         * ANALYSE DU SYMBOLE SUIVANT LES PRÉSUMÉES MINUTES
                         * ------------------------------------------------
                         * Le symbole trouvé est bel et bien celui des minutes.
                         * On continuera le bloc pour tenter de lire les secondes.
                         */
                        case MINUTES_FIELD: {
                            break; // continue outer switch
                        }
                        /* ------------------------------------------------
                         * ANALYSE DU SYMBOLE SUIVANT LES PRÉSUMÉES MINUTES
                         * ------------------------------------------------
                         * Un symbole des secondes a été trouvé au lieu du symbole des minutes
                         * attendu. On fera la modification dans les variables 'secondes' et
                         * 'minutes' et on considèrera la lecture terminée.
                         */
                        case SECONDS_FIELD: {
                            secondes = minutes;
                            minutes = Double.NaN;
                            break BigBoss;
                        }
                        /* ------------------------------------------------
                         * ANALYSE DU SYMBOLE SUIVANT LES PRÉSUMÉES MINUTES
                         * ------------------------------------------------
                         * Aucun symbole n'a été trouvé. Les minutes étaient-elles attendues?
                         * Si oui, on les acceptera et on tentera de lire les secondes. Si non,
                         * on retourne le texte lu dans le buffer et on termine la lecture.
                         */
                        default: {
                            if (width1!=0) break; // Continue outer switch
                            // fall through
                        }
                        /* ------------------------------------------------
                         * ANALYSE DU SYMBOLE SUIVANT LES PRÉSUMÉES MINUTES
                         * ------------------------------------------------
                         * Au lieu des minutes, le symbole lu est celui des degrés. On considère
                         * qu'il appartient au prochain angle. On retournera donc le texte lu dans
                         * le buffer et on terminera la lecture.
                         */
                        case DEGREES_FIELD: {
                            pos.setIndex(indexStartField);
                            minutes=Double.NaN;
                            break BigBoss;
                        }
                        /* ------------------------------------------------
                         * ANALYSE DU SYMBOLE SUIVANT LES PRÉSUMÉES MINUTES
                         * ------------------------------------------------
                         * Après les minutes (qu'on accepte), on a trouvé le préfix du prochain
                         * angle à lire. On retourne ce préfix dans le buffer et on considère la
                         * lecture terminée.
                         */
                        case PREFIX_FIELD: {
                            pos.setIndex(indexEndField);
                            break BigBoss;
                        }
                    }
                    swapDM=false;
                    // fall through
                }
                /* ----------------------------------------------
                 * ANALYSE DU SYMBOLE SUIVANT LES PRÉSUMÉS DEGRÉS
                 * ----------------------------------------------
                 * Un symbole des minutes a été trouvé au lieu du symbole des degrés attendu.
                 * On fera donc la modification dans les variables 'degrés' et 'minutes'. Ces
                 * minutes sont peut-être suivies des secondes. On tentera donc de lire le
                 * prochain nombre.
                 */
                case MINUTES_FIELD: {
                    if (swapDM) {
                        minutes = degrees;
                        degrees = Double.NaN;
                    }
                    final int indexStartField = index = pos.getIndex();
                    while (index<length && Character.isSpaceChar(source.charAt(index))) {
                        index++;
                    }
                    if (!spaceAsSeparator && index!=indexStartField) {
                        break BigBoss;
                    }
                    pos.setIndex(index);
                    fieldObject = numberFormat.parse(source, pos);
                    if (fieldObject == null) {
                        pos.setIndex(indexStartField);
                        break;
                    }
                    indexEndField = pos.getIndex();
                    secondes = fieldObject.doubleValue();
                    switch (skipSuffix(source, pos, (width2!=0) ? MINUTES_FIELD : PREFIX_FIELD)) {
                        /* -------------------------------------------------
                         * ANALYSE DU SYMBOLE SUIVANT LES PRÉSUMÉES SECONDES
                         * -------------------------------------------------
                         * Un symbole des secondes explicite a été trouvée.
                         * La lecture est donc terminée.
                         */
                        case SECONDS_FIELD: {
                            break;
                        }
                        /* -------------------------------------------------
                         * ANALYSE DU SYMBOLE SUIVANT LES PRÉSUMÉES SECONDES
                         * -------------------------------------------------
                         * Aucun symbole n'a été trouvée. Attendait-on des secondes? Si oui, les
                         * secondes seront acceptées. Sinon, elles seront retournées au buffer.
                         */
                        default: {
                            if (width2 != 0) break;
                            // fall through
                        }
                        /* -------------------------------------------------
                         * ANALYSE DU SYMBOLE SUIVANT LES PRÉSUMÉES SECONDES
                         * -------------------------------------------------
                         * Au lieu des degrés, on a trouvé un symbole des minutes ou des
                         * secondes. On renvoie donc le nombre et son symbole dans le buffer.
                         */
                        case MINUTES_FIELD:
                        case DEGREES_FIELD: {
                            pos.setIndex(indexStartField);
                            secondes = Double.NaN;
                            break;
                        }
                        /* -------------------------------------------------
                         * ANALYSE DU SYMBOLE SUIVANT LES PRÉSUMÉES SECONDES
                         * -------------------------------------------------
                         * Après les secondes (qu'on accepte), on a trouvé le préfix du prochain
                         * angle à lire. On retourne ce préfix dans le buffer et on considère la
                         * lecture terminée.
                         */
                        case PREFIX_FIELD: {
                            pos.setIndex(indexEndField);
                            break BigBoss;
                        }
                    }
                    break;
                }
            }
        }
        ////////////////////////////////////////////////////////////////////
        // BLOC B: Prend en compte l'éventualité ou le séparateur décimal //
        //         aurrait été absent, puis calcule l'angle en degrés.    //
        ////////////////////////////////////////////////////////////////////
        if (minutes<0) {
            secondes = -secondes;
        }
        if (degrees<0) {
            minutes = -minutes;
            secondes = -secondes;
        }
        if (!decimalSeparator) {
            final double facteur = XMath.pow10(widthDecimal);
            if (width2!=0) {
                if (suffix1==null && Double.isNaN(secondes)) {
                    if (suffix0==null && Double.isNaN(minutes)) {
                        degrees /= facteur;
                    } else {
                        minutes /= facteur;
                    }
                } else {
                    secondes /= facteur;
                }
            } else if (Double.isNaN(secondes)) {
                if (width1!=0) {
                    if (suffix0==null && Double.isNaN(minutes)) {
                        degrees /= facteur;
                    } else {
                        minutes /= facteur;
                    }
                } else if (Double.isNaN(minutes)) {
                    degrees /= facteur;
                }
            }
        }
        /*
         * S'il n'y a rien qui permet de séparer les degrés des minutes (par exemple si
         * le patron est "DDDMMmmm"), alors la variable 'degrés' englobe à la fois les
         * degrés, les minutes et d'éventuelles secondes. On applique une correction ici.
         */
        if (suffix1==null && width2!=0 && Double.isNaN(secondes)) {
            double facteur = XMath.pow10(width2);
            if (suffix0==null && width1!=0 && Double.isNaN(minutes)) {
                ///////////////////
                //// DDDMMSS.s ////
                ///////////////////
                secondes = degrees;
                minutes  = (int) (degrees/facteur); // Arrondie vers 0
                secondes -= minutes*facteur;
                facteur  = XMath.pow10(width1);
                degrees   = (int) (minutes/facteur); // Arrondie vers 0
                minutes -= degrees*facteur;
            } else {
                ////////////////////
                //// DDD°MMSS.s ////
                ////////////////////
                secondes = minutes;
                minutes = (int) (minutes/facteur); // Arrondie vers 0
                secondes -= minutes*facteur;
            }
        } else if (suffix0==null && width1!=0 && Double.isNaN(minutes)) {
            /////////////////
            //// DDDMM.m ////
            /////////////////
            final double facteur = XMath.pow10(width1);
            minutes = degrees;
            degrees = (int) (degrees/facteur); // Arrondie vers 0
            minutes -= degrees*facteur;
        }
        pos.setErrorIndex(-1);
        if ( Double.isNaN(degrees))  degrees  = 0;
        if (!Double.isNaN(minutes))  degrees += minutes/60;
        if (!Double.isNaN(secondes)) degrees += secondes/3600;
        /////////////////////////////////////////////////////
        // BLOC C: Vérifie maintenant si l'angle ne serait //
        //         pas suivit d'un symbole N, S, E ou W.   //
        /////////////////////////////////////////////////////
        for (int index=pos.getIndex(); index<length; index++) {
            final char c = source.charAt(index);
            switch (Character.toUpperCase(c)) {
                case NORTH: pos.setIndex(index+1); return new Latitude ( degrees);
                case SOUTH: pos.setIndex(index+1); return new Latitude (-degrees);
                case EAST : pos.setIndex(index+1); return new Longitude( degrees);
                case WEST : pos.setIndex(index+1); return new Longitude(-degrees);
            }
            if (!Character.isSpaceChar(c)) {
                break;
            }
        }
        return new Angle(degrees);
    }

    /**
     * Parses a string as an angle.
     *
     * @param  source The string to parse.
     * @return The parsed string as an {@link Angle}, {@link Latitude}
     *         or {@link Longitude} object.
     * @throws ParseException if the string has not been fully parsed.
     */
    public Angle parse(final String source) throws ParseException {
        final ParsePosition pos = new ParsePosition(0);
        final Angle angle = parse(source, pos, true);
        final int  length = source.length();
        final int  origin = pos.getIndex();
        for (int index=origin; index<length; index++) {
            if (!Character.isWhitespace(source.charAt(index))) {
                index = Math.max(origin, pos.getErrorIndex());
                throw new ParseException(LoggedFormat.formatUnparsable(source, 0, index, null), index);
            }
        }
        return angle;
    }

    /**
     * Parses a substring as an angle. Default implementation invokes
     * {@link #parse(String, ParsePosition)}.
     *
     * @param source A String whose beginning should be parsed.
     * @param pos    Position where to start parsing.
     * @return       The parsed string as an {@link Angle},
     *               {@link Latitude} or {@link Longitude} object.
     */
    public Angle parseObject(final String source, final ParsePosition pos) {
        return parse(source, pos);
    }

    /**
     * Parses a string as an object. Default implementation invokes
     * {@link #parse(String)}.
     *
     * @param  source The string to parse.
     * @return The parsed string as an {@link Angle}, {@link Latitude} or
     *        {@link Longitude} object.
     * @throws ParseException if the string has not been fully parsed.
     */
    @Override
    public Angle parseObject(final String source) throws ParseException {
        return parse(source);
    }

    /**
     * Interprète une chaîne de caractères qui devrait représenter un nombre.
     * Cette méthode est utile pour lire une altitude après les angles.
     *
     * @param  source Chaîne de caractères à interpréter.
     * @param  pos    Position à partir d'où commencer l'interprétation
     *                de la chaîne {@code source}.
     * @return Le nombre lu comme objet {@link Number}.
     */
    final Number parseNumber(final String source, final ParsePosition pos) {
        return numberFormat.parse(source, pos);
    }

    /**
     * Returns a "hash value" for this object.
     */
    @Override
    public synchronized int hashCode() {
        int c = 78236951;
        if (decimalSeparator) c^= 0xFF;
        if (prefix  !=null)   c^=         prefix.hashCode();
        if (suffix0 !=null)   c = c*37 + suffix0.hashCode();
        if (suffix1 !=null)   c^= c*37 + suffix1.hashCode();
        if (suffix2 !=null)   c^= c*37 + suffix2.hashCode();
        return c ^ (((((width0 << 8) ^ width1) << 8) ^ width2) << 8) ^ widthDecimal;
    }

    /**
     * Compares this format with the specified object for equality.
     */
    @Override
    public synchronized boolean equals(final Object obj) {
        // On ne peut pas synchroniser "obj" si on ne veut
        // pas risquer un "deadlock". Voir RFE #4210659.
        if (obj==this) {
            return true;
        }
        if (obj!=null && getClass().equals(obj.getClass())) {
            final  AngleFormat cast = (AngleFormat) obj;
            return width0           == cast.width0            &&
                   width1           == cast.width1            &&
                   width2           == cast.width2            &&
                   widthDecimal     == cast.widthDecimal      &&
                   decimalSeparator == cast.decimalSeparator  &&
                   Utilities.equals(prefix,    cast.prefix )  &&
                   Utilities.equals(suffix0,   cast.suffix0)  &&
                   Utilities.equals(suffix1,   cast.suffix1)  &&
                   Utilities.equals(suffix2,   cast.suffix2)  &&
                   Utilities.equals(numberFormat.getDecimalFormatSymbols(),
                               cast.numberFormat.getDecimalFormatSymbols());
        } else {
            return false;
        }
    }

    /**
     * Returns a string representation of this object.
     */
    @Override
    public String toString() {
        return Classes.getShortClassName(this) + '[' + toPattern() + ']';
    }
}

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
package org.geotools.io;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.text.StyleConstants;

import org.geotools.util.Utilities;
import org.geotools.resources.XArray;


/**
 * A character stream that can be used to format tables. Columns are separated
 * by tabulations (<code>'\t'</code>) and rows are separated by line terminators
 * (<code>'\r'</code>, <code>'\n'</code> or <code>"\r\n"</code>). Every table's
 * cells are stored in memory until {@link #flush()} is invoked. When invoked,
 * {@link #flush()} copy cell's contents to the underlying stream while replacing
 * tabulations by some amount of spaces. The exact number of spaces is computed
 * from cell's widths. {@code TableWriter} produces correct output when
 * displayed with a monospace font.
 * <br><br>
 * For example, the following code...
 *
 * <blockquote><pre>
 *     TableWriter out = new TableWriter(new OutputStreamWriter(System.out), 3);
 *     out.write("Prénom\tNom\n");
 *     out.nextLine('-');
 *     out.write("Idéphonse\tLaporte\nSarah\tCoursi\nYvan\tDubois");
 *     out.flush();
 * </pre></blockquote>
 *
 * ...produces the following output:
 *
 * <blockquote><pre>
 *      Prénom      Nom
 *      ---------   -------
 *      Idéphonse   Laporte
 *      Sarah       Coursi
 *      Yvan        Dubois
 * </pre></blockquote>
 *
 * @since 2.0
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class TableWriter extends FilterWriter {
    /**
     * A possible value for cell alignment. This specifies that the text is aligned
     * to the left indent and extra whitespace should be placed on the right.
     */
    public static final int ALIGN_LEFT = StyleConstants.ALIGN_LEFT;

    /**
     * A possible value for cell alignment. This specifies that the text is aligned
     * to the right indent and extra whitespace should be placed on the left.
     */
    public static final int ALIGN_RIGHT = StyleConstants.ALIGN_RIGHT;

    /**
     * A possible value for cell alignment. This specifies that the text is aligned
     * to the center and extra whitespace should be placed equally on the left and right.
     */
    public static final int ALIGN_CENTER = StyleConstants.ALIGN_CENTER;

    /**
     * A column separator for {@linkplain #TableWriter(Writer,String) constructor}.
     *
     * @since 2.5
     */
    public static final String SINGLE_VERTICAL_LINE = " \u2502 ";

    /**
     * A column separator for {@linkplain #TableWriter(Writer,String) constructor}.
     *
     * @since 2.5
     */
    public static final String DOUBLE_VERTICAL_LINE = " \u2551 ";

    /**
     * A line separator for {@plain #nextLine(char)}.
     *
     * @since 2.5
     */
    public static final char SINGLE_HORIZONTAL_LINE = '\u2500';

    /**
     * A line separator for {@plain #nextLine(char)}.
     *
     * @since 2.5
     */
    public static final char DOUBLE_HORIZONTAL_LINE = '\u2550';

    /**
     * Drawing-box characters. The last two characters
     * are horizontal and vertical line respectively.
     */
    private static final char[][] BOX = new char[][] {
        {// [0000]: single horizontal, single vertical
            '\u250C','\u252C','\u2510',
            '\u251C','\u253C','\u2524',
            '\u2514','\u2534','\u2518',
            '\u2500','\u2502'
        },
        {// [0001]: single horizontal, double vertical
            '\u2553','\u2565','\u2556',
            '\u255F','\u256B','\u2562',
            '\u2559','\u2568','\u255C',
            '\u2500','\u2551'
        },
        {// [0010]: double horizontal, single vertical
            '\u2552','\u2564','\u2555',
            '\u255E','\u256A','\u2561',
            '\u2558','\u2567','\u255B',
            '\u2550','\u2502'
        },
        {// [0011]: double horizontal, double vertical
            '\u2554','\u2566','\u2557',
            '\u2560','\u256C','\u2563',
            '\u255A','\u2569','\u255D',
            '\u2550','\u2551'
        },
        {// [0100]: ASCII characters only
            '+','+','+',
            '+','+','+',
            '+','+','+',
            '-','|'
        }
    };

    /**
     * Default character for space.
     */
    private static final char SPACE = ' ';

    /**
     * Temporary string buffer. This buffer contains only one cell's content.
     */
    private final StringBuilder buffer = new StringBuilder();

    /**
     * List of {@link Cell} objects, from left to right and top to bottom.
     * By convention, a {@code null} value or a {@link Cell} object
     * with <code>{@link Cell#text}==null</code> are move to the next line.
     */
    private final List<Cell> cells = new ArrayList<Cell>();

    /**
     * Alignment for current and next cells.
     */
    private int alignment = ALIGN_LEFT;

    /**
     * Column position of the cell currently being written. The field
     * is incremented each time {@link #nextColumn()} is invoked.
     */
    private int column;

    /**
     * Line position of the cell currently being written. The field
     * is incremented each time {@link #nextLine()} is invoked.
     */
    private int row;

    /**
     * Maximum width for each columns. This array's length must
     * be equals to the number of columns in this table.
     */
    private int width[] = new int[0];

    /**
     * The column separator.
     */
    private final String separator;

    /**
     * The left table border.
     */
    private final String leftBorder;

    /**
     * The right table border.
     */
    private final String rightBorder;

    /**
     * Tells if cells can span more than one line. If {@code true}, then EOL characters likes
     * {@code '\n'} move to the next line <em>inside</em> the current cell. If {@code false},
     * then EOL characters move to the next table's row. Default value is {@code false}.
     */
    private boolean multiLinesCells;

    /**
     * {@code true} if this {@code TableWriter} has been constructed with the no-arg constructor.
     */
    private final boolean stringOnly;

    /**
     * Tells if the next '\n' character must be ignored. This field is
     * used in order to avoid writing two EOL in place of {@code "\r\n"}.
     */
    private boolean skipCR;

    /**
     * Creates a new table writer with a default column separator.
     * <p>
     * <b>Note:</b> this writer may produces bad output on Windows console, unless the underlying
     * stream use the correct codepage (e.g. {@code OutputStreamWriter(System.out, "Cp437")}).
     * To display the appropriate codepage for a Windows NT console, type {@code chcp} on the
     * command line.
     *
     * @param out Writer object to provide the underlying stream, or {@code null} if there is no
     *        underlying stream. If {@code out} is null, then the {@link #toString} method is the
     *        only way to get the table's content.
     */
    public TableWriter(final Writer out) {
        super(out!=null ? out : new StringWriter());
        stringOnly  = (out==null);
        leftBorder  =  "\u2551 ";
        rightBorder = " \u2551" ;
        separator   = " \u2502 ";
    }

    /**
     * Creates a new table writer with the specified amount of spaces as column separator.
     *
     * @param out Writer object to provide the underlying stream, or {@code null} if there is no
     *        underlying stream. If {@code out} is null, then the {@link #toString} method is the
     *        only way to get the table's content.
     * @param spaces Amount of white spaces to use as column separator.
     */
    public TableWriter(final Writer out, final int spaces) {
        this(out, Utilities.spaces(spaces));
    }

    /**
     * Creates a new table writer with the specified column separator.
     *
     * @param out Writer object to provide the underlying stream, or {@code null} if there is no
     *        underlying stream. If {@code out} is null, then the {@link #toString} method is the
     *        only way to get the table's content.
     * @param separator String to write between columns. Drawing box characters are treated
     *        specially. For example {@code " \\u2502 "} can be used for a single-line box.
     *
     * @see #SINGLE_VERTICAL_LINE
     * @see #DOUBLE_VERTICAL_LINE
     */
    public TableWriter(final Writer out, final String separator) {
        super(out!=null ? out : new StringWriter());
        stringOnly = (out == null);
        final int length = separator.length();
        int lower = 0;
        int upper = length;
        while (lower<length && Character.isSpaceChar(separator.charAt(lower  ))) lower++;
        while (upper>0      && Character.isSpaceChar(separator.charAt(upper-1))) upper--;
        this.leftBorder  = separator.substring(lower);
        this.rightBorder = separator.substring(0, upper);
        this.separator   = separator;
    }

    /**
     * Writes a border or a corner to the specified stream.
     *
     * @param out              The destination stream.
     * @param horizontalBorder -1 for left border, +1 for right border,  0 for center.
     * @param verticalBorder   -1 for top  border, +1 for bottom border, 0 for center.
     * @param horizontalChar   Character to use for horizontal line.
     * @throws IOException     if the writting operation failed.
     */
    private void writeBorder(final Writer out,
                             final int horizontalBorder,
                             final int verticalBorder,
                             final char horizontalChar) throws IOException
    {
        /*
         * Obtient les ensembles de caractères qui
         * conviennent pour la ligne horizontale.
         */
        int boxCount = 0;
        final char[][] box = new char[BOX.length][];
        for (int i=0; i<BOX.length; i++) {
            if (BOX[i][9] == horizontalChar) {
                box[boxCount++] = BOX[i];
            }
        }
        /*
         * Obtient une chaine contenant les lignes verticales à
         * dessiner à gauche, à droite ou au centre de la table.
         */
        final String border;
        switch (horizontalBorder) {
            case -1: border = leftBorder;  break;
            case +1: border = rightBorder; break;
            case  0: border = separator;   break;
            default: throw new IllegalArgumentException(String.valueOf(horizontalBorder));
        }
        if (verticalBorder<-1 || verticalBorder>+1) {
            throw new IllegalArgumentException(String.valueOf(verticalBorder));
        }
        /*
         * Remplace les espaces par la ligne horizontale,
         * et les lignes verticales par une intersection.
         */
        final int index = (horizontalBorder+1) + (verticalBorder+1)*3;
        final int borderLength = border.length();
        for (int i=0; i<borderLength; i++) {
            char c=border.charAt(i);
            if (Character.isSpaceChar(c)) {
                c = horizontalChar;
            } else {
                for (int j=0; j<boxCount; j++) {
                    if (box[j][10] == c) {
                        c = box[j][index];
                        break;
                    }
                }
            }
            out.write(c);
        }
    }

    /**
     * Sets the desired behavior for EOL and tabulations characters.
     * <ul>
     *   <li>If {@code true}, EOL (<code>'\r'</code>, <code>'\n'</code> or
     *       <code>"\r\n"</code>) and tabulations (<code>'\t'</code>) characters
     *       are copied straight into the current cell, which mean that next write
     *       operations will continue inside the same cell.</li>
     *   <li>If {@code false}, then tabulations move to next column and EOL move
     *       to the first cell of next row (i.e. tabulation and EOL are equivalent to
     *       {@link #nextColumn()} and {@link #nextLine()} calls respectively).</li>
     * </ul>
     * The default value is {@code false}.
     *
     * @param multiLines {@code true} true if EOL are used for line feeds inside
     *        current cells, or {@code false} if EOL move to the next row.
     */
    public void setMultiLinesCells(final boolean multiLines) {
        synchronized (lock) {
            multiLinesCells = multiLines;
        }
    }

    /**
     * Tells if EOL characters are used for line feeds inside current cells.
     *
     * @return {@code true} if EOL characters are to be write inside the cell.
     */
    public boolean isMultiLinesCells() {
        synchronized (lock) {
            return multiLinesCells;
        }
    }

    /**
     * Sets the alignment for all cells in the specified column. This method
     * overwrite the alignment for all previous cells in the specified column.
     *
     * @param column The 0-based column number.
     * @param alignment Cell alignment. Must be {@link #ALIGN_LEFT}
     *        {@link #ALIGN_RIGHT} or {@link #ALIGN_CENTER}.
     */
    public void setColumnAlignment(final int column, final int alignment) {
        if (alignment != ALIGN_LEFT  &&
            alignment != ALIGN_RIGHT &&
            alignment != ALIGN_CENTER)
        {
            throw new IllegalArgumentException(String.valueOf(alignment));
        }
        synchronized (lock) {
            int current = 0;
            for (final Cell cell : cells) {
                if (cell==null || cell.text==null) {
                    current = 0;
                    continue;
                }
                if (current == column) {
                    cell.alignment = alignment;
                }
                current++;
            }
        }
    }

    /**
     * Sets the alignment for current and next cells. Change to the
     * alignment doesn't affect the alignment of previous cells and
     * previous rows. The default alignment is {@link #ALIGN_LEFT}.
     *
     * @param alignment Cell alignment. Must be {@link #ALIGN_LEFT}
     *        {@link #ALIGN_RIGHT} or {@link #ALIGN_CENTER}.
     */
    public void setAlignment(final int alignment) {
        if (alignment != ALIGN_LEFT  &&
            alignment != ALIGN_RIGHT &&
            alignment != ALIGN_CENTER)
        {
            throw new IllegalArgumentException(String.valueOf(alignment));
        }
        synchronized (lock) {
            this.alignment = alignment;
        }
    }

    /**
     * Returns the alignment for current and next cells.
     *
     * @return Cell alignment: {@link #ALIGN_LEFT} (the default),
     *         {@link #ALIGN_RIGHT} or {@link #ALIGN_CENTER}.
     */
    public int getAlignment() {
        synchronized (lock) {
            return alignment;
        }
    }

    /**
     * Returns the number of rows in this table. This count is reset to 0 by {@link #flush}.
     *
     * @return The number of rows in this table.
     *
     * @since 2.5
     */
    public int getRowCount() {
        int count = row;
        if (column != 0) {
            count++;
        }
        return count;
    }

    /**
     * Returns the number of columns in this table.
     *
     * @return The number of colunms in this table.
     *
     * @since 2.5
     */
    public int getColumnCount() {
        return width.length;
    }

    /**
     * Write a single character. If {@link #isMultiLinesCells()}
     * is {@code false} (which is the default), then:
     * <ul>
     *   <li>Tabulations (<code>'\t'</code>) are replaced by {@link #nextColumn()} invocations.</li>
     *   <li>Line separators (<code>'\r'</code>, <code>'\n'</code> or <code>"\r\n"</code>)
     *       are replaced by {@link #nextLine()} invocations.</li>
     * </ul>
     *
     * @param c Character to write.
     */
    @Override
    public void write(final int c) {
        synchronized (lock) {
            if (!multiLinesCells) {
                switch (c) {
                    case '\t': {
                        nextColumn();
                        skipCR = false;
                        return;
                    }
                    case '\r': {
                        nextLine();
                        skipCR = true;
                        return;
                    }
                    case '\n': {
                        if (!skipCR) {
                            nextLine();
                        }
                        skipCR = false;
                        return;
                    }
                }
            }
            if (c<Character.MIN_VALUE || c>Character.MAX_VALUE) {
                throw new IllegalArgumentException(String.valueOf(c));
            }
            buffer.append((char)c);
            skipCR = false;
        }
    }

    /**
     * Writes a string. Tabulations and line separators are interpreted as by {@link #write(int)}.
     *
     * @param string String to write.
     */
    @Override
    public void write(final String string) {
        write(string, 0, string.length());
    }

    /**
     * Writes a portion of a string. Tabulations and line
     * separators are interpreted as by {@link #write(int)}.
     *
     * @param string String to write.
     * @param offset Offset from which to start writing characters.
     * @param length Number of characters to write.
     */
    @Override
    public void write(final String string, int offset, int length) {
        if (offset<0 || length<0 || (offset+length)>string.length()) {
            throw new IndexOutOfBoundsException();
        }
        if (length == 0) {
            return;
        }
        synchronized (lock) {
            if (skipCR && string.charAt(offset)=='\n') {
                offset++;
                length--;
            }
            if (!multiLinesCells) {
                int upper = offset;
                for (; length!=0; length--) {
                    switch (string.charAt(upper++)) {
                        case '\t': {
                            buffer.append(string.substring(offset, upper-1));
                            nextColumn();
                            offset = upper;
                            break;
                        }
                        case '\r': {
                            buffer.append(string.substring(offset, upper-1));
                            nextLine();
                            if (length!=0 && string.charAt(upper)=='\n') {
                                upper++;
                                length--;
                            }
                            offset = upper;
                            break;
                        }
                        case '\n': {
                            buffer.append(string.substring(offset, upper-1));
                            nextLine();
                            offset = upper;
                            break;
                        }
                    }
                }
                length = upper-offset;
            }
            skipCR = (string.charAt(offset+length-1) == '\r');
            buffer.append(string.substring(offset, offset+length));
        }
    }

    /**
     * Writes an array of characters. Tabulations and line
     * separators are interpreted as by {@link #write(int)}.
     *
     * @param cbuf Array of characters to be written.
     */
    @Override
    public void write(final char cbuf[]) {
        write(cbuf, 0, cbuf.length);
    }

    /**
     * Writes a portion of an array of characters. Tabulations and
     * line separators are interpreted as by {@link #write(int)}.
     *
     * @param cbuf   Array of characters.
     * @param offset Offset from which to start writing characters.
     * @param length Number of characters to write.
     */
    @Override
    public void write(final char cbuf[], int offset, int length) {
        if (offset<0 || length<0 || (offset+length)>cbuf.length) {
            throw new IndexOutOfBoundsException();
        }
        if (length == 0) {
            return;
        }
        synchronized (lock) {
            if (skipCR && cbuf[offset]=='\n') {
                offset++;
                length--;
            }
            if (!multiLinesCells) {
                int upper = offset;
                for (; length!=0; length--) {
                    switch (cbuf[upper++]) {
                        case '\t': {
                            buffer.append(cbuf, offset, upper-offset-1);
                            nextColumn();
                            offset = upper;
                            break;
                        }
                        case '\r': {
                            buffer.append(cbuf, offset, upper-offset-1);
                            nextLine();
                            if (length!=0 && cbuf[upper]=='\n') {
                                upper++;
                                length--;
                            }
                            offset = upper;
                            break;
                        }
                        case '\n': {
                            buffer.append(cbuf, offset, upper-offset-1);
                            nextLine();
                            offset = upper;
                            break;
                        }
                    }
                }
                length = upper-offset;
            }
            skipCR = (cbuf[offset+length-1] == '\r');
            buffer.append(cbuf, offset, length);
        }
    }

    /**
     * Writes an horizontal separator.
     */
    public void writeHorizontalSeparator() {
        synchronized (lock) {
            if (column!=0 || buffer.length()!=0) {
                nextLine();
            }
            nextLine(SINGLE_HORIZONTAL_LINE);
        }
    }

    /**
     * Moves one column to the right. Next write operations will occur in a new cell on the
     * same row.
     */
    public void nextColumn() {
        nextColumn(SPACE);
    }

    /**
     * Moves one column to the right. Next write operations will occur in a new cell on the
     * same row. This method fill every remaining space in the current cell with the specified
     * character. For example calling {@code nextColumn('*')} from the first character of a cell
     * is a convenient way to put a pad value in this cell.
     *
     * @param fill Character filling the cell (default to whitespace).
     */
    public void nextColumn(final char fill) {
        synchronized (lock) {
            final String cellText = buffer.toString();
            cells.add(new Cell(cellText, alignment, fill));
            if (column >= width.length) {
                width = XArray.resize(width, column+1);
            }
            int length = 0;
            final StringTokenizer tk = new StringTokenizer(cellText, "\r\n");
            while (tk.hasMoreTokens()) {
                final int lg = tk.nextToken().length();
                if (lg > length) {
                    length = lg;
                }
            }
            if (length>width[column]) {
                width[column] = length;
            }
            column++;
            buffer.setLength(0);
        }
    }

    /**
     * Moves to the first column on the next row.
     * Next write operations will occur on a new row.
     */
    public void nextLine() {
        nextLine(SPACE);
    }

    /**
     * Moves to the first column on the next row. Next write operations will occur on a new
     * row. This method fill every remaining cell in the current row with the specified character.
     * Calling {@code nextLine('-')} from the first column of a row is a convenient way to fill
     * this row with a line separator.
     *
     * @param fill Character filling the rest of the line (default to whitespace).
     *             This caracter may be use as a row separator.
     *
     * @see #SINGLE_HORIZONTAL_LINE
     * @see #DOUBLE_HORIZONTAL_LINE
     */
    public void nextLine(final char fill) {
        synchronized (lock) {
            if (buffer.length() != 0) {
                nextColumn(fill);
            }
            assert buffer.length() == 0;
            cells.add(!Character.isSpaceChar(fill) ? new Cell(null, alignment, fill) : null);
            column = 0;
            row++;
        }
    }

    /**
     * Flushs the table content to the underlying stream. This method should not be called
     * before the table is completed (otherwise, columns may have the wrong width).
     *
     * @throws IOException if an output operation failed.
     */
    @Override
    public void flush() throws IOException {
        synchronized (lock) {
            if (buffer.length() != 0) {
                nextLine();
                assert buffer.length() == 0;
            }
            flushTo(out);
            row = column = 0;
            cells.clear();
            if (!(out instanceof TableWriter)) {
                /*
                 * Flush only if this table is not included in an outer (bigger) table.
                 * This is because flushing the outer table would break its formatting.
                 */
                out.flush();
            }
        }
    }

    /**
     * Flushs the table content and close the underlying stream.
     *
     * @throws IOException if an output operation failed.
     */
    @Override
    public void close() throws IOException {
        synchronized (lock) {
            flush();
            out.close();
        }
    }

    /**
     * Écrit vers le flot spécifié toutes les cellules qui avaient été disposées
     * dans le tableau. Ces cellules seront automatiquement alignées en colonnes.
     * Cette méthode peut être appelée plusieurs fois pour écrire le même tableau
     * par exemple vers plusieurs flots.
     *
     * @param  out Flot vers où écrire les données.
     * @throws IOException si une erreur est survenue lors de l'écriture dans {@code out}.
     */
    private void flushTo(final Writer out) throws IOException {
        final String columnSeparator = this.separator;
        final String   lineSeparator = System.getProperty("line.separator", "\n");
        final Cell[]     currentLine = new Cell[width.length];
        final int          cellCount = cells.size();
        for (int cellIndex=0; cellIndex<cellCount; cellIndex++) {
            /*
             * Copie dans  {@code currentLine}  toutes les données qui seront à écrire
             * sur la ligne courante de la table. Ces données excluent le {@code null}
             * terminal.  La liste {@code currentLine} ne contiendra donc initialement
             * aucun élément nul, mais ses éléments seront progressivement modifiés (et mis
             * à {@code null}) pendant l'écriture de la ligne dans la boucle qui suit.
             */
            Cell lineFill = null;
            int currentCount = 0;
            do {
                final Cell cell = cells.get(cellIndex);
                if (cell == null) {
                    break;
                }
                if (cell.text == null) {
                    lineFill = new Cell("", cell.alignment, cell.fill);
                    break;
                }
                currentLine[currentCount++] = cell;
            }
            while (++cellIndex < cellCount);
            Arrays.fill(currentLine, currentCount, currentLine.length, lineFill);
            /*
             * La boucle suivante sera exécutée tant qu'il reste des lignes à écrire
             * (c'est-à-dire tant qu'au moins un élément de {@code currentLine}
             * est non-nul). Si une cellule contient un texte avec des caractères EOL,
             * alors cette cellule devra s'écrire sur plusieurs lignes dans la cellule
             * courante.
             */
            while (!isEmpty(currentLine)) {
                for (int j=0; j<currentLine.length; j++) {
                    final boolean isFirstColumn = (j   == 0);
                    final boolean isLastColumn  = (j+1 == currentLine.length);
                    final Cell cell = currentLine[j];
                    final int cellWidth = width[j];
                    if (cell == null) {
                        if (isFirstColumn) {
                            out.write(leftBorder);
                        }
                        repeat(out, SPACE, cellWidth);
                        out.write(isLastColumn ? rightBorder : columnSeparator);
                        continue;
                    }
                    String cellText = cell.toString();
                    int endCR = cellText.indexOf('\r');
                    int endLF = cellText.indexOf('\n');
                    int end   = (endCR<0) ? endLF : (endLF<0) ? endCR : Math.min(endCR,endLF);
                    if (end >= 0) {
                        /*
                         * Si un retour chariot a été trouvé, n'écrit que la première
                         * ligne de la cellule. L'élément {@code currentLine[j]}
                         * sera modifié pour ne contenir que les lignes restantes qui
                         * seront écrites lors d'un prochain passage dans la boucle.
                         */
                        int top = end+1;
                        if (endCR>=0 && endCR+1==endLF) top++;
                        int scan = top;
                        final int textLength = cellText.length();
                        while (scan<textLength && Character.isWhitespace(cellText.charAt(scan))) {
                            scan++;
                        }
                        currentLine[j] = (scan<textLength) ? cell.substring(top) : null;
                        cellText = cellText.substring(0, end);
                    }
                    else currentLine[j] = null;
                    final int textLength = cellText.length();
                    /*
                     * Si la cellule à écrire est en fait une bordure,
                     * on fera un traitement spécial pour utiliser les
                     * caractres de jointures {@link #BOX}.
                     */
                    if (currentCount == 0) {
                        assert textLength == 0;
                        final int verticalBorder;
                        if      (cellIndex==0)           verticalBorder = -1;
                        else if (cellIndex>=cellCount-1) verticalBorder = +1;
                        else                             verticalBorder =  0;
                        if (isFirstColumn) {
                            writeBorder(out, -1, verticalBorder, cell.fill);
                        }
                        repeat(out, cell.fill, cellWidth);
                        writeBorder(out, isLastColumn ? +1 : 0, verticalBorder, cell.fill);
                        continue;
                    }
                    /*
                     * Si la cellule n'est pas une bordure, il s'agit
                     * d'une cellule "normale".  Procde maintenant à
                     * l'écriture d'une ligne de la cellule.
                     */
                    if (isFirstColumn) {
                        out.write(leftBorder);
                    }
                    final Writer tabExpander = (cellText.indexOf('\t')>=0) ?
                                               new ExpandedTabWriter(out) : out;
                    switch (cell.alignment) {
                        default: {
                            // Should not happen.
                            throw new AssertionError(cell.alignment);
                        }
                        case ALIGN_LEFT: {
                            tabExpander.write(cellText);
                            repeat(tabExpander, cell.fill, cellWidth-textLength);
                            break;
                        }
                        case ALIGN_RIGHT: {
                            repeat(tabExpander, cell.fill, cellWidth-textLength);
                            tabExpander.write(cellText);
                            break;
                        }
                        case ALIGN_CENTER: {
                            final int rightMargin = (cellWidth-textLength)/2;
                            repeat(tabExpander, cell.fill, rightMargin);
                            tabExpander.write(cellText);
                            repeat(tabExpander, cell.fill, (cellWidth-rightMargin)-textLength);
                            break;
                        }
                    }
                    out.write(isLastColumn ? rightBorder : columnSeparator);
                }
                out.write(lineSeparator);
            }
        }
    }

    /**
     * Checks if {@code array} contains only {@code null} elements.
     */
    private static boolean isEmpty(final Object[] array) {
        for (int i=array.length; --i>=0;) {
            if (array[i] != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Repeats a character.
     *
     * @param out   The destination stream.
     * @param car   Character to write (usually ' ').
     * @param count Number of repetition.
     */
    private static void repeat(final Writer out, final char car, int count) throws IOException {
        while (--count >= 0) {
            out.write(car);
        }
    }

    /**
     * Returns the table content as a string.
     */
    @Override
    public String toString() {
        synchronized (lock) {
            int capacity = 2; // Room for EOL.
            for (int i=0; i<width.length; i++) {
                capacity += width[i];
            }
            capacity *= getRowCount();
            final StringWriter writer;
            if (stringOnly) {
                writer = (StringWriter) out;
                final StringBuffer buffer = writer.getBuffer();
                buffer.setLength(0);
                buffer.ensureCapacity(capacity);
            } else {
                writer = new StringWriter(capacity);
            }
            try {
                flushTo(writer);
            } catch (IOException exception) {
                // Should not happen
                throw new AssertionError(exception);
            }
            return writer.toString();
        }
    }

    /**
     * A class wrapping a cell's content and its text's alignment.
     * This class if for internal use only.
     *
     * @version $Id$
     * @author Martin Desruisseaux (IRD)
     */
    private static final class Cell {
        /**
         * The text to write inside the cell.
         */
        public final String text;

        /**
         * The alignment for {@link #text} inside the cell.
         */
        public int alignment;

        /**
         * The fill character, used for filling space inside the cell.
         */
        public final char fill;

        /**
         * Returns a new cell wrapping the specified string with the
         * specified alignment and fill character.
         */
        public Cell(final String text, final int alignment, final char fill) {
            this.text      = text;
            this.alignment = alignment;
            this.fill      = fill;
        }

        /**
         * Returns a new cell which contains substring of this cell.
         */
        public Cell substring(final int lower) {
            return new Cell(text.substring(lower), alignment, fill);
        }

        /**
         * Returns the cell's content.
         */
        @Override
        public String toString() {
            return text;
        }
    }
}

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
package org.geotools.resources;

import java.io.IOException;
import java.util.Enumeration;
import java.lang.reflect.Constructor;
import javax.swing.JTree;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;


/**
 * Bridges to optional dependencies (especially {@code widget-swing} module).
 *
 * @todo Most methods of this class need to move as a {@code Trees} class in a {@code util} module.
 *
 * @since 2.0
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class OptionalDependencies {
    /**
     * Constructor for {@link org.geotools.gui.swing.tree.NamedTreeNode}.
     */
    private static Constructor treeNodeConstructor;

    /**
     * Set to {@code true} if {@link #treeNodeConstructor} can't be obtained.
     */
    private static boolean noNamedTreeNode = false;

    /**
     * Interdit la création d'objets de cette classe.
     */
    private OptionalDependencies() {
    }

    /**
     * Creates an initially empty tree node.
     *
     * @param name   The value to be returned by {@link TreeNode#toString}.
     * @param object The user object to be returned by the tree node. May
     *               or may not be the same than {@code name}.
     * @param allowsChildren if children are allowed.
     */
    public static DefaultMutableTreeNode createTreeNode(final String name,
                                                        final Object object,
                                                        final boolean allowsChildren)
    {
        /*
         * If the "modules/extension/swing-widgets" JAR is in the classpath,  then create an
         * instance of NamedTreeNode (see org.geotools.swing.tree javadoc for an explanation
         * about why the NamedTreeNode workaround is needed).  We use reflection because the
         * swing-widgets module is optional,  so we fallback on the standard Swing object if
         * we can't create an instance of NamedTreeNode.   We will attempt to use reflection
         * only once in order to avoid a overhead if the swing-widgets module is not available.
         *
         * The swing-widgets module contains a "NamedTreeNodeTest" for making sure that the
         * NamedTreeNode instances are properly created.
         *
         * Note: No need to sychronize; this is not a big deal if we make the attempt twice.
         */
        if (!noNamedTreeNode) try {
            if (treeNodeConstructor == null) {
                treeNodeConstructor = Class.forName("org.geotools.gui.swing.tree.NamedTreeNode").
                        getConstructor(new Class[] {String.class, Object.class, Boolean.TYPE});
            }
            return (DefaultMutableTreeNode) treeNodeConstructor.newInstance(
                    new Object[] {name, object, Boolean.valueOf(allowsChildren)});
        } catch (Exception e) {
            /*
             * There is a large amount of checked and unchecked exceptions that the above code
             * may thrown. We catch all of them because a reasonable fallback exists (creation
             * of the default Swing object below).  Note that none of the unchecked exceptions
             * (IllegalArgumentException, NullPointerException...) should occurs, except maybe
             * SecurityException. Maybe we could let the unchecked exceptions propagate...
             */
            noNamedTreeNode = true;
        }
        return new DefaultMutableTreeNode(name, allowsChildren);
    }

    /**
     * Creates a Swing root tree node from a XML root tree node. Together with
     * {@link #toString(TreeNode)}, this method provides a convenient way to print
     * the content of a XML document for debugging purpose.
     * <p>
     * This method should not be defined here, since this class is about optional dependencies.
     * It should be defined in {@link org.geotools.gui.swing.tree.Trees} instead. However we put
     * it here (for now) because it is used in some module that don't want to depend on widgets.
     */
    public static MutableTreeNode xmlToSwing(final Node node) {
        String label = node.getNodeName();
        final String value = node.getNodeValue();
        if (value != null) {
            label += "=\"" + value + '"';
        }
        final DefaultMutableTreeNode root = createTreeNode(label, node, true);
        final NamedNodeMap attributes = node.getAttributes();
        final int length = attributes.getLength();
        for (int i=0; i<length; i++) {
            final Node attribute = attributes.item(i);
            if (attribute != null) {
                label = attribute.getNodeName() + "=\"" + attribute.getNodeValue() + '"';
                root.add(createTreeNode(label, attribute, false));
            }
        }
        for (Node child=node.getFirstChild(); child!=null; child=child.getNextSibling()) {
            root.add(xmlToSwing(child));
        }
        return root;
    }

    /**
     * Returns a copy of the tree starting at the given node.
     *
     * @param  tree The tree to copy (may be {@code null}).
     * @return A mutable copy of the given tree, or {@code null} if the tree was null.
     * @todo Use {@code getUserObject} when we can.
     *
     * @since 2.5
     */
    public static MutableTreeNode copy(final TreeNode node) {
        if (node == null) {
            return null;
        }
        final DefaultMutableTreeNode target = new DefaultMutableTreeNode(
                node.toString(), node.getAllowsChildren());
        final Enumeration children = node.children();
        if (children != null) {
            while (children.hasMoreElements()) {
                final TreeNode child = (TreeNode) children.nextElement();
                target.add(copy(child));
            }
        }
        return target;
    }

    /**
     * Construit une chaîne de caractères qui contiendra le
     * noeud spécifié ainsi que tous les noeuds enfants.
     *
     * @param model  Arborescence à écrire.
     * @param node   Noeud de l'arborescence à écrire.
     * @param buffer Buffer dans lequel écrire le noeud.
     * @param level  Niveau d'indentation (à partir de 0).
     * @param last   Indique si les niveaux précédents sont en train d'écrire leurs derniers items.
     * @return       Le tableau {@code last}, qui peut éventuellement avoir été agrandit.
     */
    private static boolean[] format(final TreeModel model, final Object node,
                                    final Appendable buffer, final int level, boolean[] last,
                                    final String lineSeparator) throws IOException
    {
        for (int i=0; i<level; i++) {
            if (i != level-1) {
                buffer.append(last[i] ? '\u00A0' : '\u2502').append("\u00A0\u00A0\u00A0");
            } else {
                buffer.append(last[i] ? '\u2514' : '\u251C').append("\u2500\u2500\u2500");
            }
        }
        buffer.append(String.valueOf(node)).append(lineSeparator);
        if (level >= last.length) {
            last = XArray.resize(last, level*2);
        }
        final int count = model.getChildCount(node);
        for (int i=0; i<count; i++) {
            last[level] = (i == count-1);
            last = format(model, model.getChild(node,i), buffer, level+1, last, lineSeparator);
        }
        return last;
    }

    /**
     * Writes a graphical representation of the specified tree model in the given buffer.
     * <p>
     * This method should not be defined here, since this class is about optional dependencies.
     * It should be defined in {@link org.geotools.gui.swing.tree.Trees} instead. However we put
     * it here (for now) because it is used in some module that don't want to depend on widgets.
     *
     * @param  tree          The tree to format.
     * @param  buffer        Where to format the tree.
     * @param  lineSeparator The line separator, or {@code null} for the system default.
     * @throws IOException if an error occured while writting in the given buffer.
     *
     * @since 2.5
     */
    public static void format(final TreeModel tree, final Appendable buffer, String lineSeparator)
            throws IOException
    {
        final Object root = tree.getRoot();
        if (root != null) {
            if (lineSeparator == null) {
                lineSeparator = System.getProperty("line.separator", "\n");
            }
            format(tree, root, buffer, 0, new boolean[64], lineSeparator);
        }
    }

    /**
     * Writes a graphical representation of the specified tree in the given buffer.
     * <p>
     * This method should not be defined here, since this class is about optional dependencies.
     * It should be defined in {@link org.geotools.gui.swing.tree.Trees} instead. However we put
     * it here (for now) because it is used in some module that don't want to depend on widgets.
     *
     * @param  node          The root node of the tree to format.
     * @param  buffer        Where to format the tree.
     * @param  lineSeparator The line separator, or {@code null} for the system default.
     * @throws IOException if an error occured while writting in the given buffer.
     *
     * @since 2.5
     */
    public static void format(final TreeNode node, final Appendable buffer, String lineSeparator)
            throws IOException
    {
        format(new DefaultTreeModel(node, true), buffer, lineSeparator);
    }

    /**
     * Returns a graphical representation of the specified tree model. This representation can
     * be printed to the {@linkplain System#out standard output stream} (for example) if it uses
     * a monospaced font and supports unicode.
     * <p>
     * This method should not be defined here, since this class is about optional dependencies.
     * It should be defined in {@link org.geotools.gui.swing.tree.Trees} instead. However we put
     * it here (for now) because it is used in some module that don't want to depend on widgets.
     *
     * @param  tree The tree to format.
     * @return A string representation of the tree, or {@code null} if it doesn't contain any node.
     */
    public static String toString(final TreeModel tree) {
        final Object root = tree.getRoot();
        if (root == null) {
            return null;
        }
        final StringBuilder buffer = new StringBuilder();
        final String lineSeparator = System.getProperty("line.separator", "\n");
        try {
            format(tree, root, buffer, 0, new boolean[64], lineSeparator);
        } catch (IOException e) {
            // Should never happen when writting into a StringBuilder.
            throw new AssertionError(e);
        }
        return buffer.toString();
    }

    /**
     * Returns a graphical representation of the specified tree. This representation can be
     * printed to the {@linkplain System#out standard output stream} (for example) if it uses
     * a monospaced font and supports unicode.
     * <p>
     * This method should not be defined here, since this class is about optional dependencies.
     * It should be defined in {@link org.geotools.gui.swing.tree.Trees} instead. However we put
     * it here (for now) because it is used in some module that don't want to depend on widgets.
     *
     * @param  node The root node of the tree to format.
     * @return A string representation of the tree, or {@code null} if it doesn't contain any node.
     */
    public static String toString(final TreeNode node) {
        return toString(new DefaultTreeModel(node, true));
    }

    /**
     * Display the given tree in a Swing frame. This is a convenience
     * method for debugging purpose only.
     *
     * @param tree The tree to display in a Swing frame.
     * @param title The frame title, or {@code null} if none.
     *
     * @since 2.5
     */
    public static void show(final TreeNode node, final String title) {
        show(new DefaultTreeModel(node, true), title);
    }

    /**
     * Display the given tree in a Swing frame. This is a convenience
     * method for debugging purpose only.
     *
     * @param tree The tree to display in a Swing frame.
     * @param title The frame title, or {@code null} if none.
     *
     * @since 2.5
     */
    public static void show(final TreeModel tree, final String title) {
        final JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new JScrollPane(new JTree(tree)));
        frame.pack();
        frame.setVisible(true);
    }
}

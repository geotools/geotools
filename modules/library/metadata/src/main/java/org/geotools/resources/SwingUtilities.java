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

import java.util.Locale;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.awt.IllegalComponentStateException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.LookAndFeel;

import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;


/**
 * A collection of utility methods for Swing.  All <code>show*</code> methods delegate
 * their work to the corresponding method in {@link JOptionPane}, with two differences:
 *
 * <ul>
 *   <li>{@code SwingUtilities}'s method may be invoked from any thread. If they
 *       are invoked from a non-Swing thread, execution will be delegate to the Swing
 *       thread and the calling thread will block until completion.</li>
 *   <li>If a parent component is a {@link JDesktopPane}, dialogs will be rendered as
 *       internal frames instead of frames.</li>
 * </ul>
 *
 * @since 2.0
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class SwingUtilities {
    /**
     * Do not allow any instance
     * of this class to be created.
     */
    private SwingUtilities() {
    }

    /**
     * Insert a Swing component into a frame. The kind of frame depends on the owner:
     *
     * <ul>
     *   <li>If {@code owner} or one of its parent is a {@link JDesktopPane},
     *       then {@code panel} is added into a {@link JInternalFrame}.</li>
     *   <li>If {@code owner} or one of its parent is a {@link Frame} or a {@link Dialog},
     *       then {@code panel} is added into a {@link JDialog}.</li>
     *   <li>Otherwise, {@code panel} is added into a {@link JFrame}.</li>
     * </ul>
     *
     * @param  owner The frame's owner, or {@code null} if none.
     * @param  panel The panel to insert into a frame.
     * @param  title The frame's title.
     * @param  listener A listener to receives frame events.  If non-null, then this listener will
     *         be registered to whatever kind of frame this method will constructs. In the special
     *         case where this method constructs an {@linkplain JInternalFrame internal frame} and
     *         the {@code listener} is not an instance of {@link javax.swing.event.InternalFrameListener},
     *         then this method will wrap the {@code listener} into an {@code InternalFrameListener}.
     * @return The frame. This frame is not initially visible. The method
     *         {@code Component.setVisible(true)} must be invoked in order to show the frame.
     */
    public static Component toFrame(Component owner,
                                    final JComponent     panel,
                                    final String         title,
                                    final WindowListener listener)
    {
        while (owner != null) {
            if (owner == panel) {
                throw new IllegalArgumentException();
            }
            // NOTE: All 'addFooListener(...)' below ignore null argument. No need to check ourself.
            if (owner instanceof JDesktopPane) {
                final JInternalFrame frame = new JInternalFrame(title, true, true, true, true);
                frame.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
                frame.addInternalFrameListener(InternalWindowListener.wrap(listener));
                ((JDesktopPane) owner).add(frame);
                frame.getContentPane().add(panel);
                frame.pack();
                return frame;
            }
            if (owner instanceof Frame) {
                final JDialog dialog = new JDialog((Frame) owner, title);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.addWindowListener(listener);
                dialog.getContentPane().add(panel);
                dialog.pack();
                return dialog;
            }
            if (owner instanceof Dialog) {
                final JDialog dialog = new JDialog((Dialog) owner, title);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.addWindowListener(listener);
                dialog.getContentPane().add(panel);
                dialog.pack();
                return dialog;
            }
            owner = owner.getParent();
        }
        //
        // Add the panel as a standalone window.
        // This window has its own button on the task bar.
        //
        final JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(listener);
        frame.getContentPane().add(panel);
        frame.pack();
        return frame;
    }

    /**
     * Set the title of the parent frame or internal frame of the specified component.
     */
    public static void setTitle(Component component, final String title) {
        while (component != null) {
            if (component instanceof JInternalFrame) {
                ((JInternalFrame) component).setTitle(title);
            }
            if (component instanceof Frame) {
                ((Frame) component).setTitle(title);
                return;
            }
            if (component instanceof Dialog) {
                ((Dialog) component).setTitle(title);
                return;
            }
        }
    }

    /**
     * Brings up a "Ok/Cancel" dialog with no icon. This method can be invoked
     * from any thread and blocks until the user click on "Ok" or "Cancel".
     *
     * @param  owner  The parent component. Dialog will apears on top of this owner.
     * @param  dialog The dialog content to show.
     * @param  title  The title string for the dialog.
     * @return {@code true} if user clicked "Ok", {@code false} otherwise.
     */
    public static boolean showOptionDialog(final Component owner,
                                           final Object   dialog,
                                           final String    title)
    {
        return showOptionDialog(owner, dialog, title, null);
    }

    /**
     * Brings up a "Ok/Cancel/Reset" dialog with no icon. This method can be invoked
     * from any thread and blocks until the user click on "Ok" or "Cancel".
     *
     * @param  owner  The parent component. Dialog will apears on top of this owner.
     * @param  dialog The dialog content to show.
     * @param  title  The title string for the dialog.
     * @param  reset  Action to execute when user press "Reset", or {@code null}
     *                if there is no "Reset" button. If {@code reset} is an
     *                instance of {@link Action}, the button label will be set
     *                according the action's properties.
     * @return {@code true} if user clicked "Ok", {@code false} otherwise.
     */
    public static boolean showOptionDialog(final Component      owner,
                                           final Object        dialog,
                                           final String         title,
                                           final ActionListener reset)
    {
        // Delegates to Swing thread if this method is invoked from an other thread.
        if (!EventQueue.isDispatchThread()) {
            final boolean result[] = new boolean[1];
            invokeAndWait(new Runnable() {
                public void run() {
                    result[0] = showOptionDialog(owner, dialog, title, reset);
                }
            });
            return result[0];
        }
        // Constructs the buttons bar.
        Object[]    options = null;
        Object initialValue = null;
        int okChoice = JOptionPane.OK_OPTION;
        if (reset != null) {
            final Vocabulary resources = Vocabulary.getResources(owner!=null ? owner.getLocale() : null);
            final JButton button;
            if (reset instanceof Action) {
                button = new JButton((Action)reset);
            } else {
                button = new JButton(resources.getString(VocabularyKeys.RESET));
                button.addActionListener(reset);
            }
            options = new Object[] {
                resources.getString(VocabularyKeys.OK),
                resources.getString(VocabularyKeys.CANCEL),
                button
            };
            initialValue = options[okChoice=0];
        }

        // Brings ups the dialog box.
        final int choice;
        if (JOptionPane.getDesktopPaneForComponent(owner)!=null) {
            choice=JOptionPane.showInternalOptionDialog(
                    owner,                         // Composante parente
                    dialog,                        // Message
                    title,                         // Titre de la boîte de dialogue
                    JOptionPane.OK_CANCEL_OPTION,  // Boutons à placer
                    JOptionPane.PLAIN_MESSAGE,     // Type du message
                    null,                          // Icone
                    options,                       // Liste des boutons
                    initialValue);                 // Bouton par défaut
        } else {
            choice=JOptionPane.showOptionDialog(
                    owner,                         // Composante parente
                    dialog,                        // Message
                    title,                         // Titre de la boîte de dialogue
                    JOptionPane.OK_CANCEL_OPTION,  // Boutons à placer
                    JOptionPane.PLAIN_MESSAGE,     // Type du message
                    null,                          // Icone
                    options,                       // Liste des boutons
                    initialValue);                 // Bouton par défaut
        }
        return choice==okChoice;
    }

    /**
     * Brings up a message dialog with a "Ok" button. This method can be invoked
     * from any thread and blocks until the user click on "Ok".
     *
     * @param  owner   The parent component. Dialog will apears on top of this owner.
     * @param  message The dialog content to show.
     * @param  title   The title string for the dialog.
     * @param  type    The message type
     *                ({@link JOptionPane#ERROR_MESSAGE},
     *                 {@link JOptionPane#INFORMATION_MESSAGE},
     *                 {@link JOptionPane#WARNING_MESSAGE},
     *                 {@link JOptionPane#QUESTION_MESSAGE} or
     *                 {@link JOptionPane#PLAIN_MESSAGE}).
     */
    public static void showMessageDialog(final Component owner,
                                         final Object  message,
                                         final String    title,
                                         final int        type)
    {
        if (!EventQueue.isDispatchThread()) {
            invokeAndWait(new Runnable() {
                public void run() {
                    showMessageDialog(owner, message, title, type);
                }
            });
            return;
        }
        if (JOptionPane.getDesktopPaneForComponent(owner)!=null) {
            JOptionPane.showInternalMessageDialog(
                    owner,     // Composante parente
                    message,   // Message
                    title,     // Titre de la boîte de dialogue
                    type);     // Type du message
        } else {
            JOptionPane.showMessageDialog(
                    owner,     // Composante parente
                    message,   // Message
                    title,     // Titre de la boîte de dialogue
                    type);     // Type du message
        }
    }

    /**
     * Brings up a confirmation dialog with "Yes/No" buttons. This method can be
     * invoked from any thread and blocks until the user click on "Yes" or "No".
     *
     * @param  owner   The parent component. Dialog will apears on top of this owner.
     * @param  message The dialog content to show.
     * @param  title   The title string for the dialog.
     * @param  type    The message type
     *                ({@link JOptionPane#ERROR_MESSAGE},
     *                 {@link JOptionPane#INFORMATION_MESSAGE},
     *                 {@link JOptionPane#WARNING_MESSAGE},
     *                 {@link JOptionPane#QUESTION_MESSAGE} or
     *                 {@link JOptionPane#PLAIN_MESSAGE}).
     * @return {@code true} if user clicked on "Yes", {@code false} otherwise.
     */
    public static boolean showConfirmDialog(final Component owner,
                                            final Object  message,
                                            final String    title,
                                            final int        type)
    {
        if (!EventQueue.isDispatchThread()) {
            final boolean result[] = new boolean[1];
            invokeAndWait(new Runnable() {
                public void run() {
                    result[0]=showConfirmDialog(owner, message, title, type);
                }
            });
            return result[0];
        }
        final int choice;
        if (JOptionPane.getDesktopPaneForComponent(owner)!=null) {
            choice=JOptionPane.showInternalConfirmDialog(
                    owner,                     // Composante parente
                    message,                   // Message
                    title,                     // Titre de la boîte de dialogue
                    JOptionPane.YES_NO_OPTION, // Boutons à faire apparaître
                    type);                     // Type du message
        } else {
            choice=JOptionPane.showConfirmDialog(
                    owner,                     // Composante parente
                    message,                   // Message
                    title,                     // Titre de la boîte de dialogue
                    JOptionPane.YES_NO_OPTION, // Boutons à faire apparaître
                    type);                     // Type du message
        }
        return choice==JOptionPane.YES_OPTION;
    }

    /**
     * Retourne une étiquette pour la composante spécifiée.
     * Le texte de l'étiquette pourra éventuellement être
     * distribué sur plusieurs lignes.
     *
     * @param owner Composante pour laquelle on construit une étiquette.
     *              L'étiquette aura la même largeur que {@code owner}.
     * @param text  Texte à placer dans l'étiquette.
     */
    public static JComponent getMultilineLabelFor(final JComponent owner, final String text) {
        final JTextArea label=new JTextArea(text);
        final Dimension size=owner.getPreferredSize();
        size.height=label.getMaximumSize().height;
        label.setMaximumSize  (size);
        label.setWrapStyleWord(true);
        label.setLineWrap     (true);
        label.setEditable    (false);
        label.setFocusable   (false);
        label.setOpaque      (false);
        label.setBorder       (null); // Certains L&F placent une bordure.
        LookAndFeel.installColorsAndFont(label, "Label.background",
                                                "Label.foreground",
                                                "Label.font");
        return label;
    }

    /**
     * Returns the locale for the specified component, or a default one if the component
     * is not yet part of a container hierarchy.
     */
    public static Locale getLocale(final Component component) {
        if (component != null) try {
            return component.getLocale();
        } catch (IllegalComponentStateException ignore) {
            // Ignore. Will returns de default locale below.
        }
        return JComponent.getDefaultLocale();
    }

    /**
     * Causes runnable to have its run method called in the dispatch thread of
     * the event queue. This will happen after all pending events are processed.
     * The call blocks until this has happened.
     */
    public static void invokeAndWait(final Runnable runnable) {
        if (EventQueue.isDispatchThread()) {
            runnable.run();
        } else {
            try {
                EventQueue.invokeAndWait(runnable);
            } catch (InterruptedException exception) {
                // Someone don't want to let us sleep. Go back to work.
            } catch (InvocationTargetException target) {
                final Throwable exception=target.getTargetException();
                if (exception instanceof RuntimeException) {
                    throw (RuntimeException) exception;
                }
                if (exception instanceof Error) {
                    throw (Error) exception;
                }
                // Should not happen, since {@link Runnable#run} do not allow checked exception.
                throw new UndeclaredThrowableException(exception, exception.getLocalizedMessage());
            }
        }
    }
}

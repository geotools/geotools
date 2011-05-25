package org.geotools.swing.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.geotools.swing.ExceptionMonitor;

/**
 * A safe version of AbstractAction that will log any problems encountered.
 * <p>
 * This is not generally a good practice - we are just using it as an excuse to not mess up code
 * examples with exception handling code (gasp!).
 * </p>
 * TODO: provide a background Runnable...
 *
 *
 * @source $URL$
 */
public abstract class SafeAction extends AbstractAction {
    private static final long serialVersionUID = 1118122797759176800L;

    /**
     * Constructor
     * @param name name for the associated control
     */
    public SafeAction(String name) {
        super(name);
    }

    /**
     * Sub-classes (usually anonymous) must override this method instead
     * of the usual {@linkplain javax.swing.Action#actionPerformed}
     *
     * @param e the action event
     * @throws Throwable on error
     */
    public abstract void action( ActionEvent e ) throws Throwable;

    /**
     * Calls the {@linkplain #action } method
     *
     * @param e the action event
     */
    public void actionPerformed(ActionEvent e) {
        try {
            action( e );
        } catch (Throwable t) {
            ExceptionMonitor.show(e.getSource() instanceof Component ? (Component) e.getSource()
                    : null, t);
        }
    }

}

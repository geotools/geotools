package test.actions;

import java.io.File;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.program.Program;

import test.Explorer;

public class OpenAction extends Action implements ISelectionChangedListener {
    Explorer window;

    public OpenAction( Explorer w ) {
        window = w;
        setText("Run");
        setToolTipText("Run the associated program on a file");
        // setImageDescriptor(
        // ImageDescriptor.createFromURL(Util.newURL("file:icons/run.gif")));
    }

    public void run() {
        IStructuredSelection selection = window.getTableSelection();
        if (selection.size() != 1) {
            return;
        }

        File selected_file = (File) selection.getFirstElement();
        if (selected_file.isFile()) {
            Program.launch(selected_file.getAbsolutePath());
        }
    }

    public void selectionChanged( SelectionChangedEvent event ) {
        setText("Run");
        setToolTipText("Run the associated program on a file");

        IStructuredSelection selection = window.getTableSelection();
        if (selection.size() != 1) {
            setEnabled(false);
            setToolTipText(getToolTipText() + " (Only enabled when exactly one item is selected)");
            return;
        }

        File file = (File) selection.getFirstElement();
        if (file.isFile()) {
            setEnabled(true);
            setText("Run the associated program on " + file.getName());
            setToolTipText("Run the program associated with " + file.getName() + " with this file as the argument");
        }
    }
}

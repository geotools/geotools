package test.actions;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.ApplicationWindow;

public class ExitAction extends Action {
    ApplicationWindow window;

    public ExitAction( ApplicationWindow w ) {
        window = w;
        setText("E&xit@Ctrl+W");
        setToolTipText("Exit the application");
        // setImageDescriptor(
        // ImageDescriptor.createFromURL(Util.newURL("file:icons/close.gif")));
    }
    public void run() {
        window.close();
    }
}

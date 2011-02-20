package test.actions;
import java.io.File;
import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

import test.Explorer;
import test.Util;

public class CopyFileNamesToClipboardAction extends Action {
    Explorer window;

    public CopyFileNamesToClipboardAction( Explorer w ) {
        window = w;
        setToolTipText("Copy absolute file names of selected files to the clipboard");
        setText("Copy File &Names@Ctrl+Shift+C");
        // setImageDescriptor(
        // ImageDescriptor.createFromURL(Util.newURL("file:icons/copy.gif")));
    }

    public void run() {
        Clipboard clipboard = Util.getClipboard();
        TextTransfer text_transfer = TextTransfer.getInstance();

        IStructuredSelection selection = window.getTableSelection();
        if (selection.isEmpty()) {
            return;
        }

        StringBuffer string_buffer = new StringBuffer();
        for( Iterator i = selection.iterator(); i.hasNext(); ) {
            File file = (File) i.next();
            string_buffer.append(" ");
            string_buffer.append(file.getAbsolutePath());
        }

        clipboard.setContents(new Object[]{string_buffer.toString()}, new Transfer[]{text_transfer});
    }
}

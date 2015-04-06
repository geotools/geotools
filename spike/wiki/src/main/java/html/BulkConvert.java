/*    (C) 2014 Open Source Geospaital Foundation (OSGeo)
 *    (C) 2012, Refractions Research Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * (http://www.eclipse.org/legal/epl-v10.html), and the Refractions BSD
 * License v1.0 (http://udig.refractions.net/files/bsd3-v10.html).
 */
package html;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;

public class BulkConvert {
    /**
     * Used to shortlist index.html file
     */
    private static final class IndexFileFilter extends javax.swing.filechooser.FileFilter {
        public String getDescription() {
            return "index.html";
        }

        @Override
        public boolean accept(File f) {
            return f.getName().equals("index.html");
        }
    }

    /**
     * Used to list html files for conversion.
     */
    private final class HtmlFileFilter implements FileFilter {
        public boolean accept(File file) {
            return file.getName().endsWith(".html");
        }
    }

    public static void main(String args[]) {
        if (args.length == 1 && "?".equals(args[0])) {
            System.out.println(" Usage: java html.BulkConvert [index.html] [wiki directory]");
            System.out.println();
            System.out.println("Where:");
            System.out.println("  index.html Where you have unzipped the confluence html export");
            System.out.println("  wiki directory location where you would like the convertred files saved");
            System.out.println();
            System.out.println("If not provided the appication will prompt you for the above information");

            System.exit(0);
        }
        File indexFile = args.length > 0 ? new File(args[0]) : null;
        File wikiDir = args.length > 1 ? new File(args[1]) : null;

        if (indexFile != null && indexFile.isDirectory()) {
            indexFile = new File(indexFile, "index.html");
        }

        if (indexFile == null || !indexFile.exists()) {
            File cd = new File(".");

            JFileChooser dialog = new JFileChooser(cd);
            dialog.setFileFilter(new IndexFileFilter());
            dialog.setDialogTitle("Locate Confluence wiki html export");
            int open = dialog.showDialog(null, "Convert");

            if (open == JFileChooser.CANCEL_OPTION) {
                System.out.println("Conversion canceled");
                System.exit(-1);
            }
            indexFile = dialog.getSelectedFile();
        }
        if (indexFile == null || !indexFile.exists() || indexFile.isDirectory()) {
            System.out.println("File index.html to use for conversion not provided: '" + indexFile
                    + "'");
            System.exit(-1);
        }
        File htmlDirectory = null;
        try {
            htmlDirectory = indexFile.getParentFile().getCanonicalFile();
        } catch (IOException eek) {
            System.out.println("Coudl not sort parent of " + indexFile + ":" + eek);
            System.exit(-1);
        }

        if (wikiDir == null) {
            JFileChooser dialog = new JFileChooser(htmlDirectory.getParentFile());

            dialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            dialog.setDialogTitle("Target Directory for textile files");

            int open = dialog.showDialog(null, "Export");

            if (open == JFileChooser.CANCEL_OPTION) {
                System.out.println("Canceled canceled");
                System.exit(-1);
            }
            wikiDir = dialog.getSelectedFile();
        }
        if (wikiDir == null || !wikiDir.isDirectory() || !wikiDir.exists()) {
            System.out.println("Taget directory for textile files not provided: '" + wikiDir + "'");
            System.exit(-1);
        }

        BulkConvert bulk = new BulkConvert(htmlDirectory, wikiDir);
        bulk.convert();
    }

    private void convert() {

        File[] htmlFileList = htmlDirectory.listFiles(new HtmlFileFilter());
        for (File htmlFile : htmlFileList) {
            // File dir = htmlFile.getParentFile();

            String htmlName = htmlFile.getName();
            int split = htmlName.lastIndexOf('_');
            String name = split == -1 ? htmlName : htmlName.substring(0, split);
            name = fixPageReference(name);
            
            String mdName = name + ".md";
//            if( htmlName.equals("Home.html") ){
//                mdName = "index.rst";
//            }
            
            File mdFile = new File(wikiDirectory, mdName);
            System.out.println(htmlFile + " to " + mdFile.getName());
            if (mdFile.exists()) {
                boolean deleted = mdFile.delete();
                if (!deleted) {
                    System.out.println("\tCould not remove '" + mdFile
                            + "' do you have it open in an editor?");
                    System.out.println("\tSkipping ...");
                    continue;
                }
            }
            boolean success = pandoc(htmlFile, mdFile);
            if (!success) {
                break;
            }
            fixAndMoveImages(mdFile);
        }
    }

    private File htmlDirectory;
    private File wikiDirectory;

    private BulkConvert(File htmlDirectory, File rstDir) {
        this.htmlDirectory = htmlDirectory;
        this.wikiDirectory = rstDir;
    }

    String baseName(File file) {
        String name = file.getName();
        int split = name.lastIndexOf('.');
        if (split == -1) {
            return name;
        }
        String page = name.substring(0, split);
        page = fixPageReference(page);
        page = page.toLowerCase();

        return page;
    }

    String baseTitle(File file) {
        String name = file.getName();
        int split = name.lastIndexOf('.');
        if (split == -1) {
            return name;
        }
        String page = name.substring(0, split);
        
        String title = toTitleCase( page.replace('_',' ') );
        return title;
    }

    String toTitleCase(String heading) {
        StringBuilder title = new StringBuilder();
        boolean next = true;

        for (char c : heading.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                next = true;
            } else if (next) {
                c = Character.toTitleCase(c);
                next = false;
            }
            title.append(c);
        }
        return title.toString();
    }
    /**
     * Process the generated markdown file and perform a few fixes.
     * 
     * @param markdownFile
     */
    private boolean fixAndMoveImages(File markdownFile) {
        File imageDir = new File(markdownFile.getParent(), "images");
        String page = baseName(markdownFile);
        String title = baseTitle(markdownFile);

        // Fix image location
        // BEFORE: .. |image0| image:: download/attachments/3536/view.gif
        //  AFTER: .. |image0| image:: /image/active_part/view.gif
        //
        // Use http://www.regexplanet.com/advanced/java/index.html to work out
        //
        // pattern: .. \|image(\d)\| image:: (download/attachments/.+?)/(.+)
        // replace: .. |image$1| image:: /page/$3
        Pattern imagePattern = Pattern
                .compile(".. \\|image(\\d+)\\| image:: (download/attachments/.+?)/(.+)");
        String imageReplace = ".. |image$1| image:: /images/" + page + "/$3";

        // BEFORE: .. |image10| image:: download/attachments/4523/delete_feature_mode.gif
        //  AFTER: .. |image10| image:: /image/edit_tools/delete_feature_mode.gif
        
        Pattern figurePattern = Pattern.compile(".. figure:: (download/attachments/.+?)/(.+)");
        String figureReplace = ".. figure:: /images/" + page + "/$2";

        // Fix Related cross references
        // .. figure:: http://udig.refractions.net/image/EN/ngrelc.gif
        Map<Pattern, String> replace = new HashMap<Pattern, String>();
//        replace.put(Pattern.compile(".. figure:: http://udig.refractions.net/image/EN/ngrelt.gif"),
//                "**Related tasks**");
//        replace.put(Pattern.compile(".. figure:: http://udig.refractions.net/image/EN/ngrelr.gif"),
//                "**Related reference**");
//        replace.put(Pattern.compile(".. figure:: http://udig.refractions.net/image/EN/ngrelc.gif"),
//                "**Related concepts**");

        
        Pattern linkPattern = Pattern.compile("([a-zA-Z\\-0-9]+)\\_(\\d+)\\.html");
        String linkReplace = "$1";
        
        Pattern strightLinkPattern = Pattern.compile("(\\s*)`(.*) <(.*)>`_");
        String straightLinkReplace = "$1:doc:`$2`";

//        Pattern heading = Pattern.compile("(=|-|~|\\^|\\')+");
//        final String LEVEL = "=-~^'";

        StringBuilder contents = new StringBuilder();
        BufferedReader reader = null;
        String line;

        int pageLevel = -1; // no heading encountered yet
        boolean lineFeedNeeded = false;

        String previousLine = null;
        try {
            reader = new BufferedReader(new FileReader(markdownFile));
            
            // Skip Header Stuff
            while ((line = reader.readLine()) != null) {
                if( line.contains("<div id=\"main-content\" class=\"wiki-content group\">")){
                    // Wiki pages include page name at the top so no need to repeat header as part of the
                    // document 
                    break;
                }
            }
            
            while ((line = reader.readLine()) != null) {
                if( line.contains("<div id=\"footer\" role=\"contentinfo\">")){
                    // no need to include the footer 
                    break;
                }
                if( line.startsWith("<div class") ){
                    if( line.startsWith("<div class=\"columnMacro\">") ||
                        line.startsWith("<div class=\"sectionColumnWrapper\">") ||
                        line.startsWith("<div class=\"sectionMacro\">") ||
                        line.startsWith("<div class=\"sectionMacroRow\">") ||
                        line.startsWith("<div class=\"columnMacro\"") ||
                        line.startsWith("<div class=\"refresh-issues-bottom\">")){
                        continue;
                    }
                    continue;
                }
                if( line.startsWith("</div>")){
                    continue; // skip divs
                }
                if( line.contains("FeatureCollection cleanup")){
                    // breakpoint for debugging
                    System.out.println("Check delete_feature_mode.gif ");
                }
                // Fix common replacements
                for (Entry<Pattern, String> entry : replace.entrySet()) {
                    Matcher matcher = entry.getKey().matcher(line);
                    if (matcher.matches()) {
                        line = matcher.replaceAll(entry.getValue());
                        reader.readLine(); // skip :align: center
                        reader.readLine(); // skip :alt:

                        lineFeedNeeded = true;
                    }
                }
                // Fix document links
                Matcher linkMatcher = linkPattern.matcher(line);
                line = linkMatcher.replaceAll(linkReplace);
                Matcher straightLinkMatcher = strightLinkPattern.matcher(line);
                if (straightLinkMatcher.matches()) {
                    String indent = straightLinkMatcher.group(1);
                    String pageRef = straightLinkMatcher.group(2);
                    String htmlRef = straightLinkMatcher.group(3);
                    // BEFORE: `some page <some%20page.html>`_
                    // AFTER: :doc:`some page`
                    // line = straightLinkMatcher.replaceAll(straightLinkReplace);
                    // lineFeedNeeded = true;
                    String fixed_page_ref = fixPageReference(pageRef);
                    if( htmlRef.startsWith("http")){
                        // external link `
                        // $1`$2 <$3>`_
                        line = indent+"`"+pageRef+" <"+htmlRef+">`_";
                    }
                    else {
                        line = indent+":doc:`"+fixed_page_ref+"`";
                        lineFeedNeeded = true;
                    }
                }
                // check for image references
                //
                Matcher imageMatcher = imagePattern.matcher(line);
                if (imageMatcher.matches()) {
                    String image = imageMatcher.group(1);
                    String path = imageMatcher.group(2);
                    String file = imageMatcher.group(3);

                    File attachementImage = new File(new File(htmlDirectory, path), file);
                    File pageDir = new File(imageDir, page);
                    File pageImage = new File(pageDir, file);

                    duplicateImage(attachementImage, pageImage);
                    boolean moved = pageImage.exists();
                    if (moved) {
                        line = imageMatcher.replaceAll(imageReplace);
                    }
                }
                // check for figure references
                //
                Matcher figureMatcher = figurePattern.matcher(line);
                if (figureMatcher.matches()) {
                    String path = figureMatcher.group(1);
                    String file = figureMatcher.group(2);

                    File attachementImage =new File( new File(htmlDirectory,path), file);
                    File pageDir = new File(imageDir, page);
                    File pageImage = new File(pageDir, file);

                    duplicateImage(attachementImage, pageImage);
                    boolean moved = pageImage.exists();
                    if (moved) {
                        line = figureMatcher.replaceAll(figureReplace);
                    }
                }
                contents.append(line);
                contents.append("\n");
                if (lineFeedNeeded) {
                    contents.append("\n");
                    lineFeedNeeded = false;
                }
                previousLine = line; // remember for next time in case this is a heading!
            }
        } catch (FileNotFoundException e) {
            System.out.println("Unable to read '" + markdownFile + "':" + e);
            return false;
        } catch (IOException e) {
            System.out.println("Trouble reading '" + markdownFile + "':" + e);
            return false;
        } finally {
            close(reader);
        }

        String text = contents.toString();
        boolean deleted = markdownFile.delete();
        if (deleted) {
            // write out modified copy
            try {
                OutputStream modifiedCopy = new BufferedOutputStream(new FileOutputStream(markdownFile));

                InputStream textSteram = new ByteArrayInputStream(text.getBytes(Charset
                        .defaultCharset()));
                bufferedStreamsCopy(textSteram, modifiedCopy);
            } catch (IOException eek) {
                System.out.println("Trouble writing modified '" + markdownFile + "':" + eek);
                return false;
            }
        }
        return true;
    }
    /** Frank has asked that pages be all lowercase and not contain any spaces */
    private String fixPageReference(String pageRef) {
        pageRef = pageRef.replace(' ','_');
        pageRef = pageRef.toLowerCase();
        
        return pageRef;
    }

    private void duplicateImage(File attachementImage, File pageImage) {
        String fileName = attachementImage.getName();
        
        File liveImage = searchPluginImages(fileName);
        if( liveImage != null ){
            System.out.println("   Override attachment image from : "+ liveImage );
            attachementImage = liveImage;
        }
        else if (!attachementImage.exists()) {
            File otherAttachment = searchAllAttachments(fileName);
            if( otherAttachment != null ){
                System.out.println("   Found attachment on another page " + otherAttachment );
                attachementImage = otherAttachment; // okay we found it on another page
            }
            else {
                System.out.println("   WARNING: Unable to locate " + attachementImage + " broken link!");                
            }
        }

        File pageDir = pageImage.getParentFile();
        if (!pageDir.exists()) {
            pageDir.mkdirs();
        }
        if (!pageImage.exists()) {
            System.out.println("   Copy image to  " + pageImage);
            bufferedStreamsCopy(attachementImage, pageImage);
        } else {
            boolean deleted = pageImage.delete();
            if (deleted) {
                System.out.println("   Copy image to  " + pageImage);
                bufferedStreamsCopy(attachementImage, pageImage);
            } else {
                System.out.println("   WARNING: Unable to repalce " + pageImage);
            }
        }
    }

    /** Search for the provided filename */
    private File searchAllAttachments(String fileName) {
        // search downloads
        File attachments = new File( new File(htmlDirectory, "download"),"attachments");
        if (attachments.exists() && attachments.isDirectory()) {
            File file = searchDirectory(attachments, fileName);
            if (file != null) {
                return file; // found it!
            }
        }
        return null; // not found!
    }

    private File searchPluginImages(String fileName) {
        // search for live icons first!
        File checkout = wikiDirectory.getParentFile().getParentFile().getParentFile();
        File plugins = new File(checkout, "plugins");
        if( plugins.exists() && plugins.isDirectory() ){
            for (File plugin : plugins.listFiles()) {
                File icons = new File(plugin, "icons");
                if (icons.exists() && icons.isDirectory()) {
                    File file = searchDirectory(icons, fileName);
                    if (file != null) {
                        return file; // found it!
                    }
                }
            }
        }
        return null; // not found
    }

    private File searchDirectory(File dir, String fileName) {
        for( File file : dir.listFiles() ){
            if( file.isDirectory() ){
                File found = searchDirectory(file, fileName);
                if( found != null ){
                    return found; // found!
                }
            }
            if( fileName.equals( file.getName() ) ){
                return file;
            }
        }
        return null; // not found!
    }

    private boolean pandoc(File htmlFile, File mdFile) {

        String mdFilePath = mdFile.getAbsolutePath().toString();
        String htmlFilePath = htmlFile.getAbsolutePath().toString();

        System.out.println("/usr/local/bin/pandoc --columns=100 -o \"" + mdFilePath + "\" \""
                + htmlFilePath + "\"");
        String run[] = new String[] { "/usr/local/bin/pandoc", "--columns=100", "-o", mdFilePath,
                htmlFilePath };
        try {
            Process process = Runtime.getRuntime().exec(run, null, htmlFile.getParentFile());
            int exit = process.waitFor();
            System.out.println("\tGenerated " + mdFile.getName() + " with exit code " + exit);
            return exit == 0;
        } catch (InterruptedException e) {
            System.out.println("\\tFailed on " + mdFile.getName() + " with: " + e);
            return false;
        } catch (IOException e) {
            System.out.println("\\tFailed on " + mdFile.getName() + " with: " + e);
            e.printStackTrace();
            return false;
        }
    }

//    private boolean html2res(File htmlFile, File rstFile) {
//        String run[] = new String[] { "/usr/local/bin/html2rest", htmlFile.getName() };
//
//        BufferedInputStream inputStream = null;
//        BufferedOutputStream outputStream = null;
//        try {
//            Process process = Runtime.getRuntime().exec(run, null, htmlFile.getParentFile());
//            // inputStream = new BufferedInputStream(new FileInputStream(htmlFile));
//            // outputStream = new BufferedOutputStream( new FileOutputStream(rstFile));
//
//            inputStream = new BufferedInputStream(process.getInputStream());
//            outputStream = new BufferedOutputStream(new FileOutputStream(rstFile));
//
//            bufferedStreamsCopy(inputStream, outputStream);
//
//            process.waitFor();
//            int exit = process.exitValue();
//            System.out.println("\tGenerated " + rstFile.getName() + " with exist code " + exit);
//            return exit == 0;
//        } catch (IOException e) {
//            System.out.println("\\tFailed on " + rstFile.getName() + " with: " + e);
//            e.printStackTrace();
//            return false;
//        } catch (InterruptedException e) {
//            System.out.println("\\tFailed on " + rstFile.getName() + " with: " + e);
//            return false;
//        }
//    }

    private void bufferedStreamsCopy(File origional, File copy) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(new FileInputStream(origional));
            out = new BufferedOutputStream(new FileOutputStream(copy));

            bufferedStreamsCopy(in, out);
        } catch (Exception eek) {
            System.out.print("Unable to copy from " + origional.getName() + "to " + copy);
            eek.printStackTrace();
        }
    }

    // From http://java.dzone.com/articles/file-copy-java-�-benchmark
    private void bufferedStreamsCopy(InputStream fin, OutputStream fout) throws IOException {
        try {
            int data;
            while ((data = fin.read()) != -1) {
                fout.write(data);
            }
            fout.flush();
        } finally {
            close(fin);
            close(fout);
        }
    }

    private void close(Closeable closable) {
        if (closable != null) {
            try {
                closable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

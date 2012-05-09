package org.webcat.eclipse.importer;

import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;

// -------------------------------------------------------------------------
/**
 *  Utility class for reading compressed files from a stream and unzipping
 *  them to the users harddrive.
 *
 *  @author bellen08
 *  @version Apr 2, 2012
 */
public class ZipUtils
{
    // ----------------------------------------------------------


    //pass the method a file object representing the destination
    //location (on the hard drive) and the inputStream containing the
    //the expanded file
    /**
     * Goes through the contents of the zip file (via the stream), storing
     * each individual element at the given location on the harddrive.
     * @param destPath the path of the destination file
     * @param stream the input stream given the url of the host
     * @throws IOException if the given filepath doesn't exist
     */
    public void unpack(File destPath, InputStream stream)
        throws IOException
    {
     ZipInputStream zipStream = new ZipInputStream(stream);

     //getNextEntry() returns a ZipEntry object and reads
     //the next zip file entry and positions the stream
     //at the beginning of the entry data
     ZipEntry zipEntry = zipStream.getNextEntry();
     //there is still files to unzip
     while (zipEntry != null)
     {
         String name = zipEntry.getName();
         //a file itself
         if (zipEntry.isDirectory())
         {
             if (!"__MACOSX".equals(name))
             {
                 File destDir = new File(destPath, name);

                 if (!destDir.exists())
                 {
                     //method mkdirs include any necessary but
                     //nonexistant parent directories
                     destDir.mkdirs();
                 }
             }
         }
         else if (name != null
                  && !(name.equals(".DS_Store")
                       || name.startsWith("__MACOSX/")
                       || name.endsWith("/.DS_Store")))
         {
             File destFile = new File(destPath, name);
             File destParent = destFile.getParentFile();

             if (destParent != null  &&  !destParent.exists())
                {
                 destParent.mkdirs();
                }

             copyStreamToFile(zipStream, destFile, zipEntry.getTime());
         }

         zipStream.closeEntry();
         zipEntry = zipStream.getNextEntry();
     }
    }



     //helper method
    // ----------------------------------------------------------
    private static void copyStreamToFile(InputStream stream, File destFile,
        long fileTime)
        throws IOException
    {
        OutputStream outStream = new FileOutputStream(destFile);
        copyStream(stream, outStream);
        outStream.flush();
        outStream.close();

        destFile.setLastModified(fileTime);
    }


    // ----------------------------------------------------------
    private static void copyStream(InputStream in, OutputStream out)
        throws IOException
    {
        final int BUFFER_SIZE = 65536;

        // read in increments of BUFFER_SIZE
        byte[] b = new byte[BUFFER_SIZE];
        int count = in.read(b);
        while (count > -1)
        {
            out.write(b, 0, count);
            count = in.read(b);
        }

        out.flush();
    }
}
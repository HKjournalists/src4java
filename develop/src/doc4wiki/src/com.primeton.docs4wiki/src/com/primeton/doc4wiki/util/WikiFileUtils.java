/*******************************************************************************
 * $Header: /cvsroot/BPS6/develop/build/dailybuild/doc4wiki/src/com.primeton.docs4wiki/src/com/primeton/doc4wiki/util/WikiFileUtils.java,v 1.1 2013/06/06 01:44:58 liuxiang Exp $
 * $Revision: 1.1 $
 * $Date: 2013/06/06 01:44:58 $
 *
 *==============================================================================
 *
 * Copyright (c) 2001-2006 Primeton Technologies, Ltd.
 * All rights reserved.
 * 
 * Created on 2009-5-8
 *******************************************************************************/

package com.primeton.doc4wiki.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * 
 * @author liuxiang(mailto:liuxiang@primeton.com)
 * 2013-6-6 上午8:12:11
 */
public class WikiFileUtils {

    /**
     * Instances should NOT be constructed in standard programming.
     */
    public WikiFileUtils() {
        super();
    }

    /**
     * The number of bytes in a kilobyte.
     */
    public static final long ONE_KB = 1024;

    /**
     * The number of bytes in a megabyte.
     */
    public static final long ONE_MB = ONE_KB * ONE_KB;

    /**
     * The number of bytes in a gigabyte.
     */
    public static final long ONE_GB = ONE_KB * ONE_MB;

    /**
     * An empty array of type <code>File</code>.
     */
    public static final File[] EMPTY_FILE_ARRAY = new File[0];

    /**
     * Clean a directory without deleting it.
     *
     * @param directory directory to clean
     * @throws IOException in case cleaning is unsuccessful
     */
    public static void cleanDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }

        File[] files = directory.listFiles();
        if (files == null) {  // null if security restricted
            throw new IOException("Failed to list contents of " + directory);
        }

        IOException exception = null;
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            try {
                forceDelete(file);
            } catch (IOException ioe) {
                exception = ioe;
            }
        }

        if (null != exception) {
            throw exception;
        }
    }
    
  //-----------------------------------------------------------------------
    /**
     * Recursively delete a directory.
     *
     * @param directory  directory to delete
     * @throws IOException in case deletion is unsuccessful
     */
    public static void deleteDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }

        cleanDirectory(directory);
        if (!directory.delete()) {
            String message =
                "Unable to delete directory " + directory + ".";
            throw new IOException(message);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Delete a file. If file is a directory, delete it and all sub-directories.
     * <p>
     * The difference between File.delete() and this method are:
     * <ul>
     * <li>A directory to be deleted does not have to be empty.</li>
     * <li>You get exceptions when a file or directory cannot be deleted.
     *      (java.io.File methods returns a boolean)</li>
     * </ul>
     *
     * @param file  file or directory to delete, must not be <code>null</code>
     * @throws NullPointerException if the directory is <code>null</code>
     * @throws IOException in case deletion is unsuccessful
     */
    public static void forceDelete(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectory(file);
        } else {
            if (!file.exists()) {
                throw new FileNotFoundException("File does not exist: " + file);
            }
            if (!file.delete()) {
                String message =
                    "Unable to delete file: " + file;
                throw new IOException(message);
            }
        }
    }

    /**
     * Schedule a file to be deleted when JVM exits.
     * If file is directory delete it and all sub-directories.
     *
     * @param file  file or directory to delete, must not be <code>null</code>
     * @throws NullPointerException if the file is <code>null</code>
     * @throws IOException in case deletion is unsuccessful
     */
    public static void forceDeleteOnExit(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectoryOnExit(file);
        } else {
            file.deleteOnExit();
        }
    }

    /**
     * Recursively schedule directory for deletion on JVM exit.
     *
     * @param directory  directory to delete, must not be <code>null</code>
     * @throws NullPointerException if the directory is <code>null</code>
     * @throws IOException in case deletion is unsuccessful
     */
    private static void deleteDirectoryOnExit(File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }

        cleanDirectoryOnExit(directory);
        directory.deleteOnExit();
    }

    /**
     * Clean a directory without deleting it.
     *
     * @param directory  directory to clean, must not be <code>null</code>
     * @throws NullPointerException if the directory is <code>null</code>
     * @throws IOException in case cleaning is unsuccessful
     */
    private static void cleanDirectoryOnExit(File directory) throws IOException {
        if (!directory.exists()) {
            String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }

        File[] files = directory.listFiles();
        if (files == null) {  // null if security restricted
            throw new IOException("Failed to list contents of " + directory);
        }

        IOException exception = null;
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            try {
                forceDeleteOnExit(file);
            } catch (IOException ioe) {
                exception = ioe;
            }
        }

        if (null != exception) {
            throw exception;
        }
    }

    /**
     * Make a directory, including any necessary but nonexistent parent
     * directories. If there already exists a file with specified name or
     * the directory cannot be created then an exception is thrown.
     *
     * @param directory  directory to create, must not be <code>null</code>
     * @throws NullPointerException if the directory is <code>null</code>
     * @throws IOException if the directory cannot be created
     */
    public static void forceMkdir(File directory) throws IOException {
        if (directory.exists()) {
            if (directory.isFile()) {
                String message =
                    "File "
                        + directory
                        + " exists and is "
                        + "not a directory. Unable to create directory.";
                throw new IOException(message);
            }
        } else {
            if (!directory.mkdirs()) {
                String message =
                    "Unable to create directory " + directory;
                throw new IOException(message);
            }
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Recursively count size of a directory (sum of the length of all files).
     *
     * @param directory  directory to inspect, must not be <code>null</code>
     * @return size of directory in bytes, 0 if directory is security restricted
     * @throws NullPointerException if the directory is <code>null</code>
     */
    public static long sizeOfDirectory(File directory) {
        if (!directory.exists()) {
            String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }

        long size = 0;

        File[] files = directory.listFiles();
        if (files == null) {  // null if security restricted
            return 0L;
        }
        for (int i = 0; i < files.length; i++) {
            File file = files[i];

            if (file.isDirectory()) {
                size += sizeOfDirectory(file);
            } else {
                size += file.length();
            }
        }

        return size;
    }

    //-----------------------------------------------------------------------
    /**
     * Tests if the specified <code>File</code> is newer than the reference
     * <code>File</code>.
     *
     * @param file  the <code>File</code> of which the modification date must
     * be compared, must not be <code>null</code>
     * @param reference  the <code>File</code> of which the modification date
     * is used, must not be <code>null</code>
     * @return true if the <code>File</code> exists and has been modified more
     * recently than the reference <code>File</code>
     * @throws IllegalArgumentException if the file is <code>null</code>
     * @throws IllegalArgumentException if the reference file is <code>null</code> or doesn't exist
     */
     public static boolean isFileNewer(File file, File reference) {
        if (reference == null) {
            throw new IllegalArgumentException("No specified reference file");
        }
        if (!reference.exists()) {
            throw new IllegalArgumentException("The reference file '"
                    + file + "' doesn't exist");
        }
        return isFileNewer(file, reference.lastModified());
    }

    /**
     * Tests if the specified <code>File</code> is newer than the specified
     * <code>Date</code>.
     * 
     * @param file  the <code>File</code> of which the modification date
     * must be compared, must not be <code>null</code>
     * @param date  the date reference, must not be <code>null</code>
     * @return true if the <code>File</code> exists and has been modified
     * after the given <code>Date</code>.
     * @throws IllegalArgumentException if the file is <code>null</code>
     * @throws IllegalArgumentException if the date is <code>null</code>
     */
    public static boolean isFileNewer(File file, Date date) {
        if (date == null) {
            throw new IllegalArgumentException("No specified date");
        }
        return isFileNewer(file, date.getTime());
    }

    /**
     * Tests if the specified <code>File</code> is newer than the specified
     * time reference.
     *
     * @param file  the <code>File</code> of which the modification date must
     * be compared, must not be <code>null</code>
     * @param timeMillis  the time reference measured in milliseconds since the
     * epoch (00:00:00 GMT, January 1, 1970)
     * @return true if the <code>File</code> exists and has been modified after
     * the given time reference.
     * @throws IllegalArgumentException if the file is <code>null</code>
     */
     public static boolean isFileNewer(File file, long timeMillis) {
        if (file == null) {
            throw new IllegalArgumentException("No specified file");
        }
        if (!file.exists()) {
            return false;
        }
        return file.lastModified() > timeMillis;
    }


    //-----------------------------------------------------------------------
    /**
     * Tests if the specified <code>File</code> is older than the reference
     * <code>File</code>.
     *
     * @param file  the <code>File</code> of which the modification date must
     * be compared, must not be <code>null</code>
     * @param reference  the <code>File</code> of which the modification date
     * is used, must not be <code>null</code>
     * @return true if the <code>File</code> exists and has been modified before
     * the reference <code>File</code>
     * @throws IllegalArgumentException if the file is <code>null</code>
     * @throws IllegalArgumentException if the reference file is <code>null</code> or doesn't exist
     */
     public static boolean isFileOlder(File file, File reference) {
        if (reference == null) {
            throw new IllegalArgumentException("No specified reference file");
        }
        if (!reference.exists()) {
            throw new IllegalArgumentException("The reference file '"
                    + file + "' doesn't exist");
        }
        return isFileOlder(file, reference.lastModified());
    }

    /**
     * Tests if the specified <code>File</code> is older than the specified
     * <code>Date</code>.
     * 
     * @param file  the <code>File</code> of which the modification date
     * must be compared, must not be <code>null</code>
     * @param date  the date reference, must not be <code>null</code>
     * @return true if the <code>File</code> exists and has been modified
     * before the given <code>Date</code>.
     * @throws IllegalArgumentException if the file is <code>null</code>
     * @throws IllegalArgumentException if the date is <code>null</code>
     */
    public static boolean isFileOlder(File file, Date date) {
        if (date == null) {
            throw new IllegalArgumentException("No specified date");
        }
        return isFileOlder(file, date.getTime());
    }

    /**
     * Tests if the specified <code>File</code> is older than the specified
     * time reference.
     *
     * @param file  the <code>File</code> of which the modification date must
     * be compared, must not be <code>null</code>
     * @param timeMillis  the time reference measured in milliseconds since the
     * epoch (00:00:00 GMT, January 1, 1970)
     * @return true if the <code>File</code> exists and has been modified before
     * the given time reference.
     * @throws IllegalArgumentException if the file is <code>null</code>
     */
     public static boolean isFileOlder(File file, long timeMillis) {
        if (file == null) {
            throw new IllegalArgumentException("No specified file");
        }
        if (!file.exists()) {
            return false;
        }
        return file.lastModified() < timeMillis;
    }
    /**
     * 
     * @param zipFile
     * @param desDir
     * @param strip
     * @throws ZipException
     * @throws IOException
     */
 	public static void unpack(File zipFile, File desDir,boolean strip) throws ZipException, IOException{
 		if(desDir.exists()){
 			try {
 				WikiFileUtils.forceDelete(desDir);
			} catch (Exception e) {
			}
 		}
 		WikiFileUtils.forceMkdir(desDir);
 		String desDirPath = desDir.getAbsolutePath();
 		ZipFile zip = new ZipFile(zipFile);
 		Enumeration<? extends ZipEntry> enumeration = zip.entries();
 		while (enumeration.hasMoreElements()) {
 			ZipEntry zipEntry = (ZipEntry) enumeration.nextElement();
 			if (zipEntry.isDirectory()) 
 				continue;
             String name = zipEntry.getName();
             if(strip){            	 
            	 //由于压缩包打出来的内容默认会多一层spaceid的文件夹，所有需要做特殊处理
            	 name = WikiStringUtils.substringAfter(name, "/");
             }
             String path = desDirPath + "/" + name;
             WikiFileUtils.forceMkdir(new File(path).getParentFile());
             entryToFile(zip, zipEntry, path);
 		}
 		zip.close();
 	}
 	/**
 	 * 
 	 * @param zip
 	 * @param e
 	 * @param f
 	 */
 	private static void entryToFile(ZipFile zip, ZipEntry e, String f)throws IOException{
 		InputStream in = null;
 		FileOutputStream out = null;
 		try {
             in = zip.getInputStream(e);
             if(in==null ){
             	return;
             }
             out = new FileOutputStream(f);
             WikiIOUtils.copy(in, out);
         } catch (IOException ie) {
         	throw ie;
         } finally {
         	WikiIOUtils.closeQuietly(in);
         	WikiIOUtils.closeQuietly(out);
         }
     }
}

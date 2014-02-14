package org.openbankdata.test.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class TestUtils {

    private TestUtils() {
    }

    /**
     * Helper method to convert an input stream into a string.
     * @param inputStream
     * @return The string representation of the given input stream.
     */
    public static String convertStreamToString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);
        scanner.useDelimiter("\\A");
        String content = scanner.hasNext() ? scanner.next() : "";
        scanner.close();
        return content;
    }

    /**
     * Helper method to get a String representation of a file's content.
     * @param filename
     * @return Content of the given file as a <code>String</code> or <code>null</code> if file does not exists.
     */
    public static String getFileContentAsString(String filename) {
        String content = null;
        try {
            InputStream file = TestUtils.class.getClassLoader()
                    .getResourceAsStream(filename);
            content = convertStreamToString(file);
            file.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return content;
    }
}

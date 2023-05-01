package dev.jfr4jdbc.artifact;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class MultiReleaseJarTest {

    private JarFile open() throws IOException {
        File artifactDir = new File("../jfr4jdbc-driver/target");
        File[] files = artifactDir.listFiles((dir, name) -> name.matches("jfr4jdbc-driver-.+\\.jar") && !(name.endsWith("-javadoc.jar") || name.endsWith("-sources.jar")));
        assertEquals(1, files.length);
        return new JarFile(files[0]);
    }

    @DisplayName("Multi Release Jar")
    @Test
    void isMultiRelease() throws Exception {

        JarFile jarFile = open();
        assertTrue(jarFile.isMultiRelease());
    }

    @DisplayName("Service Loader File exists")
    @Test
    void hasServiceLoaderFile() throws IOException {
        JarFile jarFile = open();
        assertNotNull(jarFile.getEntry("META-INF/services/java.sql.Driver"));
    }

    @DisplayName("Java 11 Directory exists")
    @Test
    void hasJava11Directory() throws IOException {
        JarFile jarFile = open();
        assertNotNull(jarFile.getEntry("META-INF/versions/11"));
    }

    private final int JAVA_8_VERSION = 52;
    private final int JAVA_11_VERSION = 55;

    @DisplayName("Classes for JDBC 4.2 exists")
    @Test
    void hasJdbc42() throws IOException {
        JarFile jarFile = open();

        assertEquals(JAVA_8_VERSION, readMajorVersion(jarFile, "dev/jfr4jdbc/Jfr4JdbcDataSource.class"));
        assertEquals(JAVA_8_VERSION, readMajorVersion(jarFile, "dev/jfr4jdbc/Jfr4JdbcDataSource42.class"));
    }

    @DisplayName("Classes for JDBC 4.3 exists")
    @Test
    void hasJdbc43() throws IOException {
        JarFile jarFile = open();

        assertEquals(JAVA_11_VERSION, readMajorVersion(jarFile, "META-INF/versions/11/dev/jfr4jdbc/Jfr4JdbcDataSource.class"));
        assertEquals(JAVA_11_VERSION, readMajorVersion(jarFile, "META-INF/versions/11/dev/jfr4jdbc/Jfr4JdbcDataSource43.class"));
    }


    private int readMajorVersion(JarFile jarFile, String path) throws IOException {

        ZipEntry classFileEntry = jarFile.getEntry(path);
        InputStream inputStream = jarFile.getInputStream(classFileEntry);

        DataInputStream dataInputStream = new DataInputStream(inputStream);
        String cafe = Integer.toHexString(dataInputStream.readUnsignedShort());
        assertEquals("cafe", cafe);
        String babe = Integer.toHexString(dataInputStream.readUnsignedShort());
        assertEquals("babe", babe);

        int minorVersion = dataInputStream.readUnsignedShort();
        int majorVersion = dataInputStream.readUnsignedShort();

        inputStream.close();

        return majorVersion;
    }

}
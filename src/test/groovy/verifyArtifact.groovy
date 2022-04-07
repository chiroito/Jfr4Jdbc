import java.util.jar.JarFile

def version = "1.3.0"

def multiReleaseJar = new JarFile(new File('./jfr4jdbc-driver/target/jfr4jdbc-driver-' + version + '.jar'))

assert multiReleaseJar.manifest.mainAttributes.getValue('Multi-Release') == 'true'

testFileExist(multiReleaseJar, 'services/java.sql.Driver')

// Java 8 (JDBC 4.2)
testMajorVersion(multiReleaseJar, 'dev/jfr4jdbc/Jfr4JdbcDataSource.class', 52)
testMajorVersion(multiReleaseJar, 'dev/jfr4jdbc/Jfr4JdbcDataSource42.class', 52)
testFileNotExist(multiReleaseJar, 'dev/jfr4jdbc/Jfr4JdbcDataSource43.class')

// Java 11 (JDBC 4.3 + JFR)
testMajorVersion(multiReleaseJar, 'META-INF/versions/11/dev/jfr4jdbc/Jfr4JdbcDataSource.class', 55)
testMajorVersion(multiReleaseJar, 'META-INF/versions/11/dev/jfr4jdbc/Jfr4JdbcDataSource43.class', 55)
testFileNotExist(multiReleaseJar, 'META-INF/versions/11/dev/jfr4jdbc/Jfr4JdbcDataSource42.class')

void testFileExist(JarFile jarFile, String classFilePath){
    assert jarFile.getEntry(classFilePath) != null
}

void testFileNotExist(JarFile jarFile, String classFilePath){
    assert jarFile.getEntry(classFilePath) == null
}


void testMajorVersion(JarFile jarFile, String classFilePath, int expectedVersion){
    assert (classFile = jarFile.getEntry(classFilePath)) != null
    assert expectedVersion == getMajor(jarFile.getInputStream(classFile))
}

int getMajor(InputStream is) {
    def dis = new DataInputStream(is)
    final String firstFourBytes = Integer.toHexString(dis.readUnsignedShort()) + Integer.toHexString(dis.readUnsignedShort())
    if (!firstFourBytes.equalsIgnoreCase("cafebabe")) {
        throw new IllegalArgumentException(dataSourceName + " is NOT a Java .class file.")
    }
    final int minorVersion = dis.readUnsignedShort()
    final int majorVersion = dis.readUnsignedShort()

    is.close();
    return majorVersion;
}

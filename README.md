# Jfr4Jdbc

## Attention
**Jfr4Jdbc is NOT Oracle product. It is required Java Flight Recorder. If you would like to know the license of Java Flight Recorder, please check the BCL For Java SE or, please contact the Oracle. **  
BCL For Java SE
http://www.oracle.com/technetwork/java/javase/terms/license/index.html  
Contact Oracle 
https://www.oracle.com/corporate/contact/index.html

### Introduction
Jfr4Jdbc is a wrapper library for JDBC. If you use the Jfr4Jdbc, you will get the information of JDBC(connection , statement, commit,etc) as an event of Java Flight Recorder. 

The following events are recorded.
- Connecting
- Connection Close
- Statement
- ResultSet
- Commit
- Rollback
- Cancel

You can choose how to use javax.sql.DataSource and java.sql.Driver.

### How to use the javax.sql.DataSource
You pass javax.sql.DataSource instance to the construct argument of chiroito.jfr4jdbc.Jfr4JdbcDataSource.

#### Sample of DataSource：
```java
OracleDataSource ds = new OracleDataSource();  
ds.setURL("jdbc:oracle:thin:user/passwd@localhost:1521:XE");  
Jfr4jdbcDataSource jds = new Jfr4jdbcDataSource(ds);  
Connection con = jds.getConnection();
```

### How to use the java.sql.Driver
Add the "jfr:" in the back of "jdbc:" of the JDBC connection string.

##### Before changing  
```java
String url = "jdbc:oracle:thin:user/passwd@localhost:1521:XE";  
Driver driver = DriverManager.getDriver(url);  
Connection con = driver.connect(url, null);  
```
##### After changing.
```java
String url = "jdbc:jfr:oracle:thin:user/passwd@localhost:1521:XE";  
Driver driver = DriverManager.getDriver(url);  
Connection con = driver.connect(url, null);  
```

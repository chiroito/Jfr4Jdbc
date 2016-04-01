# Jfr4Jdbc

## 注意事項
**Oracle Corporationおよび日本オラクルの製品ではありませんのでご注意ください。  
Java Flight Recorder が必要です。Java Flight Recorder のライセンスにつきましては BCL For Java SE をご確認またはオラクルダイレクトへお問い合わせ下さい。**  
BCL For Java SE
http://www.oracle.com/technetwork/java/javase/terms/license/index.html  
オラクルダイレクト
http://www.oracle.com/jp/direct/index.html

### Jfr4Jdbc とは

Jfr4Jdbc は JDBC のラッパーです。Jfr4Jdbc を使用することで、コネクションの接続/クローズや、SQL の実行などが Java Flight Recorder に記録されます。データベースサーバの種類を問わず利用可能です。使い方は javax.sql.DataSource を使う方法と java.sql.Driver を使う方法の 2 種類あります。  

### DataSource を使う場合
chiroito.jfr4jdbc.Jfr4JdbcDataSource のコンストラクタに DataSource のインスタンスを引数に与えて下さい。

#### OracleDataSource を使う例：
```java
OracleDataSource ds = new OracleDataSource();  
ds.setURL("jdbc:oracle:thin:user/passwd@localhost:1521:XE");  
Jfr4jdbcDataSource jds = new Jfr4jdbcDataSource(ds);  
Connection con = jds.getConnection();
```

### Driver を使う場合
chiroito.jfr4jdbc.Jfr4JdbcDriver クラスを指定しJDBCの接続子の前の方に jfr を足すだけです。

#### Driver を使う例：  
##### 変更前  
```java
String url = "jdbc:oracle:thin:user/passwd@localhost:1521:XE";  
Driver driver = DriverManager.getDriver(url);  
Connection con = driver.connect(url, null);  
```
##### 変更後  
```java
String url = "jdbc:jfr:oracle:thin:user/passwd@localhost:1521:XE";  
Driver driver = DriverManager.getDriver(url);  
Connection con = driver.connect(url, null);  
```

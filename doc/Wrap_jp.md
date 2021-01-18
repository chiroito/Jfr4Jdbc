# 特定のデータベース処理のみを記録する

Jfr4Jdbc は特定の処理で行われるデータベース操作だけを記録できます。

Jfr4Jdbc では以下のラッパーAPIを用意しています。

- DataSource (`JfrDataSource`クラス)
- Connection (`JfrConnection`クラス)
- Statement (`JfrStatement`クラス)
- PreparedStatement (`JfrPreparedStatement`クラス)
- CallableStatement (`JfrCallableStatement`クラス)
- ResultSet (`JfrResultSet`クラス)

これらのAPIを使用すると、データベース操作が記録されます。
また、ラッパーAPIのインスタンスから生成されるJDBCのオブジェクトはラップされたオブジェクトになります。
つまり、`DataSouce`オブジェクトを`JfrDataSource`オブジェクトでラップすると、`getConnection()`メソッドで得られるオブジェクトは`JfrConnection`オブジェクトです。
また、この`JfrConnection`オブジェクトで得られる`Statement`オブジェクトも`JfrStatement`オブジェクトになります。

## DataSource のラッパーを使う
Jfr4JdbcDataSource クラスのコンストラクタに DataSource のインスタンスを引数に与えて下さい。
他のJDBCのAPIも同様の方法でラップできます。

```java
PGConnectionPoolDataSource ds = new PGConnectionPoolDataSource();  
//必要な各種設定をする
Jfr4jdbcDataSource jds = new Jfr4jdbcDataSource(ds);
//jdsオブジェクトから作られるJDBCのオブジェクトは全てラップされている
Connection con = jds.getConnection();
```

## Driver のラッパーを使う
`Driver`クラスも他のクラスと同様にオブジェクトを作成してラップできます。
他にも次の様にJDBCの接続子の前の方に`jfr:`を足すことでもラッパーオブジェクトを取得できます。

### 変更前  
```java
String url = "jdbc:postgresql://localhost:5432/postgres";  
Driver driver = DriverManager.getDriver(url);  
Connection con = driver.connect(url, null);  
```
### 変更後  
```java
String url = "jdbc:jfr:postgresql://localhost:5432/postgres";  
Driver driver = DriverManager.getDriver(url);  
Connection con = driver.connect(url, null);
//conオブジェクトから作られるJDBCのオブジェクトは全てラップされている
```


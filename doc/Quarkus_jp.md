# Quarkus で Jfr4Jdbc を使う

Quarkus で Jfr4Jdbc を使用するには「Connection の実装クラスを Jfr4Jdbc でラップ」します。

Quarkus で Jfr4Jdbc を使用するには以下の 3 つのステップが必要です。

1. 依存関係の追加
1. Driverクラスの変更
1. URL の修正

### 1. 依存関係の追加

Jfr4Jdbcのクラスを読込めるようにするため`pom.xml`に依存関係を追加します。最終的に接続するRDBMSのJDBCドライバも必要になります。

```xml
<dependency>
    <groupId>dev</groupId>
    <artifactId>jfr4jdbc</artifactId>
    <version>1.0.0</version>
</dependency>
```


### 2. Driverクラスの変更
Quarkus が使用するDriverクラスをJfr4JdbcのDriverクラスに変更します。
`application.properties`ファイルで`quarkus.datasource.jdbc.driver`に`dev.jfr4jdbc.Jfr4JdbcDriver`を指定します。最終的に接続するRDBMSのDriverクラスはJfr4Jdbcが自動的に読み込みます。
```properties
quarkus.datasource.jdbc.driver=dev.jfr4jdbc.Jfr4JdbcDriver
```

### 3. URL の修正

JDBCのURLに`jfr:`を追加して、DriverがJfr4Jdbcのコネクションを生成できるようにします。。
`application.properties`ファイルで`quarkus.datasource.jdbc.url`の`jdbc:<DBドライバ名>`に`jfr:`を追加して、`jdbc:jfr:<DBドライバ名>`とします。最終的に接続するRDBMSのURLは`jfr:`が取り除かれたURLになります。

既存のURL例
```properties
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/postgres
```
修正後のURL例
```properties
quarkus.datasource.jdbc.url=jdbc:jfr:postgresql://localhost:5432/postgres
```

`Qurakus で Jfr4Jdbc を使う`は以上で完了です。
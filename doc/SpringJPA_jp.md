# Spring JPA で Jfr4Jdbc を使う

Spring JPA で Jfr4Jdbc を使用するには以下の 2 つの使用方法があります。

- A. Jfr4Jdbc-SpringBoot を使用する方法
- B. コネクションの設定を変更する方法

お奨めは前者です。

### Javaの起動オプション

AとBどちらの方法にするにしてもJavaでJDK Flight Recorder(JFR)を有効にします。JFRはJava 8 update 262以降か、Java 11以降で使用できます。JavaプロセスのVM引数に以下のような起動オプションを追加します。

```
-XX:StartFlightRecording=dumponexit=true,filename=dump.jfr
```

これでJFRが有効になりプロセスの終了後に`dump.jfr`というファイルが作成されます。

## A. Jfr4Jdbc-SpringBoot を使用する方法

最も簡単にSpringBootでJfr4Jdbcを使う方法です。自動構成によってデータソースをJfr4Jdbcのデータソースでラップします。

使用方法は既存のアプリケーションの`pom.xml`に以下のようなJfr4Jdbc-SpringBootの依存関係を追加するだけです。

```xml
<dependency>
    <groupId>dev.jfr4jdbc</groupId>
    <artifactId>jfr4jdbc-springboot</artifactId>
    <version>1.0.0</version>
</dependency>
```

## B. コネクションの設定を変更する方法

Aの方法でSpringBootの自動構成がうまくいかない場合などはこちらの方法をご利用ください。コネクションの設定

Spring JPA で Jfr4Jdbc を使用するには以下の 3 つのステップが必要です。

1. Jfr4Jdbcの依存関係を追加
1. Driverクラスの変更
1. URL の修正

### B-1. Jfr4Jdbcの依存関係を追加

SpringBootでJfr4Jdbcを使えるようにします。これによってJfr4JdbcのJDBCドライバなどが使えるようになります。

アプリケーションの`pom.xml`に以下の依存関係を追加します。

```xml
<dependency>
    <groupId>dev.jfr4jdbc</groupId>
    <artifactId>jfr4jdbc-driver</artifactId>
    <version>1.1.0</version>
</dependency>
```


### B-2. Driverクラスの変更
Spring JPA が使用するDriverクラスをJfr4JdbcのDriverクラスに変更します。
`application.properties`ファイルで`spring.datasource.driver-class-name`に`dev.jfr4jdbc.Jfr4JdbcDriver`を指定します。最終的に接続するRDBMSのDriverクラスはJfr4Jdbcが自動的に読み込みます。
```properties
spring.datasource.driver-class-name=dev.jfr4jdbc.Jfr4JdbcDriver
```

### B-3. URL の修正

URLから使用するJDBCドライバを決定するため、JDBCのURLに`jfr:`を追加します。これで、DriverがJfr4Jdbcのコネクションを生成できるようにします。。
`application.properties`ファイルで`spring.datasource.url`の`jdbc:<DBドライバ名>`に`jfr:`を挿入して、`jdbc:jfr:<DBドライバ名>`とします。最終的に接続するRDBMSのURLは`jfr:`が取り除かれたURLになります。

既存のURL例
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
```
修正後のURL例
```properties
spring.datasource.url=jdbc:jfr:postgresql://localhost:5432/postgres
```

`コネクションの設定を変更する方法`は以上で完了です。
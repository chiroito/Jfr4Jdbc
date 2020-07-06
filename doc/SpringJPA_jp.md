# Spring JPA で Jfr4Jdbc を使う

Spring JPA で Jfr4Jdbc を使用するには以下の 2 つの使用方法があります。

1. HikariDataSource を Jfr4Jdbc でラップする方法
1. Connection の実装クラスを Jfr4Jdbc でラップする方法

お奨めは前者です。どちらの方法でもJfr4Jdbcへの依存関係の追加が必要です。

### 依存関係の追加

Jfr4Jdbcのクラスを読込めるようにするため`pom.xml`に依存関係を追加します。最終的に接続するRDBMSのJDBCドライバも必要になります。

```xml
<dependency>
    <groupId>dev</groupId>
    <artifactId>jfr4jdbc</artifactId>
    <version>1.0.0</version>
</dependency>
```

## HikariDataSource を Jfr4Jdbc でラップする方法

この方法では`HikariDataSource`を`Jfr4JdbcDataSource`でラップします。これによって、DataSourceから取り出される全てのコネクションは Jfr4Jdbc にラップされます。そのため、この方法では JDBC の URL の変更や、ドライバの変更は不要です。

JfrDataSourceConfiguration.java
```java
import com.zaxxer.hikari.HikariDataSource;
import dev.jfr4jdbc.Jfr4JdbcDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

@Configuration
public class JfrDataSourceConfiguration {

    @ConditionalOnClass({HikariDataSource.class})
    @ConditionalOnMissingBean({DataSource.class})
    @ConditionalOnProperty(
            name = {"spring.datasource.type"},
            havingValue = "com.zaxxer.hikari.HikariDataSource",
            matchIfMissing = true
    )
    static class JfrDataSource {
        JfrDataSource() {
        }

        @Bean
        @ConfigurationProperties(
                prefix = "spring.datasource.hikari"
        )
        DataSource dataSource(DataSourceProperties properties) {
            HikariDataSource dataSource = (HikariDataSource) JfrDataSourceConfiguration.createDataSource(properties, HikariDataSource.class);
            if (StringUtils.hasText(properties.getName())) {
                dataSource.setPoolName(properties.getName());
            }
            return new Jfr4JdbcDataSource(dataSource);
        }
    }

    @SuppressWarnings("unchecked")
    protected static <T> T createDataSource(DataSourceProperties properties, Class<? extends DataSource> type) {
        return (T) properties.initializeDataSourceBuilder().type(type).build();
    }
}
```
設定ファイルは以下のとおり何も変更は必要ありません。


application.properties
```properties
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
```

`HikariDataSource を Jfr4Jdbc でラップする方法`は以上で完了です。

## Connection の実装クラスを Jfr4Jdbc でラップする方法

Spring JPA で Jfr4Jdbc を使用するには以下の 2 つのステップが必要です。

1. Driverクラスの変更
1. URL の修正

## Driverクラスの変更
Spring JPA が使用するDriverクラスをJfr4JdbcのDriverクラスに変更します。
`application.properties`ファイルで`spring.datasource.driver-class-name`に`dev.jfr4jdbc.Jfr4JdbcDriver`を指定します。最終的に接続するRDBMSのDriverクラスはJfr4Jdbcが自動的に読み込みます。
```properties
spring.datasource.driver-class-name=dev.jfr4jdbc.Jfr4JdbcDriver
```

## URL の修正

JDBCのURLに`jfr:`を追加して、DriverがJfr4Jdbcのコネクションを生成できるようにします。。
`application.properties`ファイルで`spring.datasource.url`の`jdbc:<DBドライバ名>`に`jfr:`を追加して、`jdbc:jfr:<DBドライバ名>`とします。最終的に接続するRDBMSのURLは`jfr:`が取り除かれたURLになります。

既存のURL例
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
```
修正後のURL例
```properties
spring.datasource.url=jdbc:jfr:postgresql://localhost:5432/postgres
```

`Connection の実装クラスを Jfr4Jdbc でラップする方法`は以上で完了です。
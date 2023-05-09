# Introduction

To support JDBC 4.2 on Java 8 and JDBC 4.3 on Java 11, you need to build this on Java 11.
Java 9 and 10 are no longer supported.

To reduce the dependency, you need to use both pom.xml in root directory and one in jfr4jdbc-driver directory.

# Test

In root directory of Jfr4Jdbc, you can test this as usual.

```shell
bin/test.sh
bin/native-image-test.sh # GraalVM is required
```

# Build

In jfr4jdbc-driver directory, you can build this as multi release JAR file.

```shell
cd jfr4jdbc-driver
mvn package
```


# Github Actions
You can execute workflows locally using nektos/act like followings.

```
act -W .github/workflows/build-test.yml
act -W .github/workflows/release-snapshot.yml -e event.json -s MAVEN_GPG_PRIVATE_KEY="$(< gpg.pem)" --secret-file .secrets
act -W .github/workflows/release-production.yml -e event.json -s MAVEN_GPG_PRIVATE_KEY="$(< gpg.pem)" --secret-file .secrets
```

.securets
```
MAVEN_GPG_PASSPHRASE=xxx
MAVEN_USERNAME=xxx
MAVEN_USER_PASSWORD=xxx
```

event.json
```
{
  "release": {
    "tag_name": "vworkflow-SNAPSHOT"
  }
}
```

gpg.pem
```
-----BEGIN PGP PRIVATE KEY BLOCK-----

key information
-----END PGP PRIVATE KEY BLOCK-----
```
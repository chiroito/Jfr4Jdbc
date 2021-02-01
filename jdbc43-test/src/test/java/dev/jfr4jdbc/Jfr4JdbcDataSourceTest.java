package dev.jfr4jdbc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class Jfr4JdbcDataSourceTest {

    @Mock
    DataSource delegatedDs;

    @BeforeEach
    void init() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createConnectionBuilder() throws Exception {
        Jfr4JdbcDataSource dataSource = new Jfr4JdbcDataSource(delegatedDs);
        dataSource.createConnectionBuilder();

        verify(delegatedDs).createConnectionBuilder();
    }

    @Test
    void createShardingKeyBuilder() throws Exception {
        Jfr4JdbcDataSource dataSource = new Jfr4JdbcDataSource(delegatedDs);
        dataSource.createShardingKeyBuilder();

        verify(delegatedDs).createShardingKeyBuilder();
    }
}
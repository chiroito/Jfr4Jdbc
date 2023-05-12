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
class JfrDataSourceTest {

    @Mock
    DataSource delegatedDs;

    @BeforeEach
    void init() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createConnectionBuilder() throws Exception {
        JfrDataSource dataSource = new JfrDataSource(delegatedDs);
        dataSource.createConnectionBuilder();

        verify(delegatedDs).createConnectionBuilder();
    }

    @Test
    void createShardingKeyBuilder() throws Exception {
        JfrDataSource dataSource = new JfrDataSource(delegatedDs);
        dataSource.createShardingKeyBuilder();

        verify(delegatedDs).createShardingKeyBuilder();
    }
}
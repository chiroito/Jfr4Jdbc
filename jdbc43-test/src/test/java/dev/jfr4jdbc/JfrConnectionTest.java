package dev.jfr4jdbc;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JfrConnectionTest {

    @Mock
    private Connection delegatedCon;

    @BeforeAll
    static void initClass() throws Exception {
    }

    @BeforeEach
    void init() throws Exception {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void beginRequest() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.beginRequest();
        }

        verify(this.delegatedCon).beginRequest();
    }

    @Test
    void endRequest() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.endRequest();
        }

        verify(this.delegatedCon).endRequest();
    }

    @Test
    void setShardingKeyIfValid() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.setShardingKeyIfValid(null, 0);
        }

        verify(this.delegatedCon).setShardingKeyIfValid(null, 0);
    }

    @Test
    void setShardingKeyIfValid1() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.setShardingKeyIfValid(null, null, 0);
        }

        verify(this.delegatedCon).setShardingKeyIfValid(null, null, 0);
    }

    @Test
    void setShardingKey() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.setShardingKey(null);
        }

        verify(this.delegatedCon).setShardingKey(null);
    }

    @Test
    void setShardingKey1() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.setShardingKey(null, null);
        }

        verify(this.delegatedCon).setShardingKey(null, null);
    }
}
package dev.jfr4jdbc;

import dev.jfr4jdbc.event.jfr.JfrStatementEvent;
import dev.jfr4jdbc.interceptor.MockInterceptorFactory;
import dev.jfr4jdbc.interceptor.StatementContext;
import jdk.jfr.consumer.RecordedEvent;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Order(10)
public class ParameterTest {

    @Mock
    private PreparedStatement delegatePstate;

    @BeforeEach
    void init() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("bind single parameter test")
    @Test
    void singleParameterByExecuteQuery() throws Exception {
        MockInterceptorFactory mockInterceptorFactory = new MockInterceptorFactory();
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, JfrStatementTest.SAMPLE_SQL, mockInterceptorFactory);
        statement.setString(0, "a");
        statement.executeQuery();

        List<StatementContext> events = mockInterceptorFactory.createStatementInterceptor().getAllPostEvents();
        assertEquals(1, events.size());
        StatementContext event = events.get(0);
        assertEquals("0=a", event.getInquiryParameter());
    }

    @DisplayName("bind multi parameter test")
    @Test
    void multiParameterByExecuteQuery() throws Exception {
        MockInterceptorFactory mockInterceptorFactory = new MockInterceptorFactory();
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, JfrStatementTest.SAMPLE_SQL, mockInterceptorFactory);
        statement.setString(0, "a");
        statement.setInt(1, 100);
        statement.executeQuery();

        List<StatementContext> events = mockInterceptorFactory.createStatementInterceptor().getAllPostEvents();
        assertEquals(1, events.size());
        StatementContext event = events.get(0);
        assertEquals("0=a, 1=100", event.getInquiryParameter());
    }

    @DisplayName("bind single parameter twice test")
    @Test
    void singleParameterTwiceByExecuteQuery() throws Exception {
        MockInterceptorFactory mockInterceptorFactory = new MockInterceptorFactory();
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, JfrStatementTest.SAMPLE_SQL, mockInterceptorFactory);
        statement.setString(0, "a");
        statement.executeQuery();

        statement.setString(0, "b");
        statement.executeQuery();

        List<StatementContext> events = mockInterceptorFactory.createStatementInterceptor().getAllPostEvents();
        assertEquals(2, events.size());
        StatementContext event1 = events.get(0);
        assertEquals("0=a", event1.getInquiryParameter());
        StatementContext event2 = events.get(1);
        assertEquals("0=b", event2.getInquiryParameter());
    }

    @DisplayName("clear parameter test")
    @Test
    void clearParameterByExecuteQuery() throws Exception {
        MockInterceptorFactory mockInterceptorFactory = new MockInterceptorFactory();
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, JfrStatementTest.SAMPLE_SQL, mockInterceptorFactory);
        statement.setString(0, "a");
        statement.clearParameters();

        statement.setString(0, "b");
        statement.executeQuery();

        List<StatementContext> events = mockInterceptorFactory.createStatementInterceptor().getAllPostEvents();
        assertEquals(1, events.size());
        StatementContext event = events.get(0);
        assertEquals("0=b", event.getInquiryParameter());
    }
}
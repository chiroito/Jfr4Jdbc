package dev.jfr4jdbc.interceptor;

public interface Interceptor<CONTEXT> {

    void preInvoke(CONTEXT context);

    void postInvoke(CONTEXT context);
}

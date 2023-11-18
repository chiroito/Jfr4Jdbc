package dev.jfr4jdbc.interceptor;

import dev.jfr4jdbc.interceptor.impl.instant.InstantInterceptorFactory;
import dev.jfr4jdbc.interceptor.impl.legacy.LegacyInterceptorFactory;
import dev.jfr4jdbc.interceptor.impl.period.PeriodInterceptorFactory;
import dev.jfr4jdbc.internal.Jfr4Jdbc;

public class InterceptorManager {

    static {
        Jfr4Jdbc.initialize();
    }

    private static InterceptorFactory defaultInterceptorFactory;

    public static InterceptorFactory getDefaultInterceptorFactory() {
        return defaultInterceptorFactory;
    }

    public static void setDefaultInterceptorFactory(InterceptorFactory interceptorFactory) {
        defaultInterceptorFactory = interceptorFactory;
    }

    public static InterceptorFactory getInterceptorFactory(String defaultInterceptorParameter) {

        if (defaultInterceptorParameter != null) {

            if (defaultInterceptorParameter.toLowerCase().equals("instant")) {
                return new InstantInterceptorFactory();

            } else if (defaultInterceptorParameter.toLowerCase().equals("legacy")) {
                return new LegacyInterceptorFactory();
            }
        }
        return new PeriodInterceptorFactory();
    }
}

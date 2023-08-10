package dev.jfr4jdbc.interceptor;

import dev.jfr4jdbc.interceptor.impl.instant.InstantInterceptorFactory;
import dev.jfr4jdbc.interceptor.impl.legacy.LegacyInterceptorFactory;
import dev.jfr4jdbc.interceptor.impl.period.PeriodInterceptorFactory;

public class InterceptorManager {

    private final static String DEFAULT_INTERCEPTOR_FACTORY_ENV_NAME = "jfr-interceptor-factory";

    public static InterceptorFactory getDefaultInterceptorFactory() {
        return defaultInterceptorFactory;
    }

    private static InterceptorFactory defaultInterceptorFactory;

    static {
        String defaultInterceptorParameter = System.getProperty(DEFAULT_INTERCEPTOR_FACTORY_ENV_NAME);
        defaultInterceptorFactory = getInterceptorFactory(defaultInterceptorParameter);
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

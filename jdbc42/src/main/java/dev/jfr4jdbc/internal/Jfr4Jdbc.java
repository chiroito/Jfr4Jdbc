package dev.jfr4jdbc.internal;

import dev.jfr4jdbc.interceptor.InterceptorManager;
import jdk.jfr.FlightRecorder;

public class Jfr4Jdbc {

    private final static String DEFAULT_INTERCEPTOR_FACTORY_ENV_NAME = "jfr-interceptor-factory";

    private static volatile boolean initialized = false;

    public static void initialize() {
        synchronized (Jfr4Jdbc.class) {
            if (!isInitialized()) {
                String defaultInterceptorParameter = System.getProperty(DEFAULT_INTERCEPTOR_FACTORY_ENV_NAME);
                InterceptorManager.setDefaultInterceptorFactory(InterceptorManager.getInterceptorFactory(defaultInterceptorParameter));

                JfrResourceWriter writer = new JfrResourceWriter();
                ResourceMonitorManager manager = ResourceMonitorManager.getInstance();
                FlightRecorder.addPeriodicEvent(JfrConnectionResourceEvent.class, () -> {
                    manager.getMonitors().stream().forEach(m -> writer.write(m));
                });

                initialized = true;
            }
        }
    }

    public static boolean isInitialized() {
        return initialized;
    }
}

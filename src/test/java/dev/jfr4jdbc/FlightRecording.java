package dev.jfr4jdbc;

import dev.jfr4jdbc.event.jfr.*;
import jdk.jfr.Recording;
import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordingFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FlightRecording {

    private Path dumpFilePath;
    private Recording recording;

    private FlightRecording() {
    }

    public static FlightRecording start() throws Jfr4jdbcTestException {

        FlightRecording fr = new FlightRecording();

        try {
            fr.dumpFilePath = Files.createTempFile("jfr", ".jfr").toRealPath();
            fr.recording = new Recording();
            fr.recording.enable(JfrCancelEvent.class);
            fr.recording.enable(JfrCloseEvent.class);
            fr.recording.enable(JfrCommitEvent.class);
            fr.recording.enable(JfrConnectionEvent.class);
            fr.recording.enable(JfrResultSetEvent.class);
            fr.recording.enable(JfrRollbackEvent.class);
            fr.recording.enable(JfrStatementEvent.class);
            fr.recording.start();
        } catch (Exception e) {
            throw new Jfr4jdbcTestException(e);
        }

        return fr;
    }

    public void stop() throws Jfr4jdbcTestException {
        try {
            recording.disable(JfrCancelEvent.class);
            recording.disable(JfrCloseEvent.class);
            recording.disable(JfrCommitEvent.class);
            recording.disable(JfrConnectionEvent.class);
            recording.disable(JfrResultSetEvent.class);
            recording.disable(JfrRollbackEvent.class);
            recording.disable(JfrStatementEvent.class);
            recording.dump(this.dumpFilePath);
            recording.stop();
            recording.close();
        } catch (Exception e) {
            throw new Jfr4jdbcTestException(e);
        }
    }

    public List<RecordedEvent> getEvents() throws Jfr4jdbcTestException {
        try {
            return RecordingFile.readAllEvents(this.dumpFilePath);
        } catch (Exception e) {
            throw new Jfr4jdbcTestException(e);
        }
    }
}

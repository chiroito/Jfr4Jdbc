package dev.jfr4jdbc;

import jdk.jfr.Label;
import jdk.jfr.MetadataDefinition;
import jdk.jfr.Name;
import jdk.jfr.Relational;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@MetadataDefinition
@Relational
@Name("dev.jfr4jdbc.ConnectionId")
@Label("Connection ID")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConnectionId {
}

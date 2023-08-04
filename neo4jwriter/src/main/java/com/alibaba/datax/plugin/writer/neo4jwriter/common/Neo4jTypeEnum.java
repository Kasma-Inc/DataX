package com.alibaba.datax.plugin.writer.neo4jwriter.common;

public enum Neo4jTypeEnum {

    VERTEX(" "),

    EDGE(" WITH true AS ignore ");

    private final String delimiter;

    Neo4jTypeEnum(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public boolean isVertex() {
        return this == VERTEX;
    }

    public boolean isEdge() {
        return this == EDGE;
    }
}

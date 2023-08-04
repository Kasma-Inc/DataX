package com.alibaba.datax.plugin.writer.neo4jwriter.common;

public enum Neo4jDirectionEnum {

    DIRECTED("->"),

    UNDIRECTED("-");

    private final String display;

    Neo4jDirectionEnum(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}

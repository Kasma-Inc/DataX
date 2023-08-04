package com.alibaba.datax.plugin.writer.neo4jwriter.common;

public enum Neo4jWriteModeEnum {

    CREATE,

    MERGE;

    Neo4jWriteModeEnum() {
    }

    public boolean isCreateMode() {
        return this == CREATE;
    }

    public boolean isMergeMode() {
        return this == MERGE;
    }
}

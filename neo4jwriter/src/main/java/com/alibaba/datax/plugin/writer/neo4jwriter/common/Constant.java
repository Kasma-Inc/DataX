package com.alibaba.datax.plugin.writer.neo4jwriter.common;

public class Constant {

    public static final String BOLT_URL = "boltUrl";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    public static final String LABEL = "label";
    public static final String TYPE = "type";
    public static final String PROPERTIES = "properties";
    public static final String SOURCE_TYPE = "sourceType";
    public static final String SOURCE_FIELD = "sourceField";
    public static final String SOURCE_UNIQUE_FIELD = "sourceUniqueField";
    public static final String TARGET_TYPE = "targetType";
    public static final String TARGET_FIELD = "targetField";
    public static final String TARGET_UNIQUE_FIELD = "targetUniqueField";
    public static final String DIRECTION = "direction";
    public static final String PRE_STATEMENTS = "preStatements";
    public static final String POST_STATEMENTS = "postStatements";
    public static final String WRITE_MODE = "writeMode";
    public static final String BATCH_SIZE = "batchSize";

    public static final String CREATE_VERTEX_FORMAT = "CREATE (:%s%s)";
    public static final String MERGE_VERTEX_FORMAT = "MERGE (:%s%s)";
    public static final String CREATE_EDGE_FORMAT = "MATCH (a:%s%s) MATCH (b:%s%s) CREATE (a)-[:%s%s]%s(b)";
    public static final String MERGE_EDGE_FORMAT = "MATCH (a:%s%s) MATCH (b:%s%s) MERGE (a)-[:%s%s]%s(b)";

}

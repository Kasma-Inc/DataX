package com.alibaba.datax.plugin.writer.neo4jwriter;

import com.alibaba.datax.plugin.writer.neo4jwriter.common.Neo4jDirectionEnum;
import com.alibaba.datax.plugin.writer.neo4jwriter.common.Neo4jTypeEnum;
import com.alibaba.datax.plugin.writer.neo4jwriter.common.Neo4jWriteModeEnum;
import java.io.Serializable;
import java.util.List;

public class Neo4jRecordProperty implements Serializable {

    private Neo4jTypeEnum type;

    private String label;

    private List<String> properties;

    private String sourceType;

    private String sourceField;

    private String sourceUniqueField;

    private String targetType;

    private String targetField;

    private String targetUniqueField;

    private Neo4jDirectionEnum direction = Neo4jDirectionEnum.UNDIRECTED;

    private Neo4jWriteModeEnum writeMode = Neo4jWriteModeEnum.CREATE;

    public Neo4jRecordProperty() {
    }

    public Neo4jTypeEnum getType() {
        return type;
    }

    public void setType(Neo4jTypeEnum type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<String> getProperties() {
        return properties;
    }

    public void setProperties(List<String> properties) {
        this.properties = properties;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceField() {
        return sourceField;
    }

    public void setSourceField(String sourceField) {
        this.sourceField = sourceField;
    }

    public String getSourceUniqueField() {
        return sourceUniqueField;
    }

    public void setSourceUniqueField(String sourceUniqueField) {
        this.sourceUniqueField = sourceUniqueField;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetField() {
        return targetField;
    }

    public void setTargetField(String targetField) {
        this.targetField = targetField;
    }

    public String getTargetUniqueField() {
        return targetUniqueField;
    }

    public void setTargetUniqueField(String targetUniqueField) {
        this.targetUniqueField = targetUniqueField;
    }

    public Neo4jDirectionEnum getDirection() {
        return direction;
    }

    public void setDirection(Neo4jDirectionEnum direction) {
        this.direction = direction;
    }

    public Neo4jWriteModeEnum getWriteMode() {
        return writeMode;
    }

    public void setWriteMode(Neo4jWriteModeEnum writeMode) {
        this.writeMode = writeMode;
    }

    @Override
    public String toString() {
        return "Neo4jRecordProperty{" +
                "type=" + type +
                ", label='" + label + '\'' +
                ", properties=" + properties +
                ", sourceType='" + sourceType + '\'' +
                ", sourceField='" + sourceField + '\'' +
                ", sourceUniqueField='" + sourceUniqueField + '\'' +
                ", targetType='" + targetType + '\'' +
                ", targetField='" + targetField + '\'' +
                ", targetUniqueField='" + targetUniqueField + '\'' +
                ", direction=" + direction +
                ", writeMode=" + writeMode +
                '}';
    }

    public static Neo4jRecordPropertyBuilder builder() {
        return new Neo4jRecordPropertyBuilder();
    }

    public static class Neo4jRecordPropertyBuilder {

        private static final Neo4jRecordProperty INSTANCE = new Neo4jRecordProperty();

        private Neo4jRecordPropertyBuilder() {
        }

        public Neo4jRecordPropertyBuilder type(Neo4jTypeEnum type) {
            INSTANCE.type = type;
            return this;
        }

        public Neo4jRecordPropertyBuilder label(String label) {
            INSTANCE.label = label;
            return this;
        }

        public Neo4jRecordPropertyBuilder properties(List<String> properties) {
            INSTANCE.properties = properties;
            return this;
        }

        public Neo4jRecordPropertyBuilder sourceType(String sourceType) {
            INSTANCE.sourceType = sourceType;
            return this;
        }

        public Neo4jRecordPropertyBuilder sourceField(String sourceField) {
            INSTANCE.sourceField = sourceField;
            return this;
        }

        public Neo4jRecordPropertyBuilder sourceUniqueField(String sourceUniqueField) {
            INSTANCE.sourceUniqueField = sourceUniqueField;
            return this;
        }

        public Neo4jRecordPropertyBuilder targetType(String targetType) {
            INSTANCE.targetType = targetType;
            return this;
        }

        public Neo4jRecordPropertyBuilder targetField(String targetField) {
            INSTANCE.targetField = targetField;
            return this;
        }

        public Neo4jRecordPropertyBuilder targetUniqueField(String targetUniqueField) {
            INSTANCE.targetUniqueField = targetUniqueField;
            return this;
        }

        public Neo4jRecordPropertyBuilder direction(Neo4jDirectionEnum direction) {
            INSTANCE.direction = direction;
            return this;
        }

        public Neo4jRecordPropertyBuilder writeMode(Neo4jWriteModeEnum writeMode) {
            INSTANCE.writeMode = writeMode;
            return this;
        }

        public Neo4jRecordProperty build() {
            return INSTANCE;
        }
    }
}

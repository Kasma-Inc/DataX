package com.alibaba.datax.plugin.writer.neo4jwriter;

import com.alibaba.datax.common.element.Record;
import com.alibaba.datax.plugin.writer.neo4jwriter.common.Constant;
import com.alibaba.datax.plugin.writer.neo4jwriter.common.Neo4jDirectionEnum;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Neo4jWriterHelper {

    public static String buildCommand(Record record, Neo4jRecordProperty property) {
        if (property.getType().isVertex()) {
            return buildVertex(record, property);
        } else {
            return buildEdge(record, property);
        }
    }

    public static String buildVertex(Record record, Neo4jRecordProperty property) {
        String format;
        if (property.getWriteMode().isCreateMode()) {
            format = Constant.CREATE_VERTEX_FORMAT;
        } else {
            format = Constant.MERGE_VERTEX_FORMAT;
        }
        Map<String, Object> propertyMap = new LinkedHashMap<>();
        List<String> properties = property.getProperties();
        for (int i = 0; i < properties.size(); i++) {
            String propertyName = properties.get(i);
            Object propertyData = record.getColumn(i).getRawData();
            propertyMap.put(propertyName, propertyData);
        }
        return String.format(format, property.getLabel(), buildProperties(propertyMap));
    }

    public static String buildEdge(Record record, Neo4jRecordProperty property) {
        String format;
        if (property.getWriteMode().isCreateMode()) {
            format = Constant.CREATE_EDGE_FORMAT;
        } else {
            format = Constant.MERGE_EDGE_FORMAT;
        }
        String sourceType = property.getSourceType();
        String sourceField = property.getSourceField();
        String sourceUniqueField = property.getSourceUniqueField();
        String targetType = property.getTargetType();
        String targetField = property.getTargetField();
        String targetUniqueField = property.getTargetUniqueField();
        Neo4jDirectionEnum direction = property.getDirection();
        List<String> properties = property.getProperties();
        int sourceFieldIndex = -1;
        int targetFieldIndex = -1;
        for (int i = 0; i < properties.size(); i++) {
            if (properties.get(i).equalsIgnoreCase(sourceField)) {
                sourceFieldIndex = i;
            }
            if (properties.get(i).equalsIgnoreCase(targetField)) {
                targetFieldIndex = i;
            }
        }
        String sourceProperties = "";
        String targetProperties = "";
        Map<String, Object> edgeProperties = new LinkedHashMap<>();
        for (int i = 0; i < properties.size(); i++) {
            if (i == sourceFieldIndex) {
                Object propertyData = record.getColumn(i).getRawData();
                Map<String, Object> map = new LinkedHashMap<>();
                map.put(sourceUniqueField, propertyData);
                sourceProperties = buildProperties(map);
            } else if (i == targetFieldIndex) {
                Object propertyData = record.getColumn(i).getRawData();
                Map<String, Object> map = new LinkedHashMap<>();
                map.put(targetUniqueField, propertyData);
                targetProperties = buildProperties(map);
            } else {
                String propertyName = properties.get(i);
                Object propertyData = record.getColumn(i).getRawData();
                edgeProperties.put(propertyName, propertyData);
            }
        }
        return String.format(format, sourceType, sourceProperties, targetType, targetProperties,
                property.getLabel(), buildProperties(edgeProperties), direction.getDisplay());
    }

    public static String buildProperties(Map<String, Object> properties) {
        return "{" + properties.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + (entry.getValue() instanceof Number
                        ? entry.getValue() : "'" + entry.getValue() + "'"))
                .collect(Collectors.joining(", ")) + "}";
    }
}

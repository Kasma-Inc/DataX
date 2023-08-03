package com.alibaba.datax.plugin.reader.neo4jreader;

import com.alibaba.datax.common.element.BoolColumn;
import com.alibaba.datax.common.element.BytesColumn;
import com.alibaba.datax.common.element.Column;
import com.alibaba.datax.common.element.DateColumn;
import com.alibaba.datax.common.element.DoubleColumn;
import com.alibaba.datax.common.element.LongColumn;
import com.alibaba.datax.common.element.Record;
import com.alibaba.datax.common.element.StringColumn;
import com.alibaba.datax.common.plugin.TaskPluginCollector;
import com.google.common.collect.Lists;
import java.sql.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.neo4j.driver.Value;
import org.neo4j.driver.internal.types.TypeConstructor;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Relationship;
import org.neo4j.driver.util.Pair;

public class Neo4jReaderHelper {

    static Record buildRecord(Record record, org.neo4j.driver.Record row,
            TaskPluginCollector taskPluginCollector) {
        try {
            List<Pair<String, Value>> fields = row.fields();
            for (Pair<String, Value> field : fields) {
                record.addColumn(convertValueToColumn(field.value()));
            }
            return record;
        } catch (Exception e) {
            taskPluginCollector.collectDirtyRecord(record, e);
            return null;
        }
    }

    static Column convertValueToColumn(Value value) {
        Column column;
        // datax support data type is: bool, byte[], date, double, long, string
        if (TypeConstructor.NODE.covers(value)) {
            Node node = value.asNode();
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", node.id());
            map.put("elementId", node.elementId());
            map.put("labels", Lists.newArrayList(node.labels()));
            map.putAll(node.asMap());
            column = new StringColumn(map.toString());
        } else if (TypeConstructor.RELATIONSHIP.covers(value)) {
            Map<String, Object> map = new LinkedHashMap<>();
            Relationship relationship = value.asRelationship();
            map.put("id", relationship.id());
            map.put("elementId", relationship.elementId());
            map.put("startNodeId", relationship.startNodeId());
            map.put("startNodeElementId", relationship.startNodeElementId());
            map.put("endNodeId", relationship.endNodeId());
            map.put("endNodeElementId", relationship.endNodeElementId());
            map.put("type", relationship.type());
            map.putAll(relationship.asMap());
            column = new StringColumn(map.toString());
        } else if (TypeConstructor.BOOLEAN.covers(value)) {
            column = new BoolColumn(value.asBoolean());
        } else if (TypeConstructor.BYTES.covers(value)) {
            column = new BytesColumn(value.asByteArray());
        } else if (TypeConstructor.DATE.covers(value)) {
            column = new DateColumn(Date.valueOf(value.asLocalDate()));
        } else if (TypeConstructor.DATE_TIME.covers(value)) {
            column = new DateColumn(Date.valueOf(value.asZonedDateTime().toLocalDate()));
        } else if (TypeConstructor.LOCAL_DATE_TIME.covers(value)) {
            column = new DateColumn(Date.valueOf(value.asLocalDateTime().toLocalDate()));
        } else if (TypeConstructor.FLOAT.covers(value)) {
            column = new DoubleColumn(value.asDouble());
        } else if (TypeConstructor.INTEGER.covers(value)) {
            column = new LongColumn(value.asInt());
        } else if (TypeConstructor.STRING.covers(value)) {
            column = new StringColumn(value.asString());
        } else {
            // string default
            column = new StringColumn(value.toString());
        }
        return column;
    }

}

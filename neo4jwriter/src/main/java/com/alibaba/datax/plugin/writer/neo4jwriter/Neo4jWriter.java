package com.alibaba.datax.plugin.writer.neo4jwriter;

import com.alibaba.datax.common.element.Record;
import com.alibaba.datax.common.plugin.RecordReceiver;
import com.alibaba.datax.common.spi.Writer;
import com.alibaba.datax.common.util.Configuration;
import com.alibaba.datax.plugin.writer.neo4jwriter.common.Constant;
import com.alibaba.datax.plugin.writer.neo4jwriter.common.Neo4jDirectionEnum;
import com.alibaba.datax.plugin.writer.neo4jwriter.common.Neo4jTypeEnum;
import com.alibaba.datax.plugin.writer.neo4jwriter.common.Neo4jWriteModeEnum;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Neo4jWriter extends Writer {

    private static final Logger LOG = LoggerFactory.getLogger(Neo4jWriter.class);

    public static class Job extends Writer.Job {

        private Configuration jobConfig = null;

        @Override
        public void init() {
            LOG.info("Begin to initialize neo4j job.");
            this.jobConfig = super.getPluginJobConf();
            String boltUrl = jobConfig.getString(Constant.BOLT_URL, "bolt://127.0.0.1:7687");
            String username = jobConfig.getString(Constant.USERNAME, "neo4j");
            String password = jobConfig.getString(Constant.PASSWORD, "neo4j123");
            int batchSize = jobConfig.getInt(Constant.BATCH_SIZE, 2048);
            String writeMode = jobConfig.getString(Constant.WRITE_MODE, "CREATE");
            jobConfig.set(Constant.BOLT_URL, boltUrl);
            jobConfig.set(Constant.USERNAME, username);
            jobConfig.set(Constant.PASSWORD, password);
            jobConfig.set(Constant.BATCH_SIZE, batchSize);
            jobConfig.set(Constant.WRITE_MODE, writeMode);
            LOG.info("Initialize neo4j job success, neo4j config: {}", jobConfig.toJSON());
        }

        @Override
        public void destroy() {
        }

        @Override
        public List<Configuration> split(int mandatoryNumber) {
            LOG.info("Begin to split neo4j job.");
            // todo split job
            List<Configuration> list = new ArrayList<>();
            list.add(jobConfig);
            LOG.info("Split neo4j job finished, job size: {}", list.size());
            return Lists.newArrayList(jobConfig);
        }
    }

    public static class Task extends Writer.Task {

        private Driver driver = null;

        private int batchSize;

        private Neo4jRecordProperty property;

        private List<String> preStatements;

        private List<String> postStatements;

        @Override
        public void init() {
            LOG.info("Begin to initialize neo4j write task.");
            Configuration jobConfig = super.getPluginJobConf();
            String boltUrl = jobConfig.getString(Constant.BOLT_URL);
            String username = jobConfig.getString(Constant.USERNAME);
            String password = jobConfig.getString(Constant.PASSWORD);
            Neo4jTypeEnum type = Neo4jTypeEnum.valueOf(
                    jobConfig.getString(Constant.TYPE).toUpperCase());
            String label = jobConfig.getString(Constant.LABEL);
            List<String> properties = jobConfig.getList(Constant.PROPERTIES, String.class);
            Neo4jWriteModeEnum writeMode = Neo4jWriteModeEnum.valueOf(
                    jobConfig.getString(Constant.WRITE_MODE));
            if (type.isVertex()) {
                property = Neo4jRecordProperty.builder()
                        .type(type)
                        .label(label)
                        .properties(properties)
                        .writeMode(writeMode)
                        .build();
            } else {
                String direction = jobConfig.getString(Constant.DIRECTION,
                        Neo4jDirectionEnum.UNDIRECTED.name());
                property = Neo4jRecordProperty.builder()
                        .type(type)
                        .label(label)
                        .properties(properties)
                        .sourceType(jobConfig.getString(Constant.SOURCE_TYPE))
                        .sourceField(jobConfig.getString(Constant.SOURCE_FIELD))
                        .sourceUniqueField(jobConfig.getString(Constant.SOURCE_UNIQUE_FIELD))
                        .targetType(jobConfig.getString(Constant.TARGET_TYPE))
                        .targetField(jobConfig.getString(Constant.TARGET_FIELD))
                        .targetUniqueField(jobConfig.getString(Constant.TARGET_UNIQUE_FIELD))
                        .direction(Neo4jDirectionEnum.valueOf(direction.toUpperCase()))
                        .writeMode(writeMode)
                        .build();
            }
            preStatements = jobConfig.getList(Constant.PRE_STATEMENTS, String.class);
            postStatements = jobConfig.getList(Constant.POST_STATEMENTS, String.class);
            batchSize = jobConfig.getInt(Constant.BATCH_SIZE);
            driver = GraphDatabase.driver(boltUrl, AuthTokens.basic(username, password));
            if (preStatements != null && !preStatements.isEmpty()) {
                LOG.info("Begin to execute preStatements:[{}].", preStatements);
                for (String statement : preStatements) {
                    driver.session().run(statement);
                }
                LOG.info("Execution of preStatements completed.");
            }

            LOG.info("Initialize neo4j write task success.");
        }

        @Override
        public void destroy() {
            if (driver != null) {
                if (postStatements != null && !postStatements.isEmpty()) {
                    LOG.info("Begin to execute postStatements:[{}].", preStatements);
                    for (String statement : preStatements) {
                        driver.session().run(statement);
                    }
                    LOG.info("Execution of postStatements completed.");
                }
                driver.close();
            }
        }

        @Override
        public void startWrite(RecordReceiver lineReceiver) {
            LOG.info("Begin to write.");
            Record record;
            List<String> commands = new ArrayList<>();
            Session session = driver.session();
            String delimiter = property.getType().getDelimiter();
            while ((record = lineReceiver.getFromReader()) != null) {
                commands.add(Neo4jWriterHelper.buildCommand(record, property));
                if (commands.size() >= batchSize) {
                    session.run(String.join(delimiter, commands));
                    commands.clear();
                }
            }
            if (!commands.isEmpty()) {
                session.run(String.join(delimiter, commands));
                commands.clear();
            }
            lineReceiver.shutdown();
            LOG.info("Write finished.");
        }
    }

}

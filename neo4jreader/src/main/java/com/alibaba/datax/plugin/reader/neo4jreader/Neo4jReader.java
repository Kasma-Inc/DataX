package com.alibaba.datax.plugin.reader.neo4jreader;

import com.alibaba.datax.common.element.Record;
import com.alibaba.datax.common.plugin.RecordSender;
import com.alibaba.datax.common.spi.Reader;
import com.alibaba.datax.common.util.Configuration;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Neo4jReader extends Reader {

    private static final Logger LOG = LoggerFactory.getLogger(Neo4jReader.class);

    public static class Job extends Reader.Job {

        private Configuration jobConfig = null;

        @Override
        public void init() {
            LOG.info("Begin to initialize neo4j job.");
            this.jobConfig = super.getPluginJobConf();
            String boltUrl = jobConfig.getString(Constant.BOLT_URL, "bolt://127.0.0.1:7687");
            String username = jobConfig.getString(Constant.USERNAME, "neo4j");
            String password = jobConfig.getString(Constant.PASSWORD, "neo4j123");
            jobConfig.set(Constant.BOLT_URL, boltUrl);
            jobConfig.set(Constant.USERNAME, username);
            jobConfig.set(Constant.PASSWORD, password);
            LOG.info("Initialize neo4j job success, neo4j config: {}", jobConfig.toJSON());
        }

        @Override
        public void destroy() {
        }

        @Override
        public List<Configuration> split(int adviceNumber) {
            LOG.info("Begin to split neo4j job.");
            // todo split job
            List<Configuration> list = new ArrayList<>();
            list.add(jobConfig);
            LOG.info("Split neo4j job finished, job size: {}", list.size());
            return Lists.newArrayList(jobConfig);
        }
    }

    public static class Task extends Reader.Task {

        private Driver driver = null;

        private String query = null;

        @Override
        public void init() {
            LOG.info("Begin to initialize neo4j read task.");
            Configuration jobConfig = super.getPluginJobConf();
            String boltUrl = jobConfig.getString(Constant.BOLT_URL);
            String username = jobConfig.getString(Constant.USERNAME);
            String password = jobConfig.getString(Constant.PASSWORD);
            driver = GraphDatabase.driver(boltUrl, AuthTokens.basic(username, password));
            query = jobConfig.getString(Constant.QUERY);
            LOG.info("Initialize neo4j read task success.");
        }

        @Override
        public void destroy() {
            if (driver != null) {
                driver.close();
            }
        }

        @Override
        public void startRead(RecordSender recordSender) {
            LOG.info("Begin to read.");
            Result result = driver.session().run(query);
            while (result.hasNext()) {
                Record record = Neo4jReaderHelper.buildRecord(recordSender.createRecord(),
                        result.next(), super.getTaskPluginCollector());
                recordSender.sendToWriter(record);
            }
            recordSender.flush();
            LOG.info("Read finished.");
        }
    }

}

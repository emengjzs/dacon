package emengjzs.dacon.dao;

import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Map.Entry;

@Service
public class JdbcTemplateFactory {

    private final Map<String, JdbcTemplate> cachedMap = Maps.newConcurrentMap();
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private Map<String, String> dbSettings;


    public JdbcTemplateFactory() {

    }

    @PostConstruct
    private void init() {
        if (MapUtils.isNotEmpty(dbSettings)) {
            logger.info("Register jdbcTemplate instances");
            for (Entry<String, String> set : dbSettings.entrySet()) {
                this.from(set.getValue()).alias(set.getKey()).create();
            }
        }
    }

    public JdbcTemplate get(String alias) {
        return cachedMap.get("0_" + alias);
    }

    /**
     * like root@127.0.0.1:8836
     *
     * @param url
     */
    public JdbcTemplateBuilder from(String url) {
        JdbcTemplateBuilder builder = new JdbcTemplateBuilder();
        String[] userAndUrl = url.split("@", 2);

        String hostAndPort;
        if (userAndUrl.length == 1) {
            hostAndPort = userAndUrl[0];
        } else {
            hostAndPort = userAndUrl[1];
            builder.username = userAndUrl[0];
        }
        builder.host = hostAndPort.split(":", 2)[0];
        builder.port = hostAndPort.split(":", 2)[1];
        builder.alias = hostAndPort;
        return builder;
    }

    public class JdbcTemplateBuilder {
        String host = "127.0.0.1";
        String port = "3306";
        String username = "work";
        String password = "123456";
        String db = "";
        String alias;
        DriverManagerDataSource simpleDs = new DriverManagerDataSource();

        private JdbcTemplateBuilder() {

        }

        public JdbcTemplateBuilder alias(String alias) {
            this.alias = alias;
            return this;
        }

        public JdbcTemplateBuilder pwd(String pwd) {
            this.password = pwd;
            return this;
        }

        public JdbcTemplateBuilder db(String db) {
            this.db = "/" + db;
            return this;
        }

        public JdbcTemplate create() {
            if (StringUtils.isEmpty(alias)) {
                alias = host + ":" + port;
            }
            logger.info("Register DB '{}' [{}@{}:{} ({})]", alias, username, host, port + db, password);
            simpleDs.setUrl("jdbc:mysql://" + host + ":" + port + db
                    + "?useUnicode=true&amp;characterEncoding=utf8&amp;zeroDateTimeBehavior=convertToNull");
            simpleDs.setUsername(username);
            simpleDs.setPassword(password);
            simpleDs.setDriverClassName("com.mysql.jdbc.Driver");
            JdbcTemplate jdbc = new JdbcTemplate(simpleDs, true);
            cachedMap.put("0_" + alias, jdbc);
            return jdbc;
        }

    }
}

package ru.extremism.extrmismserver.listener;

import com.impossibl.postgres.api.jdbc.PGNotificationListener;
import com.impossibl.postgres.jdbc.PGDataSource;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import com.impossibl.postgres.api.jdbc.PGConnection;

import java.sql.Connection;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import ru.extremism.extrmismserver.configurers.DatabaseProperty;
import ru.extremism.extrmismserver.configurers.TextsAnalysisDatabaseConfig;

import javax.sql.DataSource;
import javax.xml.crypto.dsig.keyinfo.PGPData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

@Component
public class TextAnalysisListener {
    @Value("${app.db.notification.table}")String tableName;
    @Value("${app.db.notification.channel}")String channelName;
    @Autowired
    private PGConnection connection;
    private final TableObserver tableObserver;
    private static final Logger log = LoggerFactory.getLogger(TextAnalysisListener.class);

    private DatabaseProperty databaseProperty;


    public TextAnalysisListener(TableObserver tableObserver,
                                @Qualifier(TextsAnalysisDatabaseConfig.DATABASE_PROPERTY)
                                DatabaseProperty databaseProperty) {
        this.tableObserver = tableObserver;
        this.databaseProperty = databaseProperty;
    }

    @PostConstruct
    public void initObserver() throws SQLException {
        SqlCodePart sqlCodePart = new SqlCodePart(channelName, tableName);
        connection.addNotificationListener(new PGNotificationListener() {
            public void notification(int processId, String channelName, String payload) {
                log.info("Received Notification: " + processId + ", " + channelName + ", " + payload);
                tableObserver.notifySubscribers(payload);
            }

            public void closed() {                // initiate reconnection & restart listening
            }
        });
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(sqlCodePart.getSqlNotifyFunctionCode());
        stmt.executeUpdate(sqlCodePart.getSqlCreateTriggerCode());
        stmt.executeUpdate("LISTEN " + channelName);
    }
}


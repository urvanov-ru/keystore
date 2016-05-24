package ru.urvanov.keystore.listener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.core.userdetails.UserDetails;

import ru.urvanov.keystore.domain.UserDetailsImpl;

public class LogoutListenerImpl implements
        ApplicationListener<SessionDestroyedEvent> {

    private static final Logger logger = LoggerFactory
            .getLogger(LogoutListenerImpl.class);

    private DataSource dataSource;

    @Override
    public void onApplicationEvent(SessionDestroyedEvent event) {
        List<SecurityContext> lstSecurityContext = event.getSecurityContexts();
        UserDetails ud;
        for (SecurityContext securityContext : lstSecurityContext) {
            ud = (UserDetails) securityContext.getAuthentication()
                    .getPrincipal();
            if (ud instanceof UserDetailsImpl) {
                UserDetailsImpl userDetailsImpl = (UserDetailsImpl) ud;
                if (userDetailsImpl.getUser() != null) {
                    logger.info("session_log record creation started.");
                    Long userId = userDetailsImpl.getUser().getId();
                    Date sessionBegin = userDetailsImpl.getAuthenticatedDate();
                    Date sessionEnd = new Date();
                    try (Connection connection = dataSource.getConnection()) {
                        String sql = "insert into session_log( "
                                + "user_id, session_begin, "
                                + "session_end) values(?, ?, ?);";
                        PreparedStatement statement = connection
                                .prepareStatement(sql);
                        statement.setLong(1, userId);
                        statement.setTimestamp(2, new java.sql.Timestamp(
                                sessionBegin.getTime()));
                        statement.setTimestamp(3, new java.sql.Timestamp(
                                sessionEnd.getTime()));
                        if (statement.executeUpdate() != 1) {
                            logger.error("Write session_log query changed 0 records.");
                        }
                        logger.info(
                                "session_log record created successfully. sessionBegin={}, sessionEnd={}, userId={}.",
                                sessionBegin, sessionEnd, userId);
                    } catch (SQLException sqlEx) {
                        logger.error("Failed to write session_log record.",
                                sqlEx);
                    }
                }
            }
        }
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

}

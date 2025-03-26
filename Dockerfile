FROM payara/server-full:6.2024.11-jdk21
COPY postgresql-42.7.5.jar /tmp/
RUN echo 'add-library /tmp/postgresql-42.7.5.jar' > $POSTBOOT_COMMANDS
COPY target/*.war $DEPLOY_DIR/FinancialSalmon-0.1-SNAPSHOT.war

RUN echo 'create-jdbc-connection-pool --datasourceclassname org.postgresql.ds.PGSimpleDataSource --driverclassname org.postgresql.Driver --restype javax.sql.DataSource --property user=postgres:password=postgres:URL="jdbc\\:postgresql\\://financialsalmon-db/FinancialSalmon" financialsalmonPool' >> $POSTBOOT_COMMANDS
RUN echo 'ping-connection-pool financialsalmonPool' >> $POSTBOOT_COMMANDS
RUN echo 'create-jdbc-resource --connectionpoolid financialsalmonPool jdbc/__financialsalmon' >> $POSTBOOT_COMMANDS
RUN echo 'create-auth-realm --classname=com.sun.enterprise.security.auth.realm.jdbc.JDBCRealm --property jaas-context=jdbcRealm:datasource-jndi=jdbc/__financialsalmon:user-table=users:user-name-column=username:password-column=password:group-table=user_role:group-table-user-name-column=user_id:group-name-column=role_id:digest-algorithm=SHA-256:encoding=Base64:charset=UTF-8 financialsalmon_realm' >> $POSTBOOT_COMMANDS

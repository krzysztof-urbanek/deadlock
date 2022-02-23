package db.transaction.deadlock.config

import org.flywaydb.core.Flyway
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct
import javax.sql.DataSource


@Configuration
class FlywayConfig(
    @Qualifier("mysqlDataSource") private val mysqlDataSource: DataSource,
    @Qualifier("postgresqlDataSource") private val postgresqlDataSource: DataSource,
    @Qualifier("mssqlDataSource") private val mssqlDataSource: DataSource,
) {

    @PostConstruct
    fun migrateFlyway() {
        Flyway.configure()
            .dataSource(mysqlDataSource)
            .locations("db/specific/mysql")
            .load()
            .migrate()

        Flyway.configure()
            .dataSource(postgresqlDataSource)
            .locations("db/specific/postgresql")
            .load()
            .migrate()

        Flyway.configure()
            .dataSource(mssqlDataSource)
            .locations("db/specific/mssql")
            .mixed(true)
            .load()
            .migrate()

    }
}
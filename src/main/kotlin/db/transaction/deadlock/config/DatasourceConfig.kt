package db.transaction.deadlock.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource


@Configuration
class DatasourceConfig {

    @Bean
    @ConfigurationProperties("mysql.datasource")
    fun mysqlDataSource(): DataSource =
        DataSourceBuilder.create().build()

    @Bean
    @ConfigurationProperties("postgresql.datasource")
    fun postgresqlDataSource(): DataSource =
        DataSourceBuilder.create().build()

    @Bean
    @ConfigurationProperties("mssql.datasource")
    fun mssqlDataSource(): DataSource =
        DataSourceBuilder.create().build()

}

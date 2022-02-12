package db.transaction.deadlock.config

import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy
import org.hibernate.dialect.Dialect
import org.hibernate.dialect.MySQL8Dialect
import org.hibernate.dialect.PostgreSQL10Dialect
import org.hibernate.dialect.SQLServerDialect
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import javax.sql.DataSource


@Configuration
class EntityManagerConfig {

    @Bean
    fun mysqlEntityManagerFactory(mysqlDataSource: DataSource) =
        entityManagerFactory(mysqlDataSource, MySQL8Dialect())

    @Bean
    fun postgresqlEntityManagerFactory(postgresqlDataSource: DataSource) =
        entityManagerFactory(postgresqlDataSource, PostgreSQL10Dialect())

    @Bean
    fun mssqlEntityManagerFactory(mssqlDataSource: DataSource) =
        entityManagerFactory(mssqlDataSource, SQLServerDialect())

    fun entityManagerFactory(dataSource: DataSource, dialect: Dialect): LocalContainerEntityManagerFactoryBean =
        LocalContainerEntityManagerFactoryBean().apply {
            setDataSource(dataSource)
            setPackagesToScan("db.transaction.deadlock.model")
            jpaVendorAdapter = HibernateJpaVendorAdapter()
            setJpaPropertyMap(
                mutableMapOf(
                    "hibernate.hbm2ddl.auto" to "none",
                    "hibernate.dialect" to dialect,
                    "hibernate.physical_naming_strategy" to CamelCaseToUnderscoresNamingStrategy::class.qualifiedName,
                    "hibernate.format_sql" to true,
                )
            )
        }

}

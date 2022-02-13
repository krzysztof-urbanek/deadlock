package db.transaction.deadlock.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.orm.jpa.JpaTransactionManager
import javax.persistence.EntityManagerFactory


/**
 * Jpa requires JpaTransactionManager implementation of PlatformTransactionManager,
 * but there are other implementations for different Spring Modules:
 * - DataSourceTransactionManager for JDBC
 * - HibernateTransactionManager for Hibernate
 * - JtaTransactionManager for Jta
 */
@Configuration
class JpaTransactionManagerConfig {

    @Bean
    fun mysqlJpaTransactionManager(
        @Qualifier("mysqlEntityManagerFactory") emf: EntityManagerFactory
    ) =
        JpaTransactionManager().apply {
            entityManagerFactory = emf
        }

    @Bean
    fun postgresqlJpaTransactionManager(
        @Qualifier("postgresqlEntityManagerFactory") emf: EntityManagerFactory
    ) =
        JpaTransactionManager().apply {
            entityManagerFactory = emf
        }

    @Bean
    fun mssqlJpaTransactionManager(
        @Qualifier("mssqlEntityManagerFactory") emf: EntityManagerFactory
    ) =
        JpaTransactionManager().apply {
            entityManagerFactory = emf
        }
}

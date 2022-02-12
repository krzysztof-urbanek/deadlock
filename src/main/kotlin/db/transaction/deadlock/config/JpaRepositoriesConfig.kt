package db.transaction.deadlock.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories


@EnableJpaRepositories(
    basePackages = ["db.transaction.deadlock.repository.mysql"],
    entityManagerFactoryRef = "mysqlEntityManagerFactory",
    transactionManagerRef = "mysqlJpaTransactionManager",
)
@Configuration
class MysqlJpaRepositoriesConfig


@EnableJpaRepositories(
    basePackages = ["db.transaction.deadlock.repository.postgresql"],
    entityManagerFactoryRef = "postgresqlEntityManagerFactory",
    transactionManagerRef = "postgresqlJpaTransactionManager",
)
@Configuration
class PostgresqlJpaRepositoriesConfig


@EnableJpaRepositories(
    basePackages = ["db.transaction.deadlock.repository.mssql"],
    entityManagerFactoryRef = "mssqlEntityManagerFactory",
    transactionManagerRef = "mssqlJpaTransactionManager",
)
@Configuration
class MssqlJpaRepositoriesConfig

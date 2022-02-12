package db.transaction.deadlock

import db.transaction.deadlock.model.TcpPort
import db.transaction.deadlock.repository.mssql.MssqlTcpPortJpaRepository
import db.transaction.deadlock.repository.mysql.MysqlTcpPortJpaRepository
import db.transaction.deadlock.repository.postgresql.PostgresqlTcpPortJpaRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Transactional


private const val BOOT_TOMCAT_PORT = 8080

@SpringBootTest
class DatabaseVendorsTest {

	@Autowired lateinit var mysqlTcpPortJpaRepository: MysqlTcpPortJpaRepository
	@Autowired lateinit var postgresqlTcpPortJpaRepository: PostgresqlTcpPortJpaRepository
	@Autowired lateinit var mssqlTcpPortJpaRepository: MssqlTcpPortJpaRepository

	@Test
	@Transactional("mysqlJpaTransactionManager")
	fun testMysql() {
		//validate that the correct database is bound
		assertThat(mysqlTcpPortJpaRepository.findByPortNumber(3306)?.description)
			.isEqualTo("mysql")

		//the actual test
		testJpaRepository(mysqlTcpPortJpaRepository)
	}

	@Test
	@Transactional("postgresqlJpaTransactionManager")
	fun testPostgresql() {
		//validate that the correct database is bound
		assertThat(postgresqlTcpPortJpaRepository.findByPortNumber(5432)?.description)
			.isEqualTo("postgresql")

		//the actual test
		testJpaRepository(postgresqlTcpPortJpaRepository)
	}

	@Test
	@Transactional("mssqlJpaTransactionManager")
	fun testMssql() {
		//validate that the correct database is bound
		assertThat(mssqlTcpPortJpaRepository.findByPortNumber(1433)?.description)
			.isEqualTo("mssql")

		//the actual test
		testJpaRepository(mssqlTcpPortJpaRepository)
	}

	fun testJpaRepository(tcpPortJpaRepository: JpaRepository<TcpPort, Long>) {
		//given
		val tcpPort = TcpPort(
			portNumber = BOOT_TOMCAT_PORT,
			description = "Default Spring Boot Tomcat Port.",
		)

		//when
		tcpPortJpaRepository.saveAndFlush(tcpPort)

		//then
		assertThat((tcpPortJpaRepository.findAll().any { it.portNumber == BOOT_TOMCAT_PORT })).isTrue
	}

}

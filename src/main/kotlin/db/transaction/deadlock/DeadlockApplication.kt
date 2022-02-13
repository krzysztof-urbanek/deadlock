package db.transaction.deadlock

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(
	exclude = [
		FlywayAutoConfiguration::class,
		DataSourceAutoConfiguration::class,
	]
)
class DeadlockApplication

fun main(args: Array<String>) {
	runApplication<DeadlockApplication>(*args)
}

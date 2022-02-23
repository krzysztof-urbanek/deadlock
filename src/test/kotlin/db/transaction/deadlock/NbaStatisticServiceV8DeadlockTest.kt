package db.transaction.deadlock

import db.transaction.deadlock.dbspecific.mssql.v8.MssqlNbaPlayerJpaRepositoryV8
import db.transaction.deadlock.dbspecific.mssql.v8.MssqlNbaPlayersStatisticsServiceV8
import db.transaction.deadlock.dbspecific.mysql.v8.MysqlNbaPlayerJpaRepositoryV8
import db.transaction.deadlock.dbspecific.mysql.v8.MysqlNbaPlayersStatisticsServiceV8
import db.transaction.deadlock.dbspecific.postgresql.v8.PostgresqlNbaPlayerJpaRepositoryV8
import db.transaction.deadlock.dbspecific.postgresql.v8.PostgresqlNbaPlayersStatisticsServiceV8
import db.transaction.deadlock.model.NbaPlayer
import db.transaction.deadlock.service.NbaPlayersStatisticsServiceV8
import mu.KotlinLogging
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate
import java.util.concurrent.Executors
import java.util.concurrent.Executors.callable
import java.util.concurrent.TimeUnit


@SpringBootTest
class NbaStatisticsServiceV8DeadlockTest {

    private val log = KotlinLogging.logger {}

    @Autowired lateinit var mysqlNbaPlayersStatisticsService: MysqlNbaPlayersStatisticsServiceV8
    @Autowired lateinit var mysqlNbaPlayerJpaRepository: MysqlNbaPlayerJpaRepositoryV8

    @Autowired lateinit var postgresqlNbaPlayersStatisticsService: PostgresqlNbaPlayersStatisticsServiceV8
    @Autowired lateinit var postgresqlNbaPlayerJpaRepository: PostgresqlNbaPlayerJpaRepositoryV8

    @Autowired lateinit var mssqlNbaPlayersStatisticsService: MssqlNbaPlayersStatisticsServiceV8
    @Autowired lateinit var mssqlNbaPlayerJpaRepository: MssqlNbaPlayerJpaRepositoryV8

    @AfterEach
    fun cleanUpNbaPlayers() {
        log.info("=====\nRemoving all Nba Players from all the databases")
        mysqlNbaPlayerJpaRepository.deleteAllInBatch()
        postgresqlNbaPlayerJpaRepository.deleteAllInBatch()
        mssqlNbaPlayerJpaRepository.deleteAllInBatch()
    }

    @Test
    fun mysqlConcurrencyTest() {
        concurrencyTest(mysqlNbaPlayerJpaRepository, mysqlNbaPlayersStatisticsService)
    }

    @Test
    fun postgresqlConcurrencyTest() {
        concurrencyTest(postgresqlNbaPlayerJpaRepository, postgresqlNbaPlayersStatisticsService)
    }

    @Test
    fun mssqlConcurrencyTest() {
        concurrencyTest(mssqlNbaPlayerJpaRepository, mssqlNbaPlayersStatisticsService)
    }

    fun concurrencyTest(
        nbaPlayerJpaRepository: JpaRepository<NbaPlayer, Long>,
        nbaPlayersStatisticsService: NbaPlayersStatisticsServiceV8,
    ) {
        log.info("Populating all the databases with NBA players")
        nbaPlayerJpaRepository.saveAllAndFlush(listOf(
            NbaPlayer(name = "Stephen Curry", birthdate = LocalDate.of(1988,3,1)),
            NbaPlayer(name = "LeBron James", birthdate = LocalDate.of(1984,12,30)),
            NbaPlayer(name = "Kevin Durant", birthdate = LocalDate.of(1988,9,29)),
            NbaPlayer(name = "Chris Paul", birthdate = LocalDate.of(1985,5,6)),
        ))
        log.info("Finished populating all the databases with NBA players\n=====")
        log.info("Testing...")

        val executorService = Executors.newFixedThreadPool(2)

        try {
            listOf(
                executorService.submit(callable {
                    nbaPlayersStatisticsService.publishOldestPlayers(3)
                }),
                executorService.submit(callable {
                    nbaPlayersStatisticsService.publishYoungestPlayers(3)
                })
            ).forEach {
                it.get()
            }
        } finally {
            executorService.shutdown()
            executorService.awaitTermination(10, TimeUnit.SECONDS)
        }

        log.info("=====\nListing all Nba players from the database")
        val allNbaPlayers = nbaPlayerJpaRepository.findAll()
        allNbaPlayers.forEach { log.info("# $it") }

        Assertions.assertThat(allNbaPlayers.sumOf { it.mentions }).isEqualTo(6)
    }
}
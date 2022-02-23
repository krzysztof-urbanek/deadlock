package db.transaction.deadlock

import db.transaction.deadlock.dbspecific.mssql.v0.MssqlNbaPlayerJpaRepositoryV0
import db.transaction.deadlock.dbspecific.mssql.v0.MssqlNbaPlayersStatisticsServiceV0
import db.transaction.deadlock.dbspecific.mysql.v0.MysqlNbaPlayerJpaRepositoryV0
import db.transaction.deadlock.dbspecific.mysql.v0.MysqlNbaPlayersStatisticsServiceV0
import db.transaction.deadlock.dbspecific.postgresql.v0.PostgresqlNbaPlayerJpaRepositoryV0
import db.transaction.deadlock.dbspecific.postgresql.v0.PostgresqlNbaPlayersStatisticsServiceV0
import db.transaction.deadlock.model.NbaPlayer
import db.transaction.deadlock.service.NbaPlayersStatisticsServiceV0
import mu.KotlinLogging.logger
import org.assertj.core.api.Assertions.assertThat
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
class NbaStatisticsServiceV0DeadlockTest {

    private val log = logger {}

    @Autowired lateinit var mysqlNbaPlayersStatisticsService: MysqlNbaPlayersStatisticsServiceV0
    @Autowired lateinit var mysqlNbaPlayerJpaRepository: MysqlNbaPlayerJpaRepositoryV0

    @Autowired lateinit var postgresqlNbaPlayersStatisticsService: PostgresqlNbaPlayersStatisticsServiceV0
    @Autowired lateinit var postgresqlNbaPlayerJpaRepository: PostgresqlNbaPlayerJpaRepositoryV0

    @Autowired lateinit var mssqlNbaPlayersStatisticsService: MssqlNbaPlayersStatisticsServiceV0
    @Autowired lateinit var mssqlNbaPlayerJpaRepository: MssqlNbaPlayerJpaRepositoryV0

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
        nbaPlayersStatisticsService: NbaPlayersStatisticsServiceV0,
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
                    nbaPlayersStatisticsService.publishYoungestPlayer()
                }),
                executorService.submit(callable {
                    nbaPlayersStatisticsService.publishYoungestPlayer()
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

        assertThat(allNbaPlayers.sumOf { it.mentions }).isEqualTo(2)
    }
}
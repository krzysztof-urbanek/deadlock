package db.transaction.deadlock

import db.transaction.deadlock.dbspecific.mssql.v2.MssqlNbaPlayerJpaRepositoryV2
import db.transaction.deadlock.dbspecific.mssql.v2.MssqlNbaPlayersStatisticsServiceV2
import db.transaction.deadlock.dbspecific.mysql.v2.MysqlNbaPlayerJpaRepositoryV2
import db.transaction.deadlock.dbspecific.mysql.v2.MysqlNbaPlayersStatisticsServiceV2
import db.transaction.deadlock.dbspecific.postgresql.v2.PostgresqlNbaPlayerJpaRepositoryV2
import db.transaction.deadlock.dbspecific.postgresql.v2.PostgresqlNbaPlayersStatisticsServiceV2
import db.transaction.deadlock.model.NbaPlayer
import db.transaction.deadlock.service.NbaPlayersStatisticsServiceV2
import mu.KotlinLogging.logger
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
class NbaStatisticsServiceV2DeadlockTest {

    private val log = logger {}

    @Autowired lateinit var mysqlNbaPlayersStatisticsService: MysqlNbaPlayersStatisticsServiceV2
    @Autowired lateinit var mysqlNbaPlayerJpaRepository: MysqlNbaPlayerJpaRepositoryV2

    @Autowired lateinit var postgresqlNbaPlayersStatisticsService: PostgresqlNbaPlayersStatisticsServiceV2
    @Autowired lateinit var postgresqlNbaPlayerJpaRepository: PostgresqlNbaPlayerJpaRepositoryV2

    @Autowired lateinit var mssqlNbaPlayersStatisticsService: MssqlNbaPlayersStatisticsServiceV2
    @Autowired lateinit var mssqlNbaPlayerJpaRepository: MssqlNbaPlayerJpaRepositoryV2

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
        nbaPlayersStatisticsService: NbaPlayersStatisticsServiceV2,
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

        Assertions.assertThat(allNbaPlayers.sumOf { it.mentions }).isEqualTo(2)
    }
}
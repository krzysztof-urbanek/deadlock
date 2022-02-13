package db.transaction.deadlock

import db.transaction.deadlock.dbspecific.mssql.v5.MssqlNbaPlayerJpaRepositoryV5
import db.transaction.deadlock.dbspecific.mssql.v5.MssqlNbaPlayersStatisticsServiceV5
import db.transaction.deadlock.dbspecific.mysql.v5.MysqlNbaPlayerJpaRepositoryV5
import db.transaction.deadlock.dbspecific.mysql.v5.MysqlNbaPlayersStatisticsServiceV5
import db.transaction.deadlock.dbspecific.postgresql.v5.PostgresqlNbaPlayerJpaRepositoryV5
import db.transaction.deadlock.dbspecific.postgresql.v5.PostgresqlNbaPlayersStatisticsServiceV5
import db.transaction.deadlock.model.NbaPlayer
import db.transaction.deadlock.service.NbaPlayersStatisticsServiceV4
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
class NbaStatisticsServiceV5DeadlockTest {

    private val log = KotlinLogging.logger {}

    @Autowired lateinit var mysqlNbaPlayersStatisticsService: MysqlNbaPlayersStatisticsServiceV5
    @Autowired lateinit var mysqlNbaPlayerJpaRepositoryV1: MysqlNbaPlayerJpaRepositoryV5

    @Autowired lateinit var postgresqlNbaPlayersStatisticsService: PostgresqlNbaPlayersStatisticsServiceV5
    @Autowired lateinit var postgresqlNbaPlayerJpaRepositoryV1: PostgresqlNbaPlayerJpaRepositoryV5

    @Autowired lateinit var mssqlNbaPlayersStatisticsService: MssqlNbaPlayersStatisticsServiceV5
    @Autowired lateinit var mssqlNbaPlayerJpaRepository: MssqlNbaPlayerJpaRepositoryV5

    @AfterEach
    fun cleanUpNbaPlayers() {
        log.info("Removing all Nba Players from all the databases")
        mysqlNbaPlayerJpaRepositoryV1.deleteAllInBatch()
        postgresqlNbaPlayerJpaRepositoryV1.deleteAllInBatch()
        mssqlNbaPlayerJpaRepository.deleteAllInBatch()
    }

    @Test
    fun mysqlConcurrencyTest() {
        concurrencyTest(mysqlNbaPlayerJpaRepositoryV1, mysqlNbaPlayersStatisticsService)
    }

    @Test
    fun postgresqlConcurrencyTest() {
        concurrencyTest(postgresqlNbaPlayerJpaRepositoryV1, postgresqlNbaPlayersStatisticsService)
    }

    @Test
    fun mssqlConcurrencyTest() {
        concurrencyTest(mssqlNbaPlayerJpaRepository, mssqlNbaPlayersStatisticsService)
    }

    fun concurrencyTest(
        nbaPlayerJpaRepository: JpaRepository<NbaPlayer, Long>,
        nbaPlayersStatisticsServiceV4: NbaPlayersStatisticsServiceV4,
    ) {
        log.info("Populating all the databases with NBA players")
        nbaPlayerJpaRepository.saveAllAndFlush(listOf(
            NbaPlayer(name = "Stephen Curry", birthdate = LocalDate.of(1988,3,1)),
            NbaPlayer(name = "LeBron James", birthdate = LocalDate.of(1984,12,30)),
            NbaPlayer(name = "Kevin Durant", birthdate = LocalDate.of(1988,9,29)),
            NbaPlayer(name = "Chris Paul", birthdate = LocalDate.of(1985,5,6)),
        ))
        log.info("Finished populating all the databases with NBA players")

        val executorService = Executors.newFixedThreadPool(2)

        try {
            listOf(
                executorService.submit(callable {
                    nbaPlayersStatisticsServiceV4.publishOldestPlayers(3)
                }),
                executorService.submit(callable {
                    nbaPlayersStatisticsServiceV4.publishYoungestPlayers(3)
                })
            ).forEach {
                it.get()
            }
        } finally {
            executorService.shutdown()
            executorService.awaitTermination(10, TimeUnit.SECONDS)
        }

        log.info("Listing all Nba players from the database")
        val allNbaPlayers = nbaPlayerJpaRepository.findAll()
        allNbaPlayers.forEach { log.info("# $it") }

        Assertions.assertThat(allNbaPlayers.sumOf { it.mentions }).isEqualTo(6)
    }
}
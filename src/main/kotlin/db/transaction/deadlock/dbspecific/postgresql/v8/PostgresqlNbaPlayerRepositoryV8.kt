package db.transaction.deadlock.dbspecific.postgresql.v8

import db.transaction.deadlock.model.NbaPlayer
import mu.KotlinLogging.logger
import org.springframework.stereotype.Repository
import java.lang.Thread.sleep


@Repository
class PostgresqlNbaPlayerRepositoryV8(
    private val postgresqlNbaPlayerJpaRepository: PostgresqlNbaPlayerJpaRepositoryV8,
) {
    private val log = logger {}

    fun findYoungestPlayers(number: Int) = postgresqlNbaPlayerJpaRepository
        .findOrdinalIdByOrderByBirthdateDesc(number)
        .sortedBy { it }
        .map {
            //To increase the likelihood of potential deadlock we add a delay in between row selection
            sleep(500)
            postgresqlNbaPlayerJpaRepository.findByOrdinalId(it)
        }

    fun findOldestPlayers(number: Int) = postgresqlNbaPlayerJpaRepository
        .findOrdinalIdByOrderByBirthdateAsc(number)
        .sortedBy { it }
        .map {
            //To increase the likelihood of potential deadlock we add a delay in between row selection
            sleep(500)
            postgresqlNbaPlayerJpaRepository.findByOrdinalId(it)
        }

    fun saveAll(nbaPlayers: Iterable<NbaPlayer>) {
        //To increase the likelihood of a potential deadlock we add a delay and flush in between the updates.
        //Doing this should not cause deadlocks if the solution is sound.
        nbaPlayers.forEach {
            sleep(500)
            log.info("Thread id: ${Thread.currentThread().id}, player name: ${it.name}")
            postgresqlNbaPlayerJpaRepository.saveAndFlush(it)
        }
    }
}

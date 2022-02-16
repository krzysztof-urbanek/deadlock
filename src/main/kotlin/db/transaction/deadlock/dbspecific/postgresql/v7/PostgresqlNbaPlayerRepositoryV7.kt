package db.transaction.deadlock.dbspecific.postgresql.v7

import db.transaction.deadlock.model.NbaPlayer
import mu.KotlinLogging.logger
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository
import java.lang.Thread.sleep


@Repository
class PostgresqlNbaPlayerRepositoryV7(
    private val postgresqlNbaPlayerJpaRepository: PostgresqlNbaPlayerJpaRepositoryV7,
) {
    private val log = logger {}

    fun findYoungestPlayers(number: Int) = postgresqlNbaPlayerJpaRepository
        .findByOrderByBirthdateDesc(PageRequest.of(0, number))
        .sortedBy { it.ordinalId }
        .map {
            //To increase the likelihood of potential deadlock we add a delay in between row selection
            sleep(500)
            postgresqlNbaPlayerJpaRepository.findByOrdinalId(it.ordinalId!!)
        }

    fun findOldestPlayers(number: Int) = postgresqlNbaPlayerJpaRepository
        .findByOrderByBirthdateAsc(PageRequest.of(0, number))
        .sortedBy { it.ordinalId }
        .map {
            //To increase the likelihood of potential deadlock we add a delay in between row selection
            sleep(500)
            postgresqlNbaPlayerJpaRepository.findByOrdinalId(it.ordinalId!!)
        }

    fun saveAll(nbaPlayers: Iterable<NbaPlayer>) {
        postgresqlNbaPlayerJpaRepository.saveAllAndFlush(nbaPlayers)
    }
}
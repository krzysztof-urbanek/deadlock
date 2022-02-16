package db.transaction.deadlock.dbspecific.postgresql.v6

import db.transaction.deadlock.model.NbaPlayer
import mu.KotlinLogging.logger
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository
import java.lang.Thread.sleep


@Repository
class PostgresqlNbaPlayerRepositoryV6(
    private val postgresqlNbaPlayerJpaRepository: PostgresqlNbaPlayerJpaRepositoryV6,
) {
    private val log = logger {}

    fun findYoungestPlayers(number: Int): List<NbaPlayer> {
        postgresqlNbaPlayerJpaRepository.forceIndexes()

        return postgresqlNbaPlayerJpaRepository.findByOrderByBirthdateDescNative(number)
    }

    fun findOldestPlayers(number: Int): List<NbaPlayer> {
        postgresqlNbaPlayerJpaRepository.forceIndexes()

        return postgresqlNbaPlayerJpaRepository.findByOrderByBirthdateAscNative(number)
    }

    fun saveAll(nbaPlayers: Iterable<NbaPlayer>) {
        postgresqlNbaPlayerJpaRepository.saveAllAndFlush(nbaPlayers)
    }
}
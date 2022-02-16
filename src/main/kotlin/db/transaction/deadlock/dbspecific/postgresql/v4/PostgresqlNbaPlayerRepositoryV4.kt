package db.transaction.deadlock.dbspecific.postgresql.v4

import db.transaction.deadlock.model.NbaPlayer
import mu.KotlinLogging.logger
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.lang.Thread.sleep


@Repository
class PostgresqlNbaPlayerRepositoryV4(
    private val postgresqlNbaPlayerJpaRepository: PostgresqlNbaPlayerJpaRepositoryV4,
) {
    private val log = logger {}

    fun findYoungestPlayers(number: Int) = postgresqlNbaPlayerJpaRepository
        .findByOrderByBirthdateDesc(PageRequest.of(0, number))

    fun findOldestPlayers(number: Int) = postgresqlNbaPlayerJpaRepository
        .findByOrderByBirthdateAsc(PageRequest.of(0, number))

    fun saveAll(nbaPlayers: Iterable<NbaPlayer>) {
        postgresqlNbaPlayerJpaRepository.saveAllAndFlush(nbaPlayers)
    }
}
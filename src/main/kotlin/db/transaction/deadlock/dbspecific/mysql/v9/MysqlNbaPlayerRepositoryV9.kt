package db.transaction.deadlock.dbspecific.mysql.v9

import db.transaction.deadlock.model.NbaPlayer
import mu.KotlinLogging.logger
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository
import java.lang.Thread.sleep


@Repository
class MysqlNbaPlayerRepositoryV9(
    private val mysqlNbaPlayerJpaRepository: MysqlNbaPlayerJpaRepositoryV9,
) {
    private val log = logger {}

    fun findYoungestPlayers(number: Int) = mysqlNbaPlayerJpaRepository
        .findByOrderByBirthdateDesc(PageRequest.of(0, number))

    fun findOldestPlayers(number: Int) = mysqlNbaPlayerJpaRepository
        .findByOrderByBirthdateAsc(PageRequest.of(0, number))

    fun saveAll(nbaPlayers: Iterable<NbaPlayer>) {
        mysqlNbaPlayerJpaRepository.saveAllAndFlush(nbaPlayers)
    }
}
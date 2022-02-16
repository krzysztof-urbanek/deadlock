package db.transaction.deadlock.dbspecific.mysql.v6

import db.transaction.deadlock.model.NbaPlayer
import mu.KotlinLogging.logger
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository
import java.lang.Thread.sleep


@Repository
class MysqlNbaPlayerRepositoryV6(
    private val mysqlNbaPlayerJpaRepository: MysqlNbaPlayerJpaRepositoryV6,
) {
    private val log = logger {}

    fun findYoungestPlayers(number: Int) = mysqlNbaPlayerJpaRepository
        .findByOrderByBirthdateDescNative(number)

    fun findOldestPlayers(number: Int) = mysqlNbaPlayerJpaRepository
        .findByOrderByBirthdateAscNative(number)

    fun saveAll(nbaPlayers: Iterable<NbaPlayer>) {
        mysqlNbaPlayerJpaRepository.saveAllAndFlush(nbaPlayers)
    }
}
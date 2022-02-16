package db.transaction.deadlock.dbspecific.mssql.v6

import db.transaction.deadlock.model.NbaPlayer
import mu.KotlinLogging.logger
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository
import java.lang.Thread.sleep


@Repository
class MssqlNbaPlayerRepositoryV6(
    private val mssqlNbaPlayerJpaRepository: MssqlNbaPlayerJpaRepositoryV6,
) {
    private val log = logger {}

    fun findYoungestPlayers(number: Int) = mssqlNbaPlayerJpaRepository
        .findByOrderByBirthdateDescNative(number)

    fun findOldestPlayers(number: Int) = mssqlNbaPlayerJpaRepository
        .findByOrderByBirthdateAscNative(number)

    fun saveAll(nbaPlayers: Iterable<NbaPlayer>) {
        mssqlNbaPlayerJpaRepository.saveAllAndFlush(nbaPlayers)
    }
}
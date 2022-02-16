package db.transaction.deadlock.dbspecific.mssql.v5

import db.transaction.deadlock.model.NbaPlayer
import mu.KotlinLogging.logger
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.lang.Thread.sleep


@Repository
class MssqlNbaPlayerRepositoryV5(
    private val mssqlNbaPlayerJpaRepository: MssqlNbaPlayerJpaRepositoryV5,
) {
    private val log = logger {}

    fun findYoungestPlayers(number: Int) = mssqlNbaPlayerJpaRepository
        .findByOrderByBirthdateDesc(PageRequest.of(0, number))

    fun findOldestPlayers(number: Int) = mssqlNbaPlayerJpaRepository
        .findByOrderByBirthdateAsc(PageRequest.of(0, number))

    fun saveAll(nbaPlayers: Iterable<NbaPlayer>) {
        mssqlNbaPlayerJpaRepository.saveAllAndFlush(nbaPlayers)
    }
}
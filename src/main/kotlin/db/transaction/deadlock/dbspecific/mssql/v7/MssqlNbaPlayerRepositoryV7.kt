package db.transaction.deadlock.dbspecific.mssql.v7

import db.transaction.deadlock.model.NbaPlayer
import mu.KotlinLogging.logger
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository
import java.lang.Thread.sleep


@Repository
class MssqlNbaPlayerRepositoryV7(
    private val mssqlNbaPlayerJpaRepository: MssqlNbaPlayerJpaRepositoryV7,
) {
    private val log = logger {}

    fun findYoungestPlayers(number: Int) = mssqlNbaPlayerJpaRepository
        .findByOrderByBirthdateDesc(PageRequest.of(0, number))
        .sortedBy { it.ordinalId }
        .map {
            //To increase the likelihood of potential deadlock we add a delay in between row selection
            sleep(500)
            mssqlNbaPlayerJpaRepository.findByOrdinalId(it.ordinalId!!)
        }

    fun findOldestPlayers(number: Int) = mssqlNbaPlayerJpaRepository
        .findByOrderByBirthdateAsc(PageRequest.of(0, number))
        .sortedBy { it.ordinalId }
        .map {
            //To increase the likelihood of potential deadlock we add a delay in between row selection
            sleep(500)
            mssqlNbaPlayerJpaRepository.findByOrdinalId(it.ordinalId!!)
        }

    fun saveAll(nbaPlayers: Iterable<NbaPlayer>) {
        mssqlNbaPlayerJpaRepository.saveAllAndFlush(nbaPlayers)
    }
}

package db.transaction.deadlock.dbspecific.mssql.v5b

import db.transaction.deadlock.model.NbaPlayer
import mu.KotlinLogging.logger
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository
import java.lang.Thread.sleep


@Repository
class MssqlNbaPlayerRepositoryV5B(
    private val mssqlNbaPlayerJpaRepository: MssqlNbaPlayerJpaRepositoryV5B,
) {
    private val log = logger {}

    fun findYoungestPlayers(number: Int) = mssqlNbaPlayerJpaRepository
        .findByOrderByBirthdateDesc(PageRequest.of(0, number))
        .sortedBy { it.ordinalId }
        .map {
            //To increase the likelihood of potential deadlock we add a delay in between row selection
            sleep(1000)
            mssqlNbaPlayerJpaRepository.findByOrdinalId(it.ordinalId!!)
        }

    fun findOldestPlayers(number: Int) = mssqlNbaPlayerJpaRepository
        .findByOrderByBirthdateAsc(PageRequest.of(0, number))
        .sortedBy { it.ordinalId }
        .map {
            //To increase the likelihood of potential deadlock we add a delay in between row selection
            sleep(1000)
            mssqlNbaPlayerJpaRepository.findByOrdinalId(it.ordinalId!!)
        }

    fun saveAll(nbaPlayers: Iterable<NbaPlayer>) {
        //To increase the likelihood of potential deadlock we add delay and flush in between updates.
        //Doing this should not cause deadlocks if the solution is sound.
        nbaPlayers.forEach {
            sleep( 1000)
            log.info("Thread id: ${Thread.currentThread().id}, player name: ${it.name}")
            mssqlNbaPlayerJpaRepository.saveAndFlush(it)
        }
    }
}
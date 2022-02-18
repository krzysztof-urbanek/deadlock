package db.transaction.deadlock.dbspecific.mysql.v7

import db.transaction.deadlock.model.NbaPlayer
import mu.KotlinLogging.logger
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository
import java.lang.Thread.sleep


@Repository
class MysqlNbaPlayerRepositoryV7(
    private val mysqlNbaPlayerJpaRepository: MysqlNbaPlayerJpaRepositoryV7,
) {
    private val log = logger {}

    fun findYoungestPlayers(number: Int) = mysqlNbaPlayerJpaRepository
        .findByOrderByBirthdateDesc(PageRequest.of(0, number))
        .sortedBy { it.ordinalId }
        .map {
            //To increase the likelihood of potential deadlock we add a delay in between row selection
            sleep(500)
            mysqlNbaPlayerJpaRepository.findByOrdinalId(it.ordinalId!!)
        }

    fun findOldestPlayers(number: Int) = mysqlNbaPlayerJpaRepository
        .findByOrderByBirthdateAsc(PageRequest.of(0, number))
        .sortedBy { it.ordinalId }
        .map {
            //To increase the likelihood of potential deadlock we add a delay in between row selection
            sleep(500)
            mysqlNbaPlayerJpaRepository.findByOrdinalId(it.ordinalId!!)
        }

    fun saveAll(nbaPlayers: Iterable<NbaPlayer>) {
        //To increase the likelihood of a potential deadlock we add a delay and flush in between the updates.
        //Doing this should not cause deadlocks if the solution is sound.
        nbaPlayers.forEach {
            sleep(500)
            log.info("Thread id: ${Thread.currentThread().id}, player name: ${it.name}")
            mysqlNbaPlayerJpaRepository.saveAndFlush(it)
        }
    }
}
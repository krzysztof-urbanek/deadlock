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
        .findOrdinalIdByOrderByBirthdateDesc(number)
        .sortedBy { it }
        .map {
            //To increase the likelihood of potential deadlock we add a delay
            sleep(500)
            mysqlNbaPlayerJpaRepository.findByOrdinalId(it)
        }

    fun findOldestPlayers(number: Int) = mysqlNbaPlayerJpaRepository
        .findOrdinalIdByOrderByBirthdateAsc(number)
        .sortedBy { it }
        .map {
            //To increase the likelihood of potential deadlock we add a delay
            sleep(500)
            mysqlNbaPlayerJpaRepository.findByOrdinalId(it)
        }

    fun saveAll(nbaPlayers: Iterable<NbaPlayer>) {
        //To increase the likelihood of potential deadlock we add delay and flush in between updates.
        //Doing this should not cause deadlocks if the solution is sound.
        nbaPlayers.forEach {
            sleep(500)
            log.info("Thread id: ${Thread.currentThread().id}, player name: ${it.name}")
            mysqlNbaPlayerJpaRepository.saveAndFlush(it)
        }
    }
}
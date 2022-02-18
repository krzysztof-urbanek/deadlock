package db.transaction.deadlock.dbspecific.mysql.v4

import db.transaction.deadlock.model.NbaPlayer
import mu.KotlinLogging.logger
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.lang.Thread.sleep


@Repository
class MysqlNbaPlayerRepositoryV4(
    private val mysqlNbaPlayerJpaRepository: MysqlNbaPlayerJpaRepositoryV4,
) {
    private val log = logger {}

    fun findYoungestPlayers(number: Int) = mysqlNbaPlayerJpaRepository.
        findByOrderByBirthdateDesc(PageRequest.of(0, number))

    fun findOldestPlayers(number: Int) = mysqlNbaPlayerJpaRepository.
        findByOrderByBirthdateAsc(PageRequest.of(0, number))

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
package db.transaction.deadlock.dbspecific.mssql.v4

import db.transaction.deadlock.model.NbaPlayer
import mu.KotlinLogging.logger
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository
import java.lang.Thread.sleep


@Repository
class MssqlNbaPlayerRepositoryV4(
    private val mssqlNbaPlayerJpaRepository: MssqlNbaPlayerJpaRepositoryV4,
) {
    private val log = logger {}

    fun findYoungestPlayers(number: Int) = mssqlNbaPlayerJpaRepository
        .findByOrderByBirthdateDesc(PageRequest.of(0, number))

    fun findOldestPlayers(number: Int) = mssqlNbaPlayerJpaRepository
        .findByOrderByBirthdateAsc(PageRequest.of(0, number))

    fun saveAll(nbaPlayers: Iterable<NbaPlayer>) {
        //To increase the likelihood of a potential deadlock we add a delay and flush in between the updates.
        //Doing this should not cause deadlocks if the solution is sound.
        nbaPlayers.forEach {
            sleep(500)
            log.info("Thread id: ${Thread.currentThread().id}, player name: ${it.name}")
            mssqlNbaPlayerJpaRepository.saveAndFlush(it)
        }
    }
}
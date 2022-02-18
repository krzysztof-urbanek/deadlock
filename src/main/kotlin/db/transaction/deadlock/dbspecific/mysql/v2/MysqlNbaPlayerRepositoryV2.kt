package db.transaction.deadlock.dbspecific.mysql.v2

import db.transaction.deadlock.model.NbaPlayer
import mu.KotlinLogging.logger
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository
import java.lang.Thread.sleep


@Repository
class MysqlNbaPlayerRepositoryV2(
    private val mysqlNbaPlayerJpaRepository: MysqlNbaPlayerJpaRepositoryV2,
) {
    private val log = logger {}

    fun findYoungestPlayer() = mysqlNbaPlayerJpaRepository
        .findByOrderByBirthdateDesc(PageRequest.of(0, 1))
        .firstOrNull()

    fun save(nbaPlayer: NbaPlayer) {
        //To increase the likelihood of a potential deadlock we add a delay before the update.
        //Doing this should not cause deadlocks if the solution is sound.
        sleep(500)
        log.info("Thread id: ${Thread.currentThread().id}, player name: ${nbaPlayer.name}")
        mysqlNbaPlayerJpaRepository.saveAndFlush(nbaPlayer)
    }
}
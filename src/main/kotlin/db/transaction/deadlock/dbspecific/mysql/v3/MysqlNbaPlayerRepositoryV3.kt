package db.transaction.deadlock.dbspecific.mysql.v3

import db.transaction.deadlock.model.NbaPlayer
import mu.KotlinLogging.logger
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository
import java.lang.Thread.sleep


@Repository
class MysqlNbaPlayerRepositoryV3(
    private val mysqlNbaPlayerJpaRepository: MysqlNbaPlayerJpaRepositoryV3,
) {
    private val log = logger {}

    fun findYoungestPlayer() =
        mysqlNbaPlayerJpaRepository.findByOrderByBirthdateDesc(PageRequest.of(0, 1)).firstOrNull()

    fun save(nbaPlayer: NbaPlayer) {
        //To increase the likelihood of potential deadlock we add delay and flush before the update.
        //Doing this should not cause deadlocks if the solution is sound.
        sleep( 1000)
        log.info("Thread id: ${Thread.currentThread().id}, player name: ${nbaPlayer.name}")
        mysqlNbaPlayerJpaRepository.saveAndFlush(nbaPlayer)
    }
}
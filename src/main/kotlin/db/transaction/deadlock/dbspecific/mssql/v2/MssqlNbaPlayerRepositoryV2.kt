package db.transaction.deadlock.dbspecific.mssql.v2

import db.transaction.deadlock.model.NbaPlayer
import mu.KotlinLogging.logger
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository
import java.lang.Thread.sleep


@Repository
class MssqlNbaPlayerRepositoryV2(
    private val mssqlNbaPlayerJpaRepository: MssqlNbaPlayerJpaRepositoryV2,
) {
    private val log = logger {}

    fun findYoungestPlayer() = mssqlNbaPlayerJpaRepository
        .findByOrderByBirthdateDesc(PageRequest.of(0, 1))
        .firstOrNull()

    fun save(nbaPlayer: NbaPlayer) {
        log.info("Thread id: ${Thread.currentThread().id}, player name: ${nbaPlayer.name}")
        mssqlNbaPlayerJpaRepository.saveAndFlush(nbaPlayer)
    }
}
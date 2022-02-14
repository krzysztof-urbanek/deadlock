package db.transaction.deadlock.dbspecific.mssql.v3

import db.transaction.deadlock.service.NbaPlayersStatisticsServiceV3
import db.transaction.deadlock.service.NbaPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class MssqlNbaPlayersStatisticsServiceV3(
    private val mssqlNbaPlayerRepository: MssqlNbaPlayerRepositoryV3,
    private val nbaPublisher: NbaPublisher,
): NbaPlayersStatisticsServiceV3 {

    @Transactional("mssqlJpaTransactionManager")
    override fun publishYoungestPlayer() {
        val youngest = mssqlNbaPlayerRepository.findYoungestPlayer() ?: return

        nbaPublisher.publish(youngest)
        youngest.mentions++

        mssqlNbaPlayerRepository.save(youngest)
    }
}

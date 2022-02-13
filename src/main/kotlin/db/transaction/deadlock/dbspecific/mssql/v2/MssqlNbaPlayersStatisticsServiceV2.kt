package db.transaction.deadlock.dbspecific.mssql.v2

import db.transaction.deadlock.service.NbaPlayersStatisticsServiceV1
import db.transaction.deadlock.service.NbaPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class MssqlNbaPlayersStatisticsServiceV2(
    private val mssqlNbaPlayerRepository: MssqlNbaPlayerRepositoryV2,
    private val nbaPublisher: NbaPublisher,
): NbaPlayersStatisticsServiceV1 {

    @Transactional("mssqlJpaTransactionManager")
    override fun publishYoungestPlayer() {
        val youngest = mssqlNbaPlayerRepository.findYoungestPlayer() ?: return

        nbaPublisher.publish(youngest)
        youngest.mentions++

        mssqlNbaPlayerRepository.save(youngest)
    }
}

package db.transaction.deadlock.dbspecific.mssql.v0

import db.transaction.deadlock.service.NbaPlayersStatisticsServiceV0
import db.transaction.deadlock.service.NbaPublisher
import org.springframework.stereotype.Service


@Service
class MssqlNbaPlayersStatisticsServiceV0(
    private val mssqlNbaPlayerRepository: MssqlNbaPlayerRepositoryV0,
    private val nbaPublisher: NbaPublisher,
): NbaPlayersStatisticsServiceV0 {

    override fun publishYoungestPlayer() {
        val youngest = mssqlNbaPlayerRepository.findYoungestPlayer() ?: return

        nbaPublisher.publish(youngest)
        youngest.mentions++

        mssqlNbaPlayerRepository.save(youngest)
    }
}

package db.transaction.deadlock.dbspecific.mssql.v5

import db.transaction.deadlock.service.NbaPlayersStatisticsServiceV4
import db.transaction.deadlock.service.NbaPublisher
import org.springframework.stereotype.Service


@Service
class MssqlNbaPlayersStatisticsServiceV5(
    private val mssqlNbaPlayerRepository: MssqlNbaPlayerRepositoryV5,
    private val nbaPublisher: NbaPublisher,
): NbaPlayersStatisticsServiceV4 {

    override fun publishYoungestPlayers(number: Int) {
        val youngest = mssqlNbaPlayerRepository.findYoungestPlayers(number)

        youngest.onEach {
            nbaPublisher.publish(it)
            it.mentions++
        }

        mssqlNbaPlayerRepository.saveAll(youngest)
    }

    override fun publishOldestPlayers(number: Int) {
        val oldest = mssqlNbaPlayerRepository.findOldestPlayers(number)

        oldest.onEach {
            nbaPublisher.publish(it)
            it.mentions++
        }

        mssqlNbaPlayerRepository.saveAll(oldest)
    }
}

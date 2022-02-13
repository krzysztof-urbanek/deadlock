package db.transaction.deadlock.dbspecific.mssql.v6

import db.transaction.deadlock.dbspecific.mssql.v5.MssqlNbaPlayerRepositoryV5
import db.transaction.deadlock.service.NbaPlayersStatisticsServiceV4
import db.transaction.deadlock.service.NbaPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class MssqlNbaPlayersStatisticsServiceV6(
    private val mssqlNbaPlayerRepository: MssqlNbaPlayerRepositoryV5,
    private val nbaPublisher: NbaPublisher,
): NbaPlayersStatisticsServiceV4 {

    @Transactional("mssqlJpaTransactionManager")
    override fun publishYoungestPlayers(number: Int) {
        val youngest = mssqlNbaPlayerRepository.findYoungestPlayers(number)

        youngest.onEach {
            nbaPublisher.publish(it)
            it.mentions++
        }

        mssqlNbaPlayerRepository.saveAll(youngest)
    }

    @Transactional("mssqlJpaTransactionManager")
    override fun publishOldestPlayers(number: Int) {
        val oldest = mssqlNbaPlayerRepository.findOldestPlayers(number)

        oldest.onEach {
            nbaPublisher.publish(it)
            it.mentions++
        }

        mssqlNbaPlayerRepository.saveAll(oldest)
    }
}

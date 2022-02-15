package db.transaction.deadlock.dbspecific.mssql.v9

import db.transaction.deadlock.service.NbaPlayersStatisticsServiceV9
import db.transaction.deadlock.service.NbaPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional


@Service
class MssqlNbaPlayersStatisticsServiceV9(
    private val mssqlNbaPlayerRepository: MssqlNbaPlayerRepositoryV9,
    private val nbaPublisher: NbaPublisher,
): NbaPlayersStatisticsServiceV9 {

    @Transactional("mssqlJpaTransactionManager", isolation = Isolation.SERIALIZABLE)
    override fun publishYoungestPlayers(number: Int) {
        val youngest = mssqlNbaPlayerRepository.findYoungestPlayers(number)

        youngest.onEach {
            nbaPublisher.publish(it)
            it.mentions++
        }

        mssqlNbaPlayerRepository.saveAll(youngest)
    }

    @Transactional("mssqlJpaTransactionManager", isolation = Isolation.SERIALIZABLE)
    override fun publishOldestPlayers(number: Int) {
        val oldest = mssqlNbaPlayerRepository.findOldestPlayers(number)

        oldest.onEach {
            nbaPublisher.publish(it)
            it.mentions++
        }

        mssqlNbaPlayerRepository.saveAll(oldest)
    }
}

package db.transaction.deadlock.service


interface NbaPlayersStatisticsServiceV6 {

    fun publishYoungestPlayers(number: Int)

    fun publishOldestPlayers(number: Int)
}
package db.transaction.deadlock.service


interface NbaPlayersStatisticsServiceV4 {

    fun publishYoungestPlayers(number: Int)

    fun publishOldestPlayers(number: Int)
}
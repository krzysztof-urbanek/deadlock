package db.transaction.deadlock.service


interface NbaPlayersStatisticsServiceV9 {

    fun publishYoungestPlayers(number: Int)

    fun publishOldestPlayers(number: Int)
}
private lateinit var graph: Array<IntArray>
private lateinit var tablesCapacity: List<Int>

fun main() {
    val (n, m, k) = readLine()!!.split(' ').map { it.toInt() }
    tablesCapacity = readLine()!!.split(' ').map { it.toInt() }.sortedDescending()
    if (tablesCapacity.size != k) {
        throw Exception("Number of k_i should be equal to k")
    }

    graph = Array(n) { IntArray(n) { 1 } }

    for (i in 1..m) {
        println("Seating for day $i")
        nextDay()
    }
}

private val subgraphResolver: MaxScorelSubgraphResolver = BronKerboschMaxScoreSubgraphImpl()

private fun nextDay() {
    val curGraph = graph.map { it.copyOf() }.toTypedArray()
    val remainingPlayers = graph.indices.toMutableList()
    tablesCapacity.forEach { capacity ->
        val biggestSubgraph = subgraphResolver.resolve(capacity, curGraph, remainingPlayers.toList())
        println(biggestSubgraph.toString())
        remainingPlayers.removeAll(biggestSubgraph)
        // remove vertexes entirely from current day graph
        biggestSubgraph.forEach {
            for (j in curGraph.indices) {
                curGraph[it][j] = 0
                curGraph[j][it] = 0
            }
        }
        // remove vertex pairs from common graph
        biggestSubgraph.forEach { j ->
            biggestSubgraph.forEach { k ->
                graph[j][k] = 0
            }
        }
    }
}


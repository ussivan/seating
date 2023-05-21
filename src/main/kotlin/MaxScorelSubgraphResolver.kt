interface MaxScorelSubgraphResolver {
    fun resolve(minSize: Int, graph: Array<IntArray>, remainingPlayers: List<Int>): List<Int>
}
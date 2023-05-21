class BronKerboschMaxScoreSubgraphImpl : MaxScorelSubgraphResolver {

    private lateinit var subgraphs: MutableList<Set<Int>>
    private lateinit var graph: Array<IntArray>

    override fun resolve(minSize: Int, graph: Array<IntArray>, remainingPlayers: List<Int>): List<Int> {
        subgraphs = mutableListOf()
        this.graph = graph
        val maxFullSubgraph = maxFullSubgraph()
        if (maxFullSubgraph.size == 1) {
            return remainingPlayers.subList(0, minSize)
        }
        if (maxFullSubgraph.size >= minSize) {
            return maxFullSubgraph.subList(0, minSize)
        }
        val remainingAfterFullSubgraph = remainingPlayers.filter { !maxFullSubgraph.contains(it) }
        return completeSubgraph(minSize - maxFullSubgraph.size, maxFullSubgraph, remainingAfterFullSubgraph)
    }

    private fun completeSubgraph(minSize: Int, maxFullSubgraph: List<Int>, remainingPlayers: List<Int>): List<Int> {
        val generator = CombinationsGenerator(minSize, remainingPlayers)
        var combination = generator.next()?.toMutableList()
        var maxScore = -1
        var bestCombination = combination
        while (combination != null) {
            var score = 0
            combination.addAll(maxFullSubgraph)
            combination.forEach { i ->
                combination!!.forEach { j ->
                    score += graph[i][j]
                }
            }
            if (score > maxScore) {
                bestCombination = combination
                maxScore = score
            }
            combination = generator.next()?.toMutableList()
        }
        return bestCombination!!.toList()
    }


    private fun maxFullSubgraph(): List<Int> {
        findFullSubgraphs(mutableListOf(), graph.indices.toMutableList(), mutableListOf())
        subgraphs.sortBy { -it.size }
        return subgraphs[0].toList()
    }

    private fun findFullSubgraphs(currentSubgraph: MutableList<Int>, members: MutableList<Int>, foundSubgraphs: MutableList<Int>) {
        foundSubgraphs.forEach { found ->
            var edges = 0
            members.forEach { member ->
                if (graph[found][member] == 1) {
                    edges++
                }
            }
            if (edges == members.size) {
                return
            }
        }
        members.toList().forEach { member ->
            val potentialMembers = mutableListOf<Int>()
            val newAlreadyFound = mutableListOf<Int>()
            currentSubgraph.add(member)
            members.remove(member)
            members.forEach { potentialMember ->
                if (graph[member][potentialMember] == 1) {
                    potentialMembers.add(potentialMember)
                }
            }
            foundSubgraphs.forEach { newFound ->
                if (graph[member][newFound] == 1) {
                    newAlreadyFound.add(newFound)
                }
            }
            if (potentialMembers.isEmpty() && newAlreadyFound.isEmpty()) {
                subgraphs.add(currentSubgraph.toSet())
            } else {
                findFullSubgraphs(currentSubgraph, potentialMembers, newAlreadyFound)
            }
            foundSubgraphs.add(member)
            currentSubgraph.remove(member)
        }
    }

}
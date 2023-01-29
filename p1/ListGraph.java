import java.util.*;

public class ListGraph implements Graph {
    private HashMap<String, LinkedList<String>> nodes = new HashMap<>();

    public boolean addNode(String n) { // okay

        if (hasNode(n)) {
            return false;
        } else { // already in the graph
            nodes.put(n, new LinkedList<>());
            return true;
        }
    }

    public boolean addEdge(String n1, String n2) {
	    if (!hasNode(n1) || !hasNode(n2)) 
            throw new NoSuchElementException();
        if (hasEdge(n1, n2)) { // edge already in graph
            return false;
        } else { // new edge
            nodes.get(n1).add(n2);
            return true;
        }
    }

    public boolean hasNode(String n) { // okay
	    return nodes.containsKey(n);
    }

    public boolean hasEdge(String n1, String n2) {
	    LinkedList<String> values = nodes.get(n1);
        if (values == null) // if n1 is not a node in graph
            return false;
        return values.contains(n2);
    }

    public boolean removeNode(String n) {
	    if(!hasNode(n)) { // node not previously in graph
            return false;
        } 
        for (String succNode: succ(n)) { // remove outgoing edges
            removeEdge(n, succNode);
        }
        for (String predNode: pred(n)){ // remove incoming edges
            removeEdge(predNode, n);
        }
        nodes.remove(n); // remove the node itself
        return true;
    }

    public boolean removeEdge(String n1, String n2) {
        if (!hasNode(n1) || !hasNode(n2)) // node is not in graph
            throw new NoSuchElementException();
        if (!hasEdge(n1, n2)) { // edge not in graph
            return false;
        } else {
            nodes.get(n1).remove(n2);
            return true;
        }
    }

    public List<String> nodes() {
	     LinkedList<String> res = new LinkedList<>(nodes.keySet());
         return res;
    }

    public List<String> succ(String n) {

        if ( !hasNode(n) ) {
            throw new NoSuchElementException();
        }

	    List<String> res = new LinkedList<>();
        for (String node: nodes.get(n)) {
                res.add(node);
        }
        return res;
    }

    public List<String> pred(String n) {
        if ( !hasNode(n) )
            throw new NoSuchElementException();
    
        List<String> res = new LinkedList<>();
        for (String node: nodes.keySet()) {
        if (hasEdge(node, n))
            res.add(node);
    }
    return res;
    }

    public Graph union(Graph g) {
	    Graph res = new ListGraph();

        // put the first graph nodes and edges into result
        for (String node: nodes() ) {
            res.addNode(node);
        }
        for (String node: nodes() ) {
            for (String succNode: succ(node)) {
                res.addEdge(node, succNode);
            }
        }

        // put the second graph nodes and edges into the result
        for(String node: g.nodes()){
            if (!hasNode(node)) {
                res.addNode(node);
            }
        }
        for(String node: g.nodes()){
            for (String succNode: g.succ(node)) {
                if (!hasEdge(node, succNode))
                    res.addEdge(node, succNode);
            }
        }

        return res;
    }

    public Graph subGraph(Set<String> nodes) {
	    Graph res = new ListGraph();

        for ( String n: nodes ) {
            if ( hasNode(n) ) {
                res.addNode(n);
            }
        }

        for ( String node: res.nodes() ) {
            for( String succNode: succ(node) ) {
                if (res.hasNode(succNode))
                    res.addEdge(node, succNode);
            }
        }

        return res;
    }

    public boolean connected(String n1, String n2) {
        if (!hasNode(n1) || !hasNode(n2)) // node is not in graph
            throw new NoSuchElementException();
        if(n1.equals(n2))
            return true;

        LinkedList<String> deque = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        deque.add(n1);
        while(deque.size() > 0) {
            String node = deque.pop();
            for (String succNode: succ(node)) {
                if (succNode.equals(n2)) 
                    return true;
                if (!visited.contains(succNode)) {
                    deque.add(succNode);
                }
            }
            visited.add(node);
        }
        return false;
    }
}

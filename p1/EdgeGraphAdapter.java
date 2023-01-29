import java.util.*;

public class EdgeGraphAdapter implements EdgeGraph {

    private Graph g;

    public EdgeGraphAdapter(Graph g) { this.g = g; }

    public boolean addEdge(Edge e) {
      if ( hasEdge(e) ) {
        return false;
      }
      String src = e.getSrc();
      String dst = e.getDst();
      if ( !hasNode(src) )
        g.addNode(src);
      if ( !hasNode(dst) )
        g.addNode(dst);
      g.addEdge(src, dst);
      return true;
    }

    public boolean hasNode(String n) {
      List<Edge> edges = edges();
      for (Edge edge: edges) {
        if (edge.getSrc() == n || edge.getDst() == n) {
          return true;
        }
      }
      return false;
    }

    public boolean hasEdge(Edge e) {
      List<Edge> edges = edges();
      for (Edge edge: edges) {
        if (edge.getSrc().equals(e.getSrc()) && edge.getDst().equals(e.getDst()) ) {
          return true;
        }
      }
      return false;
    }

    public boolean removeEdge(Edge e) {
      if ( !hasEdge(e) ) {
        return false;
      }
      String src = e.getSrc();
      String dst = e.getDst();
      
      g.removeEdge(src, dst);
      if(g.succ(src).size() == 0 && g.pred(src).size() == 0)
        g.removeNode(src);
      if(g.pred(dst).size() == 0 && g.succ(dst).size() == 0)
        g.removeNode(dst);
      return true;
    }

    public List<Edge> outEdges(String n) {
      List<Edge> res = new LinkedList<>();
      List<String> succNodes = g.succ(n);
      for (String succNode: succNodes) {
        res.add(new Edge(n, succNode));
      }
      return res;
    }

    public List<Edge> inEdges(String n) {
      List<Edge> res = new LinkedList<>();
      List<String> predNodes = g.pred(n);
      for (String predNode: predNodes) {
        res.add(new Edge(predNode, n));
      }
      return res;
    }

    public List<Edge> edges() {
      List<Edge> res = new LinkedList<>();
      List<String> nodes = g.nodes();
      for (String node: nodes) {
        List<String> succNodes = g.succ(node);
        for (String succNode: succNodes) {
          res.add(new Edge(node, succNode));
        }
      }
      return res;
    }

    public EdgeGraph union(EdgeGraph g) {
      Graph lg = new ListGraph();
      EdgeGraph res = new EdgeGraphAdapter(lg);

      for (Edge e: edges()) {
        res.addEdge(e);
      }
      for (Edge e: g.edges()) {
        if (!hasEdge(e)) {
          res.addEdge(e);
        }
      }
      return res;
    }

    public boolean hasPath(List<Edge> e) {
	    if (e.isEmpty()) {
        return true;
      }

      String predEnd = e.get(0).getSrc();
      for (Edge edge: e) {
        if (!hasEdge(edge) || !edge.getSrc().equals(predEnd) ) {
          return false;
        }
        predEnd = edge.getDst();
      }
      return true;
    }

}

 /**
  * @File Main.java
  * @Author: Pengcheng Xu (pengcheng.xu@tufts.edu)
  * This is the implementation for Project 1 of CS121 Software Engineering
  * The project description is in the accompanying PDF file 
  */
 
 import java.util.*;

public class Main {

    // Run "java -ea Main" to run with assertions enabled (If you run
    // with assertions disabled, the default, then assert statements
    // will not execute!)
    
	/* addNode() and hasNode() */
    public static void testHasNodeAddNode() {
	Graph g = new ListGraph();
	assert !g.hasNode("a");
	assert g.addNode("a");
	assert g.hasNode("a");

	System.out.println("testHasNodeAddNode succeed");
    }

	/* addEdge(), hasEdge() */
    public static void testHasEdgeAddEdge() {
		Graph g = new ListGraph();
		assert !g.hasEdge("a", "b");

		boolean thrown = false;
		try {
			g.addEdge("a", "b");
		} catch(NoSuchElementException e) {
			thrown = true;
		}
		assert thrown;

		g.addNode("a");
		thrown = false;
		try {
			g.addEdge("a", "b");
		} catch(NoSuchElementException e) {
			thrown = true;
		}
		assert thrown;

		g.addNode("b");

		assert g.addEdge("a", "b");
		assert g.hasEdge("a", "b");
		System.out.println("testHasEdgeAddEdge succeed");

    }

	/* addEdge and hasEdge */
	public static void test3() {
		Graph g = new ListGraph();
		assert g.addNode("a");
		assert g.addNode("b");
		assert g.addEdge("a", "b");
		assert g.hasEdge("a", "b");
		assert !g.hasEdge("b", "a");
		System.out.println("test3 succeed");
	}

	public static void testRemoveNode() {
		Graph g = new ListGraph();
		assert !g.removeNode("a");
		assert g.addNode("a");
		assert g.addNode("b");
		assert g.addNode("c");

		assert g.addEdge("a", "b");
		assert g.addEdge("c", "a");
		
		assert g.removeNode("a");
		assert !g.hasEdge("a", "b");
		assert !g.hasEdge("c", "a");

		System.out.println("testRemoveNode succeed");
	}

	public static void testRemoveEdge() {
		Graph g = new ListGraph();
		assert g.addNode("a");

		boolean thrown = false;
		try {
			g.removeEdge("a", "b");
		} catch (NoSuchElementException e) {
			thrown = true;
		}
		assert thrown;

		g.addNode("b");
		assert !g.removeEdge("a", "b");
		assert g.addEdge("a", "b");
		assert g.removeEdge("a", "b");

		System.out.println("testRemoveEdge succeed");
	}

	public static void testNodes() {
		Graph g = new ListGraph();
		assert g.nodes().isEmpty();
		assert g.addNode("a");
		assert g.addNode("b");
		assert (g.nodes().size() == 2);
		assert g.nodes().contains("a");
		assert g.nodes().contains("b");

		System.out.println("testNodes succeed");
	}

	public static void testSucc() {
		Graph g = new ListGraph();

		boolean thrown = false;
		try {
			g.succ("a");
		} catch (NoSuchElementException e) {
			thrown = true;
		}
		assert thrown;

		assert g.addNode("a");
		assert g.addNode("b");
		assert g.addNode("c");
		assert g.addEdge("a", "b");
		assert g.addEdge("c", "a");
		assert (g.succ("a").size() == 1);
		assert g.succ("a").contains("b");

		System.out.println("testSucc succeed");
	}

	public static void testPred() {
		Graph g = new ListGraph();

		boolean thrown = false;
		try {
			g.succ("a");
		} catch (NoSuchElementException e) {
			thrown = true;
		}
		assert thrown;

		assert g.addNode("a");
		assert g.addNode("b");
		assert g.addNode("c");
		assert g.addNode("d");

		assert g.addEdge("a", "b");
		assert g.addEdge("c", "a");
		assert g.addEdge("d", "a");

		assert (g.pred("a").size() == 2);
		assert g.pred("a").contains("c");
		assert g.pred("a").contains("d");

		System.out.println("testPred succeed");
	}

	public static void testUnion() {
		Graph g1 = new ListGraph();
		assert g1.addNode("a");
		assert g1.addNode("b");
		assert g1.addNode("c");
	

		assert g1.addEdge("a", "b");
		assert g1.addEdge("b", "c");
		assert g1.addEdge("c", "a");

		Graph g2 = new ListGraph();
		assert g2.addNode("c");
		assert g2.addNode("d");
		assert g2.addNode("e");
	

		assert g2.addEdge("c", "d");
		assert g2.addEdge("d", "e");
		assert g2.addEdge("e", "c");

		Graph g3 = g1.union(g2);
		assert g3.nodes().size() == 5;
		assert g3.hasEdge("a", "b");
		assert g3.hasEdge("b", "c");
		assert g3.hasEdge("c", "a");
		assert g3.hasEdge("c", "d");
		assert g3.hasEdge("d", "e");
		assert g3.hasEdge("e", "c");
	
		System.out.println("testUnion succeed");
	}

	public static void testSubGraph() {
		Graph g = new ListGraph();
		assert g.addNode("a");
		assert g.addNode("b");
		assert g.addNode("c");
		assert g.addNode("d");

		assert g.addEdge("a", "b");
		assert g.addEdge("a", "c");
		assert g.addEdge("b", "d");
		assert g.addEdge("c", "d");

		LinkedList<String> setInitial = new LinkedList<>();
		setInitial.add("a");
		setInitial.add("b");


		Set<String> s = new HashSet<>(setInitial);
		Graph sg = g.subGraph(s);

		assert sg.nodes().size() == 2;
		assert sg.hasNode("a");
		assert sg.hasNode("b");
		assert sg.hasEdge("a", "b");
		assert !sg.hasEdge("a", "c");
		assert !sg.hasEdge("c", "d");
		assert !sg.hasEdge("b", "b");
	
		System.out.println("testSubGraph succeed");
	}

	public static void testConnected() {
		Graph g = new ListGraph();
		boolean thrown = false;
		try {
			g.connected("a", "b");
		} catch (NoSuchElementException e) {
			thrown = true;
		}
		assert thrown;

		assert g.addNode("a");
		assert g.addNode("b");
		assert g.addNode("c");
		assert g.addEdge("a", "b");
		assert g.addEdge("b", "c");

		assert g.connected("a", "a");
		assert g.connected("a", "c");
		assert !g.connected("c", "a");

		assert g.addEdge("c", "a");
		assert g.connected("b", "a");

		System.out.println("testConnected succeed");
	}	

	public static void testEdgeGraphAddEdgeHasNodeHasEdge() {
		Graph g = new ListGraph();
		EdgeGraph eg = new EdgeGraphAdapter(g);
		assert !eg.hasNode("a");
		assert !eg.hasNode("b");
		Edge e = new Edge("a", "b");
		assert eg.addEdge(e);
		assert eg.hasEdge(e);
		assert eg.hasNode("a");
		assert eg.hasNode("b");
		
		System.out.println("testEdgeGraphAddEdgeHasNodeHasEdge succeed");
	}	

	public static void testEdgeGraphOutEdges() {
		Graph g = new ListGraph();
		EdgeGraph eg = new EdgeGraphAdapter(g);
		Edge e1 = new Edge("a", "b");
		Edge e2 = new Edge("b", "c");
		assert eg.addEdge(e1);
		assert eg.addEdge(e2);
		assert eg.outEdges("a").size() == 1;
		assert eg.hasEdge(e1);
		
		System.out.println("testEdgeGraphOutEdges succeed");
	}	

	public static void testEdgeGraphInEdges() {
		Graph g = new ListGraph();
		EdgeGraph eg = new EdgeGraphAdapter(g);
		Edge e1 = new Edge("a", "b");
		Edge e2 = new Edge("b", "c");
		assert eg.addEdge(e1);
		assert eg.addEdge(e2);
		assert eg.inEdges("b").size() == 1;
		assert eg.hasEdge(e1);
		assert eg.inEdges("a").size() == 0;
		
		System.out.println("testEdgeGraphInEdges succeed");
	}	

	public static void testEdgeGraphEdges() {
		Graph g = new ListGraph();
		EdgeGraph eg = new EdgeGraphAdapter(g);
		Edge e1 = new Edge("a", "b");
		Edge e2 = new Edge("b", "c");
		assert eg.addEdge(e1);
		assert eg.addEdge(e2);
		assert eg.edges().size() == 2; 
		assert eg.hasEdge(e1);
		assert eg.hasEdge(e2);
		
		System.out.println("testEdgeGraphEdges succeed");
	}	

	public static void testEdgeGraphRemoveEdge() {
		Graph g = new ListGraph();
		EdgeGraph eg = new EdgeGraphAdapter(g);
		Edge e1 = new Edge("a", "b");
		Edge e2 = new Edge("b", "c");
		assert eg.addEdge(e1);
		assert eg.addEdge(e2);
		assert eg.edges().size() == 2; 

		Edge e3 = new Edge("a", "e");
		assert !eg.removeEdge(e3);

		assert eg.removeEdge(e1);
		assert eg.edges().size() == 1;
		assert !eg.hasEdge(e1);
		assert eg.hasEdge(e2);

		System.out.println("testEdgeGraphRemoveEdge succeed");
	}	
	public static void testEdgeGraphUnion() {
		Graph g1 = new ListGraph();
		EdgeGraph eg1 = new EdgeGraphAdapter(g1);
		Edge e1 = new Edge("a", "b");
		Edge e2 = new Edge("b", "c");
		Edge e3 = new Edge("c", "a");
		assert eg1.addEdge(e1);
		assert eg1.addEdge(e2);
		assert eg1.addEdge(e3);
		assert eg1.edges().size() == 3; 

		Edge e4 = new Edge("a", "b");
		Edge e5 = new Edge("b", "d");

		Graph g2 = new ListGraph();
		EdgeGraph eg2 = new EdgeGraphAdapter(g2);
		assert eg2.addEdge(e4);
		assert eg2.addEdge(e5);
	
		assert eg2.edges().size() == 2;
	
		EdgeGraph eg3 = eg1.union(eg2);
		assert eg3.edges().size() == 4;
		assert eg3.hasEdge(new Edge("a", "b"));
		assert eg3.hasEdge(new Edge("b", "c"));
		assert eg3.hasEdge(new Edge("c", "a"));
		assert eg3.hasEdge(new Edge("b", "d"));

		System.out.println("testEdgeGraphUnion succeed");
	}	

	public static void testEdgeGraphHasPath() {
		Graph g1 = new ListGraph();
		EdgeGraph eg1 = new EdgeGraphAdapter(g1);
		List<Edge> emptyList = new LinkedList<>();
		assert eg1.hasPath(emptyList);

		Edge e1 = new Edge("a", "b");
		Edge e2 = new Edge("b", "c");
		Edge e3 = new Edge("c", "d");

		assert eg1.addEdge(e1);
		assert eg1.addEdge(e2);
		assert eg1.addEdge(e3);

		List <Edge> l1 = new LinkedList<>();
		l1.add(e1);
		l1.add(e3);
		assert !eg1.hasPath(l1);
	
		List <Edge> l2 = new LinkedList<>();
		l2.add(e1);
		l2.add(e2);
		l2.add(e3);
		assert eg1.hasPath(l2);
		
		System.out.println("testEdgeGraphHasPath succeed");
	}		

	// peer's tests
	public static void testPeerRemoveNode(){
		Graph g = new ListGraph();
	
		/*Test addNode and hasNode methods*/
		assert !g.hasNode("a");
		assert g.addNode("a");
		assert g.addNode("b");
		assert g.hasNode("a");
	
	
		/*Test addEdge and hadEdge methods*/
		assert !g.hasEdge("a", "b");
		assert g.addEdge("a", "b");
		assert g.hasEdge("a", "b");
	
		/*Test removeEdge and removeNode methods*/
		assert g.removeNode("a");
		assert g.removeEdge("a","b");
		assert !g.hasNode("a");
		assert !g.hasEdge("a","b");
	
		/*Test connected method*/
		assert g.addNode("c");
		assert !g.connected("b","c");
		assert g.addEdge("b","c");
		assert g.connected("b","c");
		System.out.println("testPeerRemoveNode succeed");

	}

	public static void testPeerCombined(){
		// this test cover all the methods of Graph
		
		Graph graph = new ListGraph();
		
		// test add function
		assert graph.addNode("a");
		assert graph.addNode("b");
		assert graph.addNode("c");
		assert graph.addNode("d");

		assert graph.addEdge("a", "b");
		assert graph.addEdge("b", "c");
		assert graph.addEdge("c", "d");
		assert graph.addEdge("d", "b");
		
		// test valid cases about add
		assert graph.hasNode("a");
		assert graph.hasNode("b");
		assert graph.hasNode("c");
		assert graph.hasNode("d");
		
		assert graph.hasEdge("a", "b");
		assert graph.hasEdge("b", "c");
		assert graph.hasEdge("c", "d");
		assert graph.hasEdge("d", "b");
		
		List<String> nodeList = graph.nodes();
		assert nodeList.contains("a");
		assert nodeList.contains("b");
		assert nodeList.contains("c");
		assert nodeList.contains("d");
		
		// test invalid case about add
		assert !graph.hasNode("e");
		assert !graph.hasEdge("a", "d");
		
		// test succ & pred & connected
		assert graph.hasNode("b");
		nodeList = graph.succ("b");
		assert !nodeList.contains("a");
		assert nodeList.contains("c");
		
		nodeList = graph.pred("b");
		assert nodeList.contains("a");
		assert nodeList.contains("d");
		assert !nodeList.contains("c");
		
		assert graph.connected("a", "d");
		graph.addNode("z");
		assert !graph.connected("a", "z");
		graph.removeNode("z");
		
		// test remove function (valid & invalid)
		assert graph.removeEdge("c", "d");
		assert !graph.hasEdge("c", "d");
		
		assert graph.removeNode("b");
		assert !graph.hasNode("b");
		assert !graph.hasEdge("b", "c");
		assert !graph.hasEdge("b", "d");
		assert !graph.hasEdge("a", "b");
		
		// test unionGraph & subGraph
		Graph graph2 = new ListGraph();
		assert graph2.addNode("e");
		assert graph2.addNode("f");
		assert graph2.addEdge("e", "f");
		Graph unionGraph = graph.union(graph2);
		assert unionGraph.hasNode("a");
		assert unionGraph.hasNode("c");
		assert unionGraph.hasNode("d");
		assert unionGraph.hasNode("e");
		assert unionGraph.hasNode("f");
		assert unionGraph.hasEdge("e", "f");
		
		Set<String> nodes = new HashSet<String>();
		nodes.add("a");
		nodes.add("c");
		graph.addEdge("a", "c");
		graph.addEdge("c", "d");
		Graph subGraph = graph.subGraph(nodes);
		
		assert subGraph.hasNode("a");
		assert subGraph.hasEdge("a", "c");
		assert !subGraph.hasNode("d");

		System.out.println("testPeerCombined succeed");

	}

	public static void testListGraph() {

		System.out.println("Test ListGraph:");
	   
		Graph g = new ListGraph();
	   
		assert g.addNode("a");
	   
		assert g.addNode("b");
	   
		assert g.addNode("c");
	   
		assert g.addNode("d");
	   
		assert !g.addNode("a");
	   
		System.out.println("addNode pass");
	   
		assert g.hasNode("a");
	   
		assert !g.hasNode("f");
	   
		System.out.println("hasNode pass");
	   
		assert g.addEdge("a", "b");
	   
		assert g.addEdge("b", "c");
	   
		assert g.addEdge("c", "d");
	   
		assert g.addEdge("d", "a");
	   
		assert !g.addEdge("d", "a");
	   
		System.out.println("addEdge pass");
	   
		assert g.hasEdge("a", "b");
	   
		assert !g.hasEdge("a", "f");
	   
		System.out.println("hasEdge pass");
	   
		assert g.removeNode("a");
	   
		assert !g.hasNode("a");
	   
		assert !g.hasEdge("a","b");
	   
		assert !g.hasEdge("d","a");
	   
		assert !g.removeNode("a");
	   
		System.out.println("removeNode pass");
	   
		assert g.removeEdge("b","c");
	   
		assert !g.hasEdge("b","c");
	   
		assert !g.removeEdge("b","c");
	   
		System.out.println("removeEdge pass");
	   
		g.addNode("a");
	   
		g.addEdge("a", "b");
	   
		g.addEdge("b", "c");
	   
		g.addEdge("d", "a");
	   
		assert g.nodes().size()==4;
	   
		System.out.println("nodes pass");
	   
		assert g.succ("a").size()==1;
	   
		assert g.succ("a").get(0).equals("b");
	   
		System.out.println("succ pass");
	   
		assert g.pred("c").size()==1;
	   
		assert g.pred("c").get(0).equals("b");
	   
		System.out.println("pred pass");
	   
		Graph u = new ListGraph();
	   
		u.addNode("d");
	   
		u.addNode("f");
	   
		u.addEdge("d","f");
	   
		Graph gu = g.union(u);
	   
		assert gu.hasNode("a");
	   
		assert gu.hasNode("b");
	   
		assert gu.hasNode("c");
	   
		assert gu.hasNode("d");
	   
		assert gu.hasNode("f");
	   
		assert gu.hasEdge("a", "b");
	   
		assert gu.hasEdge("b", "c");
	   
		assert gu.hasEdge("c", "d");
	   
		assert gu.hasEdge("d", "a");
	   
		assert gu.hasEdge("d", "f");
	   
		System.out.println("union pass");
	   
		Set<String> nodes = new HashSet<>(Arrays.asList("a", "d","x"));
	   
		Graph sub = gu.subGraph(nodes);
	   
		assert sub.hasNode("a");
	   
		assert !sub.hasNode("b");
	   
		assert !sub.hasNode("c");
	   
		assert sub.hasNode("d");
	   
		assert !sub.hasNode("f");
	   
		assert !sub.hasEdge("a", "b");
	   
		assert !sub.hasEdge("b", "c");
	   
		assert !sub.hasEdge("c", "d");
	   
		assert sub.hasEdge("d", "a");
	   
		assert !sub.hasEdge("d", "f");
	   
		System.out.println("subGraph pass");
	   
		assert gu.connected("a","f");
	   
		assert !gu.connected("f","a");
	   
		System.out.println("connected pass");
	   
		}

		

		public static void addNodeTest() {
			Graph g = new ListGraph();
			assert g.addNode("a");
			assert g.hasNode("a");
		}
	
		public static void addEdgeTest() {
			Graph g = new ListGraph();
			g.addNode("a");
			g.addNode("b");
			g.addEdge("a","b");
			assert g.hasEdge("a","b");
		}
	
		public static void hasNodeTest() {
			Graph g = new ListGraph();
			assert g.addNode("a");
			assert g.hasNode("a");
		}
	
		public static void hasEdgeTest() {
			Graph g = new ListGraph();
			g.addNode("a");
			g.addNode("b");
			g.addEdge("a","b");
			assert g.hasEdge("a","b");
		}
	
		public static void removeNodeTest() {
			Graph g = new ListGraph();
			g.addNode("a");
			assert g.removeNode("a");
			assert !g.hasNode("a");
		}
	
		public static void removeEdgeTest()     {
			Graph g = new ListGraph();
			g.addNode("a");
			g.addNode("b");
			g.addEdge("a","b");
			g.removeEdge("a","b");
			assert !g.hasEdge("a","b");
		}
	
		public static void nodesTest() {
			Graph g = new ListGraph();
			g.addNode("a");
			g.addNode("b");
			g.addNode("c");
			LinkedList<String> expectNodes = new LinkedList<String>() {{
				add("a");
				add("b");
				add("c");
			}
			};
			assert g.nodes().containsAll(expectNodes);
		}
	
		public static void succTest() {
			Graph g = new ListGraph();
			g.addNode("a");
			g.addNode("b");
			g.addNode("c");
			g.addNode("d");
			g.addEdge("a", "b");
			g.addEdge("a", "c");
			g.addEdge("a", "d");
			LinkedList<String> succList = new LinkedList<String>() {{
				add("b");
				add("c");
				add("d");
			}
			};
			assert g.succ("a").containsAll(succList);
		}
	
		public static void predTest() {
			Graph g = new ListGraph();
			EdgeGraph eg = new EdgeGraphAdapter(g);
			g.addNode("a");
			g.addNode("b");
			g.addNode("c");
			g.addNode("d");
			g.addEdge("b", "a");
			g.addEdge("c", "a");
			g.addEdge("d", "a");
			LinkedList<String> predList = new LinkedList<String>() {{
				add("b");
				add("c");
				add("d");
			}
			};
			assert g.pred("a").containsAll(predList);
		}
	
		public static void unionTest() {
			Graph currGraph = new ListGraph();
			currGraph.addNode("a");
			currGraph.addNode("b");
			currGraph.addNode("c");
			currGraph.addEdge("a", "b");
			currGraph.addEdge("b", "c");
	
			Graph g = new ListGraph();
			g.addNode("d");
			g.addNode("e");
			g.addEdge("d", "e");
	
			Graph unionGraph = currGraph.union(g);
	
			assert unionGraph.nodes().containsAll(currGraph.nodes());
			assert unionGraph.nodes().containsAll(g.nodes());
			assert unionGraph.hasEdge("a", "b");
			assert unionGraph.hasEdge("b", "c");
			assert unionGraph.hasEdge("d", "e");
		}
	
		public static void subGraphTest() {
			Graph currGraph = new ListGraph();
			currGraph.addNode("a");
			currGraph.addNode("b");
			currGraph.addNode("c");
			currGraph.addEdge("a", "b");
			currGraph.addEdge("b", "c");
	
			Graph g = new ListGraph();
			g.addNode("a");
			g.addNode("b");
			g.addNode("d");
			g.addEdge("a", "b");
			g.addEdge("b", "d");
			Set<String> set = new HashSet<>(Arrays.asList("a", "b", "d"));
	
			Graph subGraph = new ListGraph();
			subGraph = currGraph.subGraph(set);
			assert subGraph.hasNode("a");
			assert subGraph.hasNode("b");
			assert subGraph.hasEdge("a","b");
		}
	
		public static void connectedTest() {
			Graph g1 = new ListGraph();
			g1.addNode("a");
			g1.addNode("b");
			g1.addNode("c");
			g1.addEdge("a", "b");
			g1.addEdge("b", "c");
	
			Graph g2 = new ListGraph();
			g2.addNode("d");
			g2.addNode("e");
			g2.addEdge("d","e");
	
			assert g1.connected("a","c");
			assert g2.connected("d","e");
		}
    public static void main(String[] args) {
		testHasNodeAddNode(); // okay
		testHasEdgeAddEdge();
		test3();
		testRemoveNode();
		testRemoveEdge();
		testNodes();
		testSucc();
		testPred();
		testSubGraph();
		testUnion();
		testConnected();
		testEdgeGraphAddEdgeHasNodeHasEdge();
		testEdgeGraphOutEdges();
		testEdgeGraphInEdges();
		testEdgeGraphEdges();
		testEdgeGraphRemoveEdge();
		testEdgeGraphUnion();
		testEdgeGraphHasPath();

		// Peers's help
//		testPeerRemoveNode(); // flaw
		 testPeerCombined();
		 testListGraph();
		addNodeTest();
		addEdgeTest();
		hasNodeTest();
		hasEdgeTest();
		removeNodeTest();
		removeEdgeTest();
		nodesTest();
		succTest();
		predTest();
		unionTest();
		subGraphTest();
		connectedTest();
    }

}
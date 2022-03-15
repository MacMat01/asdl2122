/**
 *
 */
package it.unicam.cs.asdl2122.pt1;

import java.lang.reflect.Array;
import java.util.*;

// TODO completare gli import necessari

// ATTENZIONE: è vietato includere import a pacchetti che non siano della Java SE

/**
 * Classe che implementa un grafo orientato tramite matrice di adiacenza. Non
 * sono accettate etichette dei nodi null e non sono accettate etichette
 * duplicate nei nodi (che in quel caso sono lo stesso nodo).
 *
 * I nodi sono indicizzati da 0 a nodeCoount() - 1 seguendo l'ordine del loro
 * inserimento (0 è l'indice del primo nodo inserito, 1 del secondo e così via)
 * e quindi in ogni istante la matrice di adiacenza ha dimensione nodeCount() *
 * nodeCount(). La matrice, sempre quadrata, deve quindi aumentare di dimensione
 * ad ogni inserimento di un nodo. Per questo non è rappresentata tramite array
 * ma tramite ArrayList.
 *
 * Gli oggetti GraphNode<L>, cioè i nodi, sono memorizzati in una mappa che
 * associa ad ogni nodo l'indice assegnato (che può cambiare nel tempo). Il
 * dominio della mappa rappresenta quindi l'insieme dei nodi.
 *
 * Gli archi sono memorizzati nella matrice di adiacenza. A differenza della
 * rappresentazione standard con matrice di adiacenza, la posizione i,j della
 * matrice non contiene un flag di presenza, ma è null se i nodi i e j non sono
 * collegati da un arco e contiene un oggetto della classe GraphEdge<L> se lo
 * sono. Tale oggetto rappresenta l'arco.
 *
 * Questa classe supporta i metodi di cancellazione di nodi e archi e supporta
 * tutti i metodi che usano indici, utilizzando l'indice assegnato a ogni nodo
 * in fase di inserimento ed eventualmente modificato successivamente.
 *
 * @author Luca Tesei (template)
 *
 *
 */
public class AdjacencyMatrixDirectedGraph<L> extends Graph<L> {
    /*
     * Le seguenti variabili istanza sono protected al solo scopo di agevolare
     * il JUnit testing
     */

    protected static int indexNode = 0;

    /*
     * Insieme dei nodi e associazione di ogni nodo con il proprio indice nella
     * matrice di adiacenza
     */
    protected Map<GraphNode<L>, Integer> nodesIndex;

    /*
     * Matrice di adiacenza, gli elementi sono null o oggetti della classe
     * GraphEdge<L>. L'uso di ArrayList permette alla matrice di aumentare di
     * dimensione gradualmente ad ogni inserimento di un nuovo nodo e di
     * ridimensionarsi se un nodo viene cancellato.
     */
    protected ArrayList<ArrayList<GraphEdge<L>>> matrix;


    /**
     * Crea un grafo vuoto.
     */
    public AdjacencyMatrixDirectedGraph() {
        this.matrix = new ArrayList<ArrayList<GraphEdge<L>>>();
        this.nodesIndex = new HashMap<GraphNode<L>, Integer>();
    }

    @Override
    public int nodeCount() {
        return this.nodesIndex.size();
    }

    @Override
    public int edgeCount() {
        int contatore = 0;
        for (ArrayList<GraphEdge<L>> graphEdges : matrix) {
            contatore += graphEdges.size();
        }
        return contatore;
    }

    @Override
    public void clear() {
        this.nodesIndex.clear();
        this.matrix.clear();
    }

    @Override
    public boolean isDirected() {
        //Tutti i grafi di questa classe sono orientati
        return true;
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(GraphNode<L> node) {
        if (node == null) {
            throw new NullPointerException("Tentativo di aggiungere un nodo nullo");
        }
        if (this.nodesIndex.containsKey(node)) {
            //Il nodo già esiste
            return false;
        }
        //Inserisco il nodo
        this.nodesIndex.put(node, indexNode++);
        return true;
    }

    /*
     * Gli indici dei nodi vanno assegnati nell'ordine di inserimento a partire
     * da zero
     */
    @Override
    public boolean addNode(L label) {
        return addNode(new GraphNode<>(label));
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(GraphNode<L> node) {
        if (node == null) {
            throw new NullPointerException("Il nodo passato è null");
        }
        if (nodesIndex.containsKey(node)) {
            int indice = this.nodesIndex.get(node);
            this.nodesIndex.remove(node);
            this.matrix.remove(indice);
            for (ArrayList<GraphEdge<L>> listaArchi : matrix) {
                for (GraphEdge<L> arco : listaArchi) {
                    if (arco.getNode1().equals(node) || arco.getNode2().equals(node)) {
                        listaArchi.remove(arco);
                    }
                }
            }
            //Una entry è una riga (una coppia chiave-valore)
            for (Map.Entry<GraphNode<L>, Integer> entry : this.nodesIndex.entrySet()) {
                if (entry.getValue() > indice) {
                    entry.setValue(entry.getValue() - 1);
                }
            }
        } else throw new IllegalArgumentException("Il nodo passato non esiste in questo grafo");
    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(L label) {
        if (label == null) {
            throw new NullPointerException("L'etichetta passata è null");
        }
        GraphNode<L> nodo = getNode(label);
        if (this.nodesIndex.containsKey(nodo)) {
            removeNode(new GraphNode<>(label));
        } else throw new IllegalArgumentException("L'etichetta data non esiste in nessun nodo di questo grafo");

    }

    /*
     * Gli indici dei nodi il cui valore sia maggiore dell'indice del nodo da
     * cancellare devono essere decrementati di uno dopo la cancellazione del
     * nodo
     */
    @Override
    public void removeNode(int i) {
        if (nodesIndex.containsValue(i) && i < this.nodeCount() - 1) {
            removeNode(new GraphNode(i));
            /* int index = nodesIndex.get(i);
            this.nodesIndex.remove(i);
            this.matrix.remove(index);
            for (ArrayList<GraphEdge<L>> listaArchi : matrix) {
                for (GraphEdge<L> arco : listaArchi) {
                    if (arco.getNode1().equals(i) || arco.getNode2().equals(i)) {
                        listaArchi.remove(arco);
                    }
                }
            } */
        } else
            throw new IndexOutOfBoundsException("L'indice passato non corrisponde a nessun nodo o è fuori dai limiti dell'intervallo");
    }

    @Override
    public GraphNode<L> getNode(GraphNode<L> node) {
        if (node == null) {
            throw new NullPointerException("Il nodo passato è nullo");
        }
        Set<GraphNode<L>> nodi = this.nodesIndex.keySet();
        for (GraphNode<L> n : nodi) {
            if (n.equals(node)) {
                return n;
            }
        }
        //Ritorna null se non esiste
        return null;
    }

    @Override
    public GraphNode<L> getNode(L label) {
        return getNode(new GraphNode<>(label));
    }

    @Override
    public GraphNode<L> getNode(int i) {
        if (!nodesIndex.containsValue(i) || i > this.nodeCount() - 1) {
            throw new IndexOutOfBoundsException("L'indice passato non esiste nel grafo o è fuori dai limiti dell'intervallo");
        }
        return getNode(new GraphNode(i));
    }

    @Override
    public int getNodeIndexOf(GraphNode<L> node) {
        int indice = 0;
        if (node == null) {
            throw new NullPointerException("Il nodo passato è null");
        }
        if (nodesIndex.containsKey(node)) {
            indice = nodesIndex.get(node);
        } else throw new IllegalArgumentException("Il nodo passato non esiste in questo grafo");
        return indice;
    }

    @Override
    public int getNodeIndexOf(L label) {
        if (label == null) {
            throw new NullPointerException("L'etichetta passata è null");
        }
        GraphNode<L> nodo = getNode(label);
        if (!nodesIndex.containsKey(nodo)) {
            throw new IllegalArgumentException("Un nodo con questa etichetta non esiste in questo grafo");
        }
        return getNodeIndexOf(new GraphNode<>(label));
    }

    @Override
    public Set<GraphNode<L>> getNodes() {
        return nodesIndex.keySet();
    }

    @Override
    public boolean addEdge(GraphEdge<L> edge) {
        if (edge == null) {
            throw new NullPointerException("L'arco passato è null");
        }
        if (!edge.isDirected()) {
            throw new IllegalArgumentException("L'arco non è orientato");
        }
        GraphNode<L> nodo1 = edge.getNode1();
        GraphNode<L> nodo2 = edge.getNode2();
        if (!nodesIndex.containsKey(nodo1) || !nodesIndex.containsKey(nodo2)) {
            throw new IllegalArgumentException("Almeno uno dei due nodi non esiste");
        }
        int indice = nodesIndex.get(nodo1);
        int indice2 = nodesIndex.get(nodo2);
        ArrayList<GraphEdge<L>> listaArchi = matrix.get(indice);
        if (listaArchi.contains(edge)) {
            return false;
        }
        listaArchi.set(indice2, edge);
        return true;
    }

    @Override
    public boolean addEdge(GraphNode<L> node1, GraphNode<L> node2) {
        if (node1 == null || node2 == null) {
            throw new NullPointerException("Almeno uno dei 2 nodi è null");
        }
        return addEdge(new GraphEdge<>(node1, node2, true));
    }

    @Override
    public boolean addWeightedEdge(GraphNode<L> node1, GraphNode<L> node2, double weight) {
        if (node1 == null || node2 == null) {
            throw new NullPointerException("Almeno uno dei due nodi è null");
        }
        return addEdge(new GraphEdge<>(node1, node2, true, weight));
    }

    @Override
    public boolean addEdge(L label1, L label2) {
        //noinspection DuplicatedCode
        if (label1 == null || label2 == null) {
            throw new NullPointerException("Una delle etichette è null");
        }
        GraphNode<L> node1 = getNode(label1);
        GraphNode<L> node2 = getNode(label2);
        if (!nodesIndex.containsKey(node1) || !nodesIndex.containsKey(node2)) {
            throw new IllegalArgumentException("Almeno una delle due etichette non esiste in nessun nodo del grafo");
        }
        return addEdge(new GraphEdge<>(node1, node2, true));
    }

    @Override
    public boolean addWeightedEdge(L label1, L label2, double weight) {
        //noinspection DuplicatedCode
        if (label1 == null || label2 == null) {
            throw new NullPointerException("Una delle etichette è null");
        }
        GraphNode<L> node1 = getNode(label1);
        GraphNode<L> node2 = getNode(label2);
        if (!nodesIndex.containsKey(node1) || !nodesIndex.containsKey(node2)) {
            throw new IllegalArgumentException("Almeno una delle due etichette non esiste in nessun nodo del grafo");
        }
        return addWeightedEdge(node1, node2, weight);
    }

    @Override
    public boolean addEdge(int i, int j) {
        //noinspection DuplicatedCode
        GraphNode<L> node1 = getNode(i);
        GraphNode<L> node2 = getNode(j);
        if (!nodesIndex.containsValue(i) || !nodesIndex.containsValue(j) || i > this.nodeCount() - 1 || j > this.nodeCount() - 1) {
            throw new IndexOutOfBoundsException("L'indice passato non esiste nel grafo o è fuori dai limiti dell'intervallo");
        }
        return addEdge(new GraphEdge<>(node1, node2, true));
    }

    @Override
    public boolean addWeightedEdge(int i, int j, double weight) {
        //noinspection DuplicatedCode
        GraphNode<L> node1 = getNode(i);
        GraphNode<L> node2 = getNode(j);
        if (!nodesIndex.containsValue(i) || !nodesIndex.containsValue(j) || i > this.nodeCount() - 1 || j > this.nodeCount() - 1) {
            throw new IndexOutOfBoundsException("L'indice passato non esiste nel grafo o è fuori dai limiti dell'intervallo");
        }
        return addWeightedEdge(node1, node2, weight);
    }

    @Override
    public void removeEdge(GraphEdge<L> edge) {
        if (edge == null) {
            throw new NullPointerException("l'arco passato è nullo");
        }
        GraphNode<L> nodo1 = edge.getNode1();
        GraphNode<L> nodo2 = edge.getNode2();
        if (!nodesIndex.containsKey(nodo1) || !nodesIndex.containsKey(nodo2)) {
            throw new IllegalArgumentException("Almeno uno dei due nodi specificati nell'arco non esiste");
        }
        int indice = nodesIndex.get(nodo1);
        ArrayList<GraphEdge<L>> listaArchi = matrix.get(indice);
        if (!listaArchi.contains(edge)) {
            throw new IllegalArgumentException("L'arco non esiste in questo grafo");
        }
        listaArchi.remove(edge);
    }

    @Override
    public void removeEdge(GraphNode<L> node1, GraphNode<L> node2) {
        if (node1 == null || node2 == null) {
            throw new NullPointerException("Almeno uno dei due nodi passati è nullo");
        }
        removeEdge(new GraphEdge<>(node1, node2, true));
    }

    @Override
    public void removeEdge(L label1, L label2) {
        if (label1 == null || label2 == null) {
            throw new NullPointerException("Almeno uno delle due etichette passate è null");
        }
        GraphNode<L> nodo1 = getNode(label1);
        GraphNode<L> nodo2 = getNode(label2);
        if (!nodesIndex.containsKey(nodo1) || !nodesIndex.containsKey(nodo2)) {
            throw new IllegalArgumentException("Almeno una delle due etichette non esiste nel grafo");
        }
        removeEdge(new GraphEdge<>(nodo1, nodo2, true));
    }

    @Override
    public void removeEdge(int i, int j) {
        //noinspection DuplicatedCode
        GraphNode<L> nodo1 = getNode(i);
        GraphNode<L> nodo2 = getNode(j);
        if (!nodesIndex.containsValue(i) || !nodesIndex.containsValue(j) || i > this.nodeCount() - 1 || j > this.nodeCount() - 1) {
            throw new IndexOutOfBoundsException("L'indice passato non esiste nel grafo o è fuori dai limiti dell'intervallo");
        }
        removeEdge(new GraphEdge<>(nodo1, nodo2, true));
    }


    @Override
    public GraphEdge<L> getEdge(GraphEdge<L> edge) {
        if (edge == null) {
            throw new NullPointerException("L'arco passato è nullo");
        }
        GraphNode<L> nodo1 = edge.getNode1();
        GraphNode<L> nodo2 = edge.getNode2();
        if (!nodesIndex.containsKey(nodo1) || !nodesIndex.containsKey(nodo2)) {
            throw new IllegalArgumentException("Almeno uno dei due nodi dell'arco passato non esiste nel grafo");
        }
        int indice = nodesIndex.get(nodo1);
        ArrayList<GraphEdge<L>> listaArchi = matrix.get(indice);
        for (GraphEdge<L> arco : listaArchi) {
            if (arco.equals(edge)) {
                return arco;
            }
        }
        //Null se l'arco non esiste
        return null;
    }

    @Override
    public GraphEdge<L> getEdge(GraphNode<L> node1, GraphNode<L> node2) {
        if (node1 == null || node2 == null) {
            throw new NullPointerException("Almeno uno dei due nodi passati è null");
        }
        return getEdge(new GraphEdge<>(node1, node2, true));
    }

    @Override
    public GraphEdge<L> getEdge(L label1, L label2) {
        if (label1 == null || label2 == null) {
            throw new NullPointerException("Almeno una delle due etichette è null");
        }
        GraphNode<L> nodo1 = getNode(label1);
        GraphNode<L> nodo2 = getNode(label2);
        return getEdge(new GraphEdge<>(nodo1, nodo2, true));
    }

    @Override
    public GraphEdge<L> getEdge(int i, int j) {
        //noinspection DuplicatedCode
        GraphNode<L> nodo1 = getNode(i);
        GraphNode<L> nodo2 = getNode(j);
        if (!nodesIndex.containsValue(i) || !nodesIndex.containsValue(j) || i > this.nodeCount() - 1 || j > this.nodeCount() - 1) {
            throw new IndexOutOfBoundsException("L'indice passato non esiste nel grafo o è fuori dai limiti dell'intervallo");
        }

        return getEdge(new GraphEdge<>(nodo1, nodo2, true));
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(GraphNode<L> node) {
        if (!nodesIndex.containsKey(node)) {
            throw new IllegalArgumentException("Il nodo passato non esiste");
        }
        if (node == null) {
            throw new NullPointerException("Il nodo passato è nullo");
        }
        Set<GraphNode<L>> listaNodi = nodesIndex.keySet();
        Set<GraphNode<L>> risultato = new HashSet<>();
        for (GraphNode<L> nodo : listaNodi) {
            if (node.getPrevious() == nodo || nodo.getPrevious() == node) {
                risultato.add(nodo);
            }
            return risultato;
        }
        //Null se non esistono nodi adiacenti
        return null;
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(L label) {
        if (label == null) {
            throw new NullPointerException("L'etichetta passata è null");
        }
        GraphNode<L> nodo = getNode(label);
        if (!nodesIndex.containsKey(nodo)) {
            throw new IllegalArgumentException("L'etichetta passata non esiste in nessun nodo di questo grafo");
        }

        return getAdjacentNodesOf(new GraphNode<>(label));
    }

    @Override
    public Set<GraphNode<L>> getAdjacentNodesOf(int i) {
        //noinspection DuplicatedCode
        GraphNode<L> nodo1 = getNode(i);
        if (!nodesIndex.containsValue(i) || i > this.nodeCount() - 1) {
            throw new IndexOutOfBoundsException("L'indice passato non esiste nel grafo o è fuori dai limiti dell'intervallo");
        }
        return getAdjacentNodesOf(nodo1);
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(GraphNode<L> node) {
        if (!nodesIndex.containsKey(node)) {
            throw new IllegalArgumentException("Il nodo passato non esiste");
        }
        if (node == null) {
            throw new NullPointerException("Il nodo passato è nullo");
        }
        Set<GraphNode<L>> listaNodi = nodesIndex.keySet();
        Set<GraphNode<L>> risultato = new HashSet<>();
        for (GraphNode<L> nodo : listaNodi) {
            if (node.getPrevious() == nodo) {
                risultato.add(nodo);
            }
            return risultato;
        }
        //Null se non esistono nodi adiacenti
        return null;
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(L label) {
        if (label == null) {
            throw new NullPointerException("L'etichetta passata è null");
        }
        GraphNode<L> nodo = getNode(label);
        if (!nodesIndex.containsKey(nodo)) {
            throw new IllegalArgumentException("L'etichetta passata non esiste in nessun nodo di questo grafo");
        }

        return getPredecessorNodesOf(new GraphNode<>(label));
    }

    @Override
    public Set<GraphNode<L>> getPredecessorNodesOf(int i) {
        //noinspection DuplicatedCode
        GraphNode<L> nodo1 = getNode(i);
        if (!nodesIndex.containsValue(i) || i > this.nodeCount() - 1) {
            throw new IndexOutOfBoundsException("L'indice passato non esiste nel grafo o è fuori dai limiti dell'intervallo");
        }
        return getPredecessorNodesOf(nodo1);
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(GraphNode<L> node) {
        if (node == null) {
            throw new NullPointerException("Il nodo passato è null");
        }
        if (!nodesIndex.containsKey(node)) {
            throw new IllegalArgumentException("Il nodo passato non esiste");
        }
        int indiceNodo = nodesIndex.get(node);
        Set<GraphEdge<L>> listaArchi = new HashSet<>();
        for (int i = 0; i < matrix.get(indiceNodo).size(); i++) {
            if (matrix.get(indiceNodo).get(i) != null) {
                listaArchi.add(matrix.get(indiceNodo).get(i));
            }
        }
        return null;
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(L label) {
        // TODO implementare
        return null;
    }

    @Override
    public Set<GraphEdge<L>> getEdgesOf(int i) {
        // TODO implementare
        return null;
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(GraphNode<L> node) {
        // TODO implementare
        return null;
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(L label) {
        // TODO implementare
        return null;
    }

    @Override
    public Set<GraphEdge<L>> getIngoingEdgesOf(int i) {
        // TODO implementare
        return null;
    }

    @Override
    public Set<GraphEdge<L>> getEdges() {
        // TODO implementare
        return null;
    }
}

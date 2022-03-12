package it.unicam.cs.asdl2122.pt1;

import java.util.List;

/**
 * Implementazione dell'algoritmo di ordinamento in tempo lineare denominato
 * Counting Sort. In questo caso l'algoritmo, oltre a restituire l'array
 * ordinato, invece del numero di comparazioni effettuate restituisce la
 * dimensione dell'array accessorio che ha creato per eseguire i calcoli.
 * 
 * @author Luca Tesei (Template)
 *
 */
public class CountingSort<E extends Comparable<E>>
        implements SortingAlgorithm<E> {

    /*
     * Invece del numero di confronti restituisce la dimensione dell'array
     * accessorio che ha creato per svolgere la computazione.
     */
    @Override
    public SortingAlgorithmResult<E> sort(List<E> l) {
        // TODO implementare
        return null;
    }

    @Override
    public String getName() {
        return "CountingSort";
    }

}

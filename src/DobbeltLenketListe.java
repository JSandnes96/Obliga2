////////////////// class DobbeltLenketListe //////////////////////////////


import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;



public class DobbeltLenketListe<T> implements Liste<T> {

    /**
     * Node class
     * @param <T>
     */
    private static final class Node<T> {
        private T verdi;                   // nodens verdi
        private Node<T> forrige, neste;    // pekere

        private Node(T verdi, Node<T> forrige, Node<T> neste) {
            this.verdi = verdi;
            this.forrige = forrige;
            this.neste = neste;
        }

        private Node(T verdi) {
            this(verdi, null, null);
        }
    }

    // instansvariabler
    private Node<T> hode;          // peker til den første i listen
    private Node<T> hale;          // peker til den siste i listen
    private int antall;            // antall noder i listen
    private int endringer;         // antall endringer i listen

    public DobbeltLenketListe() {

    }

    public DobbeltLenketListe(T[] a) {
        this();

        if (a == null){
            throw new NullPointerException("Tabellen a er null");
        }

        int i = 0; for (; i < a.length && a[i] == null; i++);

        if (i < a.length)
        {
            Node<T> p = hode = new Node<T>(a[i], null, null);  // den første noden
            antall = 1;                                 // vi har minst en node

            for (i++; i < a.length; i++)
            {
                if (a[i] != null)
                {
                    p = p.neste = new Node<T>(a[i], null, null);   // en ny node
                    antall++;
                }
            }
            hale = p;
        }
    }

    public Liste<T> subliste(int fra, int til){
        throw new NotImplementedException();
    }

    @Override
    public int antall() {
        return antall;
    }

    @Override
    public boolean tom() {
        return antall == 0;
    }

    @Override
    public boolean leggInn(T verdi) {
        if (verdi == null) {
            throw new NullPointerException("Verdien er null");
        }
        if (antall == 0) {
            hode = hale = new Node<>(verdi, null, null);
        } else {
            hale = hale.neste = new Node<>(verdi, hale, null);
        }

        antall++;
        endringer++;

        return true;
    }

    private Node<T> finnNode(int indeks) {

        Node<T> p = hode;
        Node<T> q = hale;

        if (indeks < antall / 2) {
            for (int i = 0; i < indeks; i++) {
                p = p.neste;
            }
        } else {
            for (int i = antall; i == indeks; i--) {
                q = q.forrige;
            }
        }
        return p;
    }

    @Override
    public void leggInn(int indeks, T verdi) {

        Objects.requireNonNull(verdi, "Du kan ikke ha null-verdier");

        indeksKontroll(indeks, true);

        if (indeks == 0)                     // ny verdi skal ligge først
        {
            hode = new Node<T>(verdi, null, hode);    // legges først
            if (antall == 0) hale = hode;      // hode og hale går til samme node
        }
        else if (indeks == antall)           // ny verdi skal ligge bakerst
        {
            hale = hale.neste = new Node<T>(verdi, hale.forrige, hale.neste);  // legges bakerst
        }
        else
        {
            Node<T> p = hode;                  // p flyttes indeks - 1 ganger
            for (int i = 1; i < indeks; i++) p = p.neste;

            p.neste = new Node<T>(verdi, p.forrige, p.neste);  // verdi settes inn i listen
        }

        antall++;

    }

    @Override
    public boolean inneholder(T verdi) {
        throw new NotImplementedException();
    }

    @Override
    public T hent(int indeks) {
        throw new NotImplementedException();
    }

    @Override
    public int indeksTil(T verdi) {
        throw new NotImplementedException();
    }

    @Override
    public T oppdater(int indeks, T nyverdi) {
        throw new NotImplementedException();
    }

    @Override
    public boolean fjern(T verdi) {

        if(verdi == null){
            return false; //ingen nullverdier i listen
        }

        Node<T> p = hode;   //Hjelpepeker
        Node<T> q = null;   //Hjelpepeker

        while(p != null){   //q skal finne verdien t
            if(p.verdi.equals(verdi)){
                break;
            }
            q = p;
            p = p.neste;
        }

        if(q == null){
            return false;
        }

        else if (q == hode){
            hode = hode.neste;
        }

        else{
            q.neste = p.neste;
        }

        if(p == hale){
            hale = q;
        }

        p.verdi = null;
        p.neste = null;

        antall--;
        endringer++;
        return true;


    }

    @Override
    public T fjern(int indeks) {

        indeksKontroll(indeks, false);  // Se Liste, false: indeks = antall er ulovlig

        T temp; //Hjelpevariabel

        if(indeks == 0){
            temp = hode.verdi;
            hode = hode.neste;
            if(antall == 1){
                hale = null;
            }
        }
        else{
            Node<T> p = finnNode(indeks-1);
            Node<T> q = p.neste;
            temp = q.verdi;

            if(q == hale){
                hale = p;
            }

            p.neste = q.neste;
        }

        antall--;
        endringer++;
        return temp;

    }

    @Override
    public void nullstill() {
        throw new NotImplementedException();
    }

    @Override
    public String toString() {
        Node p = hode;
        StringBuilder s = new StringBuilder();
        s.append("[");
        if (!tom()) {
            s.append(p.verdi);
            p = p.neste;
            while (p != null) {
                s.append(", " + p.verdi);
                p = p.neste;
            }
        }
        s.append("]");
        return s.toString();
    }

    public String omvendtString() {
        Node p = hale;
        StringBuilder s = new StringBuilder();
        s.append("[");
        if (!tom()) {
            s.append(p.verdi);
            p = p.forrige;
            while (p != null) {
                s.append(", " + p.verdi);
                p = p.forrige;
            }
        }
        s.append("]");
        return s.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new DobbeltLenketListeIterator();
    }

    public Iterator<T> iterator(int indeks) {
        indeksKontroll(indeks, false);
        return new DobbeltLenketListeIterator(indeks);
    }

    private class DobbeltLenketListeIterator implements Iterator<T>
    {
        private Node<T> denne;
        private boolean fjernOK;
        private int iteratorendringer;

        private DobbeltLenketListeIterator(){

            denne = hode; //p starter på den første i listen
            fjernOK = false; //blir sann når next() kalles
            iteratorendringer = endringer; //teller endringer
        }

        private DobbeltLenketListeIterator(int indeks){

            for(int i = 1; i == indeks; i++){
                denne = denne.neste;
            }

            fjernOK = false; //blir sann når next() kalles
            iteratorendringer = endringer; //teller endringer
        }

        @Override
        public boolean hasNext(){
            return denne != null;
        }

        @Override
        public T next(){

            if(iteratorendringer != endringer){
                throw new ConcurrentModificationException("iteratorendringer er ulik endringer");
            }

            if (!hasNext()){
                throw new NoSuchElementException("Ingen verdier!");
            }

            fjernOK = true;            // nå kan remove() kalles
            T denneVerdi = denne.verdi;    // tar vare på verdien i p
            denne = denne.neste;               // flytter p til den neste noden

            return denneVerdi;         // returnerer verdien
        }

        @Override
        public void remove(){

            if(!fjernOK){
                throw new IllegalStateException("Du kan ikke kalle denne metoden");
            }

            if(iteratorendringer != endringer){
                throw new ConcurrentModificationException("iteratorendringer er ulik endringer");
            }

            fjernOK = false; //remove kan ikke kalles på nytt

            Node<T> q = hode; //hjelpevariabel

            if(hode.neste == denne){
                hode = hode.neste; //den første fjernes
                if(denne == null){
                    hale = null; //dette var den eneste noden
                }
            }

            else{

                Node<T> r = hode; //må finne forgjengeren til denne

                while(r.neste.neste != denne){
                    r = r.neste; //flytter r
                }

                q = r.neste; //q skal fjernes
                r.neste = denne; //hopper over q

                if(denne == null){
                    hale = r; //q var her den siste
                }
            }

            q.verdi = null; //nuller verdien i noden
            q.neste = null; //nuller nestereferansen

            antall--;
        }

    } // class DobbeltLenketListeIterator

    public static <T> void sorter(Liste<T> liste, Comparator<? super T> c) {
        throw new NotImplementedException();
    }

} // class DobbeltLenketListe



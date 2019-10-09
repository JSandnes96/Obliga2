////////////////// class DobbeltLenketListe //////////////////////////////


import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

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



    public static void fratilKontroll(int antall, int fra, int til)
    {
        if (fra < 0)                                  // fra er negativ
            throw new IndexOutOfBoundsException
                    ("fra(" + fra + ") er negativ!");

        if (til > antall)                          // til er utenfor tabellen
            throw new IndexOutOfBoundsException
                    ("til(" + til + ") > antall (" + antall + ")");

        if (fra > til)                                // fra er større enn til
            throw new IllegalArgumentException
                    ("fra(" + fra + ") > til(" + til + ") - illegalt intervall!");
    }


    public Liste<T> subliste(int fra, int til){
        fratilKontroll(antall,fra,til);

        DobbeltLenketListe<T> liste = new DobbeltLenketListe<>();
        for (int i = fra; i < til ; i++){
            liste.leggInn(this.hent(i));
        }
        return liste;

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


        //FUNKER IKKE, VET IKKE HVORFOR
        /*
        Node<T> p;

        if (indeks < antall / 2) {
            p = hode;
            for (int i = 0; i < indeks; i++) {
                p = p.neste;
            }
            return p;
        }
        else {

            p = hale;

            for (int i = antall-1; i==indeks-1; i--) {
                p = p.forrige;
            }
            return p;
        }
        */


        //FUNKER, men er ikke sånn som den skal bli gjort og bruker for lang tid:
        Node<T> p = hode;

        for(int i = 0; i < indeks; i++){
            p = p.neste;
        }

        return p;

    }

    @Override
    public void leggInn(int indeks, T verdi) {
        /*
        indeksKontroll(indeks, true);

        if(indeks <0 || indeks > antall){
            throw new IndexOutOfBoundsException("Indeksen er ikke tillat");
        }


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

        antall++;*/

        indeksKontroll(indeks, true);



        if (verdi == null){
            throw new NullPointerException("verdi a er null");
        }


        if (indeks == 0)                     // ny verdi skal ligge først
        {
            hode = new Node<T>(verdi, null, hode);    // legges først
            hode.neste.forrige = hode;
        }
        if (antall == 0){
            hode = new Node <T>(verdi, null, null);
            hale = hode;      // hode og hale går til samme node
        }

        else if (indeks == antall)           // ny verdi skal ligge bakerst
        {
            //hale = hale.neste = new Node<T>(verdi, hale.forrige, hale.neste);  // legges bakerst
            hale.neste = new Node<T>(verdi, hale, null);
            hale = hale.neste;
        }
        else
        {
            /*Node<T> p = hode;                  // p flyttes indeks - 1 ganger
            for (int i = 1; i < indeks; i++) p = p.neste;

            p.neste = new Node<T>(verdi, p.forrige, p.neste);  // verdi settes inn i listen */

            Node<T> p = finnNode(indeks-1);
            Node<T> q = finnNode(indeks);

            Node<T> ny = new Node<T>(verdi, q.forrige, p.neste);
            p.neste = ny;
            q.forrige = ny;

        }

        endringer++;
        antall++;

    }

    @Override
    public boolean inneholder(T verdi) {
        return indeksTil(verdi) != -1;
    }

    @Override
    public T hent(int indeks) {
        indeksKontroll(indeks, false);  // Se Liste, false: indeks = antall er ulovlig
        return finnNode(indeks).verdi;
    }

    @Override
    public int indeksTil(T verdi) {
        if(verdi == null){
            return -1;
        }

        Node<T> p = hode;

        for(int i = 0; i < antall; i++){
            if(p.verdi.equals(verdi)){
                return i;
            }
            p = p.neste; //Kanskje denne må flyttes
        }
        return -1;
    }

    @Override
    public T oppdater(int indeks, T nyverdi) {
        Objects.requireNonNull(nyverdi, "Ikke tillatt med null-verdier!");

        indeksKontroll(indeks, false);  // Se Liste, false: indeks = antall er ulovlig

        Node<T> p = finnNode(indeks);
        T gammelVerdi = p.verdi;

        p.verdi = nyverdi;

        endringer++;
        return gammelVerdi;
    }

    @Override
    public boolean fjern(T verdi) {

        if(verdi == null){
            return false; //ingen nullverdier i listen
        }

        Node<T> p = hode;   //Hjelpepeker

        while(p != null){   //q skal finne verdien t
            if(p.verdi.equals(verdi)){
                break;
            }
            p = p.neste;
        }

        if(p == null){
            return false;
        }

        else if (p == hode){  //første node
            hode = hode.neste;

            if(hode != null){
                hode.forrige = null;
            }
            else{
                hale = null;
            }
        }

        else if(p == hale){
            hale = hale.forrige;
            hale.neste = null;
        }

        else{
            p.forrige.neste = p.neste;
            p.neste.forrige = p.forrige;
        }

        p.verdi = null;
        p.forrige = p.neste = null;

        antall--;
        endringer++;
        return true;


    }

    @Override
    public T fjern(int indeks) {


        indeksKontroll(indeks, false);


        /*indeksKontroll(indeks, false);  // Se Liste, false: indeks = antall er ulovlig

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
        return temp;*/

        Node<T> temp;

        if(indeks == 0){
            temp = hode;
            hode = hode.neste;
            hode.forrige = null;
        }

        else if(indeks == antall - 1){
            temp = hale;
            hale = hale.forrige;
            hale.neste = null;
        }

        else{
            Node<T> p = finnNode(indeks-1);

            temp = p.neste;

            p.neste = p.neste.neste;
            p.neste.forrige = p;
        }

        antall--;
        endringer++;
        return temp.verdi;

    }

    @Override
    public void nullstill() {
        //1
        /*
        Node<T> p = hode; //hjelpevariabel
        Node<T> q = null; //hjelpevariabel

        while (p != null){ //kjorer gjennom saalenge hode ikke = 0
            q = p.neste;
            p.neste = null;
            p.verdi = null;
            p = q;
        }

        endringer++; //oker antall endringer

        hode = hale = null; //naar den er tom er baade hode og hale lik null
        antall = 0; //naar den er tom er det 0 elementer i lista. */


        //2 Denne metoden var mest tidseffektiv

        for (int i = 0; i < antall; i++){ //lokke som kjorer gjennom og fjerner det som ligger i lista med indeks = i
            fjern(i);
        }

        endringer++; //oker antall endringer
        hode = hale = null; //naar den er tom er baade hode og hale lik null
        antall = 0; //naar den er tom er det 0 elementer i lista.

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

            if (!fjernOK)
                throw new IllegalStateException("Ulovlig tilstand!");


            if(endringer != iteratorendringer){
                throw new ConcurrentModificationException("iteratorendringer er ulik endringer");
            }

            fjernOK = false;
            Node<T> q = hode; // hjelpepeker
            if(antall == 1)//bare et element
            {
                hale = hode = null;
            }
            else if(denne == null) // siste
            {
                q = hale;
                hale = hale.forrige;
                q.forrige = null;
                hale.neste = null;
            }
            else if(denne.forrige==hode) // første
            {
                q = hode;
                hode = q.neste;
                hode.forrige = null;
            }
            else // "midt i"
            {
                Node<T> r = hode;        // hjelpenode til
                while (r.neste.neste != denne)
                {
                    r = r.neste;
                }
                q = r.neste;
                r.neste = denne;
                denne.forrige = r;
            }

            q.verdi = null;
            q.neste = null;
            endringer++;
            iteratorendringer++;
            antall--;

        }

    } // class DobbeltLenketListeIterator

    public static <T> void sorter(Liste<T> liste, Comparator<? super T> c) {
        //throw new NotImplementedException();

        //DobbeltLenketListe<T> a = new DobbeltLenketListe<>();

        for(int i = 0; i < liste.antall(); i++){
            T verdi = liste.hent(i);
            liste.oppdater(i, verdi);
        }



    }

} // class DobbeltLenketListe



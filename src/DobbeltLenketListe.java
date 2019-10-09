////////////////// class DobbeltLenketListe //////////////////////////////


import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

import java.util.function.Predicate;

//Student Jørgen Sandnes
//Studentnummer s331423

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

        if (a == null){ //sjekker om a er null
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
        fratilKontroll(antall,fra,til); //sjekker vha fratilkontrollen

        DobbeltLenketListe<T> liste = new DobbeltLenketListe<>(); //oppretter en ny dobbeltlenket liste
        for (int i = fra; i < til ; i++){ //lokke som kjorer gjennom fra int fra til int til
            liste.leggInn(this.hent(i)); //legger inn i verdien i liste
        }
        return liste; //returnerer liste

    }

    @Override
    public int antall() {
        return antall; //returnerer antall
    }

    @Override
    public boolean tom() {
        return antall == 0; //er true hvis lista er tom (antall = 0)
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
        Node<T> p;  //hjelpevariabel

        if (indeks < antall / 2) { //sjekker om indeksen er storre eller mindre enn antall/2, hvis den er mindre gjor den dette
            p = hode;  //setter p til hode
            for (int i = 0; i < indeks; i++) { //lokke som kjorer frem til i = indeks-1
                p = p.neste; //setter p = p.neste for hver gang
            }
            return p; //returnerer p
        }
        else {
            p = hale; //setter p til hale
            for (int i = antall-1; i==indeks-1; i--) { //lokke som kjorer bakover til i = indeks -1. Den starter bakerst, altsa pa antall-1;
                p = p.forrige; //setter p = p.forrige for hver gang
            }
            return p; //returnerer p
        }
        */


        //FUNKER, men er ikke sånn som den skal bli gjort og bruker for lang tid:
        Node<T> p = hode; //hjelpevariabel

        for(int i = 0; i < indeks; i++){ //lokke som kjorer gjennom helt til i = indeks-1
            p = p.neste; //setter p = p.neste for hver gang
        }

        return p; //returnerer p

    }

    @Override
    public void leggInn(int indeks, T verdi) {


        if (verdi == null) {  //exception som ser etter null-verdier
            throw new NullPointerException("Ikke lov med null-verdier!");
        }
        if (indeks < 0 || indeks > antall) { //exception som sjekker at indeksen som det skal settes inn paa er lovlig
            throw new IndexOutOfBoundsException("Indeksen: (" + indeks + ") er ulovlig");
        }

        Node temp = null; //hjelpevariabel

        if(indeks == 0){
            if(antall == 0){ //hvis det ikke er noen noder der fra for
                hode = new Node<>(verdi, null, null); //hode og hale gaar til samme node
                hale = hode;
            }
            else{ //hvis det er noder som ligger der allerede, som da skal flyttes etter den nye
                temp = hode;
                hode = temp.forrige = new Node<>(verdi, null, hode.neste);
                hode.neste = temp;
            }
        }

        else if(indeks==antall){                //den nye verdien skal ligge bakerst
            hale.neste = new Node<>(verdi, hale, null);
            hale = hale.neste;
        }

        else {
            Node<T> p = hode;
            for (int i = 1; i < indeks; i++){ // lokke som teller fra hode og til indeks - 1
                p = p.neste;
            }
            p.neste = new Node<>(verdi,p, p.neste);  // ny node settes på rett plass
            p.neste.neste.forrige = p.neste;
        }
        antall++; //antallet noder oker
        endringer++; //antallet endringer oker

    }

    @Override
    public boolean inneholder(T verdi) {
        return indeksTil(verdi) != -1; //returnerer indeks til verdien som ikke er -1
    }

    @Override
    public T hent(int indeks) {

        indeksKontroll(indeks, false);  // Se Liste, false: indeks = antall er ulovlig
        return finnNode(indeks).verdi; //returnerer verdien til noden som skal hentes, vha finnNode-metoden

    }

    @Override
    public int indeksTil(T verdi) {
        if(verdi == null){ //sjekker om verdien er null, og returnerer i safall -1
            return -1;
        }

        Node<T> p = hode;  //hjelpevariabel

        for(int i = 0; i < antall; i++){ //forlokke som kjorer gjennom
            if(p.verdi.equals(verdi)){ //hvis verdien av i er lik verdien man er ute etter returneres i.
                return i;
            }
            p = p.neste; //setter p = p.neste for a sjekke neste
        }
        return -1; //returnerer -1
    }

    @Override
    public T oppdater(int indeks, T nyverdi) {

        indeksKontroll(indeks, false);

        Objects.requireNonNull(nyverdi, "Ikke tillatt med null-verdier!"); //sjekker for 0-verdier

        Node<T> p = finnNode(indeks); //setter noden p til den noden som skal fjernes
        T gammelVerdi = p.verdi; //oppretter en variabel gammelverdi og setter denne lik p.verdi

        p.verdi = nyverdi; //setter så p.verdi lik nyverdi

        endringer++; //antall endringer okes
        return gammelVerdi; //returnerer gammelverdi


    }

    @Override
    public boolean fjern(T verdi) {

        /*
        if (verdi == null){ //ingen nullverdier i listen
            return false;
        }

        Node<T> q = hode; //hjelpepeker
        Node<T> p = hale; //hjelpepeker

        while (q != null){              //q skal finne verdien t
            if (q.verdi.equals(verdi)) {
                break;                  //verdien er funnet
            }
            p = q;
            q = q.neste;
        }


        if(q == null){
            return false;  //fant ikke verdi
        }

        else if(q == hode){
            hode = hode.neste;  //går forbi q
        }

        else{
            p.neste = q.neste; //går forbi q
        }


        if(q == hale){
            hale = p;  //oppdaterer hale
        }


        q.verdi = null; //nuller verdien til q
        q.neste = null; //nuller nestepeker

        antall--; //en node mindre i listen
        endringer++; //antall endringer øker
        return true; //vellykket fjerning

        */

        boolean returverdi = false;

        if(verdi != null){
            if(antall == 0){
                throw new IndexOutOfBoundsException("Tom tabell");
            }
            if(inneholder(verdi)) {

                Node<T> p = hode;
                if(p.verdi.equals(verdi)){
                    fjern(0).equals(verdi);
                    returverdi = true;
                }
                else{
                    for(int i = 1; i<antall; i++){
                        if(p.neste.verdi.equals(verdi)){
                            fjern(i).equals(verdi);
                            returverdi = true;
                        }
                        if(i != antall-1)
                            p = p.neste;
                    }
                }
            }
        }
        return returverdi;

    }

    @Override
    public T fjern(int indeks) {

        /*

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
        return temp; */

        indeksKontroll(indeks, false);

        T temp;


        if(antall == 0) throw new IndexOutOfBoundsException("Tabellen er tom");
        if(indeks >antall-1 || indeks <0) throw new IndexOutOfBoundsException("Indeksen du vil fjerne finnes ikke");
        if (indeks == 0) {
            temp = hode.verdi;                 // tar vare på verdien som skal fjernes
            if(hode.neste != null) {
                hode = hode.neste;                  // hode flyttes til neste node
                hode.forrige = null;
            } else
                hode = null;
            if (antall == 1) hale = null;      // det var kun en verdi i listen
        }
        else {
            Node<T> p = finnNode(indeks - 1);  // p er noden foran den som skal fjernes

            Node<T> q = p.neste;               // q skal fjernes

            temp = q.verdi;              // tar vare på verdien som skal fjernes

            if (q == hale) hale = p;           // q er siste node
            p.neste = q.neste;

            if((antall-1) >indeks)
                p.neste.forrige = p;
        }

        endringer++;                   // en ny endring

        antall--;                            // reduserer antallet

        return temp;


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

        Node p = hode;  //hjelpevariabel
        StringBuilder s = new StringBuilder();
        s.append("["); //skal starte med dette
        if (!tom()) {  //sjekker at det ikke er tomt
            s.append(p.verdi);
            p = p.neste;
            while (p != null) { //skriver ut vha en lokke sa-lenge det er verdier i lista
                s.append(", " + p.verdi); //for hver nye verdi legges det inn , + den nye verdien
                p = p.neste; //setter p til p.neste saa neste verdi kommer da
            }
        }
        s.append("]"); //avslutter med ]
        return s.toString(); //returnerer som string
    }

    public String omvendtString() {

        Node p = hale; //hjelpevariabel
        StringBuilder s = new StringBuilder();
        s.append("["); //skal starte med [
        if (!tom()) { //sjekker at det ikke er tomt
            s.append(p.verdi);
            p = p.forrige;
            while (p != null) { //skriver ut vha en lokke sa lenge det er verdier i lista
                s.append(", " + p.verdi);
                p = p.forrige; //setter p til p forrige, sa forrige verdi i lista skal komme neste gang (siden den skal være omvendt, ga motsatt veg)
            }
        }
        s.append("]"); //avslutter med ]
        return s.toString(); //returnerer som string
    }

    @Override
    public Iterator<T> iterator() {
        return new DobbeltLenketListeIterator(); //returnerer en instans av iteratorklassen
    }

    public Iterator<T> iterator(int indeks) {
        indeksKontroll(indeks, false); //sjekker at indeksen er lovlig
        return new DobbeltLenketListeIterator(indeks); //returnerer en instans av iteratorklassen
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

            if(iteratorendringer != endringer){ //kaster exception hvis iteratorendringer og endringer er ulike
                throw new ConcurrentModificationException("iteratorendringer er ulik endringer");
            }

            if (!hasNext()){ //kaster exception hvis det ikke er noen verdier
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

            fjernOK = false; //kan sette fjernok til false
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
            endringer++; //oker antall endringer
            iteratorendringer++; //oker antall iteratorendringer
            antall--; //minker antallet

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



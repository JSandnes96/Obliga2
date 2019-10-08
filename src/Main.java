import java.util.Arrays;
import java.util.Comparator;

public class Main {

    public static void main(String[] args) {

        String[] navn = {"Lars","Anders","Bodil","Kari","Per","Berit"};

        Liste<String> liste1 = new DobbeltLenketListe<>(navn);
        //Liste<String> liste2 = new TabellListe<>(navn);
        //Liste<String> liste3 = new EnkeltLenketListe<>(navn);


        DobbeltLenketListe.sorter(liste1, Comparator.naturalOrder());

        System.out.println(liste1);

        System.out.println(Arrays.toString(navn));


    }
}

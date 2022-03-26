import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

public class RsaPrime {
    public static void main(String[] args) throws IOException {
        File pk = new File("pk.txt");
        File sk = new File("sk.txt");
        File chiffre = new File("chiffre.txt");
        File dText = new File("d-text.txt");

        System.out.println("First delete Files");
        if(pk.delete()) System.out.println("pk File deleted");
        if(sk.delete()) System.out.println("sk File deleted");
        if(chiffre.delete()) System.out.println("chiffre File deleted");
        if(dText.delete()) System.out.println("dText File deleted");

        BigInteger[] fak = RsaGen.getPrimariesFromN();
        BigInteger n = fak[0].multiply(fak[1]); // multipliziert p und q um n zu erhalten
        BigInteger e = RsaGen.getE(fak);
        BigInteger d = RsaGen.getD(fak, e);
        RsaGen.saveRsaKeys(n, e, d); // speichert den Public und Privat-Key in jeweils eine Datei

        Encryption.encryptText(); // verschlüssle text.txt Datei
        Encryption.decryptText(); // entschlüssle chiffre.txt Datei
    }
}

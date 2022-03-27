import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

public class RsaPrime {
    public static void main(String[] args) throws IOException {
        RsaGen.deleteRsaFiles(); // löscht alle tmeporären Dateien bevor die RSA Generierung startet.

        BigInteger[] fak = RsaGen.getPrimariesFromN(); // erhalte zwei Primfaktoren p,q die zusammen multiplizier n ergeben und für die gilt p!=q
        BigInteger n = fak[0].multiply(fak[1]); // multipliziert p und q um n zu erhalten
        BigInteger e = RsaGen.getE(fak); // erhalte e
        BigInteger d = RsaGen.getD(fak, e);
        RsaGen.saveRsaKeys(n, e, d); // speichert den Public und Privat-Key in jeweils eine Datei

        Encryption.encryptText(); // verschlüssle text.txt Datei
        Encryption.decryptText(); // entschlüssle chiffre.txt Datei
    }
}

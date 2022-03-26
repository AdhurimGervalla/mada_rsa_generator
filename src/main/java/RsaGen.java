import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

public class RsaGen {
    final static int BIT_LENGTH = 2^2000;
    final static String SECRET_KEY_FILE_NAME = "sk.txt";
    final static String PUBLIC_KEY_FILE_NAME = "pk.txt";

    /**
     * Generiert eine Variable n mit zwei Primfaktoren p und q für die gilt dass p!= q
     *
     * @return BigInteger n
     */
    public static BigInteger[] getPrimariesFromN() {
        // Mit Hilfe der Klasse BigInteger sollen zwei unterschiedliche Primzahlen zufällig
        // generiert und multipliziert werden.
        BigInteger p = getPrime(BIT_LENGTH);
        BigInteger q;
        do { // es muss gelten p != q => daher weise q solange neu zu bis p != q gilt
            q = getPrime(BIT_LENGTH);
        } while (p.equals(q));

        return new BigInteger[]{p, q};
    }

    public static BigInteger getM(BigInteger[] primaries) {
        // e element von Ze(n) -> Alle Zahlen die zu e(n) teilerfremd sind
        // e(n) -> Euklidische e-Funktion
        // e(p*q) = (p - 1) * p^0 * (q - 1) * q^0 = (p - 1) * (q - 1)
        BigInteger e_from_n = (primaries[0].subtract(BigInteger.ONE)).multiply((primaries[1].subtract(BigInteger.ONE)));

        return e_from_n;
    }

    public static BigInteger getE(BigInteger[] primaries) {
        // e element von Ze(n) -> Alle Zahlen die zu e(n) teilerfremd sind
        // e(n) -> Euklidische e-Funktion
        // e(p*q) = (p - 1) * p^0 * (q - 1) * q^0 = (p - 1) * (q - 1)
        BigInteger e_from_n = getM(primaries);

        BigInteger e = BigInteger.TWO;
        while (!e.gcd(e_from_n).equals(BigInteger.ONE)) {
            e = e.add(BigInteger.ONE);
        }

        if (e.gcd(e_from_n).compareTo(BigInteger.ONE) == 0) return e;

        return BigInteger.ONE;
    }

    /**
     * @return
     */
    public static BigInteger getD(BigInteger[] primaries, BigInteger e) {
        // Es soll ein geeignetes e gewählt werden und dazu das
        // passende d bestimmt werden.
        // Dazu ist insbesondere der erweiterte euklidische Algorithmus zu implementieren.
        BigInteger m = getM(primaries);

        BigInteger a = m;
        BigInteger b = e;
        BigInteger x0 = BigInteger.ONE;
        BigInteger y0 = BigInteger.ZERO;
        BigInteger x1 = BigInteger.ZERO;
        BigInteger y1 = BigInteger.ONE;

        BigInteger q = a.divide(b);
        BigInteger r = a.mod(b);

        while (!b.equals(BigInteger.ZERO)) {
            a = b;
            b = r;

            BigInteger x1Old = x1;
            BigInteger y1Old = y1;
            x1 = x0.subtract(q.multiply(x1));
            y1 = y0.subtract(q.multiply(y1));

            x0 = x1Old;
            y0 = y1Old;

            if (b.compareTo(BigInteger.ZERO) == 0) break;

            q = a.divide(b);
            r = a.mod(b);
        }

        // xo * n + y0 * e

        // if ( y0 < 0) y0 = y0 mod m;
        if (y0.compareTo(BigInteger.ZERO) < 0) y0 = y0.mod(m);

        return y0;
    }

    /**
     * Speichert den PK und SK in das Dateisystem
     * @param n
     * @param e
     * @param d
     */
    public static void saveRsaKeys(BigInteger n, BigInteger e, BigInteger d) {
        // Der private Schlussel soll in einer Datei sk.txt in der Form (n, d) mit n und d in Dezimaldarstellung abgespeichert werden

        // source code for how to write a file in java took from here: https://www.w3schools.com/java/java_files_create.asp
        try {
            File skFile = new File(SECRET_KEY_FILE_NAME);
            File pkFile = new File(PUBLIC_KEY_FILE_NAME);

            if (skFile.createNewFile()) {
                System.out.println("File created: " + skFile.getName());
                FileWriter skFileWriter = new FileWriter(SECRET_KEY_FILE_NAME);
                skFileWriter.write("(" + n + "," + d + ")");
                skFileWriter.close();
            } else {
                System.out.println("File already exists.");
            }

            if (pkFile.createNewFile()) {
                System.out.println("File created: " + pkFile.getName());
                FileWriter pkFileWriter = new FileWriter(PUBLIC_KEY_FILE_NAME);
                pkFileWriter.write("(" + n + "," + e + ")");
                pkFileWriter.close();
            } else {
                System.out.println("File already exists.");
            }

        } catch (IOException ex) {
            System.out.println("An error occurred.");
            ex.printStackTrace();
        }
    }

    public static BigInteger getPrime(int bitLength) {
        Random random = new Random();
        return BigInteger.probablePrime(bitLength, random);
    }
}

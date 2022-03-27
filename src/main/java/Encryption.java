import java.io.*;
import java.math.BigInteger;

public class Encryption {

    /**
     * Diese Funktion ist für die Verschlüsslung des Inhaltes der Datei "text.txt" zuständig.
     * Sie sucht sich die Dateien text.txt und pk.txt im gleichen Verzeichnis wie das Programm.
     * Aus der Datei pk.txt holt sie sich n und e.
     * Anschliessend wird jedes einzelne Zeichen x (als ASCII) in der Datei text.txt folgendermassen verschlüsselt:
     * x^e mod n und in eine Datei chiffre.txt geschrieben.
     *
     * @throws IOException
     */
    public static void encryptText() throws IOException {
        String file ="text.txt";
        String pkFile = "pk.txt";

        BufferedReader textToEncrypt = new BufferedReader(new FileReader(file));
        String currentLine = textToEncrypt.readLine();
        textToEncrypt.close();

        BufferedReader pk = new BufferedReader(new FileReader(pkFile));
        String line = pk.readLine();
        pk.close();

        line = line.replace("(", "");
        line = line.replace(")", "");
        String[] keys = line.split(",");
        BigInteger n = new BigInteger(keys[0]);
        BigInteger e = new BigInteger(keys[1]);

        StringBuilder encryptedText = new StringBuilder();
        for (int i = 0; i < currentLine.length(); i++) {
            BigInteger a = BigInteger.valueOf(currentLine.charAt(i)); // konvertierung zu ascii
            BigInteger ch = a.modPow(e, n); // verschlüsslung des ascii Zeichens mit x^e mod n
            encryptedText.append(ch);
            if (currentLine.length() -1 > i) encryptedText.append(',');
        }

        // erstellen der chiffre.txt Datei
        File chiffreFile = new File("chiffre.txt");

        if (chiffreFile.createNewFile()) {
            System.out.println("File created: " + chiffreFile.getName());
            FileWriter chiffreFileWriter = new FileWriter("chiffre.txt");
            chiffreFileWriter.write(encryptedText.toString()); // schreiben des chiffre String in die Datei
            chiffreFileWriter.close();
        } else {
            System.out.println("File already exists.");
        }
    }

    /**
     * Diese Funktion ist für die Entschlüsslung des Inhaltes der Datei "chiffre.txt" zuständig.
     * Sie sucht sich die Dateien chiffre.txt und sk.txt im gleichen Verzeichnis wie das Programm.
     * Aus der Datei sk.txt holt sie sich n und d.
     * Die Datei chiffre wird beim Komma gesplitted. Dadurch weiss man, welche Zahlen zu einem Zeichen gehören.
     * Anschliessend werden die einzelnen Zahlen dechiffriert mit:
     * x^d mod n und in eine Datei text-d.txt geschrieben.
     *
     * @throws IOException
     */
    public static void decryptText() throws IOException {
        String file ="chiffre.txt";
        String pkFile = "sk.txt";

        BufferedReader textToDencrypt = new BufferedReader(new FileReader(file));
        String currentLine = textToDencrypt.readLine();
        textToDencrypt.close();

        BufferedReader sk = new BufferedReader(new FileReader(pkFile));
        String line = sk.readLine();
        sk.close();

        // auslesen von n und d aus sk.txt
        line = line.replace("(", "");
        line = line.replace(")", "");
        String[] keys = line.split(",");
        BigInteger n = new BigInteger(keys[0]);
        BigInteger d = new BigInteger(keys[1]);

        StringBuilder decryptedText = new StringBuilder();
        String[] chiffres = currentLine.split(","); // Spaltung beim Komma
        for (int i = 0; i < chiffres.length; i++) {
            BigInteger a = new BigInteger(chiffres[i]);
            BigInteger ch = a.modPow(d,n); // entschlüsslung mit x^d mod n
            char c  = (char) ch.intValue(); // umwandlung von ascii zu char
            decryptedText.append(c);
        }

        File decryptedFile = new File("d-text.txt");

        if (decryptedFile.createNewFile()) {
            System.out.println("File created: " + decryptedFile.getName());
            FileWriter decryptFileWriter = new FileWriter("d-text.txt"); // erstellen der dechiffrierten Datei
            decryptFileWriter.write(decryptedText.toString()); // rein schreiben
            decryptFileWriter.close();
        } else {
            System.out.println("File already exists.");
        }
    }
}

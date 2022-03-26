import java.io.*;
import java.math.BigInteger;

public class Encryption {
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
            BigInteger a = BigInteger.valueOf(currentLine.charAt(i));
            BigInteger ch = a.modPow(e, n);
            encryptedText.append(ch);
            if (currentLine.length() -1 > i) encryptedText.append(',');
        }

        File chiffreFile = new File("chiffre.txt");

        if (chiffreFile.createNewFile()) {
            System.out.println("File created: " + chiffreFile.getName());
            FileWriter chiffreFileWriter = new FileWriter("chiffre.txt");
            chiffreFileWriter.write(encryptedText.toString());
            chiffreFileWriter.close();
        } else {
            System.out.println("File already exists.");
        }
    }

    public static void decryptText() throws IOException {
        String file ="chiffre.txt";
        String pkFile = "sk.txt";

        BufferedReader textToDencrypt = new BufferedReader(new FileReader(file));
        String currentLine = textToDencrypt.readLine();
        textToDencrypt.close();

        BufferedReader sk = new BufferedReader(new FileReader(pkFile));
        String line = sk.readLine();
        sk.close();

        line = line.replace("(", "");
        line = line.replace(")", "");
        String[] keys = line.split(",");
        BigInteger n = new BigInteger(keys[0]);
        BigInteger d = new BigInteger(keys[1]);

        StringBuilder decryptedText = new StringBuilder();
        String[] chiffres = currentLine.split(",");
        for (int i = 0; i < chiffres.length; i++) {
            BigInteger a = new BigInteger(chiffres[i]);
            BigInteger ch = a.modPow(d,n);
            char c  = (char) ch.intValue();
            decryptedText.append(c);
        }

        File decryptedFile = new File("d-text.txt");

        if (decryptedFile.createNewFile()) {
            System.out.println("File created: " + decryptedFile.getName());
            FileWriter decryptFileWriter = new FileWriter("d-text.txt");
            decryptFileWriter.write(decryptedText.toString());
            decryptFileWriter.close();
        } else {
            System.out.println("File already exists.");
        }
    }
}

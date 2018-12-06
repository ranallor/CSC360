
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Luca
 */
public class Encryption {
    private final SecureRandom ran = new SecureRandom();
    private SecretKey secretKey;
    public Encryption(){
        
    }
    
    public byte[] encrypteMessage(byte[] message)
    throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        keygen.init(256, ran);
        Cipher encryptor = Cipher.getInstance("AES/ECB/PKCS5Padding");
        secretKey = keygen.generateKey();
        encryptor.init(Cipher.ENCRYPT_MODE, secretKey);
        return encryptor.doFinal(message);
        
    }
    public byte[] decryptMessage(byte[] encryptedMessage) 
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher encryptor = Cipher.getInstance("AES/ECB/PKCS5Padding");
        encryptor.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedMessage = encryptor.doFinal(encryptedMessage);
        return decryptedMessage;
    }
}

package com.leon.services;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;


public class ECCServiceImpl
{
    private static final String SPEC = "secp256k1";
    private static final String ALGO = "SHA256withECDSA";

    private JSONObject sender() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, UnsupportedEncodingException, SignatureException
    {

        ECGenParameterSpec ecSpec = new ECGenParameterSpec(SPEC);
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        keyPairGenerator.initialize(ecSpec, new SecureRandom());
        KeyPair keypair = keyPairGenerator.generateKeyPair();
        PublicKey publicKey = keypair.getPublic();
        PrivateKey privateKey = keypair.getPrivate();

        String plaintext = "bitcoin-service";

        Signature ecdsaSign = Signature.getInstance(ALGO);
        ecdsaSign.initSign(privateKey);
        ecdsaSign.update(plaintext.getBytes("UTF-8"));
        byte[] signature = ecdsaSign.sign();
        String pub = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String sig = Base64.getEncoder().encodeToString(signature);

        JSONObject obj = new JSONObject();
        obj.put("publicKey", pub);
        obj.put("signature", sig);
        obj.put("message", plaintext);
        obj.put("algorithm", ALGO);

        return obj;
    }

    private boolean receiver(JSONObject obj) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, UnsupportedEncodingException, SignatureException
    {
        Signature ecdsaVerify = Signature.getInstance(obj.getString("algorithm"));
        KeyFactory kf = KeyFactory.getInstance("EC");

        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(obj.getString("publicKey")));

        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        ecdsaVerify.initVerify(publicKey);
        ecdsaVerify.update(obj.getString("message").getBytes("UTF-8"));
        boolean result = ecdsaVerify.verify(Base64.getDecoder().decode(obj.getString("signature")));

        return result;
    }

    public static void main(String[] args)
    {

        // TODO move this to a test class.
        try
        {
            ECCServiceImpl ecc = new ECCServiceImpl();
            JSONObject obj = ecc.sender();
            boolean result = ecc.receiver(obj);
        }
        catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(ECCServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InvalidAlgorithmParameterException ex)
        {
            Logger.getLogger(ECCServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InvalidKeyException ex)
        {
            Logger.getLogger(ECCServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (UnsupportedEncodingException ex)
        {
            Logger.getLogger(ECCServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SignatureException ex)
        {
            Logger.getLogger(ECCServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (InvalidKeySpecException ex)
        {
            Logger.getLogger(ECCServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

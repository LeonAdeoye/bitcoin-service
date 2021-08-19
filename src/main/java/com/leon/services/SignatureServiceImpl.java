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
import org.json.JSONObject;
import org.slf4j.LoggerFactory;


public class SignatureServiceImpl
{
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SignatureServiceImpl.class);
    private static final String SPEC = "secp256k1";
    private static final String ALGO = "SHA256withECDSA";

    private static KeyPair createKeyPair() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException
    {
        ECGenParameterSpec ecSpec = new ECGenParameterSpec(SPEC);
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        keyPairGenerator.initialize(ecSpec, new SecureRandom());
        return keyPairGenerator.generateKeyPair();
    }

    private static String createSignature(String plainText, KeyPair keyPair) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, SignatureException
    {
        PrivateKey privateKey = keyPair.getPrivate();
        Signature ecdsaSign = Signature.getInstance(ALGO);
        ecdsaSign.initSign(privateKey);
        ecdsaSign.update(plainText.getBytes("UTF-8"));
        byte[] signature = ecdsaSign.sign();
        return Base64.getEncoder().encodeToString(signature);
    }

    public static JSONObject createSignedMessage(String plainText) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, UnsupportedEncodingException, SignatureException
    {
        KeyPair keyPair = createKeyPair();
        String signature = createSignature(plainText, keyPair);
        String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        JSONObject signedMessage = new JSONObject();
        signedMessage.put("publicKey", publicKey);
        signedMessage.put("signature", signature);
        signedMessage.put("message", plainText);
        signedMessage.put("algorithm", ALGO);
        return signedMessage;
    }

    private static boolean verifySignedMessage(JSONObject signedMessage) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, UnsupportedEncodingException, SignatureException
    {
        Signature ecdsaVerify = Signature.getInstance(signedMessage.getString("algorithm"));
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(signedMessage.getString("publicKey")));
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
        ecdsaVerify.initVerify(publicKey);
        ecdsaVerify.update(signedMessage.getString("message").getBytes("UTF-8"));
        return ecdsaVerify.verify(Base64.getDecoder().decode(signedMessage.getString("signature")));
    }

    public static void main(String[] args)
    {
        try
        {
            SignatureServiceImpl signatureService = new SignatureServiceImpl();
            JSONObject signedMessage = signatureService.createSignedMessage("Hello Horatio");

            if(signatureService.verifySignedMessage(signedMessage))
                logger.info("Signature Valid");
            else
                logger.info("Signature Invalid");
        }
        catch (NoSuchAlgorithmException ex)
        {
            logger.error(ex.getMessage());
        }
        catch (InvalidAlgorithmParameterException ex)
        {
            logger.error(ex.getMessage());
        }
        catch (InvalidKeyException ex)
        {
            logger.error(ex.getMessage());
        }
        catch (UnsupportedEncodingException ex)
        {
            logger.error(ex.getMessage());
        }
        catch (SignatureException ex)
        {
            logger.error(ex.getMessage());
        }
        catch (InvalidKeySpecException ex)
        {
            logger.error(ex.getMessage());
        }
    }

}

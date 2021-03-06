package com.leon.controllers;

import com.leon.services.KeyService;
import com.leon.services.KeyServiceImpl;
import com.leon.services.UtilityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class KeyController
{
    private static final Logger logger = LoggerFactory.getLogger(KeyController.class);
    @Autowired
    private KeyService keyService;

    @CrossOrigin
    @RequestMapping("/createNewKey")
    public String createNewKey()
    {
        logger.info("Received request to create new key.");
        return this.keyService.createNewKey();
    }

    @CrossOrigin
    @RequestMapping(value = "/getPrivateKeyHex", method=GET)
    public String getPrivateKeyHex(@RequestParam String publicKeyHex) throws IllegalArgumentException
    {
        if(publicKeyHex == null || publicKeyHex.isEmpty())
        {
            logger.error("Public key hex cannot be null or empty.");
            throw new IllegalArgumentException("Public key hex cannot be null or empty.");
        }

        logger.info("Received request to get private key in HEX using public key hex: " + publicKeyHex);
        return this.keyService.getPrivateKeyHex(publicKeyHex);
    }

    @CrossOrigin
    @RequestMapping(value = "/getPrivateKeyWIF", method=GET)
    public String getPrivateKeyWIF(@RequestParam String publicKeyHex) throws IllegalArgumentException
    {
        if(publicKeyHex == null || publicKeyHex.isEmpty())
        {
            logger.error("Public key hex cannot be null or empty.");
            throw new IllegalArgumentException("Public key hex cannot be null or empty.");
        }

        logger.info("Received request to get private key WIF using public key hex:" + publicKeyHex);
        return this.keyService.getPrivateKeyWIF(publicKeyHex);
    }

    @CrossOrigin
    @RequestMapping(value = "/convertHexadecimalToByteArray", method=GET)
    public String convertHexToByteArray(@RequestParam String keyHex) throws IllegalArgumentException
    {
        if(keyHex == null || keyHex.isEmpty())
        {
            logger.error("Key hexadecimal cannot be null or empty.");
            throw new IllegalArgumentException("Key hex cannot be null or empty.");
        }

        logger.info("Received request to convert hexadecimal key: " + keyHex + " to a byte array.");
        byte[] result = UtilityServiceImpl.convertHexadecimalToByteArray(keyHex);
        return result.toString();
    }

    @CrossOrigin
    @RequestMapping(value = "/derivePublicKey", method=GET)
    public String derivePublicKeyHexFromPrivateKeyHex(@RequestParam String privateKeyHex) throws IllegalArgumentException
    {
        if(privateKeyHex == null || privateKeyHex.isEmpty())
        {
            logger.error("Private key hex cannot be null or empty.");
            throw new IllegalArgumentException("Private key hex cannot be null or empty.");
        }

        logger.info("Received request to derive public key from private key hex: " + privateKeyHex);
        return KeyServiceImpl.derivePublicKeyHexFromPrivateKeyHex(privateKeyHex);
    }

    @CrossOrigin
    @RequestMapping(value = "/generateAddress", method=GET)
    public String getAddressFromPublicKey(@RequestParam String publicKeyHex) throws IllegalArgumentException
    {
        if(publicKeyHex == null || publicKeyHex.isEmpty())
        {
            logger.error("Public key hexadecimal cannot be null or empty.");
            throw new IllegalArgumentException("Public key hexadecimal cannot be null or empty.");
        }

        logger.info("Received request to generate a bitcoin address from a public key hexadecimal: " + publicKeyHex);
        return KeyServiceImpl.getAddressFromCompressedPublicKey(publicKeyHex);
    }

}

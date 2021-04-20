package com.leon.controllers;

import com.leon.services.ConfigurationService;
import com.leon.services.KeyService;
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
    private ConfigurationService configurationService;
    @Autowired
    private KeyService keyService;


    @CrossOrigin
    @RequestMapping("/create")
    public void createNewKey()
    {
        logger.info("Received request to create new key");
        this.keyService.createNewKey();
    }

    @CrossOrigin
    @RequestMapping(value = "/getPrivateKeyHex", method=GET)
    public String getPrivateKeyHex(@RequestParam String publicKeyHex) throws IllegalArgumentException
    {
        if(publicKeyHex == null || publicKeyHex.isEmpty())
            throw new IllegalArgumentException("public key hex cannot be null or empty");

        logger.info("Received request to get private key in HEX");
        return this.keyService.getPrivateKeyHex(publicKeyHex);
    }


    @CrossOrigin
    @RequestMapping(value = "/getPrivateKeyWIF", method=GET)
    public String getPrivateKeyWIF(@RequestParam String publicKeyHex) throws IllegalArgumentException
    {
        if(publicKeyHex == null || publicKeyHex.isEmpty())
            throw new IllegalArgumentException("public key hex cannot be null or empty");

        logger.info("Received request to get private key WIF");
        return this.keyService.getPrivateKeyWIF(publicKeyHex);
    }

}

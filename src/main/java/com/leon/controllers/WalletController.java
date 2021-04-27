package com.leon.controllers;

import com.leon.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class WalletController
{
    private static final Logger logger = LoggerFactory.getLogger(WalletController.class);

    @Autowired
    private WalletService walletService;

    @CrossOrigin
    @RequestMapping(value = "/addNewKey", method=POST)
    public String createNewKey(@RequestParam String privateKeyAsHex) throws IllegalArgumentException
    {
        if(privateKeyAsHex == null || privateKeyAsHex.isEmpty())
        {
            logger.error("Private key hex cannot be null or empty.");
            throw new IllegalArgumentException("Private key hex cannot be null or empty.");
        }
        logger.info("Received request to add private key to wallet.");
        return this.walletService.addKey(privateKeyAsHex);
    }

    @CrossOrigin
    @RequestMapping(value = "/hasPublicKeyHex", method=GET)
    public boolean hasKey(@RequestParam String publicKeyHex) throws IllegalArgumentException
    {
        if(publicKeyHex == null || publicKeyHex.isEmpty())
        {
            logger.error("Public key hex cannot be null or empty.");
            throw new IllegalArgumentException("Public key hex cannot be null or empty.");
        }

        logger.info("Received request to search wallet for public key hex: " + publicKeyHex);
        return this.walletService.hasKey(publicKeyHex);
    }
}
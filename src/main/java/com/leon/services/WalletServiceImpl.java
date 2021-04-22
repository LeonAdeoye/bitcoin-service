package com.leon.services;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.TestNet3Params;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.File;
import java.math.BigInteger;

@Service
public class WalletServiceImpl implements WalletService
{
    private static final Logger logger = LoggerFactory.getLogger(WalletServiceImpl.class);

    private static WalletAppKit kit;

    public WalletServiceImpl()
    {
    }

    @PostConstruct
    public void initialise()
    {
        NetworkParameters params;
        String filePrefix;

        params = TestNet3Params.get();
        filePrefix = "bitcoin-service";
        logger.info("Network: " + params.getId());

        // Start up a basic app using a class that automates some boilerplate.
        kit = new WalletAppKit(params, new File("."), filePrefix);
    }

    public void addKey(String privateKeyAsHex)
    {
        //kit.wallet().importKey(new ECKey().fromPrivate(new BigInteger(privateKeyAsHex, 16)));
    }
}

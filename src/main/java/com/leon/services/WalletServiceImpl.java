package com.leon.services;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.RegTestParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.File;

@Service
public class WalletServiceImpl implements WalletService
{
    private static final Logger logger = LoggerFactory.getLogger(WalletServiceImpl.class);

    private static WalletAppKit kit;

    public WalletServiceImpl()
    {
    }

    @Override
    public void initialise()
    {
        NetworkParameters params;
        String filePrefix;

        params = RegTestParams.get();
        filePrefix = "forwarding-service-regtest";
        logger.info("Network: " + params.getId());

        // Start up a basic app using a class that automates some boilerplate.
        kit = new WalletAppKit(params, new File("."), filePrefix);
    }
}

package com.leon.services;

import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.wallet.KeyChainGroup;
import org.bitcoinj.wallet.Wallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.io.File;
import java.math.BigInteger;

@Service
public class WalletServiceImpl implements WalletService
{
    private static final Logger logger = LoggerFactory.getLogger(WalletServiceImpl.class);

    private static WalletAppKit kit;

    @Autowired
    ConfigurationService configurationService;

    public WalletServiceImpl()
    {
    }

    @PostConstruct
    public void initialise()
    {
        String filePrefix = "bitcoin-service";

        NetworkParameters networkParameters = ConfigurationServiceImpl.getNetworkParams();

//        kit = new WalletAppKit(networkParameters, new File("."), filePrefix);
//
//        KeyChainGroup group = new KeyChainGroup(networkParameters, true);
//        Wallet wallet = new Wallet(networkParameters, group);
    }

    public void addKey(String privateKeyAsHex)
    {
        //wallet.importKey(new ECKey().fromPrivate(new BigInteger(privateKeyAsHex, 16)));
    }

    public void addKey(ECKey key)
    {
        //kit.wallet().importKey(key);
    }

    public boolean hasKey(ECKey key)
    {
        //return kit.wallet().hasKey(key);
        return false;
    }
}

package com.leon.services;

import org.bitcoinj.core.ECKey;

import java.util.List;

public interface WalletService
{
    String addKey(String privateKeyAsHex);
    int addKeys(List<String> privateKeysAsHex);
    boolean hasKey(String publicKeyAsHex);
}

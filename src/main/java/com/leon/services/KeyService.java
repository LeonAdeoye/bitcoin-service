package com.leon.services;



public interface KeyService
{
    void createNewKey();
    String getPrivateKeyHex(String publicKeyHex);
    String getPrivateKeyWIF(String publicKeyHEx);
}

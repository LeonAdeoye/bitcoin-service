package com.leon.services;



public interface KeyService
{
    String createNewKey();
    String getPrivateKeyHex(String publicKeyHex);
    String getPrivateKeyWIF(String publicKeyHEx);
}

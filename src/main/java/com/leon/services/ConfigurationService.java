package com.leon.services;

import org.bitcoinj.core.NetworkParameters;

public interface ConfigurationService
{
    String getConfigurationValue(String owner, String key);
    void loadAllConfigurations();
    void reconfigure();
}
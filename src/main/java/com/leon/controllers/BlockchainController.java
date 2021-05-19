package com.leon.controllers;

import com.leon.services.BlockChainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@CrossOrigin
@RestController
public class BlockchainController
{
    private static final Logger logger = LoggerFactory.getLogger(BlockchainController.class);
    @Autowired
    private BlockChainService blockChainService;

    @CrossOrigin
    @RequestMapping(value = "/getBlock", method=GET)
    public boolean getBlock(@RequestParam String blockHash) throws IllegalArgumentException
    {
        if(blockHash == null || blockHash.isEmpty())
        {
            logger.error("Hash of block to fetch cannot be null or empty.");
            throw new IllegalArgumentException("Hash of block to fetch cannot be null or empty.");
        }

        logger.info("Received request to fetch block using block hash: " + blockHash);
        return this.blockChainService.fetchBlock(blockHash);
    }
}

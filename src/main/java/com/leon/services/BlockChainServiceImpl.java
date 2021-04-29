package com.leon.services;

import org.bitcoinj.core.*;
import org.bitcoinj.net.discovery.DnsDiscovery;
import org.bitcoinj.store.BlockStore;
import org.bitcoinj.store.MemoryBlockStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.net.InetAddress;


@Service
public class BlockChainServiceImpl implements BlockChainService
{
    private static final Logger logger = LoggerFactory.getLogger(KeyServiceImpl.class);
    private boolean localhost = false;

    @Override
    public boolean fetchBlock(String blockHashToDownload)
    {
        boolean success = false;
        try
        {
            final NetworkParameters networkParameters = ConfigurationServiceImpl.getNetworkParams();
            BlockStore blockStore = new MemoryBlockStore(networkParameters);
            BlockChain blockChain = new BlockChain(networkParameters, blockStore);
            PeerGroup peerGroup = new PeerGroup(networkParameters, blockChain);

            if(localhost)
            {
                peerGroup.addPeerDiscovery(new DnsDiscovery(networkParameters));
            }
            else
            {
                PeerAddress addr = new PeerAddress(networkParameters, InetAddress.getLocalHost());
                peerGroup.addAddress(addr);
            }

            peerGroup.start();
            //peerGroup.waitForPeers(1).get();
            Peer peer = peerGroup.getConnectedPeers().get(0);

            // Retrieve a block through a peer
            Sha256Hash blockHash = Sha256Hash.wrap(blockHashToDownload);
            //Future<Block> future = peer.getBlock(blockHash);

            logger.info("Waiting for node to send us the requested block: " + blockHash);

            //Block block = future.get();
            //peerGroup.stopAsync();

            success = true;
        }
        catch(Exception e)
        {
            logger.error(e.getMessage());
        }
        finally
        {
            return success;
        }
    }
}

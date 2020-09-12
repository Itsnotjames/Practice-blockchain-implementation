package blockchain;

import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Blockchain {
    public HashMap<String, Block> blocks;
    private static Blockchain instance;
    public ArrayList<Transfer> transferPool;
    public String lastBlockHash;
    int transferCount;

    // Create the seed block, blockchain, and key generator.
    private Blockchain () {
        blocks = new HashMap<>();
        Block block = new Block( "0", 0, new ArrayList<>(), null);
        blocks.put(block.hash, block);
        transferPool = new ArrayList<>();
        lastBlockHash = block.hash;
        transferCount = 0;
    }

    public Block getLastBlock () {
        return blocks.get(lastBlockHash);
    }

    public Block getBlock (String blockHash) {
        return blocks.get(blockHash);
    }

    public void addBlock (Block blockToAdd) {
        synchronized (this) {
            if (blocks.containsKey(blockToAdd.previousHash) && !blocks.containsKey(blockToAdd.hash)) {
                blocks.put(blockToAdd.hash, blockToAdd);
                lastBlockHash = blockToAdd.hash;
                transferCount++;
            }
        }
    }

    public static Blockchain getInstance() {
        if (instance == null) {
            instance = new Blockchain();
        }
        return instance;
    }

    public void addTransfer(byte[] encryptedTransfer, PublicKey fromWallet) throws Exception {
        byte[] serialisedTransfer = CryptoUtil.decryptWithPublicKeyRSA(encryptedTransfer, fromWallet);
        Transfer transfer = (Transfer) CryptoUtil.deserialize(serialisedTransfer);
        transfer.fromWallet = fromWallet;
        transferPool.add(transfer);
    }
}

package blockchain;

import java.security.*;
import java.util.ArrayList;

public class Blockchain {
    private ArrayList<Block> blocks; // todo consider hashset to decouple id from index
    private static Blockchain instance;
    public ArrayList<Transfer> transferPool;

    // Create the seed block, blockchain, and key generator.
    private Blockchain () {
        blocks = new ArrayList<>();
        Block block = new Block(0, 0, "0", 0, new ArrayList<>());
        blocks.add(block);
        transferPool = new ArrayList<>();
    }

    public Block getBlock (int index) {
        return blocks.get(index); // todo block.id is currently coupled to the index, consider decoupling
    }

    public void addBlock (Block blockToAdd) {
        synchronized (this) {
            int lastBlockId = blocks.get(blocks.size() - 1).id;
            if (lastBlockId == blockToAdd.id - 1) {
                blocks.add(blockToAdd);
            }
        }
    }

    public int getBlockchainSize() {
        return blocks.size();
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

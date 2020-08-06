package blockchain;

import java.util.concurrent.CountDownLatch;

public class AddBlockToChain implements Runnable {
    int id;
    String previousBlockHash;
    CountDownLatch minerPoolLatch;

    public AddBlockToChain (int id, String previousBlockHash, CountDownLatch minerPoolLatch) {
        this.id = id;
        this.previousBlockHash = previousBlockHash;
        this.minerPoolLatch = minerPoolLatch;
    }
    
    @Override
    public void run(){
        Blockchain blockchain = Blockchain.getInstance();
        Block previousBlock = blockchain.getBlock(blockchain.getBlockchainSize() - 1);
        Block newBlock = new Block(id, Thread.currentThread().getId(), previousBlockHash, previousBlock.nextBlockNumberOfZeroPrefixesRequired);
        blockchain.addBlock(newBlock);
        minerPoolLatch.countDown();
    }
}
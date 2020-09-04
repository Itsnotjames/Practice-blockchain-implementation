package blockchain;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class AddBlockToChain implements Runnable {
    int id;
    String previousBlockHash;
    CountDownLatch minerPoolLatch;
    ArrayList<Transfer> transfersSentDuringPreviousBlockCreation;

    public AddBlockToChain (int id, String previousBlockHash, CountDownLatch minerPoolLatch, ArrayList<Transfer> transfersSentDuringPreviousBlockCreation) {
        this.id = id;
        this.previousBlockHash = previousBlockHash;
        this.minerPoolLatch = minerPoolLatch;
        this.transfersSentDuringPreviousBlockCreation = transfersSentDuringPreviousBlockCreation;
    }
    
    @Override
    public void run(){
        Blockchain blockchain = Blockchain.getInstance();
        Block previousBlock = blockchain.getBlock(blockchain.getBlockchainSize() - 1);
        Block newBlock = new Block(id, Thread.currentThread().getId(), previousBlockHash, previousBlock.nextBlockNumberOfZeroPrefixesRequired, transfersSentDuringPreviousBlockCreation);
        blockchain.addBlock(newBlock);
        minerPoolLatch.countDown();
    }
}
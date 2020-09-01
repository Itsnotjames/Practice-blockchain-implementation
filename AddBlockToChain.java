package blockchain;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class AddBlockToChain implements Runnable {
    int id;
    String previousBlockHash;
    CountDownLatch minerPoolLatch;
    ArrayList<String> messagesSentDuringPreviousBlockCreation;

    public AddBlockToChain (int id, String previousBlockHash, CountDownLatch minerPoolLatch, ArrayList<String> messagesSentDuringPreviousBlockCreation) {
        this.id = id;
        this.previousBlockHash = previousBlockHash;
        this.minerPoolLatch = minerPoolLatch;
        this.messagesSentDuringPreviousBlockCreation = messagesSentDuringPreviousBlockCreation;
    }
    
    @Override
    public void run(){
        Blockchain blockchain = Blockchain.getInstance();
        Block previousBlock = blockchain.getBlock(blockchain.getBlockchainSize() - 1);
        Block newBlock = new Block(id, Thread.currentThread().getId(), previousBlockHash, previousBlock.nextBlockNumberOfZeroPrefixesRequired, messagesSentDuringPreviousBlockCreation);
        blockchain.addBlock(newBlock);
        minerPoolLatch.countDown();
    }
}
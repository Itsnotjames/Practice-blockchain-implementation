package blockchain;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class AddBlockToChain implements Runnable {
    String previousBlockHash;
    CountDownLatch minerPoolLatch;
    ArrayList<Transfer> transfersSentDuringPreviousBlockCreation;
    PublicKey minedCurrencyRecipient;
    int minerDifficulty;

    public AddBlockToChain (String previousBlockHash, int minerDifficulty, CountDownLatch minerPoolLatch, ArrayList<Transfer> transfersSentDuringPreviousBlockCreation , PublicKey minedCurrencyRecipient) {
        this.previousBlockHash = previousBlockHash;
        this.minerDifficulty = minerDifficulty;
        this.minerPoolLatch = minerPoolLatch;
        this.transfersSentDuringPreviousBlockCreation = transfersSentDuringPreviousBlockCreation;
        this.minedCurrencyRecipient = minedCurrencyRecipient;
    }
    
    @Override
    public void run(){
        Block newBlock = new Block(previousBlockHash, minerDifficulty, transfersSentDuringPreviousBlockCreation, minedCurrencyRecipient);
        Blockchain blockchain = Blockchain.getInstance();
        blockchain.addBlock(newBlock);
        minerPoolLatch.countDown();
    }
}
package blockchain;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    static Blockchain blockchain;
    static final int NUMBER_OF_MINERS = 10;

    public static void main(String[] args) {
        blockchain = Blockchain.getInstance();
        addNewBlocksToBlockchain(4);
        printBlockchain();
    }

    // Create a pool of miners to create and add blocks to the blockchain.
    // todo consider refactoring
    private static void addNewBlocksToBlockchain (int blocksToAdd) {
        for(int i = 0; i < blocksToAdd; i++){
            int blockchainSizeBeforeMining = blockchain.getBlockchainSize();
            ExecutorService minerPool = Executors.newFixedThreadPool(NUMBER_OF_MINERS);
            CountDownLatch hasThePoolCreatedABlockCountdownLatch = new CountDownLatch(1);
            int previousBlockId = blockchainSizeBeforeMining - 1;

            String previousBlockHash = blockchain.getBlock(previousBlockId).hash;

            ArrayList<Transfer> transfersSentDuringPreviousBlockCreation = blockchain.transferPool;
            blockchain.transferPool.clear();

            AddBlockToChain AddBlockToChainTask = new AddBlockToChain(blockchainSizeBeforeMining, previousBlockHash, hasThePoolCreatedABlockCountdownLatch, transfersSentDuringPreviousBlockCreation);

            // Continue mining until a new block is added.
            for (int j = 0; j < NUMBER_OF_MINERS; j++){
                minerPool.submit(() -> AddBlockToChainTask.run());
            }
            try {
                hasThePoolCreatedABlockCountdownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            minerPool.shutdownNow();
        }
    }

    private static void printBlockchain () {
        for (int i = 0; i < blockchain.getBlockchainSize(); i++){
            printBlock(blockchain.getBlock(i));
        }
    }

    private static void printBlock(Block blockToPrint)
    {
        System.out.println("Block:");
        System.out.println("Created by miner #: " + blockToPrint.minerId);
        System.out.println("Id: " + blockToPrint.id);
        System.out.println("Timestamp: " + blockToPrint.timeStamp);
        System.out.println("Magic number: " + blockToPrint.magicNumber);
        System.out.println("Hash of the previous block:");
        System.out.println(blockToPrint.previousHash);
        System.out.println("Hash of the block: ");
        System.out.println(blockToPrint.hash);
        System.out.println("Block data:" + ((blockToPrint.transfers.isEmpty()) ? " no transfers" : ""));
        for (Transfer transfer : blockToPrint.transfers) {
            System.out.println(transfer.getDescription());
        }
        System.out.println("Block was generating for " + blockToPrint.timeSpentMining + " milliseconds.");
        System.out.println(blockToPrint.numberOfZeroPrefixesRequiredChangeDescription);
        System.out.println();
    }
}

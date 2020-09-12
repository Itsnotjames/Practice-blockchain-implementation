package blockchain;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    static Blockchain blockchain;
    static final int NUMBER_OF_MINERS = 10;
    static final int MINER_DIFFICULTY = 3;

    public static void main(String[] args) {
        blockchain = Blockchain.getInstance();
        addNewBlocksToBlockchain(5);
        printBlockchain();
    }

    // Create a pool of miners to create and add blocks to the blockchain.
    // todo consider refactoring
    private static void addNewBlocksToBlockchain (int blocksToAdd) {
        ArrayList<Wallet> wallets = new ArrayList<>();
        try {
            SecureRandom rng = SecureRandom.getInstance("SHA1PRNG", "SUN");
            rng.setSeed(12345);
            wallets.add(new Wallet(rng));
            wallets.add(new Wallet(rng));
            wallets.add(new Wallet(rng));
            wallets.add(new Wallet(rng));
            wallets.add(new Wallet(rng));
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < blocksToAdd; i++){
            ExecutorService minerPool = Executors.newFixedThreadPool(NUMBER_OF_MINERS);
            CountDownLatch hasThePoolCreatedABlockCountdownLatch = new CountDownLatch(1);

            String previousBlockHash = blockchain.getLastBlock().hash;

            ArrayList<Transfer> transfersSentDuringPreviousBlockCreation = blockchain.getLastBlock().transfers;
            blockchain.transferPool.clear();

            // Continue mining until a new block is added.
            for (int j = 0; j < NUMBER_OF_MINERS; j++){
                int walletRNG = (int)(Math.random() * ((4 - 0) + 1)) + 0;
                AddBlockToChain AddBlockToChainTask = new AddBlockToChain(previousBlockHash, MINER_DIFFICULTY, hasThePoolCreatedABlockCountdownLatch, transfersSentDuringPreviousBlockCreation, wallets.get(walletRNG).publicKey);
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
        Block currentBlock = blockchain.getLastBlock();
        while (currentBlock != null) {
            printBlock(currentBlock);
            currentBlock = blockchain.getBlock(currentBlock.previousHash);
        }
    }

    private static void printBlock(Block blockToPrint)
    {
        System.out.println("Block details:");
        System.out.println("Hash of the block: ");
        System.out.println(blockToPrint.hash);
        System.out.println("Hash of the previous block:");
        System.out.println(blockToPrint.previousHash);
        System.out.println("Timestamp: " + blockToPrint.timeStamp);
        System.out.println("Magic number: " + blockToPrint.magicNumber);
        System.out.println("Block data:" + ((blockToPrint.transfers.isEmpty()) ? "\n no transfers" : ""));
        for (Transfer transfer : blockToPrint.transfers) {
            System.out.println(transfer.getDescription());
        }
        System.out.println("Block was generating for " + blockToPrint.timeSpentMining + " milliseconds.");
        System.out.println();
    }
}

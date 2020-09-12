package blockchain;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Block {
    public String hash;
    public String previousHash;
    public long timeStamp;
    public long magicNumber;
    ArrayList<Transfer> transfers;
    long timeSpentMining;

    public Block (String previousHash, int minerDifficulty, ArrayList<Transfer> transfersSentDuringPreviousBlockCreation, PublicKey minedCurrencyRecipient) {
        long miningStartTime = new Date().getTime();
        this.previousHash = previousHash;
        this.transfers = new ArrayList<>();
        if (minedCurrencyRecipient != null) {
            rewardTransaction(minedCurrencyRecipient, this.transfers);
        }
        
        do {
            this.timeStamp = new Date().getTime();
            this.magicNumber = new Random().nextLong();
            this.timeSpentMining = new Date().getTime() - miningStartTime;
            this.hash = calculateHash();
        } while (!validProofOfWork(minerDifficulty));
    }

    // Reward 100 coins to the specified wallet.
    private void rewardTransaction (PublicKey recipient, ArrayList<Transfer> transfers) {
        int rewardTransferId = 0;
        if (!transfers.isEmpty()) {
            Transfer latestTransfer = this.transfers.get(this.transfers.size() - 1);
            rewardTransferId = latestTransfer.transferId + 1;
        }

        transfers.add(new Transfer(100, recipient, rewardTransferId));
    }

    // Does the hash show the block's proof of work?
    public boolean validProofOfWork(int numberOfZeroPrefixesRequired) {
        for (int i = 0; i < numberOfZeroPrefixesRequired; i++){
            if(this.hash.charAt(i) != '0'){
                return false;
            }
        }
        return true;
    }

    // Calculate the block's hash with SHA256 based on the block's content.
    public String calculateHash () {
        StringBuilder hashInput = new StringBuilder(previousHash)
                                            .append(timeStamp)
                                            .append(magicNumber)
                                            .append(timeSpentMining);
        for (Transfer transfer : this.transfers) {
            String transferDesc = transfer.getDescription();
            hashInput.append(transferDesc);
        }
        return CryptoUtil.applySha256(hashInput.toString());
    }
}

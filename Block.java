package blockchain;

import java.util.Date;
import java.util.Random;

public class Block {
    public String hash;
    public String previousHash;
    public int id;
    public long minerId;
    public long timeStamp;
    public long magicNumber;
    int nextBlockNumberOfZeroPrefixesRequired; // Next block's mining difficulty.
    public String numberOfZeroPrefixesRequiredChangeDescription;
    public long timeSpentMining;

    public Block (int id, long minerId, String previousHash, int numberOfZeroPrefixesRequired) {
        long startDate = new Date().getTime(); // Start block mining timer.

        do {
            this.id = id;
            this.minerId = minerId;
            this.previousHash = previousHash;
            this.timeStamp = new Date().getTime();
            this.magicNumber = new Random().nextLong();
            this.hash = calculateHash();
        } while (!validProofOfWork(numberOfZeroPrefixesRequired));

        long endDate = new Date().getTime();
        this.timeSpentMining = endDate - startDate; // The time spent mining this block.

        refineMinerDifficulty(timeSpentMining, numberOfZeroPrefixesRequired);
    }

    // Refine the next block's mining difficulty (nextBlockNumberOfZeroPrefixesRequired) based on the the time taken to mine the current block.
    private void refineMinerDifficulty(long timeSpentMining, int numberOfZeroPrefixesRequired) {
        if (timeSpentMining < 15) {
            nextBlockNumberOfZeroPrefixesRequired = numberOfZeroPrefixesRequired + 1;
            numberOfZeroPrefixesRequiredChangeDescription = "N was increased to " + nextBlockNumberOfZeroPrefixesRequired;
        } else if (timeSpentMining > 60) {
            nextBlockNumberOfZeroPrefixesRequired = numberOfZeroPrefixesRequired - 1;
            numberOfZeroPrefixesRequiredChangeDescription = "N was decreased by 1";
        } else {
            nextBlockNumberOfZeroPrefixesRequired = numberOfZeroPrefixesRequired;
            numberOfZeroPrefixesRequiredChangeDescription = "N stays the same";
        }
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
        return StringUtil.applySha256(
                this.previousHash +
                        this.timeStamp +
                        this.id +
                        this.magicNumber
        );
    }
}

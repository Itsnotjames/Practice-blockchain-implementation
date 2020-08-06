package blockchain;

import java.util.ArrayList;

public class Blockchain {
    private ArrayList<Block> blocks;
    private static Blockchain instance;

    // Create the seed block and blockchain.
    private Blockchain () {
        blocks = new ArrayList<>();
        Block block = new Block(0, 0, "0", 0);
        blocks.add(block);
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
}

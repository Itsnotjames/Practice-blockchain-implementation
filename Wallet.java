package blockchain;

import java.security.*;

public class Wallet {
    private KeyPairGenerator keyPairGenerator;
    public PublicKey publicKey;
    private PrivateKey privateKey;


    public Wallet () throws NoSuchAlgorithmException {
        int keyLength = 10;
        keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keyLength);

        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();
    }

    public void requestFundTransfer (double funds, PublicKey toWallet, Blockchain blockchain) throws Exception {
        Transfer transfer = new Transfer(funds, toWallet);
        byte[] serialisedTransfer = CryptoUtil.serialize(transfer);
        byte[] encryptedTransfer = CryptoUtil.encryptWithPrivateKeyRSA(serialisedTransfer, privateKey);

        blockchain.addTransfer(encryptedTransfer, publicKey);
    }
}

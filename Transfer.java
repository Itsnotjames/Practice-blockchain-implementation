package blockchain;

import java.security.PublicKey;
import java.util.Date;

public class Transfer {
    int transferId;
    long timeStamp;
    double funds;
    PublicKey fromWallet;
    String fromWalletCode;
    PublicKey toWallet;
    String toWalletCode;

    Transfer (double funds, PublicKey toWallet, int transferId) {
        this.timeStamp = new Date().getTime();
        this.funds = funds;
        this.toWallet = toWallet;
        if (fromWallet == null) {
            fromWalletCode = "Block Creation";
        } else {
            byte[] theBytes = fromWallet.getEncoded();
            fromWalletCode = new String(theBytes);
        }
        byte[] theBytes = toWallet.getEncoded();
        toWalletCode = new String(theBytes);
        this.transferId = transferId;
    }

    String getDescription() {
        return fromWalletCode + " sent " + funds + "GP to " + toWalletCode;
    }
}

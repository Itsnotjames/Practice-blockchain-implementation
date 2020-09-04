package blockchain;

import java.security.PublicKey;
import java.util.Date;

public class Transfer {
    long timeStamp;
    double funds;
    PublicKey fromWallet;
    PublicKey toWallet;

    Transfer (double funds, PublicKey toWallet) {
        this.timeStamp = new Date().getTime();
        this.funds = funds;
        this.toWallet = toWallet;
    }

    String getDescription() {
        return fromWallet.getFormat() + "sent" + funds + "GP to" + toWallet;
    }
}

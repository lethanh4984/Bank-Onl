package ASM4.model;

import java.io.IOException;

public interface ITransfer {
    double transfers(Account receiveAccount, double amount) throws IOException;
}

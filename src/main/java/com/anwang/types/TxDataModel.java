package com.anwang.types;

import com.anwang.contracts.ProxyAdmin;
import com.anwang.utils.CommonUtil;
import com.anwang.utils.TimeUtil;
import org.web3j.abi.datatypes.Address;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.List;

public class TxDataModel {
    public BigInteger txid;
    public Address from;
    public Address to;
    public BigInteger value;
    public String function;
    public BigInteger timestamp;
    public Address executor;
    public Boolean executed;

    public String fromName;
    public String toName;
    public String executorName;
    public String executeDate;
    public Boolean isConfirmed;
    public BigInteger confirmCount;
    public List<Address> confirmations;
    public String submitTxid;
    public String executeTxid;

    public TxDataModel(BigInteger txid, MultiSigTx tx, String submitTxid, Boolean isConfirmed, BigInteger confirmCount, List<Address> confirmations) {
        this.txid = txid;
        this.from = tx.from;
        if (tx.to.getValue().equals(ProxyAdmin.contractAddr.getValue())) {
            byte[] temp = new byte[20];
            System.arraycopy(tx.data, 16, temp, 0, 20);
            String to = Numeric.toHexString(temp);
            this.to = new Address(to);
        } else {
            this.to = tx.to;
        }
        this.value = tx.value;
        this.function = CommonUtil.getFunctionName(tx.data);
        this.timestamp = tx.timestamp;
        this.executor = tx.executor;
        this.executed = tx.executed;

        this.fromName = CommonUtil.getOpertor(this.from.getValue());
        this.toName = CommonUtil.getContractName(this.to.getValue());
        this.executorName = CommonUtil.getOpertor(this.executor.getValue());
        this.executeDate = TimeUtil.toDate(this.timestamp.longValue());
        this.isConfirmed = isConfirmed;
        this.confirmCount = confirmCount;
        this.confirmations = confirmations;
        this.submitTxid = submitTxid;
    }

    @Override
    public String toString() {
        return "TxModel{" +
                "txid=" + txid +
                ", from=" + from +
                ", to=" + to +
                ", value=" + value +
                ", function=" + function +
                ", timestamp=" + timestamp +
                ", executor=" + executor +
                ", executed=" + executed +
                ", fromName=" + fromName +
                ", toName=" + toName +
                ", executorName=" + executorName +
                ", executeDate=" + executeDate +
                ", isConfirmed=" + isConfirmed +
                ", confirmCount=" + confirmCount +
                '}';
    }
}

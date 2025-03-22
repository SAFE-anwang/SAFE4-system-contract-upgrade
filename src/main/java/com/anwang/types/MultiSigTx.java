package com.anwang.types;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicBytes;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.generated.Uint256;

import java.math.BigInteger;

public class MultiSigTx extends DynamicStruct {
    public Address from;
    public Address to;
    public BigInteger value;
    public byte[] data;
    public BigInteger timestamp;
    public Address executor;
    public Boolean executed;

    public MultiSigTx(Address from, Address to, BigInteger value, byte[] data, BigInteger timestamp, Address executor, Boolean executed) {
        super(from, to, new Uint256(value), new DynamicBytes(data), new Uint256(timestamp), executor, new Bool(executed));
        this.from = from;
        this.to = to;
        this.value = value;
        this.data = data;
        this.timestamp = timestamp;
        this.executor = executor;
        this.executed = executed;
    }

    public MultiSigTx(Address from, Address to, Uint256 value, DynamicBytes data, Uint256 timestamp, Address executor, Bool executed) {
        super(from, to, value, data, timestamp, executor, executed);
        this.from = from;
        this.to = to;
        this.value = value.getValue();
        this.data = data.getValue();
        this.timestamp = timestamp.getValue();
        this.executor = executor;
        this.executed = executed.getValue();
    }

    @Override
    public String toString() {
        return "MultiSigTx{" +
                "from=" + from +
                ", to=" + to +
                ", value=" + value +
                ", data=" + data +
                ", timestamp=" + timestamp +
                ", executor=" + executor +
                ", executed=" + executed +
                '}';
    }
}

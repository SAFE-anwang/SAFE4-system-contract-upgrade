package com.anwang.contracts;

import com.anwang.types.MultiSigTx;
import com.anwang.utils.CommonUtil;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MultiSig {
    private final ContractUtil contractUtil;
    public static Address contractAddr;

    public MultiSig(Web3j web3j, long chainId) {
        contractUtil = new ContractUtil(web3j, chainId, contractAddr.getValue());
    }

    public String submitTransaction(String privateKey, Address to, BigInteger value, byte[] data, long timestamp) throws Exception {
        Function function = new Function("submitTransaction", Arrays.asList(to, new Uint256(value), new DynamicBytes(data), new Uint256(timestamp)), Collections.singletonList(new TypeReference<Uint256>() {
        }));
        return contractUtil.call(privateKey, value, function);
    }

    public String confirmTransaction(String privateKey, BigInteger txid) throws Exception {
        Function function = new Function("confirmTransaction", Collections.singletonList(new Uint256(txid)), Collections.emptyList());
        return contractUtil.call(privateKey, function);
    }

    public String revokeConfirmation(String privateKey, BigInteger txid) throws Exception {
        Function function = new Function("revokeConfirmation", Collections.singletonList(new Uint256(txid)), Collections.emptyList());
        return contractUtil.call(privateKey, function);
    }

    public String executeTransaction(String privateKey, BigInteger txid) throws Exception {
        Function function = new Function("executeTransaction", Collections.singletonList(new Uint256(txid)), Collections.emptyList());
        return contractUtil.call(privateKey, function);
    }

    public BigInteger getRequired() throws Exception {
        Function function = new Function("getRequired", Collections.emptyList(), Collections.singletonList(new TypeReference<Uint256>() {
        }));
        List<Type> someTypes = contractUtil.query(function);
        return ((Uint256) someTypes.get(0)).getValue();
    }

    public List<Address> getOwners() throws Exception {
        Function function = new Function("getOwners", Collections.emptyList(), Collections.singletonList(new TypeReference<DynamicArray<Address>>() {
        }));
        List<Type> someTypes = contractUtil.query(function);
        return ((DynamicArray<Address>) someTypes.get(0)).getValue();
    }

    public Boolean existOwner(Address addr) throws Exception {
        Function function = new Function("existOwner", Collections.singletonList(addr), Collections.singletonList(new TypeReference<Bool>() {
        }));
        List<Type> someTypes = contractUtil.query(function);
        return ((Bool) someTypes.get(0)).getValue();
    }

    public BigInteger getAllTransactionCount() throws Exception {
        Function function = new Function("getAllTransactionCount", Collections.emptyList(), Collections.singletonList(new TypeReference<Uint256>() {
        }));
        List<Type> someTypes = contractUtil.query(function);
        return ((Uint256) someTypes.get(0)).getValue();
    }

    public BigInteger getTransactionCount(boolean pending, boolean executed) throws Exception {
        Function function = new Function("getTransactionCount", Arrays.asList(new Bool(pending), new Bool(executed)), Collections.singletonList(new TypeReference<Uint256>() {
        }));
        List<Type> someTypes = contractUtil.query(function);
        return ((Uint256) someTypes.get(0)).getValue();
    }

    public List<BigInteger> getTransactionIds(BigInteger start, BigInteger end, boolean pending, boolean executed) throws Exception {
        Function function = new Function("getTransactionIds", Arrays.asList(new Uint256(start), new Uint256(end), new Bool(pending), new Bool(executed)), Collections.singletonList(new TypeReference<DynamicArray<Uint256>>() {
        }));
        List<Type> someTypes = contractUtil.query(function);
        return ((DynamicArray<Uint256>) someTypes.get(0)).getValue().stream().map(v -> v.getValue()).collect(Collectors.toList());
    }

    public MultiSigTx getTransaction(BigInteger txid) throws Exception {
        Function function = new Function("getTransaction", Collections.singletonList(new Uint256(txid)), Collections.singletonList(new TypeReference<MultiSigTx>() {
        }));
        List<Type> someTypes = contractUtil.query(function);
        return (MultiSigTx) someTypes.get(0);
    }

    public BigInteger getConfirmationCount(BigInteger txid) throws Exception {
        Function function = new Function("getConfirmationCount", Collections.singletonList(new Uint256(txid)), Collections.singletonList(new TypeReference<Uint256>() {
        }));
        List<Type> someTypes = contractUtil.query(function);
        return ((Uint256) someTypes.get(0)).getValue();
    }

    public List<Address> getConfirmations(BigInteger txid) throws Exception {
        Function function = new Function("getConfirmations", Collections.singletonList(new Uint256(txid)), Collections.singletonList(new TypeReference<DynamicArray<Address>>() {
        }));
        List<Type> someTypes = contractUtil.query(function);
        return ((DynamicArray<Address>) someTypes.get(0)).getValue();
    }

    public Boolean isCanExecute(BigInteger txid) throws Exception {
        Function function = new Function("isCanExecute", Collections.singletonList(new Uint256(txid)), Collections.singletonList(new TypeReference<Bool>() {
        }));
        List<Type> someTypes = contractUtil.query(function);
        return ((Bool) someTypes.get(0)).getValue();
    }

    public Boolean isExecuted(BigInteger txid) throws Exception {
        Function function = new Function("isExecuted", Collections.singletonList(new Uint256(txid)), Collections.singletonList(new TypeReference<Bool>() {
        }));
        List<Type> someTypes = contractUtil.query(function);
        return ((Bool) someTypes.get(0)).getValue();
    }

    public Boolean isConfirmed(BigInteger txid) throws Exception {
        Function function = new Function("isConfirmed", Collections.singletonList(new Uint256(txid)), Collections.singletonList(new TypeReference<Bool>() {
        }));
        List<Type> someTypes = contractUtil.query(function);
        return ((Bool) someTypes.get(0)).getValue();
    }

    public static byte[] getAddOwnerData(Address owner) throws Exception {
        Function function = new Function("AddOwner", Collections.singletonList(owner), Collections.emptyList());
        return Numeric.hexStringToByteArray(FunctionEncoder.encode(function));
    }

    public static byte[] getRemoveOwnerData(Address owner) throws Exception {
        Function function = new Function("removeOwner", Collections.singletonList(owner), Collections.emptyList());
        return Numeric.hexStringToByteArray(FunctionEncoder.encode(function));
    }

    public static byte[] getReplaceOwner(Address owner, Address newOwner) throws Exception {
        Function function = new Function("replaceOwner", Arrays.asList(owner, newOwner), Collections.emptyList());
        return Numeric.hexStringToByteArray(FunctionEncoder.encode(function));
    }

    public static byte[] getChangeRequirement(long required) throws Exception {
        Function function = new Function("changeRequirement", Collections.singletonList(new Uint256(required)), Collections.emptyList());
        return Numeric.hexStringToByteArray(FunctionEncoder.encode(function));
    }

    public String addOwner(String privateKey, String owner, long timestamp) throws Exception {
        byte[] data = MultiSig.getAddOwnerData(new Address(owner));
        return submitTransaction(privateKey, MultiSig.contractAddr, BigInteger.ZERO, data, timestamp);
    }

    public String removeOwner(String privateKey, String owner, long timestamp) throws Exception {
        byte[] data = MultiSig.getRemoveOwnerData(new Address(owner));
        return submitTransaction(privateKey, MultiSig.contractAddr, BigInteger.ZERO, data, timestamp);
    }

    public String replaceOwner(String privateKey, String owner, String newOwner, long timestamp) throws Exception {
        byte[] data = MultiSig.getReplaceOwner(new Address(owner), new Address(newOwner));
        return submitTransaction(privateKey, MultiSig.contractAddr, BigInteger.ZERO, data, timestamp);
    }

    public String changeRequirement(String privateKey, long require, long timestamp) throws Exception {
        byte[] data = MultiSig.getChangeRequirement(require);
        return submitTransaction(privateKey, MultiSig.contractAddr, BigInteger.ZERO, data, timestamp);
    }

    public String upgradeContract(String privateKey, String contractName, long timestamp) throws Exception {
        return upgradeContract(privateKey, contractName, CommonUtil.getBin(contractName), timestamp);
    }

    public String upgradeContract(String privateKey, String contractName, String contractBin, long timestamp) throws Exception {
        String newImplementation = contractUtil.deploy(privateKey, contractBin);
        Function function = new Function("upgrade", Arrays.asList(CommonUtil.getProxy(contractName), new Address(newImplementation)), Collections.emptyList());
        byte[] data = Numeric.hexStringToByteArray(FunctionEncoder.encode(function));
        return submitTransaction(privateKey, ProxyAdmin.contractAddr, BigInteger.ZERO, data, timestamp);
    }

    public String addProperty(String privateKey, String name, BigInteger value, String description, long timestamp) throws Exception {
        Function function = new Function("add", Arrays.asList(new Utf8String(name), new Uint256(value), new Utf8String(description)), Collections.emptyList());
        byte[] data = Numeric.hexStringToByteArray(FunctionEncoder.encode(function));
        return submitTransaction(privateKey, CommonUtil.getProxy("Property"), BigInteger.ZERO, data, timestamp);
    }

    public String changeIsOfficial4MN(String privateKey, String addr, boolean flag, long timestamp) throws Exception {
        Function function = new Function("changeIsOfficial", Arrays.asList(new Address(addr), new Bool(flag)), Collections.emptyList());
        byte[] data = Numeric.hexStringToByteArray(FunctionEncoder.encode(function));
        return submitTransaction(privateKey, CommonUtil.getProxy("MasterNodeLogic"), BigInteger.ZERO, data, timestamp);
    }

    public String changeIsOfficial4SN(String privateKey, String addr, boolean flag, long timestamp) throws Exception {
        Function function = new Function("changeIsOfficial", Arrays.asList(new Address(addr), new Bool(flag)), Collections.emptyList());
        byte[] data = Numeric.hexStringToByteArray(FunctionEncoder.encode(function));
        return submitTransaction(privateKey, CommonUtil.getProxy("SuperNodeLogic"), BigInteger.ZERO, data, timestamp);
    }
}

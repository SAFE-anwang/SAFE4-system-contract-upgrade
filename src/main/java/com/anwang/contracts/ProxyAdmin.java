package com.anwang.contracts;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.Web3j;
import org.web3j.utils.Numeric;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProxyAdmin {

    private final ContractUtil contractUtil;
    public static Address contractAddr = new Address("0x0000000000000000000000000000000000000999");

    public ProxyAdmin(Web3j web3j, long chainId) {
        contractUtil = new ContractUtil(web3j, chainId, contractAddr.getValue());
    }

    public Address getProxyImplementation(Address proxy) throws Exception {
        Function function = new Function("getProxyImplementation", Collections.singletonList(proxy), Collections.singletonList(new TypeReference<Address>() {
        }));
        List<Type> someTypes = contractUtil.query(function);
        return (Address) someTypes.get(0);
    }
}

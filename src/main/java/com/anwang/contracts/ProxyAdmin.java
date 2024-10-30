package com.anwang.contracts;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.protocol.Web3j;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProxyAdmin {

    private final ContractUtil contractUtil;

    public ProxyAdmin(Web3j web3j, long chainId) {
        contractUtil = new ContractUtil(web3j, chainId, "0x0000000000000000000000000000000000000999");
    }

    public String upgrade(String privateKey, Address proxy, Address implementation) throws Exception {
        Function function = new Function("upgrade", Arrays.asList(proxy, implementation), Collections.emptyList());
        return contractUtil.call(privateKey, function);
    }

    public Address getProxyImplementation(Address proxy) throws Exception {
        Function function = new Function("getProxyImplementation", Collections.singletonList(proxy), Collections.singletonList(new TypeReference<Address>() {
        }));
        List<Type> someTypes = contractUtil.query(function);
        return (Address) someTypes.get(0);
    }
}

package com.anwang;

import com.anwang.contracts.ContractUtil;
import com.anwang.utils.CommonUtil;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.util.Collections;

public class Implementation {
    public static void main(String[] args) throws Exception {
        String[] names = {
                "Property",
                "AccountManager",
                "MasterNodeLogic",
                "MasterNodeStorage",
                "SuperNodeLogic",
                "SuperNodeStorage",
                "SNVote",
                "MasterNodeState",
                "SuperNodeState",
                "Proposal",
                "SystemReward",
                "Safe3",
                "ProxyAdmin"
        };

        Web3j newWeb3j = Web3j.build(new HttpService("http://127.0.0.1:8545"));
        long chainId = 6666666;
        Function function = new Function("owner", Collections.emptyList(), Collections.singletonList(new TypeReference<Address>() {
        }));
        for (String name : names) {
            ContractUtil contractUtil = new ContractUtil(newWeb3j, chainId, CommonUtil.getProxy(name).getValue());
            System.out.println(name + " owner: " + contractUtil.query(function).get(0));
        }
    }
}

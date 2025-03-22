package com.anwang.contracts;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.utils.Numeric;

import java.util.Arrays;
import java.util.Collections;

public class MasterNode {
    public static byte[] getChangeIsOfficialData(Address addr, boolean flag) {
        Function function = new Function("changeIsOfficial", Arrays.asList(addr, new Bool(flag)), Collections.emptyList());
        return Numeric.hexStringToByteArray(FunctionEncoder.encode(function));
    }
}

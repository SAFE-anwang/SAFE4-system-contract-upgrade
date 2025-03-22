package com.anwang.contracts;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

public class SysProperty {
    public static byte[] getAddData(String name, BigInteger value, String description) {
        Function function = new Function("add", Arrays.asList(new Utf8String(name), new Uint256(value), new Utf8String(description)), Collections.emptyList());
        return Numeric.hexStringToByteArray(FunctionEncoder.encode(function));
    }
}

package com.anwang;

import com.anwang.contracts.ContractUtil;
import com.anwang.contracts.ProxyAdmin;
import org.web3j.abi.datatypes.Address;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class TestnetUpgrade {
    private final String ownerKey;
    private final long chainId;
    private final Web3j web3j;
    private final ContractUtil contractUtil;
    private final ProxyAdmin proxyAdmin;

    public TestnetUpgrade(String ownerKey) {
        this.ownerKey = ownerKey;
        this.chainId = 6666666;
        this.web3j = Web3j.build(new HttpService("http://127.0.0.1:8545"));
        this.contractUtil = new ContractUtil(this.web3j, this.chainId, "");
        this.proxyAdmin = new ProxyAdmin(this.web3j, this.chainId);
    }

    public void compile(String contractName) {
        System.out.println("start compile contract: " + contractName);
        Path workPath = Paths.get(System.getProperty("user.dir"));
        Path contractPath = workPath.resolve("deps/SAFE4-system-contract-testnet/" + contractName + ".sol");
        Path solcPath = workPath.resolve("deps/solc.exe");
        Path outPath = workPath.resolve("build/testnet/" + contractName);
        String cmd = String.format("%s --base-path %s --optimize --optimize-runs 200 --bin -o %s openzeppelin-contracts-upgradeable/=3rd/OpenZeppelin/openzeppelin-contracts-upgradeable/contracts/ openzeppelin-contracts/=3rd/OpenZeppelin/openzeppelin-contracts/contracts/ --overwrite %s", solcPath, contractPath.getParent().toString(), outPath, contractPath);
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            process.waitFor();
            if (process.exitValue() != 0) {
                String s = null;
                StringBuilder errMsg = new StringBuilder();
                while ((s = stdError.readLine()) != null) {
                    errMsg.append(s + "\n");
                }
                throw new Exception(errMsg.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public String getBin(String contractName) throws Exception {
        Path workPath = Paths.get(System.getProperty("user.dir"));
        Path binPath = workPath.resolve("build/testnet/" + contractName + "/" + contractName + ".bin");
        Scanner scanner = new Scanner(binPath.toFile());
        String bin = "";
        while (scanner.hasNextLine()) {
            bin += scanner.nextLine();
        }
        scanner.close();
        return bin;
    }

    public String getProxy(String contractName) throws Exception {
        switch (contractName) {
            case "Property":
                return "0x0000000000000000000000000000000000001000";
            case "AccountManager":
                return "0x0000000000000000000000000000000000001010";
            case "MasterNodeStorage":
                return "0x0000000000000000000000000000000000001020";
            case "MasterNodeLogic":
                return "0x0000000000000000000000000000000000001025";
            case "SuperNodeStorage":
                return "0x0000000000000000000000000000000000001030";
            case "SuperNodeLogic":
                return "0x0000000000000000000000000000000000001035";
            case "SNVote":
                return "0x0000000000000000000000000000000000001040";
            case "MasterNodeState":
                return "0x0000000000000000000000000000000000001050";
            case "SuperNodeState":
                return "0x0000000000000000000000000000000000001060";
            case "Proposal":
                return "0x0000000000000000000000000000000000001070";
            case "SystemReward":
                return "0x0000000000000000000000000000000000001080";
            case "Safe3":
                return "0x0000000000000000000000000000000000001090";
            default:
                throw new Exception("Unsupported contract");
        }
    }

    public void deploy(String contractName) {
        try {
            String proxy = getProxy(contractName);
            String oldImplementation = proxyAdmin.getProxyImplementation(new Address(proxy)).toString();
            String newImplementation = contractUtil.deploy(this.ownerKey, getBin(contractName));
            doUpgrade(proxy, newImplementation);
            System.out.println(String.format("%s: change implementation, old: %s, new: %s", contractName, oldImplementation, newImplementation));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String doUpgrade(String proxyAddr, String implementation) {
        try {
            return proxyAdmin.upgrade(this.ownerKey, new Address(proxyAddr), new Address(implementation));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("please specify contract name");
            return;
        }

        // please change ownerKey before upgrade
        String ownerKey = "0xe2f288bd39ee0aaa7225c0b043e6aff5868cdfc0934eab4034a7a37c0d0581e0";
        TestnetUpgrade upgrade = new TestnetUpgrade(ownerKey);
        for (String contractName : args) {
            upgrade.compile(contractName);
            upgrade.deploy(contractName);
        }
    }
}

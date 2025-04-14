package com.anwang.utils;

import com.anwang.contracts.ProxyAdmin;
import org.web3j.abi.datatypes.Address;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class CommonUtil {
    public static void compile(String contractName) throws Exception {
        Path workPath = Paths.get(System.getProperty("user.dir"));
        Path contractPath = workPath.resolve("deps/SAFE4-system-contract/" + contractName + ".sol");
        Path solcPath = workPath.resolve("deps/solc.exe");
        Path outPath = workPath.resolve("build/mainnet/" + contractName);
        String cmd = String.format("%s --base-path %s --optimize --optimize-runs 200 --bin -o %s openzeppelin-contracts-upgradeable/=3rd/OpenZeppelin/openzeppelin-contracts-upgradeable/contracts/ openzeppelin-contracts/=3rd/OpenZeppelin/openzeppelin-contracts/contracts/ --overwrite %s", solcPath, contractPath.getParent().toString(), outPath, contractPath);
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
    }

    public static String getBin(String contractName) throws Exception {
        Path workPath = Paths.get(System.getProperty("user.dir"));
        Path binPath = workPath.resolve("build/mainnet/" + contractName + "/" + contractName + ".bin");
        Scanner scanner = new Scanner(binPath.toFile());
        String bin = "";
        while (scanner.hasNextLine()) {
            bin += scanner.nextLine();
        }
        scanner.close();
        return bin;
    }

    public static Address getProxy(String contractName) throws Exception {
        switch (contractName) {
            case "ProxyAdmin":
                return ProxyAdmin.contractAddr;
            case "Property":
                return new Address("0x0000000000000000000000000000000000001000");
            case "AccountManager":
                return new Address("0x0000000000000000000000000000000000001010");
            case "MasterNodeStorage":
                return new Address("0x0000000000000000000000000000000000001020");
            case "MasterNodeLogic":
                return new Address("0x0000000000000000000000000000000000001025");
            case "SuperNodeStorage":
                return new Address("0x0000000000000000000000000000000000001030");
            case "SuperNodeLogic":
                return new Address("0x0000000000000000000000000000000000001035");
            case "SNVote":
                return new Address("0x0000000000000000000000000000000000001040");
            case "MasterNodeState":
                return new Address("0x0000000000000000000000000000000000001050");
            case "SuperNodeState":
                return new Address("0x0000000000000000000000000000000000001060");
            case "Proposal":
                return new Address("0x0000000000000000000000000000000000001070");
            case "SystemReward":
                return new Address("0x0000000000000000000000000000000000001080");
            case "Safe3":
                return new Address("0x0000000000000000000000000000000000001090");
            default:
                throw new Exception("Unsupported contract");
        }
    }

    public static String getContractName(String contractAddr) {
        switch (contractAddr) {
            case "0x0000000000000000000000000000000000000999":
                return "ProxyAdmin合约";
            case "0x0000000000000000000000000000000000001000":
                return "系统参数合约";
            case "0x0000000000000000000000000000000000001010":
                return "账户管理合约";
            case "0x0000000000000000000000000000000000001020":
                return "主节点存储合约";
            case "0x0000000000000000000000000000000000001025":
                return "主节点逻辑合约";
            case "0x0000000000000000000000000000000000001030":
                return "超级节点存储合约";
            case "0x0000000000000000000000000000000000001035":
                return "超级节点逻辑合约";
            case "0x0000000000000000000000000000000000001040":
                return "超级节点投票合约";
            case "0x0000000000000000000000000000000000001050":
                return "主节点状态合约";
            case "0x0000000000000000000000000000000000001060":
                return "超级节点状态合约";
            case "0x0000000000000000000000000000000000001070":
                return "提案合约";
            case "0x0000000000000000000000000000000000001080":
                return "系统奖励合约";
            case "0x0000000000000000000000000000000000001090":
                return "Safe3迁移合约";
            case "0x190FAc606daA4d28d4a7B4d5B3aaD348bD5Aa25C":
            case "0x0000000000000000000000000000000000001102":
                return "多签合约";
            default:
                return "未知";
        }
    }

    public static String getFunctionName(byte[] data) {
        String functionSignature = "add(string,uint256,string)";
        byte[] hash = Hash.sha3(functionSignature.getBytes());
        byte[] selector = new byte[4];
        System.arraycopy(hash, 0, selector, 0, 4);
        String addHex = Numeric.toHexString(selector);

        functionSignature = "changeIsOfficial(address,bool)";
        hash = Hash.sha3(functionSignature.getBytes());
        System.arraycopy(hash, 0, selector, 0, 4);
        String changeIsOfficialHex = Numeric.toHexString(selector);

        functionSignature = "upgrade(address,address)";
        hash = Hash.sha3(functionSignature.getBytes());
        System.arraycopy(hash, 0, selector, 0, 4);
        String upgradeHex = Numeric.toHexString(selector);

        functionSignature = "addOwner(address)";
        hash = Hash.sha3(functionSignature.getBytes());
        System.arraycopy(hash, 0, selector, 0, 4);
        String addOwnerHex = Numeric.toHexString(selector);

        functionSignature = "removeOwner(address)";
        hash = Hash.sha3(functionSignature.getBytes());
        System.arraycopy(hash, 0, selector, 0, 4);
        String removeOwnerHex = Numeric.toHexString(selector);

        functionSignature = "replaceOwner(address,address)";
        hash = Hash.sha3(functionSignature.getBytes());
        System.arraycopy(hash, 0, selector, 0, 4);
        String replaceOwnerHex = Numeric.toHexString(selector);

        functionSignature = "changeRequirement(uint256)";
        hash = Hash.sha3(functionSignature.getBytes());
        System.arraycopy(hash, 0, selector, 0, 4);
        String changeRequirementHex = Numeric.toHexString(selector);

        functionSignature = "transferOwnership(address)";
        hash = Hash.sha3(functionSignature.getBytes());
        selector = new byte[4];
        System.arraycopy(hash, 0, selector, 0, 4);
        String transferOwnershipHex = Numeric.toHexString(selector);

        System.arraycopy(data, 0, selector, 0, 4);
        String srcHex = Numeric.toHexString(selector);
        if (srcHex.equals(addHex)) {
            return "新增参数";
        }
        if (srcHex.equals(changeIsOfficialHex)) {
            return "配置官方节点";
        }
        if (srcHex.equals(upgradeHex)) {
            return "升级";
        }
        if (srcHex.equals(addOwnerHex)) {
            return "新增管理员";
        }
        if (srcHex.equals(removeOwnerHex)) {
            return "移除管理员";
        }
        if (srcHex.equals(replaceOwnerHex)) {
            return "替换管理员";
        }
        if (srcHex.equals(changeRequirementHex)) {
            return "更改签名数";
        }
        if (srcHex.equals(transferOwnershipHex)) {
            return "更换合约所有者";
        }
        return "未知";
    }

    public static String getEventName(String topic) {
        String hash;
        hash = Hash.sha3String("Confirmation(address,uint256)");
        if (topic.equals(hash)) {
            return "Confirmation";
        }

        hash = Hash.sha3String("Revocation(address,uint256)");
        if (topic.equals(hash)) {
            return "Revocation";
        }

        hash = Hash.sha3String("Submission(address,uint256)");
        if (topic.equals(hash)) {
            return "Submission";
        }

        hash = Hash.sha3String("Execution(address,uint256)");
        if (topic.equals(hash)) {
            return "Execution";
        }

        hash = Hash.sha3String("RequirementChange(uint256)");
        if (topic.equals(hash)) {
            return "RequirementChange";
        }
        return "";
    }

    public static String getOpertor(String owner) {
        switch (owner) {
            case "0x345ec0f7cbc5ec4f93382ef4a6c7af4ee4c52e03":
                return "乐猛彬";
            case "0x0f4f7cdc5baf347ab82a891ed16ba086cbaf5120":
                return "舒琪";
            case "0x5d49a4e9c448e8d8e4d1bb4d1516182de47e9053":
                return "黄鑫鑫";
            case "0xa5481ced0ebaabbbf805c2740b6b08b00fb5913e":
            case "0x11bd05d16f13df64b67b774d07fcdbe38537bd56":
                return "申屠";
            default:
                return "未知";
        }
    }
}

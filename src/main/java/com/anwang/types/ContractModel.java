package com.anwang.types;

import com.anwang.contracts.MultiSig;
import org.web3j.abi.datatypes.Address;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ContractModel {
    private static volatile ContractModel instance;
    private long chainId;
    private String url;
    private Web3j web3j;
    private MultiSig multiSig;
    private final PropertyChangeSupport support;

    private ContractModel() {
        support = new PropertyChangeSupport(this);
    }

    public static ContractModel getInstance() {
        if (instance == null) {
            synchronized (ContractModel.class) {
                if (instance == null) {
                    instance = new ContractModel();
                }
            }
        }
        return instance;
    }

    public void addListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public String init(long chainId, String url) {
        Web3j newWeb3j = Web3j.build(new HttpService(url));
        try {
            Web3ClientVersion clientVersion = newWeb3j.web3ClientVersion().send();
            if (clientVersion.hasError()) {
                return clientVersion.getError().getMessage();
            }
        } catch (Exception e) {
            return e.getMessage();
        }

        this.chainId = chainId;
        this.url = url;
        this.web3j = newWeb3j;
        if (chainId == 6666666) {
            MultiSig.contractAddr = new Address("0x62b31F0eCFF7e26786593118eC25DAD8C9BCB161");
        } else {
            MultiSig.contractAddr = new Address("0x0000000000000000000000000000000000001102");
        }
        this.multiSig = new MultiSig(web3j, chainId);
        return null;
    }

    public String update(long chainId, String url) {
        if (this.chainId == chainId && this.url.equals(url)) {
            return null;
        }
        long oldChainId = this.chainId;
        String ret = init(chainId, url);
        if (ret == null) {
            support.firePropertyChange("chainId", oldChainId, chainId);
        }
        return ret;
    }

    public long getChainId() {
        return chainId;
    }

    public String getUrl() {
        return url;
    }

    public Web3j getWeb3j() {
        return web3j;
    }

    public MultiSig getMultiSig() {
        return multiSig;
    }

    public String getChainType() {
        if (chainId == 6666666) {
            return "测试网";
        }
        return "主网";
    }
}

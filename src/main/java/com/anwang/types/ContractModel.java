package com.anwang.types;

import com.anwang.contracts.MultiSig;
import org.web3j.protocol.Web3j;
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
        chainId = 6666666;
        url = "http://139.162.40.90:8545";
        web3j = Web3j.build(new HttpService(url));
        multiSig = new MultiSig(web3j, chainId);
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

    public void update(long chainId, String url) {
        if (this.chainId == chainId && this.url.equals(url)) {
            return;
        }
        long oldChainId = this.chainId;
        this.chainId = chainId;
        this.url = url;
        this.web3j = Web3j.build(new HttpService(url));
        this.multiSig = new MultiSig(web3j, chainId);
        support.firePropertyChange("chainId", oldChainId, chainId);
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
        if (chainId == 6666665) {
            return "主网";
        }
        return "测试网";
    }
}

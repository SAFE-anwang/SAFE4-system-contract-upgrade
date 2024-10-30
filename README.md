# SAFE4-system-contract-upgrade
Auto-upgrade safe4 system contracts, just for windows

Runtime: `JDK1.8`

Run `com.anwang.MainnetUpgrade` for auto-upgrade system-contract of `SAFE4-mainnet`

Run `com.anwang.MainnetUpgrade` for auto-upgrade system-contract of `SAFE4-testnet`

Allow auto-upgrade `Property AccountManager MasterNodeStorage MasterNodeLogic SuperNodeStorage SuperNodeLogic SNVote MasterNodeState SuperNodeState Proposal SystemReward Safe3` contract

Parameters must be contract name above, can be one contract, also can be multiple contract names(split by space), for example:

`MainnetUpgrade.exe Property`

or

`MainnetUpgrade.exe Property AccountManager Safe3`

Please change owner private key before upgrade contract. ownerKey is writed in main of MainnetUpgrade.java & TestnetUpgrade.java.

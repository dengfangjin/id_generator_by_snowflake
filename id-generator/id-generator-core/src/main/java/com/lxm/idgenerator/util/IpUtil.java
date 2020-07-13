package com.lxm.idgenerator.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * @author luoxiaomin
 * @version 1.0.0
 * @date 2018/11/1
 * @time 8:56
 */
public class IpUtil {

    /**
     * 获取本机网卡IP地址，这个地址为所有网卡中非回路地址的第一个<br>
     * 如果获取失败调用 {@link InetAddress#getLocalHost()}方法获取。<br>
     * 此方法不会抛出异常，获取失败将返回<code>null</code><br>
     *
     * 参考：http://stackoverflow.com/questions/9481865/getting-the-ip-address-of-the-current-machine-using-java
     * 参考: hutool.NetUtil.getLocalhost方法
     * @return 本机网卡IP地址，获取失败返回<code>null</code>
     */
    public static InetAddress getIpAddress() {
        InetAddress candidateAddress = null;
        NetworkInterface iface;
        InetAddress inetAddr;
        try {
            for (Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
                iface = ifaces.nextElement();
                for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
                    inetAddr = inetAddrs.nextElement();
                    if (false == inetAddr.isLoopbackAddress()) {
                        if (inetAddr.isSiteLocalAddress()) {
                            return inetAddr;
                        } else if (null == candidateAddress) {
                            // 非site-local地址做为候选地址返回
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
        } catch (SocketException e) {
            // ignore socket exception, and return null;
        }

        if (null == candidateAddress) {
            try {
                candidateAddress = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                // ignore
            }
        }

        return candidateAddress;
    }
}

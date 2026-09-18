package com.hexin.cbas.spider.web.filter;

import org.apache.commons.codec.binary.Base64;
import sun.security.provider.X509Factory;

import java.security.MessageDigest;

/**
 * @author xuxiang
 * @创建时间 2020/4/14 9:25
 * @描述
 */
public class CertUtils {
    
    final static Base64 base64 = new Base64();
    
    
    /**
     * 获取证书指纹，同crm统计平台
     * @param cert
     * @return
     */
    public static String getMd5Cert(String cert) {
        String md5Cert;
        
        if (cert == null) {
            return null;
        }
        
        cert = cert.replace("\n","");
        cert = cert.replace(X509Factory.BEGIN_CERT,"");
        cert = cert.replace(X509Factory.END_CERT,"");
        cert = cert.trim().replace(" ","");
        
        byte[] client_certs = base64.decode(cert);
        
        md5Cert = CertUtils.certMd5(client_certs).toUpperCase();
        
        return md5Cert;
        
    }
    
    
    public static String certMd5(byte[] inStr) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(inStr);
            StringBuffer buf = new StringBuffer();
            for (byte b:md.digest()) {
                buf.append(String.format("%02x",b&0xff));
            }
            return buf.toString();
            
        } catch (Exception e) {
            return "";
        }
    }
}

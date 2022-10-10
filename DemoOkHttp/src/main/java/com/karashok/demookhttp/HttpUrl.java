package com.karashok.demookhttp;

import android.text.TextUtils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-21-2022
 */
public class HttpUrl {

    /**
     * 协议 http/https
     */
    private String protocol;

    /**
     * 192.6.2.3
     */
    private String host;

    /**
     * 文件地址
     */
    private String file;

    /**
     * 端口
     */
    private int port;

    /**
     * scheme://host:port/path?query#fragment
     * @param url
     * @throws MalformedURLException
     */
    public HttpUrl(String url) throws MalformedURLException {
        URL url1 = new URL(url);
        this.host = url1.getHost();
        this.file = url1.getFile();
        this.file = (this.file == null || this.file.trim().length() == 0) ? "/" : this.file;
        this.protocol = url1.getProtocol();
        this.port = url1.getPort();
        this.port = this.port == -1 ? url1.getDefaultPort() : port;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public String getFile() {
        return file;
    }

    public int getPort() {
        return port;
    }
}

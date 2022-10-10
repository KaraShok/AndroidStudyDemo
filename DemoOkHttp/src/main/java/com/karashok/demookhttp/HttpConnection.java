package com.karashok.demookhttp;

import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;

import javax.net.ssl.SSLSocketFactory;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-21-2022
 */
public class HttpConnection {

    private static final String HTTPS = "https";

    private Socket socket;

    private InputStream is;

    private OutputStream os;

    private Request request;

    public long lastUsetime;

    public Request request() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public InputStream call(HttpCodec httpCodec) throws IOException {
        try{
            createSocket();
            httpCodec.writeRequest(os,request);
            return is;
        } catch (Exception e) {
            closeQuietly();
            throw new IOException(e);
        }
    }

    public void closeQuietly() {
        if (socket != null) {
            try{
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createSocket() throws IOException {
        if (socket == null || socket.isClosed()) {
            HttpUrl httpUrl = request.httpUrl();
            if (httpUrl.getProtocol().equalsIgnoreCase(HTTPS)) {
                socket = SSLSocketFactory.getDefault().createSocket();
            } else {
                socket = new Socket();
            }
            socket.connect(new InetSocketAddress(httpUrl.getHost(),httpUrl.getPort()));
            os = socket.getOutputStream();
            is = socket.getInputStream();
        }
    }

    public void updateLastUseTime() {
        lastUsetime = System.currentTimeMillis();
    }

    public boolean isSameAddress(String host, int port) {
        if (socket == null) {
            return false;
        }
        return Objects.equals(socket.getInetAddress().getHostName(),host) && port == socket.getPort();
    }
}

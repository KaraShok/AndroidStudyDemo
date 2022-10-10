package com.karashok.demookhttp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-21-2022
 */
public class HttpCodec {

    private static final String CRLF = "\r\n";
    private static final int CR = 13;
    private static final int LF = 10;
    private static final String SPACE = " ";
    private static final String VERSION = "HTTP/1.1";
    private static final String COLON = ":";

    public static final String HEAD_HOST = "Host";
    public static final String HEAD_CONNECTION = "Connection";
    public static final String HEAD_CONTENT_TYPE = "Content_Type";
    public static final String HEAD_CONTENT_LENGTH = "Content_Length";
    public static final String HEAD_TRANSFER_ENCODING = "Transfer-Encoding";

    public static final String HEAD_VALUE_KEEP_ALIVE = "Keep-Alive";
    public static final String HEAD_VALUE_CHUNKED = "Chunked";

    private ByteBuffer byteBuffer;

    public HttpCodec() {
        // 申请足够大的内存记录读取的数据 (一行)
        byteBuffer = ByteBuffer.allocate(10 * 1024);
    }

    public void writeRequest(OutputStream os, Request request) throws IOException {
        StringBuffer protocol = new StringBuffer();

        protocol.append(request.method());
        protocol.append(SPACE);
        protocol.append(request.httpUrl().getFile());
        protocol.append(SPACE);
        protocol.append(VERSION);
        protocol.append(CRLF);

        Set<Map.Entry<String, String>> entries = request.headers().entrySet();
        for (Map.Entry<String, String> entry : entries) {
            protocol.append(entry.getKey());
            protocol.append(COLON);
            protocol.append(SPACE);
            protocol.append(entry.getValue());
            protocol.append(CRLF);
        }
        protocol.append(CRLF);

        RequestBody requestBody = request.requestBody();
        if (requestBody != null) {
            protocol.append(requestBody.body());
        }

        os.write(protocol.toString().getBytes());
        os.flush();
    }

    public HashMap<String, String> readHeaders(InputStream is) throws IOException {
        HashMap<String, String> headers = new HashMap<>();
        while (true) {
            String line = readLine(is);
            // 读取到空行 则下面的为body
            if (isEmptyLine(line)) {
                break;
            }
            int index = line.indexOf(":");
            if (index > 0) {
                String name = line.substring(0,index);
                // ": "移动两位到 总长度减去两个("\r\n")
                String value = line.substring(index + 2, line.length() - 2);
                headers.put(name,value);
            }
        }
        return headers;
    }

    public String readChunked(InputStream is) throws IOException {
        int len = -1;
        boolean isEmptyData = false;
        StringBuffer chunked = new StringBuffer();
        while (true) {
            if (len < 0) {
                String line = readLine(is);
                line = line.substring(0,line.length() - 2);
                len = Integer.valueOf(line, 16);
                // chunk编码的数据最后一段为 0\r\n\r\n
                isEmptyData = len == 0;
            } else {
                // 块长度不包括\r\n  所以+2将 \r\n 读走
                byte[] bytes = readBytes(is, len + 2);
                chunked.append(new String(bytes));
                len = -1;
                if (isEmptyData) {
                    return chunked.toString();
                }
            }
        }
    }

    public boolean isEmptyLine(String line) {
        return Objects.equals(line,"\r\n");
    }

    public String readLine(InputStream is) throws IOException {
        try{
            byte b;
            boolean isMabeyEofLine = false;
            byteBuffer.clear();
            byteBuffer.mark();
            while ((b = (byte)is.read()) != -1) {
                byteBuffer.put(b);
                // 读取到/r则记录，判断下一个字节是否为/n
                if (b == CR) {
                    isMabeyEofLine = true;
                } else if (isMabeyEofLine) {
                    if (b == LF) {
                        byte[] lineBytes = new byte[byteBuffer.position()];
                        byteBuffer.reset();
                        byteBuffer.get(lineBytes);
                        byteBuffer.clear();
                        byteBuffer.mark();
                        String line = new String(lineBytes);
                        return line;
                    }
                    isMabeyEofLine = false;
                }
            }
        } catch (Exception e) {
            throw new IOException(e);
        }
        throw new IOException("Response Read Line.");
    }

    public byte[] readBytes(InputStream is, int len) throws IOException {
        byte[] bytes = new byte[len];
        int readNum = 0;
        while (true) {
            readNum ++;
            is.read(bytes,readNum,len - readNum);
            if (readNum == len) {
                return bytes;
            }
        }
    }

}

package com.liangzhicheng.common.http;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;

/**
 * HttpDeleteRequest相关类
 * @author liangzhicheng
 */
public class HttpDeleteRequest extends HttpEntityEnclosingRequestBase {

    public static final String METHOD_NAME = "DELETE";

    /**
     * 获取方法(必须重载)
     * @return String
     */
    @Override
    public String getMethod() {
        return METHOD_NAME;
    }

    public HttpDeleteRequest(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    public HttpDeleteRequest(final URI uri) {
        super();
        setURI(uri);
    }

    public HttpDeleteRequest() {
        super();
    }

}

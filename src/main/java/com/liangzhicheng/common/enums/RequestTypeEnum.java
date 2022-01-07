package com.liangzhicheng.common.enums;

import org.apache.http.client.methods.*;

public enum RequestTypeEnum {

    POST{
        @Override
        public HttpRequestBase getHttpType(String url) {
            return new HttpPost(url);
        }
    },

    GET{
        @Override
        public HttpRequestBase getHttpType(String url) {
            return new HttpGet(url);
        }
    },

    DELETE{
        @Override
        public HttpRequestBase getHttpType(String url) {
            return new HttpDelete(url);
        }
    },

    PUT{
        @Override
        public HttpRequestBase getHttpType(String url) {
            return new HttpPut(url);
        }
    };

    public abstract HttpRequestBase getHttpType(String url);

}

package com.innovator.multisongsdownloader.network;

/**
 * Created by yuxumou on 17-11-17.
 */


import java.net.MalformedURLException;
import java.net.URL;
import java.util.TreeMap;


/**
 * An HTTP request. Instances of this class are immutable if their {@link #body} is null or itself
 * immutable.
 */
public final class Request {
    final URL url;
    final String method;
    final TreeMap headers;
    final String body;
    final Object tag;

    Request(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = builder.headers;
        this.body = builder.body;
        this.tag = builder.tag != null ? builder.tag : this;
    }

    public URL url() {
        return url;
    }

    public String method() {
        return method;
    }

    public TreeMap headers() {
        return headers;
    }

    public String body() {
        return body;
    }

    public Object tag() {
        return tag;
    }

    public Builder newBuilder(String url) {
        return new Builder(url);
    }


    @Override
    public String toString() {
        return "Request{method="
                + method
                + ", url="
                + url
                + ", tag="
                + (tag != this ? tag : null)
                + '}';
    }

    public static class Builder {
        URL url;
        String method = "POST";
        TreeMap headers;
        String body;
        Object tag;

        public Builder(String httpurl) {
            try {
                this.url = new URL(httpurl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            this.headers = new TreeMap();
        }

        public Builder(URL url) {
            this.url = url;
            this.headers = new TreeMap();
        }

        public  Builder addBody(String body){
            this.body = body;
            return this;
        }

        public Builder addHeader(String name, String value) {
            headers.put(name, value);
            return this;
        }

        public Builder tag(Object tag) {
            this.tag = tag;
            return this;
        }
        public  Builder method(String method){
            this.method = method;
            return this;
        }

        public Request build() {
            if (url == null) throw new IllegalStateException("url == null");
            return new Request(this);
        }
    }
}


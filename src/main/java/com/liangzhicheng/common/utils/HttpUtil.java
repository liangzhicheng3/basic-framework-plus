package com.liangzhicheng.common.utils;

import com.liangzhicheng.common.exception.TransactionException;
import com.liangzhicheng.common.http.HttpConnectionManager;
import com.liangzhicheng.common.http.HttpDeleteRequest;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Http工具类
 * @author liangzhicheng
 */
public class HttpUtil {

    /**
     * 获取访问地址前缀
     * @param request
     * @return String
     */
    public static String getAccessUrlPrefix(HttpServletRequest request) {
        return String.format(
                "http://%s:%s%s",
                request.getServerName(),
                request.getServerPort(),
                request.getContextPath());
    }

    /**
     * 获取真实的ip地址
     * @param request
     * @return String
     */
    public static String getClientUrl(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if(ToolUtil.isNotBlank(ip)){
            //多次反向代理后会有多个IP值，第一个IP才是真实IP
            int index = ip.indexOf(",");
            if(index != -1){
                return ip.substring(0, index);
            }else{
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if(ToolUtil.isNotBlank(ip)){
            return ip;
        }
        return request.getRemoteAddr();
    }

    /**
     * 向指定Url发送Post方法的请求，String方式
     * @param url
     * @param params
     * @return String
     */
    public static String sendPost(String url, String params) {
        try {
            CloseableHttpClient client = HttpConnectionManager
                    .getInstance()
                    .getHttpClient();
            HttpPost post = new HttpPost(url);
            // 设置连接超时,设置读取超时
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(10000)
                    .setSocketTimeout(10000)
                    .build();
            post.setConfig(requestConfig);
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-Type", "application/json;charset=utf-8");
            // 设置参数
            StringEntity se = new StringEntity(params, "UTF-8");
            post.setEntity(se);
            HttpResponse response = client.execute(post);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    return EntityUtils.toString(resEntity, "UTF-8");
                }
            }
        } catch (Exception e) {
            PrintUtil.error("[发送post请求] 发生异常：{}", e.getMessage());
        }
        return null;
    }

    /**
     * 向指定Url发送Post方法的请求，Map方式
     * @param url
     * @param map
     * @return String
     */
    public static String sendPost(String url, Map<String, ?> map){
        PrintWriter out = null;
        BufferedReader in = null;
        String param = "";
        String result = "";
        Iterator<String> it = map.keySet().iterator();
        while(it.hasNext()){
            String key = it.next();
            param += key + "=" + map.get(key) + "&";
        }
        try {
            URL realUrl = new URL(url);
            //打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            //设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            //送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            //发送请求参数
            out.print(param);
            //flush输出流的缓冲
            out.flush();
            //定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            PrintUtil.error("[发送post请求] 发生异常：{}", e.getMessage());
            throw new TransactionException("发送post请求发生未知错误");
        } finally {//使用finally关闭输出流、输入流
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch(IOException e){
                PrintUtil.error("[发送post请求] 关闭资源异常：{}", e.getMessage());
            }
        }
        return result;
    }

    /**
     * 向指定Url发送Get方法的请求，String方式
     * @param url
     * @return String
     */
    public static String sendGet(String url) {
        try {
            URL getUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) getUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            //获取响应状态
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                PrintUtil.error("[发送get请求] 连接失败！");
                return "";
            }
            //获取响应内容体
            String line, result = "";
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            while ((line = in.readLine()) != null) {
                result += line + "\n";
            }
            in.close();
            return result;
        } catch (Exception e) {
            PrintUtil.error("[发送get请求] 发生异常：{}", e.getMessage());
            throw new TransactionException("发送get请求发生未知错误");
        }
    }

    /**
     * 向指定Url发送Get方法的请求，String请求体参数方式与Map请求头参数方式
     * @param url
     * @param map
     * @return String
     */
    public static String sendGet(String url, Map<String, Object> map) {
        HttpGet get = null;
        String result = "";
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            get = new HttpGet(url);
            //配置请求的超时设置
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(3000)
                    .setConnectTimeout(3000)
                    .setSocketTimeout(3000)
                    .build();
            get.setConfig(requestConfig);
            get.setHeader("Content-Type", map.get("contentType") + "");
            CloseableHttpResponse response = client.execute(get); //发送请求
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity,"utf-8");
        } catch (Exception e) {
            PrintUtil.error("[发送get请求] 发生异常：{}", e.getMessage());
            throw new TransactionException("发送get请求发生未知错误");
        } finally{
            if (get != null) {
                get.releaseConnection();
            }
        }
        return result;
    }

    /**
     * 向指定Url发送Put方法的请求，String请求体参数方式与Map请求头参数方式
     * @param urlPath
     * @param param json参数
     * @param map
     * @return String
     */
    public static String sendPut(String urlPath, Object param, Map<String, Object> map) {
        CloseableHttpClient client = null;
        CloseableHttpResponse response = null;
        String content = "";
        try {
            //创建连接
            String encode = "utf-8";
            //HttpClients.createDefault()等价于 HttpClientBuilder.create().build();
            client = HttpClients.createDefault();
            HttpPut httpput = new HttpPut(urlPath);
            /**header中通用属性*/
            httpput.setHeader("Accept","*/*");
            httpput.setHeader("Accept-Encoding","gzip, deflate");
            httpput.setHeader("Cache-Control","no-cache");
            httpput.setHeader("Connection", "keep-alive");
            /**业务参数*/
            httpput.setHeader("Content-Type", map.get("contentType") + "");
            //组织请求参数
            StringEntity stringEntity = new StringEntity(JSONUtil.toJSONString(param), encode);
            httpput.setEntity(stringEntity);
            //响应信息
            response = client.execute(httpput);
            HttpEntity entity = response.getEntity();
            content = EntityUtils.toString(entity, encode);
        } catch (Exception e) {
            PrintUtil.error("[发送put请求] 发生异常：{}", e.getMessage());
            throw new TransactionException("发送put请求发生未知错误");
        }finally{
            try {
                if (client != null) {
                    client.close();
                }
                if(response != null){
                    response.close();
                }
            } catch (IOException e) {
                PrintUtil.error("[发送put请求] 关闭资源异常：{}", e.getMessage());
            }
        }
        return content;
    }

    /**
     * 向指定Url发送Delete方法的请求，String请求体参数方式与Map请求头参数方式
     * @param url
     * @param param
     * @param map
     * @return String
     */
    public static String sendDelete(String url, String param, Map<String, Object> map) {
        CloseableHttpClient client = null;
        HttpDeleteRequest delete = null;
        String result = null;
        try {
            client = HttpClients.createDefault();
            delete = new HttpDeleteRequest(url);
            delete.addHeader("Content-Type", map.get("contentType") + "");
            delete.setHeader("Accept", "application/json; charset=utf-8");
            delete.setEntity(new StringEntity(param));
            CloseableHttpResponse response = client.execute(delete);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity);
        } catch (Exception e) {
            PrintUtil.error("[发送delete请求] 发生异常：{}", e.getMessage());
            throw new TransactionException("发送delete请求发生未知错误");
        } finally {
            try {
                if (client != null) {
                    client.close();
                }
                if (delete != null) {
                    delete.releaseConnection();
                }
            } catch (IOException e) {
                PrintUtil.error("[发送delete请求] 关闭资源异常：{}", e.getMessage());
            }
        }
        return result;
    }

    /**
     * 去除待带script、src的语句，转义替换后的value值
     * @param value
     * @return String
     */
    public static String replaceXSS(String value) {
        if (value != null) {
            try{
                value = value.replace("+","%2B");   //'+' replace to '%2B'
                value = URLDecoder.decode(value, "utf-8");
            }catch(UnsupportedEncodingException e){
                PrintUtil.error(e.getMessage());
            }catch(IllegalArgumentException e){
                PrintUtil.error(e.getMessage());
            }

            //Avoid null characters
            value = value.replaceAll("\0", "");

            //Avoid anything between script tags
            Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");

            //Avoid anything in a src='...' type of expression
            /*scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");*/

            //Remove any lonesome </script> tag
            scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");

            //Remove any lonesome <script ...> tag
            scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            //Avoid eval(...) expressions
            scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            //Avoid expression(...) expressions
            scriptPattern = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            //Avoid javascript:... expressions
            scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");

            //Avoid alert:... expressions
            scriptPattern = Pattern.compile("alert", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");

            //Avoid onload= expressions
            scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
            value = scriptPattern.matcher(value).replaceAll("");

            scriptPattern = Pattern.compile("vbscript[\r\n| | ]*:[\r\n| | ]*", Pattern.CASE_INSENSITIVE);
            value = scriptPattern.matcher(value).replaceAll("");
        }
        return XSSFilter(value);
    }

    /**
     * 特殊字符过滤
     * @param value
     * @return String
     */
    public static String XSSFilter(String value) {
        if (value == null) {
            return null;
        }
        StringBuffer result = new StringBuffer(value.length());
        for (int i = 0; i < value.length(); ++i) {
            switch (value.charAt(i)) {
                case '<':
                    result.append("<");
                    break;
                case '>':
                    result.append(">");
                    break;
                case '"':
                    result.append("\"");
                    break;
                case '\'':
                    result.append("'");
                    break;
                case '%':
                    result.append("%");
                    break;
                case ';':
                    result.append(";");
                    break;
                case '(':
                    result.append("(");
                    break;
                case ')':
                    result.append(")");
                    break;
                case '&':
                    result.append("&");
                    break;
                case '+':
                    result.append("+");
                    break;
                default:
                    result.append(value.charAt(i));
                    break;
            }
        }
        return result.toString();
    }

}

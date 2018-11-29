package com.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

public class URLConnectionProxy {

	/**
     * The User Agent
     */
    private static final String AGENT = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";

    /**
     * Used to store the proxy settings
     */
    private Proxy proxy;

    /**
     * 
     * Method used to add proxy settings
     *
     * @param ip
     *            the proxy IP
     * @param port
     *            the proxy port
     */
    public void setProxy(String ip, int port) {
    this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
    }

    /**
     * 
     * Method used to add proxy settings with authentication
     *
     * @param ip
     * @param port
     * @param username
     * @param password
     */
    public void setProxy(String ip, int port, String username, String password) {
    this.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
    Authenticator authenticator = new Authenticator() {

        public PasswordAuthentication getPasswordAuthentication() {
        return (new PasswordAuthentication(username, password.toCharArray()));
        }
    };
    Authenticator.setDefault(authenticator);

    }

    /**
     * The method will return the content in a {@link InputStream} object
     * 
     * @param domain
     *            The Domain name
     * 
     * @return the content as {@link InputStream} object
     * @throws Exception
     */
    private InputStream getContent(String domain) throws Exception {
    URL url = new URL(domain);
    URLConnection connection = null;
    if (this.proxy != null)
        connection = url.openConnection(this.proxy);
    else
        connection = url.openConnection();

    connection.setRequestProperty("User-Agent", AGENT);
    return connection.getInputStream();
    }

    /**
     * Method used to get URL content in {@link String} format
     * 
     * @param domain
     *            the {@link String} the URL
     * @return the {@link String} object returned
     * @throws Exception
     */
    public String getString(String domain) throws Exception {
    InputStream is = getContent(domain);
    StringBuilder sb = new StringBuilder();

    BufferedReader br = new BufferedReader(new InputStreamReader(is));
    String line;
    try {
        while ((line = br.readLine()) != null) {
        sb.append(line);
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        /** finally block to close the {@link BufferedReader} */
        if (br != null) {
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        }
    }
    return sb.toString();
    }

    public static void main(String[] args) throws Exception {
    System.out.println("URLConnection with Proxy Example");
    System.out.println();

    String url = "https://www.baidu.com//";

    URLConnectionProxy con = new URLConnectionProxy();

    /**
     * activate this line if you are behind a proxy server - change the settings
     * accordingly
     */
    con.setProxy("127.0.0.1", 8118);

    /**
     * activate this line if you are behind a proxy server with authentication -
     * change the settings accordingly
     */
    // con.setProxy("127.0.0.1", 7000, "user", "passowrd");

    String result = con.getString(url);

    System.out.println("URL: " + url);
    System.out.println();
    System.out.println(result);
    }
}

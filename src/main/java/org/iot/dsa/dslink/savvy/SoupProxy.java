package org.iot.dsa.dslink.savvy;

import java.io.IOException;
import java.util.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class SoupProxy {
    
    String address;
    String username;
    String password;
    private String base64login;
    
    public SoupProxy(String address, String username, String password) {
        this.address = address;
        this.username = username;
        this.password = password;
        String login = username + ":" + password;
        this.base64login = Base64.getEncoder().encodeToString(login.getBytes());
    }
    
    public Document getDoc(String suffix) throws IOException {
        return Jsoup.connect(address + suffix).header("Authorization", "Basic " + base64login).get();
    }

}

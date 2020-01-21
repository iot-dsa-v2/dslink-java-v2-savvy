package org.iot.dsa.dslink.savvy;

import java.io.IOException;
import org.iot.dsa.node.DSString;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ZoneNode extends PageNode {
    
    private String monSuffix;
    private String setSuffix;
    
    public ZoneNode() {
        
    }
    
    public ZoneNode(SoupProxy proxy, String suffix) {
        super(proxy, suffix);
        this.monSuffix = suffix.replaceFirst("loadmenu", "loadmon");
        this.setSuffix = suffix.replaceFirst("loadmenu", "loadset");
    }
    
    @Override
    public void init() {
        scrapePoints(setSuffix);
        scrapePoints(monSuffix);
    }
    
    private void scrapePoints(String suffix) {
        try {
            Document monDoc = proxy.getDoc(suffix);
            Elements trs = monDoc.select("table").last().select("tr");
            for (int i=0; i < trs.size() - 1; i++) {
                Element tr = trs.get(i);
                Elements tds = tr.select("td");
                if (tds.size() == 2) {
                    put(tds.get(0).text(), DSString.valueOf(tds.get(1).text()));
                }
            }
        } catch (IOException e) {
            warn("", e);
        }
    }

}

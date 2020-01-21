package org.iot.dsa.dslink.savvy;

import java.io.IOException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ZonesNode extends PageNode {
    
    public ZonesNode() {
    }
    
    public ZonesNode(SoupProxy proxy) {
        super(proxy, "/loaddir.htm");
    }
    
    @Override
    public void init() {
        try {
            Document doc = proxy.getDoc(suffix);
            Elements as = doc.select("table").last().select("a[href]");
            for (Element a: as) {
                info(a.text() + "  :  " + a.attr("href"));
                String childSuffix = "/" + a.attr("href");
                String childName = a.text();
                put(childName, new ZoneNode(proxy, childSuffix));
            }
        } catch (IOException e) {
            warn("", e);
        }
    }

}

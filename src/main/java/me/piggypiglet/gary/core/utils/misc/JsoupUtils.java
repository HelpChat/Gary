package me.piggypiglet.gary.core.utils.misc;

import org.jsoup.nodes.Document;

/**
 * Created by GlareMasters
 * Date: 8/26/2018
 * Time: 11:41 PM
 */
public class JsoupUtils {

    public String getText(Document doc, String selector) {
        if (!doc.select(selector).text().equalsIgnoreCase("")) {
            return doc.select(selector).text();
        }
        else {
            return "N/A";
        }
    }

}

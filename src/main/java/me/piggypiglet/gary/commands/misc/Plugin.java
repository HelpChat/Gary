package me.piggypiglet.gary.commands.misc;

import com.google.inject.Inject;
import me.piggypiglet.gary.core.framework.Command;
import me.piggypiglet.gary.core.utils.misc.JsoupUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * Created by GlareMasters
 * Date: 8/26/2018
 * Time: 11:34 PM
 */
public final class Plugin extends Command {

    @Inject JsoupUtils utils;

    String url, downloads, name,
    tag, iconURL, author, category, releaseDate,
    lvn, lvr, lvd, downloadURL, contributors,
    languages, versions;

    public Plugin() {
        super("?plugin", "Get plugin information from Spigot", true);
    }

    @Override
    protected void execute(GuildMessageReceivedEvent e, String[] args) {
        if (args.length == 1) {
            url = args[0];
            Document doc;
            try {
                doc = Jsoup.connect("https://www.spigotmc.org/resources/" + url + "/").userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6").get();
                downloads = utils.getText(doc, "#resourceInfo > div > div > dl.downloadCount > dd");
                name = utils.getText(doc, "#content > div > div > div.uix_contentFix > div > div > div.resourceInfo > h1");
                tag = utils.getText(doc, ".tagLine");
                author = utils.getText(doc, "#resourceInfo > div > div.pairsJustified > dl.author > dd > a");
                category = utils.getText(doc, "#resourceInfo > div > div.pairsJustified > dl.resourceCategory > dd > a");
                lvd = utils.getText(doc, "#versionInfo > div > div > dl.versionDownloadCount > dd");
                lvr = utils.getText(doc, "#versionInfo > div > div > dl.versionReleaseDate > dd");
                lvn = utils.getText(doc, "#content > div > div > div.uix_contentFix > div > div > div.resourceInfo > h1 > span");
                releaseDate = utils.getText(doc, "#resourceInfo > div > div.pairsJustified > dl.firstRelease > dd > span");
                contributors = utils.getText(doc, ".customResourceFieldcontributors > dd");
                languages = utils.getText(doc, ".customResourceFieldlanguages > dd");
                versions = utils.getText(doc, ".plainList");

                Element download = doc.selectFirst("#content > div > div > div.uix_contentFix > div > div > div.resourceInfo > ul > li > label > a");
                downloadURL = download.absUrl("href");
                Element icon = doc.selectFirst("#content > div > div > div.uix_contentFix > div > div > div.resourceInfo > div > img");
                iconURL = icon.absUrl("src");
                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle(name);
                builder.setDescription(tag);
                builder.addField("ℹ Plugin Information", "", false);
                builder.addField("Author", author, true);
                builder.addField("Category", category, true);
                builder.addField("Downloads", downloads, true);
                builder.addField("Initial Release", releaseDate, true);
                builder.addField("Contributors", contributors, true);
                builder.addField("Supported Languages", languages, true);
                builder.addField("Tested Minecraft Versions", versions, true);
                builder.addField("\uD83D\uDCC1 Latest Version Information", "", false);
                builder.addField("Version", lvn, true);
                builder.addField("Release Date", lvr, true);
                builder.addField("Downloads", lvd, true);
                builder.addField("Download Latest Version", downloadURL, false);
                builder.setThumbnail(iconURL);
                e.getChannel().sendMessage(builder.build()).queue();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Date;
import java.net.URL;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Feed implements ICommand {

    private String subject;
    private int numberOf;
    private boolean exista = true;
    private String topics;

    public void input(CommandContext ctx){

        List<String> args = ctx.getArgs();
        String subject = args.get(0);
        String topics = new String();
        int numberOf = Integer.parseInt(args.get(1));
        this.numberOf = numberOf;
        boolean gasit = false;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(new File("feed.xml"));

            document.getDocumentElement().normalize();

            NodeList subiect = document.getElementsByTagName("subjects");

            for (int i = 0; i < subiect.getLength(); i++) {
                Node sub = subiect.item(i);
                if (sub.getNodeType() == Node.ELEMENT_NODE) {
                    Element subInferior = (Element) sub;
                    String id = subInferior.getAttribute("type");
                    String subiectcautat = subInferior.getElementsByTagName("subject").item(0).getTextContent();
                    String link = subInferior.getElementsByTagName("url").item(0).getTextContent();
                    topics = topics + subiectcautat;
                    topics = topics + ",";
                    if(subject.equalsIgnoreCase(subiectcautat)){
                        gasit = true;
                        this.subject = link;
                    }
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.topics = topics;

        if(gasit == false){
            this.exista = false;
        }
    }

    @Override
    public void handle(CommandContext ctx) {

        final TextChannel channel = ctx.getChannel();
        input(ctx);

        if(this.exista == true) {

            boolean ok = false;
            try {

                URL feedUrl = new URL(this.subject);
                SyndFeedInput input = new SyndFeedInput();
                SyndFeed feed = input.build(new XmlReader(feedUrl));

                int count = 0;
                for (Object o : feed.getEntries()) {
                    count++;
                    SyndEntry entry = (SyndEntry) o;
                    EmbedBuilder info = new EmbedBuilder();
                    info.setTitle(entry.getTitle());
                    info.setDescription(entry.getAuthor());
                    String target = entry.getDescription().getValue();
                    target = target.replaceAll("\\<.*?\\>", "");
                    target = target.replaceAll(" &#8594;", "");
                    info.addField("Descriere", target, false);
                    info.addField("URL", entry.getLink(), false);
                    info.setColor(0xffffff);
                    channel.sendMessage(info.build()).queue();
                    if (count >= this.numberOf) {
                        break;
                    }
                }
                System.out.println(count);
                ok = true;

            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("ERROR: " + ex.getMessage());
            }

            if (!ok) {
                System.out.println();
                System.out.println("FeedReader reads and prints any RSS/Atom feed type.");
                System.out.println("The first parameter must be the URL of the feed to read.");
                System.out.println();
            }
        } else {
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle("Comanda gresita");
            info.setDescription("Ati introdus un subiect gresit!");
            info.addField("Utilizare", "Folositi altceva precum: " + this.topics, false);
            channel.sendMessage(info.build()).queue();
        }
        
        this.exista = true;
        
    }


    @Override
    public String getName() {
        return "feed";
    }

    @Override
    public String getHelp() {
        return "Ofera informatii despre ultimele postari legate de un subiect dat ca argument.";
    }
}

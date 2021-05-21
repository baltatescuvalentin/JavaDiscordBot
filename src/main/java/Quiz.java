import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Quiz implements ICommand {
    
    private EmbedBuilder info;
    private String code;
    private final EventWaiter waiter;

    public Quiz(EventWaiter waiter){
        this.waiter = waiter;
    }

    public void input(CommandContext ctx){

        final TextChannel channel = ctx.getChannel();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(new File("quiz.xml"));

            document.getDocumentElement().normalize();

            NodeList subiect = document.getElementsByTagName("subject");

            Random rand = new Random();
            int rnd = rand.nextInt(subiect.getLength());
            String numberQ = Integer.toString(rnd);

            for (int i = 0; i < subiect.getLength(); i++) {
                Node sub = subiect.item(i);
                if (sub.getNodeType() == Node.ELEMENT_NODE) {
                    Element subInferior = (Element) sub;
                    String id = subInferior.getAttribute("q");
                    //System.out.println(id + " " + numberQ);
                    if(id.equals(numberQ)) {
                        info = new EmbedBuilder();
                        info.setTitle("Quiz");
                        info.setDescription("Raspunde la intrebare trimitand raspunsul corect. Eg: 2");
                        String subiectcautat = subInferior.getElementsByTagName("question").item(0).getTextContent();
                        code = subInferior.getElementsByTagName("correct").item(0).getTextContent();
                        info.addField("Intrebare", subiectcautat, false);
                        for(int j = 1; j <= 4; j++){
                            subiectcautat = subInferior.getElementsByTagName("answer" + Integer.toString(j)).item(0).getTextContent();
                            info.addField("Intrebarea numarul " + Integer.toString(j), Integer.toString(j) + ")" +subiectcautat, false);
                        }
                        info.setColor(0xffffff);
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
    }

    @Override
    public void handle(CommandContext ctx) {

        input(ctx);
        GuildMessageReceivedEvent event = ctx.getEvent();
        final TextChannel channel = ctx.getChannel();
        System.out.println(code);

        channel.sendMessage(info.build()).queue((message) -> {
            this.waiter.waitForEvent(
                GuildMessageReceivedEvent.class,
                e -> e.getAuthor().equals(event.getAuthor())
                     && e.getChannel().equals(event.getChannel())
                     && !e.getMessage().equals(event.getMessage()),
                (e) -> {
                    if(e.getMessage().getContentRaw().equalsIgnoreCase(code)){
                        channel.sendMessage("Uraaa! Ai raspuns corect! " + e.getAuthor().getAsMention()).queue();
                    }
                    else {
                        channel.sendMessage("Raspuns gresit! \uD83D\uDE25 " + e.getAuthor().getAsMention()).queue();
                    }
                },
                10, TimeUnit.SECONDS,
                () -> channel.sendMessage("Timpul a expirat! ⏰").queue()
                                    );
        });

    }

        /**
        if(unicode.equalsIgnoreCase(this.code)){
            channel.sendMessage("Uraaa! Ai raspuns corect!").queue();
        }
        else {
            channel.sendMessage("Raspuns gresit! :(").queue();
        }
    }
         */

    @Override
    public String getName() {
        return "quiz";
    }

    @Override
    public String getHelp() {
        return "Primeste o intreabare random la care poti sa raspunzi cu ajutorul unor reactii cu emoji-uri";
    }
}
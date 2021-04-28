import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import me.duncte123.botcommons.commands.ICommandContext;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Questions implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();
        StringBuilder question = new StringBuilder(256);
        boolean gasit = false;

        for(String arg : args){
            question.append(arg);
            question.append(" ");
        }

        if(question.length() > 0){
            question.deleteCharAt(question.length() - 1);
        }

        System.out.println(question);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(new File("intrebari.xml"));

            document.getDocumentElement().normalize();

            NodeList subiect = document.getElementsByTagName("subject");

            for (int i = 0; i < subiect.getLength(); i++) {
                Node sub = subiect.item(i);
                if (sub.getNodeType() == Node.ELEMENT_NODE) {
                    Element subInferior = (Element) sub;
                    String id = subInferior.getAttribute("q");
                    String intrebare = subInferior.getElementsByTagName("question").item(0).getTextContent();
                    String raspuns = subInferior.getElementsByTagName("answer").item(0).getTextContent();

                    if(question.toString().equalsIgnoreCase(intrebare)){
                        gasit = true;
                        System.out.println(id + "/" + intrebare + "/" + raspuns);
                        channel.sendTyping().queue();
                        channel.sendMessage(raspuns).queue();
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

        if(gasit == false){
            channel.sendTyping().queue();
            channel.sendMessage("Nu stiu sa raspund! :(").queue();
        }
    }

    @Override
    public String getName() {
        return "question";
    }

    @Override
    public String getHelp() {
        return "Raspunde la anumite intrebari predefinite, daca una nu exista botul nu va sti sa raspunda";
    }
}

import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class Meme implements ICommand{

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        WebUtils.ins.getJSONObject("https://meme-api.herokuapp.com/gimme").async(
            (json) -> {

                final String title = json.get("title").asText();
                final String url = json.get("postLink").asText();
                final String image = json.get("url").asText();
                final EmbedBuilder embed = EmbedUtils.embedImageWithTitle(title, url, image);

                channel.sendMessage(embed.build()).queue();
            });

    }

    @Override
    public String getName() {
        return "meme";
    }

    @Override
    public String getHelp() {
        return "Afiseaza un meme";
    }
}
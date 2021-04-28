import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.*;
import javax.annotation.Nonnull;

public class Listener extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private final CommandManager manager = new CommandManager();


    @Override
    public void onReady(@NotNull ReadyEvent event) {
        super.onReady(event);
        LOGGER.info("{} is ready", event.getJDA().getSelfUser());
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        super.onGuildMessageReceived(event);

        User user = event.getAuthor();

        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }

        String raw = event.getMessage().getContentRaw();
        String prefix = "!";

        if(raw.equalsIgnoreCase(prefix + "shutdown") && user.getId().equals("320440948200964096")) {
            LOGGER.info("shutdown");
            event.getJDA().shutdown();
            BotCommons.shutdown(event.getJDA());
        } else if(raw.equalsIgnoreCase(prefix + "info")) {
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle("Informatii");
            info.setDescription("Bot de discord pentru proiectul la Programare Avansata");
            info.addField("Creator", "Baltatescu Valentin-Constantin 2B1", false);
            info.setColor(0xffffff);

            event.getChannel().sendMessage(info.build()).queue();
        }

        if(raw.startsWith(prefix)) {
            manager.handle(event, prefix);
        }
    }
}
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {

    private static final List<ICommand> commands = new ArrayList<>();

    public CommandManager(EventWaiter waiter) {
        addCommand(new Ping());
        addCommand(new Questions());
        addCommand(new Meme());
        addCommand(new Helps());
        addCommand(new Feed());
        addCommand(new Join());
        addCommand(new Leave());
        addCommand(new Play());
        addCommand(new Next());
        addCommand(new Stop());
        addCommand(new Quiz(waiter));
        addCommand(new Slots());
    }

    private void addCommand(ICommand cmd) {
        boolean commandName = this.commands.stream().anyMatch((it -> it.getName().equalsIgnoreCase(cmd.getName())));
        if(commandName) {
            throw new IllegalArgumentException("Comanda exista deja!");
        }

        commands.add(cmd);
    }

    public static List<ICommand> getCommands() {
        return commands;
    }

    @Nullable
    public ICommand getCommand(String search) {
        for (ICommand cmd : this.commands) {
            if(cmd.getName().equals(search.toLowerCase())) {
                return cmd;
            }
        }
        return null;
    }

    void handle(GuildMessageReceivedEvent event, String prefix) {
        String[] split = event.getMessage().getContentRaw().replaceFirst("(?i)" + Pattern.quote(prefix), "")
                              .split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke);

        if (cmd != null) {
            event.getChannel().sendTyping().queue();
            List<String> args = Arrays.asList(split).subList(1, split.length);

            CommandContext ctx = new CommandContext(event, args);

            cmd.handle(ctx);
        }
    }
}
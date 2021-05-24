import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class Helps implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();

        CommandManager comenzi;
        //comenzi = new CommandManager();
        List<ICommand> cmds = CommandManager.getCommands();

        EmbedBuilder info = new EmbedBuilder();
        info.setTitle("Help");
        info.setDescription("Informatii despre comenzi");
        info.addField("Descriere", "Pentru a folosi corect botul utilizati prefixul '!' urmat de un cuvant cheie. Comenzile existente sunt: ", false);
        info.addField("info", "Informatii despre bot", false);
        info.addField("shutdown", "Inchide bot-ul", false);
        for(ICommand i : cmds){
            info.addField(i.getName(), i.getHelp() , false);
        }
        info.setColor(0xffffff);

        channel.sendMessage(info.build()).queue();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp() {
        return "Afiseaza comenzile existente";
    }
}

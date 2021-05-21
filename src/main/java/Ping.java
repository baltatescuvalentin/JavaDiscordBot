import net.dv8tion.jda.api.JDA;

public class Ping implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        JDA jda = ctx.getJDA();

        jda.getRestPing().queue((ping) ->
                                    ctx.getChannel().sendMessageFormat("Reset ping: %sms\nWS ping: %sms",
                                                                       ping, jda.getGatewayPing()).queue());
    }

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getHelp() {
        return "Arata ping-ul botului.";
    }
}
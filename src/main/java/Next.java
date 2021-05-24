import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

public class Next implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if (!selfVoiceState.inVoiceChannel()) {
            channel.sendMessage("Bot-ul trebuie sa fie pe voice").queue();
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage("User-ul trebuie sa fie intr-un canal ca sa foloseasca comanda").queue();
            return;
        }

        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            channel.sendMessage("User-ul si bot-ul trebuie sa fie pe acelasi canal").queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        if (audioPlayer.getPlayingTrack() == null) {
            channel.sendMessage("Nicio melodie in coada").queue();
            return;
        }

        musicManager.scheduler.nextTrack();
        if(audioPlayer.getPlayingTrack() == null){
            channel.sendMessage("Nu exista melodie in coada!").queue();
            return;
        }
        channel.sendMessage("Pornim urmatoarea melodie: `" + audioPlayer.getPlayingTrack().getInfo().title + "` by `" + audioPlayer.getPlayingTrack().getInfo().author + "`").queue();
    }

    @Override
    public String getName() {
        return "next";
    }

    @Override
    public String getHelp() {
        return "Porneste urmatoarea melodie din coada daca exista.";
    }
}
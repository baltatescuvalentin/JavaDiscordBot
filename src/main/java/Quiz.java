import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Quiz implements ICommand {
    
    private EmbedBuilder info;
    private String code;
    private final EventWaiter waiter;
    private String source;

    public Quiz(EventWaiter waiter){
        this.waiter = waiter;
    }

    public void input(CommandContext ctx){

        final TextChannel channel = ctx.getChannel();

        try {

            Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/questionsdb", "root", "");

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select * from quiz");

            int count = 0;
            while(resultSet.next()){
                count++;
            }

            Random rand = new Random();
            int rnd = rand.nextInt(count);
            System.out.println("Numarul intrebarii: " + rnd);
            String numberQ = Integer.toString(rnd);

            resultSet = statement.executeQuery("select * from quiz");

            while (resultSet.next()) {

                String id = resultSet.getString("idquiz");
                String questiondb = resultSet.getString("question");
                String correctdb = resultSet.getString("correct");
                String sourcedb = resultSet.getString("source");

                if(id.equalsIgnoreCase(numberQ)) {
                    this.info = new EmbedBuilder();
                    this.info.setTitle("Quiz");
                    this.info.setDescription("Raspunde la intrebare trimitand raspunsul corect. Eg: 2");
                    this.code = correctdb;
                    this.source = sourcedb;
                    this.info.addField("Intrebare", questiondb, false);
                    for(int j = 1; j <= 4; j++){
                        String subiectcautat = resultSet.getString("answer" + Integer.toString(j));
                        this.info.addField("Intrebarea numarul " + Integer.toString(j), Integer.toString(j) + ")" +subiectcautat, false);
                    }
                    this.info.setColor(0xffffff);
                }
            }

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void handle(CommandContext ctx) {

        input(ctx);
        GuildMessageReceivedEvent event = ctx.getEvent();
        final TextChannel channel = ctx.getChannel();
        System.out.println(code);

        channel.sendMessage(this.info.build()).queue((message) -> {
            this.waiter.waitForEvent(
                GuildMessageReceivedEvent.class,
                e -> e.getAuthor().equals(event.getAuthor())
                     && e.getChannel().equals(event.getChannel())
                     && !e.getMessage().equals(event.getMessage()),
                (e) -> {
                    if(e.getMessage().getContentRaw().equalsIgnoreCase(this.code)){
                        channel.sendMessage("Uraaa! Ai raspuns corect! \uD83E\uDD73 " + e.getAuthor().getAsMention()).queue();
                    }
                    else {
                        channel.sendMessage("Raspuns gresit! \uD83D\uDE25 " + e.getAuthor().getAsMention() + " Documentatie: " + this.source).queue();
                    }
                },
                15, TimeUnit.SECONDS,
                () -> channel.sendMessage("Timpul a expirat! ⏰").queue()
                                    );
        });

    }

    @Override
    public String getName() {
        return "quiz";
    }

    @Override
    public String getHelp() {
        return "Primeste o intreabare random la care poti sa raspunzi cu ajutorul unor reactii cu emoji-uri";
    }
}

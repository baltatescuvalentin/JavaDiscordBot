import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.sql.Connection;
import net.dv8tion.jda.api.entities.TextChannel;


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

        try {

            Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/questionsdb", "root", "Varsator123B");

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select * from questions");

            while (resultSet.next()) {
                //System.out.println(resultSet.getString("question") + " " + resultSet.getString("answer"));
                String id = resultSet.getString("idquestion");
                String questiondb = resultSet.getString("question");
                String answerdb = resultSet.getString("answer");
                if(question.toString().equalsIgnoreCase(questiondb)){
                    gasit = true;
                    System.out.println(id + "/" + questiondb + "/" + answerdb);
                    channel.sendTyping().queue();
                    channel.sendMessage(answerdb).queue();
                }
            }

            if(gasit == false){
                channel.sendTyping().queue();
                channel.sendMessage("Nu stiu sa raspund! \uD83D\uDE25").queue();
            }

        } catch(Exception e){
            e.printStackTrace();
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

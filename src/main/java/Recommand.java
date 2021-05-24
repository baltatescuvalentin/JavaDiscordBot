import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class Recommand implements ICommand{

    private String dbName;
    private String genre;
    private String command;
    private String grade;
    private String name;
    private EmbedBuilder info;

    public void add(CommandContext ctx){

        try {

            Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/questionsdb", "root", "Varsator123B");

            Statement statement = connection.createStatement();

            String sql = "INSERT INTO " + this.dbName + " (nume, gen, nota, recomandat) VALUES (?,?,?,?)";

            PreparedStatement prepare = connection.prepareStatement(sql);
            prepare.setString(1, this.name);
            prepare.setString(2, this.genre);
            prepare.setString(3, this.grade);
            prepare.setString(4, ctx.getAuthor().getName());
            System.out.println(ctx.getAuthor().getName());

            prepare.executeUpdate();

            connection.close();

            ctx.getChannel().sendMessage("Am adaugat la lista " + ctx.getAuthor().getAsMention()).queue();

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void remove(CommandContext ctx){

        try {

            Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/questionsdb", "root", "Varsator123B");

            Statement statement = connection.createStatement();
            String sursa = "test";

            System.out.println(this.name);
            ResultSet resultSet = statement.executeQuery("select * from " + this.dbName);

            while(resultSet.next()){
                String nume = resultSet.getString("nume");
                if(nume.equalsIgnoreCase(this.name)){
                    sursa = resultSet.getString("recomandat");
                    break;
                }
            }

            System.out.println(sursa);

            System.out.println(ctx.getAuthor().getName());
            if(sursa.equalsIgnoreCase(ctx.getAuthor().getName())) {

                String sql = "DELETE FROM " + this.dbName + " WHERE nume = ?";

                PreparedStatement prepare = connection.prepareStatement(sql);
                prepare.setString(1, this.name);
                System.out.println(ctx.getAuthor().getName());

                prepare.executeUpdate();

                connection.close();

                ctx.getChannel().sendMessage("Am sters din lista " + ctx.getAuthor().getAsMention()).queue();
            } else {
                ctx.getChannel().sendMessage("Nu ai dreptul sa stergi acest element, nu este al tau " + ctx.getAuthor().getAsMention()).queue();
            }

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void show(CommandContext ctx){

        try {

            Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/questionsdb", "root", "Varsator123B");

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select * from " + this.dbName);


            this.info = new EmbedBuilder();
            while (resultSet.next()) {

                String nume = resultSet.getString("nume");
                String genre = resultSet.getString("gen");
                String grade = resultSet.getString("nota");
                String source = resultSet.getString("recomandat");

                this.info.setTitle("Lista " + this.dbName);
                this.info.setDescription("Recomandarile utilizatorilor de pe server");
                if(this.dbName.equalsIgnoreCase("muzica")){
                    this.info.addField(source, "Nume: " + nume + " Gen: `" + genre + "` Nota: `" + grade + "`", false);
                }
                else {
                    this.info.addField(source, "Nume: `" + nume + "` Gen: `" + genre + "` Nota: `" + grade + "`", false);
                }
                this.info.setColor(0xffffff);
            }

            ctx.getChannel().sendMessage(info.build()).queue();

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void input(CommandContext ctx){

        List<String> args = ctx.getArgs();

        this.command = args.get(0);

        if(command.equalsIgnoreCase("show")){
            this.dbName = args.get(1);
        } else if(command.equalsIgnoreCase("remove")){
            this.dbName = args.get(1);
            this.name = "";

            for (int i = 2; i < args.size(); i++) {
                if (i != args.size() - 1) {
                    this.name = this.name + args.get(i);
                    this.name = this.name + " ";
                } else {
                    this.name = this.name + args.get(i);
                }
            }
            System.out.println(this.name);
        } else {

            this.dbName = args.get(1);

            this.genre = args.get(2);

            this.grade = args.get(3);

            this.name = "";

            for (int i = 4; i < args.size(); i++) {
                if (i != args.size() - 1) {
                    this.name = this.name + args.get(i);
                    this.name = this.name + " ";
                } else {
                    this.name = this.name + args.get(i);
                }
            }

            System.out.println(this.command);
            System.out.println(this.dbName);
            System.out.println(this.genre);
            System.out.println(this.grade);
            System.out.println(this.name);
        }

        if(command.equalsIgnoreCase("add")){
            add(ctx);
        } else if(command.equalsIgnoreCase("remove")){
            remove(ctx);
        } else if(command.equalsIgnoreCase("show")){
            show(ctx);
        }
    }

    @Override
    public void handle(CommandContext ctx) {
        input(ctx);
    }

    @Override
    public String getHelp() {
        return "Un feature pentru a recomanda filme/carti/jocuri etc. in care se pot adauga/elimina elemente. " +
               "Sintaxa este `!recommand add/remove/show filme/jocuri/carti/muzica/seriale genre grade name`";
    }

    @Override
    public String getName() {
        return "recommand";
    }
}

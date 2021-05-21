import java.util.Random;

import net.dv8tion.jda.api.entities.TextChannel;

public class Slots implements ICommand{
    private String[] roll1;
    private String[] roll2;
    private String[] roll3;
    private String row1;
    private String row2;
    private String row3;
    private Random randomGenerator = new Random();

    public Slots(){
        roll1 = new String[]{":100:",":fire:",":ok_hand::skin-tone-5:",":flag_cu:",":clap::skin-tone-5:",
                             ":grapes:",":sweat_drops:",":eggplant:",":peach:",":ok:"};
        roll2 = new String[]{":ok_hand::skin-tone-5:",":sweat_drops:",":peach:",":flag_cu:",":100:",":fire:",":eggplant:",":clap::skin-tone-5:",
                             ":grapes:",":ok:"};
        roll3 = new String[]{":fire:",":eggplant:",":peach:",":flag_cu:",":clap::skin-tone-5:",
                             ":grapes:",":ok_hand::skin-tone-5:",":100:",":sweat_drops:",":ok:"};
    }

    public String pull(){
        String result =":slot_machine: Pacanele :slot_machine:\n";
        String winner="";
        row1="";
        row2="";
        row3="";
        int po1=randomGenerator.nextInt(10);
        int po2=randomGenerator.nextInt(10);
        int po3=randomGenerator.nextInt(10);
        row2=roll1[po1]+roll2[po2]+roll3[po3];
        if(roll1[po1].contentEquals(roll2[po2]) && roll2[po2].contentEquals(roll3[po3])){
                winner = winner + "\n*ZGOMOT*\nO sa castigi? Cine stie!";
             if((roll1[po1].equals(":flag_cu:")&&roll2[po2].equals(":flag_cu:")) && (roll2[po2].equals(":flag_cu:")&&roll3[po3].equals(":flag_cu:"))){
                 winner = winner + "\n*Masinaria se zguduie*\nNiste bani cubanezi!";
            }else if((roll1[po1].equals(":eggplant:")&&roll2[po2].equals(":eggplant:")) && (roll2[po2].equals(":eggplant:")&&roll3[po3].equals(":eggplant:"))){
                 winner = winner + "\n*Aparatu se misca*\nVanata :>";
            }else if((roll1[po1].equals(":peach:")&&roll2[po2].equals(":peach:")) && (roll2[po2].equals(":peach:")&&roll3[po3].equals(":peach:"))){
                 winner = winner + "\n*Se aud batai*\nUn cupon de '10% reducere'";
            }else if((roll1[po1].equals(":fire:")&&roll2[po2].equals(":fire:")) && (roll2[po2].equals(":fire:")&&roll3[po3].equals(":fire:"))){
                 winner = winner + "\n*'Ma iau caldurile'*\nNiste chibrituri ca esti 'on fire'";
            }else{
                 winner = winner + "\n*Zagazagazaga*\nCastig decent!";
            }
        }else{
            winner = "\n*Zgomot*\nAi pierdut.";
        }
        if(po1==0||po2==0||po3==0){
            if(po1==0){
                po1=10;
            }
            if(po2==0){
                po2=10;
            }
            if(po3==0){
                po3=10;
            }
        }
        row1=roll1[po1-1]+roll2[po2-1]+roll3[po3-1];
        if(po1>=9||po2>=9||po3>=9){
            if(po1>=9){
                po1=-1;
            }
            if(po2>=9){
                po2=-1;
            }
            if(po3>=9){
                po3=-1;
            }
        }
        row3=roll1[po1+1]+roll2[po2+1]+roll3[po3+1];
        result = result + row1+"\n"+row2+"\n"+row3+winner;
        return result;
    }

    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        String result = pull();
        channel.sendTyping().queue();
        channel.sendMessage(result).queue();
    }

    @Override
    public String getName() {
        return "slots";
    }

    @Override
    public String getHelp() {
        return "Pacanele";
    }
}
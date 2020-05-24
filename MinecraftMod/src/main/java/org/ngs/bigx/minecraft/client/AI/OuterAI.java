package org.ngs.bigx.minecraft.client.AI;

import net.minecraft.server.v1_15_R1.Tuple;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;
import java.util.concurrent.TimeUnit;



//import org.ngs.bigx.dictionary.objects.clinical.BiGXPatientInfo;
//import org.ngs.bigx.dictionary.objects.clinical.BiGXPatientPrescription;
//import org.ngs.bigx.minecraft.bike.BiGXPacketHandler;
import org.ngs.bigx.net.gameplugin.client.BiGXNetClient;
import scala.Int;

public class OuterAI extends QuestHeartRate{

    //Stores the quest/mini games that have been played and their avg heart rate
    public Map<Integer, String> quest_list;

    // After init_threshold numbers of heart rate transfer, the OuterAI will start to interact the game
    private Integer init_threshold= 300;
    // If the kids does not play any mini game with in time_threshold numbers of heart rate transfer,
    private Integer time_threshold= 300;
    // Count down the inactive time
    private Integer game_timer=0;
    //    private BiGXPatientInfo patientInfo= new BiGXPatientInfo();
//    TODO: List?
//    private List<BiGXPatientPrescription> prescriptions =patientInfo.getPrescriptions();
//    private BiGXPacketHandler biGXPacketHandler= new BiGXPacketHandler();
//    private Integer time=0;


    //Assign mini game AI to this variable
    public QuestHeartRate running_game;
    //Check the whether there is a mini game running or not
    public QuestStatus questStatus=QuestStatus.NONE;

    public OuterAI(){

    }

    public Integer getCurrent_heart_rate() {
        return current_heart_rate;
    }

    //Check whether the heart rate reach the goal
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event){
        //kids just started the game, don't modify

        Integer curr = (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        if (questStatus==QuestStatus.NONE) {
            if (time != curr) {
                time = curr;
                game_timer+=1;
                //kids just started the game, don't modify
                if (num_heart_rate< init_threshold){
                    ;
                }
                //if kids are doing nothing in outer world, pop up a quest
                else if( game_timer>time_threshold && !reach_target())
                {
                    pop_up_quest();
                }


            }
        }
        else if (questStatus==QuestStatus.RUNNING){
            game_timer=0;

        }
        else{
            System.out.println("Error: Invalid questStatus");
        }

    }
    //TODO: if the kids not in mini-game, and avg heart rate does not reach the goal for __ mins, pop up a quest
    public void pop_up_quest(){
        if(quest_list.size()==0){
            //Randomly generate a quest
        }
        else{
           String quest_name = quest_list.get(Collections.max(quest_list.keySet()));
           //TODO: spawn the quest, pop up hud may be?
        }
    }

    public enum QuestStatus {
        RUNNING,
        NONE
    }
}

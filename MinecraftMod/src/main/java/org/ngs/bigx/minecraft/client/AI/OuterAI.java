package org.ngs.bigx.minecraft.client.AI;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;



//import org.ngs.bigx.dictionary.objects.clinical.BiGXPatientInfo;
//import org.ngs.bigx.dictionary.objects.clinical.BiGXPatientPrescription;
//import org.ngs.bigx.minecraft.bike.BiGXPacketHandler;
import org.ngs.bigx.net.gameplugin.client.BiGXNetClient;
import scala.Int;

import java.util.List;

public class OuterAI extends QuestHeartRate{

    //Stores the quest/mini games that have been played and their avg heart rate
    private HashMap<String, Integer> quest_list;

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
        if (num_heart_rate< init_threshold){
            System.out.println();
        }
        Integer curr = (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        if (questStatus==QuestStatus.NONE) {
            if (time != curr) {
                time = curr;
                num_heart_rate += 1;
                //Handle return heart rate
                //            total_heart_rate+=biGXPacketHandler.Handle();
                //
                //            current_heart_rate=biGXPacketHandler.Handle();

            }
        }
        else if (questStatus==QuestStatus.RUNNING){

        }
        else{

        }

    }
    //TODO: if the kids not in mini-game, and avg heart rate does not reach the goal for __ mins, pop up a quest
    public void pop_up_quest(){

    }

    public enum QuestStatus {
        RUNNING,
        NONE
    }
}

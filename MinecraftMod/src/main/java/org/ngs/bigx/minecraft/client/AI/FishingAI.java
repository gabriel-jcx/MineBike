package org.ngs.bigx.minecraft.client.AI;

//import edu.ics.uci.minebike.minecraft.ClientUtils;
//import edu.ics.uci.minebike.minecraft.constants.EnumPacketClient;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class FishingAI extends QuestHeartRate {

    public String last_fish;
    //    public List<Float> heart_rate_per_fish;
//    private float avg_hr_last_fish;
    //2 retract(random), 0 false, 1 true
    public FishStatus fish_run_away = FishStatus.QUIT;
    public boolean hr_goal_reached = false;
    public int level = 1;
    private Fishpond pond= new Fishpond();
    private HashMap<String ,Integer> current_pond;
    Random random = new Random();
    Object[] crunchifyKeys;
    public FishingAI() {

    }

    public FishingAI(float h) {

    }
//    public void add_heart_rate(float h){
//        heart_rate.add(h);
//    }

    public void select_pond(int level) {
        switch (level) {
            case 1:
                System.out.println("easy");
                current_pond = pond.getEasy();

            case 2:
                System.out.println("medium");
                current_pond = pond.getMedium();
            case 3:
                System.out.println("hard");
                current_pond = pond.getHard();
        }
        crunchifyKeys = current_pond.keySet().toArray();
    }

    public Integer change_fish() {
        //Heart Rate reached
        if (fish_run_away == FishStatus.QUIT || last_fish==null) {
            Integer l=crunchifyKeys.length;
            return random_fish(l);
        }
        if (avg <= p.getTargetMax() && avg >= p.getTargetMin()) {
            //smaller fish
            if (fish_run_away == FishStatus.ESCAPE) {
//                int l=current_pond.get(last_fish);

                Integer l = Arrays.asList(crunchifyKeys).indexOf(last_fish);
                return random_fish(l);
//                String key = (String)crunchifyKeys[random.nextInt(l)];
//                last_fish= (String)key;
//                ClientUtils.sendData(EnumPacketClient.Fish,last_fish);
//                return current_pond.get(key);
            }
            else{
                Integer l=crunchifyKeys.length;
                return random_fish(l);
            }
        }
        //High,smaller fish
        else if (avg >= p.getTargetMax()) {
//            int l=pond.indexOf(last_fish);
            Integer l = Arrays.asList(crunchifyKeys).indexOf(last_fish);
            return random_fish(l);
//            int i = random.nextInt(l);
//            Pair<Integer, ItemStack> r = pond.get(i);
//            last_fish=r.getValue();
//            return r;
        }
        //Low,bigger fish
        else {
//            int l=pond.indexOf(last_fish);
//            int i = random.nextInt(pond.size()-l);
//            Pair<Integer, ItemStack> r = pond.get(l+i);
//            last_fish=r.getValue();
//            return r;
            Integer l = Arrays.asList(crunchifyKeys).indexOf(last_fish);
            Integer i= random.nextInt(crunchifyKeys.length-l);
            return random_fish(l+i);
        }
    }
    //tell the CustomHook fish name, return the resistance to FishingQuest
    private Integer random_fish(int l) {

        Object key = crunchifyKeys[random.nextInt(l)];
        last_fish= (String)key;
//      TODO:  ClientUtils.sendData(EnumPacketClient.Fish,last_fish);
        return current_pond.get(key);
    }
    public enum FishStatus {
        ESCAPE,
        CAUGHT,
        QUIT
    }

}


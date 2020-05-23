package org.ngs.bigx.minecraft.client.AI;

import java.util.HashMap;

public class Fishpond {
    HashMap<String,Integer> easy= new HashMap<>();
    HashMap<String,Integer> medium= new HashMap<>();
    HashMap<String,Integer> hard= new HashMap<>();

    public Fishpond(){

        easy_pond();
        medium_pond();
        hard_pond();

    }
    private void easy_pond(){
        easy.put("Bluegill", 0);
        easy.put("Perch", 0);
        easy.put("Gar", 0);
        easy.put("Bass", 1);
        easy.put("Muskellunge", 1);
        easy.put("Brown Trout", 1);
        easy.put("Catfish", 2);
        easy.put("Carp", 2);

    }
    private void medium_pond(){
        medium.put("Frog", 1);
        medium.put("Turtle", 2);
        medium.put("Leech", 2);
        medium.put("Piranh", 3);
        medium.put("Electric Eel", 3);
        medium.put("Tambaqui", 3);
        medium.put("Arapaima", 4);
        medium.put("Capitaine", 4);

    }
    private void hard_pond(){
        hard.put("Blowfish", 3);
        hard.put("Grouper", 4);
        hard.put("Salmon", 4);
        hard.put("Tuna", 4);
        hard.put("Halibut", 5);
        hard.put("Swordfish", 5);
        hard.put("Shark", 5);
        hard.put("Whale", 6);
    }

    public  HashMap<String,Integer> getEasy(){
        return easy;
    }

    public HashMap<String, Integer> getMedium() {
        return medium;
    }

    public HashMap<String, Integer> getHard() {
        return hard;
    }
}

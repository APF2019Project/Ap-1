package Shop;

import Creature.Plant;
import Creature.Zombie;
import User.User;
import com.google.gson.Gson;
import com.sun.tools.javac.Main;

import java.io.*;
import java.util.ArrayList;

public class Shop {
    private static ArrayList<Zombie> zombieList = new ArrayList<>();
    private static ArrayList<Plant> plantList = new ArrayList<>();


    public enum TypeOfFiles {
        ZombieCard,
        PlantCard
    }

    static
    {
        for (TypeOfFiles typeOfFiles : TypeOfFiles.values ( )) {
            File folder = new File (typeOfFiles.name ( ));
            File[] listOfFiles = folder.listFiles ( );
            if (!(listOfFiles == null)) {
                for (int i = 0; i < listOfFiles.length; i++) {
                    makeNewFromFile (listOfFiles[i].getPath ( ), typeOfFiles.toString ( ));
                }
            }
        }
    }

    public static void makeNewFromFile(String path, String type) {
        try {
            Gson gson = new Gson ( );
            InputStream input = new FileInputStream (path);
            Reader reader = new InputStreamReader (input);
            switch (type) {
                case "PlantCard":
                    Plant plant = gson.fromJson (reader, Plant.class);
                    plantList.add (plant);
                    break;
                case "ZombieCard":
                    Zombie zombie = gson.fromJson (reader, Zombie.class);
                    zombieList.add (zombie);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace ( );
        }
    }


    public static Plant makeNewPlantByName(String name) {
        Plant plant= checkNamePlant (name);
        TypeOfFiles typeOfFile= null;
        if(plant instanceof Plant){
            typeOfFile= TypeOfFiles.PlantCard;
        }
        if(plant != null){
            makeNewFromFile (typeOfFile.name ( )+"/"+plant.getName ()+".json",typeOfFile.toString ());
        }

        plantList.remove (plant);
        return plant;


    }

    public static Zombie makeNewZombieByName(String name) {
        Zombie zombie = checkNameZombie (name);
        TypeOfFiles typeOfFile = null;

        if (zombie instanceof Zombie) {
            typeOfFile = TypeOfFiles.ZombieCard;
        }

        if (zombie != null) {
            makeNewFromFile (typeOfFile.name() + "/" + zombie.getName() + ".json", typeOfFile.toString());
        }
        zombieList.remove (zombie);
        return zombie;
    }

    public static Zombie checkNameZombie(String name) {
        for (Zombie zombie : zombieList) {
            if (zombie.getName ( ).compareTo (name) == 0) {
                return zombie;
            }
        }
        return null;
    }

    public static Plant checkNamePlant(String name){
        for(Plant plant: plantList){
            if(plant.getName ().compareTo (name)== 0){
                return plant;
            }
        }
        return null;
    }

    public static void buy(String name, User user){
        boolean found = false;
        for(Plant plant : plantList){
            if(plant.getName ().compareTo(name) == 0){
                found = true;
                if(user.getCoins () >= plant.getPrice ()){
                    Plant newPlant = makeNewPlantByName (name);
                    user.getCollection ().getPlants ().add (newPlant);
                    user.setCoins (user.getCoins () - newPlant.getPrice ());
                    System.out.println ("you bought this plant successfully" );
                }
                else
                    System.out.println ("not enough money" );
                break;
            }
        }
        if(!found){
            for(Zombie zombie : zombieList){
                if(zombie.getName ().compareTo (name) == 0){
                    found = true;
                    if(user.getCoins () >= zombie.getPrice ()){
                        Zombie newZombie = makeNewZombieByName (name);
                        user.getCollection ().getZombies ().add (newZombie);
                        user.setCoins (user.getCoins () - newZombie.getPrice ());
                        System.out.println ("you bought this zombie successfully" );
                    }
                    else
                        System.out.println ("not enough money" );
                    break;
                }
            }
        }
       if(!found){
           System.out.println ("invalid name" );
       }
    }

    public static void buyForFree(String name , User user){
        boolean found = false;
        for(Plant plant : plantList){
            if(plant.getName ().compareTo(name) == 0){
                found = true;
                Plant newPlant = makeNewPlantByName (name);
                user.getCollection ().getPlants ().add (newPlant);
                break;
            }
        }
        if(!found){
            for(Zombie zombie : zombieList) {
                if (zombie.getName ( ).compareTo (name) == 0) {
                    found = true;
                    Zombie newZombie = makeNewZombieByName (name);
                    user.getCollection ( ).getZombies ( ).add (newZombie);
                    break;
                }
            }
        }
    }


    public ArrayList<Zombie> getZombieList() {
        return zombieList;
    }

    public ArrayList<Plant> getPlantList() {
        return plantList;
    }

    public static void setFirstCards(User user){
        buyForFree("Sunﬂower",user);
        buyForFree("Peashooter",user);
        buyForFree("SnowPea",user);
        buyForFree("Scaredy-shroom",user);
        buyForFree("Kernel-pult",user);
        buyForFree("Explode-o-nut",user);
        buyForFree("CherryBomb",user);
        buyForFree("Zombie",user);
        buyForFree("FootballZombie",user);
        buyForFree("Zomboni",user);
        buyForFree("ScreenDoorZombie",user);
        buyForFree("ConeheadZombie",user);
        buyForFree("BalloonZombie",user);

    }
}

package Map;

import Creature.Plant;
import Creature.Zombie;

import java.util.ArrayList;

public class Cell {
    ArrayList<Zombie> zombieContent = new ArrayList<>();
    ArrayList<Plant> plantContent = new ArrayList<>();
    CellKind cellKind;

    public ArrayList<Plant> getPlantContent() {
        return plantContent;
    }

    public ArrayList<Zombie> getZombieContent() {
        return zombieContent;
    }

    public void setCellKind(CellKind cellKind) {
        this.cellKind = cellKind;
    }

    public CellKind getCellKind() {
        return cellKind;
    }

    public void clearPlantContent(){ this.plantContent.clear();}
    public void setZombieContent(Zombie z){ this.zombieContent.add(z);}
    public void setPlantContent(Plant p){ this.plantContent.add(p);}
}


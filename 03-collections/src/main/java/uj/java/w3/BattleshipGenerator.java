package uj.java.w3;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public interface BattleshipGenerator {
    String generateMap();
    static BattleshipGenerator defaultInstance() {
        return new BattleShipGeneratorClass();
    }
}

record Cell(int x, int y){}

class Ship{
    int numberOfMasts;
    List<Cell> listMast;
    List<Cell> listNeighbors;
    int mapSize;
    char[][] map;

    public Ship(int numberOfMasts, char[][] map, int mapSize){
        this.numberOfMasts = numberOfMasts;
        listMast= new LinkedList<>();
        listNeighbors= new LinkedList<>();
        this.map = map;
        this.mapSize = mapSize;

        addFirstMast();

        while (listMast.size() != this.numberOfMasts){
            if(listNeighbors.size() != 0){
                addNextMast();
            } else {
                deleteShipFromMap();
                addFirstMast();
            }
        }
    }

    private void addFirstMast(){
        Random random = new Random();
        int randomX = random.nextInt(mapSize);
        int randomY = random.nextInt(mapSize);

        while (map[randomX][randomY] != '.' || !checkCorrectnessOfNeighbors(randomX, randomY)){
                randomX = random.nextInt(mapSize);
                randomY = random.nextInt(mapSize);
        }

        map[randomX][randomY] = '#';
        listMast.add(new Cell(randomX, randomY));
        addAllNeighborsToList(randomX, randomY);
    }

    private void addNextMast(){
        Random random = new Random();
        int len = listNeighbors.size();
        int randomElem = random.nextInt(len);
        int newX = listNeighbors.get(randomElem).x();
        int newY = listNeighbors.get(randomElem).y();
        listNeighbors.remove(randomElem);

        map[newX][newY] = '#';
        listMast.add(new Cell(newX, newY));

        addAllNeighborsToList(newX, newY);
    }

    private void deleteShipFromMap(){
        while(listMast.size() > 0){
            int len = listMast.size();
            int x = listMast.get(len - 1).x();
            int y = listMast.get(len - 1).y();
            listMast.remove(len - 1);
            map[x][y] = '.';
        }
    }

    private void addAllNeighborsToList(int x, int y) {
        checkAndAddNeighborToList(x - 1, y);
        checkAndAddNeighborToList(x + 1, y);
        checkAndAddNeighborToList(x, y + 1);
        checkAndAddNeighborToList(x, y - 1);
    }

    private void checkAndAddNeighborToList(int x, int y){
        if (x >= 0 && x < mapSize && y >= 0 && y < mapSize && !listNeighbors.contains(new Cell(x, y)) && !listMast.contains(new Cell(x, y)) && checkCorrectnessOfNeighbors(x, y)){
            listNeighbors.add(new Cell(x, y));
        }
    }


    private boolean checkCorrectnessOfNeighbors(int x, int y){
        return checkFreedomOfField(x - 1, y - 1) && checkFreedomOfField(x - 1, y) && checkFreedomOfField(x - 1, y + 1)
                && checkFreedomOfField(x, y - 1) && checkFreedomOfField(x, y + 1) && checkFreedomOfField(x - 1, y + 1)
                && checkFreedomOfField(x + 1, y - 1) && checkFreedomOfField(x + 1, y) && checkFreedomOfField(x + 1, y + 1);
    }

    private boolean checkFreedomOfField(int x, int y){
        return x < 0 || x >= mapSize || y < 0 || y >= mapSize || map[x][y] == '.' || listMast.contains(new Cell(x, y));
    }
}

class BattleShipGeneratorClass implements BattleshipGenerator {
    char[][] map;
    int mapSize = 10;
    public BattleShipGeneratorClass(){
        this.map = new char[mapSize][mapSize];
        for (int i = 0; i < mapSize; i++){
            for (int j = 0; j < mapSize; j++){
                map[i][j] = '.';
            }
        }
    }

    public String generateMap() {
        for (int k : new int[]{4, 3, 3, 2, 2, 2, 1, 1, 1, 1}) {
            new Ship(k, map, mapSize);
        }

        StringBuilder res = new StringBuilder();
        for (int i = 0; i < mapSize; i++){
            for (int j = 0; j < mapSize; j++){
                res.append(map[i][j]);
            }
        }
        return res.toString();
    }
}


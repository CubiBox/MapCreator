package fr.cubibox.com.mapcreator.map;

import fr.cubibox.com.mapcreator.Main;
import fr.cubibox.com.mapcreator.iu.Point;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import static fr.cubibox.com.mapcreator.map.Chunk.findChunkPols;

public class Map {
    private String idLevel;
    private Chunk[][] mapContent;

    private int mapSize;

    public Map(String name, float mapSize) {
        this.idLevel = name;
        this.mapSize = (int)mapSize;
        this.mapContent = new Chunk[this.mapSize/16][this.mapSize/16];
    }

    public Map(Chunk[][] mapContent, String name, float mapSize) {
        this.idLevel = name;
        this.mapSize = (int)mapSize;
        this.mapContent = mapContent;
    }

    public String exportMap(){
        int idPol = 0;
        for (Polygon pol : Main.getPolygons()) {
            pol.setId(String.valueOf(idPol));
            idPol ++;
        }

        String output = "idLevel\n"+mapSize+"\n";
        int xChunk = 0;
        int yChunk = 0;
        for (Chunk[] chunkL : mapContent){
            for (Chunk chunk : chunkL){
                output += "#"+xChunk+";"+yChunk+"\n";
                if (chunk.getPols() != null)
                    for (Polygon pol : chunk.getPols()){
                        output += "\t"+pol.toString();
                    }
                output += "!";
                xChunk++;
            }
            yChunk++;
            xChunk=0;
        }
        return output;
    }

    public static Map importMap(File f){
        //initialize Level Id
        String idLevel = "";
        boolean idLevelDone = false;

        //initialize Map Size
        int mapSize = 0;
        String mapS = "";
        boolean mapSizeDone = false;

        //Initialize Chunks
        String chunk = "";
        Chunk[][] chunks = new Chunk[0][0];
        int tempCX = 0;
        int tempCY = 0;

        //Initialize Polygons
        String poly = "";
        ArrayList<Edge> currentEdges = new ArrayList<>();
        ArrayList<Polygon> currentPolys = new ArrayList<>();
        float height;

        //Initialize Edges
        int tempX = 0;
        int tempY = 0;
        Point tempP;
        Point tempP2;

        String temp="";


        try{
            FileInputStream fis=new FileInputStream(f);
            int r;
            while((r=fis.read())!= -1){
                if (r != 32 && r != 9) {
                    temp = "";

                    //id Level
                    while (!idLevelDone) {
                        if ((char)r == '\n'|| (char)r == '\r') {
                            idLevelDone = true;
                            r=fis.read();
                            if ((char)r == '\n')   r=fis.read();
                            break;
                        }
                        else {
                            if ((char)r == '\n')   r=fis.read();
                            idLevel += (char) r;
                        }
                        r=fis.read();
                    }

                    //map Size
                    while (!mapSizeDone) {
                        if ((char)r == '\n' || (char)r == '\r') {
                            if ((char)r == '\n')   r=fis.read();
                            mapSizeDone = true;
                            mapSize = Integer.parseInt(mapS);
                            Main.setxSize(mapSize);
                            chunks = new Chunk[mapSize/16][mapSize/16];
                            break;
                        }
                        else {
                            if ((char)r == '\n')   r=fis.read();
                            mapS += (char)r;
                        }
                        r=fis.read();
                    }


                    //Chunk cord
                    if ((char)r == '#') {
                        r = fis.read();
                        temp = "";
                        while((char)r!=';') {
                            temp += (char)r;
                            r = fis.read();
                        }
                        tempCX = Integer.parseInt(temp);
                        r = fis.read();
                        temp = "";

                        while(!((char)r == '\n' || (char)r == '\r')) {
                            temp += (char)r;
                            r = fis.read();
                        }
                        if ((char)r == '\n')   r=fis.read();
                        tempCY = Integer.parseInt(temp);
                        temp = "";
                    }


                    //Polygone id
                    else if ((char)r == '$') {
                        r = fis.read();
                        while(!((char)r == '\n' || (char)r == '\r')) {
                            poly += (char)r;
                            r = fis.read();
                        }
                        if ((char)r == '\n')   r=fis.read();
                    }

                    //Edge
                    else if ((char)r == '@') {
                        temp = "";
                        r = fis.read();
                        r = fis.read();
                        while((char)r!='-') {
                            while((char)r!=';') {
                                temp += (char)r;
                                r = fis.read();
                            }
                            tempX = Integer.parseInt(temp);
                            temp = "";
                            r = fis.read();
                            while((char)r!=']') {
                                temp += (char)r;
                                r = fis.read();
                            }
                            tempY = Integer.parseInt(temp);
                            temp = "";
                            r = fis.read();
                        }
                        tempP = new Point(tempX, tempY);
                        r = fis.read();
                        r = fis.read();

                        while(!((char)r == '\n' || (char)r == '\r')) {
                            while((char)r!=';') {
                                temp += (char)r;
                                r = fis.read();
                            }
                            tempX = Integer.parseInt(temp);
                            temp = "";
                            r = fis.read();
                            while((char)r!=']') {
                                temp += (char)r;
                                r = fis.read();
                            }
                            tempY = Integer.parseInt(temp);
                            temp = "";
                            r = fis.read();
                        }
                        if ((char)r == '\n')   r=fis.read();
                        tempP2 = new Point(tempX, tempY);
                        currentEdges.add(new Edge(tempP,tempP2));
                    }

                    //Height polygon / finish it
                    if ((char)r == '%') {
                        temp = "";
                        r = fis.read();
                        while(!((char)r == '\n' || (char)r == '\r')) {
                            temp += (char)r;
                            r = fis.read();
                        }
                        if ((char)r == '\n')   r=fis.read();
                        height = Float.parseFloat(temp);
                        ArrayList<Point> tempPointsArray = new ArrayList<>();
                        for (Edge e : currentEdges){
                            tempPointsArray.add(e.getA());
                        }
                        boolean isLine = false;
                        float tmpX1 = currentEdges.get(0).getA().getX();
                        float tmpY1 = currentEdges.get(0).getA().getY();
                        float tmpX2 = currentEdges.get(currentEdges.size()-1).getB().getX();
                        float tmpY2 = currentEdges.get(currentEdges.size()-1).getB().getY();
                        if (!(tmpX1 == tmpX2 && tmpY1 == tmpY2)){
                            isLine = true;
                            tempPointsArray.add(currentEdges.get(currentEdges.size()-1).getB());
                        }
                        else {
                            isLine = false;
                        }
                        currentPolys.add(new Polygon(currentEdges, tempPointsArray, height, poly, isLine));
                        poly = "";
                        currentEdges= new ArrayList<>();
                    }

                    //Finish the currentChunk
                    if (r=='!'){
                        chunks[tempCY][tempCX] = new Chunk(currentPolys, tempCX, tempCY);
                        currentPolys = new ArrayList<>();
                    }
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("failed");
        }
        Main.setMap(new Map(chunks, idLevel, mapSize));
        return new Map(chunks, idLevel, mapSize);
    }

    public int getMapSize() {
        return mapSize;
    }

    public void setMapSize(int mapSize) {
        this.mapSize = mapSize;
    }

    public String getIdLevel() {
        return idLevel;
    }

    public void setIdLevel(String idLevel) {
        this.idLevel = idLevel;
    }

    public Chunk[][] getMapContent() {
        return mapContent;
    }

    public void setMapContent(Chunk[][] mapContent) {
        this.mapContent = mapContent;
    }
}

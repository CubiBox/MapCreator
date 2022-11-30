package fr.cubibox.com.mapcreator.map;

import fr.cubibox.com.mapcreator.Main;
import fr.cubibox.com.mapcreator.graphics.Texture;
import fr.cubibox.com.mapcreator.maths.Line2F;
import fr.cubibox.com.mapcreator.maths.Vector2F;
import fr.cubibox.com.mapcreator.maths.Polygon2F;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import static fr.cubibox.com.mapcreator.map.Chunk.findChunkPols;

public class Map {
    private int size;
    private String levelID;
    private Chunk[][] chunks;


    public Map(String name, float mapSize) {
        this.levelID = name;
        this.size = (int)mapSize;
        this.chunks = new Chunk[this.size /16][this.size /16];
    }

    public Map(Chunk[][] mapContent, String name, float mapSize) {
        this.levelID = name;
        this.size = (int)mapSize;
        this.chunks = mapContent;
    }

    public String exportMap() {
        return null;
    }
/*
    public String exportMap(){
        int idPol = 0;
        for (Polygon2F pol : Main.getPolygons()) {
            pol.setId(String.valueOf(idPol));
            idPol ++;
        }

        String output = "idLevel\n"+ size +"\n";
        int xChunk = 0;
        int yChunk = 0;
        for (Chunk[] chunkL : chunks){
            for (Chunk chunk : chunkL){
                output += "#"+xChunk+";"+yChunk+"\n";
                if (chunk.getPols() != null)
                    for (Polygon2F pol : chunk.getPols()){
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

 */

    public static Map importMap(File f) {
        return null;
    }
    /*
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
        ArrayList<Line2F> currentEdges = new ArrayList<>();
        ArrayList<Polygon2F> currentPolys = new ArrayList<>();
        float height;

        //Initialize Edges
        int tempX = 0;
        int tempY = 0;
        Vector2F tempP;
        Vector2F tempP2;

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
                        tempP = new Vector2F(tempX, tempY);
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
                        tempP2 = new Vector2F(tempX, tempY);
                        currentEdges.add(new Line2F(tempP,tempP2));
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
                        ArrayList<Vector2F> tempPointsArray = new ArrayList<>();
                        for (Line2F e : currentEdges){
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
                        currentPolys.add(new Polygon2F(currentEdges, tempPointsArray, height, poly));
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
*/
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getLevelID() {
        return levelID;
    }

    public void setLevelID(String levelID) {
        this.levelID = levelID;
    }

    public Chunk[][] getChunks() {
        return chunks;
    }

    public void setChunks(Chunk[][] chunks) {
        this.chunks = chunks;
    }
}

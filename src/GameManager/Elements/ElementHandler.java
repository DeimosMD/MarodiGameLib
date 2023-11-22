package GameManager.Elements;

import java.util.ArrayList;

public class ElementHandler {

    public static ArrayList<Start> startScripts = new ArrayList<>();
    public static ArrayList<Update> updateScripts = new ArrayList<>();
    public static ArrayList<Draw> drawScripts = new ArrayList<>();

    // Called at the first tick
    public static void start() {
        if (startScripts != null)
            for (Start script : startScripts) {
                script.start();
            }
    }

    // Called every tick after the first tick
    public static void update() {
        if (updateScripts != null)
            for (Update script : updateScripts) {
                script.update();
            }
    }

    // Called once every frame
    public static void draw() {
        if (drawScripts != null)
            for (Draw script : drawScripts) {
                script.draw();
            }
    }

    public static void addStartScript(Start s) {
        startScripts.add(s);
    }

    public static void addUpdateScript(Update u) {
        updateScripts.add(u);
    }

    public static void addDrawScript(Draw d) {
        drawScripts.add(d);
    }

    public static void removeStartScript(Start s) {
        int indexToRemove = startScripts.indexOf(s);
        if (indexToRemove < startScripts.size()) {
            startScripts.remove(indexToRemove);
            for (int i = indexToRemove; i < startScripts.size(); i++) {
                startScripts.set(i, startScripts.get(i + 1));
            }
            startScripts.remove(startScripts.size() - 1);
        } else {
            System.out.println("Invalid index: " + indexToRemove);
        }
    }

    public static void removeUpdateScript(Update u) {
        int indexToRemove = updateScripts.indexOf(u);
        if (indexToRemove < updateScripts.size()) {
            updateScripts.remove(indexToRemove);
            for (int i = indexToRemove; i < updateScripts.size(); i++) {
                updateScripts.set(i, updateScripts.get(i + 1));
            }
            updateScripts.remove(updateScripts.size() - 1);
        } else {
            System.out.println("Invalid index: " + indexToRemove);
        }
    }

    public static void removeDrawScript(Draw d) {
        int indexToRemove = drawScripts.indexOf(d);
        if (indexToRemove < drawScripts.size()) {
            drawScripts.remove(indexToRemove);
            for (int i = indexToRemove; i < drawScripts.size(); i++) {
                drawScripts.set(i, drawScripts.get(i + 1));
            }
            drawScripts.remove(drawScripts.size() - 1);
        } else {
            System.out.println("Invalid index: " + indexToRemove);
        }
    }
}
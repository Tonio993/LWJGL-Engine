package engine.items;

import engine.graph.FontTexture;
import engine.graph.Material;
import engine.graph.Mesh;
import utils.Arrays;

import java.util.ArrayList;
import java.util.List;

public class TextItem extends GameItem {

    private static final float ZPOS = 0.0f;

    private static final int VERTICES_PER_QUAD = 4;

    private String text;

    private FontTexture fontTexture;

    public TextItem(String text, FontTexture fontTexture) throws Exception {
        super();
        this.text = text;
        this.fontTexture = fontTexture;
        this.setMesh(buildMesh());
    }

    private Mesh buildMesh() {
        List<Float> positions = new ArrayList<>();
        List<Float> textCoords = new ArrayList<>();
        float[] normals   = new float[0];
        List<Integer> indices   = new ArrayList<>();

        char[] characters = text.toCharArray();
        int numChars = characters.length;

        float startX = 0.0f;
        for(int i=0; i<numChars; i++) {
            FontTexture.CharInfo charInfo = fontTexture.getCharInfo(characters[i]);

            // Build a character tile composed by two triangles

            // Left Top vertex
            positions.add(startX); // x
            positions.add(0.0f); //y
            positions.add(ZPOS); //z
            textCoords.add((float) charInfo.startX() / (float) fontTexture.getWidth());
            textCoords.add(0.0f);
            indices.add(i*VERTICES_PER_QUAD);

            // Left Bottom vertex
            positions.add(startX); // x
            positions.add((float) fontTexture.getHeight()); //y
            positions.add(ZPOS); //z
            textCoords.add((float) charInfo.startX() / (float) fontTexture.getWidth() );
            textCoords.add(1.0f);
            indices.add(i*VERTICES_PER_QUAD + 1);

            // Right Bottom vertex
            positions.add(startX + charInfo.width()); // x
            positions.add((float) fontTexture.getHeight()); //y
            positions.add(ZPOS); //z
            textCoords.add((float) (charInfo.startX() + charInfo.width()) / (float) fontTexture.getWidth());
            textCoords.add(1.0f);
            indices.add(i*VERTICES_PER_QUAD + 2);

            // Right Top vertex
            positions.add(startX + charInfo.width()); // x
            positions.add(0.0f); //y
            positions.add(ZPOS); //z
            textCoords.add((float) (charInfo.startX() + charInfo.width()) / (float) fontTexture.getWidth());
            textCoords.add(0.0f);
            indices.add(i*VERTICES_PER_QUAD + 3);

            // Add indices for left top and bottom right vertices
            indices.add(i*VERTICES_PER_QUAD);
            indices.add(i*VERTICES_PER_QUAD + 2);

            startX += charInfo.width();
        }

        float[] posArr = Arrays.toArray(positions);
        float[] textCoordsArr = Arrays.toArray(textCoords);
        int[] indicesArr = indices.stream().mapToInt(i->i).toArray();
        Mesh mesh = new Mesh(posArr, textCoordsArr, normals, indicesArr);
        mesh.setMaterial(new Material(fontTexture.getTexture()));
        return mesh;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.getMesh().deleteBuffers();
        this.setMesh(buildMesh());
    }
}
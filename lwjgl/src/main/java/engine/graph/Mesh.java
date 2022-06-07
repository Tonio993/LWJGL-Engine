package engine.graph;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Mesh {

    public final int vaoId;
    public final int posVboId;
    public final int colorVboId;
    public final int idxVboId;
    public final int vertex;

    public Mesh(float[] positions, float[] colors, int[] index) {
        FloatBuffer vertexBuffer = null;
        FloatBuffer colorBuffer = null;
        IntBuffer indexBuffer = null;
        try {
            vertexBuffer = memAllocFloat(positions.length);
            vertex = index.length;
            vertexBuffer.put(positions).flip();

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            posVboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, posVboId);
            glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
            glBindBuffer(GL_ARRAY_BUFFER, 0);

            colorVboId = glGenBuffers();
            colorBuffer = memAllocFloat(colors.length);
            colorBuffer.put(colors).flip();
            glBindBuffer(GL_ARRAY_BUFFER, colorVboId);
            glBufferData(GL_ARRAY_BUFFER, colorBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

            idxVboId = glGenBuffers();
            indexBuffer = memAllocInt(index.length);
            indexBuffer.put(index).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);

            glBindVertexArray(0);
        } finally {
            memFree(vertexBuffer);
            memFree(colorBuffer);
            memFree(indexBuffer);
        }
    }

    public void render() {
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, vertex, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }

    public void cleanUp() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        // Delete the VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(posVboId);
        glDeleteBuffers(colorVboId);
        glDeleteBuffers(idxVboId);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

}

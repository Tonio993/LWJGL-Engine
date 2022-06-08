package engine.graph;

import de.matthiasmann.twl.utils.PNGDecoder;
import utils.Files;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL33.*;

public class Texture {

    public final int id;

    public Texture(String file) throws IOException {

        // Legge il contenuto del file PNG
        PNGDecoder decoder = new PNGDecoder(Files.inputStream(file));

        // Crea un buffer di byte e inserisce al suo interno il contenuto del PNG
        ByteBuffer buf = ByteBuffer.allocateDirect(decoder.getWidth() * decoder.getHeight() * 4);
        decoder.decode(buf, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
        buf.flip();

        // Crea una texture e salva l'identificativo
        id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);

        // Informa OpenGL che ogni componente della texture ha dimensione di 1 byte
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

        // Esegue il caricamento della texture
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buf);
        glGenerateMipmap(GL_TEXTURE_2D);

    }

    public void cleanup() {
        glDeleteTextures(id);
    }

}

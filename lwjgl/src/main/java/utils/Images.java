package utils;

import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.stb.STBImage.*;

public final class Images {

    private Images() {}

    public static ImageInfo load(String fileName) throws Exception {
        return new ImageInfo(fileName);
    }

    public static class ImageInfo {
        public final ByteBuffer buffer;
        public final int width;
        public final int height;

        public ImageInfo(String fileName) throws Exception {
            // Load Texture file
            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer w = stack.mallocInt(1);
                IntBuffer h = stack.mallocInt(1);
                IntBuffer channels = stack.mallocInt(1);

                byte[] fileBytes = Files.bytes(fileName);
                ByteBuffer imageBuffer = ByteBuffer.allocateDirect(fileBytes.length);
                imageBuffer.put(fileBytes);
                imageBuffer.flip();

                buffer = stbi_load_from_memory(imageBuffer, w, h, channels, 4);
                if (buffer == null) {
                    throw new Exception("Image file [" + fileName + "] not loaded: " + stbi_failure_reason());
                }
                width = w.get();
                height = h.get();
            }
        }

        public void free() {
            stbi_image_free(buffer);
        }
    }
}

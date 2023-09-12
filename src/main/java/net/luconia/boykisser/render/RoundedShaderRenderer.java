package net.luconia.boykisser.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

/**
 * Rounded Shader Renderer
 *
 * @author shoroa
 */
public class RoundedShaderRenderer {

    private static int programID;

    private static int program;

    private static RoundedShaderRenderer instance;

    public static RoundedShaderRenderer getInstance() {
        if (instance == null) {
            programID = glCreateProgram();
            program = glCreateProgram();

            ResourceLocation fragTextureLocation = new ResourceLocation("client/shader/rounded_texture.frag");
            ResourceLocation fragLocation = new ResourceLocation("client/shader/rounded.frag");
            ResourceLocation vertexLocation = new ResourceLocation("client/shader/vertex.vert");

            int fragTextureID, fragID, vertexID;
            try {
                Minecraft mc = Minecraft.getMinecraft();
                fragTextureID = createShader(mc.getResourceManager().getResource(fragTextureLocation).getInputStream(), GL_FRAGMENT_SHADER);
                fragID = createShader(mc.getResourceManager().getResource(fragLocation).getInputStream(), GL_FRAGMENT_SHADER);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                vertexID = createShader(Minecraft.getMinecraft().getResourceManager().getResource(vertexLocation).getInputStream(), GL_VERTEX_SHADER);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            glAttachShader(programID, fragTextureID); //attach fragment shader to program
            glAttachShader(programID, vertexID); //attach vertex shader to program
            glLinkProgram(programID); //link program

            int status = glGetProgrami(programID, GL_LINK_STATUS);
            if (status == 0) {
                throw new IllegalStateException("Shader failed to link!");
            }

            glAttachShader(program, fragID);
            glAttachShader(program, vertexID);
            glLinkProgram(program);

            status = glGetProgrami(program, GL_LINK_STATUS);

            if (status == 0) {
                throw new IllegalStateException("Shader failed to link!");
            }

            instance = new RoundedShaderRenderer();
        }
        return instance;
    }

    public void drawRoundedTexturedRect(int x, int y, int width, int height, float uMin, float uMax, float vMin, float vMax, float radius) {
        GlStateManager.resetColor();
        GlStateManager.enableBlend();
        glUseProgram(programID);
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        getInstance().setUniformFloat(programID, "position", x * sr.getScaleFactor(), (Minecraft.getMinecraft().displayHeight - (height * sr.getScaleFactor())) - (y * sr.getScaleFactor()));
        getInstance().setUniformFloat(programID,"size", width * sr.getScaleFactor(), height * sr.getScaleFactor());
        getInstance().setUniformFloat(programID,"radius", radius * sr.getScaleFactor());

        glBegin(GL_QUADS);
        glTexCoord2f(uMin, vMin);
        glVertex2f(x - 1, y - 1);
        glTexCoord2f(uMin, vMax);
        glVertex2f(x - 1, y + height + 1);
        glTexCoord2f(uMax, vMax);
        glVertex2f(x + width + 1, y + height + 1);
        glTexCoord2f(uMax, vMin);
        glVertex2f(x + width + 1, y - 1);
        glEnd();
        glUseProgram(0);
        GlStateManager.disableBlend();
        GlStateManager.resetColor();
    }

    public void drawRound(float x, float y, float width, float height, float radius, Color color) {
        GlStateManager.resetColor();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        glUseProgram(program);

        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        getInstance().setUniformFloat(program, "loc", x * sr.getScaleFactor(), (Minecraft.getMinecraft().displayHeight - (height * sr.getScaleFactor())) - (y * sr.getScaleFactor()));
        getInstance().setUniformFloat(program, "size", width * sr.getScaleFactor(), height * sr.getScaleFactor());
        getInstance().setUniformFloat(program, "radius", radius * sr.getScaleFactor());
        getInstance().setUniformFloat(program, "color", color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);

        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex2f(x - 1, y - 1);
        glTexCoord2f(0, 1);
        glVertex2f(x - 1, y + height + 1);
        glTexCoord2f(1, 1);
        glVertex2f(x + width + 1, y + height + 1);
        glTexCoord2f(1, 0);
        glVertex2f(x + width + 1, y - 1);
        glEnd();
        glUseProgram(0);
        GlStateManager.disableBlend();
    }

    private static int createShader(InputStream inputStream, int shaderType) throws IOException {
        int shader = glCreateShader(shaderType);
        glShaderSource(shader, readStreamToString(inputStream));
        glCompileShader(shader);

        int compiled = glGetShaderi(shader, GL_COMPILE_STATUS);
        if (compiled == 0) {
            System.err.println(glGetShaderInfoLog(shader, glGetShaderi(shader, GL_INFO_LOG_LENGTH)));
            throw new IllegalStateException("Failed to compile shader");
        }
        return shader;
    }

    private static String readStreamToString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[512];
        int read;
        while ((read = inputStream.read(buffer, 0, buffer.length)) != -1) {
            out.write(buffer, 0, read);
        }
        return new String(out.toByteArray(), StandardCharsets.UTF_8);
    }

    public void setUniformFloat(int program, String name, float... arguments) {
        int loc = glGetUniformLocation(program, name);
        switch (arguments.length) {
            case 1:
                glUniform1f(loc, arguments[0]);
                break;
            case 2:
                glUniform2f(loc, arguments[0], arguments[1]);
                break;
            case 3:
                glUniform3f(loc, arguments[0], arguments[1], arguments[2]);
                break;
            case 4:
                glUniform4f(loc, arguments[0], arguments[1], arguments[2], arguments[3]);
                break;
        }
    }
}
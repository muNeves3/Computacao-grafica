package teste_opengl.teste_opengl;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class LeituraObj {
    public static void main(String[] args) {
        int windowWidth = 500;
        int windowHeight = 500;
        String nomeArquivo = args[0];

        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Falha ao inicializar o GLFW");
        }

        long window = GLFW.glfwCreateWindow(1000, 1000, "Teste LWJGL 3", MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL) {
            throw new RuntimeException("Falha ao criar a janela");
        }

        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwSwapInterval(1);

        GL.createCapabilities();

        Mesh mesh = new Mesh();
        mesh.LeituraObj(nomeArquivo);
        Vertice pivo = mesh.getPivo();

        Camera camera = new Camera();
        glEnable(GL_DEPTH_TEST);

        float aspect = (float) windowWidth / (float) windowHeight;
        camera.setFormatoCamera(45.0f, aspect, 0.1f, 5000.0f);
        camera.set(
                new Camera.Vetor3D((float) pivo.x, (float) pivo.y, (float) pivo.z + 50.0f),
                new Camera.Vetor3D((float) pivo.x, (float) pivo.y, (float) pivo.z),
                new Camera.Vetor3D(0.0f, 1.0f, 0.0f));

        GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        GLFW.glfwSetCharCallback(window, (win, codepoint) -> {
            char tecla = (char) codepoint;
            float passoZoom = 5.0f;
            float passoAngulo = 5.0f;

            switch (tecla) {
                case 'Z' -> camera.slide(0, 0, passoZoom);
                case 'z' -> camera.slide(0, 0, -passoZoom);
                case 'P' -> camera.pitch(-passoAngulo);
                case 'p' -> camera.pitch(passoAngulo);
                case 'R' -> camera.roll(-passoAngulo);
                case 'r' -> camera.roll(passoAngulo);
                case 'Y' -> camera.yaw(-passoAngulo);
                case 'y' -> camera.yaw(passoAngulo);
            }
        });

        GLFW.glfwSetKeyCallback(window, (win, key, scancode, action, mods) -> {
            float passoZoom = 20.0f;

            if (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT) {
                switch (key) {
                    case GLFW.GLFW_KEY_1 -> camera.slide(0, passoZoom, 0);
                    case GLFW.GLFW_KEY_2 -> camera.slide(0, -passoZoom, 0);
                    case GLFW.GLFW_KEY_3 -> camera.slide(passoZoom, 0, 0);
                    case GLFW.GLFW_KEY_4 -> camera.slide(-passoZoom, 0, 0);
                    case GLFW.GLFW_KEY_5 -> camera.slide(0, 0, passoZoom);
                    case GLFW.GLFW_KEY_6 -> camera.slide(0, 0, -passoZoom);
                    case GLFW.GLFW_KEY_ESCAPE -> GLFW.glfwSetWindowShouldClose(win, true);
                }
            }
        });

        while (!GLFW.glfwWindowShouldClose(window)) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            GL11.glPointSize(8.0f);
            GL11.glColor3f(1.0f, 0.0f, 0.0f);

            camera.setMatrizModelView();

            glPushMatrix();
            mesh.desenharWireframe();
            glPopMatrix();

            GLFW.glfwSwapBuffers(window);
            GLFW.glfwPollEvents();
        }

        org.lwjgl.glfw.Callbacks.glfwFreeCallbacks(window);
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
    }
}

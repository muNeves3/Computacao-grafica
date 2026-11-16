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

        long window = GLFW.glfwCreateWindow(1000, 1000, "Agora eu sou uma imagem 3D!", MemoryUtil.NULL,
                MemoryUtil.NULL);
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

            double angX = 0.0, angY = 0.0, angZ = 0.0;
            double fatorEscala = 1;
            boolean transformou = false;
            double fatorTranslacao = 20.0;
            double anguloRotacao = 20.0;

            // Eixo X
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
                angX -= anguloRotacao;
                transformou = true;
            }
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
                angX += anguloRotacao;
                transformou = true;
            }

            // Eixo Y
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
                angY -= anguloRotacao;
                transformou = true;
            }
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
                angY += anguloRotacao;
                transformou = true;
            }

            // Eixo Z
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_Q) == GLFW.GLFW_PRESS) {
                angZ += anguloRotacao;
                transformou = true;
            }
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_E) == GLFW.GLFW_PRESS) {
                angZ -= anguloRotacao;
                transformou = true;
            }

            // Escala
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_I) == GLFW.GLFW_PRESS) {
                fatorEscala = 1.02;
                transformou = true;
            }
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_O) == GLFW.GLFW_PRESS) {
                fatorEscala = 0.98;
                transformou = true;
            }

            // Translação
            double dx = 0.0, dy = 0.0, dz = 0.0;
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_UP) == GLFW.GLFW_PRESS) {
                dy += fatorTranslacao;
                transformou = true;
            }
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_DOWN) == GLFW.GLFW_PRESS) {
                dy -= fatorTranslacao;
                transformou = true;
            }
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT) == GLFW.GLFW_PRESS) {
                dx -= fatorTranslacao;
                transformou = true;
            }
            if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT) == GLFW.GLFW_PRESS) {
                dx += 5.0;
                transformou = true;
            }

            if (transformou) {
                mesh.aplicarTransformacaoComposta(
                        dx, dy, dz,
                        angX, angY, angZ,
                        fatorEscala, fatorEscala, fatorEscala);
            }

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

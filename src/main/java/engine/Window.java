package engine;

/*

public final class ClassSingleton
{

    private static ClassSingleton INSTANCE;
    private String info = "Initial info class";

    private ClassSingleton() {}

    public static ClassSingleton getInstance()
    {
        if(INSTANCE == null)
            INSTANCE = new ClassSingleton();

        return INSTANCE;
    }

    // other methods: getters and setters
}

*/

import engine.primitives.*;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;


public class Window
{
    int width, height;
    String name;

    private long glfwWindow; // memory address of window (very "C" ish)
    private static Window window = null; // again only ever 1 instance

    private PhysicsEngine physicsEngine;

    private Window() // only ever 1 needed so constructor is private
    {
        this.width = 1280;
        this.height = 900;
        this.name = "Basic 2D Physics";

        this.physicsEngine = new PhysicsEngine();
    }

    public static Window get()
    {
        if (Window.window == null)
            Window.window = new Window();

        return Window.window;
    }

    // https://www.lwjgl.org/guide
    public void run()
    {
        System.out.println("LWJGL Verison: " + Version.getVersion());

        init();
        loop();
    }

    private void init()
    {
        GLFWErrorCallback.createPrint(System.err).set(); // set callbacks for errors (input etc)

        if (!glfwInit()) // check for errors in init
            throw new IllegalStateException("Unable to init GLFW...");

        glfwDefaultWindowHints(); // uses these hints after creating window context
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // make hints invisible until window is done being created
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // make resizable

        glfwWindow = glfwCreateWindow(this.width, this.height, this.name, NULL, NULL); //  do not care about monitor or sharing so they are null
        if (glfwWindow == NULL)
            throw new IllegalStateException("Unable to create GLFW Window");

        glfwMakeContextCurrent(glfwWindow); // set current window context
        glfwSwapInterval(1); // swap every frame (enable v-sync)

        glfwShowWindow(glfwWindow); // make visible (all of this is using pointers by passing the memory address

        // makes GLFWs bindings usable thru LWJGL
        GL.createCapabilities();

        // setup callbacks
        glfwSetMouseButtonCallback(glfwWindow, this::mouseButtonCallback);
    }

    private void loop()
    {
        while(!glfwWindowShouldClose(glfwWindow))
        {
            glfwPollEvents(); // setup event polling

            glClearColor(0.0f, 0.0f, 0.0f, 1.0f); // set screen to black
            glClear(GL_COLOR_BUFFER_BIT);

            physicsEngine.update();
            physicsEngine.render();

            glfwSwapBuffers(glfwWindow);
        }
    }

    // CALLBACKS !!! For processing input!
    private void mouseButtonCallback(long window, int button, int action, int mods)
    {
        if ((button == GLFW_MOUSE_BUTTON_LEFT || button == GLFW_MOUSE_BUTTON_RIGHT)
                && action == GLFW_PRESS)
        {
            try (MemoryStack stack = stackPush())
            {
                DoubleBuffer pX = stack.mallocDouble(1);
                DoubleBuffer pY = stack.mallocDouble(1);

                glfwGetCursorPos(window, pX, pY);

                double x = pX.get(0);
                double y = pY.get(0);

                float normalizedX = (float)((x / width) * 2 - 1);
                float normalizedY = (float)-((y / height) * 2 - 1);

                if (button == GLFW_MOUSE_BUTTON_LEFT)
                    physicsEngine.addCircle(new Circle(normalizedX, normalizedY, 0.05f));
                else if (button == GLFW_MOUSE_BUTTON_RIGHT)
                    physicsEngine.addRectangle(new Rectangle(normalizedX, normalizedY, 0.5f, 0.1f));
            }
        }
    }
}

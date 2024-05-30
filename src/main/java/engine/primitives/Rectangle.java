package engine.primitives;

import static org.lwjgl.opengl.GL11.*;

public class Rectangle extends Primitive
{
    private float width, height;

    public Rectangle(float x, float y, float width, float height)
    {
        super(x, y);
        this.width = width;
        this.height = height;
    }

    public float getWidth() { return width; }
    public float getHeight() { return height; }

    @Override
    public void render()
    {
        glBegin(GL_QUADS);
        glVertex2f(x, y);
        glVertex2f(x + width, y);
        glVertex2f(x + width, y + height);
        glVertex2f(x, y + height);
        glEnd();
    }
}

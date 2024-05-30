package engine.primitives;

import static org.lwjgl.opengl.GL11.*;

public class Circle extends Primitive
{
    private float radius;
    private float mass;
    private int shapeResolution = 50;

    public Circle(float x, float y, float radius)
    {
        super(x, y);
        this.radius = radius;
        this.mass = radius * radius * (float) Math.PI;
    }

    public void update(float gravity)
    {
        yVel += gravity;
        y += yVel;
        x += xVel;

        // collision detection with floor (bottom boundary)
        if (y - radius < -1.0f)
        {
            y = -1.0f + radius;
            yVel *= -0.8f; // basic collision response (reverse and dampen velocity)
        }
        // top boundary
        if (y + radius > 1.0f)
        {
            y = 1.0f - radius;
            yVel *= -0.8f;
        }
        // left boundary
        if (x - radius < -1.0f)
        {
            x = -1.0f + radius;
            xVel *= -0.8f;
        }
        // right boundary
        if (x + radius > 1.0f)
        {
            x = 1.0f - radius;
            xVel *= -0.8f;
        }
    }

    public void checkCollisionWithRectangle(Rectangle rectangle)
    {
        if (x + radius > rectangle.getX() && x - radius < rectangle.getX() + rectangle.getWidth() &&
                y + radius > rectangle.getY() && y - radius < rectangle.getY() + rectangle.getHeight())
        {

            // simple collision response -> move the circle out of the rectangle and reverse the velocity
            if (y > rectangle.getY() + rectangle.getHeight() / 2)
            {
                y = rectangle.getY() + rectangle.getHeight() + radius;
            } else {
                y = rectangle.getY() - radius;
            }
            yVel *= -0.8f;
        }
    }

    public void checkCollisionWithCircle(Circle other) // started having to use this.
    {                                                  // convention because I was using x and y for different objects
        float dx = other.x - this.x;
        float dy = other.y - this.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        float minDistance = this.radius + other.radius;

        if (distance < minDistance)
        {
            float overlap = 0.5f * (distance - minDistance); // after detecting, resolve overlap!
            this.x -= overlap * (dx / distance);
            this.y -= overlap * (dy / distance);
            other.x += overlap * (dx / distance);
            other.y += overlap * (dy / distance);

            // adj all velocities
            float vx = this.yVel - other.yVel;
            float vy = this.yVel - other.yVel;
            float dotProduct = (dx * vx + dy * vy) / distance;

            float collisionScale = dotProduct / (distance * distance);
            float collisionX = dx * collisionScale;
            float collisionY = dy * collisionScale;

            collisionX /= 10;
            collisionY /= 10;

            this.xVel -= collisionX;
            this.yVel -= collisionY;
            other.xVel += collisionX;
            other.yVel += collisionY;
        }
    }

    @Override
    public void render()
    {
        double theta = 2 * Math.PI / shapeResolution;
        double cos = Math.cos(theta);
        double sin = Math.sin(theta);
        double cx = radius;
        double cy = 0;

        glBegin(GL_POLYGON);
        for (int i = 0; i < shapeResolution; i++)
        {
            glVertex2d(x + cx, y + cy);

            double temp = cx;
            cx = cos * cx - sin * cy;
            cy = sin * temp + cos * cy;
        }
        glEnd();
    }
}

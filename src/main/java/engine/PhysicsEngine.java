package engine;

import engine.primitives.*;

import java.util.ArrayList;
import java.util.List;

public class PhysicsEngine
{
    private List<Circle> circles;
    private List<Rectangle> rectangles;

    private final float gravity = -0.00098f;

    public PhysicsEngine()
    {
        this.circles = new ArrayList<>();
        this.rectangles = new ArrayList<>();

        this.rectangles.add(new Rectangle(-1.0f, -1.0f, 2.0f, 0.01f)); // bottom
        this.rectangles.add(new Rectangle(-1.0f, 1.0f - 0.01f, 2.0f, 0.01f)); // top
        this.rectangles.add(new Rectangle(-1.0f, -1.0f, 0.01f, 2.0f)); // left
        this.rectangles.add(new Rectangle(1.0f - 0.01f, -1.0f, 0.01f, 2.0f)); // right
    }

    public void addCircle(Circle obj)
    {
        circles.add(obj);
    }

    public void addRectangle(Rectangle obj)
    {
        rectangles.add(obj);
    }

    public void update()
    {
        for (int i = 0; i < circles.size(); i++)
        {
            Circle circle = circles.get(i);
            circle.update(gravity);

            for (Rectangle rectangle : rectangles)
            {
                circle.checkCollisionWithRectangle(rectangle); // update then check rectangle collision
            }

            for (int j = i + 1; j < circles.size(); j++)
            {
                Circle other = circles.get(j); // iterate thru all other circles
                circle.checkCollisionWithCircle(other);
            }
        }
    }

    public void render()
    {
        for (Circle circle : circles)
        {
            circle.render();
        }
        for (Rectangle rectangle : rectangles)
        {
            rectangle.render();
        }
    }
}

package engine.primitives;

public abstract class Primitive
{
    protected float x, y; // initial pos (x,y)
    protected float xVel, yVel;
    protected float collisionCoeff;

    public Primitive(float x, float y)
    {
        this.x = x;
        this.y = y;
        this.xVel = this.yVel = 0.0f; // initial velocity is zero
        this.collisionCoeff = -0.8f;
    }

    public float getX() { return x; }
    public float getY() { return y; }

    public abstract void render();
}

public class Rectangle
{
    private final Point TOP_LEFT;
    private final Point BOTTOM_RIGHT;

    public Rectangle (Point topLeft, Point bottomRight) {
        if(!(topLeft.equals(bottomRight))) {
            TOP_LEFT = topLeft;
            BOTTOM_RIGHT = bottomRight;
        }
        else {
            TOP_LEFT = new Point(0.0, 1.0);
            BOTTOM_RIGHT = new Point (1.0, 0.0);
            System.out.println("Invalid inputs, default vertices assigned...Unit Rectangle in quadrant I");
        }
    }
    public Point getTopLeft() {
        return TOP_LEFT;
    }
    public Point getBottomRight() {
        return BOTTOM_RIGHT;
    }
}

package target;

public class CommonShape {
	double x, y;
	
	CommonShape(double x, double y) {
        this.x = x;
        this.y = y;
    }
	
	public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    
    public double distance(CommonShape s) {
        double dx = Math.abs(s.getX() - x);
        double dy = Math.abs(s.getY() - y);
        return Math.sqrt(dx * dx + dy * dy);
    }
}

public class UAVDrawParameter {
    private int PointX;
    private int PointY;
    private int PointZ;
    private int Radius;

    public UAVDrawParameter(int PointX,int PointY,int PointZ,int Radius){
        this.PointX=PointX;
        this.PointY=PointY;
        this.PointZ=PointZ;
        this.Radius=Radius;
    }

    public int getPointX() {
        return PointX;
    }

    public void setPointX(int pointX) {
        PointX = pointX;
    }

    public int getPointY() {
        return PointY;
    }

    public void setPointY(int pointY) {
        PointY = pointY;
    }

    public int getPointZ() {
        return PointZ;
    }

    public void setPointZ(int pointZ) {
        PointZ = pointZ;
    }

    public int getRadius() {
        return Radius;
    }

    public void setRadius(int radius) {
        Radius = radius;
    }
}

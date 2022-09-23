//障碍物
public class Obstacle {

    private double UL_PositionX;
    private double UL_PositionY;
    private double UR_PositionX;
    private double UR_PositionY;
    private double DL_PositionX;
    private double DL_PositionY;
    private double DR_PositionX;
    private double DR_PositionY;
    private double Width;
    private double Height;

    public Obstacle(double UL_PositionX, double UL_PositionY, double UR_PositionX, double UR_PositionY,
                           double DL_PositionX, double DL_PositionY, double DR_PositionX, double DR_PositionY,
                           double Width, double Height) {
        this.UL_PositionX = UL_PositionX;
        this.UL_PositionY = UL_PositionY;
        this.UR_PositionX = UR_PositionX;
        this.UR_PositionY = UR_PositionY;
        this.DL_PositionX = DL_PositionX;
        this.DL_PositionY = DL_PositionY;
        this.DR_PositionX = DR_PositionX;
        this.DR_PositionY = DR_PositionY;
        this.Width = Width;
        this.Height = Height;

    }

    public double getUL_PositionX() {
        return UL_PositionX;
    }

    public void setUL_PositionX(double UL_PositionX) {
        this.UL_PositionX = UL_PositionX;
    }

    public double getUL_PositionY() {
        return UL_PositionY;
    }

    public void setUL_PositionY(double UL_PositionY) {
        this.UL_PositionY = UL_PositionY;
    }

    public double getUR_PositionX() {
        return UR_PositionX;
    }

    public void setUR_PositionX(double UR_PositionX) {
        this.UR_PositionX = UR_PositionX;
    }

    public double getUR_PositionY() {
        return UR_PositionY;
    }

    public void setUR_PositionY(double UR_PositionY) {
        this.UR_PositionY = UR_PositionY;
    }

    public double getDL_PositionX() {
        return DL_PositionX;
    }

    public void setDL_PositionX(double DL_PositionX) {
        this.DL_PositionX = DL_PositionX;
    }

    public double getDL_PositionY() {
        return DL_PositionY;
    }

    public void setDL_PositionY(double DL_PositionY) {
        this.DL_PositionY = DL_PositionY;
    }

    public double getDR_PositionX() {
        return DR_PositionX;
    }

    public void setDR_PositionX(double DR_PositionX) {
        this.DR_PositionX = DR_PositionX;
    }

    public double getDR_PositionY() {
        return DR_PositionY;
    }

    public void setDR_PositionY(double DR_PositionY) {
        this.DR_PositionY = DR_PositionY;
    }

    public double getWidth() {
        return Width;
    }

    public void setWidth(double width) {
        Width = width;
    }

    public double getHeight() {
        return Height;
    }

    public void setHeight(double height) {
        Height = height;
    }
}


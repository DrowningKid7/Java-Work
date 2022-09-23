public class UAVParameter {
    private double PositionX;
    private double PositionY;
    private double PositionZ;
    private double SpeedX;
    private double SpeedY;
    private double SpeedZ;
    private double AccelerationX;
    private double AccelerationY;
    private double AccelerationZ;
    private double Quality;
    private double Radius;

    public UAVParameter(double PositionX, double PositionY, double PositionZ,
                        double SpeedX, double SpeedY, double SpeedZ, double Quality, double Radius) {
        this.PositionX = PositionX;
        this.PositionY = PositionY;
        this.PositionZ = PositionZ;
        this.SpeedX = SpeedX;
        this.SpeedY = SpeedY;
        this.SpeedZ = SpeedZ;
        this.Quality = Quality;
        this.Radius = Radius;
    }

    public double getPositionX() {
        return PositionX;
    }

    public void setPositionX(double positionX) {
        PositionX = positionX;
    }

    public double getPositionY() {
        return PositionY;
    }

    public void setPositionY(double positionY) {
        PositionY = positionY;
    }

    public double getPositionZ() {
        return PositionZ;
    }

    public void setPositionZ(double positionZ) {
        PositionZ = positionZ;
    }

    public double getSpeedX() {
        return SpeedX;
    }

    public void setSpeedX(double speedX) {
        SpeedX = speedX;
    }

    public double getSpeedY() {
        return SpeedY;
    }

    public void setSpeedY(double speedY) {
        SpeedY = speedY;
    }

    public double getSpeedZ() {
        return SpeedZ;
    }

    public void setSpeedZ(double speedZ) {
        SpeedZ = speedZ;
    }

    public double getAccelerationX() {
        return AccelerationX;
    }

    public void setAccelerationX(double accelerationX) {
        AccelerationX = accelerationX;
    }

    public double getAccelerationY() {
        return AccelerationY;
    }

    public void setAccelerationY(double accelerationY) {
        AccelerationY = accelerationY;
    }

    public double getAccelerationZ() {
        return AccelerationZ;
    }

    public void setAccelerationZ(double accelerationZ) {
        AccelerationZ = accelerationZ;
    }

    public double getQuality() {
        return Quality;
    }

    public void setQuality(double quality) {
        Quality = quality;
    }

    public double getRadius() {
        return Radius;
    }

    public void setRadius(double radius) {
        Radius = radius;
    }
}

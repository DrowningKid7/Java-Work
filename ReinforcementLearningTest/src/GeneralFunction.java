import java.util.Vector;

class GeneralFunction {

    public static void InitializationAction() {


    }

    //二维空间上两点距离
    public static double GetPointY(double Y) {
        double PointY = GeneralData.MyPanelHeight - Y;
        return PointY;
    }

    //二维空间上两点距离
    public static double Get2DVectorValue(double X1, double Y1, double X2, double Y2) {
        double ValueSquare = (X1 - X2) * (X1 - X2) + (Y1 - Y2) * (Y1 - Y2);
        double Value = Math.sqrt(ValueSquare);
        return Value;
    }

    //二维向量的膜
    public static double Get2DVectorValue(double ValueX, double ValueY) {
        double ValueSquare = ValueX * ValueX + ValueY * ValueY;
        double Value = Math.sqrt(ValueSquare);
        return Value;
    }

    //二维空间向量与Y轴正方向的夹角：射线上的点横坐标，射线上的点纵坐标，射线原点横坐标，射线原点纵坐标（-Pi~Pi）
    public static double Get2DVectorAngle(double X1, double Y1, double X2, double Y2) {
        double DeltaX = X1 - X2;
        double DeltaY = Y1 - Y2;
        double Angle = Get2DVectorAngle(DeltaX, DeltaY);
        return Angle;
    }

    //二维空间向量与Y轴正方向的夹角（-Pi~Pi）原点引出的向量
    public static double Get2DVectorAngle(double X, double Y) {
        double Angle = 0;
        if (X == 0) {
            if (Y > 0) {
                Angle = 0;
            } else {
                Angle = Math.PI;
            }
        } else if (Y == 0) {
            if (X > 0) {
                Angle = -Math.PI / 2;
            } else {
                Angle = Math.PI / 2;
            }
        } else {
            double DeltaValue = GeneralFunction.Get2DVectorValue(X, Y);
            if (DeltaValue != 0) {
                Angle = Math.asin(Y / DeltaValue) - Math.PI / 2;
            }
            if (X < 0) {
                Angle = -Angle;
            }
        }
        return Angle;
    }

    //二维空间向量与Y轴正方向的夹角（-Pi~Pi）
    public static double Get2DVectorAngle(double X, double Y, double Value) {
        double Angle = 0;
        if (X == 0) {
            if (Y > 0) {
                Angle = 0;
            } else {
                Angle = Math.PI;
            }
        } else if (Y == 0) {
            if (X > 0) {
                Angle = -Math.PI / 2;
            } else {
                Angle = Math.PI / 2;
            }
        } else {
            if (Value != 0) {
                Angle = Math.asin(Y / Value) - Math.PI / 2;
            }
            if (X < 0) {
                Angle = -Angle;
            }
        }
        return Angle;
    }

    //标准化夹角，将角度调整到（-Pi~Pi）
    public static double NormalizedIncludedAngle(double Angle) {
        Angle = Angle % (Math.PI * 2);
        if (Math.abs(Angle) > Math.PI) {
            if (Angle > 0) {
                Angle = Angle - Math.PI * 2;
            } else {
                Angle = Angle + Math.PI * 2;
            }
        }
        return Angle;
    }

    //二维空间两向量的夹角（-Pi~Pi）射线1上的点的横坐标，射线1上的点的纵坐标，射线2上的点的横坐标，射线2上的点的纵坐标，射线原点的横坐标，射线原点的纵坐标
    //符号表示射线1逆时针+或顺时针-旋转后与射线2重合
    public static double Get2DVectorIncludedAngle(double X1, double Y1, double X2, double Y2, double X3, double Y3) {

        double Angle1 = Get2DVectorAngle(X1, Y1, X3, Y3);
        double Angle2 = Get2DVectorAngle(X2, Y2, X3, Y3);
        double IncludedAngle = Get2DVectorIncludedAngle(Angle1, Angle2);
        return IncludedAngle;
    }

    //二维空间两向量的夹角（-Pi~Pi）射线上的点的横坐标，射线上的点的纵坐标，射线原点的横坐标，射线原点的纵坐标，另一条共原点射线的角度
    //符号表示射线1逆时针+或顺时针-旋转后与射线2重合
    public static double Get2DVectorIncludedAngle(double X1, double Y1, double X2, double Y2, double Angle) {
        Angle = NormalizedIncludedAngle(Angle);
        double LineAngle = Get2DVectorAngle(X1, Y1, X2, Y2);
        double IncludeAngle = Get2DVectorIncludedAngle(LineAngle, Angle);
        return IncludeAngle;
    }

    //二维空间两向量的夹角（-Pi~Pi）射线1的偏角，射线2的偏角
    //符号表示射线1逆时针+或顺时针-旋转后与射线2重合
    public static double Get2DVectorIncludedAngle(double Angle1, double Angle2) {
        Angle2 = NormalizedIncludedAngle(Angle2);
        Angle1 = NormalizedIncludedAngle(Angle1);
        double IncludeAngle;
        if (Angle1 == 0) {
            IncludeAngle = Angle2;
        } else if (Angle2 == 0) {
            IncludeAngle = -Angle1;
        } else if (Angle1 * Angle2 > 0) {
            IncludeAngle = Angle2 - Angle1;
        } else {
            IncludeAngle = Math.abs(Angle1 - Angle2);
            if (IncludeAngle > Math.PI) {
                if (Angle2 > Angle1) {
                    IncludeAngle = IncludeAngle - Math.PI * 2;
                } else {
                    IncludeAngle = Math.PI * 2 - IncludeAngle;
                }

            } else {
                IncludeAngle = Angle2 - Angle1;
            }
        }
        return IncludeAngle;
    }

    //射线旋转，端点旋转
    public static double[] RadialRotationCoordinateTransformation
    (double CenterPointX, double CenterPointY, double Radius, double RotationAngle) {
        //顶点
        double VertexPointX = CenterPointX - Radius * Math.sin(RotationAngle);
        double VertexPointY = CenterPointY + Radius * Math.cos(RotationAngle);
        double[] Point = {VertexPointX, VertexPointY};
        return Point;
    }

   //判断点是否在多边形障碍内部
//    public static boolean IsContainsPolygon(double PositionX, double PositionY, Vector<VertexData> VertexDataVector) {
//        int VertexNumber = VertexDataVector.size();
//        boolean Result = false;
//        int i = 0;
//        for (int j = VertexNumber - 1; i < VertexNumber; j = i++) {
//            if (VertexDataVector.get(i).getVertexY() > PositionY != VertexDataVector.get(j).getVertexY() > PositionY
//                    && PositionX < (VertexDataVector.get(j).getVertexX() - VertexDataVector.get(i).getVertexX())
//                    * (PositionY - VertexDataVector.get(i).getVertexY())
//                    / (VertexDataVector.get(j).getVertexY() - VertexDataVector.get(i).getVertexY()) + VertexDataVector.get(i).getVertexX()) {
//                Result = !Result;
//            }
//        }
//        return Result;
//    }

    //点到直线的距离，点横坐标，点纵坐标，直线经过的点横坐标，直线经过的点纵坐标，直线夹角
    public static double GetPointToLineDistance(double PointX, double PointY,
                                                double LinePointX1, double LinePointY1, double LinePointX2, double LinePointY2) {
        double Distance;
        if (LinePointX1 == LinePointX2) {
            Distance = PointX - LinePointX1;
        } else if (LinePointY1 == LinePointY2) {
            Distance = PointY - LinePointY2;
        } else {
            //直线：AX+BY+C=0；
            double A = (LinePointY1 - LinePointY2) / (LinePointX1 - LinePointX2);
            double B = -1;
            double C = (LinePointX1 * LinePointY2 - LinePointX2 * LinePointY1) / (LinePointX1 - LinePointX2);
            Distance = (A * PointX + B * PointY + C) / GeneralFunction.Get2DVectorValue(A, B);
        }
        return Distance;
    }
}

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class LinkGamePanel extends JPanel {

    private final List<ImagePoint> imagePoints;
    private final List<Point> avaliable;
    private int imageWidth;
    private int imageHeigh;
    private int startX = 50;
    private int endX = 500;
    private int startY = 50;
    private int endY = 500;
    //    private int startX = 400;
//    private int endX =  500;
//    private int startY = 400;
//    private int endY = 500;
    private List<Point> path;
    private MouseEvent e = null;
    private int clickTimes = 0;
    private Point[] clickPoints;
    private Image[] images = new Image[23];
    private List<List<Point>> paths = new ArrayList<>();


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            {
                long pass = System.currentTimeMillis();
                if (e == null) {
                    return;
                }
                int i ,j;
                synchronized (e) {
                    i = (e.getX() - startX) / imageWidth;
                    j = (e.getY() - startY) / imageHeigh;
                }
                clickPoints[clickTimes] = new Point(i, j);
                if (clickTimes == 1) {
                    ImagePoint cur = getImagePoint(clickPoints[0].getX(), clickPoints[0].getY());
                    ImagePoint target = getImagePoint(clickPoints[1].getX(), clickPoints[1].getY());
                    if (cur.getId() != target.getId() || cur.getPoint().equals(target.getPoint())) {
                        return;
                    }
                    if (isLinked(clickPoints[0], clickPoints[1], avaliable, path)) {
                        System.out.println("path size " + path.size());
                        avaliable.add(clickPoints[0]);
                        avaliable.add(clickPoints[1]);
                        removeImagePoint(clickPoints[0].getX(), clickPoints[0].getY());
                        removeImagePoint(clickPoints[1].getX(), clickPoints[1].getY());
                        repaint();
                    }
                    clickTimes = 0;
                    long now = System.currentTimeMillis();
                    System.out.println("last-----------------  " + (now - pass));
                    e = null;
                    return;
                }
                clickTimes++;
                e = null;
            }
        }
    };

    public LinkGamePanel() {
        this.setBackground(new Color(127, 174, 252));

        for (int i = 0; i < images.length; i++) {
            String strFmt = "image/pieces/%d.gif";

            try {
                images[i] = ImageIO.read(new File(String.format(strFmt, i)));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        path = new ArrayList<>();
        imageHeigh = images[0].getHeight(null);
        imageWidth = images[0].getWidth(null);

        avaliable = new ArrayList<>();
        for (int i = startX - imageWidth; i < endX + imageWidth; i += imageWidth) {
            for (int j = startY - imageHeigh; j < endY + imageHeigh; j += imageHeigh) {
                if (i >= startX && i <= endX &&
                        j >= startY && j <= endY) {
                    continue;
                }
                int x = (i - startX) / imageWidth;
                int y = (j - startY) / imageHeigh;
                avaliable.add(new Point(x, y));
            }
        }
        clickPoints = new Point[2];

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                super.mouseClicked(e);
                e = mouseEvent;
                System.out.println(e);
                new Thread(runnable).start();

            }
        });


        Random r = new Random();
        imagePoints = new ArrayList<>();
        for (int x = startX; x < endX; x += imageWidth) {
            for (int y = startY; y < endY; y += imageHeigh) {
                int id = 1 + r.nextInt(22);
                Image img = images[id];
                imagePoints.add(new ImagePoint(x, y, id, img));
            }
        }
    }


    public void removeImagePoint(int i, int j) {
        ImagePoint imagePoint = getImagePoint(i, j);
        if (imagePoint != null) {
            imagePoints.remove(imagePoint);
        }
    }

    public ImagePoint getImagePoint(int i, int j) {
        for (ImagePoint imagePoint : imagePoints) {
            Point p = imagePoint.getPoint();
            if ((p.getX() - startX) / imageWidth == i &&
                    (p.getY() - startY) / imageHeigh == j) {
                return imagePoint;
            }
        }
        return null;
    }

    public void print() {
        System.out.println(this.getHeight());
        System.out.println(this.getWidth());
    }

    public boolean isLinked(Point current, Point target, List<Point> avaliable, List<Point> path) {

        if (isAdjoin(current, target)) {
            path.add(current);
            path.add(target);
            draw(path);
            return true;
        }

//        if(isLinedRecursion(current, target, path, avaliable)){
//            draw(path);
//            repaint();
//            return true;
//        }
        paths.clear();
        path.clear();
        isLinedRecursionAllVersion1(current, target, path, avaliable);
        if (!paths.isEmpty()) {
            List<Point> p = paths.get(0);
            for (int i = 0; i < paths.size(); i++) {
                if (i == 0) {
                    continue;
                }
                List<Point> tmp = paths.get(i);
                if (p.size() > tmp.size()) {
                    p = tmp;
                }
            }

            for (int i = 0; i < p.size(); i++) {
                try {
                    path.add(p.get(i).clone());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }

        return false;
    }

    private void draw(List<Point> paths) {
        if (paths.isEmpty()) {
            Exception e = new Exception();
            e.printStackTrace();
            return;
        }
        Iterator<Point> it = paths.iterator();
        Point cur = it.next();
        Point next = it.next();
        draw(cur, next);
        while (it.hasNext()) {
            System.out.println(cur);
            cur = next;
            next = it.next();
            draw(cur, next);
        }
    }

    private void draw(Point cur, Point next) {
        Graphics g = getGraphics();
        int sx = cur.getX() * imageWidth + startX + imageWidth / 2;
        int ex = next.getX() * imageWidth + startX + imageWidth / 2;
        int sy = cur.getY() * imageHeigh + startY + imageHeigh / 2;
        int ey = next.getY() * imageHeigh + startY + imageHeigh / 2;
        g.setColor(Color.red);
        g.drawLine(sx, sy, ex, ey);
    }

    private boolean isAdjoin(Point current, Point target) {
        List<Point> neighbors = current.getNeighbor();
        if (neighbors == null) {
            return false;
        } else {
            for (int i = 0; i < neighbors.size(); i++) {
                if (target.isNeighbor(neighbors.get(i))) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isLinedRecursion(Point current, Point target, List<Point> paths, List<Point> available) {
        paths.add(current);

        if (isAdjoin(current, target)) {
            paths.add(target);
            return true;
        }
        List<Point> neighbors = current.getNeighbor(available);
        for (int i = 0; i < neighbors.size(); i++) {
            available.remove(neighbors.get(i));
            if (isLinedRecursion(neighbors.get(i), target, paths, available)) {
                available.add(neighbors.get(i));
                return true;
            }
            available.add(neighbors.get(i));
        }
        paths.remove(current);
        return false;
    }

    private void isLinedRecursionAll(Point current, Point target, List<Point> path, List<Point> available) {
        path.add(current);

        if (isAdjoin(current, target)) {
            path.add(target);
            List<Point> copy = new ArrayList<>();
            for (int i = 0; i < path.size(); i++) {
                try {
                    copy.add(path.get(i).clone());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
            paths.add(copy);
            path.remove(target);
            return;
        }
        List<Point> neighbors = current.getNeighbor(available);
        for (int i = 0; i < neighbors.size(); i++) {
            available.remove(neighbors.get(i));
            isLinedRecursionAll(neighbors.get(i), target, path, available);
            available.add(neighbors.get(i));
        }
        path.remove(current);
    }


    private void isLinedRecursionAllVersion1(Point current, Point target, List<Point> path, List<Point> available) {
        boolean isFlag = true;
        path.add(current);
        if (!available.contains(current)) {
            isFlag = false;
        }
        if (isFlag)
            available.remove(current);

//        System.out.println("#path + " + path.size());
//        for (int i = 0; i < path.size(); i++) {
//            System.out.print(path.get(i) + "  ");
//        }
//        System.out.println();
//        System.out.println("#avai + " + available.size());
//
//        for (int i = 0; i < available.size(); i++) {
//            System.out.print(available.get(i) + "  ");
//        }
//        System.out.println("");


        if (isAdjoin(current, target)) {
            path.add(target);
            List<Point> copy = new ArrayList<>();
            for (int i = 0; i < path.size(); i++) {
                try {
                    copy.add(path.get(i).clone());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
            paths.add(copy);
            path.remove(target);
            path.remove(current);
            if (isFlag)
                available.add(current);
            System.out.println("find");
            return;
        }
        List<Point> neighbors = current.getNeighbor(available);
        for (int i = 0; i < neighbors.size(); i++) {
            isLinedRecursionAllVersion1(neighbors.get(i), target, path, available);
        }
        path.remove(current);
        if (isFlag)
            available.add(current);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        System.out.println("-----------------paint-------------------");
        for (int i = 0; i < imagePoints.size(); i++) {
            ImagePoint imagePoint = imagePoints.get(i);
            Point p = imagePoint.getPoint();
            g.drawImage(imagePoint.getImage(), p.getX(), p.getY(), null);
        }
        if (!path.isEmpty()) {
            System.out.println("size " + path.size());
            draw(path);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        path.clear();
    }

    class ImagePoint {
        private Point point;
        private Image image;
        private int id;


        public ImagePoint(int x, int y, int id, Image image) {
            this.point = new Point(x, y);
            this.image = image;
            this.id = id;
        }

        public Point getPoint() {
            return point;
        }

        public void setPoint(Point point) {
            this.point = point;
        }

        public Image getImage() {
            return image;

        }

        public void setImage(Image image) {
            this.image = image;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    class Point {
        private int x;
        private int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }

        @Override
        public Point clone() throws CloneNotSupportedException {
            return new Point(x, y);
        }

        public java.util.List<Point> getNeighbor() {
            Point pl = new Point(x - 1, y);
            Point pd = new Point(x, y + 1);
            Point pr = new Point(x + 1, y);
            Point pu = new Point(x, y - 1);
            List<Point> collects = new ArrayList<>();
            collects.add(pl);
            collects.add(pr);
            collects.add(pu);
            collects.add(pd);
            return collects;
        }

        public java.util.List<Point> getNeighbor(java.util.List<Point> avaliable) {
            List<Point> collects = getNeighbor();
            if (avaliable == null) {
                return null;
            }

            for (Iterator<Point> it = collects.iterator(); it.hasNext(); ) {
                Point p = it.next();
                if (!avaliable.contains(p)) {
                    it.remove();
                }
            }
            return collects;
        }

        @Override
        public boolean equals(Object obj) {
            Point target = (Point) obj;
            if (!(obj instanceof Point)) {
                return false;
            }

            if (x == target.x && y == target.y) {
                return true;
            } else {
                return false;
            }
        }

        public boolean isNeighbor(Point point) {
            return this.equals(point);
        }
    }

    class Block {

        private Point point;
        private int id;

        public Block(int x, int y, int id) {
            point = new Point(x, y);
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Point getPoint() {
            return point;
        }
    }

}

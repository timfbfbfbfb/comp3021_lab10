package lab10;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class LettersUI extends Application {

    final static int SCENE_WIDTH = 300;
    final static int SCENE_HEIGHT = 150;
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private boolean oneIsVisible = false;

    public static void main(String[] args) {
        launch(LettersUI.class, args);
    }

    @Override
    public void start(Stage stage) {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10); // Gap between nodes

        String[] string_thread = {"A", "B", "C", "D", "E"};
        for (String s : string_thread) {
            Text text = new Text(s);
            text.setFont(new Font(50));
            text.setVisible(false);
            hbox.getChildren().addAll(text);
        }

        Scene scene = new Scene(hbox, SCENE_WIDTH, SCENE_HEIGHT);
        stage.setScene(scene);
        stage.setTitle("Example");
        stage.show();

        ArrayList<Thread> arrayList = new ArrayList<>();
        // Since now you have to add 3 more Threads, Can we use oneIsVisible for loop instead ?
        for (int i = 0; i < string_thread.length; i++) {
            Thread t1 = new Thread(new MyTask((Text) hbox.getChildren().get(i), this));
            t1.setDaemon(true);
            arrayList.add(t1);
            t1.start();
        }
    }

    public void showText(Text text, boolean show) {
        // the parameter show tells if the text has to appear o disappear
        if (show) {
            lock.lock();
            try {
                while (oneIsVisible) {
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                text.setVisible(true);
                oneIsVisible =true;
            } finally {
                lock.unlock();
            }
        } else {
            lock.lock();
            try {
                text.setVisible(false);
                oneIsVisible = false;
                condition.signal();
            } finally {
                lock.unlock();
            }
        }
    }

    class MyTask implements Runnable {

        int timeBudgetms = 5 * 60 * 1000;
        LettersUI instance;
        Text text;

        public MyTask(Text text, LettersUI zeroOROne) {
            this.text = text;
            this.instance = zeroOROne;
        }

        @Override
        public void run() {
            boolean show = true;
            long startTime = System.currentTimeMillis();
            while ((startTime + timeBudgetms) > System.currentTimeMillis()) {
                instance.showText(text, show);
                show = !show;
                try {
                    Random rn = new Random();
                    int range = 3000 - 1000 + 1;
                    int randomNum = rn.nextInt(range) + 1000;
                    Thread.yield();
                    Thread.sleep(randomNum);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


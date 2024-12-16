package Man;

public class Timer {
    int t1 = 4;
    int t2 = 4;
    int t3 = 4;
    int t4 = 4;
    int time = 120;

    public void calctime() {
        if (time <= 0) {
            time = 120;
            t4++;

            if (t4 == 14) {
                t3++;
                t4 = 4;
            }

            if (t3 == 10) {
                t2++;
                t3 = 4;
            }
            if (t2 == 14) {
                t1++;
                t2 = 4;
            }
        }

    }
}

package org.sidor.androidapps.simpletuner;

/**
 * Created by Nil on 11/30/2015.
 */


public class testclass {

    private double valueToSend;
    private boolean tooLow;
    private boolean initTooLow;

    //this is called every time a new string is going to be tuned
    private void initSendMessage() {
        valueToSend = 1000;
        initTooLow = false;
    }

    private void send(double value) {
        return;
    }

    //this is called every time a frequency is detected (with a 1s waiting time between calls)
    private void sendMessage(double targetPitch, double actualPitch) {
        if (!initTooLow) {
            if (targetPitch - actualPitch > 0) tooLow = true;
            else tooLow = false;
            return;
        }

        if (tooLow && targetPitch - actualPitch > 0) {
            send(valueToSend);
        } else if (tooLow && targetPitch - actualPitch < 0) {
            tooLow = false;
            valueToSend /= 2.0;
            send(-valueToSend);
        } else if (!tooLow && targetPitch - actualPitch > 0) {
            tooLow = true;
            valueToSend /= 2.0;
            send(valueToSend);
        } else if (!tooLow && targetPitch - actualPitch < 0) {
            send(-valueToSend);
        }
    }
}



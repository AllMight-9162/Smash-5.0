package frc.Java_Is_AllMight.Sensors;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Timer;

public class UltrasonicSensor {
    private final DigitalOutput trigger;
    private final DigitalInput echo;
    private final Timer timer;

    public UltrasonicSensor(int triggerPort, int echoPort) {
        trigger = new DigitalOutput(triggerPort);
        echo = new DigitalInput(echoPort);
        timer = new Timer();
    }

    public double getDistance() {
        trigger.set(true);
        Timer.delay(0.00001); 
        trigger.set(false);

        timer.reset();
        timer.start();
        while (!echo.get()) {
            if (timer.get() > 0.05) { 
                return -1;
            }
        }

        timer.reset();
        while (echo.get()) {
            if (timer.get() > 0.05) { 
                return -1;
            }
        }

        double pulseDuration = timer.get();
        return (pulseDuration * 34300.0) / 2.0; 
    }
}
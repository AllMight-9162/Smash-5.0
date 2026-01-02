package frc.Java_Is_AllMight.Sensors;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Timer;

//Coloca comentario no codigo meu mano Rafael Henritzi
public class UltrasonicSensor {

    //Constantes (Cálculo e Tempos)
    private static final double SOUND_VELOCITY_CM_PER_SECOND = 34300.0; //Velocidadae do Som em CM/s 
    private static final double TIMEOUT_SEC = 0.05; //Timeouts utilizados nos métodos (SEGUNDOS) 
    private static final double TRIGGER_SEC = 0.00001; // Timer do Trigger (SEGUNDOS)
    
    //Hardware
    private final DigitalOutput trigger; // Botão
    private final DigitalInput echo; // Resposta
    private final Timer timer; // Tempo

    //Construtor
    public UltrasonicSensor(int triggerPort, int echoPort) {
        // Utilizamos "this" pois as variáveis pertencem a classe => foram definidas lá em cima
        this.trigger = new DigitalOutput(triggerPort);
        this.echo = new DigitalInput(echoPort);
        this.timer = new Timer();
    }
    
    //Método Público 

    /**
     * @return distância em centimetros ou -1 em caso de falha
    */
    public double getDistance(){
        sendTriggerPulse();
        double pulseDuration = measureEchoPulse();
        if(pulseDuration < 0){
            return -1;
        }
        return convertTimeToDistance(pulseDuration);
    }
    //Métodos Privados
    
    //Envia o pulso do ultrassônico 
    private void sendTriggerPulse(){
        trigger.set(true);
        Timer.delay(TRIGGER_SEC);
        trigger.set(false);
    }

    //Medindo o tempo do eco
    private double measureEchoPulse(){
        timer.reset();
        timer.start();
        //Enquanto o ultrassonico não responder, olhar o relogio; Se demorar retorne -1
        while(!echo.get()){
            if(timer.get() > TIMEOUT_SEC){
                return -1;
            }
        }

        /** 
         * Agora o eco começou => reseta o timer;
         * “Enquanto o eco estiver ativo, conta o tempo”
         * Quando o eco some, o tempo total estaráa no timer
        */
        timer.reset();
        while(echo.get()){
            if(timer.get() > TIMEOUT_SEC){
                return -1;
            }
        }

        return timer.get();
    }

    /**
     * @param pulseDuration
     * @return Tempo do pulso do ultrassônico convertido em CM; Usa o cálculo básico de dist = vel * temp;
     * Como o pulso vai e volta dividimos por dois para termos a distancia exata!
     */
    private double convertTimeToDistance(double pulseDuration){
        return (pulseDuration * SOUND_VELOCITY_CM_PER_SECOND) / 2.0;
    }
}
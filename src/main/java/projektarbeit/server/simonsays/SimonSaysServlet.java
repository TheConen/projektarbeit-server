package projektarbeit.server.simonsays;

import com.amazon.speech.speechlet.servlet.SpeechletServlet;

/**
 * Created by johan on 20.05.2016.
 */
public class SimonSaysServlet extends SpeechletServlet {

    public SimonSaysServlet() {
        this.setSpeechlet(new SimonSaysSpeechlet());
    }
}

package projektarbeit.server.simonsays;

import com.amazon.speech.speechlet.servlet.SpeechletServlet;

public class SimonSaysServlet extends SpeechletServlet {

    public SimonSaysServlet() {
        this.setSpeechlet(new SimonSaysSpeechlet());
    }
}

package projektarbeit.server.bigbrother;
import com.amazon.speech.speechlet.servlet.SpeechletServlet;

public class BigBrotherServlet extends SpeechletServlet {

    public BigBrotherServlet() {
        this.setSpeechlet(new BigBrotherSpeechlet());
    }
}
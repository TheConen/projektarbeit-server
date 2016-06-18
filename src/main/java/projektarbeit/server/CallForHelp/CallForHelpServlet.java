package projektarbeit.server.CallForHelp;

import com.amazon.speech.speechlet.servlet.SpeechletServlet;

public class CallForHelpServlet extends SpeechletServlet {
    
	public CallForHelpServlet() {
        this.setSpeechlet(new CallForHelpSpeechlet());
    }
}

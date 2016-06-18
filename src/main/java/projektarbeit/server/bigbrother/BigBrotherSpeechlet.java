package projektarbeit.server.bigbrother;

import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.PlainTextOutputSpeech;


public class BigBrotherSpeechlet implements Speechlet {

    public void onSessionStarted(SessionStartedRequest sessionStartedRequest, Session session) throws SpeechletException {
        BigBrotherWebsocket.send(sessionStartedRequest);
    }

    public SpeechletResponse onLaunch(LaunchRequest launchRequest, Session session) throws SpeechletException {
        if (!BigBrotherWebsocket.send(launchRequest)) {
            return forwardFailed();
        }
        return getAnswer(launchRequest.getRequestId());
    }

    public SpeechletResponse onIntent(IntentRequest intentRequest, Session session) throws SpeechletException {
        if (!BigBrotherWebsocket.send(intentRequest)) {
            return forwardFailed();
        }
        return getAnswer(intentRequest.getRequestId());
    }

    public void onSessionEnded(SessionEndedRequest sessionEndedRequest, Session session) throws SpeechletException {
        BigBrotherWebsocket.send(sessionEndedRequest);
    }

    private SpeechletResponse getAnswer(String requestId) {
        String answer = BigBrotherWebsocket.getAnswer(requestId);

        for (int i = 0; i < 25; i++) {
            if (answer == null) {
                try {
                    Thread.sleep(50);
                    answer = BigBrotherWebsocket.getAnswer(requestId);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        if (answer == null) {
            answer = "No answer received from client";
        }

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(answer);
        return SpeechletResponse.newTellResponse(speech);
    }

    private SpeechletResponse forwardFailed() {
        String answer = "Unable to forward message to client";
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(answer);
        return SpeechletResponse.newTellResponse(speech);
    }
}


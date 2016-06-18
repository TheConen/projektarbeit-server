package projektarbeit.server.bigbrother;

import com.amazon.speech.speechlet.SpeechletRequest;
import projektarbeit.server.WebsocketImpl;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;


@ServerEndpoint("/bigbrotherws")
public class BigBrotherWebsocket {

    private static WebsocketImpl websocket = new WebsocketImpl("BigBrother");

    @OnOpen
    public void start(Session session) {
        websocket.start(session);
    }

    @OnClose
    public void end(Session session) {
        websocket.end(session);
    }

    @OnMessage
    public void incoming(String message) {
        websocket.incoming(message);
    }

    @OnError
    public void onError(Throwable t) throws Throwable {
        websocket.onError(t);
    }

    public static boolean send(String message) {
        return websocket.send(message);
    }

    public static boolean send(SpeechletRequest request) {
        return websocket.send(request);
    }


    public static String getAnswer(String id) {
        return websocket.getAnswer(id);
    }
}
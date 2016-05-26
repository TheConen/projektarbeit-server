package projektarbeit.server;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.*;

import javax.websocket.Session;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WebsocketImpl {

    private Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());
    private String skill = null;
    private Map<String, String> answers = Collections.synchronizedMap(new HashMap<>());

    public WebsocketImpl(String skill) {
        this.skill = skill;

        ScheduledExecutorService clearMessages = Executors.newSingleThreadScheduledExecutor();
        clearMessages.scheduleAtFixedRate(() -> {
            answers.clear();
        }, 1, 30, TimeUnit.SECONDS);
    }

    public void start(Session session) {
        log(skill + ":Open");
        sessions.add(session);
    }

    public void end(Session session) {
        log("Close");
        sessions.remove(session);
    }

    public void incoming(String message) {
        log("Received: " + message);
        if (!message.equals("keep-alive")) {
            String[] messageParts = message.split(";");
            String messageId = messageParts[0];
            String messageContent = messageParts[1];
            answers.put(messageId, messageContent);
        }
    }

    public void onError(Throwable t) throws Throwable {
        log("Error: " + t.getMessage());
    }

    public boolean send(String message) {
        boolean result = true;
        log("Sending: " + message);
        for (Session session: sessions) {
            if (session.isOpen()) {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    result = false;
                }
            }
        }
        return result;
    }

    public boolean send(SpeechletRequest request) {
        String message = skill;
        if (request instanceof SessionStartedRequest) {
            message += ";SessionStarted;" + request.getRequestId();
        } else if (request instanceof LaunchRequest) {
            message += ";Launch;" + request.getRequestId();
        } else if (request instanceof IntentRequest) {
            message += ";Intent;" + request.getRequestId();
            Intent intent = ((IntentRequest) request).getIntent();
            Map<String, Slot> slots = intent.getSlots();

            for (Map.Entry<String, Slot> itr: slots.entrySet()) {
                Slot curSlot = itr.getValue();
                message += ";" + curSlot.getName() + "," + curSlot.getValue();
            }
        } else if (request instanceof SessionEndedRequest) {
            message += ";SessionEnded;" + request.getRequestId();
        } else {
            return false;
        }
        return send(message);
    }

    public String getAnswer(String id) {
        return answers.remove(id);
    }

    private void log(String message) {
        String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());

        System.out.println(timeStamp + "    " + skill + ": " + message);
    }
}

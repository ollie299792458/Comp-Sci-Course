package uk.ac.cam.olb22.fjava.tick5;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import uk.ac.cam.cl.fjava.messages.Message;

public class ReorderBuffer {

    private final List<Message> buffer;
    private VectorClock lastDisplayed;

    public void addMessage(Message m) {
        //Add message m to the end of the buffer.
        buffer.add(m);
    }

    public ReorderBuffer(Map<String,Integer> initialMsg) {
        //given a vector clock from the first message received, initialize private fields.
        buffer = new LinkedList<>();
        lastDisplayed = new VectorClock(initialMsg);

    }

    public Collection<Message> pop() {
        //search the buffer and remove and return all messages eligible for display.
        Collection<Message> result = new LinkedList<>();
        boolean foundMessage = true;
        while (foundMessage) {
            foundMessage = false;
            for(int i = 0; i < buffer.size(); i++) {
                Message message = buffer.get(i);
                VectorClock messageClock = new VectorClock(message.getVectorClock());

                int elementsOneGreater = 0;

                for(String id : message.getVectorClock().keySet()) {
                    if(messageClock.getClock(id) > lastDisplayed.getClock(id)) {
                        elementsOneGreater++;
                    }
                }

                if(elementsOneGreater <= 1) {
                    buffer.remove(i);
                    result.add(message);

                    lastDisplayed.updateClock(message.getVectorClock());
                    foundMessage = true;
                }
            }
        }
        return result;
    }
}
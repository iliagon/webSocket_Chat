package Models.EncodersAndDecoder;

import Models.Members;
import com.google.gson.Gson;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MembersEncoder implements Encoder.Text<Members> {
    private static Gson gson = new Gson();

    @Override
    public String encode(Members members) throws EncodeException {
        return gson.toJson(members);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        // Custom initialization logic
    }

    @Override
    public void destroy() {
        // Close resources
    }
}

package Models.EncodersAndDecoder;

import Models.JsonModel;
import Models.Message;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.util.Map;

public class MessageDecoder implements Decoder.Text<JsonModel> {

    private static Gson gson = new Gson();

    @Override
    public JsonModel decode(String json) throws DecodeException {
        Map<String, String> messageMap = gson.fromJson(json, new TypeToken<Map<String, Object>>() {
        }.getType());

        JsonModel model;
        switch (messageMap.get("type")) {
            case "message":
                model = gson.fromJson(json, Message.class);
                break;
//            case "members":
//                model = gson.fromJson(json, Message.class);
//                break;
            default:
                throw new DecodeException(json, "[Message] Can't decode.");
        }

        return model;
    }

    @Override
    public boolean willDecode(String s) {
        return (s != null);
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

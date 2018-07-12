package io.galaxyonline.json;

import org.json.simple.JSONObject;

public interface JSONable {
    JSONObject toJSON();
    JSONable fromJSON(JSONObject json);
}

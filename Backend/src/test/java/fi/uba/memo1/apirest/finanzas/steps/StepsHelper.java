package fi.uba.memo1.apirest.finanzas.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.uba.memo1.apirest.finanzas.dto.CostosMensualesResponse;

import java.util.ArrayList;

public class StepsHelper {
    public static ArrayList<CostosMensualesResponse> jsonToCostoMensualesResponseArray(String jsonRaw) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonRaw, new TypeReference<>(){});
    }

    public static CostosMensualesResponse jsonToCostoMensualesResponse(String jsonRaw) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonRaw, CostosMensualesResponse.class);
    }

    public static String jsonErrorToMessage(String jsonRaw) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(jsonRaw).get("message").asText();
    }
}

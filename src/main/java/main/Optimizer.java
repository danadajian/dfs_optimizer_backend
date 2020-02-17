package main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import handler.OptimizerHandler;

import java.util.*;

public class Optimizer {
    public static void main(String[] args) throws JsonProcessingException {
        String inputString = args[0];
        Map<String, Object> optimizerInput = new ObjectMapper().readValue(inputString, new TypeReference<Map<String, Object>>(){});
        new OptimizerHandler().handleRequest(optimizerInput);
    }
}

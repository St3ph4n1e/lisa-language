package org.example.service;

import jakarta.annotation.PostConstruct;
import org.example.model.Exercise;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Map;

@Service
public class ExerciseService {

    private Map<String, Exercise> exercises;

    @PostConstruct
    public void init() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        InputStream is = getClass().getResourceAsStream("/data/exercises.json");
        TypeReference<Map<String, Exercise>> typeRef = new TypeReference<>() {};
        exercises = mapper.readValue(is, typeRef);
    }

    public Map<String, Exercise> getAll() {

        return exercises;
    }

    public Exercise getByLevel(String level){
        return exercises.get(level);
    }
}
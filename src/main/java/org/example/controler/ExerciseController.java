package org.example.controler;

import org.example.Main;
import org.example.model.Exercise;
import org.example.service.ExerciseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

@Controller
public class ExerciseController {
    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping("/")
    public String index(Model model) {
        Map<String, Exercise> exercises = exerciseService.getAll();
        model.addAttribute("exercises", exercises);
        model.addAttribute("selectedExercise", exerciseService.getByLevel("level1"));
        model.addAttribute("selectedExerciseKey", "level1");
        return "index";
    }

    @PostMapping("/select")
    public String selectExercise(@RequestParam("level") String level, Model model) {
        Map<String, Exercise> exercices = exerciseService.getAll();
        model.addAttribute("exercises", exercices);
        model.addAttribute("selectedExercise", exerciseService.getByLevel(level));
        model.addAttribute("selectedExerciseKey", level); // clé pour le select
        return "index";
    }

    @PostMapping("/run")
    @ResponseBody
    public String runCode(@RequestParam("code") String code) {
        // Capture everything printed by the interpreter and Main.run
        PrintStream originalOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream capture = new PrintStream(buffer, true, StandardCharsets.UTF_8);
        try {
            System.setOut(capture);
            Main.run(code);
        } finally {
            System.setOut(originalOut);
            capture.flush();
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }
}

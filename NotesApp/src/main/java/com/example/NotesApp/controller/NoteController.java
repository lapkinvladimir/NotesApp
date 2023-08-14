package com.example.NotesApp.controller;


import com.example.NotesApp.model.Note;
import com.example.NotesApp.model.User;
import com.example.NotesApp.repository.NoteRepository;
import com.example.NotesApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/")
public class NoteController {

    private final NoteRepository noteRepository;

    private final UserRepository userRepository;

    @Autowired
    public NoteController(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String email, @RequestParam String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        userRepository.save(user);
        return "redirect:/notes"; // Перенаправляем на страницу входа
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {

        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return "redirect:/notes"; // Перенаправляем на страницу после успешного входа
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login"; // Возвращаем на страницу логина с ошибкой
        }
    }

    @PostMapping("/recover")
    public String handleRecoveryForm(@RequestParam String email, Model model) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            // Здесь можно выполнять дополнительные действия, например, отправку письма с восстановлением пароля
            return "redirect:/notes"; // Перенаправляем на страницу notes после успешного восстановления
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "/recover"; // Перенаправляем с ошибкой, если email не найден
        }
    }


    // Эндпоинт для создания новой заметки
    @PostMapping
    public ResponseEntity<Note> createNote(@RequestBody Note note) {
        Note createdNote = noteRepository.save(note);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNote);
    }

    @GetMapping
    public String authPage() {
        return "auth";
    }

    @GetMapping("/recover")
    public String recoverPage(Model model) {
        model.addAttribute("error", null);
        return "recover";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("error", null);
        return "login";
    }

    @GetMapping("/notes")
    public String mainPage() {
        return "notes";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }



//    // Эндпоинт для получения всех заметок
//    @GetMapping
//    public ResponseEntity<List<Note>> getAllNotes() {
//        List<Note> notes = noteRepository.findAll();
//        return ResponseEntity.ok(notes);
//    }

    // Эндпоинт для получения заметки по ID
    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable Long id) {
        Optional<Note> note = noteRepository.findById(id);
        return note.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Эндпоинт для обновления заметки
    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestBody Note updatedNote) {
        if (!noteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        updatedNote.setId(id);
        Note savedNote = noteRepository.save(updatedNote);
        return ResponseEntity.ok(savedNote);
    }

    // Эндпоинт для удаления заметки
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id) {
        if (!noteRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        noteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

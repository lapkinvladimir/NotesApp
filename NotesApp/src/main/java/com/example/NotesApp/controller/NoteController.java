package com.example.NotesApp.controller;


import com.example.NotesApp.mail.EmailService;
import com.example.NotesApp.model.Note;
import com.example.NotesApp.model.User;
import com.example.NotesApp.repository.NoteRepository;
import com.example.NotesApp.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class NoteController {

    private final NoteRepository noteRepository;

    private final UserRepository userRepository;

    private final EmailService emailService;


    @Autowired
    public NoteController(NoteRepository noteRepository, UserRepository userRepository, EmailService emailService) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
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
    public String login(@RequestParam String username, @RequestParam String password, Model model, HttpServletResponse response) {

        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            if (user.getSelectedAvatar() == null) {
                user.setSelectedAvatar("/img/logo.png"); // Дефолтный URL для логотипа
            }
            if (user.getSelectedBackground() == null) {
                user.setSelectedBackground("/img/background-image.jpg"); // Дефолтный URL для фонового изображения
            }
            userRepository.save(user); // Сохраняем обновленные данные пользователя
            // Остальной код остается без изменений
            Cookie usernameCookie = new Cookie("username", username);
            usernameCookie.setMaxAge(86400); // Настройте срок действия куки по вашим требованиям
            response.addCookie(usernameCookie);
            return "redirect:/notes"; // Перенаправляем на страницу после успешного входа
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login"; // Возвращаем на страницу логина с ошибкой
        }
    }

    @PostMapping("/notes")
    public ResponseEntity<String> saveImages(@RequestBody Map<String, String> imageUrls, HttpServletRequest request) {
        String selectedAvatar = imageUrls.get("selectedAvatar");
        String selectedBackground = imageUrls.get("selectedBackground");

        String baseUrl = request.getRequestURL().toString();
        String basePath = baseUrl.substring(0, baseUrl.length() - request.getRequestURI().length()) + request.getContextPath();

        Cookie[] cookies = request.getCookies();
        String username = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) {
                    username = cookie.getValue();
                    break;
                }
            }
        }

        if (username != null) {
            User user = userRepository.findByUsername(username);
            if (user != null) {
                if (selectedAvatar != null) {
                    selectedAvatar = selectedAvatar.replace(basePath, "");
                } else {
                    selectedAvatar = user.getSelectedAvatar();
                }

                if (selectedBackground != null) {
                    selectedBackground = selectedBackground.replace(basePath, "");
                } else {
                    selectedBackground = user.getSelectedBackground();
                }

                user.setSelectedAvatar(selectedAvatar);
                user.setSelectedBackground(selectedBackground);
                userRepository.save(user);

                return ResponseEntity.ok("Images saved successfully");
            } else {
                return ResponseEntity.badRequest().body("User not found");
            }
        } else {
            return ResponseEntity.badRequest().body("Username not found in cookies");
        }
    }









    @PostMapping("/recover")
    public String handleRecoveryForm(@RequestParam String email, Model model) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            String subject = "Password Recovery";
            Map<String, Object> modelMap = new HashMap<>();
            modelMap.put("username", user.getUsername());
            modelMap.put("login", user.getUsername());
            modelMap.put("password", user.getPassword());
            emailService.sendTemplatedEmail(email, subject, "email-template", modelMap); // Используйте "test" вместо "test.html"
            return "login";
        } else {
            model.addAttribute("error", "Email not found");
            return "recover";
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
    public String getUserData(HttpServletRequest request, Model model) {
        Cookie[] cookies = request.getCookies();
        String username = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) {
                    username = cookie.getValue();
                    break;
                }
            }
        }

        if (username != null) {
            User user = userRepository.findByUsername(username);

            if (user != null) {
                String selectedAvatar = user.getSelectedAvatar();
                String selectedBackground = user.getSelectedBackground();
                model.addAttribute("selectedAvatar", selectedAvatar);
                model.addAttribute("selectedBackground", selectedBackground);
                System.out.println(selectedAvatar);
                System.out.println(selectedBackground);
                return "notes";
            } else {
                // Возвращайте какое-то сообщение об ошибке, если нужно
            }
        }

        // Возвращайте какое-то сообщение об ошибке, если нужно
        return null;
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

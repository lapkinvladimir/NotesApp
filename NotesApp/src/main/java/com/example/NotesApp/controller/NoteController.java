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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
//@RequestMapping("/")
public class NoteController {

    @GetMapping
    public String authPage() {
        return "auth";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
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

        List<Note> notes = noteRepository.findAll();
        for (Note note : notes) {
            System.out.println("ID: " + note.getId());
            System.out.println("Title: " + note.getTitle());
            System.out.println("Content: " + note.getContent());
            System.out.println("Created At: " + note.getCreatedAt());
            System.out.println("Email: " + note.getEmail());
        }
        model.addAttribute("notes", notes);

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
                String email = user.getEmail();
                model.addAttribute("selectedAvatar", selectedAvatar);
                model.addAttribute("selectedBackground", selectedBackground);
                model.addAttribute("profileUsername", username);
                model.addAttribute("profileEmail", email);
                return "notes";
            }
        }
        return "redirect:/login";
    }


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
    public String registerUser(@RequestParam String username, @RequestParam String email, @RequestParam String password, HttpServletResponse response) {
        Cookie usernameCookie = new Cookie("username", username);
        usernameCookie.setMaxAge(86400); // сутки
        response.addCookie(usernameCookie);
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setSelectedBackground("/img/background-image.jpg");
        user.setSelectedAvatar("/img/logo.png");
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
    public String saveNote(@RequestBody Map<String, String> requestBody, HttpServletRequest request) {

        Note note = new Note();

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
                String email = user.getEmail();
                note.setEmail(email);
            }
        }


        String title = requestBody.get("title");
        String content = requestBody.get("content");

        // Создайте новую запись в базе данных
        note.setTitle(title);
        note.setContent(content);
        LocalDateTime now = LocalDateTime.now();
        now = now.withNano(0); // Обнулить миллисекунды
        note.setCreatedAt(now);



        // Сохраните запись
        noteRepository.save(note);

        return "redirect:/"; // Перенаправление на главную страницу после сохранения
    }


    @PutMapping("/notes")
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

}

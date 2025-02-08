package com.example.NotesApp.extra;

import org.springframework.stereotype.Service;

@Service
public class FakeService {
    public String getHelloMessage() {
        return "Hello from FakeService!";
    }

    public int getRandomValue() {
        return (int) (Math.random() * 100);
    }
}

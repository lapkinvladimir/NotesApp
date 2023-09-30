package com.example.NotesApp.repository;


import com.example.NotesApp.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByTitle(String title);


    List<Note> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);


    List<Note> findAllByOrderByCreatedAtDesc();


    List<Note> findAllByOrderByCreatedAtAsc();

    List<Note> findByEmail(String email);
}

package com.example.dudb.db2;

import com.example.dudb.Note;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/db2/notes")
public class Db2NoteController {

    private final Db2NoteService notes;

    public Db2NoteController(Db2NoteService notes) {
        this.notes = notes;
    }

    @PostMapping
    public Note create(@RequestBody Note request) {
        return notes.create(request.getBody());
    }

    @GetMapping("/{id}")
    public Note get(@PathVariable long id) {
        Note note = notes.find(id);
        if (note == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return note;
    }
}

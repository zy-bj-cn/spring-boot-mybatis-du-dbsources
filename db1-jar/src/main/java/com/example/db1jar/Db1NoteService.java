package com.example.db1jar;

import org.springframework.transaction.annotation.Transactional;

public class Db1NoteService {

    private final Db1NoteMapper mapper;

    public Db1NoteService(Db1NoteMapper mapper) {
        this.mapper = mapper;
    }

    @Transactional
    public Note create(String body) {
        Note note = new Note();
        note.setBody(body);
        mapper.insert(note);
        return note;
    }

    @Transactional(readOnly = true)
    public Note find(long id) {
        return mapper.findById(id);
    }
}

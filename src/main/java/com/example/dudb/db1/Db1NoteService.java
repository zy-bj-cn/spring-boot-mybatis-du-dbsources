package com.example.dudb.db1;

import com.example.dudb.Note;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Db1NoteService {

    private final Db1NoteMapper mapper;

    public Db1NoteService(Db1NoteMapper mapper) {
        this.mapper = mapper;
    }

    @Transactional(transactionManager = "db1TransactionManager")
    public Note create(String body) {
        Note note = new Note();
        note.setBody(body);
        mapper.insert(note);
        return note;
    }

    @Transactional(transactionManager = "db1TransactionManager", readOnly = true)
    public Note find(long id) {
        return mapper.findById(id);
    }
}

package com.example.dudb.db2;

import com.example.dudb.Note;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Db2NoteService {

    private final Db2NoteMapper mapper;

    public Db2NoteService(Db2NoteMapper mapper) {
        this.mapper = mapper;
    }

    // 不写名字时会落到 jar 里那个唯一可按类型注入的事务管理器，所以数据源 2 必须点名。
    @Transactional(transactionManager = "db2TransactionManager")
    public Note create(String body) {
        Note note = new Note();
        note.setBody(body);
        mapper.insert(note);
        return note;
    }

    @Transactional(transactionManager = "db2TransactionManager", readOnly = true)
    public Note find(long id) {
        return mapper.findById(id);
    }
}

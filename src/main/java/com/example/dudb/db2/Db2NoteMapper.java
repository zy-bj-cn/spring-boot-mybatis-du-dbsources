package com.example.dudb.db2;

import com.example.dudb.Note;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

public interface Db2NoteMapper {

    @Insert("INSERT INTO note(body) VALUES(#{body})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Note note);

    @Select("SELECT id, body FROM note WHERE id = #{id}")
    Note findById(long id);
}

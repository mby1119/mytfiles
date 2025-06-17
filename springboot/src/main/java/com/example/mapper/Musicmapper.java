package com.example.mapper;

import com.example.entity.Music;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface Musicmapper {
    List<Music>selectall(Music music);

    void insert(Music music);

    void play(Music music);

    @Select("select * from `music` where name = #{name}")
    Music selectByMusicname(String name);

    void updateById(Music music);

    @Delete("delete from `music` where id =#{id}")
    void deleteById(Integer id);

    @Select("select * from `music` where id = #{id}")
    Music selectById(String id);

    @Select("select * from `music` where id = #{id}")
    Music selecturlById(Integer id);

}

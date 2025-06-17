package com.example.mapper;

import com.example.entity.Playlist;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface Playlistmapper {
    List<Playlist>selectall(Playlist playlist);

    @Delete("delete from `playlist` where id =#{id}")
    void deleteById(Integer id);

    @Select("select * from `playlist` where id = #{id}")
    Playlist selectById(String id);

    @Select("select id from playlist where musicid = #{id}")
    Playlist selectByName(String id);

}

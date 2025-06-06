package com.example.server;

import com.example.entity.Playlist;
import com.example.mapper.Playlistmapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Playlistserver {

    @Resource
    Playlistmapper playlistmapper;

    public List<Playlist>selectall(){
        return playlistmapper.selectall(null);
    }

    public void deleteById(Integer id) {
        playlistmapper.deleteById(id);
    }

}

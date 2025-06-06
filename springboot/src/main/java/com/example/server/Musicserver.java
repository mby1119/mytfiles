package com.example.server;

import cn.hutool.core.util.StrUtil;
import com.example.entity.Music;
import com.example.entity.Playlist;
import com.example.exception.CustomerException;
import com.example.mapper.Musicmapper;
import com.example.mapper.Playlistmapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Musicserver {

    @Resource
    Musicmapper musicmapper;
    Playlistmapper playlistmapper;

    @Autowired
    private Playlistmapper Playlistmapper;

    public List<Music>selectall(){
        return musicmapper.selectall(null);
    }

    public PageInfo<Music> selectpage(Integer pagenum, Integer pagesize,Music music) {
        PageHelper.startPage(pagenum,pagesize);
        List<Music> list = musicmapper.selectall(music);
        return PageInfo.of(list);
    }

    public void add(Music music) {
        musicmapper.insert(music);
    }

    public void play(Music music) {
        Playlist dbuser=Playlistmapper.selectByName(music.getName());
        if(dbuser != null){
            throw  new CustomerException("歌曲已在播放列表");
        }
        musicmapper.play(music);
    }

    public void update(Music music) {
        musicmapper.updateById(music);
    }

    public void deleteById(Integer id) {
        musicmapper.deleteById(id);
    }

    public void selectById(String id) {
        musicmapper.selectById(id);
    }
}

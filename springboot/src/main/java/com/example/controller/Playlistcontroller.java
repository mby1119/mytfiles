package com.example.controller;

import com.example.common.Result;
import com.example.entity.Music;
import com.example.entity.Playlist;
import com.example.server.Playlistserver;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/playlist")
public class Playlistcontroller {

    @Resource
    Playlistserver playlistserver;

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        playlistserver.deleteById(id);
        return Result.success();
    }
    
    /*一个get请求接口*/
    @GetMapping("/selectall")  //完整网页路径@/playlist/selectall
    public Result selectall() {
        List<Playlist> playlistlist = playlistserver.selectall();
        return Result.success(playlistlist);
    }

}

package com.example.controller;

import com.example.common.Result;
import com.example.entity.Music;
import com.example.server.Fileserver;
import com.example.server.Musicserver;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/music")
public class Musiccontroller {

    @Resource
    Musicserver musicserver;

    @Resource
    Fileserver fileserver;

    @PostMapping("/add")
    public Result add(@RequestBody Music music) {
        musicserver.add(music);
        return Result.success();
    }

    @PostMapping("/play")
    public Result play(@RequestBody Music music) {
        musicserver.play(music);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Music music) {
        fileserver.deleteFile(music.getId());
        musicserver.update(music);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        fileserver.deleteFile(id);
        musicserver.deleteById(id);
        return Result.success();
    }
    
    /*一个get请求接口*/
    @GetMapping("/selectall")  //完整网页路径@/music/selectall
    public Result selectall() {
        List<Music> musiclist = musicserver.selectall();
        return Result.success(musiclist);
    }


    @GetMapping("/selectpage")
    public Result selectpage(@RequestParam(defaultValue = "1")Integer pagenum,
                             @RequestParam(defaultValue = "10")Integer pagesize
                              ,Music music){
        PageInfo<Music> pageInfo=musicserver.selectpage(pagenum,pagesize,music);
        return Result.success(pageInfo);
    }
}

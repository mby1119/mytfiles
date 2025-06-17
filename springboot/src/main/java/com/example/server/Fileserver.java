package com.example.server;

import com.example.common.FileStorageUtil;
import com.example.entity.Music;
import com.example.exception.CustomerException;
import com.example.mapper.Musicmapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class Fileserver {

    private static final Logger log = LoggerFactory.getLogger(Fileserver.class);
    @Autowired
    private FileStorageUtil fileStorageUtil;

    @Autowired
    private Musicmapper  musicmapper; // MyBatis接口

    @Transactional
    public void deleteFile(Integer fileId) {
        // 1. 查询文件信息
        Music musicfile = musicmapper.selecturlById(fileId);

//        if (musicfile.getAudiourl() == null) {
//            throw new CustomerException("文件不存在");
//        }

        String urlname=musicfile.getAudiourl();
        String covername=musicfile.getCover();
        String hard="D:\\aworkproject\\mymusic\\files\\";

        Integer urllen=urlname.length();
        String nam1=urlname.substring(37,urllen);

        Integer coverlen=covername.length();
        String nam2=covername.substring(37,coverlen);

        urlname=hard+nam1;
        covername=hard+nam2;

        // 2. 物理删除文件
        boolean isDeleted = fileStorageUtil.deleteFile(urlname);

        if (!isDeleted) {
            throw new CustomerException("音频文件删除失败");
        }

        isDeleted = fileStorageUtil.deleteFile(covername);

        if (!isDeleted) {
            throw new CustomerException("头像文件删除失败");
        }

        // 3. 删除数据库记录
        //fileMapper.deleteById(fileId);
    }
}

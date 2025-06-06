<template>
  <div class="mymusic">
    <!--头部查询-->
    <div class="el-card" style="margin-bottom: 5px;">
      <el-input clearable @clear="load" style="width: 240px;margin-right: 5px" v-model="data.name" placeholder=" 歌名查询" :prefix-icon="Search"></el-input>
      <el-input clearable @clear="load" style="width: 240px;margin-right: 5px" v-model="data.singer" placeholder="歌手查询" :prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button  @click="reset">重置</el-button>
      <el-button type="danger" @click="handleAdd">添加音乐</el-button>
    </div>
    <!--表单-->
    <div class="el-card" style="margin-bottom: 5px;">
      <el-table :data="data.tableData" style="width: 100%;">
        <el-table-column prop="cover" label="封面" width="180">
          <template #default="scope">
            <el-image :src="scope.row.cover"
                      :preview-src-list="[scope.row.cover]" :preview-teleported="true"
                      style="width:40px;height: 40px;display: block"/>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="歌名" width="180" />
        <el-table-column prop="singer" label="歌手" width="180" />
        <el-table-column label="操作" >
          <template #default="scope">
            <el-button type="primary" icon="VideoPlay" circle @click="get_info(scope.row)"></el-button>
            <el-button type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" icon="Delete" circle @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!--添加/修改表单-->
    <el-dialog v-model="data.formVIsible" title="音乐信息" width="500" destroy-on-close>
      <el-form ref="formRef" :model="data.form" label-width="80px" style="padding: 20px 30px 0 0">
        <el-form-item prop="name" label="歌名">
          <el-input v-model="data.form.name" autocomplete="off" />
        </el-form-item>
        <el-form-item prop="singer" label="歌手名">
          <el-input v-model="data.form.singer" autocomplete="off" />
        </el-form-item>
        <el-form-item label="封面">
          <el-upload
              action="http://localhost:8081/files/upload"
              :on-success="coverFileSuccess"
              list-type="picture">
            <el-button type="primary">上传封面</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="MP3文件">
          <el-upload
              action="http://localhost:8081/files/upload"
              :on-success="mp3FileSuccess">
            <el-button type="primary">上传封面</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <div>
          <el-button @click="data.formVIsible=false">取消</el-button>
          <el-button type="primary" @click="save">保存</el-button>
        </div>
      </template>
    </el-dialog>
    <!--分页配置-->
    <div class="el-card" style="padding-left:20px;width: auto;height:30px;margin-bottom: 5px;display: flex;">
      <el-pagination
          v-model:current-page="data.pageNum"
          :page-size="data.pageSize"
          layout="total, prev, pager, next"
          :total="data.total"
          @current-change="load"
      />
    </div>
  </div>
</template>

<script setup>
import {Search} from "@element-plus/icons-vue";
import {reactive,ref} from "vue";
import request from "../utils/request.js";
import {ElMessage} from "element-plus";
import { ElMessageBox } from 'element-plus'

const formRef =ref()

const  data=reactive({
  pageNum:1,
  pageSize:4,
  total:6,
  name:null,
  singer:null,
  from:[],
  tableData:[]
})

const load=()=>{
  request.get('/music/selectpage',{
    params:{
      pagenum:data.pageNum,
      pagesize:data.pageSize,
      name:data.name,
      singer:data.singer
    }
  }).then(res=>{
    if (res.code === '200') {
      data.tableData = res.data.list
      data.total=res.data.total
    } else {
      ElMessage.error(res.msg)
    }
  })
}
load();

const reset=()=>{
  data.name=null
  data.singer=null
  load()
}

const handleAdd=()=>{
  data.formVIsible=true
  data.form={}
}

const save = () => {
  data.form.id ? update():add()
}

const add=()=>{
  formRef.value.validate((valid) =>{
    if (valid) {
      request.post('/music/add', data.form).then(res => {
        if (res.code === '200') {
          data.formVIsible = false
          ElMessage.success('success')
          load()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

const get_info=(row)=>{
  data.form=JSON.parse(JSON.stringify(row))
  request.post('/music/play', data.form).then(res => {
    if (res.code === '200') {
      window.location.reload()
      ElMessage.success('success')
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const update = () => {
  formRef.value.validate((valid) =>{
    if (valid) {
      request.put('/music/update', data.form).then(res => {
        if (res.code === '200') {
          data.formVIsible = false
          ElMessage.success('success')
          load()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

const del = (id) => {
  ElMessageBox.confirm('sure?','sure',{type:'warning'}).then(res =>{
    request.delete('/music/delete/'+id).then(res => {
      if (res.code === '200') {
        ElMessage.success('success')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err =>{})
}

const handleEdit=(row)=>{
  data.form=JSON.parse(JSON.stringify(row))
  data.formVIsible=true
}

const coverFileSuccess = (res) => {
  data.form.cover=res.data
}
const mp3FileSuccess = (res) => {
  data.form.audiourl=res.data
}
</script>

<style>

</style>
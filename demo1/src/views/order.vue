<template>
  <div>
    <div class="el-card" style="margin-bottom: 5px;">
      <el-button type="danger" @click="handleAdd">添加订单</el-button>
    </div>
    <!--表单-->
    <div class="el-card" style="margin-bottom: 5px;">
      <el-table :data="data.tableData" style="width: 100%;">
        <el-table-column prop="name" label="歌名" width="180" />
        <el-table-column prop="paytime" label="订单时间" width="180" />
        <el-table-column label="操作" >
          <template #default="scope">
            <el-button type="primary" icon="VideoPlay" circle @click="pay(scope.row)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!--表单添加-->
    <el-dialog v-model="data.formVIsible" title="信息" width="500" destroy-on-close>
      <el-form ref="formRef" :model="data.form" label-width="80px" style="padding: 20px 30px 0 0">
        <el-form-item prop="name" label="名">
          <el-input v-model="data.form.name" autocomplete="off" />
        </el-form-item>
        <el-form-item prop="orderno" label="订单编号">
          <el-input v-model="data.form.orderno" autocomplete="off" />
        </el-form-item>
        <el-form-item prop="total" label="订单价格">
          <el-input v-model="data.form.total" autocomplete="off" />
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
  request.get('/order/selectpage',{
    params:{
      pagenum:data.pageNum,
      pagesize:data.pageSize,
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


const pay=(row)=>{
  window.open('http://localhost:8081/alipay/pay?orderno='+row.id)
}

const handleAdd=()=>{
  data.formVIsible=true
  data.form={}
}

const save=()=>{
  formRef.value.validate((valid) =>{
    if (valid) {
      request.post('/order/add', data.form).then(res => {
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

</script>

<style>

</style>
import axios from 'axios';
import {ElMessage} from "element-plus";
import router from "../router/index.js";

//axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
// 创建axios实例
const request = axios.create ({ // axios中请求配置有baseURL选项，表示请求URL公共部分
    baseURL: 'http://localhost:8081',
    // 超时
    timeout: 10000
})
// request拦截器
request.interceptors.request.use(config => {
        config.headers['Content-Type'] = 'application/json;charset=utf-8';
        let user =JSON.parse(localStorage.getItem('code_user')||'{}')
        config.headers['token']=user.token
        return config
    }, error => {
        return Promise.reject(error)
    }
);

// 响应拦截器
request.interceptors.response.use(
    response => {
        let res = response.data;
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        if(res.code === '401'){
            ElMessage.error(res.msg)
            router.push('/login')
        }
        else {
            return res;
        }
    },
    error => {
        if(error.response.status === 404){
            ElMessage.error('404')
        }
        else if(error.response.status === 500){
            ElMessage.error('500')
        }
        else{
            console.error(error.message)
        }
        return  Promise.reject(error)
     }
)

export default request


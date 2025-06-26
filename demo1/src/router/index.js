import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {path: '/', component: import('../views/main.vue'),},
        {path: '/main',component:()=>import('../views/main.vue'),
            children:[
                {path:'home',meta:{name:'主页'},component:()=>import('../views/home.vue'),},
                {path:'mymusic',meta:{name:'我的音乐'},component:()=>import('../views/mymusic.vue'),},
                {path:'order',meta:{name:'订单'},component:()=>import('../views/order.vue'),},
                {path:'setting',meta:{name:'聊天'},component:()=>import('../views/setting.vue'),},
                {path:'upload',meta:{name:'聊天'},component:()=>import('../views/upload.vue'),},

            ]
        },
        {path: '/notFouund', component: import('../views/404.vue'),},
        {path: '/:pathMatch(.*)', redirect:'/notFouund',},
        {path: '/musicplay',component:()=>import('../views/musicplay.vue'),},
    ],
})

export default router
import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
    //设置默认映射
    {
      path: '/',
      redirect: '/log'
    },
    {
      path: '/log',
      name: 'log',
      component: () => import(/* webpackChunkName: "about" */ '../views/LogView.vue')
    },
    {
      path: '/home',
      name: 'home',
      component: () => import(/* webpackChunkName: "about" */ '../views/HomeView.vue')
    },
    {
      path: '/register',
      name: 'register',
      component: () => import(/* webpackChunkName: "about" */ '../views/RegisterView.vue')
    },
    {
      path: '/test',
      name: 'test',
      component: () => import(/* webpackChunkName: "about" */ '../views/TestView.vue')
    },
    {
      path: '/vote',
      name: 'vote',
      component: () => import(/* webpackChunkName: "about" */ '../views/MyVoteView.vue')
    },
    {
      path: '/participate',
      name: 'participate',
      component: () => import(/* webpackChunkName: "about" */ '../views/MyParticipate.vue')
    },
    {
      path: '/result',
      name: 'result',
      component: () => import(/* webpackChunkName: "about" */ '../views/ResultView.vue')
    },
  // {
  //   path: '/about',
  //   name: 'about',
  //   // route level code-splitting
  //   // this generates a separate chunk (about.[hash].js) for this route
  //   // which is lazy-loaded when the route is visited.
  //   component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  // }
]

const router = new VueRouter({
  routes
})

export default router

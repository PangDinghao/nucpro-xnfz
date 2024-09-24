import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import request from "./utils/request.js"
import ElementUI from 'element-ui';
import axios from "axios";
import 'element-ui/lib/theme-chalk/index.css';

Vue.prototype.request = request
Vue.config.productionTip = false
Vue.use(ElementUI);
Vue.prototype.$httpUrl='http://localhost:8083'
Vue.prototype.$axios=axios;
Vue.prototype.$ImgUrl='D://A_vue/images/'


new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')



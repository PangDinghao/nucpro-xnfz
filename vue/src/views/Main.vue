<template>
  <div >
    <el-container style="height: 750px;font-family: 'PingFang SC'" >
      <el-header><Header></Header></el-header>
      <el-container>
        <el-aside width="200px"><Aside></Aside></el-aside>
        <router-view></router-view>
      </el-container>
    </el-container>
  </div>
</template>
<script>
import Aside from "@/views/Aside.vue";
import Header from "@/views/Header.vue";

export default {
  name:"Main",
  components: {Header, Aside},
  comments:{
    Aside,
    Header,
  }
}
</script>
<style>
.el-header, .el-footer {
  background-color: #B3C0D1;
  color: #333;
  text-align: center;
  line-height: 60px;
}

.el-aside {
  background-color: #D3DCE6;
  color: #333;
  text-align: center;
  line-height: 200px;
}

.el-main {
  background-color: #E9EEF3;
  color: #333;
  text-align: center;
  line-height: 160px;
}

body > .el-container {
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}
</style>
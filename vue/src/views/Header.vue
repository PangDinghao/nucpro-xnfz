<template>
  <div style="display: flex;line-height: 60px;" >
    <div style="font-size: 25px">
      <i class="el-icon-reading" ></i>
    </div>
    <div style="flex: 1;text-align: center;font-size: 25px">
      虚拟仿真实训教学管理及资源共享云平台
    </div>
    <el-dropdown trigger="click">
      <span style="margin-left: 5px">{{user.username}} <i class="el-icon-arrow-down" style="margin-left: 5px"></i></span>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item @click.native="toPersonal_Detail">个人中心</el-dropdown-item>
        <el-dropdown-item @click.native="logOut">退出登录</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>

  </div>


</template>

<script>
export default {
  name: "Header",
  methods:{
    logOut(){
      this.$router.push('/Login');
    },
    toPersonal_Detail() {
      sessionStorage.setItem("CurUser",JSON.stringify(this.user))
      if (this.$route.name=='Personal_Detail'){
        this.$message({
          message: '已在当前页面！',
          type: 'error',
        });
      }else {
        this.$router.push("Personal_Detail");
      }
    },
},
  data(){
    return{
      user:JSON.parse(sessionStorage.getItem('CurUser')),
    }
  }

}
</script>

<style scoped>

</style>
<template>
  <div style="align-content: center;margin: 20px;width: 1024px;text-align: center">

    <div>
      <el-page-header @back="ReturnToNotice" content="个人信息" style="float: left"></el-page-header>

    </div>
    <div style="margin-top:80px ">
      <el-descriptions>
        <el-descriptions-item label="用户名">{{ CurUser.username }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ CurUser.phone }}</el-descriptions-item>
        <el-descriptions-item label="账号">{{ CurUser.account }}</el-descriptions-item>
        <el-descriptions-item label="注册日期">{{ CurUser.createDatetime.slice(0, 10) }}</el-descriptions-item>//只取年月日
        <el-descriptions-item label="邮箱">{{ CurUser.email }}</el-descriptions-item>
        <el-descriptions-item label="密码">
          {{  maskPassword(CurUser.password) }}
        </el-descriptions-item>
      </el-descriptions>

    </div>


  </div>
</template>

<script>
export default {
  name: "Personal_Detail",
  data(){
    return{
      router:'',
      CurUser:JSON.parse(sessionStorage.getItem('CurUser')),
    }
  },
  methods: {
    ReturnToNotice() {
      this.$router.push("Notice");
    },
    maskPassword(password) {
      return '●'.repeat(password.length); // 将密码替换为等长的圆球
    }
  }
}
</script>

<style scoped>

</style>
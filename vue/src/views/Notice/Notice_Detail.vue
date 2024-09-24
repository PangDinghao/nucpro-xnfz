<template>
  <div style="align-content: center;margin: 20px;width: 1024px;text-align: center" >

  <div>
    <el-page-header @back="ReturnToNotice" content="公告详情" style="float: left"></el-page-header>
    <h1 style=" margin-right: 20%;margin-bottom: 0;margin-top: 0">
      {{notice.title}}
    </h1>
  </div>

  <!--  <h1 style="text-align: center;margin: 0;">-->
<!--    {{notice.title}}-->
<!--    <div style="float: right;">-->
<!--      <el-link icon="el-icon-s-fold" @click="ReturnToNotice">返回</el-link>-->
<!--    </div>-->
<!--  </h1>-->
  <div>
    <div style="float: right;width: 100px;margin-top: 0;">
      <p style="font-size: 12px;text-align: left">作者：{{notice.author}}</p>
      <p style="font-size: 12px;text-align: left">点击量：{{notice.hits}}</p>
    </div>
  </div>
  <hr style="margin-top: 80px">
  <el-image :src="require('D://A_vue/images/' + notice.coverImg)"
            style="width: 600px;display: block; margin: 0 auto;"></el-image>
  <p style="margin-top: 20px">
    {{    notice.details }}
  </p>
  <div style="float: right;">
    <p style="font-size: 12px;text-align: left">发布时间：{{notice.createDatetime}}</p>
  </div>


</div>
</template>

<script>
import router from "@/router";

export default {
  name: "Notice_Detail",
  data(){
    return{
      router:'',
      notice:JSON.parse(sessionStorage.getItem('notice')),
    }
  },
  methods:{
    ReturnToNotice(){//跳转到新闻公告页面
      this.$router.push("Notice");
    }
  },
}
</script>

<style scoped>

</style>
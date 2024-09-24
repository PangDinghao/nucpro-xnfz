<template>
  <div>
    <div style="align-content: center;margin: 20px;width: 1024px;text-align: center">
      <div style="display: flex;align-content: center">
        <el-page-header @back="ReturnToNotice_Manage" content="公告详情管理"
                        style="float: left;width: 200px"></el-page-header>

        <h1 style=" margin-right: auto;margin-left: auto;margin-bottom: 0;margin-top: 0">
          {{ notice.title }}
        </h1>

        <el-link type="primary" @click="update(notice)" style="width: 100px">修改<i class="el-icon-edit" ></i></el-link>

        <el-popconfirm
            title="确定删除吗？"
            @confirm="remove(notice)"
            placement="top"
            style="width: 100px"
        >
          <el-link type="danger" slot="reference" style="width: 100px">删除<i class="el-icon-delete"></i></el-link>
        </el-popconfirm>
      </div>

      <div>
        <div style="float: right;width: 100px;margin-top: 0;">
          <p style="font-size: 12px;text-align: left">作者：{{ notice.author }}</p>
          <p style="font-size: 12px;text-align: left">点击量：{{ notice.hits }}</p>
        </div>
      </div>
      <hr style="margin-top: 80px">
      <el-image :src="require('D://A_vue/images/' + notice.coverImg)"
                style="width: 600px;display: block; margin: 0 auto;"></el-image>
      <p style="margin-top: 20px">
        {{ notice.details }}
      </p>
      <div style="float: right;">
        <p style="font-size: 12px;text-align: left">发布时间：{{ notice.createDatetime }}</p>
      </div>
    </div>
    <el-dialog
        title="提示"
        :visible.sync="DialogVisible"
        width="50%"
        style="margin-top: -88px"
        center>
      <el-form ref="form" :model="form" label-width="80px" style="display: grid;place-items: center;">
        <el-form-item label="标题" prop="no">
          <el-col :span="35">
            <el-input  v-model="form.title" style="width: 400px"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="详情" prop="details">
          <el-col :span="35">
            <el-input type="textarea" v-model="form.details":rows="10" style="width: 400px;"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="是否置顶" prop="noticeType">
          <el-col :span="35">
            <el-switch v-model="switchValue"></el-switch>
          </el-col>
        </el-form-item>

        <el-form-item label="封面图片" prop="photo">
          <el-upload
              action="http://localhost:8083/file/upload"
              :on-success="handle_success"
          >
            <!-- 再次提醒，:on-success="函数名"，函数名后面不要跟括号 -->
            <el-button size="small" type="primary">点击上传</el-button>
            <div slot="tip" class="el-upload__tip">
              只能上传jpg/png文件，且不超过500kb
            </div>
          </el-upload>
        </el-form-item>

      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="DialogVisible = false;">取 消</el-button>
    <el-button type="primary" @click="doUpdate">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
import router from "@/router";

export default {
  name: "Notice_Detail",
  data(){
    return{
      DialogVisible:false,
      router:'',
      notice:JSON.parse(sessionStorage.getItem('notice')),
      Curuser:JSON.parse(sessionStorage.getItem('Curuser')),
      form:{
        no:'',
        title:'',
        author:'',
        hits:'',
        details: '',
        coverImg: '',
        createDatetime:'',
        updateDatetime:'',
        updateUser:'',
        noticeType:''
      },
    }
  },
  created() {
  },
  methods:{
    handle_success(res) {//图片上传成功过之后将图片名字传至coverImg
      console.log(res);
      this.$message.success("图片上传成功");
      return (this.form.coverImg = res.data);
    },
    ReturnToNotice_Manage(){//跳转到新闻公告管理页面
      this.$router.push("Notice_Manage");
    },
    remove(notice){
      this.$axios.get(this.$httpUrl+`/notice/remove?no=${notice.no}`).then(res=>res.data).then(res=>{
        console.log(res);

        if(res.code==200){

          this.$message({
            message: '删除成功',
            type: 'success',
          });
          this.$router.push("Notice_Manage");

        }else{
          this.$message({
            message: '操作失败',
            type: 'error'
          });
        }
      })
      this.form = '';
    },
    update(notice){
      this.form = notice;
      this.form.updateUser = this.Curuser.username;

      this.DialogVisible=true;

    },
    doUpdate(){

      this.$axios.post(this.$httpUrl+'/notice/update',this.form).then(res=>res.data).then(res=>{
        console.log(res);

        if(res.code==200){

          this.$message({
            message: '修改成功',
            type: 'success',
          });

          this.DialogVisible=false;

        }else{
          this.$message({
            message: '操作失败',
            type: 'error'
          });
        }
      })
      this.form = '';
    },
  },
  computed: {
    switchValue: {
      get() {
        return this.form.noticeType == 1; // 将1转换为true，0转换为false
      },
      set(value) {
        this.form.noticeType = value ? 1 : 0; // 将true转换为1，false转换为0
      }
    },
  }
}
</script>

<style scoped>

</style>
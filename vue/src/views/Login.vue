<template>
  <div class="loginbody"style="text-align: center" >
    <div class="logindata" style="width: 500px" >
      <div class="logintext" >
        <h2>虚拟仿真实训教学管理及资源共享云平台</h2>
      </div>
      <div class="formdata" >
        <el-form ref="form" :model="form" :rules="rules">
          <el-form-item prop="account">
            <el-input
                v-model="form.account"
                clearable
                placeholder="请输入账号"
            ></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
                v-model="form.password"
                clearable
                placeholder="请输入密码"
            ></el-input>
          </el-form-item>
        </el-form>
      </div>
      <div class="tool">
        <div>
          <el-checkbox v-model="checked" @change="remenber">
            记住密码
          </el-checkbox>
        </div>
      </div>
      <div class="butt">
        <el-button type="primary" @click="login('form')"
        >登录</el-button>
        <el-button class="shou" @click="register">注册</el-button>



      </div>
    </div>
    <el-dialog
        title="注册"
        :visible.sync="display"
        width="30%"
        top="8%"
        center>

      <el-form ref="form" :model="form" label-width="80px" >
        <el-form-item label="名字" prop="username">
          <el-col :span="35">
            <el-input v-model="form.username" ></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="账号" prop="account">
          <el-col :span="35">
            <el-input v-model="form.account" ></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-col :span="35">
            <el-input v-model="form.password" ></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="Email" prop="email">
          <el-col :span="35">
            <el-input v-model="form.email" ></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-col :span="35">
            <el-input v-model="form.phone" ></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="性别" prop="sex">&nbsp;
          <el-radio-group v-model="form.sex">
            <el-radio label=1 >男&nbsp;&nbsp;</el-radio>
            <el-radio label=0 >女</el-radio>
          </el-radio-group>
        </el-form-item>

      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="display = false">取 消</el-button>
    <el-button type="primary" @click="save">确 定</el-button>
  </span>
    </el-dialog>
  </div>



</template>

<script>
import '@/utils/request';

export default {
  name: "Login",
  data() {
    return {
      form: {
        userid:'',
        username:'',
        account:'',
        password:'',
        sex:'',
        email: '',
        phone: '',
        createDatetime:'',
        roleList:[],
      },
      checked: false,
      rules: {
        account: [
          { required: true, message: "请输入用户名", trigger: "blur" },
          { max: 10, message: "不能大于10个字符", trigger: "blur" },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { max: 10, message: "不能大于10个字符", trigger: "blur" },
        ],
      },
      display:false,
    };
  },
  mounted() {
    if(localStorage.getItem("news")){
      this.form=JSON.parse(localStorage.getItem("news"))
      this.checked=true
    }
  },
  methods: {
    login() {
      this.$axios.post(this.$httpUrl+'/user/login',this.form).then(res=>res.data).then(res=>{
        console.log(res);
        if(res.code==200){

          sessionStorage.setItem("CurUser",JSON.stringify(res.data))
          this.$router.push('/Main');

        }else{
          if(this.form.account!=""&&this.form.password){//判空
            this.$message({
              message: '密码或账号错误',
              type: 'error',
            });
          }else{
            this.$message({
              message: '密码或账号不能为空',
              type: 'error',
            });
          }

        }

      })
    },
    remenber(data){
      this.checked=data
      if(this.checked){
        localStorage.setItem("news",JSON.stringify(this.form))
      }else{
        localStorage.removeItem("news")
      }
    },
    register() {
      this.display =true;
      this.form.password = "";
      this.form.account = "";
    },
    save(){
      if (!this.form.username || !this.form.account || !this.form.password || !this.form.sex || !this.form.email || !this.form.phone) {
        this.$message.error('请填写完整信息');
        return;
      }
      if (this.form.username.length > 10) {
        this.$message.error('密码不得超过10个字符');
        return;
      }
      if (this.form.account.length > 10) {
        this.$message.error('用户名不得超过10个字符');
        return;
      }
      if (!/^\d{11}$/.test(this.form.phone)) {
        this.$message.error('电话号码格式不正确');
        return;
      }
      if (!/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(this.form.email)) {
        this.$message.error('邮箱格式不正确');
        return;
      }
      this.$axios.post(this.$httpUrl+'/user/save',this.form).then(res=>res.data).then(res=>{
        console.log(res);
        if(res.code==200){


          this.$message({
            message: '添加成功',
            type: 'success',
          });
          this.display = false;

        }else{
          this.$message({
            message: '操作失败',
            type: 'error'
          });
        }

      })
      this.form.username = "";
      this.form.password = "";
      this.form.account = "";
      this.form.email = "";
      this.form.phone = "";
      this.form.sex = "";
    }
  },
};
</script>

<style scoped>

.loginbody {

  min-width: 99%;
  min-height: 76%;
  background-size: 100% 100%;
  background-position: center center;
  background-repeat: no-repeat;
  position: fixed;
  line-height: 100%;
  padding-top: 150px;
  background-image: url("@/assets/login.webp");
}

.logintext {
  margin-bottom: 20px;
  line-height: 50px;
  text-align: center;
  font-size: 30px;
  font-weight: bolder;
  color: white;
  text-shadow: 2px 2px 4px #000000;
}

.logindata {
  width: 400px;
  height: 300px;
  transform: translate(-50%);
  margin-left: 50%;
}

.tool {
  display: flex;
  justify-content: space-between;
  color: #606266;
}

.butt {
  margin-top: 10px;
  text-align: center;
}

.shou {
  cursor: pointer;
  color: #606266;
}

</style>


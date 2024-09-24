<template>
  <div>
    <div style="margin-left: 30px;margin-top: 0;margin-bottom: 0; display: flex;width: auto">
      <p style="font-size: 20px">班级管理</p>
      <div style="margin-top: 20px;margin-bottom: 0px;margin-left: auto;" >
        <el-date-picker
            v-model="createDatetime"
            align="right"
            type="date"
            placeholder="选择建班日期"
            :picker-options="pickerOptions"
            @change="loadPost"
            :clearable="false"
            style="width: 150px"

        >
        </el-date-picker>
        <el-input
            placeholder="班级号"
            suffix-icon="el-icon-search"
            v-model="classno" style="width: 100px;margin-left: 10px"
            @keyup.enter.native="loadPost"
        >
        </el-input>
        <el-select v-model="userid" style="width: 120px;margin-left: 10px" @change="loadPost" placeholder="班主任">
          <el-option
              v-for="item in TeacherData"
              :key="item.userid"
              :label="item.username"
              :value="item.userid">
          </el-option>
        </el-select>
        <el-button type="success" style="margin-left: 30px" @click="add">创建</el-button>
        <el-button type="primary" style="margin-left: 10px" @click="loadPost">查询</el-button>
        <el-button type="success" @click="resetParam">重置</el-button>
      </div>
    </div>
    <div style="margin-top:0px;margin-left:20px;width: 1000px;">
      <el-table :data="tableData" :show-header="false">

        <el-table-column  width="760">
          <template slot-scope="scope">
            <div>
              <!-- 第一行 -->
              <p>
                班级号：{{ scope.row.classno }}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                班级人数：{{ classNum[scope.row.classid] || '0' }}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                班主任：{{
                  TeacherData.find(user => user.userid === scope.row.userid) ?
                      TeacherData.find(user => user.userid === scope.row.userid).username :
                      '未指定'
                }}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;


              </p>
              <!-- 第二行 -->
              <p>
                建班日期：{{scope.row.createDatetime}} &nbsp;&nbsp;&nbsp;&nbsp;
                最近修改者:{{scope.row.updateUser}}&nbsp;&nbsp;&nbsp;&nbsp;
                最近修改日期{{scope.row.updateDatetime}}
              </p>
            </div>
          </template>
        </el-table-column>

        <el-table-column width="70px">
          <template slot-scope="scope">
            <el-link type="success" @click="update(scope.row)">修改</el-link>
          </template>
        </el-table-column>

        <el-table-column width="70px">
          <template slot-scope="scope">
            <el-popconfirm
                title="确定删除吗？"
                @confirm="remove(scope.row)"
            >
              <el-link type="danger" slot="reference">删除</el-link>
            </el-popconfirm>
          </template>
        </el-table-column>

        <el-table-column width="90px">
          <template slot-scope="scope">
            <el-link type="success" @click="ToClassUser(scope.row)">查看班级</el-link>
          </template>
        </el-table-column>


      </el-table>
      <div style="display: flex; justify-content: center;">
        <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page= "pageNum"
            :page-sizes="[4,5, 10, 20]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
        </el-pagination>
      </div>
    </div>

    <el-dialog
        title="提示"
        :visible.sync="DialogVisible"
        width="50%"
        style="margin-top: -88px"
        center>
      <el-form ref="form" :model="form" label-width="80px" style="display: grid;place-items: center;" >

        <el-form-item label="班级编号" prop="classno">
          <el-col :span="35">
            <el-input  v-model="form.classno" style="width: 400px"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="班主任" prop="roleList">
          <el-select v-model="form.userid" filterable placeholder="请选择班主任">
            <el-option v-for="teacher in TeacherData"
                       :label="teacher.username"
                       :value="teacher.userid"
                       :key="teacher.userid">
              {{ teacher.username }}
            </el-option>
          </el-select>
        </el-form-item>



      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="resetParam">取 消</el-button>
    <el-button type="primary" @click="save">确 定</el-button>
  </span>
    </el-dialog>
  </div>




</template>

<script>

export default {
  name: "Class_Manage",
  data() {
    return{
      Curuser:JSON.parse(sessionStorage.getItem('CurUser')),//当前用户类
      DialogVisible:false,//提示窗口是否显示
      tableData: [],
      TeacherData:[],//班主任
      pageSize:4,
      pageNum:1,
      total:0,
      classNum: {},//班级人数
      classno: '',
      userid:'',
      createDatetime:'',

      form:{
        classid:'',
        classno:'',
        stunum: '',
        userid:'',//班主任用户主键
        createDatetime:'',
        updateDatetime:'',
        updateUser:'',
      },

      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
        shortcuts: [{
          text: '今天',
          onClick(picker) {
            picker.$emit('pick', new Date());
          }
        }, {
          text: '昨天',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() - 3600 * 1000 * 24);
            picker.$emit('pick', date);
          }
        }, {
          text: '一周前',
          onClick(picker) {
            const date = new Date();
            date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', date);
          }
        }]
      },
    }
  },
  created() {
    this.loadData()
  },
  methods:{
    async loadData() {
      try {
        const postData = this.loadPost();
        const userData = this.getUser();

        await Promise.all([postData, userData]);

        console.log(this.tableData);

        for (const row of this.tableData) {
          await this.getStuNum(row.classid);
        }
      } catch (error) {
        console.error("数据加载失败", error);
      }
    },
    loadPost() {
      return this.$axios.post(this.$httpUrl+'/class/listPage', {
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param:{
          classno:this.classno,
          userid: this.userid,
          createDatetime:this.createDatetime.toString(),
        }
      }).then(res => {
        console.log(res);
        if (res.data.code == 200) {
          this.tableData = res.data.data;
          console.log(this.tableData)
          this.total = res.data.total;
        } else {
          throw new Error("获取数据失败");
        }
      });
    },
    async getUser() {
      try {
        const res = await this.$axios.get(this.$httpUrl+'/user/listByRoleid?roleid=3');
        console.log(res);
        if (res.data.code == 200) {
          this.TeacherData = res.data.data;
        } else {
          throw new Error("获取数据失败");
        }
      } catch (error) {
        console.error("获取用户数据失败", error);
        throw error;
      }
    },
    async getStuNum(classid){//获取班级人数
      try {
        const res = await this.$axios.get(this.$httpUrl+'/user/getstunum?classid='+classid);
        console.log(res);
        if (res.data.code == 200) {
          this.$set(this.classNum, classid, res.data.data);
        } else {
          throw new Error("获取数据失败");
        }
      } catch (error) {
        console.error("获取班级人数失败", error);
        throw error;
      }
    }
    ,
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.pageSize=val;
      this.loadPost();
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.pageNum=val;
      this.loadPost();
    },

    save() {
      this.form.updateUser = this.Curuser.username;

      console.log(this.form)
      console.log(this.tableData)
      if (this.form.classid) {
        this.doUpdate();
      } else {
        this.doAdd();
      }
    },
    update(user){
      this.form = user;
      this.form.updateUser = this.Curuser.username;

      this.DialogVisible=true;

    },
    doUpdate(){


      this.$axios.post(this.$httpUrl+'/class/update',this.form).then(res=>res.data).then(res=>{
        console.log(res);

        if(res.code==200){

          this.$message({
            message: '修改成功',
            type: 'success',
          });
          this.loadPost()
          this.DialogVisible=false;

        }else{
          this.$message({
            message: '操作失败',
            type: 'error'
          });
        }
      })
      this.resetParam();



    },
    add(){
      this.resetParam();
      this.DialogVisible=true;
    },
    doAdd(){
      console.log(this.form)
      this.$axios.post(this.$httpUrl+'/class/save', this.form).then(res=>res.data).then(res=>{
        console.log(res);
        if(res.code==200){

          this.$message({
            message: '修改成功',
            type: 'success',
          });
          this.loadPost();
          this.DialogVisible=false;

        }else{
          this.$message({
            message: '操作失败',
            type: 'error'
          });
        }
      })
      this.resetParam();
    },
    remove(class1){
      this.$axios.get(this.$httpUrl+`/class/remove?classid=${class1.classid}`).then(res=>res.data).then(res=>{
        console.log(res);
        if(res.code==200){

          this.$message({
            message: '删除成功',
            type: 'success',
          });
          this.loadPost()

        }else{
          this.$message({
            message: '操作失败',
            type: 'error'
          });
        }
      })
    },
    resetParam(){//重置搜索栏以及表单信息
      this.classno= '';
      this.userid= '';
      this.createDatetime='';
      this.form = {
        classid: '',
        classno: '',
        stunum: '',
        userid: '',//班主任用户主键
        createDatetime: '',
        updateDatetime: '',
        updateUser: '',
      };
      this.loadPost();
    },
    ToClassUser(class1){
       sessionStorage.setItem("class",JSON.stringify(class1));
      this.$router.push("Class_UserManage");
    }

  },
  computed: {

    // beforeMount() {
    //   this.loadPost();
    //
    // },

  },

}
</script>

<style scoped>

</style>
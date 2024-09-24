<template>
  <div>
    <div style="margin-left: 30px;margin-top: 0;margin-bottom: 0; display: flex;width: auto">
      <p style="font-size: 20px">课程管理</p>
      <div style="margin-top: 20px;margin-bottom: 0px;margin-left: auto;" >
        <el-date-picker
              v-model="opentime"
              align="right"
              type="date"
              placeholder="选择开创日期"
              :picker-options="pickerOptions"
              @change="loadPost"
              :clearable="false"
              style="width: 150px"
        >
        </el-date-picker>
        <el-input
            placeholder="课程名"
            suffix-icon="el-icon-search"
            v-model="coursename" style="width: 100px;margin-left: 10px"
            @keyup.enter.native="loadPost"
            @change="loadPost"
        >
        </el-input>
        <el-select v-model="userid" style="width: 120px;margin-left: 10px" @change="loadPost" placeholder="教师">
          <el-option
              v-for="item in TeacherData"
              :key="item.userid"
              :label="item.username"
              :value="item.userid">
          </el-option>
        </el-select>

        <el-button type="success" style="margin-left: 30px" @click="add">添加</el-button>
        <el-button type="primary" style="margin-left: 10px" @click="loadPost">查询</el-button>
        <el-button type="success" @click="resetParam">重置</el-button>
      </div>
    </div>

      <div style="margin-top:0px;margin-left:20px;width: 1000px;">
        <el-table :data="tableData" :show-header="false">

          <el-table-column  width="880px">
            <template slot-scope="scope">
             <div>
              <!-- 第一行 -->
                <p>
                  课程名：{{ scope.row.coursename }}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                  代课教师
                  <!-- 遍历 roleList 数组 -->
                  <el-dropdown trigger="click" placement="bottom-start">
                    <i class="el-icon-arrow-down el-icon--right"></i>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item v-for="user in scope.row.userList" :key="user.userid">
                        {{ user.username }}
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>
                </p>
                <!-- 第二行 -->
                <p>
                 课程简介：{{scope.row.coursedetail}} &nbsp;&nbsp;&nbsp;&nbsp;
                 开创日期：{{scope.row.opentime}} &nbsp;&nbsp;&nbsp;&nbsp;

                </p>
            </div>
            </template>
          </el-table-column>

          <el-table-column width="70px">
            <template slot-scope="scope">
              <el-link type="success" @click="update(scope.row)">修改</el-link>
            </template>
          </el-table-column>
          <el-table-column width="50px">
            <template slot-scope="scope">
              <el-popconfirm
                  title="确定删除吗？"
                  @confirm="remove(scope.row)"
              >
                <el-link type="danger" slot="reference">删除</el-link>
              </el-popconfirm>
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

        <el-form-item label="课程名字" prop="courseid">
          <el-col :span="35">
            <el-input  v-model="form.coursename" style="width: 400px"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="课程简介" prop="account">
          <el-col :span="35">
            <el-input v-model="form.coursedetail":rows="10" style="width: 400px;"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="代课教师" prop="account">
          <el-select v-model="selectedTeacherIds" multiple filterable  placeholder="请选择代课教师" @change="changeSelectedTeacherIds()">
          <el-option
              v-for="teacher in TeacherData"
              :key="teacher.userid"
              :label="teacher.username"
              :value="teacher.userid">
          </el-option>
        </el-select>
        </el-form-item>


      </el-form>
      <span slot="footer" class="dialog-footer">
    <el-button @click="resetForm">取 消</el-button>
    <el-button type="primary" @click="save">确 定</el-button>
  </span>
    </el-dialog>
  </div>




</template>

<script>

export default {
  name: "Notice_Manage",
  data() {
    return{
      TeacherData:[],//教师
      selectedTeacherIds:[],//待添加/修改的教师用户的id集合
      Curuser:JSON.parse(sessionStorage.getItem('CurUser')),//当前用户类
      DialogVisible:false,//提示窗口是否显示
      tableData: [],
      topTableData: [],
      pageSize:4,
      pageNum:1,
      total:0,
      coursename:'',
      userid:'',
      opentime:'',
      index:'',

      form:{
        courseid:'',
        coursename:'',
        coursedetail:'',
        userList:[]
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
    this.loadPost();
    this.getTeacherUser()
  },
  methods:{
    loadPost() {
      this.$axios.post(this.$httpUrl+'/course/listPage', {
        pageSize: this.pageSize,
        pageNum: this.pageNum,
        param:{
          coursename:this.coursename,
          userid: this.userid,
          opentime:this.opentime.toString(),
        }

      }).then(res => res.data).then(res => {
        console.log(res);
        if (res.code == 200) {

          this.tableData = res.data;
          this.total = res.total;

        } else {
          alert("获取数据失败");
        }

      })
    },
    async getTeacherUser() {//获取到教师用户
      try {
        const res = await this.$axios.get(this.$httpUrl+'/user/listByRoleid?roleid=2');
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
      if (this.form.courseid) {
        this.doUpdate();
      } else {
        this.doAdd();
      }
    },
    update(teacher){
      this.form = teacher;
      this.form.updateUser = this.Curuser.username;

      this.updateSelectedRoleIds(this.form.userList);//将form.userList中每一项的userid存入

      this.loadPost();

      this.DialogVisible=true;

    },
    doUpdate(){

      this.$axios.post(this.$httpUrl+'/course/update',this.form).then(res=>res.data).then(res=>{
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
      this.resetForm();

    },
    add(){

      this.resetForm();
      this.DialogVisible=true;
    },
    doAdd(){
      console.log(this.form)
      this.$axios.post(this.$httpUrl+'/course/save', this.form).then(res=>res.data).then(res=>{
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
      this.resetForm();
    },
    remove(course){
      this.$axios.get(this.$httpUrl+`/course/remove?courseid=${course.courseid}`).then(res=>res.data).then(res=>{
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
    resetForm(){//重置表单信息
      this.DialogVisible = false;

      this.form = {
        courseid:'',
        coursename:'',
        coursedetail:'',
        userList:[]
      };
      this.selectedTeacherIds = [];

      this.loadPost();

    },
    resetParam(){//重置搜索栏

      this.form = {
        courseid:'',
        coursename:'',
        coursedetail:'',
        userList:[]
      };
      this.userid = '';
      this.opentime = '';
      this.coursename = '';
      this.loadPost();
    },
    updateSelectedRoleIds(TeacherIds) {//将form.userList中的userid都传给selectedTeacherIds数组
      this.selectedTeacherIds = TeacherIds.map(user => user.userid);

      this.form.userList = [];

      // 遍历roleid数组
      this.selectedTeacherIds.forEach(userId => {
        // 在roleListOptions中查找匹配的roleid
        const user = this.TeacherData.find(option => option.roleid === userId);
        // 如果找到了匹配的role对象，则将其添加到form.roleList中
        if (user) {
          this.form.userList.push(user);
        }
      });
    },

    changeSelectedTeacherIds() {//将selectedTeacherIds中userid对应的user存入form.userList

      this.form.userList = this.selectedTeacherIds.map(userId => {
        return this.TeacherData.find(user => user.userid === userId);
      })

    },
  },
  computed: {
    switchValue: {
      get() {
        return this.form.sex == 1; // 将1转换为true，0转换为false
      },
      set(value) {
        this.form.sex = value ? 1 : 0; // 将true转换为1，false转换为0
      }
    },
    beforeMount() {
      this.loadPost();
      this.loadPosttopTableData();
    },
  },

}
</script>

<style scoped>

</style>
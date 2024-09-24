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

        <el-table-column width="90px">
          <template slot-scope="scope">
            <el-link type="success" @click="ToStudent(scope.row)">查看学生</el-link>
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
    ToStudent(Course) {
      sessionStorage.setItem("Course",JSON.stringify(Course))
      this.$router.push("Student_GradeByCourseManage");

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
    resetParam(){

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